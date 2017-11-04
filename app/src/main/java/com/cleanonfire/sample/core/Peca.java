package com.cleanonfire.sample.core;

import com.cleanonfire.annotations.data.db.PrimaryKey;
import com.cleanonfire.annotations.data.db.Table;

/**
 * Created by heitorgianastasio on 28/10/17.
 */
@Table
public class Peca {
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
