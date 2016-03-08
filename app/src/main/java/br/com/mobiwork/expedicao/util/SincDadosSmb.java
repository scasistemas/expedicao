package br.com.mobiwork.expedicao.util;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
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
import jcifs.smb.SmbFileOutputStream;
import jcifs.smb.SmbFilenameFilter;

/**
 * Created by LuisGustavo on 14/05/2015.
 */
public class SincDadosSmb {

    public Config config;
    public static String user = "";
    public static String pass ="";
    public static String sharedFolder="";
    public static String pathdownup="";
    public static String arquivoenvi;
    public static String arquivoreceb;
    NtlmPasswordAuthentication auth ;
    SmbFileInputStream mFStream=null;
    SmbFile sc=null;
    SmbFile mFile=null;
    DateFormat formatterd,hora;
    String caminhover;

    public SincDadosSmb(Config c,int paramcaminho,String caminhover){
        this.config=c;
        arquivoenvi="";
        arquivoreceb="";
        user=c.getLogin();
        pass=c.getSenha();
        auth = new NtlmPasswordAuthentication("",user,pass);
        formatterd = new SimpleDateFormat("dd/MM/yyyy");
        hora = new SimpleDateFormat("hh:mm:ss a");
        if(!caminhover.equalsIgnoreCase("")){
            if(paramcaminho==2) {
                pathdownup = caminhover + "/exportados";
            }else if(paramcaminho==3) {
                pathdownup = caminhover + "/processados";

            }else{
                pathdownup=caminhover;
            }
        }else{
            //caminho 2 igual pasta exportados
            if(paramcaminho==2){
                pathdownup=c.getEndereco()+"/exportados";
            }else if(paramcaminho==3){
                pathdownup=c.getEndereco()+"/processados";
            }else if(paramcaminho==4){
                pathdownup=c.getEndereco()+"/vs";
            }else{
                pathdownup=c.getEndereco();
            }
        }



    }

    public String transPastaServ(String nome) throws IOException {


        String erro="";
        boolean ok=false;
        InputStream is = null;
        SmbFileOutputStream mFileOutputStream = null;
        SmbFile mLocalFile;
        try {
            String urltemp=config.getEndereco()+"/exportados"+"/"+nome;
            //  String urltemp=pathdownup+"/orcamento";
            SmbFile mFile = new SmbFile(urltemp,auth);
            mFStream = new SmbFileInputStream(mFile);
            mLocalFile = new SmbFile(config.getEndereco()+"/exportados/processados/"+nome,
                      auth);
            mFileOutputStream = new SmbFileOutputStream(
                    mLocalFile);
            is = mFile.getInputStream();
            int bufferSize = 5096;
            byte[] b = new byte[bufferSize];
            int noOfBytes = 0;
            while( (noOfBytes = is.read(b)) != -1 )
            {
                mFileOutputStream.write(b, 0, noOfBytes);
            }
            mFileOutputStream.close();
            is.close();
            mFStream.close();
            ok=true;
            mFile.delete();
        } catch (MalformedURLException e) {
            erro=e.getMessage();
            mFileOutputStream.close();
            is.close();
            mFStream.close();

        } catch (SmbException e) {
            erro=erro(e.getMessage(),nome,"");
            mFStream.close();

        } catch (Exception e) {
            erro=e.getMessage();
        }finally {
            if(ok){
                mFStream.close();
            }
            return erro;

        }
    }

    public String deletefile(String nome ) throws IOException {
        String erro="1";
        boolean ok=false;
        InputStream is = null;
        OutputStream mFileOutputStream = null;

        File mLocalFile;
        try {
            String urltemp=config.getEndereco()+"/exportados"+"/"+nome;
            //  String urltemp=pathdownup+"/orcamento";
            SmbFile mFile = new SmbFile(urltemp,auth);
            mFStream = new SmbFileInputStream(mFile);
            mFile.delete();
            mFStream.close();
            ok=true;
        } catch (MalformedURLException e) {
            erro=e.getMessage();
            mFStream.close();

        } catch (SmbException e) {
            erro=erro(e.getMessage(),nome,"");;
            mFStream.close();

        } catch (Exception e) {
            erro=e.getMessage();
        }finally {
            if(ok){
                mFStream.close();
            }
            return erro;

        }
    }

