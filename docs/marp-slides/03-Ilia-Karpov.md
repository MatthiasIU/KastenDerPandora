---
marp: true
theme: modern-iu
paginate: true
footer: Werkzeugkasten | Ilia Karpov | 16.02.2026
---

# Werkzeugkasten (Kasten der Pandora)

## Projektstatus - Februar 2026

1. Johannes Lehmann
2. Matthias Reps
3. **Ilia Karpov**

---

### Ilia's Fokusbereiche
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

### Clock - Grundfunktionalität
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

### Alarm - Wecker
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

### Stopwatch - Stoppuhr
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

### Timer - Countdown
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

### Clock Fragment-Architektur
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

### Bottom Navigation Setup
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

### Siren - Grundkonzept
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

### Siren - Loud Tone
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

### Siren - SOS Pattern
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

### Siren UI
**Siren-Interface:**
- Großer kreisförmiger Toggle-Button (200x200dp)
- RadioGroup für Pattern-Auswahl
- SeekBar für Frequenz (500-2500 Hz)
- SeekBar für Lautstärke (0-100%)
- Status-Indicator

**Labels:**
- Format-Strings (`%1$d Hz`, `%1$d%%`)
- Dynamisch bei Slider-Bewegung

---

### Siren - Audio Lifecycle
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

### Siren - Technical Implementation
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

### I18n - Runtime Switching
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

### Projektstatus - Gesamtbild

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

### Architektur-Überblick
**MVVM-Architektur (angestrebt):**
- Presentation Layer: Activities/Fragments
- Domain Layer: Tool-Logic
- Data Layer: Sensors/System Services

**Aktuelle Struktur:**
- Views in Activities (keine ViewModels)
- Business Logic in Activities
- Sensors direkt in Activities

---

### Herausforderungen & Lessons Learned

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

### Code-Quality & Standards

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

### Performance-Optimierungen

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

### Testing-Strategie

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

### Build-Konfiguration

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

### Deployment & Release

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

### Abgleich mit Vorlesungsanforderungen

| Anforderung | Status |
|-------------|---------|
| Planung | ✅ README.md, Projektplan |
| Systemarchitektur | ✅ MVVM-Architektur |
| Dokumentation | ✅ KI-Einsatz, Kommentare |
| Tests | ⏳ Geplant |
| Funktionale Umsetzung | ✅ 9/15 Tools |
| Git-Workflow | ✅ Feature-Branches, PRs |

---

### KI-Einsatz

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

### Nächste Schritte - Priorität 1

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

### Nächste Schritte - Priorität 2

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

### Nächste Schritte - Priorität 3

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

### Risiken & Mitigation

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

### Abschlusspräsentation - Summary

**Projektstatus:**
- Solide Grundlage gelegt
- 9 von 15 Tools implementiert
- Infrastruktur vollständig
- I18n implementiert
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

### Q&A

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