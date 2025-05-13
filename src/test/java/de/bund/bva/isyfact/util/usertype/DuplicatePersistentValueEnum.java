package de.bund.bva.isyfact.util.usertype;

import de.bund.bva.isyfact.util.persistence.annotation.EnumId;
import de.bund.bva.isyfact.util.persistence.annotation.PersistentValue;

public enum DuplicatePersistentValueEnum {

    @PersistentValue("A")
    A,
    @PersistentValue("A")
    B;

    @EnumId
    public String getId() {
        return "A";
    }
}
