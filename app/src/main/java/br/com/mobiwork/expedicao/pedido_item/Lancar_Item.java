package br.com.mobiwork.expedicao.pedido_item;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import br.com.mobiwork.expedicao.R;
import br.com.mobiwork.expedicao.carga.ListCargasBaixadas;
import br.com.mobiwork.expedicao.dao.DaoCarga;
import br.com.mobiwork.expedicao.dao.DaoConfig;
import br.com.mobiwork.expedicao.dao.DaoCreateDBEnv;
import br.com.mobiwork.expedicao.dao.DaoItem;
import br.com.mobiwork.expedicao.dao.DaoPedido;
import br.com.mobiwork.expedicao.dao.DaoPedido_Item;
import br.com.mobiwork.expedicao.dao.daoPedido_Item_Env;
import br.com.mobiwork.expedicao.modelo.Config;
import br.com.mobiwork.expedicao.sinc.SincUp;
import br.com.mobiwork.expedicao.util.Alertas;
import br.com.mobiwork.expedicao.util.BaseAdapterCursor;
import br.com.mobiwork.expedicao.util.BkpBancoDeDados;
import br.com.mobiwork.expedicao.util.PedidoRollBack;
import br.com.mobiwork.expedicao.util.SincDadosLocal;

/**
 * Created by LuisGustavo on 20/05/2015.
 */
public class Lancar_Item extends Activity implements AdapterView.OnItemClickListener {

