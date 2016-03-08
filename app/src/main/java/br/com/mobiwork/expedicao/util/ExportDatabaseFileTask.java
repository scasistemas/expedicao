package br.com.mobiwork.expedicao.util;

/**
 * Created by LuisGustavo on 19/05/2015.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import br.com.mobiwork.expedicao.dao.DaoCarga;


public class ExportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = ExportDatabaseFileTask.class.getSimpleName();

    private final ProgressDialog dialog;
    private String dbPackage;
    private String dbName;
    private String dbPath;
    String caminho,nomcarga;
    private DaoCarga dc;

    public ExportDatabaseFileTask(Context context, String dbPackage, String dbName,String caminho,String nomcarga) {
        dialog = new ProgressDialog(context);
        this.dbPackage = dbPackage;
        this.dbName = dbName;
        dc = new DaoCarga(context,nomcarga+".db");
        dbPath = Environment.getDataDirectory() + "/data/" + dbPackage + "/databases/";
        this.caminho=caminho;
        this.nomcarga=nomcarga;
    }

    // can use UI thread here
    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Exportando database...");
        this.dialog.show();
    }

    // automatically done on worker thread (separate from UI thread)
    @Override
    protected Boolean doInBackground(String[] paramArrayOfString)
    {
        File localFile2=null;
        File localFile1 = new File(this.dbPath + this.dbName);
        if(caminho.equalsIgnoreCase("carga")){
            if(dc.getstatusCarga(nomcarga)==1) {
                localFile2 = new File(FolderConfig.getExternalStorageDirectorycargaFechada());
            }else{
                localFile2 = new File(FolderConfig.getExternalStorageDirectorycargaAberta());
            }
        }else {
             localFile2 = new File(FolderConfig.getExternalStorageDirectory());
        }
        if (!localFile2.exists())
            localFile2.mkdirs();
        File localFile3 = new File(localFile2, this.dbName);
        try
        {
            localFile3.createNewFile();
            copyFile(localFile1, localFile3);
            Boolean localBoolean = Boolean.valueOf(true);
            return localBoolean;
        }
        catch (IOException localIOException)
        {
            Log.e(TAG, localIOException.getMessage(), localIOException);
        }
        return Boolean.valueOf(false);
    }

    // can use UI thread here
    @Override
    protected void onPostExecute(Boolean paramBoolean)
    {
        if (this.dialog.isShowing())
            this.dialog.dismiss();
        if (paramBoolean.booleanValue())
        {
            //    BkpBancoDeDados.showShortToast("Exportado com sucesso!");
            return;
        }
        //  BkpBancoDeDados.showShortToast("Export.failed");
    }

    void copyFile(File paramFile1, File paramFile2)
            throws IOException
    {
        FileChannel localFileChannel1 = new FileInputStream(paramFile1).getChannel();
        FileChannel localFileChannel2 = new FileOutputStream(paramFile2).getChannel();
        try
        {
            localFileChannel1.transferTo(0L, localFileChannel1.size(), localFileChannel2);
            return;
        }
        finally
        {
            if (localFileChannel1 != null)
                localFileChannel1.close();
            if (localFileChannel2 != null)
                localFileChannel2.close();
        }

    }

}