# School Management System (JavaFX)

This is desktop School Management System project built in Java using OOP and JavaFX.  
Data is saved in plain text files inside the `data` folder.

## Group members
- Humna Mustafa (FA25-BSE-051)
- Ayesha Eman (FA25-BSE-024)

## Main features
- First run setup (school details + first admin account)
- Admin login, signup, forgot password (security answer)
- Dashboard counts (students, teachers, subjects, fees)
- CRUD panels: Students, Teachers, Subjects, Fees
- Settings: edit school profile, change password, delete account (with last-admin protection)

## Tech
- Java, OOP
- JavaFX 
- File based storage (`school.txt`, `users.txt`, `students.txt`, `teachers.txt`, `subjects.txt`, `fees.txt`)

## How to run
- Open the project in IntelliJ IDEA
- Wait for Maven import to finish
- Open `Main.java` and press the green **Run** button

## Work division
We built the project in two parts and then integrated into one application.

### Humna Mustafa (FA25-BSE-051) - Data Layer + Student/Teacher CRUD + Login
**GUI (4):** `LoginView` [H], `ForgotPasswordView` [M], `StudentPanel` [H], `TeacherPanel` [H]
**Backend (6):** `EntityRecord` [M], `FileManager` [M], `DataStore` [M], `IdGenerator` [M], `InputValidator` [E], `AlertUtil` [E]

### Ayesha Eman (FA25-BSE-024) - Auth Layer + Fee CRUD + Signup + Navigation
**GUI (4):** `SignupView` [H], `FeePanel` [H], `AppContext` [E], `Main` [H]
**Backend (6):** `User` [M], `AuthService` [H], `CrudService` [E], `ThemeUtil` [M], `Constants` [E], `SessionManager` [M]

**How we integrate (one paragraph):** Ayesha's `Main` is the entry point — it creates Humna's `FileManager`, `DataStore` and Ayesha's `AuthService`, `CrudService` instances, then bundles them in Ayesha's `AppContext` record and hands them to Humna's `LoginView`; after a successful `AuthService.login()` call, `SessionManager` (Ayesha) stores the user and a Runnable callback opens the dashboard built in `Main`, where the sidebar swaps between Humna's `StudentPanel`/`TeacherPanel` and Ayesha's `FeePanel`; every panel calls its injected `CrudService` (Ayesha) → `DataStore` (Humna) → `FileManager` (Humna) → `data/*.txt`, so the two halves meet through `AppContext`, `EntityRecord` and the shared utilities (`AlertUtil`, `InputValidator`, `IdGenerator`, `ThemeUtil`, `Constants`).
