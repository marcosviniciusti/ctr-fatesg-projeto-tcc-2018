package br.com.senai.ctr.dal;

import br.com.senai.ctr.App;
import br.com.senai.ctr.model.Comando;
import br.com.senai.ctr.model.Equipamento;
import br.com.senai.ctr.model.Transmissao;
import com.google.firebase.database.*;

import java.util.concurrent.ConcurrentLinkedDeque;

public class TransmissaoDAO extends DAO<Transmissao> {

    public TransmissaoDAO(DatabaseReference reference) {
        super(reference, "transmissoes");
    }

    @Override
    protected ValueEventListener generateNewValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Transmissao obj = snapshot.getValue(Transmissao.class);
                obj.setId(snapshot.getKey());

                map.put(snapshot.getKey(), obj);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                error.toException().printStackTrace();
            }
        };
    }

    public void syncTransmissaoByEmissor(final String id_emissor, final ConcurrentLinkedDeque<Transmissao> transmissoes) {

        reference.child("equipamentos").orderByChild("emissorEquipamento").equalTo(id_emissor)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                        Equipamento e = snapshot.getValue(Equipamento.class);
                        e.setId(snapshot.getKey());
                        System.out.println(e.getId());

                        reference.child("transmissoes").orderByChild("equipamentoTransmissao").equalTo(e.getId())
                                .addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                                        Transmissao t = snapshot.getValue(Transmissao.class);
                                        t.setId(snapshot.getKey());
                                        System.out.println(e.getModeloEquipamento());
                                        System.out.println(t.getId());

//                                        String[] modelo = e.getModeloEquipamento().split("/");
//                                        if (modelo.length == 2) {
//                                            reference
//                                                    .child("marcas")
//                                                    .child(modelo[0])
//                                                    .child("modelos")
//                                                    .child(modelo[1])
//                                                    .child("comandos").child(t.getComandoTransmissao())
//                                                    .addValueEventListener(new ValueEventListener() {
//                                                        @Override
//                                                        public void onDataChange(DataSnapshot snapshot) {
//                                                            Comando c = snapshot.getValue(Comando.class);
//                                                            c.setId(snapshot.getKey());
//
//                                                            t.setComando(c);
//                                                            System.out.println(c.getId());
//                                                            System.out.println(c.getRawData().size());
//                                                        }
//
//                                                        @Override
//                                                        public void onCancelled(DatabaseError error) {
//
//                                                        }
//                                                    });
//                                        }


                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot snapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {

                                    }
                                });
                    }

                    @Override
                    public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

    }
}
