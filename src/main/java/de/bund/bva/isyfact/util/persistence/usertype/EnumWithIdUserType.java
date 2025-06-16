package de.bund.bva.isyfact.util.persistence.usertype;

import static de.bund.bva.isyfact.util.text.FehlerSchluessel.FALSCHE_ENUM_KONFIGURATION;
import static de.bund.bva.isyfact.util.text.FehlerSchluessel.UNBEKANNTER_STRING;
import static de.bund.bva.isyfact.util.text.FehlerSchluessel.UNBEKANNTE_AUSPRAEGUNG;
import static de.bund.bva.isyfact.util.text.MessageProvider.createMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jakarta.persistence.PersistenceException;

import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import de.bund.bva.isyfact.util.persistence.annotation.EnumId;

/**
 * Ein {@link UserType} zur Persistierung von Enumtypen, die einen Schlüssel enthalten, als String, d.h. in
 * eine VARCHAR-Spalte. Die get-Methode in der Enumklasse, die den Schlüssel liefert, muss mit {@link EnumId}
 * annotiert sein.
 */
public class EnumWithIdUserType extends AbstractImmutableStringUserType implements ParameterizedType {

    /**
     * Abbildung von String nach Enum-Ausprägung.
     */
    private final Map<String, Enum<?>> stringToEnum = new HashMap<String, Enum<?>>();
    /**
     * Die Enum-Klasse.
     */
    private Class<? extends Enum<?>> enumClass;
    /**
     * Die Methode der Enumklasse, die den Schlüssel liefert.
     */
    private Method idGetter;

    /**
     * Setzt die Enum-Klasse.
     *
     * @param enumClass die Enum-Klasse.
     */
    public void setEnumClass(Class<? extends Enum<?>> enumClass) {
        this.enumClass = enumClass;

        for (Method m : enumClass.getMethods()) {
            if (m.getAnnotation(EnumId.class) != null) {
                if (idGetter != null) {
                    throw new PersistenceException(createMessage(FALSCHE_ENUM_KONFIGURATION,
                            "Mehr als eine Methode in " + enumClass.getName() + " ist mit "
                                    + EnumId.class.getSimpleName() + " annotiert."));
                }
                idGetter = m;
            }
        }
        if (idGetter == null) {
            throw new PersistenceException(createMessage(FALSCHE_ENUM_KONFIGURATION, "Keine Methode in "
                    + enumClass.getName() + " ist mit " + EnumId.class.getSimpleName() + " annotiert."));
        }

        for (Enum<?> enumValue : enumClass.getEnumConstants()) {
            String enumId = getEnumId(enumValue);
            Enum<?> oldValue = stringToEnum.put(enumId, enumValue);
            if (oldValue != null) {
                throw new PersistenceException(createMessage(FALSCHE_ENUM_KONFIGURATION, "Im Enum "
                        + enumClass + " wird zweimal der Schlüssel '" + enumId + "' benutzt"));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Class<? extends Enum<?>> returnedClass() {
        return enumClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convertStringToInstance(String value) {
        Enum<?> e = stringToEnum.get(value);
        if (e == null) {
            throw new PersistenceException(createMessage(UNBEKANNTER_STRING, value, enumClass.getName()));
        }
        return e;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String convertInstanceToString(Object value) {
        String enumId = getEnumId((Enum<?>) value);
        if (enumId == null) {
            throw new PersistenceException(createMessage(UNBEKANNTE_AUSPRAEGUNG, value.toString(), enumClass.getName()));
        }
        return enumId;
    }

    /**
     * Liefert den Schlüssel einer Enumausprägung.
     *
     * @param enumValue die Enumausprägung
     * @return der Schlüssel
     */
    private String getEnumId(Enum<?> enumValue) {
        String enumId;
        try {
            enumId = (String) idGetter.invoke(enumValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);

        }
        return enumId;
    }

    /**
     * {@inheritDoc}
     */
    public void setParameterValues(Properties parameters) {
        String enumClassName = parameters.getProperty("enumClass");
        if (enumClassName == null) {
            throw new PersistenceException(createMessage(FALSCHE_ENUM_KONFIGURATION,
                    "Parameter 'enumClass' fehlt für " + EnumWithIdUserType.class.getSimpleName()));
        }
        try {
            Class<?> clazz = Class.forName(enumClassName);
            if (!clazz.isEnum()) {
                throw new PersistenceException(createMessage(FALSCHE_ENUM_KONFIGURATION, "Klasse "
                        + clazz.getName() + " ist kein Enum"));
            }
            @SuppressWarnings("unchecked")
            Class<? extends Enum<?>> enumClazz = (Class<? extends Enum<?>>) clazz;
            setEnumClass(enumClazz);
        } catch (ClassNotFoundException e) {
            throw new PersistenceException(createMessage(FALSCHE_ENUM_KONFIGURATION, "Enum-Klasse "
                    + enumClassName + " nicht gefunden"));
        }
    }
}
