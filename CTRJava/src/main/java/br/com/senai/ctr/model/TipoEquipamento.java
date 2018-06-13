package br.com.senai.ctr.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;

public class TipoEquipamento implements IEntidade {

    private String id;
    private String nome;
    private List<ModeloEquipamento> modelos;

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

    @Exclude
    public List<ModeloEquipamento> getModelos() {
        return modelos;
    }

    public TipoEquipamento setModelos(List<ModeloEquipamento> modelos) {
        this.modelos = modelos;
        return this;
    }

    /* ESTRUTURA PARA FIREBASE */
    public HashMap<String, Boolean> getModelosList() {
        return gerarHashMapParaFirebase(modelos);
    }

}
