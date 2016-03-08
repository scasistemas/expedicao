package br.com.mobiwork.expedicao.util;

/**
 * Created by LuisGustavo on 21/05/2015.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import br.com.mobiwork.expedicao.dao.DaoPedido;
import br.com.mobiwork.expedicao.dao.DaoPedido_Item;
import br.com.mobiwork.expedicao.dao.daoPedido_Item_Env;
import br.com.mobiwork.expedicao.modelo.Config;
import br.com.mobiwork.expedicao.modelo.Pedido_Item;

public final class PedidoRollBack {
    private static ArrayList<Pedido_Item> listaItensAnt, listaItensAtual;
    private static String pedidoId;
    private static Config config;
    private static String base;


    public static void  criarPedidoRollBack(String pedido,String carga){
        pedidoId = pedido;
        listaItensAnt = new ArrayList<Pedido_Item>();
        base=carga;

    }

    public static void rollBackpedido(Context ctx){

        daoPedido_Item_Env dpie= new daoPedido_Item_Env(ctx,base,base);
        Cursor c = dpie.listaritens(pedidoId);
        if (c.moveToFirst()) {
            do {
                Pedido_Item item = new Pedido_Item();
                item.setPedido_Item(c);
                DaoPedido_Item dao = new DaoPedido_Item(ctx,base);
                boolean found = false;
                for (Pedido_Item itemAnt : listaItensAnt){
                    if (itemAnt.getRomproduto().equals(item.getRomproduto())) {
                        new daoPedido_Item_Env(ctx,base,base).alterarQuantidade(itemAnt.getRomproduto(), itemAnt.getRomqtdhand(),pedidoId);
                        listaItensAnt.remove(itemAnt);
                        found = true;
                        break;
                    }
                }

            } while (c.moveToNext());
        }
    }
    public static void setItemPedido(Cursor c) {
        if (c.moveToFirst()) {
            do {
                Pedido_Item item = new Pedido_Item();
                item.setPedido_Item(c);
                listaItensAnt.add(item);
            } while (c.moveToNext());
        }
    }

}


