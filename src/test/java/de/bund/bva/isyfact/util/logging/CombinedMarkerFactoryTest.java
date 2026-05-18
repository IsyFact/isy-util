package de.bund.bva.isyfact.util.logging;

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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.StreamSupport;

import org.slf4j.Marker;

import org.junit.jupiter.api.Test;

public class CombinedMarkerFactoryTest {
    private final static String SCHLUESSEL_TEST = "SCHLUESSEL_TEST";
    private final static String NAME_TEST = "NAME_TEST";
    private final static String VALUE_TEST = "VALUE_TEST";


    @Test
    public void testGetKSDMarker() {
        Marker marker = getKSDMarker(KATEGORIE_JOURNAL, SCHLUESSEL_TEST, TECHNIKDATEN);
        assertTrue(marker.contains(ROOTMARKER));
        Iterable<Marker> iterable = marker::iterator;
        List<Marker> markers = StreamSupport.stream(iterable.spliterator(), false).toList();
        assertEquals(3, markers.size());
        assertTrue(markers.getFirst().contains(KATEGORIE_JOURNAL));
        assertTrue(markers.get(1).contains(SCHLUESSEL_TEST));
        assertTrue(markers.get(2).contains(TECHNIKDATEN));
    }

    @Test
    public void testCreateMarker() {
        Marker marker = createMarker(NAME_TEST, VALUE_TEST);
        assertEquals(NAME_TEST, marker.getName());
        assertTrue(marker.contains(VALUE_TEST));
    }

    @Test
    public void testCreateKategorieMarker() {
        Marker marker = createKategorieMarker(VALUE_TEST);
        assertTrue(marker.contains(ROOTMARKER));
        Marker childMMarker = marker.iterator().next();
        assertEquals(KATEGORIE, childMMarker.getName());
        assertTrue(childMMarker.contains(VALUE_TEST));
    }

    @Test
    public void testCreateSchluesselMarker() {
        Marker marker = createSchluesselMarker(VALUE_TEST);
        assertTrue(marker.contains(ROOTMARKER));
        Marker childMMarker = marker.iterator().next();
        assertEquals(SCHLUESSEL, childMMarker.getName());
        assertTrue(childMMarker.contains(VALUE_TEST));
    }

    @Test
    public void testCreateDatentypMarker() {
        Marker marker = createDatentypMarker(VALUE_TEST);
        assertTrue(marker.contains(ROOTMARKER));
        Marker childMMarker = marker.iterator().next();
        assertEquals(DATENTYP, childMMarker.getName());
        assertTrue(childMMarker.contains(VALUE_TEST));
    }
}