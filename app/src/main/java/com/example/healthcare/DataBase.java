package com.example.healthcare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "healthcare";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_CART = "cart";
    private static final String TABLE_ORDER = "orders";

    // Users table columns
    private static final String COL_USERNAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";

    // Cart table columns
    private static final String COL_PRODUCT = "product";
    private static final String COL_PRICE = "price";
    private static final String COL_OTYPE = "otype";
    private static final String COL_FULLNAME = "fullname";
    private static final String COL_ADDRESS = "address";
    private static final String COL_PHONENO = "phoneno";
    private static final String COL_PINCODE = "pincode";
    private static final String COL_DATE = "date";
    private static final String COL_TIME = "time";
    private static final String COL_AMOUNT = "amount";

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

        String createOrderTable = "CREATE TABLE " + TABLE_ORDER + " (" +
                COL_USERNAME + " TEXT, " +
                COL_FULLNAME + " TEXT, " +
                COL_ADDRESS + " TEXT, " +
                COL_PHONENO + " TEXT, " +
                COL_PINCODE + " INTEGER, " +
                COL_DATE + " TEXT, " +
                COL_TIME + " TEXT, " +
                COL_AMOUNT + " REAL, " +
                COL_OTYPE + " TEXT)";
        db.execSQL(createOrderTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
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
        cv.put(COL_PASSWORD, password);
        try {
            long result = db.insertOrThrow(TABLE_USERS, null, cv);
            return result != -1;
        } catch (Exception e) {
            Log.e("DataBase", "Error registering user: " + e.getMessage());
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
        } catch (Exception e) {
            Log.e("DataBase", "Error during login: " + e.getMessage());
            return false;
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
            Log.e("DataBase", "Error adding to cart: " + e.getMessage());
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
        } catch (Exception e) {
            Log.e("DataBase", "Error checking cart: " + e.getMessage());
            return false;
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
        } catch (Exception e) {
            Log.e("DataBase", "Error removing cart: " + e.getMessage());
            return false;
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
        } catch (Exception e) {
            Log.e("DataBase", "Error retrieving cart data: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return cartItems;
    }

    public void addOrder(String username, String fullname, String address, String phoneno, int pincode, String date, String time, float amount, String otype) {
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, username);
        cv.put(COL_FULLNAME, fullname);
        cv.put(COL_ADDRESS, address);
        cv.put(COL_PHONENO, phoneno);
        cv.put(COL_PINCODE, pincode);
        cv.put(COL_DATE, date);
        cv.put(COL_TIME, time);
        cv.put(COL_AMOUNT, amount);
        cv.put(COL_OTYPE, otype);
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.insertOrThrow(TABLE_ORDER, null, cv);
            Log.d("DataBase", "Order inserted successfully for user: " + username);
        } catch (Exception e) {
            Log.e("DataBase", "Error adding order: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public ArrayList<String> getOrderData(String username) {
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_ORDER + " WHERE " + COL_USERNAME + " = ?", new String[]{username});
            if (cursor.moveToFirst()) {
                do {
                    arr.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_FULLNAME)) + "$" +
                            cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS)) + "$" +
                            cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONENO)) + "$" +
                            cursor.getString(cursor.getColumnIndexOrThrow(COL_PINCODE)) + "$" +
                            cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)) + "$" +
                            cursor.getString(cursor.getColumnIndexOrThrow(COL_TIME)) + "$" +
                            cursor.getFloat(cursor.getColumnIndexOrThrow(COL_AMOUNT)) + "$" +
                            cursor.getString(cursor.getColumnIndexOrThrow(COL_OTYPE)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DataBase", "Error retrieving order data: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        Log.d("DataBase", "Orders retrieved for username " + username + ": " + arr);
        return arr;
    }

    // New method to check for duplicate appointments
    public boolean checkAppointmentExists(String username, String fullname, String address, String phoneno, String date, String time) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_ORDER + " WHERE " +
                            COL_USERNAME + " = ? AND " +
                            COL_FULLNAME + " = ? AND " +
                            COL_ADDRESS + " = ? AND " +
                            COL_PHONENO + " = ? AND " +
                            COL_DATE + " = ? AND " +
                            COL_TIME + " = ? AND " +
                            COL_OTYPE + " = 'appointment'",
                    new String[]{username, fullname, address, phoneno, date, time});
            return cursor.moveToFirst();
        } catch (Exception e) {
            Log.e("DataBase", "Error checking appointment: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }
}