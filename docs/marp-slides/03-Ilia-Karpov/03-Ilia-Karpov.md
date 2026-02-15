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

<!-- 
SPEAKER NOTES:

Willkommen zur Präsentation des Werkzeugkasten-Projekts.

Ich bin Ilia Karpov und werde heute meinen Teil des Projekts vorstellen.

Wir sind ein Dreierteam - Johannes, Matthias und ich - und arbeiten seit mehreren Wochen an dieser App.

Das Projekt heißt "Kasten der Pandora" - eine Android-App, die verschiedene nützliche Werkzeug-Tools vereint.

Die Idee ist, dass man alltägliche Hilfsmittel wie Taschenlampe, Kompass oder Stoppuhr immer dabei hat.

Ich werde heute speziell auf meine Beiträge eingehen - das Uhr-System und die Audio-Tools.
-->

---

### Ilia's Fokusbereiche
**Hauptbeiträge:**
- Uhr-System
	- Clock/Alarm/Stopwatch/Timer
- I18n System gemeinsam mit Matthias
- Siren Tool Implementierung
- UI-Standardisierung & Merge-Koordination

**Ziel:** Zeit-basierte Tools, Audio-Tools, komplette Uhr-Funktionalität

<!-- 
SPEAKER NOTES:

Meine Hauptverantwortlichkeiten im Projekt umfassen mehrere Bereiche.

Erstens: Das komplette Uhr-System mit vier Fragmenten - Uhr, Alarm, Stoppuhr und Timer.

Das war das umfangreichste Feature, weil es vier verschiedene Funktionalitäten in einer Activity vereint.

Zweitens: Die Internationalisierung zusammen mit Matthias - damit kann man die Sprache zur Laufzeit wechseln.

Drittens: Die Sirene als Audio-Tool - hier habe ich gelernt, wie man Töne synthetisch erzeugt ohne Sounddateien.

Zusätzlich habe ich bei der UI-Standardisierung geholfen, damit alle Tools ein einheitliches Look and Feel haben.

Und natürlich die Merge-Koordination - bei drei Entwicklern entstehen manchmal Konflikte, die gelöst werden müssen.
-->

---

<!--header: Uhr-System-->

#### Übersicht


**Fragment-basierte Architektur mit Bottom Navigation:**

