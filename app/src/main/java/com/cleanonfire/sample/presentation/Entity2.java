package com.cleanonfire.sample.presentation;

import com.cleanonfire.annotations.data.orm.Entity;
import com.cleanonfire.annotations.data.orm.PrimaryKey;
import com.cleanonfire.annotations.data.orm.Relationship;

/**
 * Created by heitorgianastasio on 12/10/17.
 */
@Entity(
)
public class Entity2 {
    @PrimaryKey
    long id;
    @Relationship(relation = Relationship.Type.ONE_TO_ONE,with = Entity1.class)
    Entity1 entity1;


}
