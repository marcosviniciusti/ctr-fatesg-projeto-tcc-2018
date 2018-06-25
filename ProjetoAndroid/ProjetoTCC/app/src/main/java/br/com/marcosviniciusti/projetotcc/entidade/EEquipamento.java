package br.com.marcosviniciusti.projetotcc.entidade;

import java.io.Serializable;

public class EEquipamento implements Serializable {

    private String id;
    private String nome;
    private String modelo;
    private String emissor;
    private String idGrupoEquipamento;

    public EEquipamento() {
    }

    public EEquipamento(String id, String nome, String modelo, EEmissor emissor) {
        this.id = id;
        this.nome = nome;
        this.modelo = modelo;
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getEmissor() {
        return emissor;
    }

    public void setEmissor(String emissor) {
        this.emissor = emissor;
    }

    public String getIdGrupoEquipamento() {
        return idGrupoEquipamento;
    }

    public void setIdGrupoEquipamento(String idGrupoEquipamento) {
        this.idGrupoEquipamento = idGrupoEquipamento;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
