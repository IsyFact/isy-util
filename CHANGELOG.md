# 5.0.0
### FEATURES
- `IFS-4736`: Übernahme von Inhalten zur manuellen Schema-Prüfung aus dem aufgelösten Baustein JPA/Hibernate
- `IFS-4713`: Dokumentation mit Stand aus `isyfact-standards` zusammengeführt und technische Schulden behoben.
- `IFS-4714`: Zentrale Versionierung eingeführt.
- `IFS-4575`: Portierung fehlender Tickets aus isy-standards
  - `ISY-1025`: Entfernen der JUnit Engine-Testdependency
  - `IFS-4367`: Entfernen des Serviceproviders 

### BUG FIXES
- `IFS-4730`: Standardwert für den Konfigurationsparameter `db.schema.version` geändert.
  - alter Wert: `"${db.schema.version}"`
  - neuer Wert: `null`

### DEPENDENCY UPGRADES
- `IFS-4655`: Update von Maven Checkstyle Plugin auf Version 3.6.0
- `IFS-4531`: Update von Flatten Maven Plugin auf Version 1.7.1
    * Hinzufügen von Maven Enforcer Plugin auf Version 3.6.0
    * Setzen der Maven Version auf 3.6.3
- `IFS-4580`: Spring-Boot Update auf Version 3.4.5
