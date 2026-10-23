package com.example.rpg.repository;

import com.example.rpg.exception.DataLoadException;
import com.example.rpg.model.CharacterRecord;
import com.example.rpg.service.CharacterFactory;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvCharacterRepositoryTest {

    private static final String HEADER = "type,name,level,hp,attack,defense,speed,mana,weapon_bonus,team\n";
    private static final String VALID_ROWS = """
            WARRIOR,Aldric,3,120,18,15,8,0,4,HEROES
            MONSTER,Goblin,2,60,10,5,9,0,0,MONSTERS
            """;

    private final CharacterFactory factory = new CharacterFactory();

    private CsvCharacterRepository load(String content) throws DataLoadException {
        return new CsvCharacterRepository(new StringReader(content), factory);
    }

    @Test
    void loadsValidRows() throws DataLoadException {
        CsvCharacterRepository repository = load(HEADER + VALID_ROWS);

        java.util.List<CharacterRecord> records = repository.findAll();

        assertEquals(2, records.size());
        assertEquals("WARRIOR", records.get(0).type());
        assertEquals("Aldric", records.get(0).stats().name());
        assertEquals(4, records.get(0).stats().weaponBonus());
        assertEquals("MONSTERS", records.get(1).teamName());
    }

    @Test
    void wrongColumnCountReportsTheLineNumber() {
        String content = HEADER + VALID_ROWS + "MAGE,Lyra,3,80\n";

        DataLoadException ex = assertThrows(DataLoadException.class, () -> load(content));

        assertEquals(true, ex.getMessage().contains("line 4"));
    }

    @Test
    void unknownCharacterTypeIsRejected() {
        String content = HEADER + "DRAGON,Smaug,9,500,50,40,20,0,0,MONSTERS\n" + VALID_ROWS;

        assertThrows(DataLoadException.class, () -> load(content));
    }

    @Test
    void nonNumericStatIsRejected() {
        String content = HEADER + "WARRIOR,Aldric,three,120,18,15,8,0,4,HEROES\n" + VALID_ROWS;

        assertThrows(DataLoadException.class, () -> load(content));
    }

    @Test
    void fileWithoutMonstersIsRejected() {
        String content = HEADER + "WARRIOR,Aldric,3,120,18,15,8,0,4,HEROES\n";

        assertThrows(DataLoadException.class, () -> load(content));
    }
}
