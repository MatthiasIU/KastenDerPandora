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
- UI-Standardisierung & Merge-Koordination
**Ziel:** Zeit-basierte Tools, Audio-Tools, komplette Uhr-Funktionalität

---

<!--header: Uhr-System-->

#### Übersicht


**Fragment-basierte Architektur mit Bottom Navigation:**

<style scoped> table { font-size: 18px; } </style>

| Fragment | Funktion |
|----------|----------|
| `ClockFragment` | Echtzeit-Uhr (HH:MM:SS), Handler-Updates |
| `AlarmFragment` | `AlarmManager`, `RecyclerView`, `TimePicker` |
| `StopwatchFragment` | Start/Stop/Reset, Millisekunden-Präzision |
| `TimerFragment` | Countdown-Logik, Alarm beim Ablauf |

**Navigation:**
- `menu/bottom_nav_clock.xml`
- `FragmentManager`
- Vector Drawables

---

#### Technische Details

**Echtzeit-Uhr:**
- `System.currentTimeMillis()`
- `Handler` für sekündliche Updates
- Großes Display zentriert


**Alarm:**
- `PendingIntent` für Trigger
- FAB zum Hinzufügen
- Lösch-Button pro Alarm

**Stopwatch/Timer:**
- `ElapsedTime` Berechnung
- Zero-Reach Handler
- Visual Feedback

---

<!--header: ''-->

### Sirene
#### Konzept & Patterns

**`AudioTrack` API (programmatische Sound-Generierung):**

| Pattern | Beschreibung |
|---------|--------------|
| Loud Tone | Kontinuierliche Sinuswelle, 500-2500 Hz |
| SOS | Morse: · · · — — — · · · (200ms/400ms Timing) |

**Vorteile:** Keine externen Ressourcen, Runtime-Anpassung, kleinere APK

---

<!--header: Sirene-->

#### UI & Implementation
**Interface:**
- Toggle-Button (200x200dp kreisförmig)
- `RadioGroup` für Pattern-Auswahl
- `SeekBar`: Frequenz (500-2500 Hz), Lautstärke (0-100%)

**Audio-Specs:**
- 44.1 kHz, Mono, `PCM_16BIT`
- Coroutine für Audio-Loop
- `stop()`/`release()` in `onPause()`/`onDestroy()`

---

<!--header: i18n-->

### I18n - Runtime Switching
**Language-Management:**
- `PandoraApplication` Context-Wrapper
- `attachBaseContext()` Override
- `SharedPreferences` `KEY_LANGUAGE`
- `Locale.forLanguageTag()`

**Supported:** Deutsch (default), English – erweiterbar

---

<!--header: ''-->

### Projektstatus - Gesamtbild

<div class="columns">
<div>

**Erledigte Tools (9/15):**
- ✅ Lampe, Zähler, Uhr (4 Fragments)
- ✅ Rechner, Winkelmesser, Wasserwaage
- ✅ Dezibelmesser, Kompass, Sirene

**Noch offen (6/15):**
- ⏳ Lineal, Lupe, Sprachaufnahme
- ⏳ Lumenmesser, Kamera, Audiospektrum

</div>
<div>

**Infrastruktur (100%):**
✅ `BaseToolActivity`
✅ Settings-System
✅ I18n-System
✅ Grid-Layout
✅ Darkmode
✅ Header-Layout

</div>
</div>

---

### Architektur-Überblick
**MVVM-Architektur (angestrebt):**
- Presentation Layer: Activities/Fragments
- Domain Layer: Tool-Logic
- Data Layer: Sensors/System Services

**Aktuelle Struktur:**
- Views in Activities (keine `ViewModels`)
- Business Logic in Activities
- Sensors direkt in Activities

---

### Herausforderungen & Lessons Learned

| Challenge | Lösung |
|-----------|--------|
| Audio-Permission Handling | Runtime Permission Request, Denied-Fall abfangen |
| Fragment-Navigation | Back Button Management, State Preservation |
| Audio-Synthesis | Coroutines, Buffer-Größen optimieren |
| I18n Runtime-Switching | Context recreation, Activity Restart |

---

### Code-Quality & Performance
**Standards:**
- Material Design 3, Accessibility
- Kotlin Style Guide, Consistent Naming
- Feature-Branches, Code-Review via PRs

**Optimierungen:**
- Coroutines für Background-Arbeit
- `Handler` für UI-Updates
- Efficient Buffer-Management
- ~60 FPS (Decibel), No lag (Siren)

---

### Build & Deployment
**Gradle-Setup:**
- Kotlin 2.0, AGP 8.9
- APK: ~15 MB, Build: ~30s
- 4 Dependencies (AndroidX, Material)

**Release-Vorbereitung:**
- ProGuard, APK-Signierung
- GitHub Releases
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
**Verwendet für:**
- Code-Verbesserungen und Hilfestellungen
- Code-Kommentare generieren
- README & Use-Case Dokumentation
- Bug-Fix-Suche (z.B. Coroutines)

**Wertbeitrag:** Beschleunigte Entwicklung, bessere Code-Qualität

---

### Nächste Schritte

| Priorität | Tasks |
|-----------|-------|
| **Hoch** | Audiospektrum, Sprachaufnahme, Lumenmesser, Kamera |
| **Mittel** | Unit Tests, `ViewModel`-Refactoring, Dependency Injection |
| **Niedrig** | Widget Support, Custom Themes, Animationen |

**Zeitschätzung:** 2-3 Tage pro Tool → 6-8 Wochen für alle

---

### Risiken & Mitigation
| Risiko | Mitigation |
|--------|------------|
| Android-Kompatibilität | `minSdk 31`, Test auf Geräten |
| Performance (Low-End) | Profiling, Optimierung |
| Audio-Permission Denial | Fallback UI, Erklärung |
| Battery Drain | Stop-Sensors in `onPause()` |

---

### Summary
**Projektstatus:**
- 9/15 Tools implementiert, Infrastruktur 100%
- I18n implementiert, 6 Tools noch offen

**Team-Leistung:**
- Johannes: Infrastruktur & Settings
- Matthias: Counter, Light, Decibel, Spirit Level
- Ilia: Clock-System & Audio-Tools

**Nächste Meilensteine:** Fehlende Tools → Tests → Release v1.0

---

# Vielen Dank für Eure Aufmerksamkeit!

### Q&A
**Fragen?**

**Kontakt:**
- Johannes Lehmann: johanneslehmann19@gmail.com
- Matthias Reps: matthias.reps@iu-study.org
- Ilia Karpov: ilia.karpov.96@gmail.com