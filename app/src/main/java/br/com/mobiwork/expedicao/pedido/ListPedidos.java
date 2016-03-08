package br.com.mobiwork.expedicao.pedido;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.mobiwork.expedicao.R;
import br.com.mobiwork.expedicao.carga.ListCargas;
import br.com.mobiwork.expedicao.carga.ListCargasBaixadas;
import br.com.mobiwork.expedicao.configuracoes.AsyncResponse;
import br.com.mobiwork.expedicao.dao.DaoCarga;
import br.com.mobiwork.expedicao.dao.DaoConfig;
import br.com.mobiwork.expedicao.dao.DaoPedido;
import br.com.mobiwork.expedicao.dao.DaoPonto;
import br.com.mobiwork.expedicao.dao.MyDatabaseAdapter;
import br.com.mobiwork.expedicao.modelo.Config;
import br.com.mobiwork.expedicao.pedido_item.Lancar_Item;
import br.com.mobiwork.expedicao.sinc.SincDown;
import br.com.mobiwork.expedicao.sinc.SincUp;
import br.com.mobiwork.expedicao.util.Alertas;
import br.com.mobiwork.expedicao.util.BaseAdapterCursor;
import br.com.mobiwork.expedicao.util.BaseAdapterCursorPed;
import br.com.mobiwork.expedicao.util.BaseAdapterFiltro;
import br.com.mobiwork.expedicao.util.BkpBancoDeDados;
import br.com.mobiwork.expedicao.util.SincDadosLocal;
import br.com.mobiwork.expedicao.util.SincDadosSmb;

/**
 * Created by LuisGustavo on 19/05/2015.
 */
public class ListPedidos extends Activity implements AdapterView.OnItemClickListener,AsyncResponse {

    private ListView cargaList;
    private Alertas a ;
    String base;
    DaoPedido dp ;
    private ListAdapter adapter;
    private TextView txnumped;
    private SincUp sincup;
    private Config config;
    private SincDadosLocal sdc;
    private Spinner spstatus;
    private DaoCarga dc;
    String carga;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pedidos);
        cargaList = (ListView) findViewById (R.id.list);
        txnumped = (TextView) findViewById (R.id.pednum);
        spstatus=(Spinner)findViewById(R.id.status);
        base=getIntent().getStringExtra("nomeb");
        lancar(base);
        dc = new DaoCarga(this,base);
        carga=dc.getNomecargas();
        dp = new DaoPedido(this,base);
        listar();
        cargaList.setOnItemClickListener(this);
        a= new Alertas(this);
        config=new DaoConfig(this).consultar();
        sdc = new SincDadosLocal(config,2);
        popcombosit();


    }

    public void lancar(String base){
        txnumped.setText(base);

    }


    public void listar() {
        Cursor pedidos;
        pedidos= dp.listarPedidos();
      //  adapter = new SimpleCursorAdapter(this,R.layout.list_pedidos,pedidos,new String[] {"romnumero","romnomecliente"},
           //     new int[] {R.id.nomeped,R.id.nomecliente});
        //cargaList.setAdapter(adapter);
        if(pedidos!=null) {

            cargaList.setAdapter(new BaseAdapterCursorPed(this, pedidos, cargaList, config, base));
        }

    }//
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Pedido Exportado
        if (requestCode == 1) {

            // Intent i = new Intent(Lancar_Item.this,IncluirPedido.class);
           try {
               sincup=new SincUp(ListPedidos.this,"",config,base,carga);
               sincup.delegate = ListPedidos.this;
               sincup.execute(new String[0]);

           }catch (Exception e){
                String erro =e.getMessage();
            }

        }

        if (requestCode == 101) {
             ListPedidos.this.finish();
        }
        listar();
    }


    public void voltar(View v){

        if(sdc.checkFile(base)){
            this.deleteDatabase(base);
        }
        ListPedidos.this.finish();
    }


    public void popcombosit(){
            List<String> nomes = new ArrayList<String>();
            final String[] nome = new String[1];

            nomes.add("ABERTA");
            nomes.add("FECHADA");

            //Identifica o Spinner no layout
            //Cria um ArrayAdapter usando um padrao de layout da classe R do android, passando o ArrayList nomes
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nomes);
            ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spstatus.setAdapter(spinnerArrayAdapter);


             DaoCarga dca = new DaoCarga(this,base);

             spstatus.setSelection(dca.getstatusCarga(carga));


            //Metodo do Spinner para capturar o item selecionado
            spstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                    //pega nome pela posicao


                    if(spstatus.getSelectedItem().toString().equalsIgnoreCase("ABERTA")&&dc.getstatusCarga(carga)==1) {
                        new AlertDialog.Builder(ListPedidos.this)
                                .setTitle("Atencao Deseja reabrir a carga ?")
                                .setItems(R.array.op_alerta_sim_nao, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        if (i == 0) {
                                            DaoCarga dc = new DaoCarga(ListPedidos.this,base);
                                            SincDadosLocal sdc = new SincDadosLocal(config,1);
                                            if(sdc.verifCarga(carga)){
                                                Alertas a = new Alertas(ListPedidos.this);
                                                a.AlertaSinc("Ja existe a carga "+carga+" aberta, Delete antes de abrir esta!");
                                            }else {
                                                dc.statusCarga(carga, 0);
                                                SincDadosLocal sd = new SincDadosLocal(config,2);
                                                sd.deleteFile(base);
                                                ListPedidos.this.deleteDatabase(carga);
                                                sd.deleteFile("env_"+base);
                                                ListPedidos.this.deleteDatabase("env_"+carga);
                                                altstatus();
                                            }
                                        }
                                    }
                                }).show();


                        //     this.deleteDatabase("123456.db");
                    }


                    nome[0] = parent.getItemAtPosition(posicao).toString();
                    //imprime um Toast na tela com o nome que foi selecionado

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

    public void enviar(View v){
        if(spstatus.getSelectedItem().toString().equalsIgnoreCase("FECHADA")) {
            new AlertDialog.Builder(this)
                    .setTitle("Atencao Carga Fechada.Deseja prosseguir ?")
                    .setItems(R.array.op_alerta_sim_nao, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            if (i == 0) {
                                DaoCarga dc = new DaoCarga(ListPedidos.this,base);
                                dc.statusCarga(carga,1);
                                enviar();
                            }
                        }
                    }).show();


            //     this.deleteDatabase("123456.db");
        }else {
            enviar();
        }
    }

    public void enviar(){
        Intent ix;
        ix = new Intent(this, BkpBancoDeDados.class);
        ix.putExtra("tBkp", "ecarga");
        ix.putExtra("carga", base);
        ix.putExtra("nomcarga",carga);
        ix.putExtra("env_carga","env_"+base);
        this.startActivityForResult(ix, 1);
    }

    public void altstatus(){
        Intent ix;
        ix = new Intent(this, BkpBancoDeDados.class);
        ix.putExtra("tBkp", "ecarga");
        ix.putExtra("carga", base);
        ix.putExtra("nomcarga",carga);
        ix.putExtra("env_carga","env_"+carga);
        this.startActivityForResult(ix, 101);
    }

    @Override
    public void onBackPressed() {
        return;
    }





    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        TextView tv = (TextView) v.findViewById(R.id.nomeped);
        String pedido=tv.getText().toString();
        Intent ix;
        ix = new Intent(this, Lancar_Item.class);
        ix.putExtra("pedido", pedido);
        ix.putExtra("nomeb", base);
        this.startActivityForResult(ix, 0);

    }


    @Override
    public void processFinish(String output) {

    }
}
