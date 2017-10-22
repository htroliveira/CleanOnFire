package com.cleanonfire.sample.presentation;

import com.cleanonfire.annotations.data.orm.Entity;
import com.cleanonfire.annotations.data.orm.FieldInfo;
import com.cleanonfire.annotations.data.orm.PrimaryKey;
import com.cleanonfire.annotations.data.orm.Relationship;
import com.cleanonfire.api.data.orm.CleanOnFireORM;

import java.util.List;

/**
 * Created by heitorgianastasio on 12/10/17.
 */
@Entity(
        tableName = "oi"
)
public class Entity1 {
    @PrimaryKey
    @FieldInfo(columnName = "_id")
    private long id;

    @Relationship(relation = Relationship.Type.ONE_TO_ONE, with = Entity2.class, lazyLoad = true)
    private Entity2 entity2;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Entity2 getEntity2() {
        Entity1CleanDAO.lazyFetchEntity2(this);
        return entity2;
    }

    public void setEntity2(Entity2 entity2) {
        this.entity2 = entity2;
    }
}
