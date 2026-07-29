# AGENTS.md — jblind

> Reference guide for AI agents and developers.
> **Goal:** ensure that any change to the project faithfully follows the already-established business, architecture, code, and testing patterns.
> Read this file **before** generating or changing any code.

> ⚠️ **Keep this document up to date.** This file is the source of truth for the project's conventions. Whenever you add a new feature, change the architecture, or invalidate any statement made here, you **must** update the affected sections of `AGENTS.md` in the same change. Outdated documentation is treated as a bug.

---

## 1. Overview

`jblind` is a **poker tracker** written in Java + Spring Boot.
It manages two kinds of "games" and keeps an auditable history (logs) of everything that happens in each one.

- **Backend:** versioned REST API (`/v1`) consumed by an external frontend (SPA at `http://localhost:5173`).
- **Persistence:** PostgreSQL via Spring Data JPA / Hibernate.

### Stack

| Item | Version / Tool |
|------|----------------|
| Java | **25** |
| Spring Boot | **4.0.6** (`spring-boot-starter-parent`) |
| Web | `spring-boot-starter-webmvc` (Spring MVC, servlet, **not** reactive) |
| Persistence | `spring-boot-starter-data-jpa` + PostgreSQL |
| Validation | `spring-boot-starter-validation` (Jakarta Bean Validation) |
| Boilerplate | **Lombok** (`optional`, compile-time only) |
| Build | Maven (`mvnw` / `mvnw.cmd`) |
| Formatting | `spring-javaformat-maven-plugin` (runs in the `validate` phase) |
| Tests | JUnit 5, Mockito, AssertJ, **Instancio**, Spring Test (`@WebMvcTest`) |

---

## 2. Business Domain

The system models **two independent aggregates**, each in its own domain package. They **do not know about each other** and share no code.

### 2.1 CashGame (`com.gui.jblind.cashgame`)

A **live cash game**: chips equal real money, players come and go freely and can rebuy at any time.

Entities and concepts:

- **`CashGame`** — the game itself. Has blinds (`smallBlind`, `bigBlind`), entry limits (`minBuyIn`, `maxBuyIn`), `scheduledAt`, `status`, and the list of `players`.
- **`CashGamePlayer`** — a player within a game. Holds `totalInvested` (how much they put in overall) and `currentStack` (current chips). Business rules live **on the entity itself**: `addChips`, `rebuy`, `cashout`.
- **`CashGameLog`** — an immutable audit record of an event (buy-in, rebuy, etc.). Holds `type`, `amount`, `message`, `timestamp`, `playerId`, and `cashGameId`.
- **`CashGameStatus`** — `DRAFT`, `SCHEDULED`, `IN_PROGRESS`, `FINISHED`.
- **`CashGameLogType`** — `BUY_IN`, `REBUY`, `ADD_ON`, `CASHOUT`, `INFO`. Determines how the player's statistics are updated.

### 2.2 Tournament (`com.gui.jblind.tournament`)

A **tournament**: a fixed structure of blind levels that increase over time, with prizes by finishing position. Unlike a cash game, the focus is on going further (final position), not the money stack.

Entities and concepts:

- **`Tournament`** — the tournament. Has `buyIn`, `startingStack`, `expectedPlayers`, the `allowRebuys`/`allowAddOn` flags, `status`, the ordered list of `levels`, the list of `players`, and a `prize` (`@OneToOne`).
- **`TournamentLevel`** — a level/round of the blind structure (`roundNumber`, `smallBlind`, `bigBlind`, `ante`, `durationInMinutes`, `isBreak`, `shouldColorUp`). Ordered by `roundNumber`.
- **`TournamentPlayer`** — a player in the tournament. Statistics: `entries`, `eliminationsMade`, `totalInvested`, `addOn`, `playersLeft`. Rules on the entity: `addEntry`, `addOn`, `eliminate`, `playersLeft`.
- **`TournamentPrize`** — the total prize pool (`total`), with a `mode` (`PrizeMode`) and the ordered list of `payouts`.
- **`TournamentPrizePayout`** — how much each position receives (ordered by `position`).
- **`TournamentLog`** — audit of tournament events.
- **`TournamentStatus`** — `DRAFT`, `SCHEDULED`, `IN_PROGRESS`, `FINISHED`.
- **`TournamentLogType`** — `BUY_IN`, `REBUY`, `ADD_ON`, `ELIMINATION`, `LEFT`.
- **`PrizeMode`** — `PERCENTAGE`, `FIXED`.

