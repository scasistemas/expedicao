package br.com.mobiwork.expedicao.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import br.com.mobiwork.expedicao.modelo.Config;

/**
 * Created by LuisGustavo on 14/05/2015.
 */
public class DaoConfig extends DaoCreateDB  {


    private Cursor cursor;
    SQLiteDatabase db;

    public DaoConfig(Context context) {
        super(context);


    }
    public static Config getConfig(SQLiteDatabase db){

        Config config = new Config();
        Cursor c = db.rawQuery("SELECT * FROM config tb " +
                " WHERE tb._id = ?",  new String[]{"1"});
        if (c.moveToFirst()) {
            config.setConfig(c);
        }


        return config;
    }


    public Config consultar() {
        db=this.getWritableDatabase();
        Config config = new Config();
        try {
            cursor = db.rawQuery("SELECT * FROM config tb " +
                    " WHERE tb._id = ?", new String[]{"1"});

            if (cursor.moveToFirst()) {
                config.setConfig(cursor);
            }
        }catch (Exception e){

        }finally {

        }
        return config;
    }

    public void updateImgTemp ( String imgTemp){
        this.db=this.getWritableDatabase();
        try{
            String sqlup=("UPDATE config SET imgTemp = '"+imgTemp+"'");
            db.execSQL(sqlup);
        }catch(Exception e){
            String erro= e.getMessage();
        }finally {
            db.close();
        }
    }

    public void update ( String endereco,String login,String senha){
        this.db=this.getWritableDatabase();
        try{
            String sqlup=("UPDATE config SET endereco = '"+endereco+"' , login='"+login+"' , senha='"+senha+"'  WHERE _id='1'");
            db.execSQL(sqlup);
        }catch(Exception e){
            String erro= e.getMessage();
        }finally {
            db.close();
        }
    }
    public void  atuImgTemp(){
        this.db=this.getWritableDatabase();
        Config config = new Config();
        Cursor c = db.rawQuery("SELECT * FROM config tb " +
                " WHERE tb._id = ?",  new String[]{"1"});
        if (c.moveToFirst()) {

           if(c.getString(c.getColumnIndex("imgTemp"))==null){
               updateImgTemp( Environment.getExternalStorageDirectory().toString()+"//BarcodeScanner/History");
           }
        }
    }






   /* MyDatabaseAdapter myDbHelper;
    SQLiteDatabase db;
    public DaoConfig(Context context,String base)
    {
         myDbHelper = new MyDatabaseAdapter(context,base);

    }


    public Config consultar() {
        Config config=NUL;
        try {
            myDbHelper.open();
            db=myDbHelper.getWritableDatabase();
             config = new Config();
            Cursor cursor = db.rawQuery("SELECT * FROM config tb " +
                    " WHERE tb._id = ?", new String[]{"1"});

            if (cursor.moveToFirst()) {
                config.setConfig(cursor);
            }
        }catch (Exception e){

        }finally {
            myDbHelper.close();
            db.close();

        }
        return config;
    }*/
}
