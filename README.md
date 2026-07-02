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

**Flora Break** ist eine native Android-App in Java, die Nutzerinnen und Nutzer im Arbeitsalltag zu gesunden, aktiven Pausen motivieren soll.

Die App kombiniert ein lokales Nutzerprofil, Gesundheitswerte, eine Stressbewertung, Pausenempfehlungen, Routenlogik, Foto-Beweis, Feedback und lokale Statistik. Ziel ist nicht nur die reine Anzeige von Stressdaten, sondern eine konkrete Handlungsempfehlung im passenden Moment.

```text
┌────────────────────────────────────────────────────────────────────────────┐
│                                                                            │
│  Flora Break erkennt Belastung, übersetzt sie in einen Stresswert          │
│  und empfiehlt direkt eine passende aktive Pause.                          │
│                                                                            │
│  HRV ↓  Puls ↑  Belastung ↑  →  StressScore  →  Pause  →  Route  → Feedback│
│                                                                            │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## Dynamisches Inhaltsverzeichnis

- [1. Wissenschaftlicher Hintergrund](#1-wissenschaftlicher-hintergrund)
- [2. Problemstellung und Ziel](#2-problemstellung-und-ziel)
- [3. Zielgruppe und Nutzungskontext](#3-zielgruppe-und-nutzungskontext)
- [4. Konzept von Flora Break](#4-konzept-von-flora-break)
- [5. Datenschutz und lokale Datenhaltung](#5-datenschutz-und-lokale-datenhaltung)
- [6. App-Handhabung und Demo-Ablauf](#6-app-handhabung-und-demo-ablauf)
- [7. Technische Architektur](#7-technische-architektur)
- [8. Code-Dokumentation](#8-code-dokumentation)
- [9. Datenfluss](#9-datenfluss)
- [10. Stressberechnung](#10-stressberechnung)
- [11. Health Connect und Demo-Modus](#11-health-connect-und-demo-modus)
- [12. Maps und Pausenrouten](#12-maps-und-pausenrouten)
- [13. Foto-Beweis](#13-foto-beweis)
- [14. Feedback und Routencache](#14-feedback-und-routencache)
- [15. Verlauf und Statistik](#15-verlauf-und-statistik)
- [16. Build und Installation](#16-build-und-installation)
- [17. Projektstruktur](#17-projektstruktur)
- [18. Grenzen des aktuellen Stands](#18-grenzen-des-aktuellen-stands)
- [19. Realisierungsfahrplan](#19-realisierungsfahrplan)
- [20. Credits](#20-credits)

---

# 1. Wissenschaftlicher Hintergrund

<details open>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

Stress im Arbeitsalltag entsteht häufig nicht plötzlich, sondern baut sich schrittweise auf. Typische Belastungsanzeichen können ein erhöhter Puls, eine sinkende Herzratenvariabilität und subjektiv empfundene Überforderung sein.

Flora Break nutzt diese Idee für einen App-Prototypen: Belastungsindikatoren werden nicht isoliert angezeigt, sondern in eine verständliche Empfehlung übersetzt.

Die App verfolgt dabei drei Grundgedanken:

```text
┌────────────────────────────┬──────────────────────────────────────────────┐
│ Ebene                      │ Bedeutung für Flora Break                    │
├────────────────────────────┼──────────────────────────────────────────────┤
│ Physiologische Daten       │ HRV, Puls und später weitere Gesundheitswerte│
│ Subjektive Daten           │ Profil, Arbeitszeit, Belastung               │
│ Verhaltensempfehlung       │ aktive Pause, Route, Feedback                │
└────────────────────────────┴──────────────────────────────────────────────┘
```

Die App ist kein Medizinprodukt und stellt keine Diagnose. Sie dient als prototypische Umsetzung einer gesundheitsorientierten Pausenempfehlung.

</details>

---

# 2. Problemstellung und Ziel

<details open>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

Viele Menschen arbeiten über längere Zeit ohne bewusste Pausen. Digitale Arbeit, Deadlines und dauerhafte Erreichbarkeit können dazu führen, dass Belastung erst spät wahrgenommen wird.

Typische Probleme:

- lange Arbeitsphasen ohne aktive Pause
- steigender Puls
- sinkende HRV
- subjektiv hohe Belastung
- mentale Erschöpfung
- fehlende Motivation, selbstständig Pausen einzulegen
- reine Datenanzeige ohne konkrete Handlungsempfehlung

Viele Tracking-Apps zeigen Werte an, überlassen die Interpretation aber der nutzenden Person.

```text
Klassische Tracking-App:

    Daten anzeigen
         │
         ▼
    Nutzer muss selbst interpretieren
         │
         ▼
    eventuell Pause machen


