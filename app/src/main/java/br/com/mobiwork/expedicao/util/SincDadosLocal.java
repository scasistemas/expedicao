package br.com.mobiwork.expedicao.util;

import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.mobiwork.expedicao.modelo.Config;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFilenameFilter;

/**
 * Created by LuisGustavo on 19/05/2015.
 */
public class SincDadosLocal {


    public Config config;
    public int ca;
    DateFormat formatterd,hora;

    public SincDadosLocal(Config c,int paramcaminho){
        this.config=c;
        ca = paramcaminho;
        formatterd = new SimpleDateFormat("dd/MM/yyyy");
        hora = new SimpleDateFormat("hh:mm:ss a");
    }

    public List<HashMap<String, String>> sdcfile( )  {
        File sc=null;
        File[] domains ;
        List<HashMap<String, String>>  fillMaps = new ArrayList<HashMap<String, String>>();
        fillMaps.clear();
        try {
            if(ca==1) {
                sc = (new File(FolderConfig.getExternalStorageDirectorycargaAberta() + "/"));
            }else{
                sc = (new File(FolderConfig.getExternalStorageDirectorycargaFechada() + "/"));
            }
            domains=sc.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File sc, String s) {
                    return !s.startsWith("env_");
                }
            });
            if(!sc.isDirectory()){
                sc.mkdir();
            }

            for (int i = 0; i < domains.length; i++) {
                System.out.println(domains[i]);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("_id", "" +domains[i].getName());
                map.put("data", String.valueOf(formatterd.format(new Date(domains[i].lastModified()))));
                map.put("hora", String.valueOf(hora.format(new Date(domains[i].lastModified()))));
                fillMaps.add(map);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fillMaps;
    }

    public List<HashMap<String, String>> sdcfile( String caminho)  {
        File sc=null;
        File[] domains ;
        List<HashMap<String, String>>  fillMaps = new ArrayList<HashMap<String, String>>();
        fillMaps.clear();
        try {
            sc = (new File(caminho + "/"));
            if(!sc.isDirectory()){
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("_id", "PASTA NAO ENCONTRADA");
                fillMaps.add(map);
            }else {
                domains = sc.listFiles();


                for (int i = 0; i < domains.length; i++) {
                    System.out.println(domains[i]);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("_id", "" + domains[i].getName());
                    map.put("data", String.valueOf(formatterd.format(new Date(domains[i].lastModified()))));
                    map.put("hora", String.valueOf(hora.format(new Date(domains[i].lastModified()))));
                    fillMaps.add(map);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fillMaps;
    }

    public String deleteFileTemp( )  {
        File sc=new File(config.getImgTemp());
        File[] domains ;
        String idFile;
        try {
            if(!sc.isDirectory()){
                sc.mkdir();
            }
            domains=sc.listFiles();
            for (int i = 0; i < domains.length; i++) {
                idFile=domains[i].getName();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public boolean checkFile(String carga)  {
        File sc=null;
        File[] domains ;
        List<HashMap<String, String>>  fillMaps = new ArrayList<HashMap<String, String>>();
        fillMaps.clear();
        try {
            sc = (new File(FolderConfig.getExternalStorageDirectorycargaFechada() + "/"));
            domains=sc.listFiles();
            for (int i = 0; i < domains.length; i++) {
              if(carga.equalsIgnoreCase(domains[i].getName())){
                  return true;
              }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String deleteFile(String arquivo,String caminho)  {
        File sc=null;
        try {
            sc = (new File(caminho + "/"+arquivo));
            sc.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1";
    }

    public String deleteFile(String arquivo)  {
        File sc=null;
        try {
            if(ca==1) {
                sc = (new File(FolderConfig.getExternalStorageDirectorycargaAberta() + "/"+arquivo));
            }else{
                sc = (new File(FolderConfig.getExternalStorageDirectorycargaFechada() + "/"+arquivo));
            }

         sc.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1";
    }


    public String deleteFileFechada(String arquivo)  {
        File sc=null;
        try {
            sc = (new File(FolderConfig.getExternalStorageDirectorycargaFechada() + "/"+arquivo));
            if(sc.exists()){
                sc.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1";
    }

    public Boolean verifCargaExpfE(String arquivo)  {
        File sc=null;
        try {
            sc = (new File(FolderConfig.getExternalStorageDirectorycargaFechada() + "/"+arquivo));
            if(sc.exists()){
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean verifCargaExp(String arquivo)  {
        File sc=null;
        try {
              sc = (new File(FolderConfig.getExternalStorageDirectorycargaAberta() + "/"+arquivo));
            if(sc.exists()){
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean verifCarga(String arquivo)  {
        File sc=null;
        try {
            sc = (new File(FolderConfig.getExternalStorageDirectorycargaFechada() + "/"+arquivo));
            if(sc.exists()){
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
