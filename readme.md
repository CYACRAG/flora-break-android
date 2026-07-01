# 🌿 Flora Break

```text
███████╗██╗      ██████╗ ██████╗  █████╗     ██████╗ ██████╗ ███████╗ █████╗ ██╗  ██╗
██╔════╝██║     ██╔═══██╗██╔══██╗██╔══██╗    ██╔══██╗██╔══██╗██╔════╝██╔══██╗██║ ██╔╝
█████╗  ██║     ██║   ██║██████╔╝███████║    ██████╔╝██████╔╝█████╗  ███████║█████╔╝
██╔══╝  ██║     ██║   ██║██╔══██╗██╔══██║    ██╔══██╗██╔══██╗██╔══╝  ██╔══██║██╔═██╗
██║     ███████╗╚██████╔╝██║  ██║██║  ██║    ██████╔╝██║  ██║███████╗██║  ██║██║  ██╗
╚═╝     ╚══════╝ ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝
```

## Proaktives Stressmanagement im Arbeitsalltag

**Flora Break** ist eine native Android-App, die Stress im Arbeitsalltag frühzeitig erkennen und Nutzerinnen und Nutzer zu kurzen, gesunden Pausen motivieren soll.

Die App kombiniert simulierte oder später echte Gesundheitsdaten, eine Stressbewertung, individuelle Nutzerdaten und eine Karten-/Routenlogik. Ziel ist es, nicht erst dann zu reagieren, wenn Stress bereits stark spürbar ist, sondern rechtzeitig eine passende Pause vorzuschlagen.

```text
┌────────────────────────────────────────────────────────────────────┐
│                                                                    │
│  Flora Break erkennt Stress nicht nur –                            │
│  Flora Break schlägt direkt eine passende Pause vor.                │
│                                                                    │
│  HRV ↓  Puls ↑  Belastung ↑  →  StressScore  →  Pause  →  Route    │
│                                                                    │
└────────────────────────────────────────────────────────────────────┘
```

---

# Inhaltsverzeichnis

```text
01  Projektidee
02  Problemstellung
03  Zielgruppe
04  Kernfunktionen
05  Aktueller Projektstatus
06  App-Flow
07  Technische Architektur
08  StressEngine
09  Health-Daten & Mock-Daten
10  Demo-Modus
11  Maps & Urban-Walk-Logik
12  Profil-System
13  Feedback-System
14  UI-Konzept
15  Projektstruktur
16  Installation & Setup
17  Git-Workflow
18  Bekannte Einschränkungen
19  Realisierungsfahrplan
20  Team & Rollen
21  Codeaufklärung
```

---

# 01. Projektidee

## Was ist Flora Break?

Flora Break ist eine Android-App für proaktives Stressmanagement.

Die App soll Nutzerinnen und Nutzer im Arbeitsalltag dabei unterstützen, rechtzeitig kleine Pausen einzulegen. Dabei werden Gesundheitsdaten wie HRV und Puls ausgewertet. Daraus berechnet die App einen Stresswert und entscheidet anschließend, ob eine Pause empfohlen werden sollte.

Die Besonderheit ist, dass Flora Break nicht nur sagt:

> „Du bist gestresst.“

Sondern:

> „Dein Stresslevel steigt. Hier ist eine passende Pause, die du jetzt machen kannst.“

```text
       Gesundheitsdaten
              │
              ▼
      ┌───────────────┐
      │ StressEngine  │
      └───────────────┘
              │
              ▼
      StressScore 0–6
              │
              ▼
      Pause empfohlen?
              │
      ┌───────┴────────┐
      ▼                ▼
  Urban Walk       Indoor Break
  mit Route        ohne Route
```

---

# 02. Problemstellung

Stress im Arbeitsalltag entsteht oft schleichend. Viele Menschen merken erst spät, dass sie bereits überlastet sind.

Typische Probleme:

* lange Arbeitsphasen ohne echte Pause
* steigender Puls
* sinkende HRV
* subjektiv hohe Belastung
* mentale Erschöpfung
* fehlende Motivation, selbstständig Pausen einzulegen
* keine direkte Handlungsempfehlung bei Stress

Viele bestehende Lösungen zeigen nur Daten an. Flora Break möchte einen Schritt weitergehen und aus Daten konkrete Empfehlungen ableiten.

```text
Normale Tracking-App:

  Daten anzeigen
       │
       ▼
  Nutzer muss selbst interpretieren


Flora Break:

  Daten analysieren
       │
       ▼
  Stress bewerten
       │
       ▼
  passende Pause vorschlagen
```

---

# 03. Zielgruppe

Flora Break richtet sich vor allem an Menschen, die im Arbeitsalltag regelmäßig Stress erleben und bereits Gesundheitsdaten über eine Smartwatch oder ein kompatibles Wearable erfassen können.

## Primäre Zielgruppe

```text
┌────────────────────────────────────────────┐
│ Büroangestellte                             │
│ Studierende mit hoher Belastung             │
│ Berufstätige im hybriden Arbeiten           │
│ Menschen mit Smartwatch / Wearable          │
│ Teams mit Fokus auf Gesundheit am Arbeitsplatz│
└────────────────────────────────────────────┘
```

## B2B-Perspektive

Flora Break kann perspektivisch auch für Unternehmen interessant sein.

Möglicher Nutzen für Arbeitgeber:

* Förderung der Mitarbeitergesundheit
* niedrigere mentale Belastung
* attraktivere Arbeitsumgebung
* moderne Gesundheitsprävention
* Unterstützung von Pausenkultur
* datenschutzfreundliche, freiwillige Nutzung

---

# 04. Kernfunktionen

Auch wenn die App noch nicht vollständig fertig ist, sind die zentralen Funktionen bereits klar definiert.

```text
┌───────────────────────┬────────────────────────────────────────────┐
│ Funktion              │ Beschreibung                               │
├───────────────────────┼────────────────────────────────────────────┤
│ Profil-Setup          │ Nutzer gibt persönliche Basisdaten ein     │
│ StressEngine          │ Berechnet StressScore aus Gesundheitsdaten │
│ Mock-Daten            │ Simulation ohne echte Smartwatch           │
│ Demo-Modus            │ Stresswerte können per Slider getestet werden│
│ Health Connect        │ Spätere Anbindung echter Gesundheitsdaten  │
│ Urban Walk            │ Prüfung, ob Spaziergang sinnvoll ist       │
│ Maps-Fallback         │ Route oder alternative Pause               │
│ Feedback-System       │ Nutzer bewertet Pausenempfehlung           │
│ Dashboard             │ Anzeige von Stress, Empfehlung und Status  │
└───────────────────────┴────────────────────────────────────────────┘
```

---

# 05. Aktueller Projektstatus

Flora Break befindet sich aktuell im Prototyping- und Integrationsstatus.

Das bedeutet:

* Die App ist noch nicht vollständig final.
* Einige Module arbeiten bereits mit Mock-Daten.
* Die technische Struktur ist vorbereitet.
* Die Stressbewertung ist konzeptionell und teilweise technisch umgesetzt.
* Health Connect ist vorbereitet beziehungsweise in Arbeit.
* Maps-/Routenlogik ist als Mock- oder Fallback-Struktur vorgesehen.
* UI und Profil-Flow werden weiter ausgebaut.

```text
Projektstatus:

[████████████░░░░░░░░]  ca. Prototyp / MVP in Arbeit
```

## Bereits vorhandene oder geplante Bereiche

```text
✅ Native Android-App in Java
✅ Android Studio Projektstruktur
✅ StressEngine-Konzept
✅ MockHealthDataProvider
✅ MockRouteProvider
✅ Datenmodelle für Stress, Route und Empfehlung
✅ Profil-Setup geplant / teilweise vorbereitet
✅ Demo-Modus mit Schiebereglern geplant / teilweise umgesetzt
✅ Health Connect Integration vorbereitet
✅ Urban-Walk-Entscheidung geplant
✅ Feedback-System geplant
⏳ Finale UI-Integration noch in Arbeit
⏳ Echte Smartwatch-Daten noch nicht vollständig produktiv
⏳ Kartenanzeige und echte Routenlogik noch nicht final
```

---

# 06. App-Flow

Der geplante App-Flow ist bewusst einfach gehalten.

Beim ersten Start soll zuerst das Profil ausgefüllt werden. Danach gelangt der Nutzer ins Dashboard.

```text
ERSTER APP-START

┌──────────────┐
│ App starten  │
└──────┬───────┘
       │
       ▼
┌─────────────────────┐
│ Profil vorhanden?   │
└──────┬──────────────┘
       │
   Nein│
       ▼
┌─────────────────────┐
│ Profil-Setup Screen │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│ Profil speichern    │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│ Home / Dashboard    │
└─────────────────────┘
```

```text
NORMALER APP-START

┌──────────────┐
│ App starten  │
└──────┬───────┘
       │
       ▼
┌─────────────────────┐
│ Profil vorhanden?   │
└──────┬──────────────┘
       │
    Ja │
       ▼
┌─────────────────────┐
│ Home / Dashboard    │
└─────────────────────┘
```

---

# 07. Technische Architektur

Flora Break ist modular aufgebaut. Die App soll so entwickelt werden, dass UI, Gesundheitsdaten, Stressberechnung und Maps möglichst sauber getrennt sind.

