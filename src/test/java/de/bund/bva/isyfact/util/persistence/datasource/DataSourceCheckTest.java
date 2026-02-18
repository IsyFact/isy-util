package de.bund.bva.isyfact.util.persistence.datasource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DataSourceCheckTest {

    private DataSourceCheck dataSourceCheck;
    private DataSource mockDataSource;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);

        mockDataSource = Mockito.mock(DataSource.class);
        mockResultSet = Mockito.mock(ResultSet.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        dataSourceCheck = new DataSourceCheck();
    }

    @Test
    void testCheckSchemaVersionReturnsTrueWhenVersionMatches() throws SQLException {
        String expected = "47.11";
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(anyInt())).thenReturn(expected);

        boolean result = dataSourceCheck.checkSchemaVersion(mockDataSource, expected);
        assertTrue(result);
    }

    @Test
    void testCheckSchemaVersionReturnsFalseWhenVersionMismatch() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(anyInt())).thenReturn("47.11");

        boolean result = dataSourceCheck.checkSchemaVersion(mockDataSource, "0");
        assertFalse(result);
    }

    @Test
    void testCheckSchemaVersionThrowsRuntimeExceptionOnSQLException() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(anyInt())).thenThrow(new SQLException("Testing an Exception"));

        assertThrows(RuntimeException.class, () -> dataSourceCheck.checkSchemaVersion(mockDataSource, "0"));
    }
}
