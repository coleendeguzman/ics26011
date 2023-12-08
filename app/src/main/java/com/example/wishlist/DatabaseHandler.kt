package com.example.wishlist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


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
class DatabaseHandler (context: Context) : SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 8
        private const val DATABASE_NAME = "UserDatabase.db"

        private const val TABLE_USERS = "users"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"

        private const val TABLE_WISHES = "wishes"
        private const val KEY_WISH_ID = "id"
        private const val KEY_WISH_NAME = "wishname"
        private const val KEY_WISH_DESCRIPTION = "description"
        private const val KEY_WISH_LINK = "link"
        private const val KEY_WISH_CATEGORY = "category"

    }


    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USERS_TABLE = ("CREATE TABLE $TABLE_USERS (" +
                "$KEY_USERNAME TEXT PRIMARY KEY," +
                "$KEY_PASSWORD TEXT)")

        db?.execSQL(CREATE_USERS_TABLE)

        val CREATE_WISHES_TABLE = ("CREATE TABLE IF NOT EXISTS $TABLE_WISHES (" +
                "$KEY_WISH_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_WISH_NAME TEXT," +
                "$KEY_WISH_DESCRIPTION TEXT," +
                "$KEY_WISH_LINK TEXT," +
                "$KEY_WISH_CATEGORY TEXT)")

        db?.execSQL(CREATE_WISHES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            // Check if the TABLE_WISHES exists before altering it
            val wishesTableExistenceQuery = "SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '$TABLE_WISHES'"
            val cursor: Cursor = db?.rawQuery(wishesTableExistenceQuery, null) ?: return
            val tableExists = cursor.count > 0
            cursor.close()

            if (!tableExists) {
                val CREATE_WISHES_TABLE = ("CREATE TABLE $TABLE_WISHES (" +
                        "$KEY_WISH_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "$KEY_WISH_NAME TEXT," +
                        "$KEY_WISH_DESCRIPTION TEXT," +
                        "$KEY_WISH_LINK TEXT," +
                        "$KEY_WISH_CATEGORY TEXT)")

                db?.execSQL(CREATE_WISHES_TABLE)
            }
        }
    }


//  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        val CREATE_WISHES_TABLE = ("CREATE TABLE $TABLE_WISHES (" +
//                "$KEY_WISH_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
//                "$KEY_WISH_NAME TEXT," +
//                "$KEY_WISH_DESCRIPTION TEXT," +
//                "$KEY_WISH_LINK TEXT," +
//                "$KEY_WISH_CATEGORY TEXT)")
//
//        db?.execSQL(CREATE_WISHES_TABLE)
//    }


    fun createWish(
        wishname: String,
        wishdesc: String,
        wishlink: String,
        category: String
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_WISH_NAME, wishname)
            put(KEY_WISH_DESCRIPTION, wishdesc)
            put(KEY_WISH_LINK, wishlink)
            put(KEY_WISH_CATEGORY, category)
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
            val desc = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_DESCRIPTION))
            val link = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_LINK))
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
        val query = "SELECT * FROM $TABLE_WISHES WHERE $KEY_WISH_CATEGORY = ?"
        val selectionArgs = arrayOf(category)
        val cursor = db.rawQuery(query, selectionArgs)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_WISH_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_NAME))
            val desc = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_DESCRIPTION))
            val link = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WISH_LINK))
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
        val query = "SELECT * FROM $TABLE_WISHES WHERE $KEY_WISH_ID = $wishId"
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
}


