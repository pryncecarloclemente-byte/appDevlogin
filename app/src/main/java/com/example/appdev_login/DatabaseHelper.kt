package com.example.appdev_login

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        // If a prebuilt SQLite DB asset is provided, copy it into place before any DB operations.
        try {
            copyPrepopulatedDatabaseIfNeeded()
        } catch (e: Exception) {
            if (AppConfig.ENABLE_DB_DEBUG) android.util.Log.w("DB", "copyPrepopulatedDatabaseIfNeeded failed: ${e.message}")
        }
    }

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_ROLE = "role"
        // Default admin credentials (change if you want)
        private const val DEFAULT_ADMIN_USERNAME = "admin"
        private const val DEFAULT_ADMIN_EMAIL = "admin@local"
        private const val DEFAULT_ADMIN_PASSWORD = "admin123"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Try to load SQL initialization script from assets/db_init.sql if present.
        try {
            val assetManager = context.assets
            assetManager.open("db_init.sql").bufferedReader().use { reader ->
                val sqlContent = reader.readText()
                // Split statements by semicolon; ignore empty statements
                val statements = sqlContent.split(";")
                db?.let { database ->
                    database.beginTransaction()
                    try {
                        for (stmt in statements) {
                            val s = stmt.trim()
                            if (s.isNotEmpty()) {
                                database.execSQL(s)
                            }
                        }
                        database.setTransactionSuccessful()
                    } finally {
                        database.endTransaction()
                    }
                }
            }
        } catch (e: Exception) {
            // If asset isn't present or something fails, fall back to programmatic creation
            if (AppConfig.ENABLE_DB_DEBUG) android.util.Log.w("DB", "Failed to load assets/db_init.sql, falling back to default create. Error: ${e.message}")
            val createTableQuery = """
                CREATE TABLE $TABLE_USERS (
                    $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_USERNAME TEXT NOT NULL,
                    $COLUMN_EMAIL TEXT NOT NULL UNIQUE,
                    $COLUMN_PASSWORD TEXT NOT NULL,
                    $COLUMN_ROLE TEXT NOT NULL DEFAULT 'guest'
                )
            """.trimIndent()
            db?.execSQL(createTableQuery)
            // Insert a default admin account so there's an admin to log in with
            db?.let {
                val cv = ContentValues().apply {
                    put(COLUMN_USERNAME, DEFAULT_ADMIN_USERNAME)
                    put(COLUMN_EMAIL, DEFAULT_ADMIN_EMAIL)
                    put(COLUMN_PASSWORD, DEFAULT_ADMIN_PASSWORD)
                    put(COLUMN_ROLE, "admin")
                }
                it.insertWithOnConflict(TABLE_USERS, null, cv, SQLiteDatabase.CONFLICT_IGNORE)
            }
        }
    }

    // If you bundle a binary SQLite DB in assets named 'prepopulated.db', this method
    // will copy it to the app's databases folder so the app uses that DB instead of
    // creating one programmatically. If the asset isn't present, nothing happens.
    private fun copyPrepopulatedDatabaseIfNeeded() {
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        if (dbFile.exists()) {
            if (AppConfig.ENABLE_DB_DEBUG) android.util.Log.d("DB", "Database file already exists at ${dbFile.absolutePath}")
            return
        }

        try {
            // Prefer a .sqlite3 asset; if not present, fall back to prepopulated.db
            val candidateNames = listOf("prepopulated.sqlite3", "prepopulated.db")
            var found = false
            for (name in candidateNames) {
                try {
                    context.assets.open(name).use { input ->
                        // Read first bytes to detect whether this is a binary sqlite file
                        val header = ByteArray(16)
                        val read = input.read(header)
                        input.reset()
                        val isBinarySqlite = read >= 16 && String(header, 0, 16) == "SQLite format 3\u0000"

                        if (isBinarySqlite) {
                            // It's a binary sqlite DB; copy directly
                            dbFile.parentFile?.let { parent -> if (!parent.exists()) parent.mkdirs() }
                            dbFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                            if (AppConfig.ENABLE_DB_DEBUG) android.util.Log.d("DB", "Copied binary $name to ${dbFile.absolutePath}")
                        } else {
                            // Treat as SQL text; execute statements to create DB
                            input.bufferedReader().use { reader ->
                                val sqlContent = reader.readText()
                                val statements = sqlContent.split(";")
                                val database = android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(dbFile, null)
                                database.beginTransaction()
                                try {
                                    for (stmt in statements) {
                                        val s = stmt.trim()
                                        if (s.isNotEmpty()) {
                                            database.execSQL(s)
                                        }
                                    }
                                    database.setTransactionSuccessful()
                                } finally {
                                    database.endTransaction()
                                    database.close()
                                }
                            }
                            if (AppConfig.ENABLE_DB_DEBUG) android.util.Log.d("DB", "Created DB from SQL asset $name at ${dbFile.absolutePath}")
                        }
                        found = true
                    }
                    if (found) break
                } catch (fnf: java.io.FileNotFoundException) {
                    // try next candidate
                }
            }
            if (!found) {
                if (AppConfig.ENABLE_DB_DEBUG) android.util.Log.d("DB", "No prepopulated asset found (tried ${candidateNames.joinToString()}); will use SQL init or programmatic creation.")
            }
        } catch (e: java.io.FileNotFoundException) {
            // Asset not present; not an error — we'll fall back to SQL init or programmatic create
            if (AppConfig.ENABLE_DB_DEBUG) android.util.Log.d("DB", "No prepopulated.db asset found; will use SQL init or programmatic creation.")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Add role column with default 'guest' when upgrading from v1 to v2
            try {
                db?.execSQL("ALTER TABLE $TABLE_USERS ADD COLUMN $COLUMN_ROLE TEXT NOT NULL DEFAULT 'guest'")
            } catch (e: Exception) {
                // If alter fails for any reason, fallback to rebuild table
                db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
                onCreate(db)
            }
        } else {
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
            onCreate(db)
        }
    }

    // Insert user into database
    fun insertUser(username: String, email: String, password: String, role: String = "guest"): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_ROLE, role)
        }
        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result
    }

    // Check if user exists (for login)
    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        val exists = cursor.count > 0
        if (AppConfig.ENABLE_DB_DEBUG) android.util.Log.d("DB", "checkUser query: $query -> exists=$exists for email=$email")
        cursor.close()
        db.close()
        return exists
    }

    // Return user by email and password (including role)
    fun getUserByEmailAndPassword(email: String, password: String): User? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        var user: User? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
            val emailVal = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val passwordVal = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            val roleVal = try { cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)) } catch (e: Exception) { "guest" }
            user = User(id, username, emailVal, passwordVal, roleVal)
            if (AppConfig.ENABLE_DB_DEBUG) android.util.Log.d("DB", "getUser found: id=$id email=$emailVal role=$roleVal")
        }
        if (AppConfig.ENABLE_DB_DEBUG) android.util.Log.d("DB", "getUserByEmailAndPassword executed, cursorCount=${cursor.count}")
        cursor.close()
        db.close()
        return user
    }

    // Check if email already exists (for registration)
    fun checkEmailExists(email: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // Get all users
    fun getAllUsers(): List<User> {
        val userList = mutableListOf<User>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_USERS", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
                val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
                val role = try { cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)) } catch (e: Exception) { "guest" }
                userList.add(User(id, username, email, password, role))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }

    // Update user (admin only should call this)
    fun updateUser(id: Int, username: String, email: String, password: String, role: String = "guest"): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_ROLE, role)
        }
        val rows = db.update(TABLE_USERS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    // Delete user by id
    fun deleteUser(id: Int): Int {
        val db = this.writableDatabase
        val rows = db.delete(TABLE_USERS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }
}
