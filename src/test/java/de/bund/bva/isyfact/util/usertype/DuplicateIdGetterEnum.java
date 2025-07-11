package de.bund.bva.isyfact.util.usertype;

import de.bund.bva.isyfact.util.persistence.annotation.EnumId;
import de.bund.bva.isyfact.util.persistence.annotation.PersistentValue;

public enum DuplicateIdGetterEnum {

    @PersistentValue("A")
    A,
    @PersistentValue("B")
    B;

    @EnumId
    public String getId1() {
        return "A";
    }

    @EnumId
    public String getId2() {
        return "B";
    }
}
