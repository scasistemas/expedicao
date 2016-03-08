package br.com.mobiwork.expedicao.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by LuisGustavo on 08/09/2015.
 */
public final class Conexao {
    private static String LogSync;
    private static String LogToUserTitle;
    private static String Log;
    public static boolean Conectado(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(cm.getActiveNetworkInfo()!=null&&cm.getActiveNetworkInfo().isAvailable()&&cm.getActiveNetworkInfo().isAvailable()){
                return true;
            }else{
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }
}
