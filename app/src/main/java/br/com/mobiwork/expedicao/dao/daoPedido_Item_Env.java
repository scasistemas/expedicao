package br.com.mobiwork.expedicao.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by LuisGustavo on 14/09/2015.
 */
public class daoPedido_Item_Env extends DaoCreateDBEnv {

    private SQLiteDatabase db;
    private Context ctx;
    String nomebd,base;

    public daoPedido_Item_Env(Context context, String nomebd,String base) {
        super(context, nomebd);
        this.ctx=context;
        this.nomebd=nomebd;
        this.base=base;


    }

    public void instValores(Cursor c){

        try {
            db=  this.getWritableDatabase();
            Cursor pi;
            String romnumero="";
            String romproduto="";
            if(c.moveToFirst()) {
                do {
                    romnumero = c.getString(c.getColumnIndex("romnumero"));
                    romproduto = c.getString(c.getColumnIndex("romproduto"));
                    pi = db.rawQuery("select * from pedido_item where romnumero='" + romnumero + "' and romproduto='"+romproduto+"'", null);
                    if (!pi.moveToFirst()) {
                        ContentValues values = new ContentValues();
                        values.put("romitem", c.getString(c.getColumnIndex("romitem")));
                        values.put("romnumero", c.getString(c.getColumnIndex("romnumero")));
                        values.put("romproduto", c.getString(c.getColumnIndex("romproduto")));
                        values.put("romdigito", c.getString(c.getColumnIndex("romdigito")));
                        values.put("romunidade", c.getString(c.getColumnIndex("romunidade")));
                        values.put("romquantid",0.0);
                        values.put("romqtdhand", 0.0);
                        values.put("romestoque", 0.0);
                        db.insert("pedido_item", "", values);
                    }
                }while (c.moveToNext());
            }

        }catch (Exception e){
            db.close();
        }
    }

    public double getQtdHand(String romnumero,String romproduto){

        try {
            db=  this.getWritableDatabase();
            Cursor pi = db.rawQuery("select romqtdhand from pedido_item where romnumero='" + romnumero+"' and romproduto='"+romproduto+"'", null);
            if(pi.moveToFirst()){

              return  pi.getDouble(pi.getColumnIndex("romqtdhand"));
            }else{
                return 0;
            }

        }catch (Exception e){
            db.close();
        }
        return 0;
    }

    public Cursor procunidade(String procodigo,String pedido) {
        Cursor produto=null;
        try{
            db =  this.getWritableDatabase();
            produto=db.rawQuery("select  * from pedido_item where romproduto='"+procodigo+"' and romnumero='"+pedido+"'" , null);
        }catch (Exception e){
            String erro=e.getMessage();

        }finally {

        }
        return produto;
    }

    public Cursor listaritens(String pedido) {
        Cursor produto=null;
        try{
            db =  this.getWritableDatabase();
            produto=db.rawQuery("select  * from pedido_item where romnumero='"+pedido+"'" , null);
        }catch (Exception e){
            String erro=e.getMessage();

        }finally {

        }
        return produto;
    }


    public String alterarQuantidade(String procodigo,String quantidade,String pedido) {
        String unidade="";
        Cursor produto= procunidade(procodigo,pedido);
        String mensagem="";
        if(produto.moveToFirst()){
            unidade=produto.getString(produto.getColumnIndex("romunidade"));
        }
        try{
            String sqlup = ("UPDATE pedido_item SET  romqtdhand =" + quantidade + " WHERE romproduto = '" + procodigo + "' and romnumero='" + pedido + "'");
            db.execSQL(sqlup);

        }catch (Exception e){
            mensagem=e.getMessage();

        }finally {
            db.close();

        }
        return mensagem;
    }

    public String alterarQuantidade(String procodigo,Double proembalfab,String pedido,Double qtd) {
        String unidade="";
        double quantidade=0;
        Cursor produto= procunidade(procodigo,pedido);
        String mensagem="";
        if(produto.moveToFirst()){
            unidade=produto.getString(produto.getColumnIndex("romunidade"));
        }
        DaoPedido_Item dp;
        dp = new DaoPedido_Item(ctx,base);
        double quantatual=dp.procquantidade(procodigo,pedido);
        try{
            db =  this.getWritableDatabase();
            if(unidade.equalsIgnoreCase("CX")){
                quantidade=produto.getDouble(produto.getColumnIndex("romqtdhand"))+(1*qtd);
            }else if(!unidade.equalsIgnoreCase("KG") &&!unidade.equalsIgnoreCase("CX")){
                quantidade=produto.getDouble(produto.getColumnIndex("romqtdhand"))+(proembalfab*qtd);
            }
            if(quantidade>quantatual){
                mensagem="maior";
            }else{
                String sqlup = ("UPDATE pedido_item SET  romqtdhand =" + quantidade + " WHERE romproduto = '" + procodigo + "' and romnumero='" + pedido + "'");
                db.execSQL(sqlup);
            }
        }catch (Exception e){
            mensagem=e.getMessage();

        }finally {
            db.close();
        }
        return mensagem;
    }
    public String subalterarQuantidade(String procodigo,Double proembalfab,String pedido,Double qtd) {
        String unidade="";
        double quantidade=0;
        Cursor produto= procunidade(procodigo,pedido);
        String mensagem="";
        if(produto.moveToFirst()){
            unidade=produto.getString(produto.getColumnIndex("romunidade"));
        }
        DaoPedido_Item dp;
        dp = new DaoPedido_Item(ctx,base);
        double quantatual=dp.procquantidade(procodigo,pedido);
        try{
            db =  this.getWritableDatabase();
            if(unidade.equalsIgnoreCase("CX")){
                quantidade=produto.getDouble(produto.getColumnIndex("romqtdhand"))-(1*qtd);
            }else if(!unidade.equalsIgnoreCase("KG") &&!unidade.equalsIgnoreCase("CX")){
                quantidade=produto.getDouble(produto.getColumnIndex("romqtdhand"))-(proembalfab*qtd);
            }
            if(quantidade<0){
                mensagem="menor";
            }else{
                String sqlup = ("UPDATE pedido_item SET  romqtdhand =" + quantidade + " WHERE romproduto = '" + procodigo + "' and romnumero='" + pedido + "'");
                db.execSQL(sqlup);
            }
        }catch (Exception e){
            mensagem=e.getMessage();

        }finally {
            db.close();
        }
        return mensagem;
    }


}
