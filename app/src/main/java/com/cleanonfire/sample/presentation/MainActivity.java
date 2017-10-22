package com.cleanonfire.sample.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cleanonfire.api.data.orm.CleanOnFireORM;
import com.cleanonfire.api.data.orm.QueryCriteria;

import java.util.ArrayList;

/**
 * Created by heitorgianastasio on 02/10/17.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CleanOnFireORM.init(this);
        BaseCleanDAO dao = CleanOnFireORM.getDao(BaseCleanDAO.class);
        CleanOnFireORM.getDao(Entity1CleanDAO.class);
        CleanOnFireORM.getDao(Entity2CleanDAO.class);

        dao.getId(new Base());
    }



/*
    public void fetchEntity2list(Entity1 element){
        if (element.entity2List == null)
            element.entity2List = new ArrayList<>();
        element.entity2List.addAll(CleanOnFireORM
                .getDao(Entity2CleanDAO.class)
                .query(QueryCriteria.builder()
                        .setSelection("entity1 = ?")
                        .setSelectionArgs(String.valueOf(element.id))
                        .build()));
    }*/

}
