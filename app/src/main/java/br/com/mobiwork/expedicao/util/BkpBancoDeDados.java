package br.com.mobiwork.expedicao.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.com.mobiwork.expedicao.R;

/**
 * Created by LuisGustavo on 19/05/2015.
 */
public class BkpBancoDeDados extends Activity implements View.OnClickListener {



    public static Context mainContext;
    private ProgressDialog dialog;
    private Handler handler = new Handler();
    private String tBkp;
    private String carga,nomcarga,env_carga;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bkpdb);
        this.tBkp = getIntent().getStringExtra("tBkp");
        this.carga = getIntent().getStringExtra("carga");
        this.nomcarga = getIntent().getStringExtra("nomcarga");
        this.env_carga = getIntent().getStringExtra("env_carga");

        Button button1Click = (Button) findViewById(R.id.btBackup);
        button1Click.setOnClickListener(this);
        button1Click = (Button) findViewById(R.id.btvoltar);
        button1Click.setOnClickListener(this);
        this.mainContext = this;

        if (this.tBkp.endsWith("Restaure"))
        {
          //  startActivityForResult(new Intent(this,Restaure.class), 2);
            button1Click.setText("Restaurar");
            // startActivityForResult(new Intent(this, ConfirmaRestaure.class), 2);
            return;
        }
        if(this.tBkp.endsWith("Backuprapido")){
            exportToSd();
            BkpBancoDeDados.this.finish();
        }
        if(this.tBkp.endsWith("ecarga")){
            exportToSd(this.carga);
            BkpBancoDeDados.this.finish();
        }



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btBackup:
                exportToSd();
                break;
            case R.id.btvoltar:
                BkpBancoDeDados.this.finish();
                break;
        }
    }


    public void exportToSd(String carga)
    {
        if ((!"br.com.mobiwork.expedicao".equals("")) && (!carga.equals("")))
        {
            if (isExternalStorageAvailable()) {
                try{
                    new ExportDatabaseFileTask(this, "br.com.mobiwork.expedicao", env_carga,"carga",nomcarga).execute(new String[0]);
                    new ExportDatabaseFileTask(this, "br.com.mobiwork.expedicao", carga,"carga",nomcarga).execute(new String[0]);
                    showShortToast("Exportado com sucesso!");
                }catch(Exception e){
                    showShortToast("Erro ao efetuar Backup");

                }
            }
        }
        else {
            showShortToast("Pasta de exportacao nao esta disponivel.");
            return;
        }

    }


    public void exportToSd()
    {
        if ((!"br.com.mobiwork.expedicao".equals("")) && (!"123456.db".equals("")))
        {
            if (isExternalStorageAvailable()) {
                try{
                    new ExportDatabaseFileTask(this, "br.com.mobiwork.expedicao", "123456.db","",env_carga).execute(new String[0]);
                    showShortToast("Exportado com sucesso!");
                }catch(Exception e){
                    showShortToast("Erro ao efetuar Backup");

                }
            }
        }
        else {
            showShortToast("Pasta de exportacao nao esta disponivel.");
            return;
        }

    }

    private boolean importToSd()
    {
        Boolean result=false;
        if ((!"br.com.mobiwork.expedicao".equals("")) && (!"123456".equals("")))
        {
            if (isExternalStorageAvailable()) {
                try{
                    new ImportDatabaseFileTask(this, "br.com.mobiwork.expedicao", "123456").execute(new String[0]);
                    result=true;
                }catch (Exception e){
                }
            }
        }
        else {
            showShortToast("Pasta de exportacao nao esta disponivel.");

        }
        return result;
    }

    private void importToSdBkpEnvio()
    {
        if ((!"br.com.mobiwork.mercador".equals("")) && (!"MercadoDBEnv".equals("")))
        {
            if (isExternalStorageAvailable()) {
                new ImportDatabaseFileTask(this, "br.com.mobiwork.mercador", "MercadoDBEnv").execute(new String[0]);
            }
        }
        else {
            return;
        }
        showShortToast("External storage is not available, unable to export data.");
    }

    public static void showShortToast(String paramString)
    {
        Toast.makeText(BkpBancoDeDados.mainContext, paramString, 1).show();
    }

    public boolean isExternalStorageAvailable()
    {
        return Environment.getExternalStorageState().equals("mounted");
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
    {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        Bundle params = paramIntent.getExtras();
        if (paramInt2 == -1)
        {
            if (params.getString("senhaRestaure").equals("123456"))
            {
                if(importToSd()){
                    new AlertDialog.Builder(this).setTitle("Restaurado com sucesso").setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                        {
                            paramAnonymousDialogInterface.cancel();
                            BkpBancoDeDados.this.finish();
                        }
                    }).show();
                }else {
                    new AlertDialog.Builder(this).setTitle("Nao foi possivel restaurar").setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                        {
                            paramAnonymousDialogInterface.cancel();
                            BkpBancoDeDados.this.finish();
                        }
                    }).show();
                    return;
                }
            }
        }


    }
}
