package br.com.mobiwork.expedicao.carga;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.mobiwork.expedicao.R;
import br.com.mobiwork.expedicao.configuracoes.AsyncResponse;
import br.com.mobiwork.expedicao.dao.DaoConfig;
import br.com.mobiwork.expedicao.modelo.Config;
import br.com.mobiwork.expedicao.pedido.ListPedidos;
import br.com.mobiwork.expedicao.util.Alertas;
import br.com.mobiwork.expedicao.util.BaseAdapterC;
import br.com.mobiwork.expedicao.util.SincDadosLocal;

/**
 * Created by LuisGustavo on 21/05/2015.
 */
public class ListCargasEnviadas extends Activity implements AdapterView.OnItemClickListener, AsyncResponse {

    private ListView cargaList;
    private List<HashMap<String, String>> fillMaps;
    private ProgressDialog dialog;
    ArrayList<String> checkedValue;
    private Alertas a;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listcargasbaixadas);
        cargaList = (ListView) findViewById (R.id.lista);
        fillMaps= new ArrayList<HashMap<String, String>>();
        listar();
        checkedValue = new ArrayList<String>();
        cargaList.setOnItemClickListener(this);
        a= new Alertas(this);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listar();
    }


    public void listar() {

        Config config=new DaoConfig(this).consultar();
        final SincDadosLocal sdc;
        sdc = new SincDadosLocal(config,2);

        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try{
                    fillMaps = sdc.sdcfile();
                    populateListView();

                }finally {
                    dialog.dismiss();
                }

            }
        });
        dialog = ProgressDialog.show(ListCargasEnviadas.this, "Verificando", "Atualizando lista ...", false, true);
        t.start();

    }



    private void populateListView() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseAdapterC baf = new BaseAdapterC(ListCargasEnviadas.this, fillMaps, cargaList, "");
                cargaList.setAdapter(baf);

            }
        });
        cargaList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                TextView tv = (TextView) arg1.findViewById(R.id.carga);
                deletar(tv.getText().toString());
                return true;
            }
        });

    }



    private void deletar(final String carga) {
        new AlertDialog.Builder(this)
                .setItems(R.array.op_cargas_ab, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        new AlertDialog.Builder(ListCargasEnviadas.this)
                                .setTitle("Tem certeza que deseja excluir a carga permanentemente?")
                                .setItems(R.array.op_alerta_sim_nao, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        if (i == 0){
                                            Config config=new DaoConfig(ListCargasEnviadas.this).consultar();
                                            SincDadosLocal sdc = new SincDadosLocal(config,2);
                                            sdc.deleteFile(carga);
                                            sdc.deleteFile("env_"+carga);
                                            ListCargasEnviadas.this.deleteDatabase(carga);
                                            ListCargasEnviadas.this.deleteDatabase("env_" + carga);

                                            listar();


                                        }
                                    }
                                }).show();

                    }
                }).show();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        TextView tv = (TextView) v.findViewById(R.id.carga);
        String nome=tv.getText().toString();
        Intent ix;
        ix = new Intent(this, ListPedidos.class);
        ix.putExtra("nomeb", nome);
        this.startActivityForResult(ix, 0);

    }
    public void voltar(View v){
        ListCargasEnviadas.this.finish();
    }

    @Override
    public void onBackPressed() {
        return;
    }


    @Override
    public void processFinish(String output) {

    }
}
