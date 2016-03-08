package br.com.mobiwork.expedicao.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by LuisGustavo on 11/05/2015.
 */
public class FolderConfig {




    public static String getExternalStorageDirectory(){
        String dirMercadorName = Environment.getExternalStorageDirectory().toString() + "//expedicao" ;
        File file = new File(dirMercadorName);
        if(!file.isDirectory()){
            file.mkdir();
            if(!file.isDirectory()){
                dirMercadorName = "/mnt/sdcard" + "//expedicao" ;  // Juares
                file = new File(dirMercadorName);
                if(!file.isDirectory()){
                    file.mkdir();
                    if(!file.isDirectory()){
                        dirMercadorName = "/sdcard" + "//expedicao" ;  // Juares
                        file = new File(dirMercadorName);
                        if(!file.isDirectory()){
                            file.mkdir();
                        }
                    }
                }
            }
        }
        return dirMercadorName;

    }
    public static String getExternalStorageDirectoryVs(){
        String dirMercadorName = Environment.getExternalStorageDirectory().toString() + "//expedicao/vs" ;
        File file = new File(dirMercadorName);
        if(!file.isDirectory()){
            file.mkdir();
            if(!file.isDirectory()){
                dirMercadorName = "/mnt/sdcard" + "//expedicao/vs" ;  // Juares
                file = new File(dirMercadorName);
                if(!file.isDirectory()){
                    file.mkdir();
                    if(!file.isDirectory()){
                        dirMercadorName = "/sdcard" + "//expedicao/vs" ;  // Juares
                        file = new File(dirMercadorName);
                        if(!file.isDirectory()){
                            file.mkdir();
                        }
                    }
                }
            }
        }
        return dirMercadorName;

    }
    public static String getExternalStorageDirectorycargaAberta(){
        String dirMercadorName = Environment.getExternalStorageDirectory().toString() + "//expedicao/cargaAb" ;
        File file = new File(dirMercadorName);
        if(!file.isDirectory()){
            file.mkdir();
            if(!file.isDirectory()){
                dirMercadorName = "/mnt/sdcard" + "//expedicao/cargaAb" ;  // Juares
                file = new File(dirMercadorName);
                if(!file.isDirectory()){
                    file.mkdir();
                    if(!file.isDirectory()){
                        dirMercadorName = "/sdcard" + "//expedicao/cargaAb" ;  // Juares
                        file = new File(dirMercadorName);
                        if(!file.isDirectory()){
                            file.mkdir();
                        }
                    }
                }
            }
        }
        return dirMercadorName;

    }
    public static String getExternalStorageDirectorycargaFechada(){
        String dirMercadorName = Environment.getExternalStorageDirectory().toString() + "//expedicao/cargaFe" ;
        File file = new File(dirMercadorName);
        if(!file.isDirectory()){
            file.mkdir();
            if(!file.isDirectory()){
                dirMercadorName = "/mnt/sdcard" + "//expedicao/cargaFe" ;  // Juares
                file = new File(dirMercadorName);
                if(!file.isDirectory()){
                    file.mkdir();
                    if(!file.isDirectory()){
                        dirMercadorName = "/sdcard" + "//expedicao/cargaFe" ;  // Juares
                        file = new File(dirMercadorName);
                        if(!file.isDirectory()){
                            file.mkdir();
                        }
                    }
                }
            }
        }
        return dirMercadorName;

    }
}
