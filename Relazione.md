# Meta-relazione per Progettazione e Sviluppo del Software

# Analisi

Il software mette a disposizione degli utenti un Casinò virtuale in cui è possibile giocare tre grandi classici:

- BlackJack
- Roulette
- Dadi

Inoltre il casinò tiene traccia delle partite giocate dagli utenti e da la possibilità di visualizzare la classifica dei dieci migliori giocatori in base al capitale con cui sono usciti dal casinò.

I Dadi, a differenza degli altri, sono un gioco bonus che il giocatore può scegliere o meno se giocare nel momento in cui decide di uscire dal casinò.<br/>
Se il giocatore riesce ad indovinare il numero che uscirà dopo il lancio dei dadi allora il suo capitale verrà raddoppiato altrimenti, se sbaglia, verrà dimezzato.

## Requisiti funzionali

All'avvio della applicazione viene visualizzato il **menu principale**, qui è possibile decidere se iniziare a giocare o se visionare la **classifica**.

Se si entra nella **classifica** viene visualizzata una lista di record contententi nome del giocatore ed il valore del capitale con cui il giocatore è uscito dal casinò. Proprio perchè questa lista rappresenta la top 10 dei migliori giocatori, sarà limitata solamente a dieci record ed ordinata in maniera decrescente rispetto al capitale. Da qui l'utente potrà solo tornare indietro al **menu principale**.<br/>

Se dal **menu principale** si entra nel casinò viene richiesto all'utente di inserire un nome, con il quale sarà registrato ed eventualmente salvato in **classifica**. Una volta completata la registrazione viene visualizzato un secondo menu, il **menu dei giochi**, in cui è possibile scegliere tra **Roulette** e **BlackJack**.

Nella **Roulette** sarà possibile selezionare e piazzare più tipi di scommesse, eliminare le scommesse precedentemente piazzate e premere un bottone per mostrare il numero uscente.

Nel **Blackjack**, tramite interfaccia apposita, i giocatori possono ricevere carte e decidere se "restare" o richiedere carte, il banco si occupa di dare e mescolare le carte.

Ricapitolando, l'utente può fare avanti ed indietro tra il **menu dei giochi** ed i giochi stessi ma nel momento in cui dedice di uscire dal casino ha la possibilità di scegliere se giocare o meno al gioco dei **Dadi**.

Quello dei **Dadi** è un gioco bonus in cui il giocatore deve inserire un numero per indovinare la combinazione uscente dopo il lancio dei dadi.

Una volta giocato o meno ai **Dadi** l'utente è uscito dal casinò e si ritrova al punto di partenza, il **menu principale**

## Requisiti non funzionali

- Funzionalità drag and drop nella roulette, prendi le fiches e le trascini nelle caselle del tabellone per puntare.
- Funzionalità drag and "throw" nei dadi, sarà possibile trascinare un bicchiere che prende i dadi al suo interno, si potranno mischiare tramite movimento del cursore e al rilascio i dadi verranno lanciati.
- Classifica non più locale ma online, tramite librerie per lo scambio di messaggi in rete la classifica si aggiornerà con i nomi di tutti coloro che hanno fatto un buon punteggio anche in istanze diverse dell'applicazione.
- Salvataggio partita a metà, possibilità di mettere in pausa la partita attuale per riprenderla successivamente anche alla chiusura dell'applicazione.

## Analisi e modello del dominio

All'interno dell'applicazione l'utente (Player) dovrà potersi muoversi tra il menu pricipale, la classifica, il menu dei giochi, ed i giochi stessi.
Inoltre in ogni gioco, l'utente, sarà in grado di piazzare una o più scommesse ed ottenere il risultato del gioco

```mermaid
classDiagram

class MainMenu {
  + goToScoreboard()
  + goToGamesMenu()
}
<<interface>> MainMenu

class Player {
    updateBalance(profitOrLoss)
}
<<interface>> Player

class GamesMenu {
  + goToGame(gameType)
}
<<interface>> GamesMenu

class Bet {
  + place()
}
<<interface>> Bet

class BlackJack {
  + showGameResult()
}
<<interface>> BlackJack

class Roulette {
  + showGameResult()
}
<<interface>> Roulette

class Dice {
  + showGameResult()
}
<<interface>> Dice

class Scoreboard {
  + shotBestPlayers()
  + isPlayerInTop10()
}
<<interface>> Scoreboard

Player --* Bet
Player --* MainMenu
MainMenu --* GamesMenu
MainMenu --* Scoreboard
Player *-- Scoreboard
Bet --* Roulette
Bet --* BlackJack
Bet --* Dice
GamesMenu --* Roulette
GamesMenu --* BlackJack
GamesMenu --* Dice
Dice --* MainMenu
```

Al fine di garantire un'esperienza di gioco fluida e stabile nella prima release, abbiamo deciso di concentrarci sull'implementazione delle funzionalità core del casinò. Le interazioni avanzate come il drag and drop e le funzionalità online, pur essendo altamente desiderabili, richiederanno un ulteriore sviluppo e saranno pertanto oggetto di future iterazioni del prodotto.

# Design

## Architettura

l'architettura di **Virtual Casinò** è di tipo MVC, dove ogni componente principale ha una parte di _model_, dove c'è la logica del componente, una parte di _view_, cioè la parte visuale e con cui l'utente interagisce e per concludere la parte cardine, il controller.

Quest'ultimo è ciò che permette di collegare model e view, prendendo gli input da quest'ultima per poi passarli al model che li elabora e restituire gli output nuovamente alla view.
I macro controller sono **BaseController**, scheletro generico usato da tutti, e **BaseGameController** più specifico, utilizzato dai giochi e che estende quello precedente.
Il primo è sicuramente quello più interessante dove è presente la logica per ricevere e passare dati tra view. Per funzionare usa un _singleton_ cioè una classe già inizializzata con un'istanza che rappresenta il **Player** e permette di salvare le varie informazioni, come nome e saldo.

