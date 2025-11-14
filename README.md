# Android Login & Registration App with SQLite Database

## Overview
This is a complete Android application with user authentication and database management features.

## Features
1. **Login Screen** - Allows users to login with email and password
2. **Registration Screen** - New users can register with username, email, and password
3. **Database View Screen** - Displays all registered users in a table format

## Project Structure

### Kotlin Files (`.kt`)
- `User.kt` - Data model for User entity
- `DatabaseHelper.kt` - SQLite database helper class with CRUD operations
- `LoginActivity.kt` - Login screen activity
- `RegisterActivity.kt` - Registration screen activity
- `DatabaseViewActivity.kt` - Database view screen with table display

### XML Layout Files
- `activity_login.xml` - Login screen layout
- `activity_register.xml` - Registration screen layout
- `activity_database_view.xml` - Database view screen layout with table

### Database
- `database_schema.sql` - SQL schema file with database structure

## Database Schema

**Table: users**
- `id` - INTEGER PRIMARY KEY AUTOINCREMENT
- `username` - TEXT NOT NULL
- `email` - TEXT NOT NULL UNIQUE
- `password` - TEXT NOT NULL

## Features Implemented

### DatabaseHelper.kt
- `insertUser()` - Register new user
- `checkUser()` - Validate login credentials
- `checkEmailExists()` - Check if email is already registered
- `getAllUsers()` - Fetch all users for display

### Login Screen
- Email and password input fields
- Login button with validation
- Link to registration screen
- Link to view database

### Registration Screen
- Username, email, password, and confirm password fields
- Input validation (all fields required, passwords must match)
- Email uniqueness validation
- Success/failure feedback

### Database View Screen
- Displays all registered users in a table
- Shows ID, Username, Email, and masked Password
- Scrollable table for large datasets
- Back button to return to login

## How to Run
1. Open the project in Android Studio
2. Sync Gradle files
3. Run the app on an emulator or physical device
4. Register a new user or view the database

## Screenshots Required
1. Login Screen
2. Registration Screen
3. Database View Screen (with sample data)

## Testing Steps
1. Launch the app (Login screen appears)
2. Click "Don't have an account? Register"
3. Fill registration form and submit
4. Login with registered credentials
5. View database to see registered users

## Files to Submit
✅ **Kotlin Files:**
- User.kt
- DatabaseHelper.kt
- LoginActivity.kt
- RegisterActivity.kt
- DatabaseViewActivity.kt

✅ **XML Files:**
- activity_login.xml
- activity_register.xml
- activity_database_view.xml

✅ **SQL File:**
- database_schema.sql

✅ **Screenshots:**
- Login screen
- Registration screen
- Database view screen

## Notes
- Passwords are displayed as masked (••••••••) in the database view for security
- Email must be unique (database constraint)
- All validations are implemented with user feedback
