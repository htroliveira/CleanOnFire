package com.cleanonfire.sample.core;

import com.cleanonfire.annotations.data.db.PrimaryKey;
import com.cleanonfire.annotations.data.db.Table;
import com.cleanonfire.api.data.db.QueryCriteria;

import java.util.List;

/**
 * Created by heitorgianastasio on 26/10/17.
 */
@Table
public class Modelo {
    @PrimaryKey(autoincrement = true)
    private long id;

    private String name;

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
}