```text
┌──────────────────────────────────────────────────────────────────┐
│                          Flora Break App                         │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│  UI Layer                                                        │
│  ┌────────────────────────────────────────────────────────────┐  │
│  │ Dashboard | Profil | Feedback | Demo-Slider | Map Screen   │  │
│  └────────────────────────────────────────────────────────────┘  │
│                              │                                   │
│                              ▼                                   │
│  Controller Layer                                                │
│  ┌────────────────────────────────────────────────────────────┐  │
│  │ FloraBreakController                                      │  │
│  │ verbindet UI, StressEngine, HealthDataProvider, RouteProvider│ │
│  └────────────────────────────────────────────────────────────┘  │
│                              │                                   │
│              ┌───────────────┼────────────────┐                 │
│              ▼               ▼                ▼                 │
│  ┌────────────────┐ ┌────────────────┐ ┌────────────────────┐   │
│  │ StressEngine   │ │ Health Provider│ │ Route Provider      │   │
│  │ Score 0–6      │ │ Mock / Real    │ │ Maps / Mock / OSM   │   │
│  └────────────────┘ └────────────────┘ └────────────────────┘   │
│              │               │                │                 │
│              ▼               ▼                ▼                 │
│  ┌────────────────┐ ┌────────────────┐ ┌────────────────────┐   │
│  │ StressResult   │ │ StressData     │ │ RouteResult         │   │
│  └────────────────┘ └────────────────┘ └────────────────────┘   │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

## Ziel der Architektur

Die App soll nicht aus einem einzigen großen Codeblock bestehen. Stattdessen werden einzelne Verantwortlichkeiten getrennt.

```text
UI                 → zeigt Daten an
StressEngine       → berechnet Stress
HealthDataProvider → liefert Gesundheitsdaten
RouteProvider      → liefert Routeninformationen
Controller         → verbindet alles
Models             → transportieren Daten
```

---

# 08. StressEngine

Die StressEngine ist der zentrale technische Kern von Flora Break.

Sie bewertet Gesundheitsdaten und erzeugt daraus einen StressScore.

## Grundidee

Die App nutzt unter anderem HRV-Werte. HRV steht für Heart Rate Variability, also Herzratenvariabilität. Sinkt die HRV im Vergleich zum persönlichen Normalwert, kann das ein Hinweis auf höhere Belastung sein.

Zusätzlich kann der Puls berücksichtigt werden.

```text
Eingangsdaten:

┌────────────────────────┐
│ aktuelle HRV           │
│ normale HRV            │
│ aktueller Puls         │
│ normaler Puls          │
│ subjektive Belastung   │
└────────────────────────┘
          │
          ▼
┌────────────────────────┐
│ StressEngine           │
└────────────────────────┘
          │
          ▼
┌────────────────────────┐
│ StressScore 0–6        │
└────────────────────────┘
```

## StressScore

Der StressScore ist als Skala von 0 bis 6 geplant.

```text
0 = kein auffälliger Stress
1 = sehr niedrig
2 = niedrig
3 = moderat
4 = erhöht
5 = hoch
6 = sehr hoch
```

```text
StressScore Anzeige:

0  [░░░░░░]  entspannt
1  [█░░░░░]  sehr niedrig
2  [██░░░░]  niedrig
3  [███░░░]  moderat
4  [████░░]  erhöht
5  [█████░]  hoch
6  [██████]  sehr hoch
```

## Beispielhafte HRV-Logik

Eine mögliche einfache Berechnung basiert auf dem Verhältnis:

```text
stressRatio = normale HRV / aktuelle HRV
```

Wenn die aktuelle HRV niedriger ist als der persönliche Normalwert, steigt das Verhältnis.

Beispiel:

```text
normale HRV  = 70 ms
aktuelle HRV = 60 ms

stressRatio = 70 / 60
stressRatio = 1,17
```

Je weiter der Wert über 1,0 liegt, desto stärker kann die Belastung interpretiert werden.

```text
Ratio ca. 1,00 → Score 0
Ratio ca. 1,05 → Score 1
Ratio ca. 1,10 → Score 2
Ratio ca. 1,15 → Score 3
Ratio ca. 1,20 → Score 4
Ratio ca. 1,25 → Score 5
Ratio ca. 1,30 → Score 6
```

```text
HRV-Verhältnis:

1.00 ── 1.05 ── 1.10 ── 1.15 ── 1.20 ── 1.25 ── 1.30
  │       │       │       │       │       │       │
  ▼       ▼       ▼       ▼       ▼       ▼       ▼
Score 0 Score 1 Score 2 Score 3 Score 4 Score 5 Score 6
```

---

# 09. Health-Daten & Mock-Daten

Da im Prototyp nicht garantiert ist, dass echte Smartwatch-Daten oder Health-Connect-Daten verfügbar sind, nutzt Flora Break Mock-Daten.

Das ist wichtig, damit die App auch ohne teure Hardware oder echte Sensoren getestet werden kann.

```text
┌────────────────────────────────────────────────────────────┐
│ Warum Mock-Daten?                                           │
├────────────────────────────────────────────────────────────┤
│ - Entwicklung ohne echte Smartwatch möglich                 │
│ - StressEngine kann trotzdem getestet werden                │
│ - UI kann mit realistischen Daten befüllt werden             │
│ - Präsentation funktioniert unabhängig vom Gerät             │
│ - Health Connect kann später ersetzt oder ergänzt werden     │
└────────────────────────────────────────────────────────────┘
```

## Provider-Prinzip

Die App arbeitet mit einem Provider-Prinzip.

```text
HealthDataProvider
        │
        ├── MockHealthDataProvider
        │       liefert simulierte Werte
        │
        └── HealthConnectDataProvider
                liefert später echte Werte aus Health Connect
```

Vorteil:

Die StressEngine muss nicht wissen, woher die Daten kommen.

```text
StressEngine fragt nur:

"Gib mir aktuelle StressData."

Ob diese Daten aus Mock-Daten oder Health Connect kommen,
ist für die StressEngine egal.
```

---

# 10. Demo-Modus

Der Demo-Modus ist besonders wichtig für Präsentation, Entwicklung und Tests.

Mit dem Demo-Modus können Werte wie HRV und Puls manuell simuliert werden.

## Geplante Demo-Werte

```text
┌─────────────────────────┬──────────────────────────────┐
│ Demo-Wert               │ Bedeutung                    │
├─────────────────────────┼──────────────────────────────┤
│ simulierte aktuelle HRV │ aktueller Stressindikator     │
│ normale HRV             │ persönlicher Basiswert        │
│ aktueller Puls          │ momentaner Puls               │
│ normaler Puls           │ persönlicher Ruhe-/Normalwert │
│ subjektive Belastung    │ Selbsteinschätzung des Users  │
└─────────────────────────┴──────────────────────────────┘
```

## Demo-Slider

Die Demo-Slider sollen später im Profil oder in einem Entwickler-/Demo-Bereich steuerbar sein.

```text
Demo-Modus aktivieren:  [x]

Aktuelle HRV:
[────────────●────────]  58 ms

Normale HRV:
[───────────────●─────]  72 ms

Puls:
[────────────────●────]  92 bpm

Belastung:
[──────────●──────────]  4 / 6
```

## Warum ist der Demo-Modus sinnvoll?

```text
┌────────────────────────────────────────────────────┐
│ Präsentation                                       │
│ Stress kann live simuliert werden                   │
├────────────────────────────────────────────────────┤
│ Entwicklung                                        │
│ UI und Logik können ohne echte Sensoren getestet    │
├────────────────────────────────────────────────────┤
│ Debugging                                          │
│ Grenzfälle können gezielt nachgestellt werden       │
├────────────────────────────────────────────────────┤
│ Teamarbeit                                         │
│ UI-, Health- und Maps-Team können parallel arbeiten │
└────────────────────────────────────────────────────┘
```

---

# 11. Maps & Urban-Walk-Logik

Ein wichtiges Alleinstellungsmerkmal von Flora Break ist die Verbindung von Stressbewertung und konkreter Handlung.

Wenn ein erhöhtes Stresslevel erkannt wird, kann die App prüfen, ob ein kurzer Spaziergang möglich ist.

## Urban Walk

Ein Urban Walk ist eine kurze Gehpause im näheren Umfeld.

Ziel:

```text
10–15 Minuten Bewegung
möglichst entspannte Umgebung
optional Park oder Grünfläche
direkt aus der App heraus vorgeschlagen
```

## Maps-Entscheidung

```text
Stress erkannt
      │
      ▼
Pause sinnvoll?
      │
      ▼
Route in 10–15 Minuten möglich?
      │
 ┌────┴─────┐
 │          │
Ja         Nein
 │          │
 ▼          ▼
Urban      Indoor-
Walk       Alternative
```

## Fallback-Logik

Wenn keine passende Route gefunden wird, soll Flora Break trotzdem helfen.

```text
┌────────────────────────────────────────────┐
│ Route gefunden?                            │
├────────────────────────────────────────────┤
│ Ja  → Urban Walk vorschlagen               │
│ Nein → kurze Indoor-Pause vorschlagen       │
└────────────────────────────────────────────┘
```

Mögliche Indoor-Fallbacks:

* 2 Minuten aufstehen
* Wasser holen
* kurzer Gang durchs Gebäude
* Atemübung
* Fenster öffnen
* Schulter-/Nackenbewegung
* kurze Bildschirmpause

---

# 12. Profil-System

Beim ersten Start soll der Nutzer ein Profil ausfüllen.

Dieses Profil hilft dabei, Empfehlungen persönlicher zu machen.

## Benötigte Profilfelder

```text
┌────────────────────────────┬──────────────────────────────┐
│ Feld                       │ Zweck                        │
├────────────────────────────┼──────────────────────────────┤
│ Name                       │ persönliche Ansprache         │
│ Alter                      │ Basisinformation              │
│ Größe                      │ Gesundheitskontext            │
│ Gewicht                    │ Gesundheitskontext            │
│ Arbeitsbeginn              │ Tagesstruktur                 │
│ Arbeitsende                │ Tagesstruktur                 │
│ Arbeitszeit                │ Pausenzeitraum                │
│ Job-Stress-Level           │ subjektive Belastung          │
│ Demo-Modus                 │ Testmodus aktivieren          │
│ Demo-HRV                   │ simulierte Gesundheitsdaten   │
│ Demo-Puls                  │ simulierte Gesundheitsdaten   │
└────────────────────────────┴──────────────────────────────┘
```

## Profil-Flow

```text
┌─────────────────────┐
│ Kein Profil gefunden│
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ Profil-Setup        │
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ Eingaben prüfen     │
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ Profil speichern    │
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ Dashboard öffnen    │
└─────────────────────┘
```

---

# 13. Feedback-System

Nach einer Pause soll der Nutzer Feedback geben können.

Dadurch kann Flora Break perspektivisch lernen, welche Pausen gut funktionieren.

## Mögliche Feedback-Fragen

```text
Wie fühlst du dich nach der Pause?

