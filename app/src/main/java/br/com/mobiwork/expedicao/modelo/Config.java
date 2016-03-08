package br.com.mobiwork.expedicao.modelo;

import android.database.Cursor;
import android.os.Environment;

import br.com.mobiwork.expedicao.util.FolderConfig;

/**
 * Created by LuisGustavo on 14/05/2015.
 */
public class Config {

    private int _id;
    private String nome;
    private int usuid;
    private String endereco;
    private String login;
    private String senha;
    private String emp;
    private String imgTemp;

    public Config(){
        this._id=0;
        this.nome="";
        this.usuid=0;
        this.endereco="";
        this.login="";
        this.senha="";
        this.emp="";
        this.imgTemp= Environment.getExternalStorageDirectory().toString()+"//BarcodeScanner/History";

    }

    public void setConfig(Cursor c){
        this._id=c.getInt(c.getColumnIndex("_id"));
        this.nome = c.getString(c.getColumnIndex("nome"));
        this.usuid = c.getInt(c.getColumnIndex("usuid"));
        this.endereco = c.getString(c.getColumnIndex("endereco"));
        this.login = c.getString(c.getColumnIndex("login"));
        this.senha = c.getString(c.getColumnIndex("senha"));
        this.emp = c.getString(c.getColumnIndex("emp"));
        this.imgTemp = c.getString(c.getColumnIndex("imgTemp"));
    }

    public String getImgTemp() {
        return imgTemp;
    }

    public void setImgTemp(String imgTemp) {
        this.imgTemp = imgTemp;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getUsuid() {
        return usuid;
    }

    public void setUsuid(int usuid) {
        this.usuid = usuid;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmp() {
        return emp;
    }

    public void setEmp(String emp) {
        this.emp = emp;
    }
}
