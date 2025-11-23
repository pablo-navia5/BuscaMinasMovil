package com.example.buscaminas


import android.R
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// creating a constructor
// for our database handler
class DBHandler
    (context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    // below method is for creating a database
    // by running a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + PRICE_COL + " DOUBLE)")

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query)
    }

    // this method is use to add new
    // course to our sqlite database.
    fun addNewProduct(
        productName: String?,
        productPrice: Double?,
    ) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        val db = this.writableDatabase

        // on below line we are creating a
        // variable for content values.
        val values = ContentValues()

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME_COL, productName)
        values.put(PRICE_COL, productPrice)

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values)

        // at last we are closing our
        // database after adding database.
        db.close()
    }

    fun deleteProduct(productName: String): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$NAME_COL = ?", arrayOf(productName))
        db.close()
        return result
    }


    fun readProducts(): ArrayList<R.integer> {
        // on below line we are creating a
        // database for reading our database.
        val db = this.readableDatabase

        // on below line we are creating a cursor
        // with query to read data from database.
        val cursorProducts: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        // on below line we are creating a new array list.
        val courseModalArrayList: ArrayList<R.integer> = ArrayList()

        // moving our cursor to first position.
        if (cursorProducts.moveToFirst()) {
            do {
                // on below line we are adding the
                // data from cursor to our array list.
//                courseModalArrayList.add(
////                    ProductModal(
////                        cursorProducts.getString(1),
////                        cursorProducts.getDouble(2)
////                    )
//                )
            } while (cursorProducts.moveToNext())
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorProducts.close()
        return courseModalArrayList
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        // creating a constant variables for our database.
        // below variable is for our database name.
        private const val DB_NAME = "productosdb"

        // below int is our database version
        private const val DB_VERSION = 1

        // below variable is for our table name.
        private const val TABLE_NAME = "productos"

        // below variable is for our id column.
        private const val ID_COL = "id"

        // below variable is for our product name column
        private const val NAME_COL = "name"

        // below variable id for our product price column.
        private const val PRICE_COL = "precio"
    }
}