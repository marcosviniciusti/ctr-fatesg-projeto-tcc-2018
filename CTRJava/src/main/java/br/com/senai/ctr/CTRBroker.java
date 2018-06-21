package br.com.senai.ctr;

import br.com.senai.ctr.dal.EmissorDAO;
import br.com.senai.ctr.model.Emissor;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CTRBroker {

    public static final ConcurrentLinkedDeque<TransmissaoIR> transmissoes = new ConcurrentLinkedDeque<>();
    public static final HashMap<String, Emissor> emissores = new HashMap<>();

    public static void registraEmissor(String idEmissor) {
        if (CTRBroker.emissores.get(idEmissor) == null) {
            // Recupera Emissor a partir do banco
            EmissorDAO emdao = new EmissorDAO(Firebase.getReference());
            Emissor em = emdao.syncRetrieve(idEmissor);
            emissores.put(em.getId(), em);

            CTRLogs.log("Emissor " + idEmissor + " registrado.");
        }
    }
}
