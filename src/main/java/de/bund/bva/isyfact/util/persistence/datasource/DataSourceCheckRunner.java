package de.bund.bva.isyfact.util.persistence.datasource;


import static de.bund.bva.isyfact.util.text.MessageProvider.createMessage;

import jakarta.persistence.PersistenceException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import de.bund.bva.isyfact.util.logging.CombinedMarkerFactory;
import de.bund.bva.isyfact.util.text.FehlerSchluessel;

/**
 * Performs a database schema check at the start of the application.
 */

@Component
@ConditionalOnProperty(name = "isy.util.datasource.schema-version")
public class DataSourceCheckRunner implements ApplicationRunner {

    /**
     * The used logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(DataSourceCheckRunner.class);

    /**
     * Data source, provided at startup.
     */
    private final DataSource dataSource;

    /**
     * The schema version as specified in application properties.
     */
    private final String schemaVersion;

    /**
     * Action to be performed if the schema version is invalid.
     */
    private final String schemaInvalidVersionAction;

    /**
     * Creates a new instance.
     *
     * @param dataSource    The autowired data source
     * @param schemaVersion The schema version from application properties
     */
    @Autowired
    public DataSourceCheckRunner(DataSource dataSource,
                                 @Value("${isy.util.datasource.schema-version:#{null}}") String schemaVersion,
                                 @Value("${isy.util.datasource.schema-invalid-version-action:fail}") String schemaInvalidVersionAction) {
        this.dataSource = dataSource;
        this.schemaVersion = schemaVersion;
        this.schemaInvalidVersionAction = schemaInvalidVersionAction;
    }

    /**
     * Performs the checks at application startup.
     *
     * @param args Optional start arguments.
     * @throws Exception An error has occurred.
     */
    @Override
    public void run(ApplicationArguments args) {
        LOG.info(
            CombinedMarkerFactory.createMarker(
                CombinedMarkerFactory.KATEGORIE,
                CombinedMarkerFactory.KATEGORIE_JOURNAL
            ),
            "Starte initiale Überprüfungen."
        );
        if (schemaVersion != null) {
            LOG.info(
                CombinedMarkerFactory.createMarker(
                    CombinedMarkerFactory.KATEGORIE,
                    CombinedMarkerFactory.KATEGORIE_JOURNAL
                ),
                "Überprüfung der korrekten Schema-Version {}", schemaVersion
            );
            if (new DataSourceCheck().checkSchemaVersion(dataSource, schemaVersion)) {
                LOG.info(
                    CombinedMarkerFactory.createMarker(
                        CombinedMarkerFactory.KATEGORIE,
                        CombinedMarkerFactory.KATEGORIE_JOURNAL
                    ),
                    "Schema-Version ist korrekt"
                );
            } else {
                if ("warn".equals(this.schemaInvalidVersionAction)) {
                    LOG.warn(
                        CombinedMarkerFactory.createMarker(
                            CombinedMarkerFactory.KATEGORIE,
                            CombinedMarkerFactory.KATEGORIE_JOURNAL
                        ),
                        "Schema-Version ist nicht korrekt."
                    );
                } else {
                    throw new PersistenceException(createMessage(
                        FehlerSchluessel.FALSCHE_DB_SCHEMAVERSION,
                        this.schemaVersion
                    ));
                }
            }
        } else {
            LOG.info(
                CombinedMarkerFactory.createMarker(
                    CombinedMarkerFactory.KATEGORIE,
                    CombinedMarkerFactory.KATEGORIE_JOURNAL
                ),
                "Keine Schema-Version gesetzt, Überprüfung der korrekten Schema-Version wird übersprungen"
            );
        }
    }


}