Flora Break:

    Daten erfassen
         │
         ▼
    Stresswert berechnen
         │
         ▼
    konkrete Pause empfehlen
         │
         ▼
    Route starten, Pause dokumentieren, Feedback speichern
```

Ziel von Flora Break ist daher:

> Stress frühzeitig sichtbar machen und direkt eine passende Pause anbieten.

</details>

---

# 3. Zielgruppe und Nutzungskontext

<details>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

Flora Break richtet sich vor allem an Menschen, die im Arbeitsalltag regelmäßig mental belastet sind und von strukturierten Pausen profitieren können.
Gerade für Arbeitgeber ist Florabreak eine wahre Chance ihre Mitarbeitenden vor stressbedingten Ausfällen zu schonen.
Zudem ist ein Wearable, in Form einer SmartWatch die mit Android kompatibel ist, vorrausgesetzt.


## Primäre Zielgruppe

```text
┌──────────────────────┬─────────────────────────────────────────────┐
│ Zielgruppe           │ Nutzen                                      │
├──────────────────────┼─────────────────────────────────────────────┤
│ Büroarbeitende       │ bewusste Pausen während langer Arbeitsphasen│
│                      │ gesünderer Arbeitsalltag                    │
│ Unternehmen          │ Unterstützung im Gesundheitsmanagement      │
│                      │ bessere Pausenkultur im Arbeitsalltag       │
└──────────────────────┴─────────────────────────────────────────────┘
```

## B2B-Idee

Das Projekt ist besonders im B2B-Kontext interessant. Unternehmen könnten Flora Break ihren Mitarbeitenden als Teil des betrieblichen Gesundheitsmanagements anbieten.

Möglicher Nutzen für Unternehmen:

- präventive Unterstützung statt reaktiver Maßnahmen
- Förderung gesunder Pausen
- niedrigschwellige Gesundheitsintervention
- langfristig bessere Pausenkultur
- optional ausbaubar zu einem HR- oder Gesundheitsdashboard

Wichtig: Der aktuelle Prototyp arbeitet lokal und enthält nur die Andockstellen für ein zentrales HR-Dashboard.

</details>

---

# 4. Konzept von Flora Break

<details open>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

Flora Break besteht aus einem geschlossenen App-Flow:

```text
┌─────────────┐
│ Profil      │
└──────┬──────┘
       ▼
┌─────────────┐
│ Home        │
│ Stresswert  │
└──────┬──────┘
       ▼
┌─────────────┐
│ Empfehlung  │
│ Route wählen│
└──────┬──────┘
       ▼
┌─────────────┐
│ Aktive Pause│
│ Timer/Maps  │
└──────┬──────┘
       ▼
┌─────────────┐
│ Foto-Beweis │
└──────┬──────┘
       ▼
┌─────────────┐
│ Feedback    │
└──────┬──────┘
       ▼
