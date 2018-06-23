package br.com.senai.ctr.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;

public class Marca implements IEntidade {

    private String id;
    private String nome;
    private List<Modelo> modelos;

    @Exclude
    @Override
    public String getId() {
        return id;
    }

    @Override
    public Marca setId(String id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Marca setNome(String nome) {
        this.nome = nome;
        return this;
    }

    @Exclude
    public List<Modelo> getModelos() {
        return modelos;
    }

    public Marca setModelos(List<Modelo> modelos) {
        this.modelos = modelos;
        return this;
    }
}
