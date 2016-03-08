package br.com.mobiwork.expedicao.modelo;

import android.database.Cursor;

/**
 * Created by LuisGustavo on 08/05/2015.
 */
public class Pedido_Item {
    private int _id;
    private String romitem;
    private String romnumero;
    private String romproduto;
    private String romdigito;
    private String romunidade;
    private String romqtdhand;
    private String romquantid;
    private String romestoque;

    public void setPedido_Item(Cursor c){
        this._id=c.getInt(c.getColumnIndex("_id"));
        this.romnumero = c.getString(c.getColumnIndex("romnumero"));
        this.romproduto = c.getString(c.getColumnIndex("romproduto"));
        this.romdigito = c.getString(c.getColumnIndex("romdigito"));
        this.romunidade = c.getString(c.getColumnIndex("romunidade"));
        this.romqtdhand = c.getString(c.getColumnIndex("romqtdhand"));
        this.romquantid = c.getString(c.getColumnIndex("romquantid"));
        this.romestoque = c.getString(c.getColumnIndex("romestoque"));

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getRomitem() {
        return romitem;
    }

    public void setRomitem(String romitem) {
        this.romitem = romitem;
    }

    public String getRomnumero() {
        return romnumero;
    }

    public void setRomnumero(String romnumero) {
        this.romnumero = romnumero;
    }

    public String getRomproduto() {
        return romproduto;
    }

    public void setRomproduto(String romproduto) {
        this.romproduto = romproduto;
    }

    public String getRomdigito() {
        return romdigito;
    }

    public void setRomdigito(String romdigito) {
        this.romdigito = romdigito;
    }

    public String getRomunidade() {
        return romunidade;
    }

    public void setRomunidade(String romunidade) {
        this.romunidade = romunidade;
    }

    public String getRomqtdhand() {
        return romqtdhand;
    }

    public void setRomqtdhand(String romqtdhand) {
        this.romqtdhand = romqtdhand;
    }

    public String getRomquantid() {
        return romquantid;
    }

    public void setRomquantid(String romquantid) {
        this.romquantid = romquantid;
    }

    public String getRomestoque() {
        return romestoque;
    }

    public void setRomestoque(String romestoque) {
        this.romestoque = romestoque;
    }
}
