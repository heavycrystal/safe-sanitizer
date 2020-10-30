package com.example.trial;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Trial.db";
    public static final String USERNAME = "username";
    public static final String EMAILID = "emailID";
    public static final String PHONE = "phone";
    public static final String PASSWORD = "password";
    public static final String ITEM = "item";
    public static final String CATEGORY = "category";
    public static final String ITEMS = "ITEMS";
    public static final String USERS = "USERS";
    public static final String CATEGORIES = "CATEGORIES";
    //public static final String CATID = "categoryID";
    public static final String PRICE = "price";
    public static final String QTY = "quantity";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table USERS " +
                        "(username text primary key, emailID text,phone text, password text)"
        );
        db.execSQL(
                "create table CATEGORIES"+
                        "(username text, category text)"
        );
        db.execSQL(
                "create table ITEMS"+
                        "(username text, category text, item text, price float, quantity integer)"
        );
    }


        @RequiresApi(api = Build.VERSION_CODES.O)
        public boolean addUser (String username, String email, String password, String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        {
            cv.put(USERNAME, username);
            cv.put(EMAILID, email);
            cv.put(PHONE, phone);
            cv.put(PASSWORD, Base64.getEncoder()
                    .encodeToString(password.getBytes()));
        }
        Cursor cursor = db.query(
                USERS,   // The table to query
                new String[]{USERNAME},
                "username = ?",
                new String[]{username},
                null,
                null,
                null
        );
        if (cursor.moveToNext()) {
            return false;
        } else {
            db.insert("USERS", null, cv);
            return true;
        }
    }
        public boolean editUser (String username, String email, Integer phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        {
            cv.put(USERNAME, username);
            cv.put(EMAILID, email);
            cv.put(PHONE, phone);
        }
        db.insert("USERS", null, cv);
        return true;
    }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public Integer changeUserPassword (String username, String oldPassword, String newPassword){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String[] cols = {PASSWORD};
        Cursor cursor = db.query(
                "USERS",   // The table to query
                new String[]{"password"},
                "username = ?",
                new String[]{username},
                null,
                null,
                null
        );
        String pass = cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD));
        oldPassword = Base64.getEncoder()
                .encodeToString(oldPassword.getBytes());
        if (pass == oldPassword) {
            newPassword = Base64.getEncoder().encodeToString(newPassword.getBytes());
            cv.put(PASSWORD, newPassword);
            int count = db.update(
                    "USERS",
                    cv,
                    "username = ?",
                    new String[]{username});
            return count;
        }
        return 0;
    }
        public boolean deleteUserData (String username){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + username, null);
        return true;
    }
        public boolean clearUserData (String username){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE * FROM " + username, null);
        return true;
    }
        public Integer deleteUser (String username){
        SQLiteDatabase db = this.getWritableDatabase();
        deleteUserData(username);
        return db.delete("USER",
                "username = ? ",
                new String[]{username});
    }
        public ArrayList<String> getUserData (String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "USERS",   // The table to query
                null,
                "username = ?",
                new String[]{username},
                null,
                null,
                null
        );
        ArrayList<String> data = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(USERNAME));
            String email = cursor.getString(cursor.getColumnIndex(EMAILID));
            String phone = cursor.getString(cursor.getColumnIndex(PHONE));
            data.add(name);
            data.add(email);
            data.add(phone);
        }
        return data;
    }
        public ArrayList<String> getAllUsers () {
        ArrayList<String> userList = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from USERS", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            userList.add(res.getString(res.getColumnIndex(USERNAME)));
            res.moveToNext();
        }
        return userList;
    }


    //functions that work on categories
    public ArrayList<String> getCategories(String username){
        ArrayList<String> catList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "CATEGORIES",   // The table to query
                new String[]{USERNAME, CATEGORY},
                "username = ?",
                new String[] {username},
                null,
                null,
                null
        );
        while(cursor.moveToNext())
            catList.add(cursor.getString(cursor.getColumnIndex(CATEGORY)));
        return catList;
    }
    public boolean addCategory(String username, String category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        {
            cv.put(USERNAME, username);
            cv.put(CATEGORY, category);
        }
        Cursor cursor = db.query(
                "CATEGORIES",   // The table to query
                new String[]{USERNAME, CATEGORY},
                "category = ?",
                new String[] {category},
                null,
                null,
                null
        );
        if(cursor.moveToNext())
            return false;
        else
            db.insert("CATEGORIES", null, cv);
        return true;
    }

    public boolean editCategory(String username, String newCategory, String oldCategory){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CATEGORIES", "username = ? AND category = ?", new String[]{username, oldCategory});
        boolean bool = addCategory(username, newCategory);
        return bool;
    }
    public boolean deleteCategory(String username, String category){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CATEGORIES","username = ? AND category = ?",new String[] { username, category });
        return true;
    }

    //functions that work on items
    public boolean addItem(String username, String category, String item, double price, Integer qty){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        {
            cv.put(USERNAME, username);
            cv.put(CATEGORY, category);
            cv.put(ITEM, item);
            cv.put(PRICE, price);
            cv.put(QTY, qty);
        }
        db.insert(ITEMS, null, cv);
        return true;
    }
    public boolean deleteItem(String username, String category, String item){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ITEMS,"item = ? AND category=?",new String[] { item, category });
        return true;
    }
    public boolean editItem(String username, String category, String oldItem, String newItem, double price, Integer qty){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ITEMS,"username = ? AND item = ? AND category=?",new String[] { username, oldItem, category });
        boolean bool = addItem(username, category, newItem, price, qty);
        return bool;
    }
    public ArrayList<String> getItemData(String username, String category, String item){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                ITEMS,   // The table to query
                new String[]{PRICE, QTY},
                "item = ? AND category=?",
                new String[] {item, category},
                null,
                null,
                null
        );
        ArrayList<String> data = new ArrayList<>();
        while (cursor.moveToNext())
        {
            String price =cursor.getString(cursor.getColumnIndex(PRICE));
            String qty =cursor.getString(cursor.getColumnIndex(QTY));
            data.add(price);
            data.add(qty);
        }
        return data;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}