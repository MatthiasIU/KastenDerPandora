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
Willkommen zu unserer Projektpräsentation. Ich bin Johannes und stelle zuerst die Infrastruktur und Basis-Architektur vor.
-->

---
### Projektübersicht
<!--
Erstmal eine kurze Einführung ins Projekt.

Das Projekt haben wir "Kasten der Pandora" getauft und ist im Prinzip ein digitaler Werkzeugkasten. Die Idee ist, mehrere Alltagswerkzeuge in einer zentralen App zu bündeln, statt für jede Kleinigkeit eine eigene App zu haben.

Als Oberfläche haben wir eine Rasteransicht, ähnlich wie ein Android-Homescreen, damit man die Tools schnell findet und direkt starten kann.

Wichtig ist außerdem der modulare Aufbau, damit später neue Werkzeuge unkompliziert ergänzt werden können, ohne die bestehende App jedes Mal groß umbauen zu müssen.

Technisch setzen wir das mit Android Studio und in Kotlin um.
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
<!--
Jetzt erzähle ich wie wir das Projekt begonnen haben.

Als erstes zur Projektinitialisierung.
Als erstes haben wir ein GitHub-Repository bei Matthias erstellt und da rein dann ein leeres Android-Studio-Projekt und eine ReadME gepusht.
Dann haben wir uns erste Notizen, Vorstellungen und unsere Planung in der Lucid.app aufgeschrieben. Dort haben wir festgehalten welche Funktionen unsere App haben soll und was dafür benötigt wird.
Die ersten Icons für die App an sich und die einzelnen Anwendungen haben wir auch schon relativ früh eingebaut.
Danach haben wir uns um die git-ignore gekümmert, dass wir keine build- oder ide-Dateien mit ins Repo mit hochladen.
Das Ziel von dem Ganzen war vor allem eine solide Grundlage für Teamarbeit zu schaffen: klare Code-Konventionen, ein einheitlicher Aufbau und eine dokumentierte Architektur, damit jeder im Team schnell reinkommt und wir langfristig sauber weiterentwickeln können.

Jetzt zur Infrastruktur.
Hier haben wir eine Basisklasse eingeführt, die für alle Tools als gemeinsame Grundlage dient.
Die liefert uns einen gemeinsamen Header, also eine einheitliche Kopfzeile für alle Tools, und sorgt gleichzeitig für eine konsistente Navigation.
Dazu gehört auch ein Zurück-Button, damit man nicht pro Tool wieder eine eigene Logik bauen muss.
Außerdem unterstützen wir Window Insets, damit Edge-to-Edge sauber funktioniert, also dass die Inhalte korrekt mit Statusbar und Navigationbar umgehen.
Die Vorteile sind, dass wir Code-Duplizierung vermeiden; wir bekommen ein einheitliches UI-Verhalten über die ganze App hinweg, und wir machen es insgesamt deutlich einfacher, später neue Tools zu ergänzen, weil wir auf dieser Basis einfach aufbauen können.
-->
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
Die BaseToolActivity ist unsere Basisklasse, welche ich vorhin angesprochen hatte. Du durch die verhindern wir Code-Duplizierung und sorgen für ein einheitliches Verhalten.
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
Hier sieht man die wichtigsten Methoden aus der Basisklasse und wofür wir sie benutzen.

Erstens `setupToolContent()`. Die Methode ist dafür da, dass jedes Tool sie überschreiben kann, um sein UI zu initialisieren. Also alles, was tool-spezifisch ist, kommt da rein, ohne dass wir die ganze Activity-Struktur jedes Mal neu bauen.

Zweitens `onBackButtonPressed()`. Auch die ist zum Überschreiben gedacht, wenn ein Tool eine eigene Zurück-Logik braucht. Zum Beispiel wenn man erst einen Dialog schließen will oder innerhalb eines Tools eine interne Navigation hat, bevor man wirklich aus dem Tool rausgeht.

