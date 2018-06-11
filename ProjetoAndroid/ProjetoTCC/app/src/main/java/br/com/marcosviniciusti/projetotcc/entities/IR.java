package br.com.marcosviniciusti.projetotcc.entities;

import java.io.Serializable;

public class IR implements Serializable{

    private String id;
    private String codHex;
    private int[] pulse;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodHex() {
        return codHex;
    }

    public void setCodHex(String codHex) {
        this.codHex = codHex;
    }

    public int[] getPulse() {
        return pulse;
    }

    public void setPulse(int[] pulse) {
        this.pulse = pulse;
    }
}