---

## 3. Architecture

### 3.1 Organization by domain (package-by-feature)

Packages are separated **by business domain**, not by technical layer. Each domain has:

```
com.gui.jblind.<domain>            <- isolated domain (entities, enums, services, repositories, queries)
com.gui.jblind.<domain>.web        <- HTTP edge (RestServices + Request/Response DTOs)
com.gui.jblind.core                <- cross-cutting infrastructure (config, exceptions)
```

Real example:

```
com.gui.jblind
├── JblindApplication.java
├── cashgame
│   ├── CashGame / CashGamePlayer / CashGameLog        (JPA entities)
│   ├── CashGameStatus / CashGameLogType               (enums)
│   ├── CashGameService                                (write, public)
│   ├── CashGameLogService / CashGamePlayerService     (write, package-private)
│   ├── CashGameLogQuery                               (read, package-private)
│   ├── CashGameRepository / ...Repository             (Spring Data)
│   └── web
│       ├── CashGameRestService / CashGameLogRestService
│       ├── CashGameRequest / CashGameLogRequest / ...  (input)
│       └── CashGameDetailResponse / ...Response        (output)
├── tournament   (same structure)
└── core
    ├── configuration.WebConfiguration
    └── exception.{BusinessException, ResourceNotFoundException}
```

### 3.2 Dependency rule between layers (critical)

The dependency flow is **always from the outside in**:

```
web (RestService, Request, Response)  ──▶  domain (Service, Query)  ──▶  Repository  ──▶  Entity
```

Rules that **must** be respected:

- **The domain does NOT know the `web` layer as a controller.** However, in this project, **the DTOs (`Request`/`Response`) live in the `web` package and the conversion logic lives inside them** — the `Service`s import these DTOs to receive input and return output. Keep this pattern: entity↔DTO conversion is the DTO's responsibility, never the Service's nor the RestService's.
- **The JPA entity is never exposed through the API.** RestService only carries `Request`/`Response`.
- **Domains do not reference each other** (`cashgame` never imports `tournament` and vice versa).
- **`core` depends on no domain**; domains may depend on `core` (e.g., exceptions).

### 3.3 Command / Query separation (CQRS-lite)

The project separates **writes** from **reads**:

- **`*Service`** → write/command operations (create, update, delete, play, addPlayer) and orchestration.
- **`*Query`** → specialized read operations (e.g., `CashGameLogQuery.findAllByCashGameId`). They are `@Service` and `package-private`.
- "Auxiliary" services that apply sub-rules (`CashGamePlayerService`, `CashGameLogService`, `TournamentPlayerService`) are also **package-private** — only the domain can see them.

### 3.4 Layers explained

| Term | What it is | Conventions |
|------|------------|-------------|
| **RestService** | The `@RestController`. Thin: only receives HTTP, delegates to the `Service`, and builds the `ResponseEntity`. | `class` **package-private**, `@RequestMapping("/v1/...")`, injection via `@AllArgsConstructor`. |
| **Service** | Application/transaction rule. Orchestrates repositories, entities, and queries. | `@Service`, `@Transactional(rollbackFor = Exception.class)`. Public when used by `web`; package-private when internal. |
| **Query** | Specialized read, returns `Response` DTOs. | `@Service` + `@Transactional`, package-private. |
| **Repository** | Spring Data `interface`. | `@Repository extends JpaRepository<Entity, IdType>`. Query methods derived from the name (e.g., `findAllByCashGameIdOrderByTimestampDesc`). |
| **Entity** | Rich domain model (JPA). Contains business rules as methods. | See section 4. |
| **Request** | Input DTO (`record`), with validation and a `to()`/`to(id)` method that builds the entity. | See section 5. |
| **Response** | Output DTO (`record`), with a static factory `of(...)`/`from(...)` built from the entity. | See section 5. |

