package br.com.marcosviniciusti.projetotcc.entidade;

import java.io.Serializable;
import java.util.Date;

public class ETransmissao implements Serializable {

    private String id;
    private Date dtHrSubmissao;
    private Date dtHrTransmissao;
    private String comando;
    private String idUsuario;
    private EEquipamento equipamento;

    public ETransmissao() {
    }

    public ETransmissao(String id, Date dtHrSubmissao, Date dtHrTransmissao, String comando, String idUsuario, EEquipamento equipamento) {
        this.id = id;
        this.dtHrSubmissao = dtHrSubmissao;
        this.dtHrTransmissao = dtHrTransmissao;
        this.comando = comando;
        this.idUsuario = idUsuario;
        this.equipamento = equipamento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDtHrSubmissao() {
        return dtHrSubmissao;
    }

    public void setDtHrSubmissao(Date dtHrSubmissao) {
        this.dtHrSubmissao = dtHrSubmissao;
    }

    public Date getDtHrTransmissao() {
        return dtHrTransmissao;
    }

    public void setDtHrTransmissao(Date dtHrTransmissao) {
        this.dtHrTransmissao = dtHrTransmissao;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public EEquipamento getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(EEquipamento equipamento) {
        this.equipamento = equipamento;
    }
}
