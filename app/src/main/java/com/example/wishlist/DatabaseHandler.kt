package com.example.wishlist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


data class User(
    val username: String,
    val password: String
)
class DatabaseHandler (context: Context) : SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION) {

        companion object {
            private const val DATABASE_VERSION = 2
            private const val DATABASE_NAME = "UserDatabase.db"

            private const val TABLE_USERS = "users"
            private const val KEY_USERNAME = "username"
            private const val KEY_PASSWORD = "password"

            private const val TABLE_WISHES = "wishes"
            private const val KEY_WISH_ID = "id"
            private const val KEY_WISH_NAME = "wishname"
            private const val KEY_WISH_DESCRIPTION = "description"
            private const val KEY_WISH_LINK = "link"
            private const val KEY_WISH_IMAGE = "image"
            private const val KEY_WISH_CATEGORY = "category"
        }

        override fun onCreate(db: SQLiteDatabase?) {
            val CREATE_USERS_TABLE = ("CREATE TABLE $TABLE_USERS (" +
                    "$KEY_USERNAME TEXT PRIMARY KEY," +
                    "$KEY_PASSWORD TEXT)")

            db?.execSQL(CREATE_USERS_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            val CREATE_WISHES_TABLE = ("CREATE TABLE $TABLE_WISHES (" + "$KEY_WISH_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$KEY_WISH_NAME TEXT," + "$KEY_WISH_DESCRIPTION TEXT," + "$KEY_WISH_LINK TEXT," + "$KEY_WISH_IMAGE BLOB," + "$KEY_WISH_CATEGORY TEXT)")

            db?.execSQL(CREATE_WISHES_TABLE)
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
                }

                else {
                    null
                }
            }

            else {
                null
            }
        }
    }
}