### 3.5 Exception handling

- `BusinessException` → business rule violation (e.g., starting an already-finished game).
- `ResourceNotFoundException` → non-existent resource by id.
- Both extend `RuntimeException` and live in `core.exception`.
- **Note:** there is currently **no** global `@ControllerAdvice`/`@RestControllerAdvice`. If you need to standardize HTTP error responses, that is an extension point to be created in `core` (and it must come with tests).

---

## 4. Entity Patterns (JPA + Lombok)

Every entity follows **exactly** this template:

```java
@Table
@Entity
@Getter
@EqualsAndHashCode
@Builder(toBuilder = true)                       // toBuilder only when there is update by id
@NoArgsConstructor(access = PRIVATE)             // required by JPA, hidden from the code
@AllArgsConstructor(staticName = "of", access = PACKAGE)
public class CashGame {

    @Id
    @Builder.Default
    private final String id = randomUUID().toString();   // id = String UUID generated on creation
    // ...fields...

    public CashGame start() {          // business rules live ON the entity
        this.status = IN_PROGRESS;
        return this;
    }
}
```

Rules to follow:

- **No `setters`.** Only `@Getter`. State changes only through explicit business methods (`start`, `addPlayer`, `addChips`, `rebuy`, `cashout`, `eliminate`, etc.).
- **Id:** `String` UUID (`randomUUID().toString()`) via `@Builder.Default`, except logs, which use `@GeneratedValue(strategy = IDENTITY)` with `Long`.
- **Closed constructors:** `@NoArgsConstructor(PRIVATE)` + `@AllArgsConstructor(staticName = "of", access = PACKAGE)`. Actual instantiation via the **Builder**.
- **Enums** persisted with `@Enumerated(EnumType.STRING)`.
- **Collections** with `@Builder.Default private List<...> x = new ArrayList<>();`, mapped with `@OneToMany(cascade = ALL, orphanRemoval = true)` + `@JoinColumn`, and `@OrderBy` when the order matters.
- **Monetary values** → `BigDecimal` (never `double`/`float`); chip/tournament-stack counters may be `Integer`.
- **Dates** → `LocalDateTime`.
- Business methods that make sense to chain return `this` (e.g., `start()`).

---

## 5. DTO Patterns (records in the web layer)

DTOs are always `record`s in the `<domain>.web` package.

**Request** — input, with Bean Validation and conversion to the entity:

```java
public record CashGameRequest(@NotBlank String name, @NotNull LocalDateTime scheduledAt,
        @Positive BigDecimal minBuyIn, /* ... */ List<CashGamePlayerRequest> players) {

    public CashGame to() {                       // Request -> Entity
        CashGame cashGame = CashGame.builder()./*...*/.status(SCHEDULED).build();
        players().forEach(p -> cashGame.addPlayer(p.to()));
        return cashGame;
    }

    public CashGame to(String id) {              // used in update
        return to().toBuilder().id(id).build();
    }
}
```

**Response** — output, built from the entity via a static factory:

```java
public record CashGamePlayerResponse(String id, String name, BigDecimal totalInvested, BigDecimal currentStack) {
    public static CashGamePlayerResponse of(CashGamePlayer entity) { /* ... */ }
}
```

Conventions:

- Validation on `Request` fields: `@NotBlank`, `@NotNull`, `@Positive`, `@PositiveOrZero`.
- **Input** conversion: instance method `to()` (and `to(String id)` for update).
- **Output** conversion: static factory `of(entity)` (or `from(entity)` when part of a log/"flat" entity).
- `Response` translates enums to `String` with `.name()` when it exposes status.
- Nested DTOs: a `Response`/`Request` builds its children by mapping the collections (`.stream().map(Child::of).toList()`).

---

## 6. Web Layer Patterns (REST)

