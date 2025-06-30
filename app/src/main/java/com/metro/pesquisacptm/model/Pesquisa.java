package com.metro.pesquisacptm.model;

import java.io.Serializable;

public class Pesquisa implements Serializable {
    private int id;
    private String pesquisador, tipoPesquisa, dataPesquisa, horaPesquisa, areaPesquisa, origem, destino;
    private Linha linhaPesquisa;
    private Estacao estacaoPesquisa;

    public String getPesquisador() {
        return pesquisador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPesquisador(String pesquisador) {
        this.pesquisador = pesquisador;
    }

    public String getDataPesquisa() {
        return dataPesquisa;
    }

    public void setDataPesquisa(String dataPesquisa) {
        this.dataPesquisa = dataPesquisa;
    }

    public String getHoraPesquisa() {
        return horaPesquisa;
    }

    public void setHoraPesquisa(String horaPesquisa) {
        this.horaPesquisa = horaPesquisa;
    }

    public String getAreaPesquisa() {
        return areaPesquisa;
    }

    public void setAreaPesquisa(String areaPesquisa) {
        this.areaPesquisa = areaPesquisa;
    }

    public String getTipoPesquisa() {
        return tipoPesquisa;
    }

    public void setTipoPesquisa(String tipoPesquisa) {
        this.tipoPesquisa = tipoPesquisa;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Linha getLinhaPesquisa() {
        return linhaPesquisa;
    }

    public void setLinhaPesquisa(Linha linhaPesquisa) {
        this.linhaPesquisa = linhaPesquisa;
    }

    public Estacao getEstacaoPesquisa() {
        return estacaoPesquisa;
    }

    public void setEstacaoPesquisa(Estacao estacaoPesquisa) {
        this.estacaoPesquisa = estacaoPesquisa;
    }

    @Override
    public String toString() {
        return "Pesquisa{" +
                ", Id='" + id + '\'' +
                "pesquisador='" + pesquisador + '\'' +
                ", tipoPesquisa='" + tipoPesquisa + '\'' +
                ", dataPesquisa='" + dataPesquisa + '\'' +
                ", horaPesquisa='" + horaPesquisa + '\'' +
                ", linhaPesquisa=" + linhaPesquisa + '\'' +
                ", estacaoPesquisa=" + estacaoPesquisa + '\'' +
                ", areaPesquisa='" + areaPesquisa + '\'' +
                ", origem='" + origem + '\'' +
                ", destino='" + destino +
                '}';
    }
}
