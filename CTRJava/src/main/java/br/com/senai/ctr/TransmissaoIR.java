package br.com.senai.ctr;

import java.util.List;

public class TransmissaoIR {
    private String idEmissor;
    private String type;
    private List<Integer> rawData;

    public String getIdEmissor() {
        return idEmissor;
    }

    public TransmissaoIR setIdEmissor(String idEmissor) {
        this.idEmissor = idEmissor;
        return this;
    }

    public String getType() {
        return type;
    }

    public TransmissaoIR setType(String type) {
        this.type = type;
        return this;
    }

    public List<Integer> getRawData() {
        return rawData;
    }

    public TransmissaoIR setRawData(List<Integer> rawData) {
        this.rawData = rawData;
        return this;
    }
}