```java
@RestController
@AllArgsConstructor
@RequestMapping("/v1/cashgames")
class CashGameRestService {                       // package-private
    private final CashGameService service;

    @PostMapping("/new")
    public ResponseEntity<Void> createCashGame(@Valid @RequestBody CashGameRequest request) {
        String id = service.createCashGame(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }
}
```

Endpoint conventions:

- **Versioned** base path: `/v1/<resource-in-plural>` (`/v1/cashgames`, `/v1/tournaments`).
- Nested sub-resources: `/v1/cashgames/{id}/logs`, `/v1/cashgames/{id}/players`.
- Standard verbs: `GET` list/detail, `POST /new` creates, `PUT /{id}` updates, `DELETE /{id}` removes, `POST /{id}/play` starts.
- Body always `@Valid @RequestBody <...>Request`.
- Return via `ResponseEntity` (`created` with `Location`, `noContent`, `ok`) — except when the status is fixed, in which case use `@ResponseStatus(CREATED)` returning the `Response` directly.
- CORS opened only for the frontend (`WebConfiguration`, origin `http://localhost:5173`).

---

## 7. Tests

Tests are a pillar of the project and follow strict conventions. **Whenever** you change/create production code, create or update the corresponding tests following these patterns.

### 7.1 Allowed libraries (and only these)

| Use | Library | Typical import |
|-----|---------|----------------|
| Runner / annotations | JUnit 5 | `org.junit.jupiter.api.Test` |
| Assertions | **AssertJ** | `org.assertj.core.api.Assertions.*` |
| Mocks | Mockito | `org.mockito.*` |
| Object generation | **Instancio** | `org.instancio.Instancio`, `org.instancio.Select.field` |
| Controller testing | Spring Test | `@WebMvcTest`, `MockMvc`, `@MockitoBean` |
| Occasional JSON matchers | Hamcrest | `org.hamcrest.Matchers.*` (only when necessary, e.g., `startsWith`) |

> **GOLDEN RULE:** use **only** libraries already present in the project.
> Assertions are **always AssertJ** (`assertThat(...)`). **Never** use `org.junit.jupiter.api.Assertions` (`assertEquals`, `assertTrue`, etc.) nor asserts from other libs.

### 7.2 `TestBase` (mandatory base class)

Every test extends `com.gui.jblind.TestBase`:

- Initializes mocks (`MockitoAnnotations.openMocks(this)`) in `@BeforeEach`.
- **Statically mocks `UUID.randomUUID()` to `new UUID(0,0)`** → deterministic ids in equality assertions.
- Closes the `MockedStatic`s in `@AfterEach`.
- Requires implementing `void init()` — build the system under test (SUT) and the initial state here.
- Data-generation helpers:
  - `valid(Class<T>)` → an object filled via Instancio.
  - `valid(Class<T>, Integer size)` → a `List<T>`.
  - `getMockedStatic(Class<T>)` → to mock additional static methods.

### 7.3 Test types and how to write them

**a) Entity / DTO test (pure unit):**

```java
class CashGamePlayerTest extends TestBase {
    private CashGamePlayer entity;

    @Override public void init() { entity = valid(CashGamePlayer.class); }

    @Test
    void should_add_chips() {
        entity.addChips(TEN);
        assertThat(entity.getCurrentStack()).isEqualTo(/* ... */);
    }
}
```
- `Response` DTO: a `should_instantiate_from_entity` test comparing the `of(entity)` factory against a hand-built `new ...Response(...)`.

**b) Service test (unit with Mockito):**

```java
class CashGameServiceTest extends TestBase {
    private CashGameService service;
    @Mock private CashGameRepository repository;
    @Mock private CashGameLogQuery logQuery;

    @Override public void init() { service = new CashGameService(repository, logQuery); }

    @Test
    void should_play_cash_game() {
        // given (when(...).thenReturn(...))
        // when (call the service)
        // then: assertThat(...) + ORDERED interaction verification
        InOrder inOrder = inOrder(repository, logQuery);
        inOrder.verify(repository).findById(CASH_GAME_ID);
        inOrder.verify(repository).save(cashGame);
        inOrder.verifyNoMoreInteractions();
    }
}
```
- The SUT is instantiated **manually in `init()`** with the `@Mock`s in the constructor (no `@InjectMocks`).
- **Always** verify interactions with `InOrder` + `verifyNoMoreInteractions()`.
- Error scenarios use `assertThatThrownBy(...).isInstanceOf(...).hasMessage(...)`.
- "No exception" scenarios use `assertThatCode(...).doesNotThrowAnyException()`.

