package de.bund.bva.isyfact.util.persistence.datasource;

import de.bund.bva.isyfact.util.logging.CombinedMarkerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class for checks of data sources.
 */
public class DataSourceCheck {
    /**
     * The used logger
     */
    private final static Logger LOG = LoggerFactory.getLogger(DataSourceCheck.class);

    private final static String FEHLER_DB_NICHT_VERFUEGBAR = "PERSI00008";
    private final static String DB_BEIM_HOCHFAHREN_NICHT_VERFUEGBAR = FEHLER_DB_NICHT_VERFUEGBAR +
            ": Die Version des Datenbankschemas konnte nicht gepr\u00FCft werden. Verbindungen zu diesem Schema sind erst nach einem Neustart wieder verf\u00FCgbar.";
    private static final String FALSCHE_SCHEMA_VERSION = "EPLPER00001";

    /**
     * Default message to query the data sources schema version.
     */
    private String schemaQuery = "SELECT version_nummer FROM m_schema_version WHERE version_nummer = ? AND status = 'gueltig'";

    /**
     * Default constructor. Instance will us default schema query.
     */
    public DataSourceCheck() {
    }

    /**
     * Default constructor with custom schema query.
     *
     * @param schemaQuery The custom schema query.
     */
    public DataSourceCheck(String schemaQuery) {
        this.schemaQuery = schemaQuery;
    }

    /**
     * Retrieves the actual schema version.
     *
     * @param dataSource    The data source.
     * @param schemaVersion The desired schema version.
     * @return The actual schema version.
     * @throws SQLException An error has occurred.
     */
    public String getSchemaVersion(DataSource dataSource, String schemaVersion) throws SQLException {
        LOG.info(CombinedMarkerFactory.createMarker(CombinedMarkerFactory.KATEGORIE, CombinedMarkerFactory.KATEGORIE_JOURNAL), "Überprüfung der korrekten Schema-Version {}", schemaVersion);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(schemaQuery)) {
            LOG.debug("Checking version for data source, expected: " + schemaVersion);
            statement.setString(1, schemaVersion);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String actualVersion = resultSet.getString(1);
                    LOG.debug("Found valid version: " + actualVersion);
                    return actualVersion;
                } else {
                    LOG.debug("Valid version not found");
                    return "invalid";
                }
            }
        }
    }

    /**
     * Checks the  schema version.
     *
     * @param dataSource    The data source.
     * @param schemaVersion The desired schema version.
     * @return true, if schema version is ok.
     * @throws SQLException An error has occurred.
     */
    private boolean checkSchemaVersion(DataSource dataSource, String schemaVersion) throws SQLException {
        String actualSchemaVersion = getSchemaVersion(dataSource, schemaVersion);
        if (actualSchemaVersion.equals(schemaVersion)) {
            return true;
        } else {
            LOG.warn(FALSCHE_SCHEMA_VERSION,
                    "Die Version des Datenbankschemas entspricht nicht der erwarteten Version ( {} ).", schemaVersion);
            return false;
        }
    }

    /**
     * Checks the  schema version.
     *
     * @param dataSource    The data source.
     * @param schemaVersion The desired schema version.
     * @return true, if schema version is ok.
     */
    public boolean checkSchemaVersionCriticalDataSource(DataSource dataSource, String schemaVersion) {
        try {
            return checkSchemaVersion(dataSource, schemaVersion);
        } catch (SQLException e) {
            LOG.warn(DB_BEIM_HOCHFAHREN_NICHT_VERFUEGBAR, e);
            throw new RuntimeException(e);
        }
    }


    /**
     * Checks the  schema version.
     *
     * @param dataSource    The data source.
     * @param schemaVersion The desired schema version.
     * @return true, if schema version is ok.
     * .
     */
    public boolean checkSchemaVersionNonCriticalDataSource(DataSource dataSource, String schemaVersion) {
        try {
            return checkSchemaVersion(dataSource, schemaVersion);
        } catch (SQLException e) {
            LOG.warn(DB_BEIM_HOCHFAHREN_NICHT_VERFUEGBAR, e);
            throw new RuntimeException(e);
        }
    }

}
