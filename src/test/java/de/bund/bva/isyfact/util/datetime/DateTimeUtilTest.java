package de.bund.bva.isyfact.util.datetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DateTimeUtilTest {

    @BeforeEach
    void resetClock() {
        DateTimeUtil.setClock(Clock.systemDefaultZone());
    }

    @Test
    void testDatumLiegtZwischen() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);
        LocalDate inside = LocalDate.of(2024, 6, 15);
        LocalDate before = LocalDate.of(2023, 12, 31);
        LocalDate after = LocalDate.of(2025, 1, 1);

        assertThat(DateTimeUtil.datumLiegtZwischen(start, start, end)).isTrue();
        assertThat(DateTimeUtil.datumLiegtZwischen(end, start, end)).isTrue();
        assertThat(DateTimeUtil.datumLiegtZwischen(inside, start, end)).isTrue();
        assertThat(DateTimeUtil.datumLiegtZwischen(before, start, end)).isFalse();
        assertThat(DateTimeUtil.datumLiegtZwischen(after, start, end)).isFalse();
        assertThatThrownBy(() -> DateTimeUtil.datumLiegtZwischen(inside, end, start))
            .isInstanceOf(DateTimeException.class);
    }

    @Test
    void testDatumLiegtZwischenExklusive() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 12, 31);
        LocalDate inside = LocalDate.of(2025, 6, 15);
        LocalDate before = LocalDate.of(2025, 1, 1);
        LocalDate after = LocalDate.of(2025, 12, 31);

        assertThat(DateTimeUtil.datumLiegtZwischenExklusive(start, start, end)).isFalse();
        assertThat(DateTimeUtil.datumLiegtZwischenExklusive(end, start, end)).isFalse();
        assertThat(DateTimeUtil.datumLiegtZwischenExklusive(inside, start, end)).isTrue();
        assertThat(DateTimeUtil.datumLiegtZwischenExklusive(before, start, end)).isFalse();
        assertThat(DateTimeUtil.datumLiegtZwischenExklusive(after, start, end)).isFalse();
        assertThatThrownBy(() -> DateTimeUtil.datumLiegtZwischenExklusive(inside, end, start))
            .isInstanceOf(DateTimeException.class);
    }

    @Test
    void testGetJahresanfang() {
        assertThat(DateTimeUtil.getJahresanfang(null)).isNull();
        LocalDate date = LocalDate.of(2025, 6, 15);
        assertThat(DateTimeUtil.getJahresanfang(date)).isEqualTo(LocalDate.of(2025, 1, 1));
    }

    @Test
    void testGetMonatsanfang() {
        assertThat(DateTimeUtil.getMonatsanfang(null)).isNull();
        LocalDate date = LocalDate.of(2025, 6, 15);
        assertThat(DateTimeUtil.getMonatsanfang(date)).isEqualTo(LocalDate.of(2025, 6, 1));
    }

    @Test
    void testGetMonatsende() {
        assertThat(DateTimeUtil.getMonatsende(null)).isNull();
        LocalDate date = LocalDate.of(2025, 2, 10);
        assertThat(DateTimeUtil.getMonatsende(date)).isEqualTo(LocalDate.of(2025, 2, 28));
        LocalDate leapYear = LocalDate.of(2024, 2, 10);
        assertThat(DateTimeUtil.getMonatsende(leapYear)).isEqualTo(LocalDate.of(2024, 2, 29));
        LocalDate dec = LocalDate.of(2025, 12, 10);
        assertThat(DateTimeUtil.getMonatsende(dec)).isEqualTo(LocalDate.of(2025, 12, 31));
    }

    @Test
    void testGetWerktag() {
        LocalDate sunday = LocalDate.of(2025, 10, 5); // Sunday
        LocalDate monday = LocalDate.of(2025, 10, 6); // Monday
        LocalDate tuesday = LocalDate.of(2025, 10, 7); // Tuesday
        LocalDate wednesday = LocalDate.of(2025, 10, 8); // Wednesday
        LocalDate thursday = LocalDate.of(2025, 10, 9); // Thursday
        LocalDate friday = LocalDate.of(2025, 10, 10); // Friday
        LocalDate saturday = LocalDate.of(2025, 10, 11); // Saturday

        // return next work day
        assertThat(DateTimeUtil.getWerktag(sunday)).isEqualTo(monday);
        // return same day if it is already a workday
        assertThat(DateTimeUtil.getWerktag(monday)).isEqualTo(monday);
        assertThat(DateTimeUtil.getWerktag(tuesday)).isEqualTo(tuesday);
        assertThat(DateTimeUtil.getWerktag(wednesday)).isEqualTo(wednesday);
        assertThat(DateTimeUtil.getWerktag(thursday)).isEqualTo(thursday);
        assertThat(DateTimeUtil.getWerktag(friday)).isEqualTo(friday);
        assertThat(DateTimeUtil.getWerktag(saturday)).isEqualTo(saturday);
    }

    @Test
    void testNowMethodsWithFixedClock() {
        LocalDateTime ldt = LocalDateTime.of(2025, 10, 6, 12, 12, 12);
        ZoneOffset offset = ZoneOffset.ofHours(2);
        Clock fixedClock = Clock.fixed(ldt.toInstant(offset), ZoneId.ofOffset("UTC", offset));
        DateTimeUtil.setClock(fixedClock);

        assertThat(DateTimeUtil.localTimeNow()).isEqualTo(LocalTime.of(12, 12, 12));
        assertThat(DateTimeUtil.localDateNow()).isEqualTo(LocalDate.of(2025, 10, 6));
        assertThat(DateTimeUtil.localDateTimeNow()).isEqualTo(ldt);
        assertThat(DateTimeUtil.offsetTimeNow()).isEqualTo(OffsetTime.of(12, 12, 12, 0, offset));
        assertThat(DateTimeUtil.offsetDateTimeNow()).isEqualTo(OffsetDateTime.of(ldt, offset));
        assertThat(DateTimeUtil.zonedDateTimeNow()).isEqualTo(ZonedDateTime.of(ldt, ZoneId.ofOffset("UTC", offset)));
    }

    @Test
    void testGetAndSetClock() {
        Clock original = DateTimeUtil.getClock();
        Clock newClock = Clock.offset(original, Duration.ofHours(1));
        DateTimeUtil.setClock(newClock);
        assertThat(DateTimeUtil.getClock()).isEqualTo(newClock);
        assertThatThrownBy(() -> DateTimeUtil.setClock(null)).isInstanceOf(NullPointerException.class);
    }
}
