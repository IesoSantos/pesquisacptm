package com.metro.pesquisacptm.controller;

import android.content.Context;

import com.metro.pesquisacptm.model.Estacao;
import com.metro.pesquisacptm.model.Linha;
import com.metro.pesquisacptm.persistence.DatabaseHelper;

import java.util.ArrayList;

public class EstacaoController {
    private DatabaseHelper databaseHelper;
    private ArrayList<Linha> linhas;
    private ArrayList<Estacao> estacoes;

    public EstacaoController(Context context) throws Exception{
        this.databaseHelper = new DatabaseHelper(context);
        LinhaController linhaController = new LinhaController(context);
        linhas = linhaController.getLinhas();
    }

    public Estacao getEstacaoPorId(int id){
        for (Linha linha:linhas) {
            for(Estacao estacao:linha.getEstacoes()) {
                if(estacao.getId()==id){
                    return estacao;
                }
            }
        }
        return null;
    }

    public Estacao getEstacaoPorSigla(String sigla){
        for (Linha linha:linhas) {
            for(Estacao estacao:linha.getEstacoes()) {
                if(estacao.getSigla().equals(sigla)){
                    return estacao;
                }
            }
        }
        return null;
    }

    public Estacao getEstacoesPorLinhEstacaoNome(Linha linhaParametro, String nome){
        Linha linha = this.linhas.stream().filter(
                linhaVariavel -> linhaVariavel.equals(linhaParametro)
        ).findFirst().orElse(null);
        assert linha != null;
        return linha.getEstacoes().stream().filter(
                estacao -> estacao.getNome().equals(nome)
        ).findFirst().orElse(null);
    }

    public ArrayList<Estacao> getEstacoesPorLinha(Linha linhaParametro){
        Linha linha = this.linhas.stream().filter(
                linhaVariavel -> linhaVariavel.equals(linhaParametro)
        ).findFirst().orElse(null);
        assert linha != null;
        return linha.getEstacoes();
    }

    public void criarEstacao(Estacao estacao){
        //this.databaseHelper.criarEstacao(estacao);
    }

    public void deletarEstacao(Estacao estacao){
        //this.databaseHelper.deletarEstacao(estacao);
    }

    public void atualizarEstacao(Estacao estacao){
        //this.databaseHelper.atualizarEstacao(estacao);
    }

    public ArrayList<Estacao> getEstacoes(){
        ArrayList<Estacao> estacoes = new ArrayList<>();
        for (Linha linha:linhas
        ) {
            estacoes.addAll(linha.getEstacoes());
        }
        return estacoes;
    }
}