![bg left h:100%](https://i.imgur.com/tlRBPso.jpeg)
![bg left h:100%](https://i.imgur.com/iyshgSK.png)

<style scoped> table { font-size: 17px; } </style>

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

<!-- 
SPEAKER NOTES:

Das Uhr-System ist das Herzstück meiner Arbeit - es besteht aus vier Fragmenten in einer einzigen Activity.

Die Navigation erfolgt über eine Bottom Navigation Bar - genau wie man es von Apps wie YouTube oder Instagram kennt.

Die ClockFragment zeigt die aktuelle Uhrzeit im Format Stunden, Minuten, Sekunden mit sekündlichen Updates über einen Handler.

Die AlarmFragment ist komplexer - sie nutzt den Android AlarmManager, der auch funktioniert wenn die App geschlossen ist.

Alarme werden in einer RecyclerView angezeigt und mit einem TimePicker erstellt.

Stoppuhr und Timer bieten präzise Zeitmessung - die Stoppuhr bis auf Millisekunden genau.

Alle Icons sind Vector Drawables - das bedeutet scharfe Darstellung auf jeder Bildschirmgröße ohne Qualitätsverlust.

Die Navigation ist in einer XML-Datei definiert und wird vom FragmentManager verwaltet.
-->

---

#### Technische Details

**Echtzeit-Uhr:**
- `System.currentTimeMillis()`
- `Handler` für sekündliche Updates
- Großes Display zentriert


![bg left h:100%](https://i.imgur.com/tlRBPso.jpeg)
![bg left h:100%](https://i.imgur.com/iyshgSK.png)

<!-- 
SPEAKER NOTES:

Jetzt zu den technischen Details der Echtzeit-Uhr.

System.currentTimeMillis() liefert die aktuelle Zeit in Millisekunden seit 1970.

Das wird dann in ein lesbares Format umgewandelt und im UI angezeigt.

Ein Handler postet alle 1000 Millisekunden - also jede Sekunde - ein Update für die UI.

Das ist effizienter als ein Timer, weil der Handler mit dem Main Thread synchronisiert ist.

Das Display ist bewusst groß und zentriert gestaltet - für gute Lesbarkeit auch auf einen Blick.

Wichtig für die Batterielaufzeit: Bei onPause() werden die Handler-Callbacks entfernt.

Das bedeutet, wenn die App im Hintergrund ist, verbraucht sie keine Ressourcen für Updates.

Bei onResume() werden die Callbacks dann wieder gestartet.
-->

---

<!--theme:modern-->

**Alarm:**
- `PendingIntent` für Trigger
- FAB zum Hinzufügen
- Lösch-Button pro Alarm

**Stopwatch/Timer:**
- `ElapsedTime` Berechnung
- Zero-Reach Handler
- Visual Feedback

![bg left h:100%](https://i.imgur.com/n5gprZN.png)

![bg left h:100%](https://i.imgur.com/71bV2KN.png)

<!-- 
SPEAKER NOTES:

Die Alarm-Funktionalität nutzt PendingIntents - das ist Android's Mechanismus für zeitverzögerte Aktionen.

Der AlarmManager weckt die App zum eingestellten Zeitpunkt auf, selbst wenn sie nicht läuft.

Das Hinzufügen neuer Alarme erfolgt über einen Floating Action Button - der runde Plus-Button unten rechts.

Ein Klick öffnet den TimePicker, man wählt die Zeit, und der Alarm wird in der Liste angezeigt.

Jeder Alarm hat seinen eigenen Lösch-Button - einfach antippen und er ist weg.

Bei Stoppuhr und Timer ist die Logik ähnlich aber unterschiedlich.

Die Stoppuhr berechnet die ElapsedTime aus der Differenz zwischen Start- und aktueller Zeit.

Der Timer läuft rückwärts - ein Handler checkt kontinuierlich ob die Zeit auf Null gefallen ist.

Wenn ja, wird ein Alarm-Sound abgespielt und das UI zeigt visuelles Feedback.

Beide haben Start, Stop und Reset Buttons mit entsprechenden Zustandsänderungen.
-->

---

<!--header: ''-->

### Sirene
#### Konzept & Patterns

**`AudioTrack` API**

<style scoped> table { font-size: 18px; margin: 0 auto; } </style>

| Pattern | Beschreibung |
|---------|--------------|
| Loud Tone | Kontinuierliche Sinuswelle, 500-2500 Hz |
| SOS | Morse: · · · — — — · · · (200ms/400ms Timing) |

**Vorteile:** Keine externen Ressourcen, Runtime-Anpassung, kleinere APK


![bg left h:100%](https://i.imgur.com/FdeYXW6.png)

<!-- 
SPEAKER NOTES:

Die Sirene ist ein besonders interessantes Tool - hier wird Audio komplett synthetisch erzeugt.

Wir nutzen die AudioTrack API, eine Low-Level Android API für direkte Audio-Ausgabe.

Anders als MediaPlayer, der Dateien abspielt, generiert AudioTrack Samples in Echtzeit.

Zwei Patterns sind implementiert:

Der Loud Tone ist eine kontinuierliche Sinuswelle - mathematisch berechnet, einstellbar von 500 bis 2500 Hertz.

Das SOS-Pattern ist das internationale Morse-Notsignal: drei kurze Töne, drei lange, drei kurze.

Das Timing ist authentisch - kurze Töne 200ms, lange Töne 400ms, mit passenden Pausen dazwischen.

Der große Vorteil dieser Herangehensweise: Keine MP3s oder WAV-Dateien nötig.

Alles wird zur Laufzeit berechnet, was die APK kleiner macht.

Und wir können die Frequenz dynamisch anpassen - der User kann mit einem Slider den Ton verändern.
-->

---

<!--header: Sirene-->

<style scoped> p, li { font-size: 19px; } </style>

#### UI & Implementation
**Interface:**
- Toggle-Button (200x200dp kreisförmig)
- `RadioGroup` für Pattern-Auswahl
- `SeekBar`: Frequenz (500-2500 Hz), Lautstärke (0-100%)

**Audio-Specs:**
- 44.1 kHz, Mono, `PCM_16BIT`
- Coroutine für Audio-Loop
- `stop()`/`release()` in `onPause()`/`onDestroy()`

![bg left h:100%](https://i.imgur.com/FdeYXW6.png)

<!-- 
SPEAKER NOTES:

Das User Interface der Sirene ist bewusst einfach und intuitiv gehalten.

Der zentrale Element ist ein großer runder Toggle-Button - 200 mal 200 dp - zum Ein- und Ausschalten.

Darunter sind RadioButtons für die Pattern-Wahl zwischen Loud Tone und SOS.

Zwei SeekBars ermöglichen die Anpassung von Frequenz und Lautstärke in Echtzeit während die Sirene läuft.

Technisch nutzen wir CD-Qualität: 44.1 Kilohertz Sample-Rate und 16-bit Audio in Mono.

Die Audio-Generierung läuft in einer Kotlin Coroutine im Hintergrund.

Das ist wichtig, weil die Berechnung der Samples den Main Thread blockieren würde.

Mit Coroutines bleibt das UI responsiv während der Ton generiert wird.

Lifecycle-Management ist kritisch: Bei onPause() und onDestroy() wird stop() und release() aufgerufen.

Sonst würde der Ton im Hintergrund weiterspielen - das wäre sehr störend für den User.
-->

---

<!--header: i18n-->

### I18n - Runtime Switching
**Language-Management:**
- `PandoraApplication` Context-Wrapper
- `attachBaseContext()` Override
- `SharedPreferences` `KEY_LANGUAGE`
- `Locale.forLanguageTag()`

**Supported:** Deutsch (default), English – erweiterbar

<!-- 
SPEAKER NOTES:

Das I18n-System - also die Internationalisierung - war eine Zusammenarbeit mit Matthias.

Das Besondere daran: Man kann die Sprache zur Laufzeit umschalten, ohne die App neu zu starten.

Das ist bei Android nicht trivial, weil Ressourcen normalerweise beim Start geladen werden.

Technisch funktioniert das so:

Die PandoraApplication-Klasse - unsere Application-Subklasse - wrappt den Context mit der gewählten Sprache.

Die Methode attachBaseContext() wird überschrieben um die richtige Locale zu setzen.

Die aktuelle Spracheinstellung wird in SharedPreferences unter dem Key "language" gespeichert.

Locale.forLanguageTag() konvertiert den String wie "de" oder "en" zur Android-Locale.

Aktuell unterstützen wir Deutsch als Default und Englisch als Alternative.

Das System ist aber so gebaut, dass man weitere Sprachen einfach hinzufügen kann.

Man braucht nur eine neue strings.xml im entsprechenden values-Ordner.
-->

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

<!-- 
SPEAKER NOTES:

Hier ein Überblick über den Gesamtstatus des Projekts - nicht nur mein Teil, sondern alles.

9 von 15 Tools sind fertig implementiert - das entspricht 60 Prozent.

Die Infrastruktur ist zu 100 Prozent fertig - das war besonders wichtig für eine konsistente User Experience.

Die fertigen Tools umfassen einfache wie Lampe und Zähler, aber auch komplexe wie die Wasserwaage.

Der Dezibelmesser nutzt das Mikrofon, der Kompass den Magnetometer-Sensor.

Mein Uhr-System zählt als vier Funktionen, weil es vier separate Fragmente hat.

Noch offen sind sechs Tools: Lineal, Lupe, Sprachaufnahme, Lumenmesser, Kamera und Audiospektrum.

Sprachaufnahme und Audiospektrum werden technisch anspruchsvoll wegen der Audio-Permissions und FFT-Berechnungen.

Die Kamera braucht Camera2 API - auch nicht trivial.

Die Infrastruktur auf der rechten Seite ist das Fundament: BaseToolActivity, Settings, I18n, einheitliches Layout.
-->

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

<!-- 
SPEAKER NOTES:

Unser ursprüngliches Architektur-Ziel war MVVM - Model-View-ViewModel.

Das ist ein bewährtes Pattern für Android-Apps mit klarer Trennung der Verantwortlichkeiten.

Der Presentation Layer wären Activities und Fragments, der Domain Layer die Tool-Logik, und der Data Layer die Sensoren.

In der Realität haben wir aus Zeitgründen eine einfachere Struktur implementiert.

Die Activities enthalten sowohl die View-Logik als auch die Business Logic - das ist nicht ideal.

ViewModels, die den UI-State halten würden, sind noch nicht implementiert.

Sensoren werden direkt in den Activities angesprochen statt über ein Repository.

Das ist bekannter Technical Debt - wir wissen, dass es verbessert werden sollte.

Der Vorteil: Es funktioniert und war schneller zu entwickeln.

Der Nachteil: Schlechtere Testbarkeit und Wartbarkeit auf lange Sicht.

Das ViewModel-Refactoring steht auf der mittleren Prioritätsliste für die Zukunft.
-->

---

### Herausforderungen & Lessons Learned

| Challenge | Lösung |
|-----------|--------|
| Audio-Permission Handling | Runtime Permission Request, Denied-Fall abfangen |
| Fragment-Navigation | Back Button Management, State Preservation |
| Audio-Synthesis | Coroutines, Buffer-Größen optimieren |
| I18n Runtime-Switching | Context recreation, Activity Restart |

<!-- 
SPEAKER NOTES:

Hier sind einige Herausforderungen, die wir während der Entwicklung überwinden mussten.

Audio-Permissions: Seit Android 6 braucht man Runtime-Permissions für das Mikrofon.

Wir mussten einen sauberen Flow implementieren - Permission anfragen, auf Antwort warten, Ablehnung behandeln.

Wenn der User ablehnt, zeigen wir eine Erklärung warum wir die Permission brauchen.

Fragment-Navigation war knifflig: Der Hardware-Back-Button muss sich richtig verhalten.

Außerdem muss der Fragment-State bei Configuration Changes wie Rotation erhalten bleiben.

Wir nutzen savedInstanceState und den FragmentManager dafür.

Audio-Synthesis mit AudioTrack hatte anfangs Probleme - Knackser und Aussetzer.

Die Lösung war Coroutines für den Background Thread und optimierte Buffer-Größen.

I18n Runtime-Switching: Man muss den Context neu erstellen wenn die Sprache wechselt.

Manchmal ist auch ein Activity Restart nötig damit alle Strings neu geladen werden.
-->

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

<!-- 
SPEAKER NOTES:

Code-Qualität war uns wichtig, auch wenn wir nicht alle Best Practices perfekt umsetzen konnten.

Wir folgen Material Design 3 für ein modernes Look and Feel - das ist Googles aktuelles Design-System.

Accessibility wurde berücksichtigt - Content Descriptions für Screen Reader, ausreichende Kontraste.

Der Kotlin Style Guide gibt uns einheitliche Namenskonventionen und Code-Formatierung.

Alle Code-Changes gingen durch Pull Requests mit Review von mindestens einem anderen Teammitglied.

Performance-seitig haben wir einige Optimierungen gemacht:

Coroutines für alles was nicht auf dem Main Thread laufen sollte - Audio, Sensor-Verarbeitung.

Handler für sichere UI-Updates vom Background Thread - sonst gibt es CalledFromWrongThreadExceptions.

Effizientes Buffer-Management bei der Audio-Generierung um Latenz zu minimieren.

Das Ergebnis: Der Dezibelmesser läuft mit etwa 60 FPS, die Sirene hat keine hörbaren Verzögerungen.
-->

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

<!-- 
SPEAKER NOTES:

Zum Build-System: Wir nutzen die aktuellen Versionen von Kotlin 2.0 und Android Gradle Plugin 8.9.

Das gibt uns Zugang zu den neuesten Sprachfeatures und Build-Optimierungen.

Die APK ist mit etwa 15 Megabyte relativ schlank für eine App mit so vielen Features.

Das liegt daran, dass wir wenige externe Dependencies haben und keine großen Assets wie Sounds brauchen.

Build-Zeit liegt bei ungefähr 30 Sekunden für einen Clean Build - akzeptabel für ein Projekt dieser Größe.

Wir haben nur 4 Dependencies: AndroidX Core, AppCompat, Material Design und ConstraintLayout.

Alles andere ist selbst geschrieben - das reduziert die APK-Größe und potenzielle Sicherheitslücken.

Für den Release ist ProGuard konfiguriert - das obfuskiert den Code und entfernt unbenutzten Code.

Die APK wird mit einem Release-Key signiert.

GitHub Releases nutzen wir für Versioning und um APKs zu verteilen.

Play Store Upload ist technisch vorbereitet, aber noch nicht durchgeführt.
-->

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

<!-- 
SPEAKER NOTES:

Hier der Abgleich mit den Anforderungen aus der Vorlesung - wichtig für die Bewertung.

Planung: Wir haben ein ausführliches README.md und einen Projektplan mit Meilensteinen - das ist erfüllt.

Systemarchitektur: MVVM ist dokumentiert und zumindest konzeptionell vorhanden - erfüllt, auch wenn nicht perfekt umgesetzt.

Dokumentation: Wir dokumentieren den KI-Einsatz transparent und haben Code-Kommentare - erfüllt.

Tests: Das ist der Punkt, der noch offen ist.

Unit Tests und UI Tests sind geplant aber nicht implementiert.

Das ist ein bekanntes Defizit - im echten Projektalltag würde man das vor dem Release nachholen.

Funktionale Umsetzung: 9 von 15 Tools funktionieren vollständig - das entspricht 60 Prozent, also erfüllt.

Git-Workflow: Feature-Branches und Pull Requests werden konsequent genutzt - erfüllt.

Jedes Feature hat seinen eigenen Branch und wird erst nach Review gemergt.
-->

---

### KI-Einsatz
**Verwendet für:**
- Code-Verbesserungen und Hilfestellungen
- Code-Kommentare generieren
- README & Use-Case Dokumentation
- Bug-Fix-Suche (z.B. Coroutines)

**Wertbeitrag:** Beschleunigte Entwicklung, bessere Code-Qualität

<!-- 
SPEAKER NOTES:

Wir haben KI-Tools transparent und dokumentiert eingesetzt - das war eine Anforderung der Vorlesung.

Bei Code-Verbesserungen hat die KI geholfen, bessere Patterns und Idiome vorzuschlagen.

Zum Beispiel bei der Umstellung von Callbacks auf Coroutines - da hat die KI gezeigt wie das elegant geht.

Code-Kommentare wurden teilweise von der KI generiert und dann von uns überprüft und angepasst.

Die README und Use-Case Dokumentation wurde mit KI-Unterstützung erstellt.

Das spart Zeit beim Formulieren und sorgt für konsistente Struktur.

Bei Bug-Fixes war die KI besonders hilfreich - zum Beispiel bei den Coroutine-Problemen mit Audio.

Die KI konnte schnell erkennen was falsch war und Lösungen vorschlagen.

Der Wertbeitrag insgesamt: Wir konnten deutlich schneller entwickeln und die Code-Qualität verbessern.

Wichtig zu betonen: Die KI war ein Werkzeug, die finale Entscheidung und Verantwortung lag immer bei uns.

Wir haben jeden Vorschlag geprüft und verstanden bevor wir ihn übernommen haben.
-->

---

### Nächste Schritte

| Priorität | Tasks |
|-----------|-------|
| **Hoch** | Audiospektrum, Sprachaufnahme, Lumenmesser, Kamera |
| **Mittel** | Unit Tests, `ViewModel`-Refactoring, Dependency Injection |
| **Niedrig** | Widget Support, Custom Themes, Animationen |

**Zeitschätzung:** 2-3 Tage pro Tool → 6-8 Wochen für alle

<!-- 
SPEAKER NOTES:

Die nächsten Schritte sind klar priorisiert nach Wichtigkeit.

Hohe Priorität haben die fehlenden sechs Tools - die müssen fertig werden für ein vollständiges Produkt.

Audiospektrum und Sprachaufnahme sind komplex wegen Audio-Permissions und FFT-Berechnungen.

Das Audiospektrum braucht Fast Fourier Transform um Frequenzen in Echtzeit anzuzeigen.

Lumenmesser und Kamera nutzen Hardware-Sensoren beziehungsweise die Camera2 API.

Mittlere Priorität hat der Technical Debt: Unit Tests schreiben für Regression-Sicherheit.

ViewModels einführen für bessere Architektur und Testbarkeit.

Vielleicht Dependency Injection mit Hilt um die Abhängigkeiten sauber zu verwalten.

Niedrige Priorität sind die Nice-to-haves: Widgets für den Android Homescreen wären cool.

Custom Themes damit User ihre eigenen Farben wählen können.

Animationen für eine flüssigere, angenehmere User Experience.

Zeitschätzung: Etwa 2-3 Tage pro Tool, das ergibt 6-8 Wochen für den kompletten Rest.
-->

---

### Risiken & Mitigation
| Risiko | Mitigation |
|--------|------------|
| Android-Kompatibilität | `minSdk 31`, Test auf Geräten |
| Performance (Low-End) | Profiling, Optimierung |
| Audio-Permission Denial | Fallback UI, Erklärung |
| Battery Drain | Stop-Sensors in `onPause()` |

<!-- 
SPEAKER NOTES:

Wir haben die wichtigsten Risiken identifiziert und Strategien zur Mitigation entwickelt.

Android-Kompatibilität ist immer ein Thema wegen der Fragmentierung.

Wir setzen minSdk auf 31, also Android 12 - damit erreichen wir etwa 85 Prozent der aktiven Geräte.

Das vermeidet viele Legacy-Probleme und gibt uns Zugang zu modernen APIs.

Wir testen auf echten Geräten verschiedener Hersteller, nicht nur auf Emulatoren.

Performance auf Low-End Geräten könnte ein Problem sein, besonders beim Dezibelmesser und Audiospektrum.

Bei Bedarf nutzen wir Android Studio Profiler um Bottlenecks zu finden und gezielt zu optimieren.

Audio-Permission verweigert: Manche User lehnen Mikrofon-Zugriff ab.

Wir zeigen eine Erklärung warum wir die Permission brauchen und haben ein Fallback-UI das trotzdem funktioniert.

Battery Drain ist kritisch bei Sensor-Apps: Alle Sensoren und Audio werden in onPause() sauber gestoppt.

Kein Background-Processing wenn die App nicht im Vordergrund ist.
-->

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

<!-- 
SPEAKER NOTES:

Zum Abschluss eine kurze Zusammenfassung des Projektstatus.

Wir haben 9 von 15 Tools fertig implementiert - das sind 60 Prozent der geplanten Funktionalität.

Die komplette Infrastruktur steht - das ist das Fundament auf dem alles aufbaut.

Das I18n-System funktioniert und ermöglicht Deutsch und Englisch mit einfacher Erweiterbarkeit.

Sechs Tools stehen noch aus, die werden in den nächsten Wochen umgesetzt.

Die Teamaufteilung war klar definiert und hat gut funktioniert:

Johannes hat die Basis gelegt - die Infrastruktur, das Settings-System, die BaseToolActivity.

Matthias hat mehrere Sensor-basierte Tools implementiert - Zähler, Lampe, Dezibelmesser, Wasserwaage.

Ich habe das komplette Uhr-System mit vier Fragmenten übernommen und die Audio-Tools wie die Sirene.

Die Roadmap für die nächste Phase ist klar: Erst die fehlenden sechs Tools fertigstellen.

Dann Unit Tests und UI Tests schreiben um die Qualität abzusichern.

Und schließlich Release Version 1.0 - bereit für den Play Store.
-->

---

# Vielen Dank für Eure Aufmerksamkeit!

### Q&A
**Fragen?**

**Kontakt:**
- Johannes Lehmann: johannes.lehmann@iu-study.org
- Matthias Reps: matthias.reps@iu-study.org
- Ilia Karpov: ilia.karpov.96@gmail.com

<!-- 
SPEAKER NOTES:

Vielen Dank für eure Aufmerksamkeit während meiner Präsentation!

Ich hoffe, ich konnte einen guten Einblick in meine Arbeit am Werkzeugkasten-Projekt geben.

Das Uhr-System mit seinen vier Fragmenten und die Sirene mit synthetischem Audio waren spannende Herausforderungen.

Jetzt bin ich offen für Fragen - zum Projekt allgemein, zur technischen Implementierung, zu den Architektur-Entscheidungen, oder zu irgendwelchen anderen Details.

Fragt gerne auch nach Code-Beispielen oder wie bestimmte Features konkret umgesetzt wurden.

Die Kontaktdaten sind hier auf der Folie zu sehen.

Falls später noch Fragen aufkommen, könnt ihr uns jederzeit per E-Mail erreichen.

Wir freuen uns auch über Feedback zum Projekt!
-->
