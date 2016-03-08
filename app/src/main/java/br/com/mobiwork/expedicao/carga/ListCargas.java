package br.com.mobiwork.expedicao.carga;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.mobiwork.expedicao.R;
import br.com.mobiwork.expedicao.configuracoes.AsyncResponse;
import br.com.mobiwork.expedicao.dao.DaoConfig;
import br.com.mobiwork.expedicao.modelo.Config;
import br.com.mobiwork.expedicao.sinc.SincDown;
import br.com.mobiwork.expedicao.util.Alertas;
import br.com.mobiwork.expedicao.util.BaseAdapterFiltro;
import br.com.mobiwork.expedicao.util.SincDadosSmb;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * Created by LuisGustavo on 14/05/2015.
 */
public class ListCargas extends Activity implements OnItemClickListener  , AsyncResponse {

    private ListView cargaList;
    private  List<HashMap<String, String>> fillMaps;
    private ProgressDialog dialog,dialog2;
    ArrayList<String> checkedValue,checkedtodos;
    private BaseAdapterFiltro baf;
    private SincDown sinc;
    private Config config;
    private Alertas a ;
    private boolean activityActive;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cargas);
        cargaList = (ListView) findViewById (R.id.list);
        cargaList.setChoiceMode(cargaList.CHOICE_MODE_SINGLE);
        fillMaps= new ArrayList<HashMap<String, String>>();
        listar();
        checkedValue = new ArrayList<String>();
        cargaList.setOnItemClickListener(this);
        a= new Alertas(this);

    }

    public void listar() {

        Config config=new DaoConfig(this).consultar();
        final SincDadosSmb smb;
        smb = new SincDadosSmb(config,2,"");




       Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                 //   smb.listAvailableHosts(true);

                    fillMaps = smb.smbfile();

                    if (fillMaps.size() > 0){
                        if (fillMaps.get(0).get("_id").equalsIgnoreCase("ErroDadosSinc123")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(activityActive){
                                        a.AlertaSinc(fillMaps.get(0).get("data"));
                                    }
                                }
                            });

                        } else {
                            populateListView();
                        }
                    }else{
                        fillMaps.clear();
                        populateListView();
                    }

                }finally {
                    dialog.dismiss();
                }
            }
        });
        dialog = ProgressDialog.show(ListCargas.this, "Verificando", "Atualizando lista ...", false, true);
        t.start();

    }
    public void trasnfsele(View v){
        checkedValue=  baf.getCheckedValue();
        if(checkedValue.size()==0){
            a.AlertaSinc("Nenhum item Selecionado");
        }else{
            sinc = new SincDown(ListCargas.this,checkedValue,"exportados","");
            sinc.delegate = ListCargas.this;
            sinc.execute(new String[0]);
        }

    }
    public void transftodos(View v){
        checkedtodos = new ArrayList<String>();
        for(int i=0;i<fillMaps.size();i++){
            checkedtodos.add(fillMaps.get(i).get("_id"));
        }
            sinc = new SincDown(ListCargas.this,checkedtodos,"exportados","");
            sinc.delegate = ListCargas.this;
        sinc.execute(new String[0]);

    }
    public void carregarCargas(View v){
        Intent ix;
        ix = new Intent(this, ListCargasBaixadas.class);
        this.startActivityForResult(ix, 0);
        ListCargas.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityActive=true;


    }

    @Override
    protected void onStop() {
        super.onStop();
        activityActive=false;

        // Store our shared preference


    }

    @Override
    protected void onPause() {
        super.onPause();
        activityActive=false;

        // Store our shared preference


    }



    private void populateListView() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                baf = new BaseAdapterFiltro(ListCargas.this, fillMaps, cargaList, "restaure");
                cargaList.setAdapter(baf);

            }
        });

    }

    public void atualizar(View v){
        listar();
    }

    public void voltar(View v){
        ListCargas.this.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox1);
        TextView tv = (TextView) v.findViewById(R.id.nomeCarga);
        cb.performClick();
        if (cb.isChecked()) {
            //baf.setCheckedValue(tv.getText().toString());
        } else if (!cb.isChecked()) {
          // baf.reomevValue(tv.getText().toString());
        }
    }
    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void processFinish(String output) {
        listar();
    }


}