┌─────────────┐
│ Verlauf     │
│ Statistik   │
└─────────────┘
```

## Kernidee

Flora Break soll nicht nur anzeigen:

> „Dein Stress ist hoch.“

Sondern:

> „Dein Stress ist hoch. Starte jetzt eine kurze aktive Pause. Hier ist eine passende Route.“

## Aktueller Funktionsumfang

```text
┌───────────────────────────────┬──────────────────────────────────────────┐
│ Funktion                      │ Aktueller Stand                          │
├───────────────────────────────┼──────────────────────────────────────────┤
│ Nutzerprofil                  │ lokal umgesetzt                          │
│ Demo-HRV/Puls                 │ lokal einstellbar                        │
│ Stressanzeige                 │ umgesetzt                                │
│ Pausenempfehlung              │ umgesetzt                                │
│ Routenauswahl                 │ umgesetzt                                │
│ Google Maps öffnen            │ per Intent umgesetzt                     │
│ Timer für aktive Pause        │ umgesetzt                                │
│ Foto-Beweis                   │ Kamera + lokale Speicherung              │
│ Feedback                      │ Sternebewertung + Routenerfahrung        │
│ Verlauf                       │ lokale Datenbankausgabe                  │
│ Statistik                     │ lokale Auswertung + Stressverlauf        │
│ Cloud/Login                   │ nicht umgesetzt                          │
└───────────────────────────────┴──────────────────────────────────────────┘
```

</details>

---

# 5. Datenschutz und lokale Datenhaltung

<details open>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

Da Flora Break mit sensiblen Gesundheits- und Profildaten arbeitet, ist Datenschutz ein zentraler Punkt.

## Aktueller Datenschutzstand

Im aktuellen Prototyp gilt:

- keine Cloud-Anbindung
- kein Login
- kein externer Server
- keine Übertragung an ein Backend
- keine zentrale Nutzerverwaltung
- Foto-Beweise werden lokal gespeichert
- Pausenverlauf wird lokal gespeichert
- Demo-Gesundheitswerte werden lokal gespeichert

```text
┌─────────────────────────┬──────────────────────────────┐
│ Datentyp                │ Speicherort                  │
├─────────────────────────┼──────────────────────────────┤
│ Profil                  │ lokal auf dem Gerät          │
│ Demo-HRV/Puls           │ lokale Einstellungen         │
│ Pausen                  │ lokale Room-Datenbank        │
│ Bewertungen             │ lokale Room-Datenbank        │
│ Foto-Beweise            │ app-interner Speicher        │
│ Routenerfahrungen       │ lokal auf dem Gerät          │
└─────────────────────────┴──────────────────────────────┘
```

## Datenschutzbewertung

Der lokale Ansatz reduziert Risiken, weil keine sensiblen Daten automatisch das Gerät verlassen.

Für eine spätere Produktversion wären zusätzlich nötig:

- Datenschutzerklärung
- Einwilligung zur Verarbeitung von Gesundheitsdaten
- klare Löschfunktionen
- Exportfunktion
- Rollen- und Rechtekonzept bei Unternehmensnutzung
- Prüfung rechtlicher Anforderungen
- Sicherheitskonzept für eventuelle Cloud-Funktionen

## Kein Medizinprodukt

Flora Break ist keine medizinische Diagnose-App. Der Stresswert dient nur als Orientierung und Handlungsempfehlung für Pausen.

</details>

---

# 6. App-Handhabung und Demo-Ablauf

<details open>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

## Empfohlener Ablauf für eine Demonstration

```text
1. App starten
2. Profil öffnen
3. Demo-Modus aktivieren
4. aktuelle HRV niedriger als normale HRV einstellen
5. Puls erhöhen
6. Profil speichern
7. Home-Screen zeigt erhöhten Stress
8. Pausenvorschlag öffnen
9. Route auswählen
10. aktive Pause starten
11. Google Maps öffnen
12. Foto-Beweis aufnehmen
13. Pause abschließen
14. Sternebewertung abgeben
15. Verlauf prüfen
16. Statistik prüfen
```

## Demo-Werte für hohen Stress

```text
┌──────────────────┬──────────────┐
│ Wert             │ Empfehlung   │
├──────────────────┼──────────────┤
│ Aktuelle HRV     │ 45           │
│ Normale HRV      │ 60           │
│ Puls             │ 100 bpm      │
│ Belastung        │ 7 bis 9      │
└──────────────────┴──────────────┘
```

## Demo-Werte für niedrigeren Stress

```text
┌──────────────────┬──────────────┐
│ Wert             │ Empfehlung   │
├──────────────────┼──────────────┤
│ Aktuelle HRV     │ 58 bis 60    │
│ Normale HRV      │ 60           │
│ Puls             │ 75 bis 80    │
│ Belastung        │ 2 bis 4      │
└──────────────────┴──────────────┘
```

Nach einer abgeschlossenen Pause werden im Demo-Modus entspanntere Werte gesetzt. Dadurch wirkt der App-Flow logisch: Pause abgeschlossen, Stress sinkt.

</details>

---

# 7. Technische Architektur

<details open>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

Flora Break ist eine native Android-App in Java.

## Architekturübersicht

```text
┌──────────────────────────────────────────────────────────────┐
│                         UI Activities                        │
│ Main, Profile, BreakSuggestion, ActiveBreak, Feedback, Stats │
└───────────────────────────────┬──────────────────────────────┘
                                │
                                ▼