```mermaid
classDiagram
class Player {
    +getAccount() Account
    +addWin(amount)
    +removeLost(amount)
}
<<interface>> Player

class PlayerHolder {
    +getInstance() PlayerHolder
    +setInstance()
}
<<interface>> PlayerHolder

class Game {
    +nextRound()
    +showResult()
}
<<interface>> Game

class BaseController {
    #sendData()
    +receiveData()
}
<<interface>> Game

class BaseGameController {
    #setGame()
    +placeBetInternal()
}
<<interface>> Game

class View

Player -- PlayerHolder
PlayerHolder -- BaseController
BaseController <|-- BaseGameController
Game -- BaseGameController
BaseGameController -- View
```

Nell'UML sopra viene mostrato in maniera sintetica come funziona la comunicazione tra vari componenti e l'implementazione del pattern MVC.

## Design dettagliato - Matteo Calvanico

### Blackjack Game

Rappresentazione UML **minimale** del Blackjack:

```mermaid
classDiagram
class Blackjack{
    -currentPlayer: Player
    -playDeck: List<Deck>
    -playDeckIndex: int
    -dealerDeck: Deck
    -playerDeck: Array[Deck]
    -checkAndChangeDeck() void
    -usedPlayDeck() int
    -setPlayDeckIndex() void
}

class Deck{
    -deck: List<Card>
    +Deck()
    +initPlayDeck() void
}

class Player{
    +Player(String)
    +getName() String
    +getAccount() double
}

Blackjack -- Deck
Blackjack -- Player
```

La sfida principale nell'implementazione del gioco è stata la gestione di tutti i vari mazzi (successivamente nel dettaglio la gestione delle carte). Infatti nel gioco del Blackjack i mazzi sono molteplici:

- Mazzo del banco (dealer): che rappresenta il numero da battere.
- Mazzi del giocatore: ben due, il primo è quello principale e il secondo usato in caso di _split_.
- Mazzi da gioco: i mazzi da dove si prendono le carte da assegnare, normalmente possono variare tra 2 e 6 (in base al tipo di blackjack) e quando uno finisce si cambia subito con un altro.

#### Problema

Fin da subito si è capito che il mazzo più complicato da gestire sarebbe stato quello da gioco, creato tramite una lista di **Deck**, infatti è necessario tenere conto del numero di mazzo utilizzato e, se necessario, cambiarlo prima di ogni azione per evitare di andare in eccezione.

#### Soluzione

Per far fronte a questo continuo controllo si è creato un indice, chiamato **playDeckIndex**, per tenere conto del mazzo utilizzato e un metodo, chiamato **checkAndChangeDeck**, che controlla se il mazzo è finito e in caso lo cambia, in modo da far continuare il gioco senza far notare nulla al player.

### Gestione delle carte

Rappresentazione UML della gestione delle carte

```mermaid
classDiagram
class Card {
    +Card(CardNumber, CardSeed, CardColor)
    +getCardNumber() int
    +getCardSeed() CardSeed
    +getCardColor() CardColor
    +getCardName() string
    +isFlip() boolean
    +flip() void
}

class CardNumber{
    +getValue() int
    +getName() string
}
<<Enumeration>> CardNumber

class CardSeed
<<Enumeration>> CardSeed

class CardColor
<<Enumeration>> CardColor

Card -- CardNumber
Card -- CardSeed
Card -- CardColor
```

#### Problema

Gestire le varie informazioni (semi, valore, colore) delle carte in maniera pulita e riusicire a creare un mazzo equilibrato e velocemente.

#### Soluzione

Utilizzo degli Enumeratori per le informazioni, dove ognuno contiene determinati valori che sono anche iterabili, permettendo di creare in **Deck** un mazzo seguendo le regole classiche delle carte francesi e senza troppe righe di codice.

Inoltre visto la necessità di dover rappresentare lo stato della carta (Nascosta/Visibile) si è pensato di aggiungere un flag per indicare se la carta è girata o meno, accessibile tramite setter e getter.

### Condivisione informazioni Player

Rappresentazione UML del pattern Singleton per salvare e condividere le informazioni del Player tra le varie scene.

```mermaid
classDiagram
class Player {
    +getAccount() Account
    +addWin(amount) void
    +removeLost(amount) void
}
<<interface>> Player

class PlayerHolder {
    +getPlayerHolded() Player
    setPlayerHolded(Player) void
    +getInstance() PlayerHolder
}
<<interface>> PlayerHolder

Player -- PlayerHolder
PlayerHolder <|-- PlayerHolder
```

#### Problema

Riuscire a mantenere salvata l'istanza del player, in modo da gestire le varie vincite/perdite e condividere le informazioni tra le varie scene

#### Soluzione

Utilizzo del design patter _Singleton_, dove si salva il Player e si modifica utilizzando i metodi della classe singleton tramite l'istanza creata privatamente e resa disponibile tramite un metodo pubblico

## Design dettagliato – Filippo Monti

### Bonus Game – Dice
Rappresentazione UML del gioco bonus **Dice**:

```mermaid
classDiagram
class Dice {
    +roll(): int
    +getLastRoll(): int
    +applyLuckyFactor(guess: int): void
    +nextRound(): void
    +showResult(): void
    +getLastRollFirstDie(): int
    +getLastRollSecondDie(): int
}
Dice --> Player : uses
Dice ..|> Games

class DiceController {
    -onRoll(): void
    -onContinue(): void
}
DiceController --> Dice : uses
DiceController --> View : updates
```

#### Problema

Garantire che il lancio del dato sia deterministico durante la fase di test.

#### Soluzione

È stato applicato il pattern di *Dependency Injection*: la classe `Dice` accetta nel costruttore un `java.util.Random` esterno che, nei test, viene creato con *seed* fisso (`42`). In questo modo ogni esecuzione produce la stessa sequenza di valori, rendendo i test perfettamente riproducibili.

## Problema   
Eseguire in automatico i test delle viste Java FX senza un display fisico.

## Soluzione
Utilizzo di TestFX con back‑end *Monocle* in modalità head‑less. Nel task `test` di Gradle sono impostate le proprietà:

- `testfx.headless=true`  
- `testfx.robot=glass`  
- `prism.order=sw`  
- `java.awt.headless=true`

