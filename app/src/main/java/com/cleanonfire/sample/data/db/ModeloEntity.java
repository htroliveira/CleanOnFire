package com.cleanonfire.sample.data.db;

import com.cleanonfire.annotations.data.db.PrimaryKey;
import com.cleanonfire.annotations.data.db.Table;
import com.cleanonfire.api.data.db.QueryCriteria;
import com.generated.cleanonfire.db.CleanOnFireDB;

import java.util.List;

/**
 * Created by heitorgianastasio on 26/10/17.
 */
@Table(tableName = "modelo")
public class ModeloEntity {
    @PrimaryKey(autoincrement = true)
    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CarroEntity> getCarros(){
        return CleanOnFireDB.get()
                .getCarroEntityCleanDAO().query(
                        QueryCriteria.builder()
                        .setSelectionArgs("id_modelo = ?")
                        .setSelectionArgs(id)
                        .build()
                );
    }
}
