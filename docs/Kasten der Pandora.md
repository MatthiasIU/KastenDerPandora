---
marp: true
theme: modern-iu
paginate: true
footer:
---

# Kasten der Pandora - Project Status Presentation
## Projektstatus - Februar 2026

---

# TEIL 1: JOHANNES LEHMANN
## (15 Folien)

---

### Folie 1: Projektübersicht
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

### Folie 2: Projektinitiale
**Erste Schritte (Johannes Lehmann):**
- Repository-Initialisierung
- README.md Dokumentation
- Projektstruktur festgelegt
- Icon-Integration
- Git-Ignore Konfiguration

**Ziel:**
- Solide Grundlage für Teamarbeit
- Klare Code-Konventionen
- Dokumentierte Architektur

---

### Folie 3: Infrastruktur - BaseToolActivity
**Einführung der Basisklasse:**
- Gemeinsame Header für alle Tools
- Konsistente Navigation
- Zurück-Button Funktionalität
- Window Insets für Edge-to-Edge Support

**Vorteile:**
- Weniger Code-Duplizierung
- Einheitliches UI-Verhalten
- Einfache Erweiterbarkeit

---

### Folie 4: Placeholder-System
**Initiales Tool-Konzept:**
- Placeholder-Activities für alle geplanten Tools
- Grid-basierte Startseite
- Dynamische Tool-Liste
- Responsive Icon-Sizing

**Implementierte Tools:**
- Lampe, Lineal, Zähler, Uhr
- Rechner, Winkelmesser, Wasserwaage
- Dezibelmesser, Kompass, Sirene
- Lupe, Sprachaufnahme, Lumenmesser
- Kamera, Audiospektrum

---

### Folie 5: Settings-Infrastruktur
**Einstellungsseite implementiert:**
- SettingsActivity erstellt
- Shared UI-Komponenten
- SharedPreferences Integration
- Rückkehr zu MainActivity

**Architektur:**
- Separation of Concerns
- Settings in eigenem Activity
- Persistenz via SharedPreferences

---

### Folie 6: Darkmode-Implementierung
**Dark Mode Feature:**
- Umschaltbare Themes
- `values-night` Resource Ordner
- Material Design 3 Support
- Theme-Selection in Settings

**Herausforderungen:**
- Konsistente Farben in beiden Themes
- Text-Kontraste sicherstellen
- Icon-Sichtbarkeit prüfen

---

### Folie 7: Grid-Columns Einstellung
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

### Folie 8: Merge-Management
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

### Folie 9: Button-Positioning & UX
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

### Folie 10: Header-Layout
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

### Folie 11: Placeholders für Tools
**Demo-Aktivitäten:**
- Zeigten App-Struktur
- Ermöglichten Testen des Grids
- Placeholder-Text mit Tool-Name
- Fehlende Icons als Placeholder

**Herausforderungen:**
- Balance zwischen Demo und Implementation
- Entscheidung, was zuerst implementiert wird

---

### Folie 12: Dokumentationsstruktur
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

### Folie 13: Git-Workflow
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

### Folie 14: Design-System
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

### Folie 15: Status & Rückblick
**Johannes' Beiträge Zusammenfassung:**
- ✅ Projekt-Initialisierung
- ✅ Infrastruktur (BaseToolActivity, Grid)
- ✅ Settings-System (Darkmode, Grid-Columns)
- ✅ Merge-Management & Dokumentation
- ✅ Design-System

**Erkenntnisse:**
- Gute Architektur spart Zeit
- Konsistente UX wichtig für Akzeptanz
- Dokumentation von Anfang an

---

# TEIL 2: MATTHIAS REPS
## (15 Folien)

---

### Folie 16: Matthias' Fokusbereiche
**Hauptbeiträge:**
- Counter Tool Implementierung
- Light Tool (Taschenlampe)
- Internationalization (I18n) System
- Decibel Meter (Dezibelmesser)
- Spirit Level (Wasserwaage)

**Ziel:**
- Konkrete Funktionalität implementieren
- Sensor-Integration
- Benutzeroberflächen realisieren

---

### Folie 17: Counter Tool
**Zähler-Implementierung:**
- Inkrement/Dekrement Buttons
- Großes Zahlen-Display (120sp)
- Reset-Funktionalität
- Double-Tap zum Reset

**UX-Features:**
- GestureDetector für Double-Tap
- ClickableTextView für zentralen Button
- Material Design Buttons (+/-)
- Große Touch-Targets

---

### Folie 18: Counter - Technical Details
**Technische Aspekte:**
- State Management in Activity
- OnClickListener Setup
- GestureDetector.SimpleOnGestureListener
- Text-Updates bei Klick

