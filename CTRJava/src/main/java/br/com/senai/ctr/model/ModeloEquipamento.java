package br.com.senai.ctr.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;

public class ModeloEquipamento implements IEntidade {

    private String id;
    private String nome;
    private Marca marca;
    private TipoEquipamento tipo;
    private List<Comando> comandos;

    @Exclude
    @Override
    public String getId() {
        return id;
    }

    @Override
    public ModeloEquipamento setId(String id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public ModeloEquipamento setNome(String nome) {
        this.nome = nome;
        return this;
    }

    @Exclude
    public Marca getMarca() {
        return marca;
    }

    public ModeloEquipamento setMarca(Marca marca) {
        this.marca = marca;
        return this;
    }

    @Exclude
    public TipoEquipamento getTipo() {
        return tipo;
    }

    public ModeloEquipamento setTipo(TipoEquipamento tipo) {
        this.tipo = tipo;
        return this;
    }

    @Exclude
    public List<Comando> getComandos() {
        return comandos;
    }

    public ModeloEquipamento setComandos(List<Comando> comandos) {
        this.comandos = comandos;
        return this;
    }

    /* ESTRUTURA PARA FIREBASE */
    public String getMarcaModelo() {
        return marca.getId();
    }

    public String getTipoModelo() {
        return tipo.getId();
    }

    public HashMap<String, Boolean> getComandosList() {
        return gerarHashMapParaFirebase(comandos);
    }
}
