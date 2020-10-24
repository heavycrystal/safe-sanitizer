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
    public static final String CATID = "categoryID";
    public static final String CAT = "category";
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
                        "(username text primary key, emailID text,phone integer, password text)"
        );
        db.execSQL(
                "create table CATEGORIES"+
                        "(username text, categoryID integer primary key, category text)"
        );
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean addUser(String username, String password, String email, long phone){
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
                username,   // The table to query
                new String[]{USERNAME},
                "username = ?",
                new String[] {username},
                null,
                null,
                null
        );
        if(cursor.moveToNext()){
            return false;
        }
        else {
            db.insert("USERS", null, cv);
            db.execSQL(
                    "create table" + username +
                            "(item text, category text, price float, quantity integer)"
            );
            return true;
        }
    }
    public boolean editUser(String username, String email, Integer phone){
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
    public Integer changeUserPassword(String username, String oldPassword, String newPassword){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String[] cols = {PASSWORD};
        Cursor cursor = db.query(
                "USERS",   // The table to query
                new String[] {"password"},
                "username = ?",
                new String[] {username},
                null,
                null,
                null
        );
        String pass = cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD));
        oldPassword = Base64.getEncoder()
                .encodeToString(oldPassword.getBytes());
        if(pass==oldPassword){
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

    public boolean deleteUserData (String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE "+username, null);
        return true;
    }
    public boolean clearUserData (String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE * FROM "+username, null);
        return true;
    }
    public Integer deleteUser (String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        deleteUserData(username);
        return db.delete("USER",
                "username = ? ",
                new String[] { username });
    }

    public ArrayList<String> getUserData(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "USERS",   // The table to query
                null,
                "username = ?",
                new String[] {username},
                null,
                null,
                null
        );
        ArrayList<String> data = new ArrayList<>();
        while (cursor.moveToNext())
        {
            String name =cursor.getString(cursor.getColumnIndex(USERNAME));
            String email =cursor.getString(cursor.getColumnIndex(EMAILID));
            String phone =cursor.getString(cursor.getColumnIndex(PHONE));
            data.add(name);
            data.add(email);
            data.add(phone);
        }
        return data;
    }

    public ArrayList<String> getAllUsers() {
        ArrayList<String> userList = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from USERS", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            userList.add(res.getString(res.getColumnIndex(USERNAME)));
            res.moveToNext();
        }
        return userList;
    }
    public boolean addCategory(String username, String category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        {
            cv.put(USERNAME, username);
            cv.put(CAT, category);
        }
        Cursor cursor = db.query(
                "CATEGORIES",   // The table to query
                new String[]{USERNAME, CAT},
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
    public Integer getCategoryID(String username, String category){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                "CATEGORIES",   // The table to query
                new String[]{CATID},
                "username = ? AND category = ?",
                new String[] {username, category},
                null,
                null,
                null
        );
        ArrayList<String> data = new ArrayList<>();
        int ID = cursor.getInt(cursor.getColumnIndexOrThrow(CATID));
        return ID;
    }
    public boolean editCategory(String username, String newCategory, String oldCategory){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                "CATEGORIES",   // The table to query
                new String[]{USERNAME, CAT},
                "username = ? AND category = ?",
                new String[] {username, oldCategory},
                null,
                null,
                null
        );
        if(cursor.moveToNext()) {
            int ID = getCategoryID(username, oldCategory);
            db.delete("CATEGORIES", "username = ? AND category = ?", new String[]{username, oldCategory});
            ContentValues cv = new ContentValues();
            {
                cv.put(USERNAME, username);
                cv.put(CATID, ID);
                cv.put(CAT, newCategory);
            }
            db.insert("CATEGORIES",null, cv);
            return true;
        }
        else
            return false;
    }
    public boolean deleteCategory(String username, String category){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CATEGORIES","username = ? AND category = ?",new String[] { username, category });
        return true;
    }
    public boolean addItem(String username, String category, String item, double price, Integer qty){
        SQLiteDatabase db = this.getWritableDatabase();
        int categoryID = getCategoryID(username, category);
        ContentValues cv = new ContentValues();
        {
            cv.put(ITEM, item);
            cv.put(CATID, categoryID);
            cv.put(PRICE, price);
            cv.put(QTY, qty);
        }
        Cursor cursor = db.query(
                username,   // The table to query
                new String[]{ITEM, CATID, PRICE, QTY},
                "item = ?",
                new String[] {item},
                null,
                null,
                null
        );
        if(cursor.moveToNext())
            return false;
        else
            db.insert(username, null, cv);
        return true;
    }
    Integer editItemData(String username, String category, String item, double price, Integer qty){
        SQLiteDatabase db = this.getWritableDatabase();

        int categoryID = getCategoryID(username, category);
        ContentValues cv = new ContentValues();
        {
            cv.put(ITEM, item);
            cv.put(CATID, categoryID);
            cv.put(PRICE, price);
            cv.put(QTY, qty);
        }
        int count = db.update(
                username,
                cv,
                "item = ?",
                new String[]{item});
        return count;


    }
    public boolean deleteItem(String username, String item){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(username,"item = ? ",new String[] { item });
        return true;
    }

    public ArrayList<String> getItemData(String username, String item){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                username,   // The table to query
                new String[]{CAT, PRICE, QTY},
                "item = ?",
                new String[] {item},
                null,
                null,
                null
        );
        ArrayList<String> data = new ArrayList<>();
        while (cursor.moveToNext())
        {
            String category = cursor.getString(cursor.getColumnIndexOrThrow(CAT));
            String price =cursor.getString(cursor.getColumnIndex(PRICE));
            String qty =cursor.getString(cursor.getColumnIndex(QTY));
            data.add(category);
            data.add(price);
            data.add(qty);
        }
        return data;
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}