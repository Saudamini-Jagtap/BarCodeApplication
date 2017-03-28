/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.effectivenavigation;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Databases Name
    public static final String DATABASE_NAME = "ItemListManager";
    // items table name
    private static final String ITEM_TABLE = "items";
    // categories table name
    private static final String CATEGORY_TABLE = "categories";
    // items Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_BARCODE = "barcode";
    private static final String KEY_PRICE = "price";
    private static final String KEY_CATEGORY_ID = "category_ID";

    //default category list
    public static final String[] defaultCategories = {"Healthcare","Groceries","Home Utilities","Electronics", "Clothing" };

    //default three item contents

    public static final String[] item1 = {"Coke", " - ", "Healthcare", " - "};
    public static final String[] item2 = {"Britannia", " - ", "Home Utilities", " - "};
    public static final String[] item3 = {"Nokia", " - ", "Clothing", " - "};

    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }
    //turning on the foriegn key constraints
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

//        String CREATE_ITEMS_TABLE = "CREATE TABLE " + ITEM_TABLE + "("
//                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_BARCODE + " TEXT," + KEY_PRICE + " TEXT, FOREIGN KEY("+KEY_CATEGORY_ID+") REFERENCES "+CATEGORY_TABLE+ "("+KEY_NAME+") "+"");";
        final String CREATE_ITEM_TABLE = "CREATE TABLE "+ITEM_TABLE+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_NAME+" TEXT, "+KEY_BARCODE+" TEXT, "+KEY_PRICE+" TEXT, "+KEY_CATEGORY_ID+" TEXT);";
        db.execSQL(CREATE_ITEM_TABLE);

        final String CREATE_CATEGORY_TABLE = "CREATE TABLE "+CATEGORY_TABLE+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_NAME+" TEXT);";
        db.execSQL(CREATE_CATEGORY_TABLE);

        //creating default category list in the database
        for(String category:defaultCategories){
            ContentValues values = new ContentValues();
            values.put(KEY_NAME,category);
            try {
                db.beginTransaction();
                db.insertOrThrow(CATEGORY_TABLE, null, values);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
        //creating default item database
        List<String[]> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        for(String[] s : itemList){
            ContentValues values = new ContentValues();
            values.put(KEY_NAME,s[0]);
            values.put(KEY_BARCODE,s[1]);
            values.put(KEY_CATEGORY_ID,s[2]);
            values.put(KEY_PRICE,s[3]);
            try {
                db.beginTransaction();
                db.insertOrThrow(ITEM_TABLE, null, values);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);

        // Create tables again
        onCreate(db);
    }
    //Getting the column count in the table
    public int getColumnCount(String tableName){
        String countQuery = "SELECT  * FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getColumnCount();
    }
    //Deleting a table in the database
    public boolean deleteDatabase(Context context,String databaseName){
        return  context.deleteDatabase(databaseName);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations for the Item table
     */
    // Adding new item
    boolean addItem(Items item) {
        long success = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getName()); // item Name
        values.put(KEY_BARCODE, item.getBarcode()); // item barcode
        values.put(KEY_PRICE,item.getPrice());//item price
        values.put(KEY_CATEGORY_ID,item.getCategory());//item category
        // Inserting Row
        //2nd argument is String containing nullColumnHack
        try {
            db.beginTransaction();
            success = db.insertOrThrow(ITEM_TABLE, null, values);
            db.setTransactionSuccessful();
        } catch (SQLiteConstraintException e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        //db.close(); // Closing database connection
        if (success == -1)
            return false;
        else
            return true;
    }

    // Getting single item
    public Items getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ITEM_TABLE, new String[] { KEY_ID, KEY_NAME, KEY_BARCODE, KEY_PRICE, KEY_CATEGORY_ID }, KEY_ID + "=?",new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Items currItem = new Items(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4));
        return currItem;
    }

    // Getting All Items
    public List<Items> getAllItems() {
        List<Items> itemList = new ArrayList<Items>();
//        // Select All Query
        String selectQuery = "SELECT  * FROM " + ITEM_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Items item = new Items();
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.setName(cursor.getString(1));
                item.setBarcode(cursor.getString(2));
                item.setPrice(cursor.getString(3));
                item.setCategory(cursor.getString(4));
                // Adding contact to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        // return contact list
        return itemList;
    }

    // Updating single item
    public int updateItem(Items item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getName());
        values.put(KEY_BARCODE, item.getBarcode());
        values.put(KEY_PRICE,item.getPrice());
        values.put(KEY_CATEGORY_ID,item.getCategory());
        // updating row
        return db.update(ITEM_TABLE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getID()) });
    }

    // Deleting single item
    public boolean deleteItem(Items item) {
        SQLiteDatabase db = this.getWritableDatabase();
        long sucess = db.delete(ITEM_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getID()) });
        db.close();
        if(sucess == -1)
            return false;
        else
            return true;
    }

    // Getting items Count
    public int getItemsCount() {
        String countQuery = "SELECT  * FROM " + ITEM_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();
    }

    /**
     * End of CRUD(Create, Read, Update, Delete) Operations for the Items table
     *
     */


    /**
     * All CRUD(Create, Read, Update, Delete) Operations for the Category table
     *
     */
    //Adding a new category to category list
    boolean addCategory(Category category){
        long sucess;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_ID,category.getID()); //category ID
        values.put(KEY_NAME,category.getName()); //category name
        // Inserting Row
        //2nd argument is String containing nullColumnHack
        try {
            db.beginTransaction();
            sucess = db.insertOrThrow(CATEGORY_TABLE, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close(); // Closing database connection
        if (sucess == -1)
            return false;
        else
            return true;
    }

    //Getting single category to category list
    public Category getCategory(int category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CATEGORY_TABLE, new String[] {KEY_ID, KEY_NAME}, KEY_ID + "=?",new String[] { String.valueOf(category) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Category currCategory = new Category(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
        return currCategory;
    }

    //Getting All Categories
    public List<Category> getAllCategories(){
        List<Category> categoryList = new ArrayList<Category>();
//        // Select All Query
        String selectQuery = "SELECT  * FROM " + CATEGORY_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setID(Integer.parseInt(cursor.getString(0)));
                category.setName(cursor.getString(1));
                // Adding contact to list
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        // return contact list
        return categoryList;
    }

    //updating single category from the category list
    public int updateCategory(Category category){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.getName());
        // updating row
        return db.update(CATEGORY_TABLE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(category.getID()) });
    }

    // Deleting single category from the category list
    public boolean deleteCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        long success = db.delete(CATEGORY_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(category.getID()) });
        db.close();
        if(success == -1)
            return false;
        else
            return true;
    }

    // Getting categories Count
    public int getCategoriesCount() {
        String countQuery = "SELECT  * FROM " + CATEGORY_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();
    }

    /**
     * End of CRUD(Create, Read, Update, Delete) Operations for the Category table
     *
     */

}