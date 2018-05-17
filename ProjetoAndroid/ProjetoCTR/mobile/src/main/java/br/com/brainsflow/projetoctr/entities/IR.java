package br.com.brainsflow.projetoctr.entities;

import java.io.Serializable;

public class IR implements Serializable{

    private String codHex;
    private int[] pulse;

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