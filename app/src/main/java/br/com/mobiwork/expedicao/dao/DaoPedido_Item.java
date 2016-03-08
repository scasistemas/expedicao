package br.com.mobiwork.expedicao.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by LuisGustavo on 08/05/2015.
 */
public class DaoPedido_Item {

    private SQLiteDatabase db;
    Context ctx;
    MyDatabaseAdapter myDbHelper;
    String base;

    public DaoPedido_Item(Context context,String base) {
        ctx=context;
        myDbHelper = new MyDatabaseAdapter(context,base);
        db=myDbHelper.getWritableDatabase();
        this.base=base;
    }

    public Cursor listaritens(String pedido) {
        Cursor carga=null;
        try{

            myDbHelper.open();
            db =  myDbHelper.getWritableDatabase();
            carga=db.rawQuery("select * from pedido_item inner join item on procodigo=romproduto where romnumero='"+pedido+"'" , null);
        }catch (Exception e){
            String erro=e.getMessage();

        }finally {

        }
        return carga;
    }


    public Cursor listaritenstransf(String pedido) {
        Cursor carga=null;
        try{

            myDbHelper.open();
            db =  myDbHelper.getWritableDatabase();
            carga=db.rawQuery("select * from pedido_item where romnumero='"+pedido+"'" , null);
        }catch (Exception e){
            String erro=e.getMessage();

        }finally {

        }
        return carga;
    }

    public boolean verpedcompleto(String pedido,String base) {
        Cursor carga=null;
        boolean ret=true;
        daoPedido_Item_Env dpie = new daoPedido_Item_Env(ctx,"env_"+base,base);
        try{
            myDbHelper.open();
            db =  myDbHelper.getWritableDatabase();
            carga=db.rawQuery("select * from pedido_item inner join item on procodigo=romproduto where romnumero='"+pedido+"'" , null);
            int ef=carga.getCount();
            if(carga.getCount()==0){
                ret=false;
            }if(carga.moveToFirst()) {
                do {
                    double carga1=carga.getDouble(carga.getColumnIndex("romquantid"));
                    double carga2= dpie.getQtdHand(carga.getString(carga.getColumnIndex("romnumero")), carga.getString(carga.getColumnIndex("romproduto")));
                    if (carga.getDouble(carga.getColumnIndex("romquantid")) > dpie.getQtdHand(carga.getString(carga.getColumnIndex("romnumero")), carga.getString(carga.getColumnIndex("romproduto")))) {
                        ret=false;
                    }
                } while (carga.moveToNext());

            }
        }catch (Exception e){
            String erro=e.getMessage();

        }finally {

        }
       return ret;
    }

    public Cursor procunidade(String procodigo,String pedido) {
        Cursor produto=null;
        try{
            myDbHelper.open();
            db =  myDbHelper.getWritableDatabase();
            produto=db.rawQuery("select  * from pedido_item where romproduto='"+procodigo+"' and romnumero='"+pedido+"'" , null);
        }catch (Exception e){
            String erro=e.getMessage();

        }finally {

        }
       return produto;
    }

    public double procquantidade(String procodigo,String romnumero) {
        Cursor produto=null;
        try{
            myDbHelper.open();
            db =  myDbHelper.getWritableDatabase();
            produto=db.rawQuery("select  romquantid from pedido_item where romproduto='"+procodigo+"' and romnumero='"+romnumero+"'" , null);
            if(produto.moveToFirst()){
                return produto.getDouble(produto.getColumnIndex("romquantid"));
            }
        }catch (Exception e){
            String erro=e.getMessage();

        }finally {

        }
        return 0.0;
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
            myDbHelper.close();
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
        try{
            myDbHelper.open();
            db =  myDbHelper.getWritableDatabase();
            if(unidade.equalsIgnoreCase("CX")){
                quantidade=produto.getDouble(produto.getColumnIndex("romqtdhand"))+(1*qtd);
            }else if(!unidade.equalsIgnoreCase("KG") &&!unidade.equalsIgnoreCase("CX")){
                quantidade=produto.getDouble(produto.getColumnIndex("romqtdhand"))+(proembalfab*qtd);
            }
            if(quantidade>produto.getDouble(produto.getColumnIndex("romquantid"))){
                mensagem="maior";
            }else{
                String sqlup = ("UPDATE pedido_item SET  romqtdhand =" + quantidade + " WHERE romproduto = '" + procodigo + "' and romnumero='" + pedido + "'");
                db.execSQL(sqlup);
            }
        }catch (Exception e){
             mensagem=e.getMessage();

        }finally {
            db.close();
            myDbHelper.close();
        }
        return mensagem;
    }



}
