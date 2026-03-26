package de.bund.bva.isyfact.util.persistence.datasource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.persistence.PersistenceException;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

class DataSourceCheckRunnerTest {

    private static final Logger LOG = (Logger) LoggerFactory.getLogger(DataSourceCheckRunner.class);
    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        listAppender = new ListAppender<>();
        listAppender.start();
        LOG.addAppender(listAppender);
    }

    @AfterEach
    void tearDown() {
        LOG.detachAppender(listAppender);
        listAppender.stop();
    }

    @Configuration
    static class TestConfig {

        @Bean
        public DataSource dataSource() throws SQLException {
            DataSource dataSource = Mockito.mock(DataSource.class);
            Connection connection = Mockito.mock(Connection.class);
            PreparedStatement statement = Mockito.mock(PreparedStatement.class);
            ResultSet resultSet = Mockito.mock(ResultSet.class);

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getString(anyInt())).thenReturn("1.2.3");

            return dataSource;
        }
    }

    @Nested
    @SpringBootTest(
        classes = { DataSourceCheckRunnerTest.TestConfig.class, DataSourceCheckRunner.class },
        properties = { "isy.util.datasource.schema-version=1.2.3" }
    )
    class RunDataSourceCheckRunnerWithValidVersion {

        @MockitoSpyBean
        DataSourceCheckRunner dataSourceCheckRunner;

        @Test
        void dbSchemaCheckRunSuccessfully() {
            assertThat(dataSourceCheckRunner).isNotNull();
            // Verify automatic run happened during startup
            verify(dataSourceCheckRunner, times(1)).run(any());

            // Clear previous logs and run again
            listAppender.list.clear();
            dataSourceCheckRunner.run(null);

            assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .contains("Schema-Version ist korrekt");
        }
    }

    @Nested
    @SpringBootTest(classes = { DataSourceCheckRunnerTest.TestConfig.class, DataSourceCheckRunner.class })
    class RunDataSourceCheckRunnerWithNullVersion {

        private DataSourceCheckRunner dataSourceCheckRunner;
        private DataSource mockDataSource;

        @BeforeEach
        void setUp() throws SQLException {
            mockDataSource = Mockito.mock(DataSource.class);
            Connection connection = Mockito.mock(Connection.class);
            PreparedStatement statement = Mockito.mock(PreparedStatement.class);
            ResultSet resultSet = Mockito.mock(ResultSet.class);

            when(mockDataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getString(anyInt())).thenReturn("1.2.3");

            // Create with null schema version
            dataSourceCheckRunner = new DataSourceCheckRunner(mockDataSource, null, "fail");
        }

        @Test
        void dbSchemaCheckSkipsWhenVersionIsNull() {
            listAppender.list.clear();
            dataSourceCheckRunner.run(null);

            assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .contains("Keine Schema-Version gesetzt, Überprüfung der korrekten Schema-Version wird übersprungen");
        }
    }

    @Nested
    @SpringBootTest(
        classes = { DataSourceCheckRunnerTest.TestConfig.class, DataSourceCheckRunner.class },
        properties = {
            "isy.util.datasource.schema-version=wrong-version",
            "isy.util.datasource.schema-invalid-version-action=warn"
        }
    )
    class RunDataSourceCheckRunnerWithInvalidVersionWarn {

        @MockitoSpyBean
        DataSourceCheckRunner dataSourceCheckRunner;

        @Test
        void dbSchemaCheckWarnsWhenVersionMismatch() {
            assertThat(dataSourceCheckRunner).isNotNull();
            // Verify automatic run happened during startup
            verify(dataSourceCheckRunner, times(1)).run(any());

            // Clear previous logs and run again
            listAppender.list.clear();
            dataSourceCheckRunner.run(null);

            assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .contains("Schema-Version ist nicht korrekt.");
        }
    }

    @Nested
    class RunDataSourceCheckRunnerWithInvalidVersionFail {

        private DataSourceCheckRunner dataSourceCheckRunner;

        @BeforeEach
        void setUp() throws SQLException {
            DataSource mockDataSource = Mockito.mock(DataSource.class);
            Connection connection = Mockito.mock(Connection.class);
            PreparedStatement statement = Mockito.mock(PreparedStatement.class);
            ResultSet resultSet = Mockito.mock(ResultSet.class);

            when(mockDataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getString(anyInt())).thenReturn("1.2.3");

            // Create with empty schema version and default "fail" action
            dataSourceCheckRunner = new DataSourceCheckRunner(mockDataSource, "wrong-version", "fail");
        }

        @Test
        void dbSchemaCheckThrowsExceptionWhenVersionMismatch() {
            assertThrows(PersistenceException.class, () -> dataSourceCheckRunner.run(null));
        }
    }
}
