# File Organizer System (StorageManagment)

## Title
File Organizer System (StorageManagment)

## Description
This project is a Java/JavaFX desktop application that provides a small file organizer and user management system. It offers user authentication (signup/login), simple file metadata management through a UI, and persistent storage using a local SQLite database (file `storage_manager.db` in the `target/` folder). The codebase uses a layered structure with controllers (JavaFX), DAOs for database access, domain models, and utility classes for common tasks and navigation.

Key packages and files (from `src/main/java/com/aau/storagemanagment`):
- `MainApp`, `Launcher`, and `module-info.java` — application bootstrap and module declarations.
- Controllers: `LoginController`, `SignupController`, `MainController` — handle UI logic.
- Views (FXML): `login-view.fxml`, `signup-view.fxml`, `main-view.fxml`, `hello-view.fxml`.
- DAO: `UserDAO`, `FileDAO` — database access objects.
- DB: `DatabaseConnection` — handles SQLite connection.
- Model: `User`, `FileModel` — domain objects.
- Util: `FileUtil`, `NavigationUtil`, `UserSession` — helper and session management classes.

## Architecture / UML
Below is a simplified UML class diagram (PlantUML) describing the main classes and their relationships. You can paste this into a PlantUML renderer (or online PlantUML live server) to visualize it.


If you don't have PlantUML set up, here's an ASCII-style class-summary for quick reference:

- MainApp / Launcher
  - bootstrap application and load primary stage

- LoginController
  - input: username/email, password
  - uses: UserDAO, UserSession, NavigationUtil
  - responsibilities: authenticate user, navigate on success

- SignupController
  - input: user fields
  - uses: UserDAO, NavigationUtil
  - responsibilities: create new user, validation, navigation

- MainController
  - input: file management actions (add, remove, list)
  - uses: FileDAO, FileUtil, FileCard, UserSession
  - responsibilities: show user's files, manage file metadata

- UserDAO / FileDAO
  - uses: DatabaseConnection
  - responsibilities: CRUD operations on `users` and `files` tables

- DatabaseConnection
  - responsibilities: open/close SQLite connection, initialize DB if needed

- Models: `User`, `FileModel`
  - represent application domain objects persisted via DAO layers

## System features
- User authentication
  - Signup new users with basic validation
  - Login for returning users
  - Session management using `UserSession`

- File metadata management
  - Add file entries (metadata; actual file handling may be via `FileUtil`)
  - List and display files in UI using `FileCard`
  - Delete or update file metadata (if implemented in `FileDAO`)

- Persistence
  - SQLite database accessed through `DatabaseConnection`
  - DAOs (`UserDAO`, `FileDAO`) encapsulate SQL

- Navigation / UX
  - JavaFX-based views (FXML) with controllers that manage view transitions via `NavigationUtil`

## Data model (high-level)
- User
  - typical fields: id (int/long), username/email (String), passwordHash (String), createdAt

- FileModel
  - typical fields: id, ownerUserId, originalFileName, storedPath (optional), size, tags/description, createdAt

(Exact field names and types can be found in `src/main/java/com/aau/storagemanagment/model`.)

## How to run (local development)
Assumptions: JDK 11+ or a version matching `module-info.java` and Maven is available. This project includes the Maven wrapper (`mvnw`) so you can run without a global Maven installation.

From the project root run:

```bash
# Make wrapper executable if needed
chmod +x mvnw

# Compile and run using the Maven wrapper
./mvnw clean javafx:run
```

Or build a jar and run (if the project is configured to produce a runnable jar):

```bash
./mvnw clean package
java -jar target/StorageManagment-1.0-SNAPSHOT.jar
```

Note: If the above artifact name differs, check the `target/` folder or `pom.xml` for the produced JAR name.

## Notes & assumptions
- The repository includes a `storage_manager.db` file under `target/`—this indicates an SQLite DB is used. If you need to reset state, remove or rename that file; the application may recreate it if initialization logic exists in `DatabaseConnection`.
- The project appears to be Java module-based (has `module-info.java`) and uses JavaFX. Make sure JavaFX runtime libraries are available (Maven should pull them if configured in `pom.xml`).
- File handling: the app likely stores only metadata in the DB; `FileUtil` appears to assist with copying, storing, or opening actual files. Review `FileUtil` for exact behavior.

## Suggested next steps (small improvements)
- Add a UML sequence diagram for the login flow (could be generated from the controllers/DAO flows).
- Add a brief contributor guide and development setup to `README.md` (JDK version, Maven wrapper usage, environment variables if any).
- Add unit tests for DAOs using an in-memory SQLite fixture or test DB file.

---

If you'd like, I can also:
- generate PNG/SVG UML diagrams (render PlantUML) and add them to the `docs/` folder; or
- open and extract exact model fields and SQL schema from the DAO/DatabaseConnection and include a precise data dictionary.

Which of those would you like next?