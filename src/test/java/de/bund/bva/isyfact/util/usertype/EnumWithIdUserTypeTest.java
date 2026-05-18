package de.bund.bva.isyfact.util.usertype;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import jakarta.persistence.PersistenceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.bund.bva.isyfact.util.persistence.usertype.EnumWithIdUserType;

public class EnumWithIdUserTypeTest {

    private EnumWithIdUserType userType;

    @BeforeEach
    public void setUp() {
        userType = new EnumWithIdUserType();
    }

    @Test
    public void testSetParameterValues() {
        final String sollId = Vermerkstyp.NACHRICHT_EMPFANGEN.getId();
        Properties prop = new Properties();
        prop.setProperty("enumClass", Vermerkstyp.class.getName());
        userType.setParameterValues(prop);
        assertEquals(sollId, userType.convertInstanceToString(Vermerkstyp.NACHRICHT_EMPFANGEN));
        assertEquals(Vermerkstyp.NACHRICHT_EMPFANGEN, userType.convertStringToInstance(sollId));
    }

    @Test
    public void testConvertStringToInstance() {
        final String sollId = Vermerkstyp.NACHRICHT_EMPFANGEN.getId();
        userType.setEnumClass(Vermerkstyp.class);
        assertEquals(sollId, userType.convertInstanceToString(Vermerkstyp.NACHRICHT_EMPFANGEN));
    }

    @Test
    public void testConvertInstanceToString() {
        final String sollId = Vermerkstyp.NACHRICHT_EMPFANGEN.getId();
        userType.setEnumClass(Vermerkstyp.class);
        assertEquals(Vermerkstyp.NACHRICHT_EMPFANGEN, userType.convertStringToInstance(sollId));
    }

    @Test
    public void testSetEnumClassZweiIdGetter() {
        assertThrows(PersistenceException.class, () ->
            userType.setEnumClass(DuplicateIdGetterEnum.class));
    }

    @Test
    public void testSetEnumClassKeinIdGetter() {
        assertThrows(PersistenceException.class, () ->
            userType.setEnumClass(Vorgangsstatus.class));
    }

    @Test
    public void testSetEnumClassDuplictaeKey() {
        assertThrows(PersistenceException.class, () ->
            userType.setEnumClass(DuplicatePersistentValueEnum.class));
    }

    @Test
    public void testConvertStringToInstanceFalscheId() {
        assertThrows(PersistenceException.class, () -> {
            userType.setEnumClass(Vermerkstyp.class);
            userType.convertStringToInstance("C");
        });
    }

    @Test
    public void testConvertInstanceToStringFalscheId() {
        assertThrows(PersistenceException.class, () -> {
            userType.setEnumClass(WrongIdEnum.class);
            userType.convertInstanceToString(WrongIdEnum.A);
        });
    }

    @Test
    public void testSetParameterValuesEnumClassNotSet() {
        assertThrows(PersistenceException.class, () -> {
            Properties prop = new Properties();
            userType.setParameterValues(prop);
        });
    }

    @Test
    public void testSetParameterValuesEnumClassNotEnum() {
        assertThrows(PersistenceException.class, () -> {
            Properties prop = new Properties();
            prop.setProperty("enumClass", Object.class.getName());
            userType.setParameterValues(prop);
        });
    }

    @Test
    public void testSetParameterValuesEnumClassNotFound() {
        assertThrows(PersistenceException.class, () -> {
            Properties prop = new Properties();
            prop.setProperty("enumClass", "ObjectA");
            userType.setParameterValues(prop);
        });
    }

    @Test
    public void testAbstractImmutableUserType() {
        assertTrue(userType.equals(Vermerkstyp.NACHRICHT_EMPFANGEN, Vermerkstyp.NACHRICHT_EMPFANGEN));
        assertEquals(Vermerkstyp.NACHRICHT_EMPFANGEN.hashCode(), userType.hashCode(Vermerkstyp.NACHRICHT_EMPFANGEN));
        assertEquals(Vermerkstyp.NACHRICHT_EMPFANGEN, userType.deepCopy(Vermerkstyp.NACHRICHT_EMPFANGEN));
        assertFalse(userType.isMutable());
        assertEquals(Vermerkstyp.NACHRICHT_EMPFANGEN, userType.disassemble(Vermerkstyp.NACHRICHT_EMPFANGEN));
        assertEquals(Vermerkstyp.NACHRICHT_EMPFANGEN, userType.assemble(Vermerkstyp.NACHRICHT_EMPFANGEN, null));
        assertEquals(Vermerkstyp.NACHRICHT_EMPFANGEN, userType.replace(Vermerkstyp.NACHRICHT_EMPFANGEN, null, null));
    }
}