package br.com.marcosviniciusti.projetotcc.entidade;

import java.io.Serializable;
import java.util.List;

import br.com.marcosviniciusti.projetotcc.util.EnumTipoUsuario;

public class EUsuario implements Serializable {

    private String id;
    private String nome;
    private String email;
    private EnumTipoUsuario tipoUsuario;
    private List<EGrupoEquipamento> grupoEquipamentos;

    public EUsuario() {
    }

    public EUsuario(String id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EnumTipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(EnumTipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public List<EGrupoEquipamento> getGrupoEquipamentos() {
        return grupoEquipamentos;
    }

    public void setGrupoEquipamentos(List<EGrupoEquipamento> grupoEquipamentos) {
        this.grupoEquipamentos = grupoEquipamentos;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
