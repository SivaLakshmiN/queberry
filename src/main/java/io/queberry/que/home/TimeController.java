package io.queberry.que.home;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.zone.ZoneRulesException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class TimeController {

    @GetMapping("/timezones")
    public ResponseEntity<?> timeZones(@RequestParam(value = "continent", required = false, defaultValue = "") String continent) {
        Instant instant = Instant.now();

        Set<TimeZoneResource> timeZoneResources = ZoneId.getAvailableZoneIds().stream()
                .filter(s -> s.contains(continent))
                .map(ZoneId::of)
                .sorted(Comparator.comparingInt(zoneId ->
                        instant.atZone(zoneId).getOffset().getTotalSeconds()))
                .map(zoneId -> new TimeZoneResource(zoneId))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(timeZoneResources);
    }

    @GetMapping("/continents")
    public ResponseEntity<?> continents() {
        Set<String> continents = new TreeSet<String>();
        continents.add("Asia");
        continents.add("Europe");
        continents.add("Africa");
        continents.add("America");
        continents.add("Antarctica");
        continents.add("Australia");

        return ResponseEntity.ok(continents);
    }

    @GetMapping("/locales")
    public ResponseEntity<?> locales() {
        List<LocaleResource> locales = Stream.of(Locale.getAvailableLocales())
                .filter(locale -> !locale.getDisplayCountry(Locale.ENGLISH).isEmpty())
                .map(locale -> new LocaleResource(locale))
                .sorted(Comparator.comparing(item -> item.getLocale().getLanguage()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(locales);
    }

    @GetMapping("/countries")
    public ResponseEntity<?> countries() {
        Set<String> locales = Stream.of(Locale.getAvailableLocales())
                .filter(locale -> !locale.getDisplayCountry(Locale.ENGLISH).isEmpty())
                .map(locale -> locale.getDisplayCountry())
                .collect(Collectors.toSet());
        return ResponseEntity.ok(locales.stream().sorted(String::compareToIgnoreCase).collect(Collectors.toList()));
    }


    @GetMapping("/countries/{countryCode}/timezones")
    public ResponseEntity<?> timezonesOfCountry(@PathVariable String countryCode) {
        Set<TimeZoneResource> timeZoneResources = Stream.of(TimeZone.getAvailableIDs(Integer.parseInt(countryCode)))
                .map(s -> {
                    ZoneId zoneId = null;
                    try {
                        zoneId = ZoneId.of(s);
                    } catch (ZoneRulesException e) {
                        log.error("{}", e.getMessage());
                    }
                    return zoneId;
                })
                .filter(zoneId -> zoneId != null)
                .map(zoneId -> new TimeZoneResource(zoneId))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(timeZoneResources);
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    static class LocaleResource {
        private final Locale locale;

        public String getCountryCode() {
            return this.locale.getCountry();
        }

        public String getCountry() {
            return this.locale.getDisplayCountry();
        }


        public String getDisplayCountryEnglish() {
            return locale.getDisplayCountry(Locale.ENGLISH);
        }

        public String getDisplayCountryLocale() {
            return locale.getDisplayCountry(this.locale);
        }

        public String getDisplayLanguage() {
            return this.locale.getDisplayLanguage();
        }
    }

    @Getter
    @EqualsAndHashCode
    static class TimeZoneResource {
        private final ZoneId zoneId;

        @JsonIgnore
        private final Instant instant;

        @JsonIgnore
        private final ZonedDateTime current;

        @JsonIgnore
        private final ZonedDateTime zdt;

        TimeZoneResource(ZoneId zoneId) {
            this.zoneId = zoneId;

            instant = Instant.now();
            current = instant.atZone(ZoneId.systemDefault());
            zdt = current.withZoneSameInstant(this.zoneId);
        }

        public ZoneOffset getOffset() {
            return this.zdt.getOffset();
        }

        public ZoneId getZoneId() {
            return this.zoneId;
        }

        public String getLocalTime() {
            return zdt.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        }
    }
}