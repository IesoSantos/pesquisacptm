package com.metro.pesquisacptm.controller;

import android.content.Context;

import com.metro.pesquisacptm.model.Estacao;
import com.metro.pesquisacptm.model.Linha;
import com.metro.pesquisacptm.persistence.DatabaseHelper;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LinhaController {
    private ArrayList<Linha> linhas;
    private EstacaoController estacaoController;
    //private DatabaseHelper databaseHelper;

    private Context context;
    public LinhaController(Context context) throws Exception{
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        this.linhas = databaseHelper.getLinhas();
        this.context = context;
    }

    public ArrayList<Linha> getLinhas(){
        return this.linhas;
    }

    public Linha getLinhaPorNome(String nomeLinha){
        return linhas.stream().filter(
                linha -> linha.getNome().equals(nomeLinha)
        ).findFirst().orElse(null);
    }

    public Linha getLinhaPorID(int id){
        return linhas.stream().filter(
                linha -> linha.getId()==id
        ).findFirst().orElse(null);
    }

    public ArrayList<Linha> getLinhaPorGestora(String gestora){
        return linhas.stream().filter(
                linha -> linha.getGestora().equals(gestora)
        ).collect(Collectors.toCollection(ArrayList<Linha>::new)
        );
    }

    public Linha getLinhaPorSigla(String sigla){
        return linhas.stream().filter(
                linha -> linha.getSigla().equals(sigla)
        ).findFirst().orElse(null);
    }

    public void criarLinha(Linha linha){
        //databaseHelper.setLinha(linha);
    }

    public void deletarLinha(Linha linha){
        //databaseHelper.deletarLinha(linha);
    }

    public void atualizarLinha(Linha linha){
        //databaseHelper.atualizarLinha(linha);
    }

    public Estacao getEstacaoPorId(int id) throws Exception{
        if(estacaoController==null){
            estacaoController = new EstacaoController(context);
        }
        return estacaoController.getEstacaoPorId(id);
    }
}