    public String checkFileenv(String nome ) throws IOException {
        String erro="";
        boolean ok=false;
        InputStream is = null;
        OutputStream mFileOutputStream = null;

        File mLocalFile;
        try {
            String urltemp=pathdownup+"/"+nome;
            //  String urltemp=pathdownup+"/orcamento";
            SmbFile mFile = new SmbFile(urltemp,auth);
            mFStream = new SmbFileInputStream(mFile);
            mFStream.close();
            ok=true;
        } catch (MalformedURLException e) {
            erro=e.getMessage();
            mFStream.close();

        } catch (SmbException e) {
            erro=erro(e.getMessage(),nome,"");;
            mFStream.close();

        } catch (Exception e) {
            erro=e.getMessage();
        }finally {
            if(ok){
                mFStream.close();
            }
            return erro;

        }
    }



    public String downloadFilePc(String nome,String ext,int caminho ) throws IOException {
        String erro="";
        boolean ok=false;
        InputStream is = null;
        OutputStream mFileOutputStream = null;

        File mLocalFile;
        try {
            String urltemp=pathdownup+"/"+nome+ext;
            //  String urltemp=pathdownup+"/orcamento";
            SmbFile mFile = new SmbFile(urltemp,auth);

            mFStream = new SmbFileInputStream(mFile);
            if(caminho==2) {
                mLocalFile = new File(FolderConfig.getExternalStorageDirectorycargaAberta(),
                        mFile.getName());
            }else if(caminho==3) {
                mLocalFile = new File(FolderConfig.getExternalStorageDirectoryVs(),
                        mFile.getName());
            }else{
                mLocalFile = new File(FolderConfig.getExternalStorageDirectory(),
                        mFile.getName());
            }
            mFileOutputStream = new FileOutputStream(
                    mLocalFile);
            is = mFile.getInputStream();
            try {
                mFile.listFiles();
            } catch (SmbException e) {
                e.printStackTrace();
            }

            int bufferSize = 5096;
            byte[] b = new byte[bufferSize];
            int noOfBytes = 0;
            while( (noOfBytes = is.read(b)) != -1 )
            {
                mFileOutputStream.write(b, 0, noOfBytes);
            }

            mFileOutputStream.close();
            mFStream.close();
            is.close();
            ok=true;
        } catch (MalformedURLException e) {
            erro=e.getMessage();
            mFileOutputStream.close();
            is.close();
            mFStream.close();

        } catch (SmbException e) {
            erro=erro(e.getMessage(),nome,ext);
            mFileOutputStream.close();
            is.close();
            mFStream.close();

        } catch (Exception e) {
            erro=e.getMessage();
        }finally {
            if(ok){
                mFileOutputStream.close();
                is.close();
                mFStream.close();
            }
            return erro;

        }
    }



