package com.cleanonfire.sample;

import com.cleanonfire.annotations.data.orm.Entity;
import com.cleanonfire.annotations.data.orm.Field;
import com.cleanonfire.annotations.data.orm.IgnoreField;
import com.cleanonfire.annotations.data.orm.PrimaryKey;
import com.cleanonfire.api.data.orm.LazyLoader;

import java.util.Date;

/**
 * Created by heitorgianastasio on 03/10/17.
 */
@Entity(
        tableName = "picanha"
)
public class Base {
    @IgnoreField
    Date lastModification;
    //@IgnoreField
    //PessoaCleanDAO dao;
    @PrimaryKey
    private long id;

    private String name;
    private String cpf;
    private int age;

    @Field(unique = true, columnName = "addressId", nullable = false)
    private String address;

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
