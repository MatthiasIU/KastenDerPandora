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

---
### Projektübersicht
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

**Wichtige Methoden:**

| Methode                 | Zweck                                 |
| ----------------------- | ------------------------------------- |
| `setupToolContent()`    | Überschreiben für UI-Init             |
| `onBackButtonPressed()` | Überschreiben für eigene Zurück-Logik |
| `dispatchTouchEvent()`  | Leitet an Wisch-Erkennung weiter      |

---

### Placeholder-System

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
**README.md Content:**
- Projektbeschreibung
- Funktionsumfang
- Technologie-Stack
- Projektplanung
- Systemarchitektur
- Use-Cases

**Kosten-Nutzen-Analyse:**
- KI-Einsatz dokumentiert
- Code-Kommentare
- Test-Dokumentation

---

### Git-Workflow
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