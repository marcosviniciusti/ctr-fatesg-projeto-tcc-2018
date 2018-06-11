package br.com.marcosviniciusti.projetotcc.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by marcos on 02/03/18.
 */

public class EquipmentGroup implements Serializable {

    private String id;
    private String name;
    private List<Equipment> listEquipment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Equipment> getListEquipment() {
        return listEquipment;
    }

    public void setListEquipment(List<Equipment> listEquipment) {
        this.listEquipment = listEquipment;
    }

    @Override
    public String toString() {
        return name;
    }
}