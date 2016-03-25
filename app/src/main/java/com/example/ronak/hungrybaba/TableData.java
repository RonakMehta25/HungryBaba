package com.example.ronak.hungrybaba;

import android.provider.BaseColumns;

/**
 * Created by Ronak on 26-02-2016.
 */
public class TableData {
    public TableData()
    {

    }
    public static abstract class TableInfo implements BaseColumns
    {
        public static final  String USER_NAME="username";
        public static final  String USER_PASS="userpass";
        public static final  String DATABSE_NAME="user_info";
        public static final  String TABLE_NAME="reg_info";
    }
}