Questo consente l’esecuzione dei test anche su runner CI o macchine prive di display grafico.

---

## Problema  
Evitare interferenze dovute a singleton, file di persistenza e `Stage` JavaFX aperti tra un test e l’altro.

## Soluzione  
È stata creata una utility `TestUtils.cleanAfterFxTest`, invocata in `@AfterEach`, che:

- chiude tutti gli `Stage` aperti con `FxToolkit.cleanupStages()`  
- azzera il singleton `PlayerHolder`  
- svuota il file `scoreboard.json` con `Scoreboard.clear()` e `deleteStorageFile()`

Così ogni test parte sempre da uno stato neutro.

---

## Problema  
Verificare ramificazioni interne non esposte dall’API pubblica.

## Soluzione  
Uso mirato della *Reflection* nei test (es. accesso al campo `playDeck` in `Blackjack`) per ispezionare o manipolare lo stato interno. In questo modo l’interfaccia pubblica del codice di produzione resta pulita, mentre i test raggiungono una copertura completa.

---

## Problema  
Gestire correttamente animazioni e caricamenti asincroni dei file FXML che potrebbero non essere completati quando il test interroga il DOM.

## Soluzione
Dopo ogni azione che avvia un’animazione o un `FXMLLoader.load(...)`, i test invocano `WaitForAsyncUtils.waitForFxEvents()` (eventualmente incapsulato in `waitFor(timeout, ...)`) per attendere che il Java FX Application Thread svuoti la coda degli eventi prima di procedere con le asserzioni.

---

## Problema   
Alcuni nodi dell’interfaccia non erano referenziabili nei test perché privi di attributo `fx:id`.

## Soluzione  
Sono stati aggiunti gli `fx:id` necessari direttamente nei file FXML, mantenendo un naming coerente (`btnPlaceBet`, `txtWinningNumber`, ecc.). In questo modo TestFX può effettuare il `lookup` dei nodi con `robot.lookup("#fxId")`.

---

## Problema  
I dialoghi modali bloccano l’esecuzione automatica se non vengono chiusi.

## Soluzione  
È stato implementato l’helper `closeAnyAlert(robot)` che intercetta la `DialogPane` aperta, individua il primo pulsante (`OK`, `Yes`, ecc.) e lo clicca tramite `FxRobot`. I test lo richiamano subito dopo l’azione che genera il pop‑up, garantendo che il flusso prosegua senza intervento umano.




## Design dettagliato - Giacomo Ghinelli

### Roulette Game

#### Problema

Visualizzare a schermo in maniera dinamica dei selettori che l'utente può selezionare per indicare la posizione della scommessa, evitando di scrivere in maniera hard-coded la posizione degli stessi.

Nel caso in cui il lettore non conosca il gioco della roulette è giusto specificare che le scommesse, nel gioco reale, vengono effettuate posizionando le fiches sulla tabella dei numeri. La posizione scelta indica direttamente la tipologia di scommessa, i numeri su cui si sta puntando ed il moltiplicatore in caso di vincita.

La sfida quindi è stata quella di riuscire a gestire tutti i tipi e le posizioni delle possibili scommesse che si possono fare alla roulette (ben 149 possibili scommesse diverse).

Per il gioco della roulette è stato progettato un sistema basato su tre modelli principali: **Roulette**, **RouletteBetPositionsGrid** e **RouletteBet**.

Tutti e tre questi modelli estendono una classe base contentente costanti di valori e sequenze specifiche e condivise nel gioco della roulette.

#### Soluzione

Differenziare il più possibile la logica interna del gioco, secondo il principio di singola responsabilità, introducendo le classi **RouletteBetPositionsGrid** e **RouletteBet**

1. **RouletteBetPositionsGrid** contiene la logica responsabile del calcolo dinamico delle posizioni all'interno della tabella dei numeri di tutte le possibili scommesse. Questo approccio di calcolo delle posizioni a run-time rende il software maggiormente mantenibile. Al contrario, optando per una dichiarazione hard-coded delle posizioni, nel caso in cui si fosse deciso di cambiare la dimensione della schermata dell'applicazione, si sarebbero dovute aggiornare tutte le costanti manualmente.

2. **RouletteBet** oltre a fungere da contenitore per l'ammontare ed il tipo di scommessa, contiene la logica responsabile del calcolo dei numeri vincenti. I numeri vincenti sono infatti derivati dalla posizione della scommessa all'interno della tabella dei numeri.

```mermaid
classDiagram
class Roulette {
    - generateWinningNumber(): int
    - calculateGameResult(): double
}

class RouletteBet {
    - calculateWinningNumbers(): void
}

class RouletteBetPositionsGrid {
    - getBetPositionIdicatorsListByBetType(): List[RouletteBetPositionIndicator]
    - getBetPositionIndicatorById(): RouletteBetPositionIndicator
}

class RouletteBetPositionIndicator {
    - betType: BetType
    - betPosition: BetPosition
    - coordinate: Coordinate
}

class RouletteController {
    - roulette: Roulette
    - betPositionsGrid: RouletteBetPositionsGrid
    - createBet(indicator: RouletteBetPositionIndicator): RouletteBet
}

Roulette *-- RouletteBet : has many
RouletteController --* Roulette : uses
RouletteController --* RouletteBetPositionsGrid : uses
RouletteBetPositionsGrid *-- RouletteBetPositionIndicator : has many
```

### Score board

#### Problema

L’applicazione richiede la gestione di una classifica persistente tra diverse esecuzioni dell'applicazione dei 10 migliori giocatori. Il saldo del giocatore deve venir salvato nel momento in cui esce dal casinò.

#### Soluzione

Classe statica **Scoreboard** che funge da servizio per il salvataggio e il recupero dei dati relativi alla scoreboard su file.

**PersistentStorageHelper**: helper generico per la gestione dei file e cartelle usate come storage. Contiene della logica che in futuro potrà essere riutilizzata da altri modelli che richiedono l'inserimento di dati in uno storage persistente.

