package com.cleanonfire.api.data.orm;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.content.res.AppCompatResources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by heitorgianastasio on 02/10/17.
 */

public final class SQLiteCleanHelper extends SQLiteOpenHelper {
    Context context;

    public SQLiteCleanHelper(Context context, String name) {
        super(context, name, null, 1);
        this.context = context;
    }

    public SQLiteCleanHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }


    private String fromRawResource(int resourceId){
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String result = "";
        try {
            do{
                result = result.concat(reader.readLine());
            }while (reader.ready());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        int idResource = context.getResources().getIdentifier("clean_db_create","raw",context.getApplicationContext().getPackageName());
        db.execSQL(fromRawResource(idResource));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