[ 😊 besser ]  [ 😐 gleich ]  [ 😟 schlechter ]

War die Empfehlung passend?

[ Ja ] [ Teilweise ] [ Nein ]

Möchtest du solche Pausen öfter vorgeschlagen bekommen?

[ Ja ] [ Nein ]
```

## Nutzen des Feedbacks

```text
┌────────────────────────────────────────────┐
│ Feedback hilft bei:                         │
├────────────────────────────────────────────┤
│ - Verbesserung der Empfehlungen             │
│ - Erkennen passender Pausentypen            │
│ - Anpassung an Nutzerpräferenzen            │
│ - späterer Personalisierung                 │
│ - Bewertung der Wirksamkeit                 │
└────────────────────────────────────────────┘
```

---

# 14. UI-Konzept

Flora Break soll eine ruhige, freundliche und moderne Oberfläche bekommen.

Der Stil soll eher entspannend wirken und nicht wie eine medizinische Warn-App.

## Design-Ziele

```text
┌────────────────────────────────────────────┐
│ UI-Ziele                                   │
├────────────────────────────────────────────┤
│ ruhig                                      │
│ grün / natürlich                           │
│ modern                                     │
│ übersichtlich                              │
│ wenig überladen                            │
│ schnelle Orientierung                      │
│ gute Präsentierbarkeit                     │
└────────────────────────────────────────────┘
```

## Mögliche Hauptbereiche

```text
┌──────────────────────────────┐
│ Dashboard                    │
│ StressScore                  │
│ aktuelle Empfehlung          │
│ Start Pause                  │
└──────────────────────────────┘

┌──────────────────────────────┐
│ Profil                       │
│ persönliche Daten            │
│ Demo-Modus                   │
│ Stress-Simulation            │
└──────────────────────────────┘

┌──────────────────────────────┐
│ Karte                        │
│ Route                        │
│ Dauer                        │
│ Urban Walk                   │
└──────────────────────────────┘

┌──────────────────────────────┐
│ Feedback                     │
│ Pause bewerten               │
│ Wirkung einschätzen          │
└──────────────────────────────┘
```

## Dashboard-Skizze

```text
┌────────────────────────────────────┐
│ 🌿 Flora Break                     │
├────────────────────────────────────┤
│                                    │
│        Stresslevel                 │
│                                    │
│           4 / 6                    │
│        [████░░]                    │
│                                    │
│  Empfehlung:                       │
│  12 Minuten Urban Walk             │
│                                    │
│  ┌────────────────────────────┐    │
│  │ Pause starten              │    │
│  └────────────────────────────┘    │
│                                    │
│  HRV: 58 ms     Puls: 92 bpm       │
│                                    │
└────────────────────────────────────┘
```

## Profil-Skizze

```text
┌────────────────────────────────────┐
│ Profil einrichten                  │
├────────────────────────────────────┤
│ Name:        Owen                  │
│ Alter:       20                    │
│ Größe:       178 cm                │
│ Gewicht:     75 kg                 │
│                                    │
│ Arbeitsbeginn: 09:00               │
│ Arbeitsende:   17:00               │
│ Belastung:     4 / 6               │
│                                    │
│ Demo-Modus:   [x] aktiv            │
│                                    │
│ HRV aktuell:  [────●────] 58       │
│ Puls:         [──────●──] 92       │
│                                    │
│ ┌────────────────────────────┐     │
│ │ Speichern                  │     │
│ └────────────────────────────┘     │
└────────────────────────────────────┘
```

---

# 15. Projektstruktur

Die genaue Struktur kann sich noch ändern. Die geplante oder vorhandene Struktur sieht ungefähr so aus:

```text
flora-break-android/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/
│           │       └── florabreak/
│           │           └── app/
│           │               │
│           │               ├── controller/
│           │               │   └── FloraBreakController.java
│           │               │
│           │               ├── data/
│           │               │   ├── HealthDataProvider.java
│           │               │   └── RouteProvider.java
│           │               │
│           │               ├── health/
│           │               │   ├── MockHealthDataProvider.java
│           │               │   └── HealthConnectDataProvider.java
│           │               │
│           │               ├── maps/
│           │               │   └── MockRouteProvider.java
│           │               │
│           │               ├── model/
│           │               │   ├── StressData.java
│           │               │   ├── StressResult.java
│           │               │   ├── RouteResult.java
│           │               │   └── BreakRecommendation.java
│           │               │
│           │               ├── stress/
│           │               │   └── StressEngine.java
│           │               │
│           │               └── ui/
│           │                   ├── dashboard/
│           │                   ├── profile/
│           │                   ├── feedback/
│           │                   └── map/
│           │
│           ├── res/
│           │   ├── layout/
│           │   ├── drawable/
│           │   ├── values/
│           │   └── mipmap/
│           │
│           └── AndroidManifest.xml
│
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
└── README.md
```

---

# 16. Installation & Setup

## Voraussetzungen

```text
Benötigt:

✅ Android Studio
✅ Java / JDK passend zum Android-Projekt
✅ Git
✅ GitHub-Zugang
✅ Android Emulator oder echtes Android-Gerät
✅ optional: Health Connect App / kompatibles Gerät
```

## Repository klonen

```bash
git clone https://github.com/CYACRAG/flora-break-android.git
cd flora-break-android
```

## In Android Studio öffnen

```text
1. Android Studio starten
2. "Open" auswählen
3. Projektordner flora-break-android öffnen
4. Gradle Sync abwarten
5. Emulator starten
6. App ausführen
```

## Projekt bauen

```bash
./gradlew build
```

Unter Windows:

```bash
gradlew.bat build
```

## App starten

In Android Studio:

```text
Run ▶
```

Oder per Gradle:

```bash
./gradlew installDebug
```

---

# 17. Git-Workflow

Damit mehrere Personen gleichzeitig arbeiten können, sollte jede Person auf einem eigenen Branch arbeiten.

## Empfohlener Workflow

```text
main
 │
 ├── feature/ui
 ├── feature/health-connect
 ├── feature/maps
 ├── feature/stress-engine
 └── feature/profile-feedback
```

## Wichtig

Vor jeder neuen Arbeit:

```bash
git checkout main
git pull origin main
```

Dann eigenen Branch aktualisieren:

```bash
git checkout feature/dein-branch
git merge main
```

Oder alternativ:

```bash
git pull origin main
```

## Änderungen speichern

```bash
git status
git add .
git commit -m "Beschreibung der Änderung"
git push origin feature/dein-branch
```

## Pull Request

```text
1. Auf GitHub gehen
2. Branch auswählen
3. Compare & Pull Request
4. Beschreibung schreiben
5. Prüfen, ob Konflikte vorhanden sind
6. Pull Request erstellen
```

---

# 18. Bekannte Einschränkungen

Da Flora Break noch ein Prototyp ist, gibt es aktuell Einschränkungen.

```text
┌──────────────────────────────┬────────────────────────────────────┐
│ Bereich                      │ Einschränkung                      │
├──────────────────────────────┼────────────────────────────────────┤
│ Health Connect               │ noch nicht vollständig produktiv   │
│ Smartwatch-Daten             │ aktuell teilweise simuliert        │
│ Maps                         │ echte Routenlogik noch nicht final │
│ UI                           │ noch im Aufbau                     │
│ Profil                       │ Flow noch in Integration           │
│ Feedback                     │ noch nicht vollständig verbunden   │
│ StressEngine                 │ Formel kann noch verbessert werden │
│ Datenschutz                  │ finales Konzept noch ausarbeiten   │
└──────────────────────────────┴────────────────────────────────────┘
```

---

# 19. Realisierungsfahrplan

## Phase 1: Grundstruktur

```text
✅ Android-Projekt aufsetzen
✅ Paketstruktur erstellen
✅ Models definieren
✅ Mock-Daten vorbereiten
✅ StressEngine-Grundlogik erstellen
```

## Phase 2: UI und Flow

```text
⏳ Dashboard erstellen
⏳ Profil-Setup beim ersten Start
⏳ Demo-Modus in Profil integrieren
⏳ StressScore anzeigen
⏳ Pausenempfehlung anzeigen
```

## Phase 3: Datenintegration

```text
⏳ HealthConnectDataProvider anbinden
⏳ Fallback auf MockHealthDataProvider
⏳ Berechtigungen prüfen
⏳ reale Werte testen
```

## Phase 4: Maps und Urban Walk

```text
⏳ RouteProvider integrieren
⏳ MockRouteProvider ersetzen oder erweitern
⏳ 10–15-Minuten-Route prüfen
⏳ Fallback-Pause anzeigen
```

## Phase 5: Feedback und Verbesserung

```text
⏳ Feedback-Screen integrieren
⏳ Bewertung speichern
⏳ Empfehlungen anpassen
⏳ Präsentationsversion stabilisieren
```

---

# 20. Team & Rollen

Die genaue Aufgabenverteilung kann sich ändern. Für das Projekt bietet sich folgende Aufteilung an:

```text
┌───────────────────────────┬─────────────────────────────────────┐
│ Rolle                     │ Aufgabe                              │
├───────────────────────────┼─────────────────────────────────────┤
│ Main / StressEngine       │ Kernlogik, Controller, Score         │
│ UI                        │ Screens, Layouts, App-Design         │
│ Health Connect            │ echte Gesundheitsdaten / Fallback    │
│ Maps                      │ Routenlogik, Urban Walk, Fallback    │
│ Präsentation / Poster     │ Erklärung, Visualisierung, Pitch     │
└───────────────────────────┴─────────────────────────────────────┘
```

---

# Beispiel: Gesamtablauf der App

```text
┌──────────────────────┐
│ Nutzer öffnet App    │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ Profil vorhanden?    │
└──────┬─────────┬─────┘
       │         │
     Nein        Ja
       │         │
       ▼         ▼
