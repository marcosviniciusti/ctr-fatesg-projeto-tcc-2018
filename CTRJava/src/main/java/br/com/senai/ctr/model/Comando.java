package br.com.senai.ctr.model;

import com.google.firebase.database.Exclude;

import java.util.List;

public class Comando implements IEntidade {

    private String id;
    private String nome;
    private List<Integer> rawData;

    @Exclude
    @Override
    public String getId() {
        return id;
    }

    @Override
    public Comando setId(String id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Comando setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public List<Integer> getRawData() {
        return rawData;
    }

    public Comando setRawData(List<Integer> rawData) {
        this.rawData = rawData;
        return this;
    }
}
