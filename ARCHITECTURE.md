# App Architecture & Flow

## Screen Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     LOGIN SCREEN                            │
│                   (LoginActivity)                           │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Email:    [________________]                       │   │
│  │  Password: [________________]                       │   │
│  │                                                      │   │
│  │            [    LOGIN BUTTON    ]                   │   │
│  │                                                      │   │
│  │  → Don't have an account? Register                  │   │
│  │  → View Database                                    │   │
│  └─────────────────────────────────────────────────────┘   │
└───────────────┬─────────────────────────┬───────────────────┘
                │                         │
                │ Click Register          │ Click View Database
                │ or Login Success        │
                ↓                         ↓
    ┌───────────────────────┐   ┌─────────────────────────┐
    │  REGISTER SCREEN      │   │  DATABASE VIEW SCREEN   │
    │  (RegisterActivity)   │   │  (DatabaseViewActivity) │
    │  ┌─────────────────┐  │   │  ┌───────────────────┐  │
    │  │ Username:  [__] │  │   │  │ ┏━━━━━━━━━━━━━━┓ │  │
    │  │ Email:     [__] │  │   │  │ ┃ ID │ User... ┃ │  │
    │  │ Password:  [__] │  │   │  │ ┣━━━━━━━━━━━━━━┫ │  │
    │  │ Confirm:   [__] │  │   │  │ ┃ 1  │ John... ┃ │  │
    │  │                 │  │   │  │ ┃ 2  │ Jane... ┃ │  │
    │  │ [REGISTER]      │  │   │  │ ┃ 3  │ Bob...  ┃ │  │
    │  │                 │  │   │  │ ┗━━━━━━━━━━━━━━┛ │  │
    │  │ → Login         │  │   │  │                   │  │
    │  └─────────────────┘  │   │  │ [BACK TO LOGIN]   │  │
    └───────────┬───────────┘   │  └───────────────────┘  │
                │               └─────────────────────────┘
                │ Success
                ↓
        Back to Login Screen
```

## Component Architecture

```
┌──────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                      │
├──────────────────────────────────────────────────────────┤
│  LoginActivity          RegisterActivity                  │
│  activity_login.xml     activity_register.xml            │
│                                                           │
│  DatabaseViewActivity                                     │
│  activity_database_view.xml                              │
└────────────────────────┬─────────────────────────────────┘
                         │ Uses
                         ↓
┌──────────────────────────────────────────────────────────┐
│                     DATA LAYER                            │
├──────────────────────────────────────────────────────────┤
│  DatabaseHelper.kt                                        │
│  ├─ insertUser()                                         │
│  ├─ checkUser()                                          │
│  ├─ checkEmailExists()                                   │
│  └─ getAllUsers()                                        │
└────────────────────────┬─────────────────────────────────┘
                         │ Manages
                         ↓
┌──────────────────────────────────────────────────────────┐
│                   MODEL LAYER                             │
├──────────────────────────────────────────────────────────┤
│  User.kt (Data Class)                                    │
│  └─ id, username, email, password                        │
└────────────────────────┬─────────────────────────────────┘
                         │ Stored in
                         ↓
┌──────────────────────────────────────────────────────────┐
│                 SQLite DATABASE                           │
├──────────────────────────────────────────────────────────┤
│  UserDatabase.db                                         │
│  Table: users                                            │
│  Columns: id, username, email, password                  │
└──────────────────────────────────────────────────────────┘
```

## Database Operations Flow

### Registration Flow
```
User Input → RegisterActivity
              ↓
Check if email exists (checkEmailExists)
              ↓
         [If exists]     [If not exists]
              ↓                ↓
    Show error toast    Insert user (insertUser)
                             ↓
                        Show success toast
                             ↓
                    Navigate to Login
```

### Login Flow
```
User Input → LoginActivity
              ↓
Check credentials (checkUser)
              ↓
    [Valid]         [Invalid]
       ↓                ↓
Success toast    Error toast
       ↓
Navigate to DatabaseView
```

### Database View Flow
```
DatabaseViewActivity launched
              ↓
Get all users (getAllUsers)
              ↓
    [Has users]       [No users]
         ↓                 ↓
Display in table    Show "No users" message
         ↓
User can scroll and view
         ↓
Click back → LoginActivity
```

## File Structure

```
appDevlogin/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/appdev_login/
│   │   │   │   ├── User.kt                    ← Data Model
│   │   │   │   ├── DatabaseHelper.kt          ← SQLite Helper
│   │   │   │   ├── LoginActivity.kt           ← Screen 1
│   │   │   │   ├── RegisterActivity.kt        ← Screen 2
│   │   │   │   └── DatabaseViewActivity.kt    ← Screen 3
│   │   │   ├── res/
│   │   │   │   └── layout/
│   │   │   │       ├── activity_login.xml     ← UI for Screen 1
│   │   │   │       ├── activity_register.xml  ← UI for Screen 2
│   │   │   │       └── activity_database_view.xml ← UI for Screen 3
│   │   │   └── AndroidManifest.xml            ← App configuration
│   │   └── build.gradle.kts                   ← Dependencies
├── database_schema.sql                        ← SQL Schema
├── README.md                                  ← Documentation
├── PROJECT_SUMMARY.md                         ← File listing
└── TESTING_GUIDE.md                          ← Testing steps
```

## Key Features Implementation

### Security Features
- ✓ Email uniqueness constraint in database
- ✓ Password confirmation during registration
- ✓ Password masking in UI (dots)
- ✓ Password masking in database view

### Validation Features
- ✓ Empty field validation
- ✓ Password match validation
- ✓ Duplicate email check
- ✓ User feedback via Toast messages

### User Experience Features
- ✓ Navigation between screens
- ✓ Clear error messages
- ✓ Success confirmations
- ✓ Scrollable database table
- ✓ Back button navigation

### Database Features
- ✓ CRUD operations (Create, Read)
- ✓ Auto-increment ID
- ✓ Unique constraint on email
- ✓ Proper data types
- ✓ Query methods for different operations
