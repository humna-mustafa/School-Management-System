# School Management System (JavaFX)

This is desktop School Management System project built in Java using OOP and JavaFX.  
Data is saved in plain text files inside the `data` folder.

## Group members
- Humna Mustafa (FA25-BSE-051)
- Ayesha Eman (FA25-BSE-024)

## Main features
- First run setup (first admin account)
- Admin login, signup, forgot password (security answer)
- Dashboard counts (students, teachers, fees)
- CRUD panels: Students, Teachers, Fees


## Tech
- Java, OOP
- JavaFX 
- File based storage (`school.txt`, `users.txt`, `students.txt`, `teachers.txt`, `fees.txt`)

## How to run
- Open the project in IntelliJ IDEA
- Wait for Maven import to finish
- Open `Main.java` and press the green **Run** button

## Work division
We built the project in two parts and then integrated into one application.

### Humna Mustafa (FA25-BSE-051)
**GUI (4):** `LoginView`, `ForgotPasswordView`, `StudentPanel`, `TeacherPanel`  
**Backend (6):** `EntityRecord`, `FileManager`, `DataStore`, `IdGenerator`, `InputValidator`, `AlertUtil`

### Ayesha Eman (FA25-BSE-024)
**GUI (4):** `SignupView`, `FeePanel`, `AppContext`, `Main`  
**Backend (6):** `User`, `AuthService`, `CrudService`, `ThemeUtil`, `Constants`, `SessionManager`
