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
import br.com.marcosviniciusti.projetotcc.entities.Equipment;
import br.com.marcosviniciusti.projetotcc.entities.EquipmentGroup;

public class EquipmentAdapter extends ArrayAdapter<Equipment> {

    private Context context;
    private EquipmentGroup equipmentGroup;
    private List<Equipment> listEquipment;

    public EquipmentAdapter(Context context, EquipmentGroup equipmentGroup) {
        super(context, 0, equipmentGroup.getListEquipment());
        this.context = context;
        this.equipmentGroup = equipmentGroup;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        listEquipment = this.equipmentGroup.getListEquipment();
        Equipment equipment = listEquipment.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.content_list, null);

        TextView txNameEquipmentGroup = convertView.findViewById(R.id.txName);
        txNameEquipmentGroup.setText(equipment.toString());

        return convertView;
    }
}