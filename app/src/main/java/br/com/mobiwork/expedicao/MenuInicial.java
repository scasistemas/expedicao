package br.com.mobiwork.expedicao;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.sql.SQLException;

import br.com.mobiwork.expedicao.atualizar.ListAtu;
import br.com.mobiwork.expedicao.carga.ListCargas;
import br.com.mobiwork.expedicao.carga.ListCargasBaixadas;
import br.com.mobiwork.expedicao.carga.ListCargasEnviadas;
import br.com.mobiwork.expedicao.configuracoes.Configuracoes;
import br.com.mobiwork.expedicao.dao.DaoConfig;
import br.com.mobiwork.expedicao.dao.DaoPonto;
import br.com.mobiwork.expedicao.dao.MyDatabaseAdapter;
import br.com.mobiwork.expedicao.util.Alertas;
import br.com.mobiwork.expedicao.util.BkpBancoDeDados;
import br.com.mobiwork.expedicao.util.FolderConfig;


public class MenuInicial extends ActionBarActivity {

    Alertas a;
    public int selecionarsincMenuPricipalID;
    private DaoConfig dc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);
        dc = new DaoConfig(this);
        dc.atuImgTemp();
        a= new Alertas(this);
    }


    public void cargas(View v){
        Intent ix;
        ix = new Intent(this, ListCargasBaixadas.class);
        this.startActivityForResult(ix, 0);
    }

    public void reenviar(View v){
        Intent ix;
        ix = new Intent(this, ListCargasEnviadas.class);
        this.startActivityForResult(ix, 0);
    }

    public void bcargas(View v){
        Intent ix;
        ix = new Intent(this, ListCargas.class);
        this.startActivityForResult(ix,1);
    }

    public void sair(View v){

        new AlertDialog.Builder(this)
                .setTitle("Tem certeza que deseja sair?")
                .setItems(R.array.op_alerta_sim_nao, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        if (i == 0){
                            MenuInicial.this.finish();
                            System.exit(0);
                        }
                    }
                }).show();


    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void criarConfig(View v){

        new AlertDialog.Builder(this).setTitle(R.string.op_menu_principal).setItems(R.array.op_menu_principalSinc, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {

                MenuInicial.this.selecionarsincMenuPricipal(paramAnonymousInt);
            }
        }).show();

    }
    public void exportar(View v){
        Intent ix;
        ix = new Intent(this, BkpBancoDeDados.class);
        ix.putExtra("tBkp", "Backuprapido");
        ix.putExtra("carga", "");
        this.startActivityForResult(ix, 0);
        this.deleteDatabase("123456.db");
       // MenuInicial.this.finish();
    }

    protected void selecionarsincMenuPricipal(int param)
    {
        if(param==0){
            Intent ix;
            ix = new Intent(this, Configuracoes.class);
            this.startActivityForResult(ix,0);
        }
        if(param==1){
            try {
                FolderConfig.getExternalStorageDirectory();
                FolderConfig.getExternalStorageDirectoryVs();
                FolderConfig.getExternalStorageDirectorycargaAberta();
                FolderConfig.getExternalStorageDirectorycargaFechada();
                a.AlertaSinc("Configuracoes criadas com sucesso");
            }catch (Exception e){
                a.AlertaSinc("Erro ao criar config" + e.getMessage());
            }
        }

        if(param==2){
            Intent ix;
            ix = new Intent(this, ListAtu.class);
            this.startActivityForResult(ix,0);
        }



    }



}
