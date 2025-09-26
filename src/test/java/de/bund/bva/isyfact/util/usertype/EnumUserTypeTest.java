package de.bund.bva.isyfact.util.usertype;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import jakarta.persistence.PersistenceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.bund.bva.isyfact.util.persistence.usertype.EnumUserType;

public class EnumUserTypeTest {

    private EnumUserType userType;

    @BeforeEach
    public void setUp() {
        userType = new EnumUserType();
    }

    @Test
    public void testSetParameterValues() {
        Properties prop = new Properties();
        prop.setProperty("enumClass", Vorgangsstatus.class.getName());
        userType.setParameterValues(prop);
        assertEquals("B", userType.convertInstanceToString(Vorgangsstatus.IN_BEARBEITUNG));
        assertEquals(Vorgangsstatus.IN_BEARBEITUNG, userType.convertStringToInstance("B"));
    }

    @Test
    public void testSetParameterValuesNull() {
        assertThrows(NullPointerException.class, () ->
            userType.setParameterValues(null));
    }

    @Test
    public void testSetParameterValuesNoEnumClassSet() {
        assertThrows(PersistenceException.class, () -> {
            Properties prop = new Properties();
            userType.setParameterValues(prop);
        });
    }

    @Test
    public void testSetParameterValuesKeineEnumClass() {
        assertThrows(PersistenceException.class, () -> {
            Properties prop = new Properties();
            prop.setProperty("enumClass", Object.class.getName());
            userType.setParameterValues(prop);
        });
    }

    @Test
    public void testSetParameterValuesKeineKlasse() {
        assertThrows(PersistenceException.class, () -> {
            Properties prop = new Properties();
            prop.setProperty("enumClass", "ObjectA");
            userType.setParameterValues(prop);
        });
    }

    @Test
    public void testConvertStringToInstance() {
        userType.setEnumClass(Vorgangsstatus.class);
        assertEquals("B", userType.convertInstanceToString(Vorgangsstatus.IN_BEARBEITUNG));
        assertEquals(Vorgangsstatus.class, userType.returnedClass());
    }

    @Test
    public void testConvertInstanceToString() {
        userType.setEnumClass(Vorgangsstatus.class);
        assertEquals(Vorgangsstatus.IN_BEARBEITUNG, userType.convertStringToInstance("B"));
    }

    @Test
    public void testKeineAnnotationAnEnumKonstanten() {
        assertThrows(PersistenceException.class, () ->
            userType.setEnumClass(Vermerkstyp.class));
    }

    @Test
    public void testDuplicatePersistentValue() {
        assertThrows(PersistenceException.class, () ->
            userType.setEnumClass(DuplicatePersistentValueEnum.class));
    }

    @Test
    public void testConvertStringToInstanceKeyNotExists() {
        assertThrows(PersistenceException.class, () -> {
            userType.setEnumClass(Vorgangsstatus.class);
            userType.convertStringToInstance("");
        });
    }

    @Test
    public void testConvertInstanceToStringObjectNotExists() {
        assertThrows(PersistenceException.class, () -> {
            userType.setEnumClass(Vorgangsstatus.class);
            userType.convertInstanceToString(Vermerkstyp.NACHRICHT_EMPFANGEN);
        });
    }

    @Test
    public void testNullSafeGet() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getString(0)).thenReturn("E");
        when(rs.wasNull()).thenReturn(false);

        userType.setEnumClass(Vorgangsstatus.class);
        Object obj = userType.nullSafeGet(rs, 0, null, null);
        assertEquals(Vorgangsstatus.ERLEDIGT, obj);
    }

    @Test
    public void testNullSafeGetNull() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getString(0)).thenReturn("NONE");
        when(rs.wasNull()).thenReturn(true);

        userType.setEnumClass(Vorgangsstatus.class);
        Object obj = userType.nullSafeGet(rs, 0, null, null);
        assertNull(obj);
    }

    @Test
    public void nullSafeSetNull() throws SQLException {
        PreparedStatement st = mock(PreparedStatement.class);
        userType.setEnumClass(Vorgangsstatus.class);
        userType.nullSafeSet(st, null, 0, null);
        verify(st, times(1)).setNull(0, userType.getSqlType());
    }

    @Test
    public void nullSafeSetVorgangsstatus() throws SQLException {
        PreparedStatement st = mock(PreparedStatement.class);
        userType.setEnumClass(Vorgangsstatus.class);
        userType.nullSafeSet(st, Vorgangsstatus.ERLEDIGT, 0, null);
        verify(st, times(1)).setString(0, "E");
    }
}
