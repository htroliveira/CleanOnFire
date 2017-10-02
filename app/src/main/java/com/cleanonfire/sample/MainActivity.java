package com.cleanonfire.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cleanonfire.annotations.SaysHello;

/**
 * Created by heitorgianastasio on 02/10/17.
 */
@SaysHello
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HelloMainActivity.hello(this);
    }
}
