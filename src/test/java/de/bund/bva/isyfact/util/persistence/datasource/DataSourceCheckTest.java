package de.bund.bva.isyfact.util.persistence.datasource;

import junit.framework.TestCase;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


public class DataSourceCheckTest extends TestCase {
    DataSource dataSource = Mockito.mock(DataSource.class);
    Connection connection = Mockito.mock(Connection.class);
    PreparedStatement statement = Mockito.mock(PreparedStatement.class);
    ResultSet resultSet = Mockito.mock(ResultSet.class);


    public void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
    }

    public void testGetSchemaVersion() throws SQLException {
        String expected = "47.11";
        when(resultSet.getString(anyInt())).thenReturn(expected);

        DataSourceCheck check = new DataSourceCheck("SELECT version_nummer FROM m_schema_version WHERE version_nummer = ? AND status = 'gueltig'");
        String version = check.getSchemaVersion(dataSource, "");
        assertEquals(expected, version);
    }

    public void testCheckSchemaVersionCriticalDataSourceTrue() throws SQLException {
        String expected = "47.11";
        when(resultSet.getString(anyInt())).thenReturn(expected);

        DataSourceCheck check = new DataSourceCheck("SELECT version_nummer FROM m_schema_version WHERE version_nummer = ? AND status = 'gueltig'");
        boolean ok = check.checkSchemaVersionCriticalDataSource(dataSource, expected);
        assertTrue(ok);
    }

   public void testCheckSchemaVersionCriticalDataSourceFalse() throws SQLException {
        String expected = "47.11";
        when(resultSet.getString(anyInt())).thenReturn(expected);

        DataSourceCheck check = new DataSourceCheck("SELECT version_nummer FROM m_schema_version WHERE version_nummer = ? AND status = 'gueltig'");
        boolean ok = check.checkSchemaVersionCriticalDataSource(dataSource, "0");
        assertFalse(ok);
    }


    public void testCheckSchemaVersionNonCriticalDataSourceTrue() throws SQLException {
        String expected = "47.11";
        when(resultSet.getString(anyInt())).thenReturn(expected);

        DataSourceCheck check = new DataSourceCheck("SELECT version_nummer FROM m_schema_version WHERE version_nummer = ? AND status = 'gueltig'");
        boolean ok = check.checkSchemaVersionNonCriticalDataSource(dataSource, expected);
        assertTrue(ok);
    }

    public void testCheckSchemaVersionNonCriticalDataSourceFalse() throws SQLException {
        String expected = "47.11";
        when(resultSet.getString(anyInt())).thenReturn(expected);

        DataSourceCheck check = new DataSourceCheck("SELECT version_nummer FROM m_schema_version WHERE version_nummer = ? AND status = 'gueltig'");
        boolean ok = check.checkSchemaVersionNonCriticalDataSource(dataSource, "0");
        assertFalse(ok);
    }
}