package br.com.senai.ctr.model;

import com.google.firebase.database.Exclude;

public class TipoEquipamento implements IEntidade {

    private String id;
    private String nome;

    @Exclude
    @Override
    public String getId() {
        return id;
    }

    @Override
    public TipoEquipamento setId(String id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public TipoEquipamento setNome(String nome) {
        this.nome = nome;
        return this;
    }

}