```mermaid
classDiagram
class Scoreboard {
    + loadScoreboard(): List~ScoreboardRecord~
    + addRecord(ScoreboardRecord): boolean
    + containsName(String): boolean
}

class PersistentStorageHelper {
    + getStorageFile(String): File
    + getStorageDirectory(): String
}

class ScoreboardController {
    + ListView~String~ scoreboardList
    + Button btnExit
    + exit(event): void
}

class MainMenuController

MainMenuController --> Scoreboard : uses
ScoreboardController --> Scoreboard : uses
Scoreboard --> PersistentStorageHelper : uses
```

### Interscambiabilità delle modalità con cui il player piazza scommesse nei giochi del casino.

In un casinò tutti i giochi danno la possibilità di piazzare scommesse.
L'azione di piazzare una scommessa però non è effettuata dal gioco stesso, il gioco si limita ad esporre al giocatore la modalità con cui è possibile piazzare le scommesse. Sarà poi il giocatore a controllare di aver soldi da scommettere ed effettivamente piazzare la scommessa.

Rappresentazione UML del pattern Strategy per esporre le modalità con cui il player può scommettere.

```mermaid
classDiagram

class BlackJackController

class RouletteController

class BaseGameController
<<abstract>> BaseGameController

class IPlaceBet {
    + placeBet(): void
    + getTotalBetsAmount(): double
}
<<interface>> IPlaceBet

class Player {
    placeBet(): void
}

BaseGameController --> IPlaceBet : implements
BlackJackController --> BaseGameController : extends
RouletteController --> BaseGameController : extends
IPlaceBet -- Player : uses
```

#### Problema

Ogni gioco ha la sua specifica logica interna con cui vengono piazzate le scommesse, tale logica deve essere usata dal giocatore per piazzare le scommesse.

#### Soluzione

Il sistema per pizzare le scommesse utilizza il **pattern Strategy**. Tutti i giochi del casino, implementano l'interfaccia **IPlaceBet**, i metodi di questa interfaccia vengono poi esposti ed usati dal giocatore per piazzare le scommesse. In questo modo, il giocatore non deve conoscere la logica interna di ogni gioco per piazzare le scommesse ma è soltato a conoscenza che è possibile piazzare una scommessa su di esso.

# Sviluppo

## Testing automatizzato – Filippo Monti


Il progetto implementa una **batteria completa di test automatici** che copre sia la logica di dominio (*model*) sia le principali viste Java FX (*view*).  
Tutti i test sono eseguibili con un unico comando `./gradlew test` e non richiedono alcun intervento manuale, rispettando quindi il requisito di *total automation*.

### Componenti sottoposti a test

- **Model**: testate le classi `Card`, `Deck`, `Blackjack`, `Dice`, `Roulette`, `RouletteBet`, `Player`.  
  Sono stati realizzati unit test per verificare la correttezza dei metodi pubblici, la gestione delle eccezioni, gli edge-case e, ove necessario, il comportamento interno tramite riflessione.
  
- **View (JavaFX)**: testate le scene `mainMenu.fxml`, `gamesMenu.fxml`, `blackjack.fxml`, `roulette.fxml`.  
  I test sono stati realizzati con **TestFX** in modalità headless (*Monocle*) per validare il comportamento dell'interfaccia grafica.

### Strumenti e framework utilizzati

- **JUnit Jupiter 5** (`org.junit.jupiter:5.10.1`) per unit test e test di integrazione.
- **TestFX 4.0.16-alpha** per l’automazione delle interfacce JavaFX, in esecuzione headless con *OpenJFX Monocle*.
- **Gradle**: il task `test` è stato configurato per attivare JUnit 5 e le proprietà di sistema necessarie a TestFX (`testfx.headless=true`, ecc.).
- **Random deterministico**: per i test sul gioco dei Dadi viene utilizzato un oggetto `java.util.Random` con seed fisso (`42`) per garantire la riproducibilità dei risultati.
- **Reflection**: utilizzata in `BlackjackTest` e `RouletteTest` per isolare scenari complessi non accessibili tramite API pubbliche, senza violare l’incapsulamento delle classi di produzione.

### Strategia di testing

- **Happy case ed edge case**: ogni metodo pubblico è testato sia in condizioni normali che ai limiti dell’input, con uso sistematico di `assertThrows` per i casi di errore attesi.
- **Transizioni di stato**: i test verificano gli effetti collaterali sulle variabili interne (es. numero di carte rimanenti, saldo del giocatore, abilitazione/disabilitazione dei pulsanti GUI).
- **Behaviour-Driven naming**: i metodi di test sono descrittivi e spiegano il comportamento verificato (es. `roll() produces a valid sum and synchronizes accessors`), rendendo più leggibili i report.
- **UI smoke test e test di interazione**: ogni scena JavaFX viene caricata verificando che il root non sia nullo, e che input e azioni dell’utente (click, inserimento testo) generino la risposta corretta del controller.
- **Isolamento tra i test**: è stata implementata un’utility (`TestUtils.cleanAfterFxTest`) per ripristinare singleton, scoreboard e stage JavaFX tra un test e l’altro, evitando interferenze.

### Copertura e risultati

- Circa **240 assert** totali, distribuiti in oltre **80 metodi di test** su 16 classi.

### Limitazioni note

**Menu di navigazione (model)**: la logica lato model è minima (in pratica un semplice delegate all’apertura delle scene Java FX) ed è già implicitamente verificata dai test UI con TestFX. Un ulteriore unit test del metodo avrebbe duplicato la copertura senza reale beneficio.

**Scoreboard**: la classe `Scoreboard` gestisce solo serializzazione/deserializzazione JSON tramite Gson e semplici ordinamenti di lista. L’affidabilità di tali operazioni è demandata alla libreria esterna; test di integrazione su filesystem avrebbero richiesto mock o I/O temporaneo, introducendo complessità non giustificata rispetto al valore aggiunto. Eventuali regressioni emergerebbero comunque dai test UI che mostrano il punteggio.

### Come eseguire i test

```bash
# Clona il repository e posizionati nella root
./gradlew test  # Esegue tutti i test
```



### Struttura generale della suite

Il progetto è corredato da una **suite JUnit 5** che copre i package di dominio; ogni
test è headless e completamente automatico.

