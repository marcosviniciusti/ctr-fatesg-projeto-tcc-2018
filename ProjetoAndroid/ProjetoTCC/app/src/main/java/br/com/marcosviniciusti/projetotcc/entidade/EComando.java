package br.com.marcosviniciusti.projetotcc.entidade;

import java.io.Serializable;

public class EComando implements Serializable {

    private String id;
    private String nome;
    private int[] rawData;
    private String idModelo;

    public EComando() {
    }

    public EComando(String id, String nome, int[] rawData) {
        this.id = id;
        this.nome = nome;
        this.rawData = rawData;
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

    public int[] getRawData() {
        return rawData;
    }

    public void setRawData(int[] rawData) {
        this.rawData = rawData;
    }

    public String getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(String idModelo) {
        this.idModelo = idModelo;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
