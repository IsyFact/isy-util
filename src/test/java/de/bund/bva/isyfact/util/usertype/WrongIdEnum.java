package de.bund.bva.isyfact.util.usertype;

import de.bund.bva.isyfact.util.persistence.annotation.EnumId;
import de.bund.bva.isyfact.util.persistence.annotation.PersistentValue;

public enum WrongIdEnum {
    @PersistentValue("A")
    A;

    @EnumId
    public String getId() {
        return null;
    }
}
