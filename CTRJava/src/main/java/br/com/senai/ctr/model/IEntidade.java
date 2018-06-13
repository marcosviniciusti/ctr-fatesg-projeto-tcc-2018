package br.com.senai.ctr.model;

import java.util.*;

public interface IEntidade {

    String getId();

    IEntidade setId(String id);

//    default HashMap<String, Boolean> gerarHashMapParaFirebase(IEntidade ent) {
//        HashMap<String, Boolean> ret = new HashMap<>();
//        if (ent != null && ent.getId() != null) {
//            ret.put(ent.getId(), true);
//        }
//        return ret;
//    }

    default HashMap<String, Boolean> gerarHashMapParaFirebase(List lst) {
        return gerarHashMap(lst);
    }

    static HashMap<String, Boolean> gerarHashMap(List lst) {
        HashMap<String, Boolean> ret = new HashMap<>();
        if (lst != null && lst.size() > 0) {
            for (Object obj : lst) {
                ret.put(
                        ((IEntidade) obj).getId(),
                        true
                );
            }
        }

        return ret;
    }
}
