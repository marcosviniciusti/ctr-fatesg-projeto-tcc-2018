package br.com.senai.ctr.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;

public class Usuario implements IEntidade {

    private String id;
    private String nome;
    private String email;
    private EnumTipoUsuario tipo;
    private List<Grupo> grupos;

    @Exclude
    @Override
    public String getId() {
        return id;
    }

    @Override
    public Usuario setId(String id) {
        this.id = id;
        return this;
    }


    public String getNome() {
        return nome;
    }

    public Usuario setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Usuario setEmail(String email) {
        this.email = email;
        return this;
    }

    public EnumTipoUsuario getTipo() {
        return tipo;
    }

    public Usuario setTipo(EnumTipoUsuario tipo) {
        this.tipo = tipo;
        return this;
    }

    @Exclude
    public List<Grupo> getGrupos() {
        return grupos;
    }

    public Usuario setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
        return this;
    }

    /* ESTRUTURA PARA FIREBASE */
    public HashMap<String, Boolean> getGruposList() {
        return gerarHashMapParaFirebase(grupos);
    }
}
