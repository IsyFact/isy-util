# 5.0.0
- `IFS-4736`: Übernahme von Inhalten zur manuellen Schema-Prüfung aus dem aufgelösten Baustein JPA/Hibernate
- `IFS-4713`: Dokumentation mit Stand aus `isyfact-standards` zusammengeführt und technische Schulden behoben.
- `IFS-4714`: Zentrale Versionierung eingeführt.
- `IFS-4575`: Portierung fehlender Tickets aus isy-standards
  - `ISY-1025`: Entfernen der JUnit Engine-Testdependency
  - `IFS-4367`: Entfernen des Serviceproviders 
- `IFS-4655`: Update von Maven Checkstyle Plugin auf Version 3.6.0

# 4.0.0
- `IFS-4575`: Portierung fehlender Tickets aus isy-standards
  - `IFS-4482`: Entfernen des Message Source Holders
    - *BREAKING CHANGE*: Deprecated Klasse `MessageSourceHolder` entfernt

# 2.4.0
- `IFS-4185`: Bereitstellung eines MarkerGenerator, nutzbar für das Logging 
- `IFS-4181`: Überführung EnumHandler aus isy-persistence und Bereitstellung zur Nutzung als Baustein
- `RF-1040`: Scope für Spotbugs-Annotations Abhängigkeit auf provided gesetzt

# 2.2.0
- `IFS-568`: Entfernt redundante Methode `getMessage(String schluessel)` aus dem `MessageSourceHolder`

# 2.0.0
- `IFS-32`: Package-Name auf `de.bund.bva.isyfact` geändert

# 1.8.0
- `IFS-262`: `isyfact-masterpom` deprecated (Abschaffung mit IsyFact 2.0), `isyfact-masterpom-lib` aufgelöst, Bibliotheken benutzen `isyfact-standards` als Parent-POM

# 1.7.0
- `IFS-189`: Repositories der IsyFact-Standards zusammengeführt, Bibliotheken benutzen wieder gemeinsames Produkt-BOM und werden zentral über das POM `isyfact-standards` versioniert

# 1.6.0
- `IFS-111`: Abhängigkeit von `isy-util` auf `isy-serviceapi-sst` aufgelöst. `StelltLoggingKontextBereitInterceptor` nach `isy-serviceapi-core` verschoben.
**Achtung**: Dieses Ticket nimmt die Änderungen von `IFS-9` (s. v1.5.1) zurück. Der `checkAndUpdate()`-Task der Konfiguration kann in Zukunft direkt über die Bibliothek `isy-task` eingebunden und konfiguriert werden.

# 1.5.4
- `IFS-120`: Der `StelltLoggingKontextBereitInterceptor` erzeugt keine Warn-Logausgabe mehr, wenn eine `StelltLoggingKontextBereit`-Annotation mit dem Parameter `nutzeAufrufKontext = false` definiert ist.

# 1.5.1
- `RF-161`: Bibliotheken binden genutzte Bibliotheken direkt ein und nicht mehr über BOM-Bibliotheken
- `IFS-9`: `StelltLoggingKontextBereit`-Annotation auch ohne `AufrufkontextTo` nutzbar
