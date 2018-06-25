package br.com.marcosviniciusti.projetotcc.entidade;

import java.io.Serializable;
import java.util.List;

public class EEmissor implements Serializable {

    private String id;
    private String nome;
    private List<EEquipamento> equipamentos;
    private String idEquipamento;

    public EEmissor() {
    }

    public EEmissor(String id, String nome, List<EEquipamento> equipamentos, String idEquipamento) {
        this.id = id;
        this.nome = nome;
        this.equipamentos = equipamentos;
        this.idEquipamento = idEquipamento;
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

    public String getIdEquipamento() {
        return idEquipamento;
    }

    public void setIdEquipamento(String idEquipamento) {
        this.idEquipamento = idEquipamento;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