┌──────────────────────────────────────────────────────────────┐
│                    Controller / Services                     │
│ FloraBreakController, RealMapsBreakService, LocationService  │
└───────────────────────────────┬──────────────────────────────┘
                                │
                                ▼
┌────────────────────────────────────────────────────────────────┐
│                        Repositories                            │
│ ProfileRepository, BreakSessionRepository, RouteCacheRepository│
└───────────────────────────────┬────────────────────────────────┘
                                │
                                ▼
┌──────────────────────────────────────────────────────────────┐
│                  Lokale Daten und Modelle                    │
│ Room, SharedPreferences, DemoSettings, BreakEntity           │
└──────────────────────────────────────────────────────────────┘
```

## Technische Prinzipien

```text
┌────────────────────────────┬────────────────────────────────────┐
│ Prinzip                    │ Umsetzung                          │
├────────────────────────────┼────────────────────────────────────┤
│ lokal zuerst               │ keine Cloud, kein Login            │
│ demo-fähig                 │ steuerbare HRV- und Pulswerte      │
│ modular                    │ getrennte Activities/Repositories  │
│ erklärbar                  │ einfache Java-Klassen              │
│ präsentationsfähig         │ kompletter App-Flow klickbar       │
└────────────────────────────┴────────────────────────────────────┘
```

</details>

---

# 8. Code-Dokumentation

<details open>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

Dieser Abschnitt erklärt die wichtigsten Klassen und ihre Aufgaben.

## UI-Klassen

| Klasse | Aufgabe |
|---|---|
| `MainActivity` | Home-Screen, Stressanzeige, Einstieg in Pausenvorschlag |
| `ProfileActivity` | Nutzerprofil, Arbeitszeiten, Demo-HRV/Puls |
| `BreakSuggestionActivity` | Anzeige und Auswahl von Pausenrouten |
| `ActiveBreakActivity` | aktive Pause mit Timer, Maps-Button und Foto-Beweis |
| `RouteProofActivity` | Kamera starten, Foto komprimieren und lokal speichern |
| `BreakFeedbackActivity` | Sternebewertung, Pause abschließen, Demo-Stress senken |
| `HistoryActivity` | gespeicherte Pausen als Verlauf anzeigen |
| `StatsActivity` | Statistiken und Stressverlauf anzeigen |
| `StressHistoryChartView` | eigene View für Stressverlauf |

## Logik- und Service-Klassen

| Klasse | Aufgabe |
|---|---|
| `FloraBreakController` | verbindet Gesundheitsdaten, StressEngine und Empfehlung |
| `StressEngine` | berechnet aus Gesundheitsdaten einen Stresswert |
| `RealMapsBreakService` | erzeugt/prüft Pausenrouten und Fallbacks |
| `DeviceLocationService` | liefert Gerätestandort oder Fallback |
| `BreakReminderReceiver` | erinnert während aktiver Pause an Foto/Ende |

## Repository-Klassen

| Klasse | Aufgabe |
|---|---|
| `ProfileRepository` | speichert und lädt Nutzerprofil |
| `DemoStressSettingsRepository` | speichert Demo-HRV, Normal-HRV und Puls |
| `BreakSessionRepository` | verwaltet Pausen in der lokalen Datenbank |
| `RouteCacheRepository` | merkt Routenerfahrungen und verarbeitet Feedback |

## Datenmodelle

| Modell | Bedeutung |
|---|---|
| `StressData` | Eingangsdaten für Stressberechnung |
| `StressResult` | Ergebnis der Stressberechnung |
| `BreakRecommendation` | Empfehlung für Pause |
| `RouteResult` | Ergebnis einer Routenberechnung |
| `DemoStressSettings` | gespeicherte Demo-Gesundheitswerte |
| `UserProfile` | lokales Nutzerprofil |
| `BreakEntity` | gespeicherte Pause in Room |

</details>

---

# 9. Datenfluss

<details>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

Der zentrale Datenfluss sieht so aus:

```text
Profil + Demo-Werte
        │
        ▼
