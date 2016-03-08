package br.com.mobiwork.expedicao.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.mobiwork.expedicao.R;
import br.com.mobiwork.expedicao.dao.DaoCreateDB;

/**
 * Created by LuisGustavo on 18/05/2015.
 */
public class BaseAdapterC extends BaseAdapter {

    private Context context;
    private Cursor c, c2;
    private SQLiteDatabase db;
    DateFormat formatter;
    Date d;
    private int _id;
    private String _id2;
    ArrayList<Integer> p = new ArrayList<Integer>();
    ArrayList<String> p2 = new ArrayList<String>();
    String sit, filtro;
    ListView le;
    List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
    ArrayList<String> checkedValue;

    public BaseAdapterC(Context context, List<HashMap<String, String>> fillMaps2, ListView l, String filtror) {
        this.context = context;
        //this.c = rotas;
        this.fillMaps = fillMaps2;
        DaoCreateDB dao = new DaoCreateDB(context);
        db = dao.getWritableDatabase();
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        d = new Date();
        le = l;
        this.filtro = filtror;
        checkedValue = new ArrayList<String>();

    }

    public ArrayList<String> getCheckedValue() {
        return checkedValue;
    }

    public void setCheckedValue(String value) {
        this.checkedValue.add(value);
    }

    public void reomevValue(String value) {
        this.checkedValue.remove(value);
    }

    @Override
    public int getCount() {
        return fillMaps.size();
    }

    @Override
    public Object getItem(int position) {

            return position;

    }


    @Override
    public long getItemId(int position) {
            return position;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_cargasb, null);
        }

        TextView descricao = (TextView) convertView.findViewById(R.id.carga);
        descricao.setText(fillMaps.get(position).get("_id"));
        TextView data = (TextView) convertView.findViewById(R.id.data);
        data.setText(fillMaps.get(position).get("data"));
        TextView hora = (TextView) convertView.findViewById(R.id.hora);
        hora.setText(fillMaps.get(position).get("hora"));
        return convertView;

    }
}