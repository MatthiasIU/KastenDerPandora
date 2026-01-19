# Projektübersicht – Kasten der Pandora

## Allgemeine Informationen
- **Projektname:** Kasten der Pandora  
- **Thema:** Digitaler Werkzeugkasten als Android-App  
- **Teilnehmer:** Matthias Reps, Johannes Lehmann  
- **Technologie:** Android Studio, Kotlin  

## Kurzbeschreibung
Der *Kasten der Pandora* ist eine Android-Anwendung, die verschiedene Alltagswerkzeuge in einer zentralen App bündelt. Die Werkzeuge werden in einer Rasteransicht dargestellt, ähnlich dem Android-Homescreen, und können vom Nutzer frei angeordnet werden. Die App ist modular aufgebaut und auf Erweiterbarkeit ausgelegt.

## Funktionsumfang (geplant)
- Rasteransicht mit umsortierbaren Anwendungen  
- Platzhalter für Werbung (bei nicht vorhandenem Premium)  

### Einstellungen
- Darkmode  
- Schriftgröße  
- Sprache  
- Premium-Platzhalter  

### Werkzeuge
- Taschenlampe mit SOS-Funktion  
- Lineal  
- Zähler  
- Rechner  
- Winkelmesser  
- Wasserwaage  
- Dezibelmesser  
- Kompass mit GPS  
- Sirene  
- Lupe  
- Sprachaufnahme mit Wiedergabe  
- Lumenmesser (Lichtstärke)  
- Optional: Audiospektrum  

### Plugins
- Platzhalter zur Veranschaulichung der Erweiterbarkeit  

## Planung & Vorgehen
- Analyse der Anforderungen anhand der Vorlesungstabelle  
- Entwurf des UI- und Architekturkonzepts  
- Schrittweise Implementierung der Werkzeuge  
- Schreiben ausgewählter Tests  
- Laufende Dokumentation während der Entwicklung  

## Systemarchitektur
- **Presentation Layer:** Activities, Fragments, ViewModels  
- **Domain Layer:** Logik der einzelnen Werkzeuge  
- **Data Layer:** Sensorzugriffe und Systemdienste  
- **Plugin-Schnittstelle:** vorbereitet, aktuell nur als Platzhalter  

### Use-Case (textuelle UML-Beschreibung)
**Akteur:** Nutzer  
- App starten  
- Werkzeug auswählen  
- Werkzeuge anordnen  
- Einstellungen ändern  
- Premium aktivieren (Platzhalter)  

## Einsatz von KI
KI wurde geplant bzw. genutzt für:
- Erstellung und Verbesserung von Bildern  
- Codeverbesserungen und Hilfestellungen  
- Verbesserung von Code-Kommentaren  
- Unterstützung bei Dokumentation und Use-Cases  
- Recherche zu hilfreichen Apps und UX-Ideen  

## Code & Tests
- Kommentare im Code zur Erklärung von Logik und Architektur  
- Unit-Tests für ausgewählte Funktionen, z. B. Rechner oder Zähler  
- Tests werden dokumentiert und ihr Zweck erläutert  

## Abgleich mit Vorlesungsanforderungen

| Anforderung aus der Vorlesung | Umsetzung |
|------------------------------|-----------|
| Planung | Projekt- und Vorgehensplanung |
| Systemarchitektur | Schichtenmodell, Use-Case |
| Dokumentation | KI-Einsatz, Kommentare |
| Tests | Unit-Tests ausgewählter Funktionen |
| Funktionale Umsetzung | Mehrere Werkzeuge implementiert |

----------------------------------------------------------------------------------------------------


1. Projektgrundlage festziehen

Package-Struktur festlegen (ui, domain, data, utils)

GitHub-Repo aufräumen, README mit Projektziel, Tech-Stack, Planung anlegen

Kotlin-Style, Namenskonventionen definieren

2. Architektur aufsetzen

MVVM einführen (View, ViewModel, Model)

Zentrale MainActivity mit Fragment-Container

Navigation Component einrichten

Basisklassen für Werkzeuge definieren, z. B. ToolFragment

3. Raster-Startscreen (Kernfeature)

RecyclerView mit GridLayoutManager

Dummy-Tools als Platzhalter

Drag & Drop zum Umsortieren (ItemTouchHelper)

Persistenz der Reihenfolge (SharedPreferences oder DataStore)

4. Erste echte Werkzeuge implementieren

Reihenfolge empfohlen:

Zähler

Rechner

Taschenlampe

Begründung: wenig Sensorabhängigkeit, gut testbar.

5. Einstellungen-Seite

Darkmode umschaltbar

Schriftgröße global

Sprache vorbereiten (Strings.xml)

Premium-Platzhalter

6. Systemzugriffe kapseln

Sensoren, Kamera, Audio strikt im Data-Layer

Permissions zentral verwalten

Fallbacks für nicht verfügbare Hardware

7. Tests einführen

Unit-Tests für Rechner und Zähler

Test erklären (Given, When, Then)

Testordner sauber strukturieren

8. Dokumentation parallel schreiben

Wo KI genutzt wurde explizit festhalten

Code kommentieren, warum etwas so gebaut ist

UML-Use-Case zeichnen (draw.io oder PlantUML)

9. Plugins & Premium vorbereiten

Plugin-Interface definieren (noch ohne Implementierung)

Werbung, Premium nur als UI-Platzhalter

10. Abgleich mit Vorlesung

Tabelle Punkt für Punkt abhaken

Fehlende Punkte gezielt ergänzen

Präsentation daraus ableiten
