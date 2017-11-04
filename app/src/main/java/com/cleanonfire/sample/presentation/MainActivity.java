package com.cleanonfire.sample.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.cleanonfire.sample.core.Carro;
import com.cleanonfire.sample.core.CarroCleanDAO;
import com.cleanonfire.sample.core.ModeloPecaCleanDAO;
import com.generated.cleanonfire.db.CleanOnFireDB;
import com.idescout.sql.SqlScoutServer;

import java.util.Date;


/**
 * Created by heitorgianastasio on 02/10/17.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SqlScoutServer.create(this,getPackageName());
        CleanOnFireDB.init(getApplicationContext());
    }





}
