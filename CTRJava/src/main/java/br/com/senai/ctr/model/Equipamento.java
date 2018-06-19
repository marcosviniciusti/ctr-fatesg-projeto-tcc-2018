package br.com.senai.ctr.model;

import com.google.firebase.database.Exclude;

public class Equipamento implements IEntidade {

    private String id;
    private String nome;
    private Modelo modelo;
    private Emissor emissor;

    @Exclude
    @Override
    public String getId() {
        return id;
    }

    @Override
    public Equipamento setId(String id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Equipamento setNome(String nome) {
        this.nome = nome;
        return this;
    }

    @Exclude
    public Modelo getModelo() {
        return modelo;
    }

    public Equipamento setModelo(Modelo modelo) {
        this.modelo = modelo;
        return this;
    }

    @Exclude
    public Emissor getEmissor() {
        return emissor;
    }

    public Equipamento setEmissor(Emissor emissor) {
        this.emissor = emissor;
        return this;
    }

    /* ESTRUTURA PARA FIREBASE */
    public String getModeloEquipamento() {
        return modelo.getMarca().getId() + "/" + modelo.getId();
    }

    public Equipamento setModeloEquipamento(String modeloEquipamento) {
        String[] dados = modeloEquipamento.split("/");
        if (dados.length == 2) {
            modelo = new Modelo()
                    .setId(dados[1])
                    .setMarca(new Marca().setId(dados[0]));
        }
        return this;
    }

    public String getEmissorEquipamento() {
        return emissor.getId();
    }
}
