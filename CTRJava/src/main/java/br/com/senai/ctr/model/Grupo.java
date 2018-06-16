package br.com.senai.ctr.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;

public class Grupo implements IEntidade {

    private String id;
    private String nome;
    private List<Usuario> usuarios;
    private List<Equipamento> equipamentos;

    @Exclude
    @Override
    public String getId() {
        return id;
    }

    @Override
    public Grupo setId(String id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Grupo setNome(String nome) {
        this.nome = nome;
        return this;
    }

    @Exclude
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public Grupo setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
        return this;
    }

    @Exclude
    public List<Equipamento> getEquipamentos() {
        return equipamentos;
    }

    public Grupo setEquipamentos(List<Equipamento> equipamentos) {
        this.equipamentos = equipamentos;
        return this;
    }

    /* ESTRUTURA PARA FIREBASE */
    public HashMap<String, Boolean> getUsuariosList() {
        return gerarHashMapParaFirebase(usuarios);
    }

    public HashMap<String, Boolean> getEquipamentosList() {
        return gerarHashMapParaFirebase(equipamentos);
    }
}
