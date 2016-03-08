package br.com.mobiwork.expedicao.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.mobiwork.expedicao.dao.MyDatabaseAdapter;

/**
 * Created by LuisGustavo on 20/05/2015.
 */
public class DaoItem {

    private SQLiteDatabase db;
    Context ctx;
    MyDatabaseAdapter myDbHelper;
    String base;

    public DaoItem(Context context,String base) {
        ctx=context;
        myDbHelper = new MyDatabaseAdapter(context,base);
        db=myDbHelper.getWritableDatabase();
        this.base=base;
    }

    public Cursor consCodBar(String codbar,String pedido) {
        Cursor item=null;
        try{
            myDbHelper.open();
            db =  myDbHelper.getWritableDatabase();
            item=db.rawQuery("SELECT  *  FROM item inner join pedido_item on item.procodigo = pedido_item.romproduto where  rtrim(probarracaixa)='"+codbar.replaceAll("\\s+$", "")+"' and pedido_item.romnumero='"+pedido+"'" , null);
            if(!item.moveToFirst()){
                item=db.rawQuery("SELECT  *  FROM item inner join pedido_item on item.procodigo = pedido_item.romproduto where rtrim(probarra)='"+codbar.replaceAll("\\s+$", "")+"' and pedido_item.romnumero='"+pedido+"'" , null);
            }
        }catch (Exception e){
            String erro=e.getMessage();

        }finally {

        }
        return item;
    }

    public Cursor consnumped(String romproduto,String romnumero) {
        Cursor item=null;
        try{

            myDbHelper.open();
            db =  myDbHelper.getWritableDatabase();
            item=db.rawQuery("SELECT  *  FROM item inner join pedido_item on procodigo=romproduto where  rtrim(romproduto)='"+romproduto.replaceAll("\\s+$", "")+"' and pedido_item.romnumero='"+romnumero+"'" , null);
            if(!item.moveToFirst()){
                item=db.rawQuery("SELECT  *  FROM item inner join pedido_item on procodigo=romproduto where rtrim(romproduto)='"+romproduto.replaceAll("\\s+$", "")+"' and pedido_item.romnumero='"+romnumero+"'" , null);
            }
        }catch (Exception e){
            String erro=e.getMessage();

        }finally {

        }
        return item;
    }


}
