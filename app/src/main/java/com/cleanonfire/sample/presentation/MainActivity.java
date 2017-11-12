package com.cleanonfire.sample.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cleanonfire.annotations.data.db.Table;
import com.cleanonfire.sample.data.db.CarroEntity;
import com.cleanonfire.sample.data.db.CarroEntityCleanDAO;
import com.cleanonfire.sample.data.db.ModeloEntity;
import com.cleanonfire.sample.data.db.ModeloEntityCleanDAO;
import com.cleanonfire.sample.data.db.ModeloPeca;
import com.cleanonfire.sample.data.db.ModeloPecaCleanDAO;
import com.cleanonfire.sample.data.db.PecaEntity;
import com.cleanonfire.sample.data.db.PecaEntityCleanDAO;
import com.generated.cleanonfire.db.CleanOnFireDB;
import com.idescout.sql.SqlScoutServer;


/**
 * Created by heitorgianastasio on 02/10/17.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SqlScoutServer.create(this,getPackageName());
        CleanOnFireDB.init(getApplicationContext());
        a();
    }

    private void a(){
        ModeloEntityCleanDAO dao = CleanOnFireDB.get().getModeloEntityCleanDAO();

        ModeloEntity modelo = new ModeloEntity();
        modelo.setName("Kwid");
        int idmodelo = dao.save(modelo).getId();


        CarroEntityCleanDAO carroDao = CleanOnFireDB.get().getCarroEntityCleanDAO();

        CarroEntity carro = new CarroEntity();
        carro.setCor("preto");
        carro.setModeloId(idmodelo);
        long idcarro = carroDao.save(carro).getId();

        PecaEntityCleanDAO pecaDao = CleanOnFireDB.get().getPecaEntityCleanDAO();
        PecaEntity peca = new PecaEntity();
        peca.setName("Rodão");

        long idpeca = pecaDao.save(peca).getId();

        ModeloPecaCleanDAO modeloHasPecaDao = CleanOnFireDB.get().getModeloPecaCleanDAO();
        ModeloPeca modeloPeca = new ModeloPeca();
        modeloPeca.setIdModelo(idmodelo);
        modeloPeca.setIdPeca(idpeca);
        modeloPeca.setQuantidade(5);
        modeloHasPecaDao.save(modeloPeca);


    }




}
