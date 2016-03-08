package br.com.mobiwork.expedicao.modelo;

import android.database.Cursor;

/**
 * Created by LuisGustavo on 08/05/2015.
 */
public class Carga {

    private int _id;
    private String carga;
    private String cardescri;
    private String carimport;
    private int carstatus;

    public void setCarga(Cursor c, String emp){
        this._id=c.getInt(c.getColumnIndex("_id"));
        this.carga = c.getString(c.getColumnIndex("carga"));
        this.cardescri = c.getString(c.getColumnIndex("cardescri"));
        this.carimport = c.getString(c.getColumnIndex("carimport"));
        this.carstatus = c.getInt(c.getColumnIndex("carstatus"));

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getCarstatus() {
        return carstatus;
    }

    public void setCarstatus(int carstatus) {
        this.carstatus = carstatus;
    }

    public String getCarga() {
        return carga;
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }

    public String getCardescri() {
        return cardescri;
    }

    public void setCardescri(String cardescri) {
        this.cardescri = cardescri;
    }

    public String getCarimport() {
        return carimport;
    }

    public void setCarimport(String carimport) {
        this.carimport = carimport;
    }
}
