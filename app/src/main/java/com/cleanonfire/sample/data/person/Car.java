package com.cleanonfire.sample.data.person;

import com.cleanonfire.annotations.data.orm.Entity;
import com.cleanonfire.annotations.data.orm.IgnoreField;
import com.cleanonfire.annotations.data.orm.PrimaryKey;
import com.cleanonfire.api.data.orm.SQLiteCleanHelper;

import java.util.Date;

/**
 * Created by heitorgianastasio on 03/10/17.
 */
@Entity(sqLiteOpenHelper = SQLiteCleanHelper.class)
public class Car {
    @PrimaryKey
    int id;
    String marca;
    String modelo;
    long ano;
    @IgnoreField
    Date ultimoId;
}
