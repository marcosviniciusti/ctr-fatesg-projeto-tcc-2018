package br.com.marcosviniciusti.projetotcc.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by marcos on 02/03/18.
 */

public class Definicao implements Serializable {

    private String nome;
    private List<Equipamento> equipamentos;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Equipamento> getEquipamentos() {
        return equipamentos;
    }

    public void setEquipamentos(List<Equipamento> equipamentos) {
        this.equipamentos = equipamentos;
    }
}