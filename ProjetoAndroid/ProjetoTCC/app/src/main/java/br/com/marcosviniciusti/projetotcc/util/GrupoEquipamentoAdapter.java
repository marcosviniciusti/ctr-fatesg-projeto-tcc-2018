package br.com.marcosviniciusti.projetotcc.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.entidade.EGrupoEquipamento;

public class GrupoEquipamentoAdapter extends ArrayAdapter<EGrupoEquipamento> implements Serializable {

    private Context context;
    private List<EGrupoEquipamento> listaGrupoEquipamentos;

    public GrupoEquipamentoAdapter(Context context, List<EGrupoEquipamento> listaGrupoEquipamentos) {
        super(context, 0, listaGrupoEquipamentos);
        this.context = context;
        this.listaGrupoEquipamentos = listaGrupoEquipamentos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EGrupoEquipamento grupoEquipamento = this.listaGrupoEquipamentos.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.content_list, null);

        TextView txNomeItem = convertView.findViewById(R.id.txvNomeItem);
        txNomeItem.setText(grupoEquipamento.toString());

        return convertView;
    }
}