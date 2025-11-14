# Quick Testing Guide

## Before Running
1. Open Android Studio
2. Sync Gradle (File → Sync Project with Gradle Files)
3. Wait for sync to complete

## Testing the App

### Step 1: Launch App
- Click Run button (green play icon) or press Shift+F10
- Login screen should appear

### Step 2: Test Registration
1. Click "Don't have an account? Register"
2. Fill in the form:
   - Username: `John Doe`
   - Email: `john@example.com`
   - Password: `password123`
   - Confirm Password: `password123`
3. Click "Register" button
4. Should see "Registration Successful" toast
5. App navigates back to Login screen

### Step 3: Test Duplicate Email Validation
1. Click "Register" again
2. Try to register with same email: `john@example.com`
3. Should see "Email already exists" message

### Step 4: Test Login with Wrong Credentials
1. On Login screen, enter:
   - Email: `john@example.com`
   - Password: `wrongpassword`
2. Click "Login"
3. Should see "Invalid email or password" message

### Step 5: Test Successful Login
1. On Login screen, enter:
   - Email: `john@example.com`
   - Password: `password123`
2. Click "Login"
3. Should see "Login Successful" toast
4. App navigates to Database View screen

### Step 6: Test Database View
1. Should see table with header (ID, Username, Email, Password)
2. Should see one row with John's data
3. Password should be masked (••••••••)
4. Click "Back to Login" button

### Step 7: Direct Database Access
1. From Login screen, click "View Database"
2. Should see the database view with all registered users

### Step 8: Register More Users
Register 2-3 more users to populate the database:
- Jane Smith / jane@example.com / pass456
- Bob Wilson / bob@example.com / bob789
- Alice Brown / alice@example.com / alice321

### Step 9: View Full Database
1. Click "View Database" from Login screen
2. Should see all registered users in the table
3. Take screenshot for submission

## Expected Behavior

### Registration Validation
- ✅ All fields required
- ✅ Passwords must match
- ✅ Email must be unique
- ✅ Success message on valid registration

### Login Validation
- ✅ All fields required
- ✅ Email and password must match a registered user
- ✅ Success message on valid login

### Database View
- ✅ Shows all registered users
- ✅ Displays in table format
- ✅ Passwords are masked
- ✅ Can scroll horizontally and vertically if needed

## Screenshots to Capture

1. **Login Screen** (empty or with sample data in fields)
2. **Registration Screen** (empty or with sample data in fields)
3. **Database View Screen** (with at least 3 registered users visible)

## Common Issues

### Build Errors
- Make sure to sync Gradle first
- Check that all dependencies are downloaded

### App Crashes
- Check Logcat for error messages
- Ensure Android SDK is properly installed

### Database Not Showing Users
- Make sure you've registered at least one user
- Check that registration was successful (toast message)

## Database Location (for debugging)
The SQLite database is stored at:
```
/data/data/com.example.appdev_login/databases/UserDatabase.db
```

You can view it using Android Studio's Database Inspector:
- View → Tool Windows → App Inspection → Database Inspector

## Testing Complete! ✅
After completing all steps and taking screenshots, you'll have everything needed for submission.
