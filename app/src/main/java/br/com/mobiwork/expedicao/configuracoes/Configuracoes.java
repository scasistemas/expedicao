package br.com.mobiwork.expedicao.configuracoes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.mobiwork.expedicao.R;
import br.com.mobiwork.expedicao.dao.DaoConfig;
import br.com.mobiwork.expedicao.dao.DaoCreateDB;
import br.com.mobiwork.expedicao.modelo.Config;
import br.com.mobiwork.expedicao.sinc.LimpImg;
import br.com.mobiwork.expedicao.sinc.SincDown;
import br.com.mobiwork.expedicao.sinc.SincUp;
import br.com.mobiwork.expedicao.util.Alertas;
import br.com.mobiwork.expedicao.util.DataXmlExporter;
import br.com.mobiwork.expedicao.util.FolderConfig;
import br.com.mobiwork.expedicao.util.SincDadosLocal;

/**
 * Created by LuisGustavo on 15/05/2015.
 */
public class Configuracoes extends Activity implements AsyncResponse,AsyncResponse2 {

    private EditText editExp,editUsui,editAnd,editEmp,result,versao,edtxlimp;
    private Config config;
    private Alertas a;
    private SincDown sinc;
    private SincUp sincup;
    private LimpImg limpg;
    private DaoConfig dc;
    private ProgressDialog dialog;
    private List<HashMap<String, String>> fillMaps;

    EditText userInput,login,senha;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.configuracoes);
        iniEdit();
        inibloq();
        int j= android.os.Build.VERSION.SDK_INT;
        if(android.os.Build.VERSION.SDK_INT>10)
        {
            this.setFinishOnTouchOutside(false);
        }
        DaoCreateDB dcb = new DaoCreateDB(this);
        SQLiteDatabase dbP=dcb.getWritableDatabase();
        a=new Alertas(this);
        dc= new DaoConfig(this);
        config= new Config();
        config= new DaoConfig(this).consultar();
        if(config.getLogin()==null||config.getLogin().equalsIgnoreCase("")){
            new DataXmlExporter(dbP, FolderConfig.getExternalStorageDirectory()).importData(this, "config", "config", "");
            config= new DaoConfig(this).consultar();
        }

        preencherCampos();
    }


    public void preencherCampos(){

        editUsui.setText(String.valueOf(config.getUsuid()));
        editExp.setText(config.getEndereco());
        editAnd.setText(FolderConfig.getExternalStorageDirectory());
        edtxlimp.setText(config.getImgTemp());
        editEmp.setText("BomDestino" + " - 1.13");

    }

    public void habserv(View v){
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(Configuracoes.this);
        View promptsView = li.inflate(R.layout.alertinput, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Configuracoes.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        login = (EditText) promptsView
                .findViewById(R.id.txtlogin);
        senha = (EditText) promptsView
                .findViewById(R.id.txtsenha);
        userInput.setText(config.getEndereco());
        login.setText(config.getLogin());
        senha.setText(config.getSenha());
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                try {
                                    config.setLogin(login.getText().toString());
                                    config.setSenha(senha.getText().toString());
                                    ArrayList<String> checkedValue= new ArrayList<String>();
                                    checkedValue.add("teste");
                                    sinc = new SincDown(Configuracoes.this,checkedValue,"",userInput.getText().toString() );
                                    sinc.delegate = Configuracoes.this;
                                    sinc.execute(new String[0]);
                                } catch (Exception e) {
                                    a.AlertaSinc("Caminho invalido");
                                }
                            }

                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void inibloq(){
        editExp.setEnabled(false);
        editUsui.setEnabled(false);
        editAnd.setEnabled(false);
        editEmp.setEnabled(false);

    }
    public void limpar(View v){

        limpg=new LimpImg(Configuracoes.this,edtxlimp.getText().toString());
        limpg.delegate = Configuracoes.this;
        limpg.execute(new String[0]);



    }




    public void iniEdit(){
        editExp=(EditText) findViewById(R.id.editExp);
        editUsui=(EditText) findViewById(R.id.editUsu);
        editAnd=(EditText) findViewById(R.id.editAnd);
        editEmp=(EditText) findViewById(R.id.editEmp);
        edtxlimp=(EditText) findViewById(R.id.edtxlimp);

        result = (EditText) findViewById(R.id.editTextDialogUserInput);


    }


    public void voltar(View v){
        Configuracoes.this.finish();
    }



    @Override
    public void processFinish(String output) {

      //  if(output.equalsIgnoreCase("")){
        dc.update( userInput.getText().toString(),login.getText().toString(),senha.getText().toString());
        config = new DaoConfig(Configuracoes.this).consultar();
       // }
        config = new DaoConfig(Configuracoes.this).consultar();
        preencherCampos();


    }

    @Override
    public void processFinish(String output,String caminho) {
        dc.updateImgTemp(caminho);
    }
}
