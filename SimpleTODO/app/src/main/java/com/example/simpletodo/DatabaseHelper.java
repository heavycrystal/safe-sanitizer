package com.example.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;


class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SimpleTODO.db";
    private static final String TABLE_TODO = "MyTODO";

    private static final String TABLE_TODO_ID = "ID";
    private static final String TABLE_TODO_TITLE = "TITLE";
    private static final String TABLE_TODO_DESCRIPTION = "DESCRIPTION";
    private static final String TABLE_TODO_DUE = "DUE";
    private static final String TABLE_TODO_STATUS = "STATUS";
    private static final String TABLE_TODO_CREATED = "CREATED";
    private static final String TABLE_TODO_UPDATED = "UPDATED";


    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_TODO +" ( " +
                TABLE_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_TODO_TITLE + " TEXT, " +
                TABLE_TODO_DESCRIPTION + " TEXT," +
                TABLE_TODO_DUE + " INTEGER," +
                TABLE_TODO_STATUS + " TEXT," +
                TABLE_TODO_CREATED + " INTEGER," +
                TABLE_TODO_UPDATED + " INTEGER" +
                " )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TODO);
        onCreate(db);
    }

    long InsertTODO(String Title, String Description, Date Due, String Status, Date Created, Date Updated){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_TODO_TITLE,Title);
        contentValues.put(TABLE_TODO_DESCRIPTION,Description);
        contentValues.put(TABLE_TODO_DUE,Due.getTime());
        contentValues.put(TABLE_TODO_STATUS,Status);
        contentValues.put(TABLE_TODO_CREATED,Created.getTime());
        contentValues.put(TABLE_TODO_UPDATED,Updated.getTime());

        return db.insert(TABLE_TODO,null,contentValues);
    }

    int UpdateTODO(Long ID, String Title, String Description, Date Due, String Status, Date Updated){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_TODO_ID,ID);
        contentValues.put(TABLE_TODO_TITLE,Title);
        contentValues.put(TABLE_TODO_DESCRIPTION,Description);
        contentValues.put(TABLE_TODO_DUE,Due.getTime());
        contentValues.put(TABLE_TODO_STATUS,Status);
        contentValues.put(TABLE_TODO_UPDATED,Updated.getTime());

        return db.update(TABLE_TODO,contentValues,"ID=?",new String[]{ID.toString()});
    }

    int SetStatus(Long ID,String Status, Date Updated){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_TODO_ID,ID);
        contentValues.put(TABLE_TODO_STATUS,Status);
        contentValues.put(TABLE_TODO_UPDATED,Updated.getTime());

        return db.update(TABLE_TODO,contentValues,"ID=?",new String[]{ID.toString()});
    }

    int DeleteTODO(Long ID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TODO,"ID=?",new String[]{ID.toString()});
    }

    int DeleteAllByStatus(String Status){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TODO,"STATUS=?",new String[]{Status});
    }

    Cursor GetAllTODO(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+TABLE_TODO,null);
    }

    Cursor GetAllTODOBByStatus(String Status){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+TABLE_TODO+ " WHERE "+TABLE_TODO_STATUS+" = '"+Status+"' ORDER BY "+TABLE_TODO_TITLE,null);
    }

    Cursor GetTODOBByID(Long ID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+TABLE_TODO+ " WHERE "+TABLE_TODO_ID+" = '"+ID+"'",null);
    }
}
