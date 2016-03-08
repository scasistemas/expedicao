package br.com.mobiwork.expedicao.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.mobiwork.expedicao.R;
import br.com.mobiwork.expedicao.dao.daoPedido_Item_Env;
import br.com.mobiwork.expedicao.modelo.Config;

/**
 * Created by LuisGustavo on 22/05/2015.
 */
public class BaseAdapterCursor  extends BaseAdapter {

    private Context context;
    private Cursor c;
    private SQLiteDatabase db;
    DateFormat formatter;
    Date d ;
    private int _id;
    ArrayList<Integer> p= new ArrayList<Integer>();
    ListView le ;
    private Config config;
    private daoPedido_Item_Env dpie;

    public BaseAdapterCursor(Context context, Cursor itens,ListView l,Config config,daoPedido_Item_Env dpie){
        this.context = context;
        this.c = itens;
        formatter= new SimpleDateFormat("dd/MM/yyyy");
        d = new Date();
        le=l;
        this.config=config;
        this.dpie= dpie;


    }


    @Override
    public int getCount() {
        return c.getCount();
    }

    @Override
    public Object getItem(int position) {
        return this.p.get(position);
    }

    @Override
    public long getItemId(int position) {

        return this.p.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_pedidos_item, null);
        }
        else{
            convertView = convertView;
        }
        boolean r=false;
        this.c.moveToPosition(position);
        this._id=Integer.parseInt(c.getString(c.getColumnIndex("_id")));
        if(p.isEmpty()){
            this.p.add(position,_id);
        }else{
            if(position==p.size()){
                this.p.add(position,_id);
            }
        }

        TextView produto = (TextView) convertView.findViewById(R.id.txproduto);
        produto.setText(c.getString(c.getColumnIndex("prodescri")));
        String prodescri=c.getString(c.getColumnIndex("prodescri"));

        TextView codprod = (TextView) convertView.findViewById(R.id.txtcodproduto);
        codprod.setText(c.getString(c.getColumnIndex("romproduto")));

        TextView num = (TextView) convertView.findViewById(R.id.txtnumero);
        num.setText(c.getString(c.getColumnIndex("romnumero")));

        TextView romdigito = (TextView) convertView.findViewById(R.id.txdig);
        romdigito.setText(c.getString(c.getColumnIndex("romdigito")));
        String romdigito2=c.getString(c.getColumnIndex("romdigito"));

        TextView romunidade = (TextView) convertView.findViewById(R.id.txun);
        romunidade.setText(c.getString(c.getColumnIndex("romunidade")));

        TextView romestoque = (TextView) convertView.findViewById(R.id.txest);
        romestoque.setText(c.getString(c.getColumnIndex("romestoque")));

        TextView romquantid = (TextView) convertView.findViewById(R.id.txquantid);
        romquantid.setText(c.getString(c.getColumnIndex("romquantid")));
        String romquantid2=c.getString(c.getColumnIndex("romquantid"));

        TextView romqtdhand = (TextView) convertView.findViewById(R.id.txqtdhand);
        romqtdhand.setText(String.valueOf(dpie.getQtdHand(c.getString(c.getColumnIndex("romnumero")),c.getString(c.getColumnIndex("romproduto")))));
        double romqtdhandp=dpie.getQtdHand(c.getString(c.getColumnIndex("romnumero")), c.getString(c.getColumnIndex("romproduto")));
        if(romqtdhandp==0){
            convertView.setBackgroundResource(R.drawable.button_shape_red);
        }else if(c.getDouble(c.getColumnIndex("romquantid"))>romqtdhandp){
            convertView.setBackgroundResource(R.drawable.button_shape_yellow);
        }else if(c.getDouble(c.getColumnIndex("romquantid"))==romqtdhandp){
            convertView.setBackgroundResource(R.drawable.button_shape_green);
        }else{
            convertView.setBackgroundResource(R.drawable.button_shape_red);

        }






        return convertView;
    }
}
