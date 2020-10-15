package com.avisek11699654.detect_it.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MySQLITE extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "DATA";
    private static final String TABLE_CONTACTS = "image";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CONFIDENCE = "confidence";


    public MySQLITE(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE "
                + TABLE_CONTACTS + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_CONFIDENCE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(sqLiteDatabase);

    }

    public ArrayList<FruitModel> listFruits() {
        String sql = "select * from " + TABLE_CONTACTS ;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<FruitModel> storeContacts = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String phno = cursor.getString(2);
                storeContacts.add(new FruitModel(id, name, phno));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeContacts;
    }


    public void addFruit(FruitModel fruitModel) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, fruitModel.getName());
        values.put(COLUMN_CONFIDENCE, fruitModel.getConfidecne());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CONTACTS, null, values);
    }

    public void deleteFruit(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

    }

}
