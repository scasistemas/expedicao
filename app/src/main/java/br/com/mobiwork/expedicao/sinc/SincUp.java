package br.com.mobiwork.expedicao.sinc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;

import br.com.mobiwork.expedicao.configuracoes.AsyncResponse;
import br.com.mobiwork.expedicao.dao.DaoCarga;
import br.com.mobiwork.expedicao.dao.DaoConfig;
import br.com.mobiwork.expedicao.dao.DaoCreateDB;
import br.com.mobiwork.expedicao.dao.MyDatabaseAdapter;
import br.com.mobiwork.expedicao.modelo.Config;
import br.com.mobiwork.expedicao.util.Alertas;
import br.com.mobiwork.expedicao.util.DataXmlExporter;
import br.com.mobiwork.expedicao.util.FolderConfig;
import br.com.mobiwork.expedicao.util.SincDadosLocal;
import br.com.mobiwork.expedicao.util.SincDadosSmb;
import br.com.mobiwork.expedicao.util.ToastManager;

/**
 * Created by LuisGustavo on 18/05/2015.
 */
public class SincUp  extends AsyncTask<String, Void, Boolean> {

    private SQLiteDatabase db;
    private ProgressDialog dialog;
    private Handler handler = new Handler();
    private String erro,carga;
    private Config config;
    private DaoCreateDB daoDB;
    private DaoConfig dc;
    Intent i ;
    ToastManager tm;
    SincDadosSmb smb;
    Context ctx;
    Alertas a;
    Activity confirma = (Activity)ctx;
    public AsyncResponse delegate=null;
    String caminteste,nomcarga;
    private volatile boolean running = true;
    private DaoCarga dca ;


    public  SincUp(Context paramctx,String caminhoteste,Config c,String carga,String nomcarga) {
        ctx=paramctx;
        daoDB= new DaoCreateDB(ctx);
        confirma = (Activity)ctx;
        this.dialog = new ProgressDialog(paramctx);
        db = daoDB.getWritableDatabase();
        config= c;
        dca = new DaoCarga(ctx,carga);
        if(config==null){
            new DataXmlExporter(this.db, FolderConfig.getExternalStorageDirectory()).importData(ctx, "config", "config", "");
            config= new DaoConfig(ctx).consultar();
        }
        a= new Alertas(paramctx);
        dc = new DaoConfig(ctx);
        erro="1";
        this.carga=carga;
        caminteste=caminhoteste;
        this.nomcarga=nomcarga;

    }

    @Override
    protected void onPreExecute() {
        this.dialog.setCancelable(true);
        this.dialog.setCanceledOnTouchOutside(false);
        this.dialog.setMessage("Carga");
        this.dialog.setMessage("Processando Carga...");
        dialog.setProgressStyle(dialog.STYLE_HORIZONTAL);
        dialog.setProgress(0);
        dialog.setMax(2);

        dialog.setButton(ProgressDialog.BUTTON_NEUTRAL,
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //button click stuff here
                        try {
                            cancel(true);
                            ((Activity) ctx).finish();

                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                });

        this.dialog.show();
    }

    @Override
    protected Boolean doInBackground(String[] paramArrayOfString)
    {
        try
        {
            smb = new SincDadosSmb(config,3,"");

            try {
                int status=dca.getstatusCarga(nomcarga);
                erro=smb.sendFileFromPeerToSdcard("env_"+carga,status);
                if(smb.checkFileenv(carga).equalsIgnoreCase("")&&erro.equalsIgnoreCase("1")){
                    SincDadosLocal sdc = new SincDadosLocal(config, 1);
                  if(status==1) {
                      erro = sdc.deleteFile(carga);
                      ctx.deleteDatabase("env_"+carga);
                      ctx.deleteDatabase(carga);
                  }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finally
        {
            dialog.setProgress(2);
            if(erro.equals("1")){
                return Boolean.valueOf(false);
            }else{
                return Boolean.valueOf(true);
            }

        }

    }


    @Override
    protected void onCancelled() {
        running = false;
    }


    @Override
    protected void onPostExecute(Boolean paramBoolean)
    {
        dialog.getButton(ProgressDialog.BUTTON_NEUTRAL).setText("OK");
        dialog.getButton(ProgressDialog.BUTTON_NEUTRAL).setVisibility(View.VISIBLE);
        delegate.processFinish(erro);
        if(paramBoolean.booleanValue())
        {
            this.dialog.setMessage(erro);
            return;
        }else{
            this.dialog.setMessage("Enviado com Sucesso");
            return;

        }

    }


}