┌────────────┐  ┌─────────────────┐
│ Setup      │  │ Dashboard       │
│ ausfüllen  │  │ anzeigen        │
└─────┬──────┘  └────────┬────────┘
      │                  │
      ▼                  ▼
┌─────────────────────────────────┐
│ HealthDataProvider              │
│ Mock oder Health Connect         │
└───────────────┬─────────────────┘
                ▼
┌─────────────────────────────────┐
│ StressEngine                    │
│ berechnet StressScore 0–6        │
└───────────────┬─────────────────┘
                ▼
┌─────────────────────────────────┐
│ BreakRecommendation             │
│ Pause ja/nein, Typ, Dauer        │
└───────────────┬─────────────────┘
                ▼
┌─────────────────────────────────┐
│ RouteProvider                   │
│ Urban Walk möglich?              │
└───────┬─────────────────┬───────┘
        │                 │
       Ja                Nein
        │                 │
        ▼                 ▼
┌──────────────┐   ┌────────────────┐
│ Route zeigen │   │ Indoor-Pause   │
│ Urban Walk   │   │ vorschlagen    │
└──────┬───────┘   └───────┬────────┘
       │                   │
       ▼                   ▼
┌─────────────────────────────────┐
│ Feedback nach Pause             │
└─────────────────────────────────┘
```
# 21. Code-Erklärung

Da Flora Break ein Informatik-Projekt ist, wird in diesem Abschnitt erklärt, welche Dateien es gibt, welche Aufgabe sie haben und wie die einzelnen Code-Bausteine zusammenarbeiten.

Der Code ist modular aufgebaut. Das bedeutet: Jede Klasse hat eine möglichst klare Aufgabe. Dadurch kann das Team parallel arbeiten und einzelne Bereiche wie UI, Health Connect oder Maps später einfacher austauschen.

```text id="cdv1w4"
┌────────────────────────────────────────────────────────────┐
│ Code-Grundidee                                             │
├────────────────────────────────────────────────────────────┤
│ Models       → speichern und transportieren Daten           │
│ Provider     → liefern Daten aus Mock, Health oder Maps     │
│ Engine       → berechnet den StressScore                    │
│ Controller   → verbindet Daten, Logik und UI                │
│ UI           → zeigt Ergebnisse für den Nutzer an           │
└────────────────────────────────────────────────────────────┘
```

---

## 21.1 Überblick über die wichtigsten Dateien

```text id="ekc1s4"
app/src/main/java/com/florabreak/app/
│
├── controller/
│   └── FloraBreakController.java
│
├── data/
│   ├── HealthDataProvider.java
│   └── RouteProvider.java
│
├── health/
│   ├── MockHealthDataProvider.java
│   └── HealthConnectDataProvider.java
│
├── maps/
│   └── MockRouteProvider.java
│
├── model/
│   ├── StressData.java
│   ├── StressResult.java
│   ├── RouteResult.java
│   └── BreakRecommendation.java
│
├── stress/
│   └── StressEngine.java
│
└── ui/
    ├── dashboard/
    ├── profile/
    ├── feedback/
    └── map/
```

---

# 21.2 Models

Models sind reine Datenklassen. Sie enthalten hauptsächlich Attribute, Konstruktoren, Getter und Setter.

Sie führen selbst keine komplizierte Logik aus, sondern dienen dazu, Daten sauber zwischen verschiedenen Teilen der App weiterzugeben.

```text id="57gus5"
┌────────────┐
│ Model      │
├────────────┤
│ speichert  │
│ Daten      │
└─────┬──────┘
      │
      ▼
wird von Engine, Provider,
Controller und UI verwendet
```

---

## 21.2.1 StressData.java

### Aufgabe

`StressData.java` enthält die Eingangsdaten, die für die Stressberechnung benötigt werden.

Dazu gehören zum Beispiel:

* aktuelle HRV
* normale HRV
* aktueller Puls
* normaler Puls
* subjektive Belastung
* Zeitstempel oder Statusinformationen

### Warum ist diese Datei wichtig?

Die StressEngine braucht eine einheitliche Datenstruktur. Es soll egal sein, ob die Werte aus Mock-Daten, Demo-Slidern oder später aus Health Connect kommen.

Deshalb gibt es `StressData`.

```text id="twavq4"
Mock-Daten
    │
    ▼
StressData
    │
    ▼
StressEngine
```

Oder später:

```text id="79jyhr"
Health Connect
      │
      ▼
StressData
      │
      ▼
StressEngine
```

### Beispielhafter Aufbau

```java id="4lqqbq"
public class StressData {

    private int currentHrv;
    private int normalHrv;
    private int currentPulse;
    private int normalPulse;
    private int subjectiveStressLevel;

    public StressData(
            int currentHrv,
            int normalHrv,
            int currentPulse,
            int normalPulse,
            int subjectiveStressLevel
    ) {
        this.currentHrv = currentHrv;
        this.normalHrv = normalHrv;
        this.currentPulse = currentPulse;
        this.normalPulse = normalPulse;
        this.subjectiveStressLevel = subjectiveStressLevel;
    }

    public int getCurrentHrv() {
        return currentHrv;
    }

    public int getNormalHrv() {
        return normalHrv;
    }

    public int getCurrentPulse() {
        return currentPulse;
    }

    public int getNormalPulse() {
        return normalPulse;
    }

    public int getSubjectiveStressLevel() {
        return subjectiveStressLevel;
    }
}
```

### Erklärung

```text id="lakbuc"
currentHrv              → aktuell gemessene HRV
normalHrv               → persönlicher Normalwert der HRV
currentPulse            → aktueller Puls
normalPulse             → normaler Puls / Ruhewert
subjectiveStressLevel   → Selbsteinschätzung des Nutzers
```

Die Klasse berechnet nichts selbst. Sie sammelt nur die Werte an einem Ort.

---

## 21.2.2 StressResult.java

### Aufgabe

`StressResult.java` enthält das Ergebnis der Stressberechnung.

Die StressEngine bekommt `StressData` und erzeugt daraus ein `StressResult`.

```text id="h4kwr2"
StressData
    │
    ▼
StressEngine
    │
    ▼
StressResult
```

### Enthaltene Informationen

Ein `StressResult` kann zum Beispiel enthalten:

* StressScore von 0 bis 6
* Stresslevel als Text
* Information, ob eine Pause empfohlen wird
* kurze Begründung

### Beispielhafter Aufbau

```java id="1se5xu"
public class StressResult {

    private int stressScore;
    private String stressLevelText;
    private boolean breakRecommended;
    private String reason;

    public StressResult(
            int stressScore,
            String stressLevelText,
            boolean breakRecommended,
            String reason
    ) {
        this.stressScore = stressScore;
        this.stressLevelText = stressLevelText;
        this.breakRecommended = breakRecommended;
        this.reason = reason;
    }

    public int getStressScore() {
        return stressScore;
    }

    public String getStressLevelText() {
        return stressLevelText;
    }

    public boolean isBreakRecommended() {
        return breakRecommended;
    }

    public String getReason() {
        return reason;
    }
}
```

### Erklärung

```text id="rt1o5l"
stressScore        → Zahl zwischen 0 und 6
stressLevelText    → z. B. "moderat", "hoch", "sehr hoch"
breakRecommended   → true, wenn Pause empfohlen wird
reason             → kurze Erklärung für UI oder Debugging
```

Beispiel:

```text id="o3rlym"
Score: 4
Level: erhöht
Pause empfohlen: ja
Grund: HRV ist deutlich niedriger als normal
```

---

## 21.2.3 RouteResult.java

### Aufgabe

`RouteResult.java` speichert das Ergebnis der Routenprüfung.

Wenn die App erkennt, dass eine Pause sinnvoll ist, fragt sie den `RouteProvider`, ob ein Urban Walk möglich ist.

Das Ergebnis wird in `RouteResult` gespeichert.

```text id="zk1old"
RouteProvider
      │
      ▼
RouteResult
      │
      ▼
BreakRecommendation
```

### Beispielhafter Aufbau

```java id="vmu7fb"
public class RouteResult {

    private boolean routeAvailable;
    private String routeName;
    private int durationMinutes;
    private double distanceKm;
    private String fallbackReason;

    public RouteResult(
            boolean routeAvailable,
            String routeName,
            int durationMinutes,
            double distanceKm,
            String fallbackReason
    ) {
        this.routeAvailable = routeAvailable;
        this.routeName = routeName;
        this.durationMinutes = durationMinutes;
        this.distanceKm = distanceKm;
        this.fallbackReason = fallbackReason;
    }