**Challenges:**
- Double-Tap vs. Single-Tap Unterscheidung
- Performanz bei schnellen Klicks
- Layout in verschiedenen Bildschirmgrößen

---

### Folie 19: Light Tool (Taschenlampe)
**Flashlight-Implementierung:**
- CameraManager API
- setTorchMode() Methode
- Kamera-ID Detection
- Toggle-Button (200x200dp kreisförmig)

**Sicherheitsfeatures:**
- Auto-Ausschalten in onPause()
- Exception Handling für Kamera
- User Feedback (Farbwechsel)

---

### Folie 20: Light - Camera Handling
**Kamera-Integration:**
- CAMERA_SERVICE Zugriff
- CameraIdList Iteration
- Torch Mode Verfügbarkeit prüfen

**UI-States:**
- EIN: Blauer Hintergrund, weißer Text
- AUS: Grauer Hintergrund, schwarzer Text
- Smooth Transition zwischen States

---

### Folie 21: Decibel Meter (Teil 1)
**Dezibelmesser-Konzept:**
- AudioRecord für Mikrofon-Aufnahme
- RMS (Root Mean Square) Berechnung
- Dezibel-Formel: 20 * log10(RMS)
- Moving Average für Glättung

**Sample-Rate:**
- 44.1 kHz für gute Auflösung
- Mono-Kanal ausreichend
- PCM_16BIT Encoding

---

### Folie 22: Decibel Meter (Teil 2)
**Visualisierung:**
- Aktuellen dB-Wert groß anzeigen
- 5 dB-Bereiche highlighten (0-45, 45-60, 60-80, 80-115, 115+)
- Statistiken über 10 Sekunden (Min, Max, Average)
- ArrayDeque für Zeitfenster

**Bereiche:**
- Farbe: Alpha-Wert für Highlight
- Text-Labels für dB-Bereiche
- Statistik-Anzeige

---

### Folie 23: Decibel Meter - Performance
**Optimierungen:**
- Background Thread für Aufnahme
- Handler für UI-Updates (Main Thread)
- Buffer-Management
- Effiziente RMS-Berechnung

**Herausforderungen:**
- Mikrofon-Permission Handling
- Real-time Performance
- Thread-Sicherheit bei UI-Updates

---

### Folie 24: Spirit Level (Wasserwaage)
**Wasserwaage-Implementierung:**
- SensorManager für Accelerometer
- X- und Y-Achse für Neigung
- Winkelberechnung: atan2(x, y)
- Visuelle Blase

**Visualisierung:**
- Kreis als Rahmen
- Blase verschobener Kreis
- Position abhängig vom Neigungswinkel
- Grün wenn +/- 1°, sonst rot

---

### Folie 25: Spirit Level - Sensor Fusion
**Sensor-Architektur:**
- TYPE_ACCELEROMETER verwenden
- SensorEventListener Implementierung
- Sensor-Speed optimiert (Fastest)
- Filterung für Glättung

**Precision:**
- Winkel mit 1 Nachkommastelle
- Status-Text ("Waagerecht" wenn platt)
- Dynamische Farbumschaltung

---

### Folie 26: Internationalization (I18n)
**Mehrsprachigkeits-System:**
- Runtime Language Switching
- `values` und `values-en` Ordner
- SharedPreferences für Sprach-Preference
- ConfigurationContext recreation

**Implementierung:**
- LanguageManager Singleton
- Context-Wrapper
- Locale.forLanguageTag()
- setLocale() auf Configuration

---

### Folie 27: I18n - String Resources
**String-Management:**
- Alle Strings externalisiert
- Format-Strings (%1$d, %1$s)
- Plurals für Mengenangaben
- Konsistente Naming-Konventionen

**Beispiele:**
- `tool_name`, `settings_language`
- `tools_available` (Plural)
- `siren_start/stop`

---

### Folie 28: Spirit Level Integration
**Merge-Prozess:**
- Feature-Branch `spitit-level`
- Konfliktlösung mit master
- Settings-Integration
- Grid-Columns Kombination

**Ergebnis:**
- Spirit Level in Tool-Grid
- Icon: `ic_spirit_level`
- String: "Wasserwaage" / "Spirit Level"

---

### Folie 29: Counter UI Layout
**Activity Counter Layout:**
- Grid für +/- Buttons (50/50 Split)
- Centered TextView für Counter (0)
- Reset-Button über Mitte
- Bottom-Alignment für Bedienelemente

**Layout-Hierarchie:**
- ConstraintLayout als Root
- Guideline für vertikale Mitte
- Responsive Breite via Constraints

