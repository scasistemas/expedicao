package br.com.mobiwork.expedicao.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import br.com.mobiwork.expedicao.util.FolderConfig;

/**
 * Created by LuisGustavo on 11/05/2015.
 */
public class MyDatabaseAdapter extends SQLiteOpenHelper {

    private Context mycontext;
    String dbPackage = "br.com.mobiwork.expedicao";
    private String DB_PATH = Environment.getDataDirectory() + "/data/" + dbPackage + "/databases/";
    //dont forget to change your namespace.package.app
    private static String DB_NAME = "";
    // the extension may be .sqlite or db
    public SQLiteDatabase myDataBase;



    public MyDatabaseAdapter(Context context,String nomeBase) {
        super(context, nomeBase, null, 1);
        this.DB_NAME=nomeBase;
        this.mycontext = context;
        boolean dbexist = checkdatabase(DB_PATH,DB_NAME);

        if (dbexist) {
            Log.e("tabela", "Base existente");
        } else {
            System.out.println("Database doesn't exist");
            try {
                createdatabase();
                createdatabaseenv();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void createdatabaseenv() throws IOException {


        boolean dbexist = checkdatabase(DB_PATH ,"env_"+ DB_NAME);
        if (dbexist) {
            Log.e("tabela", "Base existente");
        } else {
            this.getReadableDatabase();
            String thisFile =  FolderConfig.getExternalStorageDirectorycargaFechada() + "/"+"env_"+DB_NAME;
            File file = new File(thisFile);
            if (!file.exists()) {
                thisFile = FolderConfig.getExternalStorageDirectorycargaAberta() + "/"+"env_"+DB_NAME;
                file = new File(thisFile);
            }
            File dbFile = new File(thisFile);
            if (dbFile.exists()) {
                String dbPath = Environment.getDataDirectory() + "/data/" + dbPackage + "/databases/";
                File exportDir = new File(dbPath);
                file = new File(exportDir, "env_"+DB_NAME);
                try {
                    file.createNewFile();
                    copydatabase(dbFile, file);

                } catch (IOException e) {
                    String t=e.getMessage();

                }
                //break;
            }
            //   copydatabase();
        }
    }

    public void createdatabase() throws IOException {


        boolean dbexist = checkdatabase(DB_PATH , DB_NAME);
        if (dbexist) {
            Log.e("tabela", "Base existente");
        } else {
            this.getReadableDatabase();
            String thisFile =  FolderConfig.getExternalStorageDirectorycargaFechada() + "/"+DB_NAME;
            File file = new File(thisFile);
            if (!file.exists()) {
                thisFile = FolderConfig.getExternalStorageDirectorycargaAberta() + "/"+DB_NAME;
                file = new File(thisFile);
            }
            File dbFile = new File(thisFile);
            if (dbFile.exists()) {
                String dbPath = Environment.getDataDirectory() + "/data/" + dbPackage + "/databases/";
                File exportDir = new File(dbPath);
                file = new File(exportDir, DB_NAME);
                try {
                    file.createNewFile();
                    copydatabase(dbFile, file);

                } catch (IOException e) {
                    String t=e.getMessage();

                }
                //break;
            }
            //   copydatabase();
        }
    }

    public boolean checkdatabase( String DB_PATH ,String DB_NAME) {
        boolean checkdb = false;
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            System.out.println("Database doesn't exist");
        }

        return checkdb;
    }

    private void copydatabase(java.io.File src, java.io.File dst) throws IOException {

        // Open your local db as the input stream
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
        // Close the streams
    }

    public void open() {
        // Open the database
        String mypath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(mypath, null,
                SQLiteDatabase.OPEN_READWRITE);

    }

    public synchronized void close() {
        myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
