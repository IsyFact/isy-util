package de.bund.bva.isyfact.util.persistence.datasource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

class ApplicationRunnerDbSchemaCheckTest {

    protected static final Logger LOG = (Logger) LoggerFactory.getLogger(ApplicationRunnerDbSchemaCheck.class);
    protected static ListAppender<ILoggingEvent> listAppender;

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

            when(resultSet.getString(anyInt())).thenReturn("1.2.3" );
            return dataSource;
        }
    }

    @Nested
    @SpringBootTest(classes = {ApplicationRunnerDbSchemaCheckTest.TestConfig.class, ApplicationRunnerDbSchemaCheck.class}, properties = {"db.schema.version=1.2.3"})
    class RunDBSchemaCheck {

        @MockitoSpyBean
        ApplicationRunnerDbSchemaCheck applicationRunnerDbSchemaCheck;

        @Test
        void dbSchemaCheckRun() {
            assertThat(applicationRunnerDbSchemaCheck).isNotNull();
            // check automatic run
            verify(applicationRunnerDbSchemaCheck, times(1)).run(any());

            // check logs
            applicationRunnerDbSchemaCheck.run(null);
            assertThat(listAppender.list).hasSize(3);
            assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .contains("Schema-Version ist korrekt");
        }
    }

    @Nested
    @SpringBootTest(classes = {ApplicationRunnerDbSchemaCheckTest.TestConfig.class, ApplicationRunnerDbSchemaCheck.class})
    class RunDBSchemaCheckWithNull {

        @MockitoSpyBean
        ApplicationRunnerDbSchemaCheck applicationRunnerDbSchemaCheck;

        @Test
        void dbSchemaCheckRun() {
            assertThat(applicationRunnerDbSchemaCheck).isNotNull();
            // check automatic run
            verify(applicationRunnerDbSchemaCheck, times(1)).run(any());

            // check logs
            applicationRunnerDbSchemaCheck.run(null);
            assertThat(listAppender.list).hasSize(2);
            assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .contains("Keine Schema-Version gesetzt, Überprüfung der korrekten Schema-Version wird übersprungen");
        }
    }

    @Nested
    @SpringBootTest(classes = {ApplicationRunnerDbSchemaCheckTest.TestConfig.class, ApplicationRunnerDbSchemaCheck.class}, properties = {"db.schema.version="})
    class RunDBSchemaCheckWithEmpty {

        @MockitoSpyBean
        ApplicationRunnerDbSchemaCheck applicationRunnerDbSchemaCheck;

        @Test
        void dbSchemaCheckRun() {
            assertThat(applicationRunnerDbSchemaCheck).isNotNull();
            // check automatic run
            verify(applicationRunnerDbSchemaCheck, times(1)).run(any());

            // check logs
            applicationRunnerDbSchemaCheck.run(null);
            assertThat(listAppender.list).hasSize(3);
            assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .contains("Schema-Version ist nicht korrekt.");
        }
    }
}

