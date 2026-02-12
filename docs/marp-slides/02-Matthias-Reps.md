---
marp: true
theme: modern-iu
paginate: true
footer: Werkzeugkasten | Matthias Reps | 16.02.2026
---

# Werkzeugkasten<br>(Kasten der Pandora)

## Projektstatus - Februar 2026

1. Johannes Lehmann
2. **Matthias Reps**
3. Ilia Karpov

---

### Matthias' Fokusbereiche
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

### Counter Tool
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

### Counter - Technical Details
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

### Light Tool (Taschenlampe)
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

### Light - Camera Handling
**Kamera-Integration:**
- CAMERA_SERVICE Zugriff
- CameraIdList Iteration
- Torch Mode Verfügbarkeit prüfen

**UI-States:**
- EIN: Blauer Hintergrund, weißer Text
- AUS: Grauer Hintergrund, schwarzer Text
- Smooth Transition zwischen States

---

### Decibel Meter (Teil 1)
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

### Decibel Meter (Teil 2)
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

### Decibel Meter - Performance
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

### Spirit Level (Wasserwaage)
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

### Spirit Level - Sensor Fusion
**Sensor-Architektur:**
- `TYPE_ACCELEROMETER` verwenden
- SensorEventListener Implementierung
- Sensor-Speed optimiert (Fastest)
- Filterung für Glättung

**Precision:**
- Winkel mit 1 Nachkommastelle
- Status-Text ("Waagerecht" wenn platt)
- Dynamische Farbumschaltung

---

### Internationalization (I18n)
**Mehrsprachigkeits-System:**
- Runtime Language Switching
- `values` und `values-en` Ordner
- SharedPreferences für Sprach-Preference
- ConfigurationContext recreation

**Implementierung:**
- LanguageManager Singleton
- Context-Wrapper
- `Locale.forLanguageTag()`
- `setLocale()` auf Configuration

---

### I18n - String Resources
**String-Management:**
- Alle Strings externalisiert
- Format-Strings (`%1$d`, `%1$s`)
- Plurals für Mengenangaben
- Konsistente Naming-Konventionen

**Beispiele:**
- `tool_name`, `settings_language`
- `tools_available` (Plural)
- `siren_start/stop`

---

### Spirit Level Integration
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

### Counter UI Layout
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

### Icon-Integration
**Icon-Struktur:**
- Mipmap-Ordner für Tool-Icons
- HDPI Icons (64x64 typisch)
- Placeholder für fehlende Icons
- Consistenter Style

**Icons erstellt:**
- `ic_counter`, `ic_light`, `ic_spirit_level`
- `ic_decibel_meter`
- `ic_siren` (für später)

---

# Übergabe an Ilia Karpov