DemoStressSettingsRepository
        │
        ▼
HealthDataProvider / Mock-Daten
        │
        ▼
StressEngine
        │
        ▼
StressResult
        │
        ▼
FloraBreakController
        │
        ▼
MainActivity / BreakSuggestionActivity
        │
        ▼
ActiveBreakActivity
        │
        ▼
BreakSessionRepository
        │
        ▼
HistoryActivity / StatsActivity
```

## Pausen-Datenfluss

```text
Route auswählen
      │
      ▼
ActiveBreakActivity startet BreakSession
      │
      ▼
Timer läuft
      │
      ├── Google Maps kann geöffnet werden
      │
      └── Foto-Beweis kann aufgenommen werden
      │
      ▼
BreakFeedbackActivity beendet Pause
      │
      ▼
Bewertung wird gespeichert
      │
      ▼
RouteCacheRepository verarbeitet Feedback
      │
      ▼
Verlauf und Statistik lesen gespeicherte Daten
```

</details>

---

# 10. Stressberechnung

<details open>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

Die Stressberechnung basiert im aktuellen Stand vor allem auf Demo-Werten.

## Genutzte Werte

```text
┌────────────────────┬──────────────────────────────────────────┐
│ Wert               │ Bedeutung                                │
├────────────────────┼──────────────────────────────────────────┤
│ aktuelle HRV       │ momentane Herzratenvariabilität          │
│ normale HRV        │ individueller Referenzwert               │
│ Puls               │ aktuelle Herzfrequenz                    │
│ subjektive Belastung│ Einschätzung aus dem Profil             │
└────────────────────┴──────────────────────────────────────────┘
```

## Grundlogik

Eine niedrigere aktuelle HRV im Vergleich zum Normalwert spricht für höhere Belastung. Ein erhöhter Puls verstärkt diese Einschätzung.

```text
Normale Situation:

    aktuelle HRV ≈ normale HRV
    Puls normal
    → niedriger Stresswert


Belastete Situation:

    aktuelle HRV < normale HRV
    Puls erhöht
    → höherer Stresswert
```

Der berechnete Stresswert wird im Home-Screen angezeigt und löst eine Pausenempfehlung aus.

</details>

---

# 11. Health Connect und Demo-Modus

<details>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

Flora Break ist so aufgebaut, dass Health Connect berücksichtigt werden kann. Für den aktuellen Präsentationsstand ist der Demo-Modus besonders wichtig.

## Aktueller Stand

```text
┌────────────────────────────┬─────────────────────────────────────┐
│ Bereich                    │ Stand                               │
├────────────────────────────┼─────────────────────────────────────┤
│ Health Connect Prüfung     │ vorhanden                           │
│ echte Werte                │ abhängig vom Gerät/Berechtigungen   │
│ Demo-Modus                 │ umgesetzt                           │
│ HRV/Puls steuerbar         │ umgesetzt                           │
│ Emulator-tauglich          │ ja                                  │
└────────────────────────────┴─────────────────────────────────────┘
```

Der Demo-Modus ist keine Notlösung, sondern bewusst eingebaut, damit der App-Flow reproduzierbar präsentiert werden kann.

Ohne Demo-Modus wäre eine Präsentation abhängig von:

- passender Smartwatch
- Health-Connect-Unterstützung
- Berechtigungen
- realen aktuellen Gesundheitsdaten
- Gerät und Android-Version

</details>

---

# 12. Maps und Pausenrouten

<details>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

Flora Break bietet Pausenrouten an und kann Google Maps für die Wegführung öffnen.

## Aktueller Stand

```text
┌────────────────────────────┬─────────────────────────────────────┐
│ Funktion                   │ Stand                               │
├────────────────────────────┼─────────────────────────────────────┤
│ mehrere Routenvorschläge   │ umgesetzt                           │
│ Route auswählen            │ umgesetzt                           │
│ Route in Google Maps öffnen│ per Intent umgesetzt                │
│ Routenerfahrung speichern  │ umgesetzt                           │
│ schlechte Route meiden     │ über Feedback vorbereitet           │
│ eigene Karten-Navigation   │ nicht umgesetzt                     │
└────────────────────────────┴─────────────────────────────────────┘
```

Die eigentliche Navigation übernimmt Google Maps. Flora Break übergibt das Ziel per Intent.

```text
Flora Break
    │
    ▼
