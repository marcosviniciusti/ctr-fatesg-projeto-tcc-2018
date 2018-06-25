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
import br.com.marcosviniciusti.projetotcc.entidade.EUsuario;

public class UsuariosAdapter extends ArrayAdapter<EUsuario> implements Serializable{

    private Context context;
    private List<EUsuario> listaUsuarios;

    public UsuariosAdapter (Context context, List<EUsuario> listaUsuarios) {
        super(context, 0, listaUsuarios);
        this.context = context;
        this.listaUsuarios = listaUsuarios;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EUsuario usuario = this.listaUsuarios.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.content_list, null);

        TextView txNomeItem = convertView.findViewById(R.id.txvNomeItem);
        txNomeItem.setText(usuario.getEmail());

        return convertView;
    }
}