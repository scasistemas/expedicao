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

import br.com.mobiwork.expedicao.configuracoes.AsyncResponse;
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
public class SincDown   extends AsyncTask<String, Void, Boolean> {

    private SQLiteDatabase dbP;
    private ProgressDialog dialog;
    private Handler handler = new Handler();
    private String erro,utilcaminhoteste;
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
    public AsyncResponse delegate=null;
    ArrayList<String> listcargas;
    String caminhover;





    public  SincDown(Context paramctx,ArrayList<String> checkedValue,String caminho,String caminhover) {
        this.caminhover=caminhover;
        ctx=paramctx;
        daoDB= new DaoCreateDB(ctx);
        af= (Activity) ctx;
        utilcaminhoteste=caminho;
        if(!caminhover.equalsIgnoreCase("")&&caminhover!=null){
            this.caminhover=caminhover;
        }
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
        listcargas=checkedValue;
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
        this.dialog.setMax(listcargas.size());
        dialog.setButton(ProgressDialog.BUTTON_NEUTRAL,
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //button click stuff here

                        dialog.dismiss();
                        try {
                            //         this.finalize();
                            SincDown.this.cancel(true);
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
        int paramcaminho=0;
        if(utilcaminhoteste.equalsIgnoreCase("exportados")){
            paramcaminho=2;
        }else {
            paramcaminho=1;
         }
        String cargasex="";

        this.erro="";
        String result="";
        try {

                dialog.setProgress(0);
                try{


                    if(utilcaminhoteste.equalsIgnoreCase("exportados")){
                        smb = new SincDadosSmb(config,paramcaminho,caminhover);
                    }else{
                        smb = new SincDadosSmb(config,paramcaminho,caminhover);
                    }

                    for(int i=0;i<listcargas.size();i++) {
                        if (!isCancelled()) {
                            //smb.smbfile();
                                if(sdl.verifCargaExp(listcargas.get(i).toString())){
                                    String cargaw=listcargas.get(i).toString();
                                    SincDadosLocal sdc = new SincDadosLocal(config,1);
                                    sdc.deleteFile(cargaw);
                                    ctx.deleteDatabase(cargaw);

                                }
                                if(sdl.verifCargaExpfE(listcargas.get(i).toString())){
                                    if(cargasex.equalsIgnoreCase("")) {
                                        cargasex = "Ja existem cargas Fechadas " + listcargas.get(i).toString();
                                    }else{
                                        cargasex=cargasex+", "+listcargas.get(i).toString();
                                    }
                                }else {

                                    result = smb.downloadFilePc(listcargas.get(i).toString(), "", paramcaminho);
                                    if (this.caminhover.equalsIgnoreCase("")) {
                                        result = smb.transPastaServ(listcargas.get(i).toString());
                                    }
                                    //    result=smb.downloadFilePc(String.valueOf(config.getVendid()) , ".zip",0);
                                    if (!result.equalsIgnoreCase("")) {
                                        erro = result;
                                    }
                                }


                            dialog.setProgress(i+1);
                        }
                    }
                }catch (Exception e){
                    erro="Erro desconhecido!";
                }

            handler.post(new Runnable() {
                public void run() {
                }
            });
        } finally {
            dialog.setProgress(listcargas.size());
            if(erro.equals("")){
                if(!cargasex.equalsIgnoreCase("")){
                    erro=cargasex;
                    return Boolean.valueOf(true);
                }else {
                    return Boolean.valueOf(false);
                }
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
        delegate.processFinish(erro);
        if(paramBoolean.booleanValue())
        {
            this.dialog.setMessage(erro);

            return  ;
        }else{
            this.dialog.setMessage("Download Efetuado com Sucesso");

            return ;

        }

    }



}
