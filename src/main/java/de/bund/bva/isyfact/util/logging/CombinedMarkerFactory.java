package de.bund.bva.isyfact.util.logging;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Arrays;


/**
 * Factory for the construction of combinations of logging markers. Hierarchies of named markers may be built.
 */
public class CombinedMarkerFactory {

    public final static String KATEGORIE_JOURNAL = "JOURNAL";
    public final static String KATEGORIE_METRIK = "METRIK";
    public final static String KATEGORIE_PROFILING = "PROFILING";
    public final static String KATEGORIE_SICHERHEIT = "SICHERHEIT";
    public final static String TECHNIKDATEN = "Technikdaten";
    public final static String KATEGORIE = "kategorie";
    public final static String SCHLUESSEL = "schluessel";
    public final static String DATENTYP = "datentyp";
    public final static String ROOTMARKER = "rootmarker";

    /**
     * Return a root marker containing markes for Kategorie, Schluessel and Datentyp.
     *
     * @param kategorie  The value for the Kategorie.
     * @param schluessel The value for the Schluessel.
     * @param datentyp   The value for the Datentyp.
     * @return The root marker.
     */
    public static Marker getKSDMarker(final String kategorie, final String schluessel, final String datentyp) {
        Marker rootMarker = createRootMarker();
        rootMarker.add(createMarker(KATEGORIE, kategorie));
        rootMarker.add(createMarker(SCHLUESSEL, schluessel));
        rootMarker.add(createMarker(DATENTYP, datentyp));
        return rootMarker;
    }

    /**
     * Creates the needed root marker.
     *
     * @return The marker named root.
     */

    private static Marker createRootMarker() {
        Marker rootMarker = MarkerFactory.getMarker(ROOTMARKER);
        rootMarker.remove(MarkerFactory.getMarker(KATEGORIE));
        rootMarker.remove(MarkerFactory.getMarker(SCHLUESSEL));
        rootMarker.remove(MarkerFactory.getMarker(DATENTYP));
        return rootMarker;
    }

    /**
     * Creates a marker with the given name, containing the provided values.
     *
     * @param name   The name.
     * @param values Optional values.
     * @return The crated marker.
     */

    public static Marker createMarker(String name, String... values) {
        Marker marker = MarkerFactory.getMarker(name);
        Arrays.stream(values).forEach(value -> marker.add(MarkerFactory.getMarker(value)));
        return marker;
    }

    /**
     * Creates a marker with the name KATEGORIE, containing the provided values.
     *
     * @param values Optional values.
     * @return The crated marker.
     */
    public static Marker createKategorieMarker(String... values) {
        Marker rootMarker = createRootMarker();
        rootMarker.add(createMarker(KATEGORIE, values));
        return rootMarker;
    }

    /**
     * Creates a marker with the name SCHLUESSEL, containing the provided values.
     *
     * @param values Optional values.
     * @return The crated marker.
     */

    public static Marker createSchluesselMarker(String... values) {
        Marker rootMarker = createRootMarker();
        rootMarker.add(createMarker(SCHLUESSEL, values));
        return rootMarker;
    }

    /**
     * Creates a marker with the name DATENTYP, containing the provided values.
     *
     * @param values Optional values.
     * @return The crated marker.
     */
    public static Marker createDatentypMarker(String... values) {
        Marker rootMarker = createRootMarker();
        rootMarker.add(createMarker(DATENTYP, values));
        return rootMarker;
    }
}

