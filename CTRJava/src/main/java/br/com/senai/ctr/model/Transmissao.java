package br.com.senai.ctr.model;

import com.google.firebase.database.Exclude;

import java.util.Date;

public class Transmissao implements IEntidade {

    private String id;
    private Integer status;
    private Date dtHrSubmissao;
    private Date dtHrTransmissao;
    private Comando comando;
    private Usuario usuario;

    @Exclude
    @Override
    public String getId() {
        return id;
    }

    @Override
    public Transmissao setId(String id) {
        this.id = id;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public Transmissao setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Date getDtHrSubmissao() {
        return dtHrSubmissao;
    }

    public Transmissao setDtHrSubmissao(Date dtHrSubmissao) {
        this.dtHrSubmissao = dtHrSubmissao;
        return this;
    }

    public Date getDtHrTransmissao() {
        return dtHrTransmissao;
    }

    public Transmissao setDtHrTransmissao(Date dtHrTransmissao) {
        this.dtHrTransmissao = dtHrTransmissao;
        return this;
    }

    @Exclude
    public Comando getComando() {
        return comando;
    }

    public Transmissao setComando(Comando comando) {
        this.comando = comando;
        return this;
    }

    @Exclude
    public Usuario getUsuario() {
        return usuario;
    }

    public Transmissao setUsuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    /* ESTRUTURA PARA FIREBASE */
    public String getComandoTransmissao() {
        return comando.getId();
    }

    public String getUsuarioTransmissao() {
        return usuario.getId();
    }
}
