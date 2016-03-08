package br.com.mobiwork.expedicao.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import br.com.mobiwork.expedicao.dao.DaoPedido_Item;
import br.com.mobiwork.expedicao.modelo.Config;

/**
 * Created by LuisGustavo on 22/05/2015.
 */
public class BaseAdapterCursorPed  extends BaseAdapter {

    private Context context;
    private Cursor c;
    private SQLiteDatabase db;
    DateFormat formatter;
    Date d ;
    private int _id;
    ArrayList<Integer> p= new ArrayList<Integer>();
    ListView le ;
    private Config config;
    private DaoPedido_Item dped;
    String carga="";


    public BaseAdapterCursorPed(Context context, Cursor itens,ListView l,Config config,String carga){
        this.context = context;
        this.c = itens;
        formatter= new SimpleDateFormat("dd/MM/yyyy");
        d = new Date();
        le=l;
        this.config=config;
        dped = new DaoPedido_Item(context,carga);
        this.carga=carga;



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
            convertView = inflater.inflate(R.layout.list_pedidos, null);
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

        TextView pedido = (TextView) convertView.findViewById(R.id.nomeped);
        pedido.setText(c.getString(c.getColumnIndex("romnumero")));

        TextView numped = (TextView) convertView.findViewById(R.id.numped);
        TextView romnomecliente = (TextView) convertView.findViewById(R.id.nomecliente);
        romnomecliente.setText(c.getString(c.getColumnIndex("romnomecliente")));


        if(dped.verpedcompleto(c.getString(c.getColumnIndex("romnumero")),carga)) {
            convertView.setBackgroundResource(R.drawable.button_shape_green);
            romnomecliente.setTextColor(Color.WHITE);
            numped.setTextColor(Color.WHITE);
            pedido.setTextColor(Color.WHITE);
        }else{
            convertView.setBackgroundResource(R.drawable.button_shape_white);
            romnomecliente.setTextColor(Color.BLACK);
            pedido.setTextColor(Color.BLACK);
            numped.setTextColor(Color.BLACK);
        }
        /*}else if(c.getDouble(c.getColumnIndex("romquantid"))>c.getDouble(c.getColumnIndex("romqtdhand"))){
            convertView.setBackgroundResource(R.drawable.button_shape_yellow);
        }else if(c.getDouble(c.getColumnIndex("romquantid"))==c.getDouble(c.getColumnIndex("romqtdhand"))){
            convertView.setBackgroundResource(R.drawable.button_shape_green);
        }*/






        return convertView;
    }
}
