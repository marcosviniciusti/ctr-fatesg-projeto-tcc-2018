package br.com.senai.ctr.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;

public class Emissor implements IEntidade {

    private String id;
    private String nome;

    @Exclude
    @Override
    public String getId() {
        return id;
    }

    @Override
    public Emissor setId(String id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Emissor setNome(String nome) {
        this.nome = nome;
        return this;
    }
}