| Modulo         | Classi testate                                   | N. test | Focus principali                                                          |
|----------------|--------------------------------------------------|---------|---------------------------------------------------------------------------|
| **Core model** | `Player`, `Deck`, `Card`                         | 34      | Costruttori, mutatori di saldo, integrità mazzo, shuffle                  |
| **Blackjack**  | `Blackjack` (+ supporto `Deck`)                  | 24      | Call / Receive, Split, regola dealer ≥ 17, cambio mazzo run-time          |
| **Roulette**   | `Roulette`, `RouletteBet`, `RouletteBetType`     | 22      | Mappa puntate, payout, calcolo numeri vincenti, aggiornamento saldo       |
| **Dice**       | `Dice`                                           | 11      | Lancio deterministico, lucky-factor, gestione errori, reset round         |

---

### Tipologie di test

1. **Unit Test**  
   Verificano in isolamento singole classi (`CardTest`, `DeckTest`, …).

2. **Integration Test**  
   Simulano flussi completi di gioco  
   (es. `Blackjack`: *call → split → showResult*; `Roulette`: creazione puntata → estrazione → saldo).

3. **Edge-case & Fault Injection**  
   Uso controllato di *reflection* per riprodurre stati limite  
   (mazzi esauriti, vittoria certa in Roulette, roll non ancora effettuato in Dice).

---

### Test dell’interfaccia grafica (TestFX)

Per verificare la correttezza del comportamento delle view abbiamo realizzato una **seconda suite** basata su **TestFX**.  
I test risiedono nel package `it.unibo.viewTest`
e vengono eseguiti in modalità **headless**  
(TestFX + Monocle tramite `xvfb-run` nella GitHub Action).


| View / Scena                     | Classe di test          | N. test | Obiettivi principali                                                                                  |
|----------------------------------|-------------------------|---------|--------------------------------------------------------------------------------------------------------|
| **Main-menu**                    | `MainMenuViewTest`      | 4       | Smoke-load FXML, dialogo inserimento nome, routing a Games-menu, apertura Scoreboard                  |
| **Games-menu**                   | `GamesMenuViewTest`     | 8       | Visualizzazione nome/saldo, lancio Blackjack/Roulette/Dice, dialogo Exit (Yes / No / Cancel)          |
| **Blackjack**                    | `BlackjackViewTest`     | 10      | Flusso completo puntata → gioco (*Card / Stay / Split*), abilitazione pulsanti, uscita sicura         |
| **Roulette**                     | `RouletteViewTest`      | 9       | Creazione/annullo puntate, Spin ruota, update numero vincente, scommessa *RED_BLACK*, ritorno a menu  |

**Totale UI:** **31 test**.

#### Aspetti verificati

* **Smoke-load** di ogni FXML (`assertNotNull(root)`).
* **Binding di pulsanti e campi**: corretta transizione `disable / enable`.
* **Routing tra scene**: `MainMenu → GamesMenu → (Game)` e ritorno, con `WaitForAsyncUtils`.
* **Persistenza di stato** (nome giocatore, saldo) tramite `PlayerHolder`.
* **Azioni di gameplay**:
  * **Blackjack** – sequenza `+100 → Set bet → Card → Stay → Split`.
  * **Roulette** – `Bet amount 50 → RED_BLACK → click posizione → New Bet → Spin`.
* **Dialoghi / Alert** – lookup `.dialog-pane` e chiusura automatica.

#### Problemi riscontrati e workaround

| Problema                         | Work-around nei test                                 |
|---------------------------------|------------------------------------------------------|
| Animazioni / caricamento FXML   | `WaitForAsyncUtils.waitForFxEvents()` + timeout      |
| Componenti senza `fx:id`        | Aggiunta degli `fx:id` negli FXML per il lookup      |
| Pop-up modali (alert Split, Exit)| Helper `closeAnyAlert(robot)` per premere *OK*       |

#### Copertura UI

Non misuriamo la **line coverage** sulla GUI, bensì la **copertura scenari**: tutti i flussi utente principali (avvio, puntata, gioco, exit) sono ora eseguiti in modo automatico, così ogni regressione visibile viene intercettata in CI prima del rilascio.

---

## Note di sviluppo - Matteo Calvanico

### Utilizzo della libreria Media di JavaFX

**Dove**: Nella classe `it.unibo.virtualCasino.controller`, dentro al metodo _playSound_, che verrà chiamato ogni volta che bisogna far partire un suono.

**Permalink**: https://github.com/MatteoCalvanico/pss23-Calvanico-Monti-Ghinelli-VirtualCasino/blob/master/src/main/java/it/unibo/virtualCasino/controller/BaseController.java#L104

**Snippet**:

```java
protected void playSound(String soundFilePath) {
    URL resource = getClass().getResource(soundFilePath);

    if (resource == null) {
        throw new IllegalArgumentException("File path is null");
    }

    try {
        Media soundFile = new Media(resource.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(soundFile);
        mediaPlayer.play();
    } catch (Exception e) {
        throw new IllegalArgumentException("Error playing sound: " + soundFilePath);
    }
}
```

### Utilizzo operatore ternario

**Dove**: in diverse classi, ad esempio: `it.unibo.virtualCasino.model.games.impl.blackjack`

**Permalink**: https://github.com/MatteoCalvanico/pss23-Calvanico-Monti-Ghinelli-VirtualCasino/blob/master/src/main/java/it/unibo/virtualCasino/model/games/impl/blackjack/Blackjack.java#L243

**Snippet**:

```java
public boolean isBlackjack() {
    return (playerDeck[0].size() == 2 && playerDeck[0].countCard() == 21) ? true : false;
    }
```

### Utilizzo Optional di java.util

**Dove**: Nella classe `it.unibo.virtualCasino.controller.menu.MainMenuController` in _showGames()_

**Permalink**: https://github.com/MatteoCalvanico/pss23-Calvanico-Monti-Ghinelli-VirtualCasino/blob/master/src/main/java/it/unibo/virtualCasino/controller/menu/MainMenuController.java#L105

