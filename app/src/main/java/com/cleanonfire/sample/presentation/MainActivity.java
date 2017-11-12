package com.cleanonfire.sample.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.cleanonfire.R;
import com.cleanonfire.api.presentation.adapter.OnItemClickListener;
import com.cleanonfire.api.presentation.adapter.OnItemLongClickListener;
import com.cleanonfire.sample.presentation.carros.CarroVisualization;
import com.cleanonfire.sample.presentation.carros.CarroVisualizationAdapter;
import com.cleanonfire.sample.presentation.carros.CarroVisualizationViewHolderBinder;
import com.generated.cleanonfire.db.CleanOnFireDB;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by heitorgianastasio on 02/10/17.
 */
public class MainActivity extends AppCompatActivity {
    static List<CarroVisualization> LISTA = new ArrayList<>();
    static {
        CarroVisualization c1 = new CarroVisualization();
        CarroVisualization c2 = new CarroVisualization();
        CarroVisualization c3 = new CarroVisualization();
        CarroVisualization c4 = new CarroVisualization();
        CarroVisualization c5 = new CarroVisualization();


        c1.setModeloId("Kwid");
        c2.setModeloId("Celta");
        c3.setModeloId("Gol");
        c4.setModeloId("Up");
        c5.setModeloId("Polo");

        c1.setFabricacao(true);
        c2.setFabricacao(false);
        c3.setFabricacao(true);
        c4.setFabricacao(false);
        c5.setFabricacao(true);

        c1.setCor("azul");
        c2.setCor("preto");
        c3.setCor("cinza");
        c4.setCor("prata");
        c5.setCor("vermelho");

        c1.setId(1L);
        c2.setId(2L);
        c3.setId(3L);
        c4.setId(4L);
        c5.setId(5L);

        LISTA.add(c1);
        LISTA.add(c2);
        LISTA.add(c3);
        LISTA.add(c4);
        LISTA.add(c5);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CleanOnFireDB.init(getApplicationContext());
        setContentView(R.layout.actvity_main);


        CarroVisualizationAdapter adapter = new CarroVisualizationAdapter(LISTA,this,new CarroVisualizationViewHolderBinder());
        adapter.setOnFabricacaoLongClickListener(new OnItemLongClickListener<CarroVisualization>() {
            @Override
            public boolean onItemLongClick(CarroVisualization carroVisualization, int position, View view) {
                Toast.makeText(MainActivity.this, carroVisualization.getCor(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        adapter.setOnCorClickListener(new OnItemClickListener<CarroVisualization>() {
            @Override
            public void onItemClick(CarroVisualization carroVisualization, int position, View view) {
                Toast.makeText(MainActivity.this, "Bombou", Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rvTest);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }






}
