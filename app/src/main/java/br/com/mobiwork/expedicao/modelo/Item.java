package br.com.mobiwork.expedicao.modelo;

import android.database.Cursor;

/**
 * Created by LuisGustavo on 08/05/2015.
 */
public class Item {

    private int _id;
    private String procodigo;
    private String prodescri;
    private String probarracaixa;
    private String probarra;
    private String proembalfab;


    public void setItem(Cursor c ,String emp){
        this._id = c.getInt(c.getColumnIndex("_id"));
        this.procodigo = c.getString(c.getColumnIndex("procodigo"));
        this.prodescri = c.getString(c.getColumnIndex("prodescri"));
        this.probarracaixa = c.getString(c.getColumnIndex("probarracaixa"));
        this.probarra = c.getString(c.getColumnIndex("probarra"));
        this.proembalfab = c.getString(c.getColumnIndex("proembalfab"));

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getProcodigo() {
        return procodigo;
    }

    public void setProcodigo(String procodigo) {
        this.procodigo = procodigo;
    }

    public String getProdescri() {
        return prodescri;
    }

    public void setProdescri(String prodescri) {
        this.prodescri = prodescri;
    }

    public String getProbarracaixa() {
        return probarracaixa;
    }

    public void setProbarracaixa(String probarracaixa) {
        this.probarracaixa = probarracaixa;
    }

    public String getProbarra() {
        return probarra;
    }

    public void setProbarra(String probarra) {
        this.probarra = probarra;
    }

    public String getProembalfab() {
        return proembalfab;
    }

    public void setProembalfab(String proembalfab) {
        this.proembalfab = proembalfab;
    }
}
