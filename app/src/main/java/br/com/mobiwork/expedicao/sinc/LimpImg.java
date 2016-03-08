package br.com.mobiwork.expedicao.sinc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.mobiwork.expedicao.configuracoes.AsyncResponse;
import br.com.mobiwork.expedicao.configuracoes.AsyncResponse2;
import br.com.mobiwork.expedicao.dao.DaoConfig;
import br.com.mobiwork.expedicao.dao.DaoCreateDB;
import br.com.mobiwork.expedicao.modelo.Config;
import br.com.mobiwork.expedicao.util.Alertas;
import br.com.mobiwork.expedicao.util.Arquivo;
import br.com.mobiwork.expedicao.util.DataXmlExporter;
import br.com.mobiwork.expedicao.util.FolderConfig;
import br.com.mobiwork.expedicao.util.SincDadosLocal;
import br.com.mobiwork.expedicao.util.SincDadosSmb;
import br.com.mobiwork.expedicao.util.ToastManager;

/**
 * Created by LuisGustavo on 18/05/2015.
 */
public class LimpImg   extends AsyncTask<String, Void, Boolean> {

    private SQLiteDatabase dbP;
    private ProgressDialog dialog;
    private Handler handler = new Handler();
    private String erro;
    private DaoCreateDB daoDB;
    private DaoConfig dc;
    Intent i ;
    ToastManager tm;
    SincDadosSmb smb;
    SincDadosLocal sdl;
    Context ctx;
    Alertas a;;
    Activity confirma = (Activity)ctx;
    Config config;
    Activity af;
    public AsyncResponse2 delegate=null;
    private List<HashMap<String, String>> fillMaps;
    String caminho;




    public  LimpImg(Context paramctx,String caminho) {
        this.caminho=caminho;
        ctx=paramctx;
        daoDB= new DaoCreateDB(ctx);
        af= (Activity) ctx;
        confirma = (Activity)ctx;
        this.dialog = new ProgressDialog(paramctx);
        dbP=daoDB.getWritableDatabase();
        config= new DaoConfig(ctx).consultar();
        if(config.getEmp()==null){
            new DataXmlExporter(this.dbP, FolderConfig.getExternalStorageDirectory()).importData(ctx, "config", "config", "");
            config= new DaoConfig(ctx).consultar();
        }
        a= new Alertas(paramctx);
        dc = new DaoConfig(ctx);
        sdl=new SincDadosLocal(config,1);



        erro="";

    }

    @Override
    protected void onPreExecute() {
        this.dialog.setCancelable(true);
        this.dialog.setCanceledOnTouchOutside(false);
        this.dialog.setMessage("Cargas");
        this.dialog.setMessage("Baixando Cargas...");
        this.dialog.setProgressStyle(dialog.STYLE_HORIZONTAL);
        this.dialog.setProgress(0);


        dialog.setButton(ProgressDialog.BUTTON_NEUTRAL,
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //button click stuff here

                        dialog.dismiss();
                        try {
                            //         this.finalize();
                            LimpImg.this.cancel(true);
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


        this.erro="";

        try {

            dialog.setProgress(0);

            try{
                dialog.setMessage("Aguarde.....");
                fillMaps = sdl.sdcfile(caminho);
                this.dialog.setMax(fillMaps.size());
            }catch (Exception e){
                erro=e.getMessage();
            }

            try {
                dialog.setMessage("Deletando Imagens");
                if(fillMaps.size()>0) {
                    for (int i = 0; i < fillMaps.size(); i++) {
                        if(fillMaps.get(i).get("_id").equalsIgnoreCase("PASTA NAO ENCONTRADA")){
                            erro="PASTA NAO ENCONTRADA !!!";
                        }else {
                            sdl.deleteFile(fillMaps.get(i).get("_id"), caminho);
                        }
                        dialog.setProgress(i + 1);
                    }
                }else{
                    erro="Pasta vazia !!!";
                }

            }catch (Exception e){
                erro=e.getMessage();
            }

            handler.post(new Runnable() {
                public void run() {
                }
            });
        } finally {
            dialog.setProgress(fillMaps.size());
            if(erro.equals("")){

                    return Boolean.valueOf(false);

            }else{
                return Boolean.valueOf(true);
            }

        }

    }
    @Override
    protected void onPostExecute(Boolean paramBoolean)
    {
        dialog.getButton(ProgressDialog.BUTTON_NEUTRAL).setText("OK");
        dialog.getButton(ProgressDialog.BUTTON_NEUTRAL).setVisibility(View.VISIBLE);
        delegate.processFinish(erro, caminho);
        if(paramBoolean.booleanValue())
        {
            this.dialog.setMessage(erro);
            return  ;
        }else{
            this.dialog.setMessage("Imagens limpadas com sucesso");

            return ;

        }

    }



}
