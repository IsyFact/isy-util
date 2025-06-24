package de.bund.bva.isyfact.util.usertype;

import de.bund.bva.isyfact.util.persistence.annotation.PersistentValue;

/**
 * Test-Enum.
 */
public enum Vorgangsstatus {
    /**
     * .
     */
    @PersistentValue("N")
    NEU,
    /**
     * .
     */
    @PersistentValue("B")
    IN_BEARBEITUNG,
    /**
     * .
     */
    @PersistentValue("E")
    ERLEDIGT

}