---

### Folie 30: Icon-Integration
**Icon-Struktur:**
- Mipmap-Ordner für Tool-Icons
- HDPI Icons (64x64 typisch)
- Placeholder für fehlende Icons
- Consistenter Style

**Icons erstellt:**
- ic_counter, ic_light, ic_spirit_level
- ic_decibel_meter
- ic_siren (für später)

---

# TEIL 3: ILIA KARPOV (WSHAPER)
## (15 Folien)

---

### Folie 31: Ilia's Fokusbereiche
**Hauptbeiträge:**
- Uhr-System (Clock/Alarm/Stopwatch/Timer)
- I18n System gemeinsam mit Matthias
- Siren Tool Implementierung
- UI-Standardisierung
- Merge-Koordination

**Ziel:**
- Zeit-basierte Tools
- Komplette Uhr-Funktionalität
- Audio-Tools

---

### Folie 32: Clock - Grundfunktionalität
**Echtzeit-Uhr:**
- System.currentTimeMillis()
- Formatierung: HH:MM:SS
- Update jede Sekunde (Handler)
- Großes Display zentriert

**Layout:**
- Fragment-basierte Navigation
- Bottom Navigation Bar
- 4 Tabs: Clock, Alarm, Stopwatch, Timer

---

### Folie 33: Alarm - Wecker
**Alarm-Implementierung:**
- AlarmManager Integration
- PendingIntent für Alarm-Trigger
- RecyclerView für Alarm-Liste
- Floating Action Button (+)

**Features:**
- Zeit einstellen (TimePicker)
- Datum anzeigen
- Lösch-Button pro Alarm
- "Keine Wecker eingestellt" Placeholder

---

### Folie 34: Stopwatch - Stoppuhr
**Stoppuhr-Funktionalität:**
- Start/Stop/Reset Buttons
- Millisekunden-Präzision
- Format: MM:SS:ms
- ElapsedTime Berechnung

**Performance:**
- Handler für regelmäßige Updates
- Start-Zeit speichern
- Stop-Zeit berechnen
- Formatierung zu String

---

### Folie 35: Timer - Countdown
**Timer-Implementierung:**
- Start-Zeit eingeben
- Countdown-Logik
- Zero-Reach Handler
- Alarm beim Ablauf

**UI-Elemente:**
- Button zum Starten
- Anzeige verbleibender Zeit
- Reset-Funktionalität
- Visual Feedback beim Ablauf

---

### Folie 36: Clock Fragment-Architektur
**Navigation-Implementierung:**
- Bottom Navigation Bar
- FragmentManager für Tab-Switching
- Menu-XML für Navigation-Items
- Icons für Tabs

**Fragmente:**
- ClockFragment (Aktuelle Uhr)
- AlarmFragment (Alarme verwalten)
- StopwatchFragment (Stoppuhr)
- TimerFragment (Countdown)

---

### Folie 37: Bottom Navigation Setup
**Navigation-Konfiguration:**
- `menu/bottom_nav_clock.xml`
- 4 Items mit Icons
- OnItemSelectedListener
- Fragment Transaction

**Icons:**
- ic_clock, ic_alarm
- ic_stopwatch, ic_timer
- Vector Drawables für Skalierbarkeit

---

### Folie 38: Siren - Grundkonzept
**Siren-Implementierung:**
- AudioTrack API (nicht Sound-Dateien)
- Programmatic Sound Generation
- Zwei Patterns: Loud Tone, SOS
- Start/Stop Toggle-Button

**Vorteile:**
- Keine externen Ressourcen
- Runtime-Anpassung möglich
- Kleinere APK-Größe

---

### Folie 39: Siren - Loud Tone
**Lauter Ton Modus:**
- Kontinuierliche Sinuswelle
- Frequenz: 500-2500 Hz (einstellbar)
- Volumen: 0-100% (einstellbar)
- AudioAttributes.USAGE_ALARM

**Implementation:**
- Coroutine für Audio-Loop
- Buffer-Generierung mit Sinus
- Real-time Parameter-Updates

---

### Folie 40: Siren - SOS Pattern
**Morse-Code Implementierung:**
- SOS: · · · — — — · · ·
- Timing: Short 200ms, Long 400ms
- Gaps: 200ms (inter), 400ms (letter), 800ms (word)
- Endlosschleife

**Technical:**
- Tone/Silence abwechselnd
- Frequenz aus Slider übernommen
- Coroutine suspend functions

---

