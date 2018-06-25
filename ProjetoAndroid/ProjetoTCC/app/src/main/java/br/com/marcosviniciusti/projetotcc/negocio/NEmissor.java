package br.com.marcosviniciusti.projetotcc.negocio;

import android.util.Log;

import com.google.firebase.database.ValueEventListener;

import br.com.marcosviniciusti.projetotcc.entidade.EEquipamento;
import br.com.marcosviniciusti.projetotcc.persistencia.PEquipamento;

public class NEmissor {

    // Atributos da classe.
    private static final String TAG = "NEquipamento";
    private EEquipamento equipamento;
    private PEquipamento persistencia;

    public NEmissor() {
        try {
            this.persistencia = new PEquipamento();
        } catch (Exception error) {
            Log.e(TAG, "ERRO no construtor: "+error.getMessage());
        }
    }

    public void salvar(EEquipamento equipamento) {
        try {
            this.persistencia.salvar(equipamento);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método salvar: "+error.getMessage());
        }
    }

    public void deletar(EEquipamento equipamento) {
        try {
            this.persistencia.deletar(equipamento);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método deletar: "+error.getMessage());
        }
    }

    public void consultar(EEquipamento equipamento, ValueEventListener event) {
        try {
            this.persistencia.consultar(equipamento, event);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método salvar: "+error.getMessage());
        }
    }

    public void listar(ValueEventListener event) {
        try {
            this.persistencia.listar(event);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método salvar: "+error.getMessage());
        }
    }
}
