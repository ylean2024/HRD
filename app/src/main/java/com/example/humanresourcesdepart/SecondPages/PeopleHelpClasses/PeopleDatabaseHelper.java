package com.example.humanresourcesdepart.SecondPages.PeopleHelpClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PeopleDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "people_db";
    private static final String TABLE_PEOPLE = "people";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_AGE = "age";
    private static final String KEY_POSITION = "position";
    private static final String KEY_EMAIL = "email";

    private static final String KEY_IMAGE_PATH = "image_path";


    public PeopleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PEOPLE_TABLE = "CREATE TABLE " + TABLE_PEOPLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_SURNAME + " TEXT,"
                + KEY_AGE + " INTEGER,"
                + KEY_POSITION + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_IMAGE_PATH + " TEXT" + ")";
        db.execSQL(CREATE_PEOPLE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE);
        onCreate(db);
    }

    public void deletePeopleWithEmailNull() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("people", "email IS NULL", null);
        db.close();
    }
    public long addPerson(PeopleDataClass person) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, person.getName());
        values.put(KEY_SURNAME, person.getSurname());
        values.put(KEY_AGE, person.getAge());
        values.put(KEY_EMAIL, person.getEmail());
        values.put(KEY_POSITION, person.getPosition());
        values.put(KEY_IMAGE_PATH, person.getImagePath());

        long result = db.insertWithOnConflict(TABLE_PEOPLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();

        return result;
    }


    public List<PeopleDataClass> getAllPeople() {
        List<PeopleDataClass> peopleList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PEOPLE, null);

        if (cursor.moveToFirst()) {
            do {
                PeopleDataClass person = new PeopleDataClass();
                person.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                person.setSurname(cursor.getString(cursor.getColumnIndex(KEY_SURNAME)));
                person.setAge(cursor.getInt(cursor.getColumnIndex(KEY_AGE)));
                person.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                person.setPosition(cursor.getString(cursor.getColumnIndex(KEY_POSITION)));
                person.setImagePath(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)));

                peopleList.add(person);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return peopleList;
    }






    public void loadPeopleFromFirebase(List<PeopleDataClass> people) {
        for (PeopleDataClass person : people) {
            addPerson(person);
        }
    }

    public boolean isEmailUnique(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PEOPLE + " WHERE " + KEY_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean isUnique = (cursor.getCount() == 0);
        cursor.close();
        db.close();
        return isUnique;
    }
}
