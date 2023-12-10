package com.example.wishlist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.IllegalStateException


data class User(
    val username: String,
    val password: String
)

data class Wishes(
    val id: Int,
    val wishname: String,
    val wishlink: String,
    val wishdesc: String,
    val category: String
)
class DatabaseHandler (private val context: Context) : SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 10
        private const val DATABASE_NAME = "Wishdex.db"

        private const val TABLE_USERS = "users"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"

        private const val TABLE_WISHES = "wishes"
        private const val KEY_WISH_ID = "id"
        private const val KEY_WISH_NAME = "wishname"
        private const val KEY_WISH_LINK = "link"
        private const val KEY_WISH_DESCRIPTION = "description"
        private const val KEY_WISH_CATEGORY = "category"
        private const val KEY_WISH_USERNAME_FK = "user_fk"

        private const val PREFS_NAME = "MyPrefs"
        private const val KEY_LOGGED_IN_USER = "loggedInUser"

    }


    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USERS_TABLE = ("CREATE TABLE $TABLE_USERS (" +
                "$KEY_USERNAME TEXT PRIMARY KEY," +
                "$KEY_PASSWORD TEXT)")

        db?.execSQL(CREATE_USERS_TABLE)

        val CREATE_WISHES_TABLE = ("CREATE TABLE IF NOT EXISTS $TABLE_WISHES (" +
                "$KEY_WISH_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_WISH_NAME TEXT," +
                "$KEY_WISH_LINK TEXT," +
                "$KEY_WISH_DESCRIPTION TEXT," +
                "$KEY_WISH_CATEGORY TEXT," +
                "$KEY_WISH_USERNAME_FK TEXT," +
                "FOREIGN KEY($KEY_WISH_USERNAME_FK) REFERENCES $TABLE_USERS($KEY_USERNAME))")

        db?.execSQL(CREATE_WISHES_TABLE)
    }

override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    db!!.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
    db.execSQL("DROP TABLE IF EXISTS $TABLE_WISHES")
    onCreate(db) }

    fun setLoggedInUser(username: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_LOGGED_IN_USER, username)
        editor.apply()
    }

    fun getLoggedInUser(): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_LOGGED_IN_USER, null)
    }
    fun createWish(
        wishname: String,
        wishlink: String,
        wishdesc: String,
        category: String
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_WISH_NAME, wishname)
            put(KEY_WISH_DESCRIPTION, wishdesc) // Assign wishdesc to description
            put(KEY_WISH_LINK, wishlink) // Assign wishlink to link
            put(KEY_WISH_CATEGORY, category)

            val loggedInUser = getLoggedInUser()

            if (loggedInUser != null) {
                put(KEY_WISH_USERNAME_FK, loggedInUser)
            } else {
                throw IllegalStateException("No user logged in.")
            }
        }

        val successwish = db.insert(TABLE_WISHES, null, values)
        db.close()

        return successwish
    }



    fun registerUser(username: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_USERNAME, username)
            put(KEY_PASSWORD, password)
        }

        val success = db.insert(TABLE_USERS, null, values)
        db.close()

        return success
    }

    fun loginUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "$KEY_USERNAME = ? AND $KEY_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null)

        val userExists = cursor.moveToFirst() && cursor.count > 0
        cursor.close()

        return userExists
    }

    fun getUserInfo(username: String): User? {
        val db = this.readableDatabase
        val selection = "$KEY_USERNAME = ?"
        val selectionArgs = arrayOf(username)
        val cursor: Cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null)

        return cursor.use {
            if (it.moveToFirst()) {
                val usernameIndex = it.getColumnIndex(KEY_USERNAME)
                val passwordIndex = it.getColumnIndex(KEY_PASSWORD)

                if (usernameIndex != -1 && passwordIndex != -1) {
                    User(
                        it.getString(usernameIndex),
                        it.getString(passwordIndex)
                    )
                } else {
                    null
                }
            } else {
                null
            }
        }
    }
    fun getAllWishes(): List<Wishes> {
        val wishList = mutableListOf<Wishes>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_WISHES"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_WISH_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_NAME))
            val link = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_LINK))
            val desc = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_DESCRIPTION))
            val cat = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_CATEGORY))

            val wish = Wishes(id, name, link, desc, cat)
            wishList.add(wish)
        }
        cursor.close()
        db.close()
        return wishList
    }

    fun getWishesByCategory(category: String): List<Wishes> {
        val wishList = mutableListOf<Wishes>()
        val db = this.readableDatabase
        val loggedInUser = getLoggedInUser()
        val query = "SELECT * FROM $TABLE_WISHES WHERE $KEY_WISH_CATEGORY = ? AND $KEY_WISH_USERNAME_FK = '$loggedInUser'"
        val selectionArgs = arrayOf(category)
        val cursor = db.rawQuery(query, selectionArgs)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_WISH_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_NAME))
            val link = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_LINK))
            val desc = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_DESCRIPTION))
            val cat = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_CATEGORY))

            val wish = Wishes(id, name, link, desc, cat)
            wishList.add(wish)
        }
        cursor.close()
        db.close()
        return wishList
    }

    fun deleteWishById(id: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_WISHES, "$KEY_WISH_ID=?", arrayOf(id.toString()))
        db.close()
        return success
    }

    fun updateWish(id: Int, newName: String, newLink: String, newDesc: String, newCategory: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_WISH_NAME, newName)
            put(KEY_WISH_LINK, newLink)
            put(KEY_WISH_DESCRIPTION, newDesc)
            put(KEY_WISH_CATEGORY, newCategory)
        }
        val whereClause = "$KEY_WISH_ID = ?"
        val whereArgs = arrayOf(id.toString())

        val success = db.update(TABLE_WISHES, values, whereClause, whereArgs)
        db.close()
        return success
    }
    fun getWishByID(wishId: Int): Wishes{
        val db = this.readableDatabase
        val loggedInUser = getLoggedInUser()
        val query = "SELECT * FROM $TABLE_WISHES WHERE $KEY_WISH_ID = $wishId AND $KEY_WISH_USERNAME_FK = '$loggedInUser'"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_WISH_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_NAME))
        val link = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_LINK))
        val desc = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_DESCRIPTION))
        val cat = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_CATEGORY))

        cursor.close()
        db.close()
        return Wishes(id, name, link, desc, cat)
    }

    fun checkIfUserExists(username: String): Boolean {
        val db = this.readableDatabase
        var userExists = false

        val query = "SELECT * FROM $TABLE_USERS WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf(username))

        if (cursor != null) {
            if (cursor.count > 0) {
                userExists = true
            }
            cursor.close()
        }
        db.close()
        return userExists
    }

}


