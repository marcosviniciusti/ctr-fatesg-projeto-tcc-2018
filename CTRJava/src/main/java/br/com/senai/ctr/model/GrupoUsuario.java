package br.com.senai.ctr.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;

public class GrupoUsuario implements IEntidade {

    private String id;
    private String nomeGrupo;
    private List<Usuario> usuarios;
    private List<GrupoEquipamento> gruposEquipamento;

    @Exclude
    @Override
    public String getId() {
        return id;
    }

    @Override
    public GrupoUsuario setId(String id) {
        this.id = id;
        return this;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public GrupoUsuario setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
        return this;
    }

    @Exclude
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public GrupoUsuario setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
        return this;
    }

    @Exclude
    public List<GrupoEquipamento> getGruposEquipamento() {
        return gruposEquipamento;
    }

    public GrupoUsuario setGruposEquipamento(List<GrupoEquipamento> gruposEquipamento) {
        this.gruposEquipamento = gruposEquipamento;
        return this;
    }

    /* ESTRUTURA PARA FIREBASE */
    public HashMap<String, Boolean> getUsuariosList() {
        return gerarHashMapParaFirebase(usuarios);
    }
    public HashMap<String, Boolean> getGruposEquipamentos() {
        return gerarHashMapParaFirebase(gruposEquipamento);
    }
}
