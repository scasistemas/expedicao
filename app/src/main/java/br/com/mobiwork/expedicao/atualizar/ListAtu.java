package br.com.mobiwork.expedicao.atualizar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.mobiwork.expedicao.R;
import br.com.mobiwork.expedicao.dao.DaoConfig;
import br.com.mobiwork.expedicao.dao.DaoCreateDB;
import br.com.mobiwork.expedicao.modelo.Config;
import br.com.mobiwork.expedicao.util.Alertas;
import br.com.mobiwork.expedicao.util.BaseAdapterFiltro;
import br.com.mobiwork.expedicao.util.BaseAdapterFiltroLista;
import br.com.mobiwork.expedicao.util.Conexao;
import br.com.mobiwork.expedicao.util.DataXmlExporter;
import br.com.mobiwork.expedicao.util.FolderConfig;
import br.com.mobiwork.expedicao.util.SincDadosSmb;
import br.com.mobiwork.expedicao.util.UpdateApp;
import jcifs.smb.SmbFile;

/**
 * Created by LuisGustavo on 08/09/2015.
 */
public class ListAtu  extends Activity {





    private ArrayAdapter mAdapter;
    List<HashMap<String, String>> fillMaps;
    private ListAdapter adapter;
    String mod;
    private static final String Tag = "DataMobile";

    private ProgressDialog dialog,dialog2;
    private BaseAdapterFiltro b;
    ListView list;
    Config config;
    private boolean activityActive;





    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listatu);

        this.list = (ListView) findViewById(R.id.list);
        listar();
        fillMaps = new ArrayList<HashMap<String, String>>();
        DaoCreateDB daoDBP = new DaoCreateDB(this);
        SQLiteDatabase dbP=daoDBP.getWritableDatabase();
        config =  new DaoConfig(this).consultar();
        //populateListView();
        dbP.close();
        daoDBP.close();



        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener()
        {

            public void onItemClick(AdapterView parent, View v, final int position, long id)
            {
                download(position);
            }
        };
        list.setOnItemClickListener(mMessageClickedHandler);




    }
    public void sair(View v){
        ListAtu.this.finish();
    }

    public void download(final int position){
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try{
                    if(Conexao.Conectado(ListAtu.this)){
                        SincDadosSmb smb= new SincDadosSmb(config,4,"");

                        if(smb.downloadFilePc(fillMaps.get(position).get("_id"),"",3).trim().isEmpty()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(ListAtu.this)
                                            .setTitle("Alerta")
                                            .setMessage("Tem certeza que deseja atualizar o sistema?")
                                            .setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    UpdateApp atualizaApp = new UpdateApp();
                                                    atualizaApp.setContext(getApplicationContext(), fillMaps.get(position).get("_id"));
                                                    atualizaApp.execute();
                                                }
                                            })
                                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            })

                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            });
                        }
                    }else{
                        showToast("Sem Conexao com a Internet");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                    dialog2.dismiss();
                }

            }
        });
        dialog2 = ProgressDialog.show(ListAtu.this, "Transferencia", "Fazendo a transferencia do arquivo...", false, true);
        t.start();
    }

    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ListAtu.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void atualizar(View v){
        listar();
    }





    public void ListarPasta(SmbFile vDir, ArrayList<String> vLista) {
        SmbFile[] files = new SmbFile[100];
        try {
            files = vDir.listFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0;i<files.length;i++){
            vLista.add(i,files[i].getName().toString());
        }

    }


    public void listar() {

        DaoCreateDB daoDBP = new DaoCreateDB(this);
        SQLiteDatabase dbP=daoDBP.getWritableDatabase();
        final SincDadosSmb smb;
        config= new DaoConfig(this).consultar();
        if(config==null){
            new DataXmlExporter(dbP, FolderConfig.getExternalStorageDirectory()).importData(this, "config", "config", "");
            config= new DaoConfig(this).consultar();
        }
        smb = new SincDadosSmb(config,4,"");




        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try{
                    fillMaps = smb.smbfileatu();
                    final Alertas a = new Alertas(ListAtu.this);
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
        dialog = ProgressDialog.show(ListAtu.this, "Verificando", "Atualizando lista ...", false, true);
        t.start();


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
                list.setAdapter(new BaseAdapterFiltroLista(ListAtu.this, fillMaps, list, "restaure"));


            }
        });


    }


}
