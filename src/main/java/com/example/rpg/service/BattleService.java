package com.example.rpg.service;

import com.example.rpg.exception.CharacterDeadException;
import com.example.rpg.exception.InvalidActionException;
import com.example.rpg.model.Battle;
import com.example.rpg.model.BattleOutcome;
import com.example.rpg.model.Character;
import com.example.rpg.model.Healable;
import com.example.rpg.model.Team;

import java.util.Objects;
import java.util.Optional;

/**
 * Runs a battle turn by turn. This is the business logic core: it decides what each fighter does
 * and applies the rules. It does not print anything; the log is read by the UI afterwards.
 */
public class BattleService {

    /** Healers step in when an ally drops below this fraction of max HP. */
    static final double HEAL_THRESHOLD = 0.4;

    private final DamageCalculator damageCalculator;
    private final TurnOrderService turnOrderService;

    public BattleService(DamageCalculator damageCalculator, TurnOrderService turnOrderService) {
        this.damageCalculator = Objects.requireNonNull(damageCalculator);
        this.turnOrderService = Objects.requireNonNull(turnOrderService);
    }

    public BattleOutcome run(Battle battle) {
        Objects.requireNonNull(battle, "battle must not be null");
        while (!battle.isFinished()) {
            battle.nextTurn();
            battle.log("--- Turn " + battle.getTurn() + " ---");
            for (Character actor : turnOrderService.computeTurnOrder(battle.getHeroes(), battle.getMonsters())) {
                if (battle.isFinished()) {
                    break;
                }
                if (actor.isAlive()) {
                    takeTurn(battle, actor);
                }
            }
        }
        return battle.getOutcome();
    }

    private void takeTurn(Battle battle, Character actor) {
        if (actor instanceof Healable healer && healer.canHeal()) {
            Optional<Character> injured = battle.teamOf(actor).findMostInjured(HEAL_THRESHOLD);
            if (injured.isPresent()) {
                Character ally = injured.get();
                healer.healAlly(ally);
                battle.log(String.format("%s heals %s (HP %d/%d)",
                        actor.getName(), ally.getName(), ally.getCurrentHp(), ally.getMaxHp()));
                return;
            }
        }

        battle.opposingTeamOf(actor).findWeakestAlive()
                .ifPresent(target -> attack(battle, actor, target));
    }

    /** Resolves one attack. Public so the rules can be tested directly. */
    public void attack(Battle battle, Character attacker, Character target) {
        if (!attacker.isAlive()) {
            throw new CharacterDeadException(attacker.getName());
        }
        if (!target.isAlive()) {
            throw new CharacterDeadException(target.getName());
        }
        if (battle.teamOf(attacker) == battle.teamOf(target)) {
            throw new InvalidActionException(attacker.getName() + " cannot attack an ally: " + target.getName());
        }

        DamageResult result = damageCalculator.calculate(attacker, target);
        target.takeDamage(result.amount());
        battle.log(String.format("%s attacks %s for %d damage%s (HP %d/%d)",
                attacker.getName(), target.getName(), result.amount(),
                result.critical() ? " CRITICAL" : "",
                target.getCurrentHp(), target.getMaxHp()));

        if (!target.isAlive()) {
            battle.log(target.getName() + " is defeated!");
        }
    }
}
