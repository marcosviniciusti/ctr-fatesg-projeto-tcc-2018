package br.com.brainsflow.projetotcc.util;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

import br.com.brainsflow.projetotcc.entities.Definition;

/**
 * Created by marcos on 02/03/18.
 */

public class StableArrayAdapter extends ArrayAdapter<Definition> {
    HashMap<Definition, Integer> mIdMap = new HashMap<Definition, Integer>();

    public StableArrayAdapter(Context context, int textViewResourceId, List<Definition> objects) {
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        Definition item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