    public static String[] listAvailableHosts(boolean withIp){
        List<String> hostNames=new ArrayList<String>();
        String erro;
        try {
            SmbFile[] workgroups=new SmbFile("smb://").listFiles();
            for (int i=0; i < workgroups.length; i++) {
                try {
                    SmbFile[] hosts=workgroups[i].listFiles();
                    for (int j=0; j < hosts.length; j++) {
                        String name=hosts[j].getName();
                        String nameWithoutSlash=name.substring(0,name.length() - 1);
                        hostNames.add(nameWithoutSlash);
                        if (withIp ) {
                            try {
                                hostNames.add(InetAddress.getByName(nameWithoutSlash).getHostAddress());
                            }
                            catch (            UnknownHostException e) {
                            }
                        }
                    }
                }
                catch (      SmbException e) {
                }
            }
        }
        catch (  SmbException e) {
          erro=e.getMessage();
            String r="";
        }
        catch (  MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String[] hosts=hostNames.toArray(new String[0]);
        return hosts;
    }

    public List<HashMap<String, String>> smbfile( )  {

        SmbFile[] domains ;
        List<HashMap<String, String>>  fillMaps = new ArrayList<HashMap<String, String>>();
        fillMaps.clear();


        try {
            sc = (new SmbFile(pathdownup+"/", auth));
            if(!sc.isDirectory()){
                sc.mkdir();
            }
            domains=sc.listFiles(new SmbFilenameFilter() {
                @Override
                public boolean accept(SmbFile smbFile, String s) throws SmbException {
                    return s.endsWith(".db");
                }
            });


            for (int i = 0; i < domains.length; i++) {
                System.out.println(domains[i]);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("_id", "" +domains[i].getName());
                map.put("data", String.valueOf(formatterd.format(new Date(domains[i].getDate()))));
                map.put("hora", String.valueOf(hora.format(new Date(domains[i].getDate()))));
                fillMaps.add(map);
           /*   SmbFile[] servers = domains[i].listFiles();
                for (int j = 0; j < servers.length; j++) {
                    System.out.println("\t" + servers[j]);
                }*/
            }
        } catch (SmbException e) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("_id", "ErroDadosSinc123" );
            map.put("data", e.getMessage());
            map.put("hora", "");
            fillMaps.add(map);
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
       /* if(fillMaps.size()==0){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("_id", "EMPTY" );
            map.put("data", "Pasta vazia");
            map.put("hora", "");
            fillMaps.add(map);
        }*/
        return fillMaps;
    }

    public List<HashMap<String, String>> smbfileatu( )  {

        SmbFile[] domains ;
        List<HashMap<String, String>>  fillMaps = new ArrayList<HashMap<String, String>>();
        fillMaps.clear();


        try {
            sc = (new SmbFile(pathdownup+"/", auth));
            if(!sc.isDirectory()){
                sc.mkdir();
            }
            domains=sc.listFiles(new SmbFilenameFilter() {
                @Override
                public boolean accept(SmbFile smbFile, String s) throws SmbException {
                    return s.endsWith(".apk");
                }
            });


            for (int i = 0; i < domains.length; i++) {
                System.out.println(domains[i]);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("_id", "" +domains[i].getName());
                map.put("data", String.valueOf(formatterd.format(new Date(domains[i].getDate()))));
                map.put("hora", String.valueOf(hora.format(new Date(domains[i].getDate()))));
                fillMaps.add(map);
           /*   SmbFile[] servers = domains[i].listFiles();
                for (int j = 0; j < servers.length; j++) {
                    System.out.println("\t" + servers[j]);
                }*/
            }
        } catch (SmbException e) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("_id", "ErroDadosSinc123" );
            map.put("data", e.getMessage());
            map.put("hora", "");
            fillMaps.add(map);
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
       /* if(fillMaps.size()==0){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("_id", "EMPTY" );
            map.put("data", "Pasta vazia");
            map.put("hora", "");
            fillMaps.add(map);
        }*/
        return fillMaps;
    }






    public String sendFileFromPeerToSdcard(String file,int status) throws IOException {
        String erro="1";
        boolean ok=false;
        String pathserv=pathdownup+"/"+file;
        SmbFile smbFile = null;
        BufferedOutputStream out=null;
        InputStream in = null;
        FileInputStream f=null;
        f = null;
        try{
            try {
                smbFile = new SmbFile(pathserv,auth);
                if(status==1) {
                    f = new FileInputStream(FolderConfig.getExternalStorageDirectorycargaFechada() + "/" + file);
                }else{
                    f = new FileInputStream(FolderConfig.getExternalStorageDirectorycargaAberta() + "/" + file);

                }
                in  = new BufferedInputStream(f);
                out = new BufferedOutputStream(smbFile.getOutputStream());

                byte[] buffer = new byte[10240];
                int len = 0;
                while((len = in.read(buffer))>0){
                    out.write(buffer,0,len);
                }
                ok=true;
            } catch (MalformedURLException e) {
                erro=e.getMessage();
                e.printStackTrace();


            } catch (FileNotFoundException e) {
                erro=e.getMessage();
                e.printStackTrace();
                erro=erro(e.getMessage(),"","");
            }

            catch (IOException e) {
                erro=erro(e.getMessage(),"","");
            }

        }catch (Exception e){
            erro=e.getMessage();

        }finally {
            if(ok){
                out.close();
                in.close();
                f.close();
            }
            return  erro;
        }
    }
    public String erro(String mensagem,String nome,String ext){
        String e="";
        if(mensagem.equalsIgnoreCase("The system cannot find the path specified.")){
            e="Não foi possivel encontrar a pasta no servidor !";
        }else if(mensagem.equalsIgnoreCase("The system cannot find the file specified.")){
            if(nome.equalsIgnoreCase("teste")){
                e = "Atualizado com sucesso !";
            }else {
                e = "Não foi possivel encontrar o arquivo no servidor !";
            }
        }else if(mensagem.equalsIgnoreCase("Logon failure: unknown user name or bad password.")){
            e="Usuario ou senha incorretos";
        }else{
            e=mensagem;
        }
        return e;
    }
}
