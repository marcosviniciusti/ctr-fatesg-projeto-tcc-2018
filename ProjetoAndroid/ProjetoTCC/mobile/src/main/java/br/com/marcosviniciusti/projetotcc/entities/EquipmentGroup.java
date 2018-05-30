package br.com.marcosviniciusti.projetotcc.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by marcos on 02/03/18.
 */

public class EquipmentGroup implements Serializable {

    private String nome;
    private List<Equipment> equipment;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }
}