**c) RestService test (web slice):**

```java
@WebMvcTest(CashGameRestService.class)
class CashGameRestServiceTest extends TestBase {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private CashGameService service;

    @Override public void init() { /* empty */ }

    @Test
    void should_create_cash_game() throws Exception {
        mockMvc.perform(post("/v1/cashgames/new").contentType(APPLICATION_JSON).content("""
                { "name": "..." }
                """))
            .andExpect(status().isCreated());
        // + InOrder on the mocked service
    }
}
```
- `@WebMvcTest(<RestService>.class)` isolates only the controller; the dependency (`Service`) is `@MockitoBean`.
- JSON bodies written as **text blocks** (`"""..."""`).
- Response assertions via `jsonPath(...)`; for `BigDecimal` compare with `.doubleValue()`; use Hamcrest (`startsWith`) only when necessary.

**d) `TemplateLoader` (scenario factories):**

`*TemplateLoader` classes centralize creating objects in specific states using Instancio + `Select.field`:

```java
class CashGameTemplateLoader {
    static CashGame scheduled() {
        return Instancio.of(CashGame.class).set(field(CashGame::getStatus), SCHEDULED).create();
    }
    static CashGame finished() { /* ... FINISHED ... */ }
}
```
Reuse/extend these loaders instead of duplicating state setup across tests.

### 7.4 General testing conventions

- Method names in **snake_case** starting with `should_...` (e.g., `should_throw_exception_when_cash_game_not_found_by_id`).
- Test classes are `package-private` and mirror the package/name of the class under test with a `Test` suffix.
- One test per behavior; cover **happy path + negative scenarios + edge cases** (not found, already finished, empty list, etc.).

---

## 8. Build, Formatting, and Execution

Always use the Maven wrapper.

```powershell
# Compile
.\mvnw.cmd clean compile

# Run ALL tests (mandatory before considering the task complete)
.\mvnw.cmd test

# Run a specific test
.\mvnw.cmd test "-Dtest=CashGameServiceTest"

# Start the application
.\mvnw.cmd spring-boot:run
```

- **Formatting:** the `spring-javaformat-maven-plugin` runs in the `validate` phase and **fails the build** if the style is incorrect. The Spring style uses **tabs** for indentation and a width of ~120 columns. Follow the exact format of the existing files; if needed, apply `.\mvnw.cmd spring-javaformat:apply`.
- **Execution prerequisite:** a local PostgreSQL (`jdbc:postgresql://localhost:5432/JBlind`, user/password `postgres`). Web/unit tests do **not** require a database.
- `ddl-auto: update` — the schema is generated/updated by Hibernate.

---

## 9. Checklist for the Agent (before finishing)

1. Did I follow **package-by-feature**? Did nothing new cross the boundary between `cashgame` and `tournament`?
2. Is the entity still **rich and setter-free**, with business rules as methods?
3. Did conversions stay in the **DTOs** (`to()` / `of()` / `from()`), and the entity did **not** leak through the API?
4. Is the `Service` `@Transactional` (and `readOnly = true` on reads)?
5. Is the RestService still thin, `package-private`, under `/v1/...`?
6. Did I create/update tests using `TestBase`, **AssertJ**, Mockito `InOrder` + `verifyNoMoreInteractions`, and Instancio? **No** new libraries.
7. Did I cover negative scenarios and edge cases (not found, invalid state, empty list)?
8. Does the build pass: `.\mvnw.cmd clean test` green and formatting (`spring-javaformat`) OK?
9. **Did I keep `AGENTS.md` in sync?** If the change introduces a new feature or contradicts anything documented here, update the relevant sections in the same change.
