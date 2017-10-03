package com.cleanonfire.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cleanonfire.annotations.SaysHello;
import com.cleanonfire.sample.data.person.CleanCarDAO;
import com.cleanonfire.sample.data.person.CleanPersonEntityDAO;

/**
 * Created by heitorgianastasio on 02/10/17.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CleanPersonEntityDAO dao = new CleanPersonEntityDAO(null);
        dao.getById(2).setName("oi");
        CleanCarDAO
    }
}
