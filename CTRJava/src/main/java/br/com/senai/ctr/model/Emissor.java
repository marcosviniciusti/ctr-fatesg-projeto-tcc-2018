package br.com.senai.ctr.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;

public class Emissor implements IEntidade {

    private String id;
    private String nome;
    private List<Equipamento> equipamentos;

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

    @Exclude
    public List<Equipamento> getEquipamentos() {
        return equipamentos;
    }

    public Emissor setEquipamentos(List<Equipamento> equipamentos) {
        this.equipamentos = equipamentos;
        return this;
    }

    /* ESTRUTURA PARA FIREBASE */
    public HashMap<String, Boolean> getEquipamentosList() {
        return gerarHashMapParaFirebase(equipamentos);
    }
}
