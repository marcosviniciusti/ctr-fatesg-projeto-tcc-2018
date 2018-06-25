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

public class TipoEquipamentoAdapter extends ArrayAdapter<EnumTipoEquipamento> implements Serializable {

    private Context context;
    private List<EnumTipoEquipamento> listaTipos;

    public TipoEquipamentoAdapter(Context context, int layout, List<EnumTipoEquipamento> listaTipos) {
        super(context, android.R.layout.simple_spinner_item,listaTipos);
        this.context = context;
        this.listaTipos = listaTipos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EnumTipoEquipamento tipo = this.listaTipos.get(position);

        convertView = LayoutInflater.from(this.context).inflate(android.R.layout.simple_spinner_item, null);

        TextView txNomeItem = convertView.findViewById(android.R.id.text1);
        txNomeItem.setText(tipo.toString());

        return convertView;
    }
}