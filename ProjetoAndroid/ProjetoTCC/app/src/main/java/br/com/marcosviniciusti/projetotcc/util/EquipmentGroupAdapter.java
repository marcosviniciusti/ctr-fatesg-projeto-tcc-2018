package br.com.marcosviniciusti.projetotcc.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.entities.EquipmentGroup;

public class EquipmentGroupAdapter extends ArrayAdapter {

    private Context context;
    private List<EquipmentGroup> listEquipmentGroup;

    public EquipmentGroupAdapter(Context context, List<EquipmentGroup> listEquipmentGroup) {
        super(context, 0, listEquipmentGroup);
        this.context = context;
        this.listEquipmentGroup = listEquipmentGroup;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EquipmentGroup equipmentGroup = this.listEquipmentGroup.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.content_list, null);

        TextView txName = convertView.findViewById(R.id.txName);
        txName.setText(equipmentGroup.toString());

        return convertView;
    }
}