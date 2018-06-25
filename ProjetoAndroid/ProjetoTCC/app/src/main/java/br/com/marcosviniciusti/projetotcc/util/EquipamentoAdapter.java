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
import br.com.marcosviniciusti.projetotcc.entidade.EEquipamento;
import br.com.marcosviniciusti.projetotcc.entidade.EGrupoEquipamento;

public class EquipamentoAdapter extends ArrayAdapter<EEquipamento> implements Serializable{

    private Context context;
    private EGrupoEquipamento grupoEquipamento;
    private List<EEquipamento> equipamentos;

    public EquipamentoAdapter(Context context, EGrupoEquipamento grupoEquipamento) {
        super(context, 0, grupoEquipamento.getEquipamentos());
        this.context = context;
        this.grupoEquipamento = grupoEquipamento;
        this.equipamentos = grupoEquipamento.getEquipamentos();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        EEquipamento equipamento = this.equipamentos.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.content_list, null);

        TextView txNomeItem = convertView.findViewById(R.id.txvNomeItem);
        txNomeItem.setText(equipamento.toString());

        return convertView;
    }
}