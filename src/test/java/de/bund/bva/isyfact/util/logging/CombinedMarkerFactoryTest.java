package de.bund.bva.isyfact.util.logging;

import junit.framework.TestCase;
import org.slf4j.Marker;

import java.util.List;
import java.util.stream.StreamSupport;

import static de.bund.bva.isyfact.util.logging.CombinedMarkerFactory.DATENTYP;
import static de.bund.bva.isyfact.util.logging.CombinedMarkerFactory.KATEGORIE;
import static de.bund.bva.isyfact.util.logging.CombinedMarkerFactory.KATEGORIE_JOURNAL;
import static de.bund.bva.isyfact.util.logging.CombinedMarkerFactory.ROOTMARKER;
import static de.bund.bva.isyfact.util.logging.CombinedMarkerFactory.SCHLUESSEL;
import static de.bund.bva.isyfact.util.logging.CombinedMarkerFactory.TECHNIKDATEN;
import static de.bund.bva.isyfact.util.logging.CombinedMarkerFactory.createDatentypMarker;
import static de.bund.bva.isyfact.util.logging.CombinedMarkerFactory.createKategorieMarker;
import static de.bund.bva.isyfact.util.logging.CombinedMarkerFactory.createMarker;
import static de.bund.bva.isyfact.util.logging.CombinedMarkerFactory.createSchluesselMarker;
import static de.bund.bva.isyfact.util.logging.CombinedMarkerFactory.getKSDMarker;

public class CombinedMarkerFactoryTest extends TestCase {
    private final static String SCHLUESSEL_TEST = "SCHLUESSEL_TEST";
    private final static String NAME_TEST = "NAME_TEST";
    private final static String VALUE_TEST = "VALUE_TEST";


    public void testGetKSDMarker() {
        Marker marker = getKSDMarker(KATEGORIE_JOURNAL, SCHLUESSEL_TEST, TECHNIKDATEN);
        assertTrue(marker.contains(ROOTMARKER));
        Iterable<Marker> iterable = marker::iterator;
        List<Marker> markers = StreamSupport.stream(iterable.spliterator(), false).toList();
        assertEquals(3, markers.size());
        assertTrue(markers.get(0).contains(KATEGORIE_JOURNAL));
        assertTrue(markers.get(1).contains(SCHLUESSEL_TEST));
        assertTrue(markers.get(2).contains(TECHNIKDATEN));
    }

    public void testCreateMarker() {
        Marker marker = createMarker(NAME_TEST, VALUE_TEST);
        assertEquals(NAME_TEST, marker.getName());
        assertTrue(marker.contains(VALUE_TEST));
    }

    public void testCreateKategorieMarker() {
        Marker marker = createKategorieMarker(VALUE_TEST);
        assertTrue(marker.contains(ROOTMARKER));
        Marker childMMarker = marker.iterator().next();
        assertEquals(KATEGORIE, childMMarker.getName());
        assertTrue(childMMarker.contains(VALUE_TEST));
    }

    public void testCreateSchluesselMarker() {
        Marker marker = createSchluesselMarker(VALUE_TEST);
        assertTrue(marker.contains(ROOTMARKER));
        Marker childMMarker = marker.iterator().next();
        assertEquals(SCHLUESSEL, childMMarker.getName());
        assertTrue(childMMarker.contains(VALUE_TEST));
    }

    public void testCreateDatentypMarker() {
        Marker marker = createDatentypMarker(VALUE_TEST);
        assertTrue(marker.contains(ROOTMARKER));
        Marker childMMarker = marker.iterator().next();
        assertEquals(DATENTYP, childMMarker.getName());
        assertTrue(childMMarker.contains(VALUE_TEST));
    }
}