### Folie 41: Siren UI
**Siren-Interface:**
- Großer kreisförmiger Toggle-Button (200x200dp)
- RadioGroup für Pattern-Auswahl
- SeekBar für Frequenz (500-2500 Hz)
- SeekBar für Lautstärke (0-100%)
- Status-Indicator

**Labels:**
- Format-Strings (%1$d Hz, %1$d%%)
- Dynamisch bei Slider-Bewegung

---

### Folie 42: Siren - Audio Lifecycle
**Audio-Management:**
- AudioTrack.play() im Coroutine
- stop/release in onPause()
- stop/release in onDestroy()
- Verhindert Audio nach Activity-Verlassen

**Bug Fix:**
- Initialisierung der Display-Werte
- TextViews zeigten "%1$d Hz" statt "1000 Hz"
- `initializeDisplayValues()` Methode

---

### Folie 43: Siren - Technical Implementation
**Audio-Spezifikationen:**
- SampleRate: 44.1 kHz
- Mono-Kanal
- PCM_16BIT Encoding
- Buffer: sampleRate/10 (100ms)

**Pattern-Switch:**
- Bei Pattern-Wechsel: Stop → Start neu
- Verhindert Audio-Artefakte
- Smooth Transition

---

### Folie 44: I18n - Runtime Switching
**Language-Management:**
- PandoraApplication Context-Wrapper
- attachBaseContext() Override
- SharedPreferences KEY_LANGUAGE
- Locale.forLanguageTag()

**Supported:**
- Deutsch (default)
- English (Englisch)
- Erweiterbar für mehr Sprachen

---

### Folie 45: Projektstatus - Gesamtbild

**Erledigte Tools (9/15):**
✅ Lampe (Johannes/Matthias)
✅ Zähler (Matthias)
✅ Uhr (Johannes/Ilia) - mit 4 Fragments
✅ Rechner (Placeholder)
✅ Winkelmesser (Placeholder)
✅ Wasserwaage (Matthias)
✅ Dezibelmesser (Matthias)
✅ Kompass (Placeholder)
✅ Sirene (WSHAPER)

**Noch zu tun (6/15):**
⏳ Lineal (Placeholder)
⏳ Lupe (Placeholder)
⏳ Sprachaufnahme (Placeholder)
⏳ Lumenmesser (Placeholder)
⏳ Kamera (Placeholder)
⏳ Audiospektrum (Implementiert aber reverted)

**Infrastruktur (100%):**
✅ BaseToolActivity (Johannes)
✅ Settings-System (Johannes)
✅ I18n-System (Ilia/Matthias)
✅ Grid-Layout (Johannes)
✅ Darkmode (Johannes)
✅ Header-Layout (Johannes)

---

### Folie 46: Architektur-Überblick
**MVVM-Architektur (angestrebt):**
- Presentation Layer: Activities/Fragments
- Domain Layer: Tool-Logic
- Data Layer: Sensors/System Services

**Aktuelle Struktur:**
- Views in Activities (keine ViewModels)
- Business Logic in Activities
- Sensors direkt in Activities

---

### Folie 47: Herausforderungen & Lessons Learned

**Challenges:**
1. **Audio-Permission Handling** (Decibel, Siren)
   - Runtime Permission Request nötig
   - Denied-Fall abfangen

2. **Fragment-Navigation** (Clock)
   - Back Button Management
   - State Preservation

3. **Audio-Synthesis** (Siren)
   - Coroutine vs Thread Entscheidung
   - Buffer-Größen optimieren

4. **I18n Runtime-Switching**
   - Context recreation tricky
   - Activity Restart nötig

---

### Folie 48: Code-Quality & Standards

**Angewandte Standards:**
- Material Design 3 Richtlinien
- Accessibility (Screen Reader, Fokus)
- Kotlin Style Guide
- Consistent Naming

**Best Practices:**
- Kommentare in Code
- Klare Commit-Messages
- Feature-Branches
- Code-Review via PRs

---

### Folie 49: Performance-Optimierungen

**Maßnahmen:**
- Coroutines für Background-Arbeit
- Handler für UI-Updates
- Efficient Buffer-Management
- View Recycling (RecyclerView)

**Messungen:**
- Decibel: ~60 FPS
- Clock: Smooth second updates
- Siren: No lag in audio loop

---

### Folie 50: Testing-Strategie

**Geplante Tests:**
- Unit Tests für Rechner
- Unit Tests für Zähler-Logik
- Instrumented Tests für UI
- Sensor-Mock für Tests

**Challenges:**
- Sensor-Tests schwierig ohne Device
- Audio-Tests require Hardware
- Fragment-Test komplex

---

### Folie 51: Build-Konfiguration

