package br.com.mobiwork.expedicao.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LuisGustavo on 08/05/2015.
 */

public class DaoPedido  {

    private SQLiteDatabase db;
    Context ctx;
    MyDatabaseAdapter myDbHelper;
    String base;

    public DaoPedido(Context context,String base) {
        ctx=context;
        myDbHelper = new MyDatabaseAdapter(context,base);
        db=myDbHelper.getWritableDatabase();
        this.base=base;
    }

    public Cursor listarPedidos() {
        Cursor carga=null;

        try{

            myDbHelper.open();
            db =  myDbHelper.getWritableDatabase();
            carga=db.rawQuery("SELECT  *  FROM pedido  " , null);
        }catch (Exception e){
            String erro=e.getMessage();

        }finally {

        }
        return carga;
    }
}
