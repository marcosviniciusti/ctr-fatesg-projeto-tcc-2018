package br.com.marcosviniciusti.projetotcc.negocio;

import android.util.Log;

import com.google.firebase.database.ValueEventListener;

import br.com.marcosviniciusti.projetotcc.entidade.EGrupoEquipamento;
import br.com.marcosviniciusti.projetotcc.persistencia.PGrupoEquipamento;

public class NGrupoEquipamento {

    // Atributos da classe.
    private static final String TAG = "NGrupoEquipamento";
    private EGrupoEquipamento grupoEquipamento;
    private PGrupoEquipamento persistencia;

    public NGrupoEquipamento() {
        try {
            this.persistencia = new PGrupoEquipamento();
        } catch (Exception error) {
            Log.e(TAG, "ERRO no construtor: "+error.getMessage());
        }
    }

    public void salvar(EGrupoEquipamento grupoEquipamento) {
        try {
            this.persistencia.salvar(grupoEquipamento);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método salvar: "+error.getMessage());
        }
    }

    public void deletar(EGrupoEquipamento grupoEquipamento) {
        try {
            this.persistencia.deletar(grupoEquipamento);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método deletar: "+error.getMessage());
        }
    }

    public void consultar(EGrupoEquipamento grupoEquipamento, ValueEventListener event) {
        try {
            this.persistencia.consultar(grupoEquipamento, event);
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