    private ListView cargaList;
    private Alertas a ;
    String pedido,base;
    DaoItem di ;
    DaoPedido_Item dp;
    private ListAdapter adapter;
    private TextView txnum;
    private SincDadosLocal sdc;
    private Config config;
    private DaoCarga dc;
    private String carga;
    private EditText edtxqtd;
    private  daoPedido_Item_Env dpie;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_item);
        cargaList = (ListView) findViewById (R.id.list);
        txnum=(TextView)findViewById(R.id.txnumero);
        edtxqtd=(EditText)findViewById(R.id.edtxqtd);
        pedido=getIntent().getStringExtra("pedido");
        txnum.setText(pedido);
        base=getIntent().getStringExtra("nomeb");
        di = new DaoItem(this,base);
        dp= new DaoPedido_Item(this,base);
        dpie= new daoPedido_Item_Env(this,"env_"+base,base);
        dpie.instValores(dp.listaritens(pedido));
        dc = new DaoCarga(this,base);
        listar();
        cargaList.setOnItemClickListener(this);
        a= new Alertas(this);
        PedidoRollBack.criarPedidoRollBack(pedido,"env_"+base);
        Cursor cursorItens = dpie.listaritens(pedido);
        PedidoRollBack.setItemPedido(cursorItens);
        config=new DaoConfig(this).consultar();
        sdc = new SincDadosLocal(config,2);
        carga=dc.getNomecargas();


    }

    public void codbar(View v){
        if(dc.getstatusCarga(carga)==0) {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            intent.putExtra("SAVE_HISTORY", false);
            intent.putExtra("SCAN_FORMATS", "ITF,EAN_13,EAN_8,QR_CODE");
            try {
                startActivityForResult(intent, 0);

            } catch (ActivityNotFoundException e) {
                a.baixarBarcode();
            }
        }else{
            Alertas a = new Alertas(this);
            a.AlertaSinc("Carga Fechada !");
        }

    }

    public void salvar(View v){
        new AlertDialog.Builder(this)
                .setTitle("Tem certeza que deseja salvar?")
                .setItems(R.array.op_alerta_sim_nao, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        if (i == 0) {
                            if (sdc.checkFile(base)) {
                                Intent ix;
                                ix = new Intent(Lancar_Item.this, BkpBancoDeDados.class);
                                ix.putExtra("tBkp", "ecarga");
                                ix.putExtra("carga", base);
                                Lancar_Item.this.startActivityForResult(ix, 100);
                            } else {
                                Lancar_Item.this.finish();
                            }
                        }
                    }
                }).show();


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Codigo de Barras
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            double quantidade=1;
            if(!edtxqtd.getText().toString().equalsIgnoreCase("")){
                quantidade=Double.parseDouble(edtxqtd.getText().toString());
            }
            String unidade;
           // Intent i = new Intent(Lancar_Item.this,IncluirPedido.class);
            String qrcode = data.getStringExtra("SCAN_RESULT");
            //rcString qrcode="7898130990543";
            String format = data.getStringExtra("SCAN_RESULT_FORMAT");
            String name =data.getStringExtra("barcode_data");
            Cursor item= di.consCodBar(qrcode,pedido);
            if(item.moveToFirst()){
            //   String m=dp.alterarQuantidade(item.getString(item.getColumnIndex("procodigo")),item.getDouble(item.getColumnIndex("proembalfab")),pedido,quantidade);
                String m=dpie.alterarQuantidade(item.getString(item.getColumnIndex("procodigo")),item.getDouble(item.getColumnIndex("proembalfab")),pedido,quantidade);
                if(m.equalsIgnoreCase("maior")){
                    a.AlertaSinc("Quantidade excede a do pedido!");
                }
            } else {
                a.AlertaSinc("Nao foi encontrado o codigo de barra ("+qrcode+") no pedido!");
            }
            listar();
        }
        if (requestCode ==100) {
            Lancar_Item.this.finish();
        }
        edtxqtd.setText("");
    }

    public void voltar(View v){

        new AlertDialog.Builder(this)
                .setTitle("Tem certeza que deseja sair?")
                .setItems(R.array.op_alerta_sim_nao, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        if (i == 0){
                            PedidoRollBack.rollBackpedido(Lancar_Item.this);
                            Lancar_Item.this.finish();
                        }
                    }
                }).show();



    }

    public void listar() {
        Cursor pedidos;
        pedidos= dp.listaritens(pedido);
       /* adapter = new SimpleCursorAdapter(this,R.layout.list_pedidos_item,pedidos,new String[] {"prodescri","romdigito","romunidade","romestoque","romquantid","romqtdhand"},
                new int[] {R.id.txproduto,R.id.txdig,R.id.txun,R.id.txest,R.id.txquantid,R.id.txqtdhand});*/
        cargaList.setAdapter(new BaseAdapterCursor(this, pedidos, cargaList,config,dpie));

        cargaList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                if(dc.getstatusCarga(carga)==0) {
                    TextView tn = (TextView) arg1.findViewById(R.id.txtnumero);
                    TextView tp = (TextView) arg1.findViewById(R.id.txtcodproduto);
                    subtrair(tn.getText().toString(), tp.getText().toString());
                }else {
                    a.AlertaSinc("CARGA FECHADA");
                }

                return true;
            }
        });

    }

    private void subtrair(final String numero,final String produto) {
        new AlertDialog.Builder(this)
                .setItems(R.array.op_del_ent, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        new AlertDialog.Builder(Lancar_Item.this)
                                .setTitle("Deseja subtrair uma entrada deste item?")
                                .setItems(R.array.op_alerta_sim_nao, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        if (i == 0) {
                                            double quantidade=1;
                                            if(!edtxqtd.getText().toString().equalsIgnoreCase("")){
                                                quantidade=Double.parseDouble(edtxqtd.getText().toString());
                                            }
                                            Cursor item = di.consnumped(produto, numero);
                                            if (item.moveToFirst()) {
                                                //   String m=dp.alterarQuantidade(item.getString(item.getColumnIndex("procodigo")),item.getDouble(item.getColumnIndex("proembalfab")),pedido,quantidade);
                                                String m = dpie.subalterarQuantidade(item.getString(item.getColumnIndex("procodigo")), item.getDouble(item.getColumnIndex("proembalfab")), pedido, quantidade);
                                                if(m.equalsIgnoreCase("menor")){
                                                    a.AlertaSinc("Quantidade menor que 0 !");
                                                }
                                            }

                                            listar();
                                            edtxqtd.setText("");
                                            }
                                        }
                                    }

                                    ).

                                    show();

                                }
                    }

                    ).

                    show();
                }


    public void carregarItens(View v){
        Intent ix;
        ix = new Intent(this, ListCargasBaixadas.class);
        this.startActivityForResult(ix, 0);
    }


    @Override
    public void onBackPressed() {
        return;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


    }


}
