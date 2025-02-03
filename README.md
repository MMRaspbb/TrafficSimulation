# Symulacja Inteligentnych Świateł Drogowych

## Opis projektu

Celem tego projektu jest stworzenie symulacji skrzyżowania z inteligentnymi światłami drogowymi, które dostosowują cykle świateł na podstawie natężenia ruchu. System ma za zadanie zapewniać bezpieczeństwo ruchu drogowego i umożliwiać płynne przemieszczanie się pojazdów przez skrzyżowanie.

---

## Funkcjonalności

### Podstawowe:
- Reprezentacja skrzyżowania z czterema drogami dojazdowymi: północ, południe, wschód, zachód.
- Symulacja standardowych cykli świateł drogowych: zielone, żółte, czerwone oraz opcjonalnych zielonych strzałek.
- Bezpieczeństwo:
  - Unikanie sytuacji konfliktowych (np. jednoczesne zielone światła dla kolidujących kierunków).
- Śledzenie liczby pojazdów oczekujących na każdej drodze dojazdowej.
- Obsługa komend w formacie JSON (dodawanie pojazdów i wykonywanie kroków symulacji).

### Zaimplementowane dodatkowe funkcjonalności:
- zielone strzałki
- skrzyżowanie z dwoma drogami dojazdowymi

---

## Przykład wejścia i wyjścia

### **Przykład wejściowego pliku JSON:**
```json
{
  "commands": [
    {
      "type": "addVehicle",
      "vehicleId": "vehicle1",
      "startRoad": "south",
      "endRoad": "north"
    },
    {
      "type": "addVehicle",
      "vehicleId": "vehicle2",
      "startRoad": "north",
      "endRoad": "south"
    },
    {
      "type": "step"
    },
    {
      "type": "addVehicle",
      "vehicleId": "vehicle3",
      "startRoad": "west",
      "endRoad": "south"
    },
    {
      "type": "step"
    }
  ]
}
```
### **Przykład wyjściowego pliku JSON:**
```json
{
  "stepStatuses": [
    {
      "leftVehicles": [
        "vehicle2",
        "vehicle1"
      ]
    },
    {
      "leftVehicles": []
    },
    {
      "leftVehicles": [
        "vehicle3"
      ]
    },
    {
      "leftVehicles": [
        "vehicle4"
      ]
    }
  ]
}
```
## Uruchomienie projektu i testów
Aby uruchomić projekt, wymagane jest JDK w wersji 17 lub nowszej.
### Linux
```bash
./gradlew run --args="path/to/input.json path/to/output.json"
./gradlew test
```
### Windows cmd
```cmd
gradlew run --args="path/to/input.json path/to/output.json"
gradlew test
```
## Windows PowerShell
```cmd
./gradlew run --args="path/to/input.json path/to/output.json"
gradlew test
```
Chcąc uruchomić wariację skrzyżowania z dwoma drogami dojazdowymi należy jako trzeci argument podać "2" np:
```bash
./gradlew run --args="path/to/input.json path/to/output.json 2"
```
## Działanie algorytmu:
Każda droga dojazdowa posiada swój kierunek początku oraz możliwości kierunki dojazdu. W momencie wywoałania komendy "addVehicle", samochód jest umieszczany na pasującej mu lini(takiej z której może dojechać do celu). Przy wywołaniu komendy "step", sprawdzane jest czy samochody mogą aktualnie osiągnąć cel(np. czy światło na ich lini jest zielone albo czy skręcając w lewo na lini kolizyjnej samochód z naprzeciwka jedzie do przodu). Długość świecenia się świateł jest wyznaczana przez algorytm:
- Dla skrzyżowania z jedną drogą dojazdową sprawdzane jest jak długo czekał najdłużej czekający samochód na lini, długość świecenia się świateł jest wyznaczana na podstawie tej wartości.
- Dla skrzyżowania z jedną z dwoma drogami dojazdowymi tylko jedna strona(np. północ) może mieć włączone światła na raz, kolejność zaświecania się świateł jest wyznaczana dynamicznie na podstawie najdłużej czekającego samochodu na liniach, dodatkowo światła działają w cyklu to znaczy: co cztery zmiany świateł każde ze świateł się świeciło i przepuszczało samochody.

Niestety nie zdążyłem pokryć testami jednostkowymi dużej części kodu, aktulanie jestem podczas sesji i musiałem zająć się projektem pod dużą presją czasu.
