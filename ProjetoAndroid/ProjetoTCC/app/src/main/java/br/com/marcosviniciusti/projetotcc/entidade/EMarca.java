package br.com.marcosviniciusti.projetotcc.entidade;

import java.io.Serializable;
import java.util.List;

public class EMarca implements Serializable {

    private String id;
    private String nome;

    public EMarca() {
    }

    public EMarca(String id, String nome) {
        this.id = id;
        this.nome = nome;
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

    @Override
    public String toString() {
        return this.nome;
    }
}
