package de.bund.bva.isyfact.util.persistence.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.bund.bva.isyfact.util.logging.CombinedMarkerFactory;

/**
 * Utility class for checks of data sources.
 */
public class DataSourceCheck {

    /**
     * The used logger.
     */
    private final static Logger LOG = LoggerFactory.getLogger(DataSourceCheck.class);

    /**
     * DB unavailable exception key.
     */
    private final static String FEHLER_DB_NICHT_VERFUEGBAR = "PERSI00008";

    /**
     * DB unavailable exception message.
     */
    private final static String DB_BEIM_HOCHFAHREN_NICHT_VERFUEGBAR = FEHLER_DB_NICHT_VERFUEGBAR +
        ": Die Version des Datenbankschemas konnte nicht gepr\u00FCft werden. Verbindungen zu diesem Schema sind erst nach einem Neustart wieder verf\u00FCgbar.";

    /**
     * Wrong schema version exception key.
     */
    private static final String FALSCHE_SCHEMA_VERSION = "EPLPER00001";

    /**
     * Checks the schema version.
     *
     * @return true, if schema version is ok.
     */
    public boolean checkSchemaVersion(DataSource dataSource, String schemaVersion) {
        try {
            String actualSchemaVersion = getSchemaVersion(dataSource, schemaVersion);
            if (actualSchemaVersion.equals(schemaVersion)) {
                return true;
            } else {
                LOG.warn(
                    FALSCHE_SCHEMA_VERSION,
                    "Die Version des Datenbankschemas entspricht nicht der erwarteten Version ( {} ).", schemaVersion
                );
                return false;
            }
        } catch (SQLException e) {
            LOG.warn(DB_BEIM_HOCHFAHREN_NICHT_VERFUEGBAR, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the actual schema version.
     *
     * @param dataSource    The data source.
     * @param schemaVersion The desired schema version.
     * @return The actual schema version.
     * @throws SQLException An error has occurred.
     */
    private String getSchemaVersion(DataSource dataSource, String schemaVersion) throws SQLException {
        LOG.info(CombinedMarkerFactory.createMarker(CombinedMarkerFactory.KATEGORIE, CombinedMarkerFactory.KATEGORIE_JOURNAL), "Überprüfung der korrekten Schema-Version {}", schemaVersion);

        String schemaQuery = "SELECT version_nummer FROM m_schema_version WHERE version_nummer = ? AND status = 'gueltig'";

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
}
