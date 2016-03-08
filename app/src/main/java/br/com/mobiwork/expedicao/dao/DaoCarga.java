package br.com.mobiwork.expedicao.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.mobiwork.expedicao.modelo.Carga;

/**
 * Created by LuisGustavo on 08/05/2015.
 */
public class DaoCarga  {

    private SQLiteDatabase db;
    Context ctx;
    MyDatabaseAdapter myDbHelper;
    String base;
    public DaoCarga(Context context,String base) {

        ctx=context;
        myDbHelper = new MyDatabaseAdapter(context,base);

        this.base=base;
    }

    public Cursor cargas(String carnumero) {
        Cursor carga=null;
        myDbHelper.open();
        db =  myDbHelper.getWritableDatabase();
        try{
             carga=db.rawQuery("SELECT  *  FROM carga  " +
                    " WHERE carnumero = ? " ,  new String[]{""+carnumero});
        }catch (Exception e){

        }finally {
            db.close();
            myDbHelper.close();
        }
        return carga;
    }

    public String getNomecargas() {
        Cursor carga=null;
        myDbHelper.open();
        db =  myDbHelper.getWritableDatabase();
        try{
            carga=db.rawQuery("SELECT  *  FROM carga" , null);
            if(carga.moveToFirst()){
                return carga.getString(carga.getColumnIndex("carnumero"));
            }
        }catch (Exception e){
            String erro=e.getMessage();

        }finally {
            db.close();
            myDbHelper.close();
        }
        return "";
    }
    public void statusCarga (String carga, int status){
        myDbHelper.open();
        db =  myDbHelper.getWritableDatabase();
        try{
            String sqlup=("UPDATE carga SET carstatus = "+status+" WHERE carnumero='"+carga+"'");
            db.execSQL(sqlup);
        }catch(Exception e){
            String erro= e.getMessage();
        }finally {
            db.close();
            myDbHelper.close();
        }
    }
    public int getstatusCarga (String carga){
        myDbHelper.open();
        db =  myDbHelper.getWritableDatabase();
        try{
          Cursor dadoscarga=db.rawQuery("select carstatus from carga where carnumero='"+carga+"'",null);
            if(dadoscarga.moveToFirst()){
                return dadoscarga.getInt(dadoscarga.getColumnIndex("carstatus"));
            }
        }catch(Exception e){
            String erro= e.getMessage();
            return 0;
        }finally {
            db.close();
            myDbHelper.close();
        }
        return 0;
    }






}
