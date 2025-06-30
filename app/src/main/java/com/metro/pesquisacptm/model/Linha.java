package com.metro.pesquisacptm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Linha implements Serializable {
    private int id;
    private String nome,sigla, gestora;
    private ArrayList<Estacao> estacoes;
    private Estacao estacao;

    public Linha() {
        estacoes = new ArrayList<>();
    }

    public Linha(int id, String nome, String sigla, String gestora) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        this.gestora = gestora;
        this.estacoes = new ArrayList<>();
    }

    public Linha(int id, String nome, String sigla, String gestora, ArrayList<Estacao> estacoes) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        this.gestora = gestora;
        this.estacoes = estacoes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getGestora() {
        return gestora;
    }

    public void setGestora(String gestora) {
        this.gestora = gestora;
    }

    public ArrayList<Estacao> getEstacoes() {
        return estacoes;
    }

    public Estacao getEstacaoPorIndice(int indice){
        return this.estacoes.get(indice);
    }

    public void setEstacoes(ArrayList<Estacao> estacoes) {
        this.estacoes = estacoes;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Linha)) return false;
        Linha linha = (Linha) o;
        return id == linha.id &&
                Objects.equals(nome, linha.nome) &&
                Objects.equals(sigla, linha.sigla) &&
                Objects.equals(gestora, linha.gestora);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, sigla, gestora);
    }
}