Und drittens `dispatchTouchEvent()`. Die Methode hängt sich in die Touch-Events rein und leitet sie an die Wisch-Erkennung weiter. Damit können wir Gesten zentral auswerten, ohne dass jedes Tool selber Touch-Handling implementieren muss.
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
Jetzt komme ich zu unserem Placeholder-System.
Die Idee dahinter war, dass wir sehr früh die Navigation und das Grundgefühl der App testen können, auch wenn die einzelnen Tools noch nicht fertig implementiert sind.
Am Anfang haben wir haben wir für alle geplanten Tools Placeholder-Activities angelegt.
Auf dem Homescreen habe ich eine Grid-basierte Übersicht erstellt, welche über ein responsives Icon-Sizing verfügt.
Nach und nach haben wir dann unsere Platzhalter-Activities mit den echten Tools ersetzt wie zum Beispiel einer Lampe, einer Wasserwaage und einem Kompass.
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
Jetzt komme ich zu unserer Settings-Activity.
Ich habe eine zentrale Einstellungsseite erstellt. Dabei läuft die Persistenz über SharedPreferences, damit die gespeicherten Einstellungen nach dem schließen der App nicht verlorengehen sondern, beim nächsten Start wieder verfügbar sind.
Außerdem kann man hier zwischen Deutsch und Englisch, der Anzahl der Spalten des Rasters auf dem Homescreen und dem Darkmode auswählen.
Auf dem Screenshot sieht man auch das finale Design von der Seite.
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
Jetzt zur Darkmode-Implementierung.
Hier sieht man in den zwei Screenshots einmal den Light Mode und den Dark Mode. Die besondere Herausforderung dabei war vor allem, dass die Kontraste von Farben und Texten passen.
Hier sieht man nicht nur den Darkmode sondern auch die Rasteränderungen. Auf dem linken ist das Raster in den Einstellungen auf 4 gesetzt während auf dem rechten Bild das Raster 3 ist.
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
Jetzt komme ich kurz zu den Grid-Columns-Einstellungen.
Dabei kann man zwischen 2 und 6 Spalten wählen. Die Größe der Icons wird automatisch angepasst und wie bereits erwähnt wird auch dies in SharedPreferences gespeichert.
-->
**Anpassbare Raster-Spalten:**
- 2-6 Spalten wählbar
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
Als nächstes ist das Merge-Management.
Das war bei uns ein wichtiger Punkt, weil wir mehrere Feature-Branches parallel hatten und die Koordination entscheidend war, damit die Zusammenarbeit reibungslos läuft. Dabei haben wir mit GitHub und Pull-Requests gearbeitet.
Konkret haben wir mit Feature-Branches gearbeitet, zum Beispiel `settings_view`, `clock` und weitere. Diese Branches wurden systematisch gemanagt, also nicht einfach wild zusammengeworfen, sondern Schritt für Schritt über Pull Requests zusammengeführt.
Dabei gehörten natürlich auch Konfliktlösungen dazu. Wir mussten also aufpassen, dass wenn mehrere an einem gearbeitet haben, was aber seltener vorkam, dass keine Konflikte vorkamen oder diese schnell wieder behoben wurden.
Der Ablauf war dann: Feature fertigstellen, Pull Request erstellen, prüfen, und dann mergen.
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
Jetzt kurz zum Button-Positioning und zur User-Experience.
Hier war uns wichtig, dass wir die Material Design Richtlinien einhalten und alle Buttons gut zugänglich machen. 
Dafür haben wir konsistente UI-Elemente geschaffen, indem wir Buttons standardisiert haben. Das heißt: gleiche Positionierung, gleiche Abstände und ein einheitliches Verhalten über die App hinweg.
Bei den konkreten Verbesserungen gehören dazu einmal die Umsetzung nach Material Design Richtlinien, dann Ripple-Effekte für Tap-Feedback, Fokus-Zustände für bessere Bedienbarkeit.
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
Und jetzt zum Header-Layout.
Hier haben wir ein wiederverwendbares Header-Layout gebaut, das wir über den include-Tag einbinden. Der Hauptgrund war, Duplizierung zu reduzieren und Änderungen später deutlich einfacher zu machen, weil wir den Header dann nur an einer Stelle anpassen müssen.
Der Header ist dabei für alle Tools gemeinsam: Der Titel-Text ist dynamisch und wird je Tool gesetzt, damit jedes Tool oben korrekt benannt ist.
Rechts ist das App-Icon platziert, links der Zurück-Button, und das Styling ist überall einheitlich, damit sich die App konsistent anfühlt und man sofort erkennt, dass man innerhalb derselben Anwendung ist.
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
Durch Demo-Activities wurde uns das Testen der App-Struktur bevor echte Tools implementiert waren ermöglicht. Dabei musste keine Funktion der einzelnen Anwendungen implementiert sein damit wir das Grid und die Navigation testen konnten.
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
Unsere Dokumentationsstruktur haben wir in unserem README festgehalten und dies enthält alles Wichtige für neue Entwickler. Darin haben wir zum Beispiel die Projektbeschreibung und Projektplanung notiert, sowie KI-Einsatz transparent dokumentiert.
-->
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
<!--
Jetzt zu unserem Git-Wrokflow.
Wie bereits gesagt haben wir Feature-Branches, Pull-Requests, finale Reviews und merges in GitHub genutzt und durchgeführt.
Außerdem war uns wichtig, dass unsere Commit-Messages nicht nur beschreiben was geändert wurde, sondern vor allem warum.
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
Dann zum Design-System.
Als Grundlage nutzen wir Material Design 3, damit wir von Anfang an ein konsistentes und modernes UI haben.
Dabei geht’s vor allem um visuelle Konsistenz: Wir haben ein Farbschema definiert, also feste Farben für Primär- und Sekundärelemente und für Zustände wie aktiv oder deaktiviert.
Dazu kommen Typographie-Regeln, damit Überschriften, Fließtext und Labels überall gleich wirken und man nicht pro Screen andere Schriftgrößen oder Styles sieht.
Und wir achten auf konsistente Abstände und Größen, also einheitliche Margins, Paddings und Elementgrößen, damit das Layout ruhig und professionell aussieht.
Zusätzlich haben wir uns eine kleine Komponenten-Bibliothek aufgebaut: wiederverwendbare Views, die überall gleich funktionieren.
Das sorgt nicht nur für konsistentes Verhalten, sondern hilft auch bei Barrierefreiheit, weil wir Accessibility-Anforderungen zentral berücksichtigen können, statt sie in jedem Screen neu zu erfinden.
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
Und als letztes eine kurze Zusammenfassung meiner Beiträge in diesem Projekt.
Ich habe mich mit den anderen beiden um die Projektinitialisierung gekümmert.
Außerdem auch mit an der BaseToolActivity und Homepage gearbeitet, sowie das Raster implementiert.
Die Settingsseite zu erstellen, funktionstüchtig zu machen und mein Merge-Management und meine Dokumentation aktuell zu halten war ebenfalls meine Aufgabe.
Als letztes habe ich mich noch mit Anwendungsweitem Verbesserern, testen und der Erstellung der Kompasses-App beschäftigt.
Erkenntnisse die man daraus ziehen kann sind, dass eine gute Architektur und Dokumentation Zeit spart und eine konsistente User Experience wichtig für die Akzeptanz ist.
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
Nun übernimmt Matthias und zeigt seine Tool-Implementierungen.
-->