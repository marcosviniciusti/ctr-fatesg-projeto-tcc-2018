package br.com.marcosviniciusti.projetotcc.entidade;

import java.io.Serializable;
import java.util.List;

public class EGrupoEquipamento implements Serializable {

    private String id;
    private String nome;
    private List<EEquipamento> equipamentos;
    private String idUsuarioDono;
    private List<String> listaUsuarios;

    public EGrupoEquipamento() {
    }

    public EGrupoEquipamento(String id, String nome, List<EEquipamento> equipamentos) {
        this.id = id;
        this.nome = nome;
        this.equipamentos = equipamentos;
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

    public List<EEquipamento> getEquipamentos() {
        return equipamentos;
    }

    public void setEquipamentos(List<EEquipamento> equipamentos) {
        this.equipamentos = equipamentos;
    }

    public String getIdUsuarioDono() {
        return idUsuarioDono;
    }

    public void setIdUsuarioDono(String idUsuarioDono) {
        this.idUsuarioDono = idUsuarioDono;
    }

    public List<String> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<String> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
