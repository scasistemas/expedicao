package br.com.mobiwork.expedicao.modelo;

import android.database.Cursor;

/**
 * Created by LuisGustavo on 08/05/2015.
 */
public class Pedido {

    private int _id;
    private String romcarga;
    private String romnumero;
    private String romcliente;
    private String romnomecliente;
    private String romdata;

    public void setPedido(Cursor c, String emp){
        this._id=c.getInt(c.getColumnIndex("_id"));
        this.romcarga = c.getString(c.getColumnIndex("romcarga"));
        this.romnumero = c.getString(c.getColumnIndex("romnumero"));
        this.romcliente = c.getString(c.getColumnIndex("romcliente"));
        this.romnomecliente = c.getString(c.getColumnIndex("romnomecliente"));
        this.romdata = c.getString(c.getColumnIndex("romdata"));
    }
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getRomnumero() {
        return romnumero;
    }

    public void setRomnumero(String romnumero) {
        this.romnumero = romnumero;
    }

    public String getRomcliente() {
        return romcliente;
    }

    public void setRomcliente(String romcliente) {
        this.romcliente = romcliente;
    }

    public String getRomnomecliente() {
        return romnomecliente;
    }

    public void setRomnomecliente(String romnomecliente) {
        this.romnomecliente = romnomecliente;
    }

    public String getRomdata() {
        return romdata;
    }

    public void setRomdata(String romdata) {
        this.romdata = romdata;
    }
}
