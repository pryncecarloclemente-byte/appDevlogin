# Project Files Summary

## Created Files for Submission

### Kotlin Files (5 files)
1. ✅ `app/src/main/java/com/example/appdev_login/User.kt`
   - Data class for User model

2. ✅ `app/src/main/java/com/example/appdev_login/DatabaseHelper.kt`
   - SQLite database helper with all CRUD operations
   - Methods: insertUser(), checkUser(), checkEmailExists(), getAllUsers()

3. ✅ `app/src/main/java/com/example/appdev_login/LoginActivity.kt`
   - Login screen functionality
   - Validates email and password

4. ✅ `app/src/main/java/com/example/appdev_login/RegisterActivity.kt`
   - Registration screen functionality
   - Validates all fields and checks for duplicate emails

5. ✅ `app/src/main/java/com/example/appdev_login/DatabaseViewActivity.kt`
   - Displays all users in a table format
   - Shows ID, Username, Email, and masked Password

### XML Layout Files (3 files)
1. ✅ `app/src/main/res/layout/activity_login.xml`
   - Login screen UI with email/password fields
   - Links to Register and Database View

2. ✅ `app/src/main/res/layout/activity_register.xml`
   - Registration screen UI with username, email, password fields
   - Link back to Login

3. ✅ `app/src/main/res/layout/activity_database_view.xml`
   - Table layout displaying all users
   - Scrollable table with header row
   - Back button

### SQL File (1 file)
✅ `database_schema.sql`
   - Complete database schema
   - CREATE TABLE statement for users table
   - Sample queries and comments

### Configuration Files (Modified)
- `app/build.gradle.kts` - Added viewBinding and dependencies
- `gradle/libs.versions.toml` - Added activity and constraintlayout versions
- `app/src/main/AndroidManifest.xml` - Registered all 3 activities

## Total Files Created/Modified: 12

## Application Flow
1. App starts → Login Screen (LoginActivity)
2. Click "Register" → Registration Screen (RegisterActivity)
3. After registration → Back to Login Screen
4. After login → Database View Screen (DatabaseViewActivity)
5. Click "View Database" from Login → Database View Screen

## Database Features
- SQLite database: `UserDatabase.db`
- Table: `users`
- Auto-increment ID
- Unique email constraint
- Password storage (in real app, should be hashed)

## Screenshots Needed (as per requirement)
1. 📸 Login Screen
2. 📸 Registration Screen  
3. 📸 Database View Screen (with sample registered users)

## How to Take Screenshots
1. Run the app in Android Studio
2. Register 2-3 sample users
3. Take screenshots using:
   - Android Studio: Tools → Device Manager → Camera icon
   - Emulator: Ctrl + S (Windows) or Cmd + S (Mac)
   - Physical device: Volume Down + Power button

## Submission Checklist
- [x] 5 Kotlin (.kt) files
- [x] 3 XML layout files
- [x] 1 SQL schema file
- [ ] 3 Screenshots (to be taken after running the app)
