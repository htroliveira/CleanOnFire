package com.cleanonfire.sample.data.person;

import android.database.sqlite.SQLiteOpenHelper;

import com.cleanonfire.annotations.data.orm.Entity;
import com.cleanonfire.annotations.data.orm.IgnoreField;
import com.cleanonfire.annotations.data.orm.PrimaryKey;
import com.cleanonfire.api.data.orm.SQLiteCleanHelper;

import java.util.Date;

/**
 * Created by heitorgianastasio on 02/10/17.
 */
@Entity(sqLiteOpenHelper = SQLiteCleanHelper.class, tableName = "oi")
public class PersonEntity {
    @PrimaryKey(autoincrement = true)
    private long id;
    private String name;
    private int age;
    @IgnoreField
    private Date lastUse;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getLastUse() {
        return lastUse;
    }

    public void setLastUse(Date lastUse) {
        this.lastUse = lastUse;
    }
}
