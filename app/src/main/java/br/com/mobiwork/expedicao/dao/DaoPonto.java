package br.com.mobiwork.expedicao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by LuisGustavo on 12/05/2015.
 */
public class DaoPonto {



    public Cursor consultar(SQLiteDatabase db,String data){
        String temp="";
        Cursor cursor=null;
        try{
            cursor= db.rawQuery("SELECT * from ponto ", null);
        }catch (Exception e){
            String erro = e.getMessage();
        }
        if(cursor.moveToFirst()){
            while (cursor.moveToNext()){
                temp=cursor.getString(cursor.getColumnIndex("datainicial"));
            }
        }
        return cursor;
    }
}
