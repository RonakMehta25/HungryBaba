package com.example.ronak.hungrybaba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ronak on 26-02-2016.
 */
public class DatabaseOperations extends SQLiteOpenHelper {
    public static final int database_version=1;
    public String CREATE_QUERY="CREATE TABLE"+ TableData.TableInfo.TABLE_NAME+"("+TableData.TableInfo.USER_NAME+" TEXT,"+ TableData.TableInfo.USER_PASS+" TEXT);";
    public DatabaseOperations(Context context) {
        super(context, TableData.TableInfo.DATABSE_NAME, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void putInformation(DatabaseOperations dop,String name,String pass)
    {
        SQLiteDatabase SQ=dop.getWritableDatabase();
        ContentValues CV=new ContentValues();
        CV.put(TableData.TableInfo.USER_NAME,name);
        CV.put(TableData.TableInfo.USER_PASS,pass);
        long k=SQ.insert(TableData.TableInfo.TABLE_NAME,null,CV);
        //Log.d("Database operation","1 row inserted");

    }

}
