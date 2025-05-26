package com.example.healthcare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "healthcare";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_CART = "cart";

    // Users table columns
    private static final String COL_USERNAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";

    // Cart table columns
    private static final String COL_PRODUCT = "product";
    private static final String COL_PRICE = "price";
    private static final String COL_OTYPE = "otype";

    public DataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USERNAME + " TEXT PRIMARY KEY, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);

        String createCartTable = "CREATE TABLE " + TABLE_CART + " (" +
                COL_USERNAME + " TEXT, " +
                COL_PRODUCT + " TEXT, " +
                COL_PRICE + " REAL, " +
                COL_OTYPE + " TEXT, " +
                "PRIMARY KEY (" + COL_USERNAME + ", " + COL_PRODUCT + "))";
        db.execSQL(createCartTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    public boolean register(String username, String email, String password) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return false;
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, username);
        cv.put(COL_EMAIL, email);
        cv.put(COL_PASSWORD, password); // Note: Password should be hashed in production
        try {
            long result = db.insertOrThrow(TABLE_USERS, null, cv);
            return result != -1;
        } catch (Exception e) {
            return false;
        } finally {
            db.close();
        }
    }

    public boolean login(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?",
                    new String[]{username, password});
            return cursor.moveToFirst();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public boolean addCart(String username, String product, float price, String otype) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, username);
        cv.put(COL_PRODUCT, product);
        cv.put(COL_PRICE, price);
        cv.put(COL_OTYPE, otype);
        try {
            long result = db.insertOrThrow(TABLE_CART, null, cv);
            return result != -1;
        } catch (Exception e) {
            return false;
        } finally {
            db.close();
        }
    }

    public boolean checkCart(String username, String product) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + COL_USERNAME + " = ? AND " + COL_PRODUCT + " = ?",
                    new String[]{username, product});
            return cursor.moveToFirst();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public boolean removeCart(String username, String otype) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            int rows = db.delete(TABLE_CART, COL_USERNAME + " = ? AND " + COL_OTYPE + " = ?",
                    new String[]{username, otype});
            return rows > 0;
        } finally {
            db.close();
        }
    }

    public ArrayList<String> getCartData(String username, String otype) {
        ArrayList<String> cartItems = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + COL_USERNAME + " = ? AND " + COL_OTYPE + " = ?",
                    new String[]{username, otype});
            if (cursor.moveToFirst()) {
                int productIndex = cursor.getColumnIndex(COL_PRODUCT);
                int priceIndex = cursor.getColumnIndex(COL_PRICE);
                do {
                    String product = cursor.getString(productIndex);
                    float price = cursor.getFloat(priceIndex);
                    cartItems.add(product + "$" + price);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return cartItems;
    }
}