    public boolean isRouteAvailable() {
        return routeAvailable;
    }

    public String getRouteName() {
        return routeName;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public String getFallbackReason() {
        return fallbackReason;
    }
}
```

### Erklärung

```text id="34z1qa"
routeAvailable   → gibt an, ob eine Route gefunden wurde
routeName        → Name oder Beschreibung der Route
durationMinutes  → Dauer des Spaziergangs
distanceKm       → Länge der Route
fallbackReason   → Grund, warum keine Route möglich ist
```

Beispiel mit Route:

```text id="x1x6t2"
Route verfügbar: ja
Name: Parkrunde
Dauer: 12 Minuten
Distanz: 0,9 km
```

Beispiel ohne Route:

```text id="770u21"
Route verfügbar: nein
Fallback: Keine passende Route im Zeitfenster gefunden
```

---

## 21.2.4 BreakRecommendation.java

### Aufgabe

`BreakRecommendation.java` enthält die finale Empfehlung, die dem Nutzer angezeigt wird.

Diese Klasse verbindet Stressbewertung und Routenentscheidung.

```text id="x26bk2"
StressResult + RouteResult
          │
          ▼
BreakRecommendation
          │
          ▼
UI / Dashboard
```

### Beispielhafter Aufbau

```java id="9dl7gv"
public class BreakRecommendation {

    private boolean recommended;
    private String title;
    private String description;
    private String breakType;
    private int durationMinutes;