**Snippet**:

```java
void showGames(ActionEvent event) {
    ...
    Optional<String> result;
    String name = "";
    boolean validName = false;

    while (!validName) {
        result = dialog.showAndWait();
        if (result.isPresent()) {
            name = result.get().trim();
            if (name.isEmpty()) {
                dialog.setHeaderText("Name cannot be empty. Please enter your name:");
            } else if (Scoreboard.containsName(name)) {
                dialog.setHeaderText("Invalid name. Name already exists on the casino scoreboard!");
            } else {
                validName = true;
            }
        } else {
            return;
        }
    }
    ...
}
```

## Note di sviluppo - Filippo Monti

### Dependency Injection di `Random` (testabilità)

**Dove** `it.unibo.virtualCasino.model.games.impl.dice.Dice` – costruttore overload

**Permalink**: https://github.com/MatteoCalvanico/pss23-Calvanico-Monti-Ghinelli-VirtualCasino/blob/723dd9d2691e4b7918aa5e013b1dd03ae6fbb262/src/main/java/it/unibo/virtualCasino/model/games/impl/dice/Dice.java#L33

**Snippet**:

```java
public Dice(Player player, Random rng) {     // <-- Random iniettato
    this.player = player;
    this.rng = rng;
}
```
La possibilità di fornire un Random esterno rende la classe completamente
deterministica nei test (seed fissato) senza toccare la logica di produzione:
un esempio di constructor-based dependency injection minimale ma efficace.

### Animazione con Timeline + Lambda JavaFX

**Dove** `it.unibo.virtualCasino.controller.dice.DiceController` – metodo onRoll()

**Permalink**: https://github.com/MatteoCalvanico/pss23-Calvanico-Monti-Ghinelli-VirtualCasino/blob/723dd9d2691e4b7918aa5e013b1dd03ae6fbb262/src/main/java/it/unibo/virtualCasino/controller/dice/DiceController.java#L80

**Snippet**:

```java
Timeline shake = new Timeline();
for (int i = 0; i < FRAMES; i++) {
    shake.getKeyFrames().add(
        new KeyFrame(Duration.millis(i * INTERVAL_MS), ev -> {   // lambda
            int f1 = ThreadLocalRandom.current().nextInt(1, 7);
            int f2 = ThreadLocalRandom.current().nextInt(1, 7);
            imgDie1.setImage(getImage("dieRed" + f1 + ".png").getImage());
            imgDie2.setImage(getImage("dieRed" + f2 + ".png").getImage());
        }));
}

// ...

shake.play();
```
Sfrutta Timeline e KeyFrame di JavaFX con una lambda expression
inline per creare un’animazione di shake dei dadi (24 frame,
80 ms di passo) senza thread espliciti né Timer.

### Aggiornamento saldo tramite delta semantics

**Dove** `it.unibo.virtualCasino.model.games.impl.dice.Dice` - applyLuckyFactor()

**Permalink**: https://github.com/MatteoCalvanico/pss23-Calvanico-Monti-Ghinelli-VirtualCasino/blob/723dd9d2691e4b7918aa5e013b1dd03ae6fbb262/src/main/java/it/unibo/virtualCasino/model/games/impl/dice/Dice.java#L66

**Snippet**:

```java

// ...

double oldBalance = player.getAccount();
double newBalance = (guess == lastRoll) ? oldBalance * 2 : oldBalance / 2;

double delta = newBalance - oldBalance;
if (delta >= 0) {                // vincita: metodo semantico addWin
    player.addWin(delta);
} else {                         // perdita: metodo semantico removeLoss
    player.removeLoss(-delta);
}
```
Invece di impostare direttamente il saldo, si calcola un delta e
si delega a addWin / removeLoss, mantenendo coerenza con
l’invariante del Player (saldo ≥ 0) e centralizzando i controlli.

### Validazione input con AlertHelper e early-return

**Dove** `it.unibo.virtualCasino.controller.dice.Dice` - DiceController.onRoll()

**Permalink**: https://github.com/MatteoCalvanico/pss23-Calvanico-Monti-Ghinelli-VirtualCasino/blob/723dd9d2691e4b7918aa5e013b1dd03ae6fbb262/src/main/java/it/unibo/virtualCasino/controller/dice/DiceController.java#L60

**Snippet**:

```java

// ...

try {
    guess = Integer.parseInt(txtGuess.getText());
} catch (NumberFormatException ex) {
    AlertHelper.show(AlertType.WARNING, "Invalid guess",
                     "Enter a number between 2 and 12");
    return;                                   // early-return: esce subito
}
if (guess < 2 || guess > 12) {
    AlertHelper.show(AlertType.WARNING, "Invalid guess",
                     "Guess must be 2-12");
    return;
}

// ...

```
Mostra l’uso di Alert JavaFX tramite helper comune e la tecnica
dell’early-return per mantenere il metodo lineare e leggibile,
evitando rami annidati.

### Reflection nei test per scenari limite

**Dove** `it.unibo.modelTest.BlackjackTest` – metodo forceEmptyPlayDeck()

**Permalink**: https://github.com/MatteoCalvanico/pss23-Calvanico-Monti-Ghinelli-VirtualCasino/blob/c3239ee6df6f09e6c7f9f0527e6d8833bdb40037/src/test/java/it/unibo/modelTest/BlackjackTest.java#L218

```java
// ...

Field playDeckField = Blackjack.class.getDeclaredField("playDeck");
    playDeckField.setAccessible(true);

    @SuppressWarnings("unchecked")
    List<Deck> playDeck = (List<Deck>) playDeckField.get(blackjack);

    playDeck.forEach(Deck::emptyDeck);

// ...
```

Nei test integrazione svuoto i mazzi privati di Blackjack per
verificare la corretta gestione di fallback quando il deck è
esaurito. Reflection confinata alla suite di test (no impatto runtime).

## Note di sviluppo - Giacomo Ghinelli

### Calcolo dinamico dei selettori di posizione delle scommesse nella tabella dei numeri.

**Dove**: Nel costruttore della classe `it.unibo.virtualCasino.model.games.impl.roulette.RouletteBetPositionsGrid`,

