package com.metro.pesquisacptm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Estacao implements Serializable {
    private int id;
    private String nome, sigla;
    private boolean isTransferencia;
    private ArrayList<String> areas;

    public Estacao() {
        isTransferencia = false;
        areas = new ArrayList<>();
    }

    public Estacao(int id, String nome, String sigla,
                   boolean isTransferencia, ArrayList<String> areas) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        this.isTransferencia = isTransferencia;
        this.areas = areas;
    }

    public Estacao(int id, String nome, String sigla,
                   boolean isTransferencia) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        this.isTransferencia = isTransferencia;
        this.areas = new ArrayList<>();
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

    public boolean isTransferencia() {
        return isTransferencia;
    }

    public void setTransferencia(boolean transferencia) {
        isTransferencia = transferencia;
    }

    public ArrayList<String> getAreas() {
        return areas;
    }

    public void setAreas(ArrayList<String> areas) {
        this.areas = areas;
    }

    public void setArea(String area) {
        this.areas.add(area);
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estacao)) return false;
        Estacao estacao = (Estacao) o;
        return id == estacao.id && Objects.equals(nome, estacao.nome) && Objects.equals(sigla, estacao.sigla);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, sigla);
    }
}

