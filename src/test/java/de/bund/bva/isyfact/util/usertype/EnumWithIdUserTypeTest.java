package de.bund.bva.isyfact.util.usertype;

import static org.junit.Assert.*;

import java.util.Properties;

import jakarta.persistence.PersistenceException;

import org.junit.Before;
import org.junit.Test;

import de.bund.bva.isyfact.util.persistence.usertype.EnumWithIdUserType;

public class EnumWithIdUserTypeTest {

    private EnumWithIdUserType userType;

    @Before
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

    @Test(expected = PersistenceException.class)
    public void testSetEnumClassZweiIdGetter() {
        userType.setEnumClass(DuplicateIdGetterEnum.class);
    }

    @Test(expected = PersistenceException.class)
    public void testSetEnumClassKeinIdGetter() {
        userType.setEnumClass(Vorgangsstatus.class);
    }

    @Test(expected = PersistenceException.class)
    public void testSetEnumClassDuplictaeKey() {
        userType.setEnumClass(DuplicatePersistentValueEnum.class);
    }

    @Test(expected = PersistenceException.class)
    public void testConvertStringToInstanceFalscheId() {
        userType.setEnumClass(Vermerkstyp.class);
        userType.convertStringToInstance("C");
    }

    @Test(expected = PersistenceException.class)
    public void testConvertInstanceToStringFalscheId() {
        userType.setEnumClass(WrongIdEnum.class);
        userType.convertInstanceToString(WrongIdEnum.A);
    }

    @Test(expected = PersistenceException.class)
    public void testSetParameterValuesEnumClassNotSet() {
        Properties prop = new Properties();
        userType.setParameterValues(prop);
    }

    @Test(expected = PersistenceException.class)
    public void testSetParameterValuesEnumClassNotEnum() {
        Properties prop = new Properties();
        prop.setProperty("enumClass", Object.class.getName());
        userType.setParameterValues(prop);
    }

    @Test(expected = PersistenceException.class)
    public void testSetParameterValuesEnumClassNotFound() {
        Properties prop = new Properties();
        prop.setProperty("enumClass", "ObjectA");
        userType.setParameterValues(prop);
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