    public BreakRecommendation(
            boolean recommended,
            String title,
            String description,
            String breakType,
            int durationMinutes
    ) {
        this.recommended = recommended;
        this.title = title;
        this.description = description;
        this.breakType = breakType;
        this.durationMinutes = durationMinutes;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBreakType() {
        return breakType;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}
```

### Erklärung

```text id="o1h3v7"
recommended       → gibt an, ob eine Pause vorgeschlagen wird
title             → Überschrift für die Empfehlung
description       → Beschreibung für den Nutzer
breakType         → z. B. "Urban Walk" oder "Indoor Break"
durationMinutes   → empfohlene Pausendauer
```

Beispiel:

```text id="7b4mrq"
Titel: Zeit für eine kurze Pause
Typ: Urban Walk
Dauer: 12 Minuten
Beschreibung: Dein Stresslevel ist erhöht. Ein kurzer Spaziergang kann helfen.
```

---

# 21.3 Data Interfaces

Interfaces definieren, welche Methoden eine Klasse anbieten muss.

Das ist wichtig, weil später echte Datenquellen eingebaut werden können, ohne die restliche App komplett umzuschreiben.

```text id="lvaega"
┌────────────────────────────┐
│ Interface                  │
├────────────────────────────┤
│ sagt: Diese Methode muss   │
│ vorhanden sein             │
└─────────────┬──────────────┘
              │
              ▼
┌────────────────────────────┐
│ konkrete Klasse            │
│ setzt Methode wirklich um   │
└────────────────────────────┘
```

---

## 21.3.1 HealthDataProvider.java

### Aufgabe

`HealthDataProvider.java` ist ein Interface für Gesundheitsdaten.

Es legt fest, dass jede Gesundheitsdatenquelle eine Methode bereitstellen muss, mit der aktuelle Stressdaten abgefragt werden können.

### Beispiel

```java id="cun6ts"
public interface HealthDataProvider {

    StressData getCurrentStressData();
}
```

### Erklärung

Diese Datei enthält keine echte Logik. Sie legt nur die Regel fest:

```text id="6gpd81"
Jede Klasse, die HealthDataProvider benutzt,
muss aktuelle StressData liefern können.
```

Das kann dann später so aussehen:

```text id="ojkq7s"
HealthDataProvider
        │
        ├── MockHealthDataProvider
        │
        └── HealthConnectDataProvider
```

Der Vorteil:

Die StressEngine und der Controller müssen nicht wissen, ob die Daten simuliert oder echt sind.

---

## 21.3.2 RouteProvider.java

### Aufgabe

`RouteProvider.java` ist ein Interface für Routeninformationen.

Es legt fest, dass jede Routenquelle eine Methode anbieten muss, um eine passende Route zu suchen.

### Beispiel

```java id="825n02"
public interface RouteProvider {

    RouteResult findShortBreakRoute();
}
```

### Erklärung

Auch hier ist die Datei nur eine Vorgabe.

```text id="6tgsut"
RouteProvider sagt:

"Jede Routenklasse muss eine kurze Pausenroute suchen können."
```

Später kann man dadurch verschiedene Implementierungen verwenden:

```text id="chn7g3"
RouteProvider
      │
      ├── MockRouteProvider
      ├── GoogleMapsRouteProvider
      └── OpenStreetMapRouteProvider
```

---

# 21.4 Health-Klassen

Health-Klassen liefern die Gesundheitsdaten an die App.

---

## 21.4.1 MockHealthDataProvider.java

### Aufgabe

`MockHealthDataProvider.java` liefert simulierte Gesundheitsdaten.

Diese Klasse ist im Prototyp besonders wichtig, weil Flora Break dadurch auch ohne echte Smartwatch oder Health Connect getestet werden kann.

```text id="10wlyu"
┌──────────────────────────────┐
│ MockHealthDataProvider       │
├──────────────────────────────┤
│ erzeugt künstliche Werte      │
│ für HRV, Puls und Belastung   │
└──────────────┬───────────────┘
               ▼
        StressData
```

### Beispiel

```java id="dkux6x"
public class MockHealthDataProvider implements HealthDataProvider {

    @Override
    public StressData getCurrentStressData() {
        return new StressData(
                58,
                72,
                92,
                75,
                4
        );
    }
}
```

### Erklärung

```text id="dlxyfh"
58  → aktuelle HRV
72  → normale HRV
92  → aktueller Puls
75  → normaler Puls
4   → subjektive Belastung
```

Diese Werte simulieren eine Situation mit erhöhtem Stress.

Die aktuelle HRV ist niedriger als der Normalwert. Gleichzeitig ist der Puls höher als normal. Dadurch kann die StressEngine einen erhöhten StressScore berechnen.

---

## 21.4.2 HealthConnectDataProvider.java

### Aufgabe

`HealthConnectDataProvider.java` ist für die spätere echte Anbindung an Health Connect gedacht.

Health Connect kann Gesundheitsdaten aus kompatiblen Apps und Geräten bereitstellen.

Im Prototyp ist diese Klasse entweder noch nicht vollständig umgesetzt oder nutzt bei fehlenden Berechtigungen einen Fallback auf Mock-Daten.

```text id="byufj1"
┌──────────────────────────────┐
│ HealthConnectDataProvider    │
├──────────────────────────────┤
│ liest später echte Daten      │
│ aus Health Connect            │
└──────────────┬───────────────┘
               ▼
        StressData
```

### Geplantes Verhalten

```text id="4cxabv"
Health Connect verfügbar?
        │
   ┌────┴─────┐
   │          │
  Ja         Nein
   │          │
   ▼          ▼
echte       Mock-Daten
Daten       verwenden
lesen
```

### Beispielhafter Aufbau

```java id="bqsxx2"
public class HealthConnectDataProvider implements HealthDataProvider {

    private final MockHealthDataProvider fallbackProvider;

    public HealthConnectDataProvider() {
        this.fallbackProvider = new MockHealthDataProvider();
    }

    @Override
    public StressData getCurrentStressData() {
        // Später: echte Werte aus Health Connect lesen

        // Solange Health Connect nicht verfügbar ist,
        // werden Mock-Daten als Fallback verwendet.
        return fallbackProvider.getCurrentStressData();
    }
}
```

### Erklärung

Die Klasse besitzt einen `fallbackProvider`.

Wenn echte Health-Connect-Daten noch nicht gelesen werden können, liefert die App trotzdem Werte.

Das ist wichtig, damit die App nicht abstürzt und weiterhin präsentierbar bleibt.

```text id="wxoskq"
Kein Health Connect
      │
      ▼
FallbackProvider
      │
      ▼
Mock-Daten
      │
      ▼
StressEngine läuft trotzdem
```

---

# 21.5 StressEngine.java

### Aufgabe

`StressEngine.java` ist die wichtigste Logik-Klasse der App.

Sie berechnet aus den Gesundheitsdaten einen StressScore.

```text id="cqmdsv"
StressData
    │
    ▼
┌────────────────┐
│ StressEngine   │
└───────┬────────┘
        ▼
StressResult
```

### Beispielhafte Berechnung

```java id="ulc220"
public class StressEngine {

    public StressResult calculateStress(StressData data) {
        double hrvRatio = (double) data.getNormalHrv() / data.getCurrentHrv();

        int hrvScore = calculateHrvScore(hrvRatio);
        int pulseScore = calculatePulseScore(
                data.getCurrentPulse(),
                data.getNormalPulse()
        );

        int subjectiveScore = data.getSubjectiveStressLevel();

        int finalScore = Math.round(
                (hrvScore + pulseScore + subjectiveScore) / 3.0f
        );

        finalScore = clamp(finalScore, 0, 6);

        boolean breakRecommended = finalScore >= 3;

        String levelText = getStressLevelText(finalScore);
        String reason = createReason(hrvScore, pulseScore, subjectiveScore);

        return new StressResult(
                finalScore,
                levelText,
                breakRecommended,
                reason
        );
    }

    private int calculateHrvScore(double hrvRatio) {
        if (hrvRatio < 1.05) return 0;
        if (hrvRatio < 1.10) return 1;
        if (hrvRatio < 1.15) return 2;
        if (hrvRatio < 1.20) return 3;
        if (hrvRatio < 1.25) return 4;
        if (hrvRatio < 1.30) return 5;
        return 6;
    }

    private int calculatePulseScore(int currentPulse, int normalPulse) {
        int difference = currentPulse - normalPulse;

        if (difference < 5) return 0;
        if (difference < 10) return 1;
        if (difference < 15) return 2;
        if (difference < 20) return 3;
        if (difference < 25) return 4;
        if (difference < 30) return 5;
        return 6;
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private String getStressLevelText(int score) {
        switch (score) {
            case 0:
                return "entspannt";
            case 1:
                return "sehr niedrig";
            case 2:
                return "niedrig";
            case 3:
                return "moderat";
            case 4:
                return "erhöht";
            case 5:
                return "hoch";
            case 6:
                return "sehr hoch";
            default:
                return "unbekannt";
        }
    }

    private String createReason(int hrvScore, int pulseScore, int subjectiveScore) {
        return "HRV-Score: " + hrvScore
                + ", Puls-Score: " + pulseScore
                + ", subjektive Belastung: " + subjectiveScore;
    }
}
```

---

## Erklärung der StressEngine

### 1. HRV-Verhältnis berechnen

```java id="a6qxeo"
double hrvRatio = (double) data.getNormalHrv() / data.getCurrentHrv();
```

Hier wird berechnet, wie stark die aktuelle HRV vom Normalwert abweicht.

Beispiel:

```text id="0asp3p"
Normal-HRV:   72
Aktuelle HRV: 58

72 / 58 = 1,24
```

Je höher dieses Verhältnis ist, desto stärker ist die aktuelle HRV abgesunken.

---

### 2. HRV-Score berechnen

```java id="d9czqx"
int hrvScore = calculateHrvScore(hrvRatio);
```

Das HRV-Verhältnis wird in einen Score von 0 bis 6 umgewandelt.

```text id="m1t1ba"
1,00 → Score 0
1,10 → Score 2
1,20 → Score 4
1,30 → Score 6
```

---

### 3. Puls-Score berechnen

```java id="q74gxf"
int pulseScore = calculatePulseScore(
        data.getCurrentPulse(),
        data.getNormalPulse()
);
```

Hier wird geprüft, wie stark der aktuelle Puls über dem Normalwert liegt.

Beispiel:

```text id="04m4q7"
Aktueller Puls: 92
Normaler Puls:  75

Differenz: 17
```

Das ergibt ungefähr einen mittleren bis erhöhten Puls-Score.

---

### 4. Subjektive Belastung einbeziehen

```java id="v0wyn8"
int subjectiveScore = data.getSubjectiveStressLevel();
```

Nicht jeder Stress ist nur durch Sensoren messbar. Deshalb wird auch die Selbsteinschätzung des Nutzers berücksichtigt.

---

### 5. Finalen Score berechnen

```java id="4el7ce"
int finalScore = Math.round(
        (hrvScore + pulseScore + subjectiveScore) / 3.0f
);
```

Die drei Teilwerte werden gemittelt:

```text id="eyl8qu"
HRV-Score + Puls-Score + subjektiver Score
──────────────────────────────────────────
                    3
```

So entsteht der finale StressScore.

---

### 6. Score begrenzen

```java id="2w4l18"
finalScore = clamp(finalScore, 0, 6);
```

Die Methode `clamp` stellt sicher, dass der Score nie kleiner als 0 und nie größer als 6 wird.

```text id="9fh30q"
-1 wird zu 0
 7 wird zu 6
 4 bleibt 4
```

---

### 7. Pause empfehlen

```java id="wxy2lj"
boolean breakRecommended = finalScore >= 3;
```

Ab Score 3 wird eine Pause empfohlen.

```text id="6c073p"
Score 0–2 → keine dringende Pause
Score 3–6 → Pause empfohlen
```

---

# 21.6 MockRouteProvider.java

### Aufgabe

`MockRouteProvider.java` simuliert eine Route.

Diese Klasse ist wichtig, solange noch keine echte Maps-API vollständig angebunden ist.

```text id="x83a8c"
┌──────────────────────────────┐
│ MockRouteProvider            │
├──────────────────────────────┤
│ liefert simulierte Route      │
│ z. B. 12 Minuten Urban Walk   │
└──────────────┬───────────────┘
               ▼
        RouteResult
```

### Beispiel

```java id="sbc4og"
public class MockRouteProvider implements RouteProvider {

    @Override
    public RouteResult findShortBreakRoute() {
        return new RouteResult(
                true,
                "Kurze Parkrunde",
                12,
                0.9,
                ""
        );
    }
}
```

### Erklärung

```text id="t1z8ih"
true               → Route wurde gefunden
"Kurze Parkrunde"  → Name der Route
12                 → Dauer in Minuten
0.9                → Distanz in Kilometern
""                 → kein Fallback-Grund nötig
```

Diese Klasse simuliert also, dass eine passende Route gefunden wurde.

---

## Beispiel ohne Route

```java id="kto2tb"
public class MockRouteProvider implements RouteProvider {

    @Override
    public RouteResult findShortBreakRoute() {
        return new RouteResult(
                false,
                "",
                0,
                0.0,
                "Keine passende Route im Zeitfenster gefunden"
        );
    }
}
```

Dann würde die App keine Urban-Walk-Route anzeigen, sondern eine alternative Indoor-Pause empfehlen.

---

# 21.7 FloraBreakController.java

### Aufgabe

`FloraBreakController.java` verbindet die einzelnen Module miteinander.

Der Controller ist die zentrale Vermittlungsstelle zwischen:

* HealthDataProvider
* StressEngine
* RouteProvider
* BreakRecommendation
* UI

```text id="c05ozz"
┌────────────────────────────┐
│ FloraBreakController       │
├────────────────────────────┤
│ holt Gesundheitsdaten       │
│ berechnet Stress            │
│ fragt Route ab              │
│ erstellt Empfehlung         │
└─────────────┬──────────────┘
              ▼
       BreakRecommendation
```

---

## Beispielhafter Aufbau

```java id="dt8ba7"
public class FloraBreakController {

    private final HealthDataProvider healthDataProvider;
    private final RouteProvider routeProvider;
    private final StressEngine stressEngine;

    public FloraBreakController(
            HealthDataProvider healthDataProvider,
            RouteProvider routeProvider,
            StressEngine stressEngine
    ) {
        this.healthDataProvider = healthDataProvider;
        this.routeProvider = routeProvider;
        this.stressEngine = stressEngine;
    }

    public BreakRecommendation createRecommendation() {
        StressData stressData = healthDataProvider.getCurrentStressData();

        StressResult stressResult = stressEngine.calculateStress(stressData);

        if (!stressResult.isBreakRecommended()) {
            return new BreakRecommendation(
                    false,
                    "Alles im grünen Bereich",
                    "Aktuell ist keine Pause notwendig.",
                    "None",
                    0
            );
        }

        RouteResult routeResult = routeProvider.findShortBreakRoute();

        if (routeResult.isRouteAvailable()) {
            return new BreakRecommendation(
                    true,
                    "Zeit für einen Urban Walk",
                    "Dein Stresslevel ist "
                            + stressResult.getStressLevelText()
                            + ". Eine kurze Route wurde gefunden.",
                    "Urban Walk",
                    routeResult.getDurationMinutes()
            );
        }

        return new BreakRecommendation(
                true,
                "Zeit für eine kurze Pause",
                "Dein Stresslevel ist erhöht. Es wurde keine passende Route gefunden, daher empfehlen wir eine Indoor-Pause.",
                "Indoor Break",
                5
        );
    }
}
```

---

## Erklärung des Controllers

### 1. Gesundheitsdaten abrufen

```java id="eo78qt"
StressData stressData = healthDataProvider.getCurrentStressData();
```

Der Controller fragt aktuelle Gesundheitsdaten ab.

Dabei ist egal, ob die Daten aus Mock-Daten oder Health Connect kommen.

---

### 2. Stress berechnen

```java id="xrypbq"
StressResult stressResult = stressEngine.calculateStress(stressData);
```

Die Daten werden an die StressEngine übergeben.

Die StressEngine gibt ein Ergebnis zurück.

---

### 3. Prüfen, ob eine Pause nötig ist

```java id="e4vv7w"
if (!stressResult.isBreakRecommended()) {
    return new BreakRecommendation(...);
}
```

Wenn keine Pause nötig ist, wird eine neutrale Empfehlung erzeugt.

Beispiel:

```text id="xjwa7k"
Alles im grünen Bereich.
Aktuell ist keine Pause notwendig.
```

---

### 4. Route suchen

```java id="mjt3h4"
RouteResult routeResult = routeProvider.findShortBreakRoute();
```

Wenn eine Pause empfohlen wird, fragt der Controller den RouteProvider nach einer kurzen Route.

---

### 5. Urban Walk empfehlen

```java id="su6gxw"
if (routeResult.isRouteAvailable()) {
    return new BreakRecommendation(...);
}
```

Wenn eine passende Route gefunden wurde, schlägt die App einen Urban Walk vor.

---

### 6. Indoor-Fallback empfehlen

```java id="iaj0og"
return new BreakRecommendation(
        true,
        "Zeit für eine kurze Pause",
        "...",
        "Indoor Break",
        5
);
```

Wenn keine Route gefunden wurde, wird stattdessen eine kurze Indoor-Pause empfohlen.

---

# 21.8 UI-Dateien

Die UI-Dateien liegen typischerweise in:

```text id="vlxw7j"
app/src/main/java/com/florabreak/app/ui/
```

und die Layout-Dateien in:

```text id="pdjqdv"
app/src/main/res/layout/
```

Die UI ist dafür zuständig, die Daten für den Nutzer sichtbar und bedienbar zu machen.

```text id="peykp8"
┌────────────────────────────┐
│ UI                         │
├────────────────────────────┤
│ zeigt StressScore           │
│ zeigt Empfehlung            │
│ ermöglicht Profil-Eingabe   │
│ ermöglicht Demo-Slider      │
│ zeigt Feedback-Screen       │
│ zeigt Route / Map           │
└────────────────────────────┘
```

---

## 21.8.1 Dashboard UI

### Aufgabe

Das Dashboard ist der Hauptbildschirm der App.

Hier sieht der Nutzer:

* aktuellen StressScore
* Stresslevel als Text
* aktuelle Empfehlung
* HRV und Puls
* Button zum Starten der Pause
* gegebenenfalls Route oder Kartenansicht

### Beispielhafter UI-Flow

```text id="1sog02"
Dashboard öffnet
      │
      ▼
Controller erzeugt Empfehlung
      │
      ▼
UI zeigt Empfehlung an
```

### Beispielhafter Java-Code

```java id="fe7myt"
BreakRecommendation recommendation = controller.createRecommendation();

titleTextView.setText(recommendation.getTitle());
descriptionTextView.setText(recommendation.getDescription());
durationTextView.setText(recommendation.getDurationMinutes() + " Minuten");
```

### Erklärung

Die UI berechnet die Empfehlung nicht selbst. Sie fragt den Controller und zeigt das Ergebnis an.

Das ist wichtig, damit die UI nicht zu viel Logik enthält.

```text id="865z0h"
Falsch:
UI berechnet alles selbst

Richtig:
UI fragt Controller
Controller nutzt StressEngine
UI zeigt Ergebnis
```

---

## 21.8.2 Profile UI

### Aufgabe

Der Profil-Screen sammelt persönliche Basisdaten des Nutzers.

Dazu gehören:

* Name
* Alter
* Größe
* Gewicht
* Arbeitsbeginn
* Arbeitsende
* subjektive Belastung
* Demo-Modus
* Demo-Werte

### Beispielhafter Ablauf

```text id="03j0eh"
Profil-Screen
      │
      ▼
Nutzer gibt Daten ein
      │
      ▼
Daten werden geprüft
      │
      ▼
Profil wird gespeichert
      │
      ▼
Dashboard wird geöffnet
```

### Beispielhafter Code

```java id="a9xvn3"
String name = nameEditText.getText().toString();
int age = Integer.parseInt(ageEditText.getText().toString());

boolean demoModeEnabled = demoSwitch.isChecked();

profileManager.saveProfile(name, age, demoModeEnabled);
```

### Erklärung

Die Profil-UI ist wichtig, weil Flora Break personalisiert funktionieren soll.

Ein Stresswert ist sinnvoller, wenn die App persönliche Normalwerte kennt.

---

## 21.8.3 Feedback UI

### Aufgabe

Der Feedback-Screen fragt den Nutzer nach einer Pause, ob die Empfehlung hilfreich war.

### Mögliche Buttons

```text id="7c42xr"
[ Besser ]  [ Gleich ]  [ Schlechter ]

[ Empfehlung passend ]  [ Nicht passend ]
```

### Beispielhafter Code

```java id="59cmaz"
betterButton.setOnClickListener(v -> {
    feedbackManager.saveFeedback("better");
});

sameButton.setOnClickListener(v -> {
    feedbackManager.saveFeedback("same");
});

worseButton.setOnClickListener(v -> {
    feedbackManager.saveFeedback("worse");
});
```

### Erklärung

Das Feedback kann später genutzt werden, um Empfehlungen zu verbessern.

Im Prototyp kann es zunächst reichen, Feedback lokal zu speichern oder nur visuell zu simulieren.

---

## 21.8.4 Map UI

### Aufgabe

Die Map UI zeigt später eine Route für den Urban Walk an.

Im Prototyp kann zunächst eine einfache Mock-Ansicht verwendet werden.

### Mögliche Anzeige

```text id="d7c4cq"
┌────────────────────────────────────┐
│ Urban Walk                         │
├────────────────────────────────────┤
│ Route: Kurze Parkrunde             │
│ Dauer: 12 Minuten                  │
│ Distanz: 0,9 km                    │
│                                    │
│ [ Kartenbereich / Route ]          │
│                                    │
│ [ Spaziergang starten ]            │
└────────────────────────────────────┘
```

### Beispielhafter Code

```java id="znyyw3"
RouteResult routeResult = routeProvider.findShortBreakRoute();

routeNameTextView.setText(routeResult.getRouteName());
durationTextView.setText(routeResult.getDurationMinutes() + " Minuten");
distanceTextView.setText(routeResult.getDistanceKm() + " km");
```

### Erklärung

Die Map UI soll nur anzeigen, welche Route gefunden wurde.

Die Entscheidung, ob eine Route geeignet ist, gehört in den RouteProvider beziehungsweise die Maps-Logik.

---

# 21.9 Resource-Dateien

Neben Java-Code gibt es in Android auch Resource-Dateien.

Diese liegen meistens unter:

```text id="74woy6"
app/src/main/res/
```

---

## 21.9.1 layout-Dateien

Pfad:

```text id="wet1sx"
app/src/main/res/layout/
```

### Aufgabe

Layout-Dateien beschreiben, wie ein Screen aussieht.

Beispiele:

```text id="tdz4ww"
activity_main.xml
fragment_dashboard.xml
fragment_profile.xml
fragment_feedback.xml
fragment_map.xml
```

### Enthaltene Elemente

* TextViews
* Buttons
* EditTexts
* Switches
* Slider
* Cards
* Container
* MapView

### Beispiel

```xml id="dltyit"
<TextView
    android:id="@+id/stressScoreTextView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="StressScore: 4/6" />
```

### Erklärung

Dieses Element zeigt Text auf dem Bildschirm an.

Über die ID kann Java später auf das Element zugreifen.

```java id="d9q64u"
TextView stressScoreTextView = findViewById(R.id.stressScoreTextView);
```

---

## 21.9.2 drawable-Dateien

Pfad:

```text id="vi18af"
app/src/main/res/drawable/
```

### Aufgabe

Drawable-Dateien enthalten grafische Elemente.

Beispiele:

* Icons
* Hintergründe
* runde Buttons
* Kartenrahmen
* StressGauge-Grafiken
* Logo-Elemente

### Beispiel

```xml id="kvb9y4"
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="#E8F5E9" />
    <corners android:radius="18dp" />
</shape>
```

### Erklärung

Diese Datei beschreibt einen hellgrünen Hintergrund mit abgerundeten Ecken.

Das passt zum ruhigen Flora-Break-Design.

---

## 21.9.3 values-Dateien

Pfad:

```text id="iymk7j"
app/src/main/res/values/
```

### Aufgabe

Hier werden zentrale Werte gespeichert.

Typische Dateien:

```text id="6ncc54"
colors.xml
strings.xml
themes.xml
styles.xml
```

---

### colors.xml

Enthält Farben der App.

```xml id="nb4hh3"
<color name="flora_green">#4CAF50</color>
<color name="flora_light_green">#E8F5E9</color>
<color name="flora_dark">#1B1B1B</color>
```

### strings.xml

Enthält Texte der App.

```xml id="p4b63y"
<string name="app_name">Flora Break</string>
<string name="stress_score">StressScore</string>
<string name="start_break">Pause starten</string>
```

### themes.xml

Enthält das Design der App.

Hier wird zum Beispiel festgelegt, welche Hauptfarbe verwendet wird.

---

# 21.10 AndroidManifest.xml

Pfad:

```text id="y7ll2p"
app/src/main/AndroidManifest.xml
```

### Aufgabe

Das AndroidManifest ist eine zentrale Konfigurationsdatei jeder Android-App.

Hier wird festgelegt:

* welche Activities es gibt
* welche Activity beim App-Start geöffnet wird
* welche Berechtigungen benötigt werden
* welche App-Eigenschaften gelten

### Beispiel

```xml id="3a0y52"
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:theme="@style/Theme.FloraBreak"
        android:label="@string/app_name">

        <activity
            android:name=".MainActivity"
            android:exported="true">

            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>

        </activity>

    </application>

</manifest>
```

### Erklärung

```text id="bw3ba8"
android:label      → Name der App
android:theme      → Design der App
MAIN               → Startpunkt der App
LAUNCHER           → App erscheint im Launcher
```

Für Health Connect oder Standort können später zusätzliche Berechtigungen nötig sein.

---

# 21.11 Gradle-Dateien

Gradle ist das Build-System von Android.

Es sorgt dafür, dass die App kompiliert, Abhängigkeiten geladen und APKs gebaut werden können.

---

## 21.11.1 settings.gradle

### Aufgabe

`settings.gradle` legt fest, welche Module zum Projekt gehören.

Beispiel:

```gradle id="0sg4um"
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "FloraBreak"
include ':app'
```

### Erklärung

```text id="u4kyv4"
rootProject.name  → Name des Projekts
include ':app'    → Android-App-Modul wird eingebunden
repositories      → Quellen für Libraries
```

---

## 21.11.2 build.gradle auf Projektebene

### Aufgabe

Diese Datei enthält projektweite Build-Einstellungen.

Beispiel:

```gradle id="7fkv4k"
plugins {
    id 'com.android.application' version '8.5.0' apply false
}
```

### Erklärung

Hier wird festgelegt, welche Android-Gradle-Plugin-Version verwendet wird.

---

## 21.11.3 build.gradle auf App-Ebene

Pfad:

```text id="kawbnk"
app/build.gradle
```

### Aufgabe

Diese Datei enthält die wichtigsten Einstellungen für die App selbst.

Beispiel:

```gradle id="a8dzg6"
plugins {
    id 'com.android.application'
}

android {
    namespace 'com.florabreak.app'
    compileSdk 35

    defaultConfig {
        applicationId "com.florabreak.app"
        minSdk 26
        targetSdk 35
        versionCode 1
        versionName "1.0"
    }
}
```

### Erklärung

```text id="waqzf6"
namespace       → Paketname im Code
applicationId   → eindeutige App-ID
compileSdk      → SDK-Version zum Kompilieren
minSdk          → niedrigste unterstützte Android-Version
targetSdk       → Zielversion
versionCode     → interne Versionsnummer
versionName     → sichtbarer Versionsname
```

---

# 21.12 Zusammenspiel aller Klassen

Der wichtigste Ablauf im Code sieht so aus:

```text id="55qsfy"
┌──────────────────────┐
│ UI / Dashboard       │
└──────────┬───────────┘
           │ fragt Empfehlung an
           ▼
┌──────────────────────┐
│ FloraBreakController │
└──────────┬───────────┘
           │ holt Daten
           ▼
┌──────────────────────┐
│ HealthDataProvider   │
└──────────┬───────────┘
           │ liefert
           ▼
┌──────────────────────┐
│ StressData           │
└──────────┬───────────┘
           │ wird berechnet
           ▼
┌──────────────────────┐
│ StressEngine         │
└──────────┬───────────┘
           │ liefert
           ▼
┌──────────────────────┐
│ StressResult         │
└──────────┬───────────┘
           │ wenn Pause nötig
           ▼
┌──────────────────────┐
│ RouteProvider        │
└──────────┬───────────┘
           │ liefert
           ▼
┌──────────────────────┐
│ RouteResult          │
└──────────┬───────────┘
           │ daraus entsteht
           ▼
┌──────────────────────┐
│ BreakRecommendation  │
└──────────┬───────────┘
           │ wird angezeigt
           ▼
┌──────────────────────┐
│ UI / Dashboard       │
└──────────────────────┘
```

---

# 21.13 Warum diese Struktur sinnvoll ist

Die Struktur ist für ein Informatik-Projekt besonders geeignet, weil sie zeigt, dass die App nicht nur aus Oberflächendesign besteht, sondern eine echte technische Architektur besitzt.

```text id="zsskov"
┌────────────────────────────────────────────────────────────┐
│ Vorteile der Struktur                                      │
├────────────────────────────────────────────────────────────┤
│ klare Trennung von UI und Logik                             │
│ einfacher Austausch von Mock-Daten gegen echte Daten         │
│ paralleles Arbeiten im Team möglich                          │
│ bessere Testbarkeit                                          │
│ übersichtlicher Code                                         │
│ gute Grundlage für spätere Erweiterungen                     │
└────────────────────────────────────────────────────────────┘
```

---

# 21.14 Beispiel für eine komplette Code-Kette

Dieses Beispiel zeigt, wie die App technisch von Daten zu einer Empfehlung kommt.

```java id="bbcqvp"
HealthDataProvider healthProvider = new MockHealthDataProvider();
RouteProvider routeProvider = new MockRouteProvider();
StressEngine stressEngine = new StressEngine();

FloraBreakController controller = new FloraBreakController(
        healthProvider,
        routeProvider,
        stressEngine
);

BreakRecommendation recommendation = controller.createRecommendation();

System.out.println(recommendation.getTitle());
System.out.println(recommendation.getDescription());
```

## Erklärung

```text id="63xh05"
1. MockHealthDataProvider erzeugt simulierte Gesundheitsdaten.
2. StressEngine berechnet daraus den StressScore.
3. MockRouteProvider liefert eine simulierte Route.
4. FloraBreakController erzeugt daraus eine BreakRecommendation.
5. Die Empfehlung kann in der UI angezeigt werden.
```

---

# 21.15 Code-Zusammenfassung für die Präsentation

Für die Präsentation kann die technische Umsetzung so erklärt werden:

```text id="sis43m"
Unsere App ist modular aufgebaut. Die Gesundheitsdaten werden über ein
HealthDataProvider-Interface bereitgestellt. Dadurch können wir im Prototyp
Mock-Daten verwenden und später echte Health-Connect-Daten einbauen, ohne die
StressEngine umzuschreiben.

Die StressEngine berechnet aus HRV, Puls und subjektiver Belastung einen
StressScore von 0 bis 6. Ab einem bestimmten Score wird eine Pause empfohlen.

Der FloraBreakController verbindet alle Module miteinander. Er holt die
Gesundheitsdaten, ruft die StressEngine auf, prüft über den RouteProvider,
ob ein Urban Walk möglich ist, und erstellt anschließend eine finale
BreakRecommendation für die UI.

Durch diese Architektur können UI, Health Connect, Maps und Stresslogik
getrennt entwickelt und später sauber integriert werden.
```

---

# 21.16 Kurze Erklärung jeder Code-Art

```text id="l7rekz"
┌────────────────────────────┬────────────────────────────────────┐
│ Code-Art                   │ Aufgabe                            │
├────────────────────────────┼────────────────────────────────────┤
│ Java-Klassen               │ App-Logik und Datenmodelle         │
│ Interfaces                 │ einheitliche Schnittstellen        │
│ XML-Layouts                │ Aufbau der Benutzeroberfläche      │
│ Drawable XML               │ Formen, Hintergründe, Icons        │
│ values XML                 │ Farben, Texte, Themes              │
│ Gradle-Dateien             │ Build-System und Abhängigkeiten    │
│ Manifest                   │ App-Konfiguration und Berechtigungen│
└────────────────────────────┴────────────────────────────────────┘
```

---

# 21.17 Fazit zur technischen Umsetzung

```text id="5k34zi"
┌────────────────────────────────────────────────────────────┐
│ Technisches Fazit                                          │
├────────────────────────────────────────────────────────────┤
│ Flora Break besitzt eine klare technische Grundstruktur.   │
│ Die App trennt Daten, Logik, Routenentscheidung und UI.    │
│ Mock-Daten ermöglichen Tests ohne echte Hardware.          │
│ Health Connect und Maps können später sauber ergänzt werden│
│ Die StressEngine bildet den Kern der Anwendung.            │
└────────────────────────────────────────────────────────────┘
```

Damit zeigt das Projekt nicht nur eine App-Idee, sondern auch eine nachvollziehbare informatische Umsetzung mit Klassen, Schnittstellen, Datenmodellen und modularer Architektur.

---

# Datenschutzgedanke

Da Flora Break mit Gesundheitsdaten arbeitet, ist Datenschutz besonders wichtig.

Für eine spätere finale Version sollten folgende Punkte beachtet werden:

```text
┌────────────────────────────────────────────┐
│ Datenschutz-Prinzipien                     │
├────────────────────────────────────────────┤
│ Datensparsamkeit                           │
│ transparente Berechtigungen                │
│ lokale Verarbeitung, wenn möglich          │
│ keine unnötige Weitergabe von Daten        │
│ freiwillige Nutzung                        │
│ klare Erklärung für Health Connect Zugriff │
└────────────────────────────────────────────┘
```

Der Prototyp kann zunächst mit Mock-Daten arbeiten, damit keine echten Gesundheitsdaten notwendig sind.

---

# Warum Flora Break besonders ist

```text
┌─────────────────────────────────────────────────────────────┐
│ Flora Break kombiniert mehrere Ebenen:                      │
├─────────────────────────────────────────────────────────────┤
│ 1. Gesundheitsdaten                                         │
│ 2. Stressbewertung                                          │
│ 3. persönliche Arbeitsstruktur                              │
│ 4. konkrete Pausenempfehlung                                │
│ 5. Urban-Walk-Route                                         │
│ 6. Feedback nach der Pause                                  │
└─────────────────────────────────────────────────────────────┘
```

Viele Apps zeigen Werte an.

Flora Break möchte aus diesen Werten eine direkte, alltagstaugliche Handlung machen.

```text
Nicht nur messen.
Nicht nur warnen.
Sondern sinnvoll unterbrechen.
```

---

# Kurzbeschreibung für GitHub

```text
Flora Break ist eine native Android-App in Java für proaktives
Stressmanagement im Arbeitsalltag. Die App berechnet aus Gesundheitsdaten
wie HRV und Puls einen StressScore, empfiehlt passende Pausen und kann
bei erhöhtem Stress einen kurzen Urban Walk vorschlagen. Für den Prototyp
werden Mock-Daten und ein Demo-Modus genutzt, damit die App auch ohne
echte Smartwatch oder Health-Connect-Daten getestet werden kann.
```

---

# Elevator-Pitch-Version

```text
Flora Break hilft Menschen im Arbeitsalltag, Stress früher zu erkennen
und direkt sinnvoll zu handeln. Statt nur Gesundheitswerte anzuzeigen,
berechnet die App aus HRV, Puls und subjektiver Belastung einen StressScore
und schlägt passende Pausen vor. Wenn möglich, empfiehlt Flora Break einen
kurzen Urban Walk in der Umgebung. Für den Prototyp nutzt die App Mock-Daten
und einen Demo-Modus, später können echte Daten über Health Connect und
kompatible Smartwatches eingebunden werden.
```

---

# Abschluss

```text
┌────────────────────────────────────────────────────────────┐
│                                                            │
│  🌿 Flora Break                                            │
│                                                            │
│  Eine kleine Pause, bevor Stress groß wird.                │
│                                                            │
└────────────────────────────────────────────────────────────┘
```
