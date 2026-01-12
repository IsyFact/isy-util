# 5.0.0
### FEATURES
- `IFS-4736`: Übernahme von Inhalten zur manuellen Schema-Prüfung aus dem aufgelösten Baustein JPA/Hibernate
- `IFS-4713`: Dokumentation mit Stand aus `isyfact-standards` zusammengeführt und technische Schulden behoben.
- `IFS-4714`: Zentrale Versionierung eingeführt.
- `IFS-4575`: Portierung fehlender Tickets aus isy-standards
  - `ISY-1025`: Entfernen der JUnit Engine-Testdependency
  - `IFS-4367`: Entfernen des Serviceproviders 
- `IFS-4583`: Wiedereinführung der Quality-Gates

### BREAKING CHANGES
- `IFS-4922`: Aktualisierung von Java 17 auf 25

### BUG FIXES
- `IFS-4730`: Standardwert für den Konfigurationsparameter `db.schema.version` geändert.
  - alter Wert: `"${db.schema.version}"`
  - neuer Wert: `null`

### DEPENDENCY UPGRADES
- Update org.apache.maven.plugins:maven-source-plugin von Version 3.2.1 auf 3.4.0
- Update net.logstash.logback:logstash-logback-encoder von Version 8.0 auf 8.1
- Update org.apache.maven.plugins:maven-jar-plugin von Version 3.3.0 auf 3.5.0
- Update IsyFact/isy-github-actions-templates/.github/workflows/docs_build_template.yml von Version 2.1.0 auf 2.1.1
- Update IsyFact/isy-github-actions-templates/.github/workflows/maven_create_release_template.yml von Version 2.0.0 auf 2.1.1
- Update IsyFact/isy-github-actions-templates/.github/workflows/maven_build_template.yml von Version 2.0.0 auf 2.1.1
- Update IsyFact/isy-github-actions-templates/.github/workflows/next_version.yml von Version 2.0.0 auf 2.1.1
- Update IsyFact/isy-github-actions-templates/.github/workflows/dependabot_auto_changelog_template.yml von Version 1.8.0 auf 2.1.1
- Update com.github.spotbugs:spotbugs-maven-plugin von Version 4.9.8.1 auf 4.9.8.2
- `IFS-4655`: Update von Maven Checkstyle Plugin auf Version 3.6.0
- `IFS-4531`: Update von Flatten Maven Plugin auf Version 1.7.1
    * Hinzufügen von Maven Enforcer Plugin auf Version 3.6.0
    * Setzen der Maven Version auf 3.6.3
- `IFS-4580`: Spring-Boot Update auf Version 3.4.5
- `IFS-4864`: Spring-Boot Update auf Version 3.5.6
 