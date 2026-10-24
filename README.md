# RPG Battle Simulator

A console turn-based battle simulator in Java 25. Heroes (Warrior, Mage, Archer) fight monsters.
The characters are loaded from a CSV file at startup. After a victory, surviving heroes gain XP and level up.

## How to run

Requirements: JDK 25+, Maven 3.9+.

```bash
mvn test                                   # run the JUnit tests
mvn package                                # build target/rpg-battle-1.0-SNAPSHOT.jar
java -cp target/classes com.example.rpg.Application                    # bundled data
java -cp target/classes com.example.rpg.Application path/to/other.csv  # custom data
```

Menu: `1` lists the characters, `2` starts a battle, `0` quits.

## Main features

- Turn-based combat in speed order (ties broken by name).
- Damage = attack − defense (minimum 1), with critical hits for archers and occasional critical hits for everyone.
- Warrior "Rage": attack ×1.5 below 30% HP.
- Mage heals the most injured ally below 40% HP. Each heal costs 20 mana.
- Victory: surviving heroes receive XP and level up. Each class has its own level-up growth.
- Draw after 50 turns.

## Main classes

| Package | Class | Role |
|---|---|---|
| `model` | `Character` | Abstract base of all fighters: HP, stats, damage, XP, level-up |
| `model` | `Warrior`, `Mage`, `Archer`, `Monster` | Concrete fighters with their own rules |
| `model` | `Healable` | Interface for characters that can heal allies |
| `model` | `Team`, `Battle`, `BattleOutcome`, `Weapon` | Combat state and value objects |
| `model` | `CharacterStats`, `CharacterRecord` | Immutable records for the raw data |
| `service` | `BattleService` | Runs the turns and applies the rules (business logic core) |
| `service` | `DamageCalculator`, `DamageFormula`, `StandardDamageFormula` | Damage computation, pluggable formula |
| `service` | `TurnOrderService` | Decides who acts when |
| `service` | `ExperienceService` | Post-battle rewards |
| `service` | `CharacterFactory`, `RosterBuilder` | Create objects from data |
| `repository` | `CharacterRepository`, `CsvCharacterRepository` | Loads and validates `characters.csv` |
| `exception` | `RpgException` and subclasses | Game-rule errors |
| `ui` | `ConsoleMenu`, `BattleView` | Menu loop and console output |
| `utils` | `CsvParser`, `RandomSource`, `JavaRandomSource` | Helpers, injectable randomness |

## Technical decisions

1. **Abstract class for `Character`, interface for `Healable`.** `Character` has state (HP, stats, XP) and shared rules, so it must be a class. Healing applies only to some characters. Putting `heal()` on `Character` would force warriors and archers to expose a method they cannot use, so healing is an interface (Interface Segregation).
2. **Factory with a lookup `Map`, not a `switch`.** Adding a character type means adding one line to `CharacterFactory.CREATORS`, without touching existing branches (Open/Closed).
3. **Randomness is injected (`RandomSource`).** Tests pass a fixed value, so critical hits and outcomes are deterministic. Without this, the combat tests would be flaky.
4. **Data is validated once, at load time.** `CsvCharacterRepository` builds every row as an object, so a bad row fails at startup with its line number, not in the middle of a battle. Fresh characters are built from the stored records for each battle.
5. **Checked vs unchecked exceptions.** `DataLoadException` is checked because the caller must handle a missing or broken file. Game-rule errors extend `RpgException`, which is unchecked, and the menu catches them in one place.

## Inheritance

- `Character` → `Warrior`, `Mage`, `Archer`, `Monster`. Each subclass is a real kind of fighter that shares HP, stats and XP with the others.
- `RpgException` → `InvalidActionException`, `CharacterDeadException`, `InsufficientManaException`.

## Polymorphism

- `BattleService.run` and `TurnOrderService` manipulate `Character` references. The behaviour depends on the real class: `getAttackPower()` (the Warrior adds Rage), `getCriticalChance()` (the Archer has a higher chance), `onLevelUp()` (each class grows differently), `getTypeLabel()`.
- `takeTurn` uses `instanceof Healable`: a mage heals, others attack.

## Abstract class and interface

- `Character` is abstract because `getTypeLabel()` and `onLevelUp()` have no sensible default. Each class must define them.
- `Healable` is an interface because the healing capability is shared by unrelated parts of the design and is not an identity.

## Custom exceptions

- `InvalidActionException`: attacking an ally.
- `CharacterDeadException`: a defeated character tries to act or is targeted.
- `InsufficientManaException`: a heal without enough mana. It carries the required and available values.
- `DataLoadException` (checked): missing file, wrong column count, unknown type, non-numeric stat, missing team.

## Data loading

At startup, `Application` opens `src/main/resources/characters.csv`, or the file given as argument. `CsvCharacterRepository` reads it line by line and validates every row. The game never creates characters by hand in code.

CSV columns: `type,name,level,hp,attack,defense,speed,mana,weapon_bonus,team`. `type` is `WARRIOR`, `MAGE`, `ARCHER` or `MONSTER`. `team` is `HEROES` or `MONSTERS`.

## SOLID

- **SRP**: the model holds state, `BattleService` holds rules, `BattleView` holds output, `CsvCharacterRepository` holds parsing.
- **OCP**: new character types go into the factory. New damage rules go into a new `DamageFormula`.
- **LSP**: any `Character` subclass works wherever a `Character` is expected. `BattleService` does not check the type.
- **ISP**: `Healable` is separate from `Character`.
- **DIP**: `ConsoleMenu` and `BattleService` depend on abstractions (`CharacterRepository`, `RandomSource`, `DamageFormula`), and `Application` wires the concrete classes.

## Tests

```bash
mvn test
```

33 JUnit 5 tests cover:
- `CharacterTest`: HP limits, healing cap, XP and level-up, Rage threshold, critical chance.
- `MageTest`: healing, insufficient mana, dead ally.
- `DamageCalculatorTest`: minimum damage, critical hits, rolls above and below the chance.
- `TurnOrderServiceTest`: speed order, name tie-break, dead characters excluded.
- `BattleServiceTest`: victory, draw at the turn limit, attacking an ally, dead attacker.
- `ExperienceServiceTest`: XP award, level-up, dead heroes get nothing.
- `CsvCharacterRepositoryTest`: valid data, wrong column count, unknown type, non-numeric stat, missing team.
