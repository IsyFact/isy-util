package de.bund.bva.isyfact.util.persistence.datasource;

import de.bund.bva.isyfact.util.logging.CombinedMarkerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Performs a database schema check at the start of the application.
 */

@Component
public class ApplicationRunnerDbSchemaCheck implements ApplicationRunner {
    /**
     * The used logger.
     */
    private final static Logger LOG = LoggerFactory.getLogger(ApplicationRunnerDbSchemaCheck.class);
    /**
     * Data source, provided at startup.
     */
    private final DataSource dataSource;
    /**
     * The schema version as specified in application properties.
     */
    private final String schemaVersion;

    /**
     * Creates a new instance.
     *
     * @param dataSource    The autowired data source
     * @param schemaVersion The schema version from application properties
     */
    @Autowired
    public ApplicationRunnerDbSchemaCheck(DataSource dataSource, @Value("${db.schema.version}") String schemaVersion) {
        this.dataSource = dataSource;
        this.schemaVersion = schemaVersion;
    }

    /**
     * Performs the checks at application startup.
     *
     * @param args Optional start arguments.
     * @throws Exception An error has occurred.
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info(CombinedMarkerFactory.createMarker(CombinedMarkerFactory.KATEGORIE, CombinedMarkerFactory.KATEGORIE_JOURNAL),
                "Starte initiale Überprüfungen.");
        if (schemaVersion != null) {
            LOG.info(CombinedMarkerFactory.createMarker(CombinedMarkerFactory.KATEGORIE, CombinedMarkerFactory.KATEGORIE_JOURNAL),
                    "Überprüfung der korrekten Schema-Version {}", schemaVersion);
            if (isSchemaOk()) {
                LOG.info(CombinedMarkerFactory.createMarker(CombinedMarkerFactory.KATEGORIE, CombinedMarkerFactory.KATEGORIE_JOURNAL),
                        "Schema-Version ist korrekt");
            } else {
                LOG.warn(CombinedMarkerFactory.createMarker(CombinedMarkerFactory.KATEGORIE, CombinedMarkerFactory.KATEGORIE_JOURNAL),
                        "Schema-Version ist nicht korrekt.");
            }
        } else {
            LOG.info(CombinedMarkerFactory.createMarker(CombinedMarkerFactory.KATEGORIE, CombinedMarkerFactory.KATEGORIE_JOURNAL),
                    "Keine Schema-Version gesetzt, Überprüfung der korrekten Schema-Version wird übersprungen");
        }
    }

    /**
     * Runs a db schema check.
     */
    private boolean isSchemaOk() {
        return new DataSourceCheck().checkSchemaVersionCriticalDataSource(dataSource, schemaVersion);

    }
}