package br.com.marcosviniciusti.projetotcc.entidade;

import java.io.Serializable;
import java.util.List;

import br.com.marcosviniciusti.projetotcc.util.EnumTipoEquipamento;

public class EModelo implements Serializable {

    private String id;
    private String nome;
    private String idMarca;
    private EnumTipoEquipamento tipo;
    private List<String> comandos;

    public EModelo() {
    }

    public EModelo(String id, String nome, String idMarca, EnumTipoEquipamento tipo, List<String> comandos) {
        this.id = id;
        this.nome = nome;
        this.idMarca = idMarca;
        this.tipo = tipo;
        this.comandos = comandos;
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

    public String getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(String idMarca) {
        this.idMarca = idMarca;
    }

    public EnumTipoEquipamento getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoEquipamento tipo) {
        this.tipo = tipo;
    }

    public List<String> getComandos() {
        return comandos;
    }

    public void setComandos(List<String> comandos) {
        this.comandos = comandos;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}