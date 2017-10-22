package com.cleanonfire.sample.presentation;

import com.cleanonfire.annotations.data.orm.Entity;
import com.cleanonfire.annotations.data.orm.FieldInfo;
import com.cleanonfire.annotations.data.orm.PrimaryKey;
import com.cleanonfire.annotations.data.orm.Relationship;

import java.util.Date;

/**
 * Created by heitorgianastasio on 03/10/17.
 */
@Entity(
        tableName = "picanha"
)
public class Base {
    private Date lastModification;
    //@IgnoreField
    //PessoaCleanDAO dao;
    @PrimaryKey(autoincrement = true)
    private long id;


    private String name;

    @Relationship(relation = Relationship.Type.ONE_TO_ONE, with = Entity1.class,lazyLoad = true)
    private Entity1 entity1;


    private String cpf;
    private Integer age;
    private byte[] file;

    @FieldInfo(unique = true, columnName = "nasme", nullable = false)
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Date getLastModification() {
        return lastModification;
    }

    public void setLastModification(Date lastModification) {
        this.lastModification = lastModification;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Entity1 getEntity1() {
        return entity1;
    }

    public void setEntity1(Entity1 entity1) {
        this.entity1 = entity1;
    }
}