**Gradle-Setup:**
- Kotlin 2.0
- Android Gradle Plugin 8.9
- Dependencies via Version Catalog
- Multi-variant Builds (debug/release)

**Build-Stats:**
- APK Größe: ~15 MB (debug)
- Build Zeit: ~30 Sekunden
- Dependencies: 4 (AndroidX Core, AppCompat, Material, ConstraintLayout)

---

### Folie 52: Deployment & Release

**Release-Vorbereitung:**
- ProGuard Konfiguration
- Signierung der APK
- Versioning (v1.0)
- Release Notes erstellen

**Distribution:**
- GitHub Releases
- APK Upload
- Play Store Vorbereitung

---

### Folie 53: Abgleich mit Vorlesungsanforderungen

| Anforderung | Status |
|-------------|---------|
| Planung | ✅ README.md, Projektplan |
| Systemarchitektur | ✅ MVVM-Architektur |
| Dokumentation | ✅ KI-Einsatz, Kommentare |
| Tests | ⏳ Geplant |
| Funktionale Umsetzung | ✅ 9/15 Tools |
| Git-Workflow | ✅ Feature-Branches, PRs |

---

### Folie 54: KI-Einsatz

**KI verwendet für:**
- Code-Verbesserungen und Hilfestellungen
- Code-Kommentare generieren
- README Dokumentation
- Use-Case Dokumentation
- Bug-Fix-Suche (z.B. Coroutines)

**Wertbeitrag:**
- Beschleunigte Entwicklung
- Bessere Code-Qualität
- Dokumentations-Unterstützung

---

### Folie 55: Nächste Schritte - Priorität 1

**Hochprioritäre Tasks:**
1. ✅ Siren Tool (abgeschlossen)
2. ⏳ Audiospektrum (implementiert, aber reverted)
3. ⏳ Sprachaufnahme implementieren
4. ⏳ Lumenmesser implementieren
5. ⏳ Kamera implementieren

**Zeitschätzung:**
- Je 2-3 Tage pro Tool
- Gesamt: 6-8 Wochen für alle 6 verbleibenden Tools

---

### Folie 56: Nächste Schritte - Priorität 2

**Mittelprioritäre Tasks:**
1. Unit Tests schreiben (Rechner, Zähler)
2. Instrumented Tests für UI
3. ViewModel-Refactoring
4. Dependency Injection einführen
5. Offline-Modus implementieren

**Architektur:**
- ViewModel für State-Management
- LiveData für Observables
- Repository Pattern für Data Layer

---

### Folie 57: Nächste Schritte - Priorität 3

**Niedrigprioritäre Tasks:**
1. Widget Support (Homescreen)
2. Widgets pro Tool
3. Custom Themes erstellen
4. Animationen hinzufügen
5. Sound-Effekte für Feedback
6. Haptic Feedback

**Polish:**
- Transitions verbessern
- Loading States
- Error Handling

---

### Folie 58: Risiken & Mitigation

**Identifizierte Risiken:**
1. **Kompatibilität** (verschiedene Android-Versionen)
   - Lösung: MinSdk 31, Test auf Geräten

2. **Performance** auf Low-End Devices
   - Lösung: Profiling, Optimierung

3. **Audio-Permissions** (User-Denial)
   - Lösung: Fallback UI, Erklärung

4. **Battery Drain** (Sensoren im Hintergrund)
   - Lösung: Stop-Sensors in onPause

---

### Folie 59: Abschlusspräsentation - Summary

**Projektstatus:**
- ✅ Solide Grundlage gelegt
- ✅ 9 von 15 Tools implementiert
- ✅ Infrastruktur vollständig
- ✅ I18n implementiert
- ⏳ 6 Tools noch offen
- ⏳ Testing-Phase beginnt

**Team-Leistung:**
- Johannes: Infrastruktur & Settings
- Matthias: Concrete Tools (Counter, Light, Decibel, Spirit)
- Ilia: Clock-System & Audio-Tools

**Nächste Meilensteine:**
1. Fehlende Tools implementieren
2. Tests schreiben
3. Release v1.0 vorbereiten
4. Dokumentation finalisieren

---

### Folie 60: Q&A

**Fragen?**

- Zu welchem Tool gibt es Fragen?
- Wie geht es mit Audiospektrum weiter?
- Soll Priority auf Testing gelegt werden?
- Timeline für Release v1.0?

**Kontakt:**
- Johannes Lehmann: johanneslehmann19@gmail.com
- Matthias Reps: matthias.reps@iu-study.org
- Ilia Karpov: 42714629+WSHAPER@users.noreply.github.com

---

# Vielen Dank für Ihre Aufmerksamkeit!
