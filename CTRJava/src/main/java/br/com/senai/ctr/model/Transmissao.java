package br.com.senai.ctr.model;

import com.google.firebase.database.Exclude;

import java.util.Date;

public class Transmissao implements IEntidade {

    private String id;
    private Date dtHrSubmissao;
    private Date dtHrTransmissao;
    private Comando comando;
    private Usuario usuario;
    private Equipamento equipamento;

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

    @Exclude
    public Equipamento getEquipamento() {
        return equipamento;
    }

    public Transmissao setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
        return this;
    }

    /* ESTRUTURA PARA FIREBASE */
    private String comandoTransmissao;

    public String getComandoTransmissao() {
        if (comando != null) {
            return comando.getId();
        } else {
            return comandoTransmissao;
        }
    }

    private String usuarioTransmissao;

    public String getUsuarioTransmissao() {
        if (usuario != null) {
            return usuario.getId();
        } else {
            return usuarioTransmissao;
        }
    }

    private String equipamentoTransmissao;

    public String getEquipamentoTransmissao() {
        if (equipamento != null) {
            return equipamento.getId();
        } else {
            return equipamentoTransmissao;
        }
    }
}