**Permalink**: https://github.com/MatteoCalvanico/pss23-Calvanico-Monti-Ghinelli-VirtualCasino/blob/f138e2c61eac0d5b68cfbdfb95616795a6b41817/src/main/java/it/unibo/virtualCasino/model/games/impl/roulette/RouletteBetPositionsGrid.java

Questa classe continene un'algoritmo che prende come input le coordinate degli angoli della tabella dei numeri e genera come output, per ogni possibile scommessa, la sua posizione all'interno della tabella dei numeri.

**Snippet**:

```java
public RouletteBetPositionsGrid(RouletteTableLayout tableLayout) {
    this.tableLayout = new RouletteTableLayout(tableLayout);

    this.horizontalLinesVerticalOffset = Math
            .abs(tableLayout.topLeftCoordinate.yAxisValue - tableLayout.bottomRightCoordinate.yAxisValue)
            / this.TABLE_COLS;

    this.verticalLinesHorizontalOffset = Math
            .abs(tableLayout.topLeftCoordinate.xAxisValue - tableLayout.bottomRightCoordinate.xAxisValue)
            / (this.TABLE_ROWS + 1);

    prepareBetPositionIdicatorsLayoutData();
}
```

### Utilizzo di lambda expression per il filtraggio di elementi in una lista

**Dove**: Nella classe `it.unibo.virtualCasino.model.games.impl.roulette.RouletteBetPositionsGrid`, nel metodo _getBetPositionIdicatorsListByBetType()_

**Permalink**: https://github.com/MatteoCalvanico/pss23-Calvanico-Monti-Ghinelli-VirtualCasino/blob/f138e2c61eac0d5b68cfbdfb95616795a6b41817/src/main/java/it/unibo/virtualCasino/model/games/impl/roulette/RouletteBetPositionsGrid.java

**Snippet**:

```java
public ArrayList<RouletteBetPositionIndicator> getBetPositionIdicatorsListByBetType(RouletteBetType betType) {
    ArrayList<RouletteBetPositionIndicator> listCopy = new ArrayList<>(betPositionIdicatorsList);

    listCopy.removeIf(betPositionIndicator -> betPositionIndicator.betType != betType);

    return listCopy;
}
```

### Algoritmo per il calcolo dei numeri vincenti dato il tipo e la posizione di una scommessa all'interno della tabella dei numeri nella roulette

**Dove**: Nella classe `it.unibo.virtualCasino.model.games.impl.roulette.RouletteBet`, nel metodo _getWinningNumbers(RouletteBetType betType, int betPositionInTable)_

**Permalink**: https://github.com/MatteoCalvanico/pss23-Calvanico-Monti-Ghinelli-VirtualCasino/blob/f138e2c61eac0d5b68cfbdfb95616795a6b41817/src/main/java/it/unibo/virtualCasino/model/games/impl/roulette/RouletteBet.java

**Snippet**:

```java
private ArrayList<Integer> getWinningNumbers(RouletteBetType betType, int betPositionInTable) {
    ArrayList<Integer> numbers = new ArrayList<>();
    switch (betType) {
        case STRAIGHT_UP -> numbers.add(betPositionInTable);
        case SPLIT -> {
            if (betPositionInTable < this.MAX_VERTICAL_SPLITS) {
                int topNum = calcBottomNumberBasedOnPosition(betPositionInTable);
                numbers.add(topNum + 1);
                numbers.add(topNum);
            } else {
                int bottomNum = betPositionInTable - this.MAX_VERTICAL_SPLITS + 1;
                numbers.add(bottomNum);
                numbers.add(bottomNum + this.TABLE_COLS);
            }
        }
        case STREET -> {
            int topNum = betPositionInTable * this.TABLE_COLS;
            numbers.add(topNum--);
            numbers.add(topNum--);
            numbers.add(topNum);
        }
        case CORNER -> {...}
        case DOUBLE_STREET -> {...}
        case COLUMN -> {...}
        case DOZEN -> {...}
        case ODD_EVEN -> {...}
        case RED_BLACK -> {...}
        case HALF -> {...}
        default -> {}
    }

    return numbers;
}
```

# Commenti finali

In quest'ultimo capitolo si tirano le somme del lavoro svolto e si
delineano eventuali sviluppi futuri.

**Nessuna delle informazioni incluse in questo capitolo verrà utilizzata
per formulare la valutazione finale**, a meno che non sia assente o
manchino delle sezioni obbligatorie. Al fine di evitare pregiudizi
involontari, l'intero capitolo verrà letto dai docenti solo dopo aver
formulato la valutazione.

## Autovalutazione e lavori futuri

### Matteo Calvanico

Per quanto riguarda la mia parte all'interno del progetto (Blackjack e vari menù) sono più che soddisfatto, soprattuto della parte di _Controller_ e _View_ dove credo di aver passato anche la maggior parte del tempo. Per quanto riguarda il _Model del Blackjack_, anche se sono estremamente soddisfatto, credo di poter fare alcuni miglioramenti, soprattutto perchè è stata sviluppata molto prima e con parecchi mesi di distanza dalle altre e nel mentre sono "migliorato" come sviluppatore.

Come team abbiamo lavorato molto bene insieme e credo che il carico di lavoro sia stato abbastanza equilibrato, forse solo la di Roulette (sviluppata da Giacomo) è più complicata concettualmente rispetto alle altre e quindi ha richiesto più impegno da parte sua.

Vorrei aggiungere che mi piacerebbe molto implementare altri giochi in futuro, rendendo l'applicativo simile a quello pre-installato su Windows (_Solitaire & Casual Games_). Alcune possibili aggiunte potrebbero essere Mahjong e Caravan (_Fallout: New Vegas_)

---

### Filippo Monti

Sono complessivamente soddisfatto del mio contributo all’interno del progetto, in particolare per quanto riguarda l’implementazione del gioco dei Dadi e la realizzazione dell’intero sistema di test automatici. Portare a termine un progetto così ampio e strutturato rappresentava per me una sfida importante, che inizialmente percepivo come uno degli ostacoli più grandi della mia carriera universitaria. Averlo superato è motivo di grande orgoglio personale.

