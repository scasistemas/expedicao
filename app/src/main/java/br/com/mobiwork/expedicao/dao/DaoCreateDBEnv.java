package br.com.mobiwork.expedicao.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import br.com.mobiwork.expedicao.R;

/**
 * Created by LuisGustavo on 14/09/2015.
 */
public class DaoCreateDBEnv extends SQLiteOpenHelper {

        private static final int VERSAO_BD = 1;
        private Context contexto;

        public DaoCreateDBEnv(Context context,String nomebd) {
            super(context, nomebd, null, VERSAO_BD);
            this.contexto = context;
        }



        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.beginTransaction();

            try
            {
                ExecutarComandosSQL(db, contexto.getString(R.string.tabela_pedido_item).split("\n"));

                db.setTransactionSuccessful();
            }
            catch (SQLException e)
            {
                Log.e("Erro ao CRIAR TABELAS", e.toString());
            }
            finally
            {
                db.endTransaction();
            }


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            atualizaDBSQL(db, oldVersion, newVersion);

        }

        private void ExecutarComandosSQL(SQLiteDatabase paramSQLiteDatabase, String[] paramArrayOfString)
        {
            int i = paramArrayOfString.length;
            for (int j = 0; j < i; j++)
            {
                String str = paramArrayOfString[j];
                if (str.trim().length() > 0) {
                    paramSQLiteDatabase.execSQL(str);
                }
            }
        }

        private void atualizaDBSQL(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
        {

            paramSQLiteDatabase.beginTransaction();

            try
            {
                if (paramInt1<1&&paramInt2>=1) {
                    //   ExecutarComandosSQL(paramSQLiteDatabase, contexto.getString(R.string.tabela_ponto).split("\n"));
                }
                if (paramInt1<2&&paramInt2>=2) {
                    ExecutarComandosSQL(paramSQLiteDatabase, contexto.getString(R.string.atualiza_bd_vs2_1).split("\n"));
                }
                paramSQLiteDatabase.setTransactionSuccessful();
                return;
            }
            catch (SQLException localSQLException)
            {
                Log.e("Erro ao atualizar as tabelas e testar os dados", localSQLException.toString());
                throw localSQLException;
            }
            finally
            {
                paramSQLiteDatabase.endTransaction();
            }
        }


        public long insert(SQLiteDatabase db,String table, String nullColumnHack, ContentValues values) {
            return db.insert(table, nullColumnHack, values);
        }

        public static int update(SQLiteDatabase db,String table, ContentValues values, String whereClause, String[] whereArgs){
            return db.update(table, values, whereClause, whereArgs);
        }

        public static int delete(SQLiteDatabase db,String table, String whereClause, String[] whereArgs){
            return db.delete(table, whereClause, whereArgs);
        }
}


