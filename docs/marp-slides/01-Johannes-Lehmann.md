---
marp: true
theme: modern-iu
paginate: true
footer: Werkzeugkasten | Johannes Lehmann | 16.02.2026
---

# Werkzeugkasten<br>(Kasten der Pandora)

## Projektstatus - Februar 2026

1. **Johannes Lehmann**
2. Matthias Reps
3. Ilia Karpov

<!--
Willkommen zur Projektpräsentation. Ich bin Johannes und stelle zuerst die Infrastruktur und Basis-Architektur vor.
-->

---
### Projektübersicht
<!--
Kurze Einführung ins Projekt: Android-App mit mehreren Alltagstools. Fokus auf Modularität und Erweiterbarkeit.
-->
**Projekt:** Kasten der Pandora
- Digitaler Werkzeugkasten als Android-App
- Mehrere Alltagswerkzeuge in zentraler App
- Rasteransicht ähnlich Android-Homescreen
- Modularer Aufbau für Erweiterbarkeit

**Technologie-Stack:**
- Android Studio
- Kotlin
- MinSdk 31, TargetSdk 36

---

<div class="columns">
<div>

### Projektinitialisierung
<!--
Zwei Säulen meiner Arbeit: Links die Initialisierung, rechts die Infrastruktur. Beides war Grundlage für die Teamarbeit.
-->
**Erste Schritte:**
- Repository-Initialisierung
- README.md Dokumentation
- Projektstruktur festgelegt
- Icon-Integration
- Git-Ignore Konfiguration

**Ziel:**
- Solide Grundlage für Teamarbeit
- Klare Code-Konventionen
- Dokumentierte Architektur

</div>
<div>

### Infrastruktur
**Einführung der Basisklasse:**
- Gemeinsame Header für alle Tools
- Konsistente Navigation
- Zurück-Button Funktionalität
- Window Insets für Edge-to-Edge Support

**Vorteile:**
- Weniger Code-Duplizierung
- Einheitliches UI-Verhalten
- Einfache Erweiterbarkeit

</div>
</div>

---
#### `BaseToolActivity`
<!--
Herzstück der Architektur: Abstrakte Basisklasse verhindert Code-Duplizierung und sorgt für einheitliches Verhalten.
-->

Abstrakte Basisklasse für Tool-Screens mit einheitlichem Setup

```kotlin
abstract class BaseToolActivity : AppCompatActivity() {
    // Konfiguration (in Unterklassen überschreiben)
    abstract val toolTitle: Int
    open val toolIcon: Int = R.drawable.ic_placeholder
    open val layoutResId: Int = R.layout.activity_tool_placeholder
    open val showBackButton: Boolean = true
    open val enableSwipeBack: Boolean = false
```

**Lebenszyklus:** `onCreate` → Edge-to-Edge → Insets → Header → Zurück-Btn → Wischen → Inhalt

---
<!--
Die wichtigsten überschreibbaren Methoden. setupToolContent für UI-Init ist am häufigsten genutzt.
-->

**Wichtige Methoden:**

| Methode                 | Zweck                                 |
| ----------------------- | ------------------------------------- |
| `setupToolContent()`    | Überschreiben für UI-Init             |
| `onBackButtonPressed()` | Überschreiben für eigene Zurück-Logik |
| `dispatchTouchEvent()`  | Leitet an Wisch-Erkennung weiter      |

---

### Placeholder-System
<!--
Placeholder ermöglichten frühes Testen der Navigation ohne vollständige Tool-Implementierung.
-->

<div class="columns">
<div>

**Initiales Tool-Konzept:**
- Placeholder-Activities für alle geplanten Tools
- Grid-basierte Startseite
- Dynamische Tool-Liste
- Responsive Icon-Sizing

</div>
<div>

**Implementierte Tools:**
- Lampe, Lineal, Zähler, Uhr
- Rechner, Winkelmesser, Wasserwaage
- Dezibelmesser, Kompass, Sirene
- Lupe, Sprachaufnahme, Lumenmesser
- Kamera, Audiospektrum

</div>
</div>

---

### Settings-Infrastruktur
<!--
Zentrale Einstellungsseite mit SharedPreferences für Persistenz. Screenshot zeigt das finale Design.
-->

**Einstellungsseite implementiert:**
- SettingsActivity erstellt
- Shared UI-Komponenten
- SharedPreferences Integration
- Rückkehr zu MainActivity

**Architektur:**
- Separation of Concerns
- Settings in eigenem Activity
- Persistenz via SharedPreferences

