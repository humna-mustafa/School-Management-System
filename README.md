# School Management System (JavaFX)

This is a small desktop School Management System project built in Java using OOP and JavaFX.  
Data is saved in plain text files inside the `data` folder.

Professor: [Muhammad Shahid Bhatti](https://github.com/mshahidbhatti)

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

## Work division (2 members)
We built the project in two parts and then integrated into one application.

### Humna Mustafa
- UI panels: `StudentPanel`, `TeacherPanel`, `FeePanel`
- Data layer support: records format handling and id generation usage in these panels
- Testing: add, update, delete flows for students, teachers, fees

### Ayesha Eman
- UI panels: `SubjectPanel`, `SettingsPanel`, `DashboardPanel`
- Auth screens: `LoginView`, `SignupView`, `ForgotPasswordView`, `SchoolSetupView`
- Theme and UI consistency: shared color palette and button styles


