package com.metro.pesquisacptm.persistence;
import android.content.Context;

import com.metro.pesquisacptm.model.Estacao;
import com.metro.pesquisacptm.model.Linha;
import com.metro.pesquisacptm.model.Pesquisa;

import java.util.ArrayList;

public class Utilidades {
    /*
    private static Linha[] linhas = {
    new Linha(1,"Linha 1 - Azul", "L1", "Metrô SP"),
    new Linha(2,"Linha 2 - Verde", "L2", "Metrô SP"),
    new Linha(3,"Linha 3 - Vermelha", "L3", "Metrô SP"),
    new Linha(4,"Linha 4 - Amarela", "L4", "ViaQuatro"),
    new Linha(5,"Linha 5 - Lilás", "L5", "ViaMobilidade"),
    new Linha(6,"Linha 7 - Rubi", "L7", "CPTM"),
    new Linha(7,"Linha 8 - Diamante", "L8", "ViaMobilidade"),
    new Linha(8,"Linha 9 - Esmeralda", "L9", "ViaMobilidade"),
    new Linha(9,"Linha 10 - Turquesa", "L10", "CPTM"),
    new Linha(10,"Linha 11 - Coral", "L11", "CPTM"),
    new Linha(11,"Linha 12 - Safira", "L12", "CPTM"),
    new Linha(12,"Linha 13 - Jade", "L13", "CPTM"),
    new Linha(13,"Linha 15 - Prata", "L15", "Metrô SP")
    };
     */

    private final ArrayList<Linha>linhas;
    private static DatabaseHelper databaseHelper;
    private static Utilidades instance;
    //private UtilidadeLinhaAzulSingleton linhaAzul;


    //comentado para utilizar o context
    /*
    private UtilidadesLinhas(){
        linhaAzul = UtilidadeLinhaAzulSingleton.getInstance();
    }
     */

    private Utilidades(Context context){
        //linhaAzul = UtilidadeLinhaAzulSingleton.getInstance();
        databaseHelper = new DatabaseHelper(context);
        linhas = databaseHelper.getLinhas();
    }

/*
comentado para usar context

    public static synchronized UtilidadesLinhas getInstance(int quantidadeTotalLinhas){
        if(instance==null){
            instance = new UtilidadesLinhas(quantidadeTotalLinhas);
        }
        return instance;
    }

    public static Linha[] getLinhas(){
        return linhas;
    }

 */

    public static synchronized Utilidades getInstance(Context context){
        if(instance==null){
            instance = new Utilidades(context);
        }
        return instance;
    }

    public ArrayList<Linha> getLinhas(){
        return linhas;
    }

    public Linha getLinhaPorId(int id_linha){
        for(Linha linha:linhas){
            if(linha.getId()==id_linha)
                return linha;
        }
        return null;
    }

    public Estacao getEstacaoPorSigla(String sigla){
        for(Linha linha:linhas){
            for(Estacao estacao:linha.getEstacoes()){
                if(estacao.getSigla().equals(sigla)){
                    return estacao;
                }
            }
        }
        return null;
    }

    public  Estacao getEstacaoPorSiglaLinhaNomeEstacao(String siglaLinha,String nomeEstacao){
        for(Linha linha:linhas){
            if(linha.getSigla().equals(siglaLinha)){
                for(Estacao estacao:linha.getEstacoes()){
                    if(estacao.getNome().equals(nomeEstacao)){
                        return estacao;
                    }
                }
            }
        }
        return null;
    }

    public Estacao getEstacaoPorId(int id){
        for(Linha linha:linhas){
            for(Estacao estacao:linha.getEstacoes()){
                if(estacao.getId()==id){
                    return estacao;
                }
            }
        }
        return null;
    }


    public void salvarPesquisa(Pesquisa pesquisa){
        databaseHelper.salvarPesquisa(pesquisa);
    }

    public ArrayList<Pesquisa> getPesquisas(){
        return databaseHelper.getPesquisas();
    }
}