![bg left h:100%](https://i.imgur.com/di5H7yH.png)

---

### Darkmode<br>Implementierung
<!--
Zwei Screenshots zeigen Light und Dark Mode. Besondere Herausforderung: Kontraste und Icon-Sichtbarkeit.
-->
**Dark Mode Feature:**
- Umschaltbare Themes
- `values-night` Resource Ordner
- Material Design 3 Support
- Theme-Selection in Settings

**Herausforderungen:**
- Konsistente Farben in beiden Themes
- Text-Kontraste sicherstellen
- Icon-Sichtbarkeit prüfen

![bg left h:100%](https://i.imgur.com/7CzxIxy.png)
![bg left h:100%](https://i.imgur.com/kjOAf49.png)

---

### Grid-Columns Einstellung
<!--
Nutzer kann 2-5 Spalten wählen. Icons werden automatisch skaliert. Live-Vorschau während der Auswahl.
-->
**Anpassbare Raster-Spalten:**
- 2-5 Spalten wählbar
- Dynamische Icon-Größenberechnung
- Responsive Layout
- Persistenz der Auswahl

**Implementierung:**
- SeekBar für Spaltenzahl
- Real-time Vorschau
- Speicherung in SharedPreferences

---

### Merge-Management
<!--
Koordination der Feature-Branches war wichtig für reibungslose Zusammenarbeit. PR-Workflow über GitHub.
-->
**Zusammenführung von Branches:**
- Feature-Branches: `settings_view`, `clock`, etc.
- Systematisches Merge-Management
- Konfliktlösung
- PR-Workflow über GitHub

**Zusammengeführte Features:**
- Settings View
- Clock/Alarm/Stopwatch/Timer
- Darkmode
- Grid-Settings

---

### Button-Positioning & UX
<!--
Material Design Richtlinien eingehalten. Touch-Targets mindestens 48dp. Screen Reader Unterstützung implementiert.
-->
**Konsistente UI-Elemente:**
- Button-Standardisierung
- Accessibility-Verbesserungen
- Touch-Targets optimiert
- Visuelle Feedback-Systeme

**Verbesserungen:**
- Material Design Richtlinien
- Ripple-Effekte
- Fokus-Zustände
- Screen Reader Unterstützung

---

### Header-Layout
<!--
Wiederverwendbares Header-Layout via include-Tag. Reduziert Duplizierung und erleichtert Änderungen.
-->
**Gemeinsamer Header:**
- Titel-Text dynamisch pro Tool
- App-Icon rechts
- Zurück-Button links
- Einheitliches Styling

**Resource-Struktur:**
- `layout_header.xml` wiederverwendbar
- Via `<include>` eingebunden
- Konsistentes Design

---

### Placeholders für Tools
<!--
Demo-Activities ermöglichten Testen der App-Struktur bevor echte Tools implementiert waren.
-->
**Demo-Aktivitäten:**
- Zeigten App-Struktur
- Ermöglichten Testen des Grids
- Placeholder-Text mit Tool-Name
- Fehlende Icons als Placeholder

**Herausforderungen:**
- Balance zwischen Demo und Implementation
- Entscheidung, was zuerst implementiert wird

---

### Dokumentationsstruktur
<!--
README enthält alles Wichtige für neue Entwickler. KI-Einsatz transparent dokumentiert.
-->

<div class="columns">
<div>

**README.md Content:**
- Projektbeschreibung
- Funktionsumfang
- Technologie-Stack
- Projektplanung
- Systemarchitektur
- Use-Cases

</div>
<div>

**Kosten-Nutzen-Analyse:**
- KI-Einsatz dokumentiert
- Code-Kommentare
- Test-Dokumentation

</div>
</div>

---

### Git-Workflow
<!--
Feature-Branches und PR-Workflow. Commit-Messages beschreiben das Warum, nicht das Was.
-->
**Branching-Strategie:**
- Feature-Branches pro Tool
- Pull-Request Workflow
- Code-Review-Prozess
- Merge in master

**Commit-Struktur:**
- Klare Commit-Messages
- Beschreibung von Warum, nicht Was
- Versionierung via Tags

---

### Design-System
<!--
Material Design 3 als Grundlage. Konsistente Farben, Typographie und Abstände für professionelles Erscheinungsbild.
-->
**Visuelle Konsistenz:**
- Material Design 3
- Farbschema definiert
- Typographie-Regeln
- Abstände und Größen

**Komponenten-Bibliothek:**
- Wiederverwendbare Views
- Consistentes Verhalten
- Barrierefreiheit

---

### Status & Rückblick
<!--
Zusammenfassung meiner Beiträge. Wichtigste Erkenntnis: Gute Architektur von Anfang an spart später Zeit.
-->
**Johannes' Beiträge Zusammenfassung:**
- Projekt-Initialisierung
- Infrastruktur (BaseToolActivity, Grid)
- Settings-System (Darkmode, Grid-Columns)
- Merge-Management & Dokumentation
- Design-System

**Erkenntnisse:**
- Gute Architektur spart Zeit
- Konsistente UX wichtig für Akzeptanz
- Dokumentation von Anfang an

---

## Übergabe an Matthias Reps
<!--
Matthias übernimmt und zeigt seine Tool-Implementierungen: Counter, Light, I18n, Decibel Meter und Spirit Level.
-->