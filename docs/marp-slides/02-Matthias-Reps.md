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
<!--
- Willkommen zu meinem Teil des Projekts "Werkzeugkasten"
- Heute geben wir ein Update zum Projektstatus im Februar 2026.
-->
---

### Matthias' Fokusbereiche
**Hauptbeiträge:**
- Counter Tool (Zähler)
- Light Tool (Taschenlampe)
- Decibel Meter (Dezibelmesser)
- Spirit Level (Wasserwaage)
- Internationalization (I18n) System zusammen mit Ilia

**Ziel:**
- Konkrete Funktionalität implementieren
- Sensor-Integration
- Benutzeroberflächen realisieren
<!--
- Mein Fokus lag auf der Kernfunktionalität und der Sensorik.
- Ich habe fünf Hauptmodule umgesetzt: Den Zähler, die Taschenlampe, den Dezibelmesser, die Wasserwaage und das globale Internationalisierungssystem.
- Ziel war eine saubere Sensor-Integration gepaart mit intuitiver UI.
-->

---

<style scoped> li { font-size: 22px; } </style>
### Counter Tool
**Zähler-Implementierung:**
- Inkrement/Dekrement Buttons
- Großes Zahlen-Display (120sp)
- Reset-Funktionalität
- Double-Tap zum Reset
![bg left h:80%](https://imgur.com/FvcR1wq.png)

**UX-Features:**
- GestureDetector für Double-Tap
- ClickableTextView für zentralen Button
- Material Design Buttons (+/-)
- Große Touch-Targets
<!--
- Das Counter Tool ist simpel, aber effektiv: Inkrement- und Dekrement-Buttons steuern die Anzeige.
- Die Zahl ist mit 120sp bewusst groß gewählt für beste Lesbarkeit.
- Besonderes UX-Feature: Ein Double-Tap auf das Display setzt den Zähler sofort zurück, ergänzend zum Reset-Button.
-->

---

<style scoped> li { font-size: 22px; } </style>
### Counter<br>Technical Details
**Technische Aspekte:**
- State Management in Activity
- OnClickListener Setup
- GestureDetector.SimpleOnGestureListener
- Text-Updates bei Klick

**Challenges:**
- Double-Tap vs. Single-Tap Unterscheidung
- Performanz bei schnellen Klicks
- Layout in verschiedenen Bildschirmgrößen
  ![bg left h:80%](https://imgur.com/ojClfun.png)
<!--
- Technisch nutzen wir einen `SimpleOnGestureListener`, um zwischen Klicks und Double-Taps zu unterscheiden.
- Die größte Herausforderung war hier das State Management innerhalb der Activity, um auch bei schnellen Klickfolgen keine Events zu verlieren.
-->

---

<style scoped> li { font-size: 22px; } </style>
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
  ![bg left h:60%](https://imgur.com/yL1Xuyy.png)
<!--
- Das Layout basiert auf einem `ConstraintLayout` mit einem 50/50 Split für die Hauptinteraktion.
- Durch Guidelines stellen wir sicher, dass die Buttons auf allen Displaygrößen symmetrisch bleiben und die Touch-Targets groß genug für die einhändige Bedienung sind.
-->

---

<style scoped> li { font-size: 22px; } </style>
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
![bg left h:80%](https://imgur.com/iSqgKcw.png)
<!--
- Die Taschenlampe nutzt die `CameraManager` API.
- Wichtig war hier das Lifecycle-Management: Wenn die App pausiert wird (`onPause`), schaltet sich das Licht automatisch aus, um den Akku zu schonen und die Kamera-Ressource freizugeben.
-->

---

<style scoped> li { font-size: 22px; } </style>
### Light - Camera Handling
**Kamera-Integration:**
- CAMERA_SERVICE Zugriff
- CameraIdList Iteration
- Torch Mode Verfügbarkeit prüfen

**UI-States:**
- EIN: Blauer Hintergrund, weißer Text
- AUS: Grauer Hintergrund, schwarzer Text
- Smooth Transition zwischen States
![bg left h:80%](https://imgur.com/F3Azi1M.png)
<!--
- Wir iterieren über die `CameraIdList`, um die korrekte Hardware-ID für den Flash zu finden.
- Die UI gibt direktes Feedback: Ein blauer Hintergrund signalisiert „An“, Grau steht für „Aus“.
-->

---

<style scoped> li { font-size: 22px; } </style>
### Light UI Layout
**Activity Light Layout:**
- **Circular Toggle:** Zentraler 200dp Button (Material3 Tonal)
- **Status Text:** Subtile Beschriftung unter dem Button (60% Opazität)
- **Alignment:** Vertikal und horizontal mittig zentriert

**Layout-Hierarchie:**
- **ConstraintLayout** (Root mit 16dp Padding)
- **Header** (Top-Alignment)
- **Button** (Circular Shape via ShapeAppearance)
![bg left h:60%](https://imgur.com/jVEOaf2.png)
<!--
- Zentrales Element ist der kreisförmige Toggle-Button mit 200dp Durchmesser.
- Wir nutzen Material3 `ShapeAppearance`, um das moderne, runde Design ohne komplexe XML-Drawables umzusetzen.
-->

---

<style scoped> li { font-size: 22px; } </style>
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
![bg left h:80%](https://imgur.com/1YmZPqD.png)
<!--
- Der Dezibelmesser nutzt `AudioRecord` für Rohdaten-Capturing.
- Die Berechnung erfolgt über den RMS-Wert (Root Mean Square), der dann in die logarithmische Dezibel-Skala umgerechnet wird.
- Ein Moving Average glättet die Werte, damit die Anzeige nicht zu nervös springt.
-->

---

<style scoped> li { font-size: 22px; } </style>
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
![bg left h:80%](https://imgur.com/vmNKBI1.png)
<!--
- Die App kategorisiert Lärm in 5 Bereiche (von leise bis gesundheitsgefährdend).
- Zusätzlich tracken wir Min-, Max- und Durchschnittswerte über ein 10-Sekunden-Fenster mittels einer `ArrayDeque`.
-->

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
<!--
- Audio-Verarbeitung ist rechenintensiv, daher läuft die Aufnahme in einem Background-Thread.
- Die Herausforderung war die Thread-Sicherheit: Nur der Main-Thread darf die UI-Anzeige mit den berechneten Werten aktualisieren.
- Besonderes Augenmerk lag auf dem Request-Flow für das Mikrofon, da die App ohne diese Permission natürlich nicht starten darf.
-->

---

<style scoped> li { font-size: 22px; } </style>
### Decibel Meter - Layout
**Activity Decibel Layout:**
- **Hero-Metrik:** Prominente 72sp Anzeige für dB-Werte
- **Legend-Stack:** Vertikale Liste für Lautstärkebereiche
- **Stats-Footer:** Kompakte Textanzeige für Zusatzdaten

**Layout-Hierarchie:**
- **ConstraintLayout** (Root)
- **Main Display** (Top-Header-Anker)
- **LinearLayout** (Vertikaler Container für dB-Ranges)
![bg left h:60%](https://imgur.com/jXVJD6f.png)
<!--
- Hier dominiert die „Hero-Metrik“ mit 72sp.
- Darunter befindet sich der „Legend-Stack“, der die verschiedenen Lautstärkebereiche farblich visualisiert.
-->

---
<style scoped> li { font-size: 22px; } </style>
### Spirit Level (Wasserwaage)
**Wasserwaage-Implementierung:**
- SensorManager für Accelerometer
- X- und Y-Achse für Neigung
- Winkelberechnung: atan2(x, y)
- Visueller Balken

**Visualisierung:**
- Balken als Anzeige für Neigung
- Position abhängig vom Neigungswinkel
- Grün wenn +/- 1°, sonst blau
![bg right h:80%](https://imgur.com/bgwG2Tk.png)
<!--
- Die Wasserwaage nutzt den Beschleunigungssensor (`Accelerometer`).
- Der „Balken“ wird dynamisch berechnet. Ein cooles Feature: Wenn das Gerät perfekt waagerecht ist (Toleranz +/- 1°), färbt sich der Balken grün.
-->

---
<style scoped> li { font-size: 22px; } </style>
### Spirit Level - Sensor Fusion
**Sensor-Architektur:**
- `TYPE_ACCELEROMETER` verwenden
- SensorEventListener Implementierung
- Sensor-Speed optimiert (Fastest)
- Filterung für Glättung

**Precision:**
- Winkel mit ohne Nachkommastelle
- Verzögern der Aktualisierung (Smoothing)
- Dynamische Farbumschaltung
![bg right h:40%](https://imgur.com/dTAy4Xi.png)
<!--
- Wir nutzen die Sensor-Geschwindigkeit `FASTEST` für direkte Reaktion.
- Ein Tiefpassfilter verhindert, dass leichtes Zittern der Hand die Anzeige unbrauchbar macht.
-->

---

<style scoped> li { font-size: 22px; } </style>
### Spirit Level Integration
**Merge-Prozess:**
- Feature-Branch `spirit-level`
- Konfliktlösung mit master
- Settings-Integration
- Grid-Columns Kombination

**Ergebnis:**
- Spirit Level in Tool-Grid
- Icon: `ic_spirit_level`
- String: "Wasserwaage" / "Spirit Level"
 ![bg left h:60%](https://imgur.com/9StwOMR.png)
<!--
- Die Integration erfolgte über einen eigenen Feature-Branch.
- Das Tool ist nun nahtlos in das Haupt-Grid der App eingegliedert, inklusive lokalisierter Strings und passendem Icon.
-->

---

<style scoped> li { font-size: 22px; } </style>
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
 ![bg left h:60%](https://imgur.com/52vm4Lw.png)
<!--
- Das System unterstützt den Sprachwechsel zur Laufzeit (Deutsch/Englisch).
- Ein `LanguageManager` Singleton verwaltet die Locales und sorgt per Context-Wrapper dafür, dass die App die Sprache ohne kompletten OS-Neustart übernimmt.
-->

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
<!--
- Kein Hardcoding: Alle Texte liegen in `strings.xml`.
- Wir nutzen Platzhalter wie `%1$d` für dynamische Werte und Android `Plurals`, um grammatikalische Fehler (z.B. „1 Werkzeuge“) zu vermeiden.
-->

---

<style scoped> li { font-size: 22px; } </style>
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
 ![bg left h:80%](https://imgur.com/qSZ0X8D.png)
<!--
- Abschließend zur Optik: Alle Icons liegen in den entsprechenden `mipmap`-Ordnern für verschiedene Pixeldichten.
- Dies garantiert eine scharfe Darstellung von HDPI bis XXXHDPI.
-->

---

# Übergabe an Ilia Karpov

<!--
Das war mein Teil der Entwicklung. Ich übergebe nun an Ilia Karpov für die nächsten Projektschritte.
-->
