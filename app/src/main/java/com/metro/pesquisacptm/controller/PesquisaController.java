package com.metro.pesquisacptm.controller;

import android.content.Context;

import com.metro.pesquisacptm.model.Linha;
import com.metro.pesquisacptm.model.Pesquisa;
import com.metro.pesquisacptm.persistence.DatabaseHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PesquisaController {

    private DatabaseHelper databaseHelper;
    private ArrayList<Pesquisa> pesquisas;

    public PesquisaController(Context context){
        this.databaseHelper = new DatabaseHelper(context);
        this.pesquisas = this.databaseHelper.getPesquisas();
    }

    public void salvarPesquisa(Pesquisa pesquisa) throws Exception{
        this.databaseHelper.salvarPesquisa(pesquisa);
    }

    public void deletarPesquisa(Pesquisa pesquisa) throws Exception{
        //this.databaseHelper.deletarPesquisa(pesquisa);
    }

    public void atualizarPesquisa(Pesquisa pesquisa) throws Exception{
        //this.databaseHelper.atualizarPesquisa(pesquisa);
    }

    public ArrayList<Pesquisa> getPesquisas() {
        return this.pesquisas;
    }

    public Pesquisa getPesquisaPorId(int id){
        return pesquisas.stream().filter(
                pesquisa -> pesquisa.getId()==id
        ).findFirst().orElse(null);
    }

    public ArrayList<Pesquisa> getPesquisaPorPesquisador(String pesquisador){
        return pesquisas.stream().filter(
                pesquisa -> pesquisa.getPesquisador().equals(pesquisador)
        ).collect(Collectors.toCollection(ArrayList<Pesquisa>::new));
    }

    public ArrayList<Pesquisa> getPesquisaPorLinhaDaPesquisa(Linha linha){
        return pesquisas.stream().filter(
                pesquisa -> pesquisa.getLinhaPesquisa().equals(linha)
        ).collect(Collectors.toCollection(ArrayList<Pesquisa>::new));
    }

    public ArrayList<Pesquisa> getPesquisaPorOrigem(String origem){
        return pesquisas.stream().filter(
                pesquisa -> pesquisa.getOrigem().equals(origem)
        ).collect(Collectors.toCollection(ArrayList<Pesquisa>::new));
    }

    public ArrayList<Pesquisa> getPesquisaPorDestino(String destino){
        return pesquisas.stream().filter(
                pesquisa -> pesquisa.getDestino().equals(destino)
        ).collect(Collectors.toCollection(ArrayList<Pesquisa>::new));
    }

    public ArrayList<Pesquisa> getPesquisaPorTipoDePesquisa(String tipoDePesquisa){
        return pesquisas.stream().filter(
                pesquisa -> pesquisa.getTipoPesquisa().equals(tipoDePesquisa)
        ).collect(Collectors.toCollection(ArrayList<Pesquisa>::new));
    }

    public ArrayList<Pesquisa> getPesquisaPorData(String data){
        return pesquisas.stream().filter(
                pesquisa -> pesquisa.getDataPesquisa().equals(data)
        ).collect(Collectors.toCollection(ArrayList<Pesquisa>::new));
    }

    public ArrayList<Pesquisa> getPesquisaDataInicial(String dataInicio){
        ArrayList<Pesquisa> retorno = new ArrayList<>();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataInicial = LocalDate.parse(dataInicio, formatador);
        for (Pesquisa pesquisa:pesquisas) {
            LocalDate dataConvertida = LocalDate.parse(pesquisa.getDataPesquisa(), formatador);
            if( dataConvertida.isEqual(dataInicial) || dataConvertida.isAfter(dataInicial)){
                retorno.add(pesquisa);
            }
        }
        return retorno;
    }

    public ArrayList<Pesquisa> getPesquisaPorDataFinal(String dataFim){
        ArrayList<Pesquisa> retorno = new ArrayList<>();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataFinal = LocalDate.parse(dataFim, formatador);
        for (Pesquisa pesquisa:pesquisas) {
            LocalDate dataConvertida = LocalDate.parse(pesquisa.getDataPesquisa(), formatador);
            if(dataConvertida.isEqual(dataFinal) || dataConvertida.isBefore(dataFinal)){
                retorno.add(pesquisa);
            }
        }
        return retorno;
    }

    public ArrayList<Pesquisa> getPesquisaEntreDatas(String dataInicio, String dataFim){
        ArrayList<Pesquisa> retorno = new ArrayList<>();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataInicial = LocalDate.parse(dataInicio, formatador);
        LocalDate dataFinal = LocalDate.parse(dataFim, formatador);
        for (Pesquisa pesquisa:pesquisas) {
            LocalDate dataConvertida = LocalDate.parse(pesquisa.getDataPesquisa(), formatador);
            if(dataConvertida.isAfter(dataInicial) && dataConvertida.isBefore(dataFinal)){
                retorno.add(pesquisa);
            }
        }
        return retorno;
    }
}