Zielkoordinaten
    │
    ▼
Google Maps Intent
    │
    ▼
Google Maps Gehroute
```

</details>

---

# 13. Foto-Beweis

<details>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

Während einer aktiven Pause kann ein Foto-Beweis aufgenommen werden.

## Ablauf

```text
Aktive Pause
    │
    ▼
Foto-Beweis öffnen
    │
    ▼
Kamera-Berechtigung prüfen
    │
    ▼
Kamera-App starten
    │
    ▼
Foto speichern
    │
    ▼
Foto komprimieren
    │
    ▼
mit Pause verknüpfen
```

## Datenschutz

Das Foto wird lokal im app-internen Speicher abgelegt. Es wird nicht hochgeladen und nicht an einen Server gesendet.

</details>

---

# 14. Feedback und Routencache

<details>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

Nach einer Pause kann die Route bewertet werden.

## Bewertung

```text
1 Stern  → nicht erholsam
2 Sterne → okay
3 Sterne → solide
4 Sterne → gut
5 Sterne → sehr erholsam
```

Das Feedback wird lokal gespeichert und kann die spätere Routenauswahl beeinflussen.

```text
Bewertung abgeben
      │
      ▼
BreakSessionRepository speichert Feedback
      │
      ▼
RouteCacheRepository verarbeitet Routenerfahrung
      │
      ▼
