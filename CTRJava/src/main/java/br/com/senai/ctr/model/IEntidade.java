package br.com.senai.ctr.model;

import java.util.*;

public interface IEntidade {

    String getId();

    IEntidade setId(String id);

    default List<String> gerarIDListParaFirebase(List lst) {
        return gerarIDList(lst);
    }

    static List<String> gerarIDList(List lst) {
        List<String> ret = new LinkedList<>();

        if (lst != null && lst.size() > 0) {
            for (Object obj : lst) {
                ret.add(((IEntidade) obj).getId());
            }
        }

        return ret;
    }
}