Sono felice di essere riuscito a sviluppare un gioco completo, con tanto di animazione, una cosa che prima di questa esperienza non avrei mai pensato di saper fare. Allo stesso modo, sono soddisfatto della copertura test che ho garantito al progetto: ho scritto test automatici per tutte le classi, con particolare attenzione alla correttezza funzionale e alla stabilità delle interfacce grafiche.

Un ringraziamento sincero va al mio team, che con grande pazienza mi ha aspettato e supportato durante tutto il percorso. Sono stato l’ultimo a concludere il mio lavoro, molto oltre la scadenza che ci eravamo prefissati, eppure i miei colleghi non hanno mai smesso di incoraggiarmi e sostenermi. Mi sono spesso sentito meno esperto rispetto a loro, ma proprio grazie al loro aiuto e alla loro fiducia sono riuscito a crescere e a dare un contributo concreto e significativo al progetto.

---

### Giacomo Ghinelli

Per quanto possibile ho cercato di promuovere riutilizzo e condivisione del codice all'interno progetto, tramite l'uso di helper per logiche comuni ai vari componenti software.
Anche per le sezioni di cui ho curato completamente lo sviluppo ho sempre cercato di mantenere le responsabilità e le logiche il più separate possibili al fine di avere un software modulare e facilmente mentenibile. La complessita della gestione delle scommesse della roulette ha richiesto la scrittura di molta logica che per forza di cose non sarà facilmente mantenibile in futuro o per chi vede per la prima volta il codice.

Nonostante ciò penso che come team abbiamo dato tutti il massimo e per questo motivo mi ritengo estremamente soddisfatto del progetto.

## Difficoltà incontrate e commenti per i docenti

# Guida utente

L'utilizzo dell'applicativo è abbastanza intuitivo, all'avvio sarà possibile iniziare una nuova partita o vedere la classifica (in caso di primo avvio la classifica sarà normalmente vuota).

All'avvio di una nuova partita sarà richiesto un nome, finchè non lo si inserirà non sarà possibile continuare, successivamente vi sarannò accreditati 1000 crediti e finiti questi non sarà più possibile giocare ad altro. Se si decide di uscire sarà possibile giocare opzionalmente al bonus **Dadi** che permette di raddoppiare o azzerrare i vostri crediti in base all'esito del gioco.

Ora davanti a voi sarà presente la scelta dei giochi, di seguito le regole nel dettaglio...

## Blackjack

Per iniziare a giocare bisogna fare una puntata, è possibile puntare di 100 alla volta, e appena confermata la puntata(pulsante _Set bet_ collocato a destra) la partita inizia.

All'inizio del turno voi e il banco riceverete due carte (nel suo caso una è girata), il vostro scopo è fare più del banco ma senza superare il 21, se fate 21 con le prime due carte vincete automaticamente (il cosidetto **blackjack**).

Durante il gioco avete a disposizione tre pulsanti:

- _Split_: se avete due carte uguali potete spostarne una nel secondo mazzo a se stante, quindi non importa quanto fate nell'altro, e come fare due partite contemporaneamente con due puntate uguali.
- _+_: come potete immaginare serve per riceve una carta nel mazzo corrispondente...attenzione a non chiederne troppe.
- _Stay_: anche qui abbastanza esplicativo, fa capire al dealer che non si vogliono più carte, può voler dire fine del turno e quindi mostrare i risultati.

Si può uscire in qualsiasi momento (tranne quando è in corso un turno) dal comodo pulsante _Exit_ posto a destra sotto la sezione delle scommesse.

## Roulette

**Obiettivo del Gioco** : Punta su uno o più numeri o combinazioni, premi **Spin!**, e scopri se hai indovinato il numero vincente!

- **Piazza una scommessa**:

  - Scrivi quanto vuoi scommettere nel campo **Bet amount**.
  - Seleziona dal menu a tendina il tipo di scommessa: `STRAIGHT_UP` (un numero singolo) `SPLIT`, `CORNER`, ecc. (in base alle opzioni disponibili)
  - In base al tipo selezionato, il gioco proporrà degli indicatori sulla tavola dei numeri, usando questi indicatori seleziona la posizione della scommessa.
  - Clicca su **New Bet**

- **Controlla le puntate piazzate**

  - Nella sezione **Placed bets**, vedi l’elenco delle puntate attive
  - Puoi annullarle cliccando sul pulsante rosso **X**

- **Gira la ruota**

  - Clicca sul pulsante **Spin!**
  - Il numero vincente apparirà al centro della ruota (in giallo)
  - Il tuo saldo si aggiornerà in base al risultato

- **Fine o nuovo giro**

  - Continua a giocare usando oppure esci dal gioco premendo **Exit**

- **Consigli**
  - Puoi piazzare più puntate prima di girare
  - Il gioco non permette di scommettere più di quanto possiedi

## Dadi

**Obiettivo del Gioco**: Indovina la somma dei due dadi (da 2 a 12). Se ci riesci, il tuo saldo viene raddoppiato; in caso contrario viene dimezzato. È un round “all-in” — perfetto per chiudere la serata con un colpo di fortuna (o di sfortuna!).

**Inserisci la tua previsione**
- Nella casella Your Guess (2-12) digita il numero che pensi uscirà sommando i due dadi.

- Premi ROLL per lanciare.

Nota: valori fuori dall’intervallo 2-12 vengono rifiutati con un messaggio d’errore.

**Assisti al lancio**
- I dadi “vibrano” per qualche secondo (animazione shake).

- Sentirai l’audio del lancio mentre le facce cambiano.

- Al termine compare:

    - Rolled: la somma effettiva uscita.

    - YOU WIN ×2! se hai indovinato, You lose (balance / 2) altrimenti.

- Il tuo saldo in alto a sinistra si aggiorna immediatamente.

**Prosegui o torna alla classifica**
- Clicca Continue per registrare il risultato nella Scoreboard e rivedere la Top 10.

- Se vuoi semplicemente chiudere la finestra, il punteggio viene comunque salvato.