Route kann künftig bevorzugt oder seltener empfohlen werden
```

</details>

---

# 15. Verlauf und Statistik

<details>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

## Verlauf

Der Verlauf zeigt gespeicherte Pausen mit:

- Datum und Uhrzeit
- Dauer
- Route
- Routentyp
- Bewertung
- Startstress
- Foto-Beweis-Status

## Statistik

Die Statistik zeigt unter anderem:

```text
┌────────────────────────────┬──────────────────────────────┐
│ Statistikwert              │ Bedeutung                    │
├────────────────────────────┼──────────────────────────────┤
│ Flora Breaks               │ Anzahl gespeicherter Pausen  │
│ aktive Erholungszeit       │ Summe der Pausenminuten      │
│ durchschnittlicher Stress  │ Ø Startstress                │
│ durchschnittliche Bewertung│ Ø Sternebewertung            │
│ Foto-Beweise               │ Anzahl dokumentierter Fotos  │
│ Stressverlauf              │ Chart der letzten 30 Tage    │
└────────────────────────────┴──────────────────────────────┘
```

</details>

---

# 16. Build und Installation

<details open>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

## Projekt bauen

```bash
./gradlew assembleDebug
```

## Java-Kompilierung prüfen

```bash
./gradlew compileDebugJavaWithJavac
```

## Debug-APK

Nach dem Build liegt die APK hier:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## APK auf Android-Gerät kopieren

```bash
adb push app/build/outputs/apk/debug/app-debug.apk /sdcard/Download/flora-debug.apk
```

Danach auf dem Smartphone:

```text
Dateimanager → Downloads → flora-debug.apk → installieren
```

Bei manchen Xiaomi-Geräten blockiert `adb install`. Deshalb ist der Weg über `adb push` und manuelle Installation oft zuverlässiger.

</details>

---

# 17. Projektstruktur

<details>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

```text
app/src/main/java/com/florabreak/app
│
├── data
│   ├── local
│   └── repository
│
├── health
│
├── maps
│
├── model
│
└── ui
```

## Wichtige Ordner

```text
┌──────────────────────────────────────────────┬──────────────────────────────┐
│ Ordner                                       │ Inhalt                       │
├──────────────────────────────────────────────┼──────────────────────────────┤
│ ui                                           │ Activities und UI-Logik      │
│ model                                        │ Datenmodelle                 │
│ data/repository                              │ lokale Speicherlogik         │
│ data/local                                   │ Room-Datenbankklassen        │
│ health                                       │ Health-/Demo-Daten           │
│ maps                                         │ Standort- und Routenlogik    │
│ res/layout                                   │ XML-Screens                  │
│ res/drawable                                 │ Hintergründe und UI-Elemente │
│ res/mipmap-*                                 │ App-Launcher-Icons           │
└──────────────────────────────────────────────┴──────────────────────────────┘
```

</details>

---

# 18. Grenzen des aktuellen Stands

<details>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

Der aktuelle Stand ist ein funktionsfähiger Android-Prototyp für Demonstration und Projektbewertung.

Nicht enthalten sind:

- produktionsreife medizinische Auswertung
- vollständige echte Smartwatch-Anbindung
- eigene Karten-Navigation
- Cloud-Backend
- Login-System
- Admin-Dashboard
- Unternehmensverwaltung
- Langzeitstudie zur Wirksamkeit
- vollständig getestete Edge Cases

Diese Grenzen sind bewusst akzeptiert, weil der Fokus auf dem nachweisbaren App-Flow liegt.

</details>

---

# 19. Realisierungsfahrplan

<details>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

```text
┌────────────┬─────────────────────────────────────────────────────┐
│ Phase      │ Inhalt                                              │
├────────────┼─────────────────────────────────────────────────────┤
│ Phase 1    │ Android-Prototyp mit Demo-Daten                     │
│ Phase 2    │ UI-Flow, Profil, Stressanzeige, Pausenempfehlung    │
│ Phase 3    │ lokale Datenbank, Verlauf, Statistik, Foto-Beweis   │
│ Phase 4    │ stabilere Health-Connect-Anbindung                  │
│ Phase 5    │ bessere Karten-/Routenlogik                         │
│ Phase 6    │ Datenschutzkonzept und Unternehmensintegration      │
│ Phase 7    │ Wirksamkeitsanalyse und produktionsreife Version    │
└────────────┴─────────────────────────────────────────────────────┘
```

</details>

---

# 20. Credits

<details open>
<summary><strong>Abschnitt öffnen / schließen</strong></summary>

## Hauptentwicklung und Integration

|       Git-Name    |---------------Beitrag---------------------------------------------------------------------------------------------| Contributions laut GitHub |
|----------|--------|
|       CYACRAG     | Hauptentwicklung, Integration, Stress-Flow, UI-Flow, Datenlogik, App-Polish, README, GitHub Repository Management | 66 commits 14201 ++
|----------|--------|
|       cemdoJ      | Grundgerüst für UI, Design und Funktionen                                                                         | 12 commits 6761 ++
|----------|--------|
| amarnalic890-prog | Grundgerüst für HealthConnect Verknüfpung und StressEngine                                                        | 7 commits 387 ++
|----------|--------|
|      Maikm1408    | Grundgerüst für MapsAPI, Routenplanung und Controller                                                             | 3 commits 698 ++

Florabreak besteht aktuell 01.07.2026 aus insgesamt 167990 Zeilen Code. Davon wurden 7359 Zeilen in Java und XML Dateien, von uns mittels Vibecoding in ChatGPT geschrieben.

## Projektcharakter

Flora Break wurde als Teamprojekt entwickelt. Die aktuelle integrierte Version verbindet die einzelnen Projektideen zu einem lauffähigen Android-Demo-Flow.

</details>

---

# Zusammenfassung

Flora Break zeigt, wie Gesundheitsdaten, Stressbewertung, Pausenempfehlung, Routenlogik, aktive Pause, Foto-Beweis, Feedback, Verlauf und Statistik in einer nativen Android-App zusammengeführt werden können.

Der aktuelle Stand ist lokal, präsentationsfähig und ohne Cloud oder Login nutzbar.

```text
Profil → Stresswert → Pausenvorschlag → Route → aktive Pause → Foto-Beweis → Feedback → Verlauf/Statistik
```
```text
                                                        ^
                                                       /|\
                                                      / | \
                                                     /  |  \
                                                    /   |   \
                                                   /  #CYAX  \
                                                  /    |||    \
                                                 /     |||     \
                                                / __--"\|/"--__ \
                                               /""      V      ""\
```

