package br.com.senai.ctr.dal;

import br.com.senai.ctr.model.IEntidade;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.List;

public abstract class DAO<T extends IEntidade> {

    protected DatabaseReference reference = null;

    protected final String child;

    protected final HashMap<String, T> map;

    public DAO(DatabaseReference reference, String child) {
        this.reference = reference;
        this.child = child;
        this.map = new HashMap<>();
    }

    protected abstract ValueEventListener generateNewValueEventListener();

    public HashMap<String, T> getMap() {
        return map;
    }

    public DatabaseReference create(T obj) {
        DatabaseReference objRef = reference.child(child);

        if (obj.getId() != null) {
            objRef = objRef.child(obj.getId());
        } else {
            objRef = objRef.push();
        }

        objRef.setValueAsync(obj);
        return objRef;
    }

    public DatabaseReference syncCreate(T obj) {
        DatabaseReference objRef = reference.child(child);

        if (obj.getId() != null) {
            objRef = objRef.child(obj.getId());
        } else {
            objRef = objRef.push();
        }

        final Boolean[] complete = {false};
        objRef.setValue(obj, (error, ref) -> {
            if (error == null) {
                obj.setId(ref.getKey());
            }
            complete[0] = true;
        });
        while (!complete[0]) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return objRef;
    }

    public T retrieve(String id) {
        DatabaseReference objRef = reference.child(child).child(id);

        objRef.addValueEventListener(generateNewValueEventListener());

        return null;
    }

    public T retrieve(String id, ValueEventListener listener) {
        DatabaseReference objRef = reference.child(child).child(id);

        objRef.addValueEventListener(listener);

        return null;
    }

    public T syncRetrieve(String id) {
        map.remove(id);
        DatabaseReference objRef = reference.child(child).child(id);

        objRef.addValueEventListener(generateNewValueEventListener());

        while (map.get(id) == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return map.get(id);
    }

//    public T syncRetrieveByPath() {
//        DatabaseReference objRef = reference.child(child)
//    }

    HashMap<String, Boolean> syncRetrieveReferences(T obj, String listFieldName) {
        if (obj == null || obj.getId() == null) {
            return null;
        }

        HashMap<String, Boolean> ret = new HashMap<>();

        DatabaseReference objRef = reference.child(child).child(obj.getId()).child(listFieldName);

        boolean[] complete = new boolean[]{false};
        objRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot c : snapshot.getChildren()) {
                    if (c.getValue(Boolean.class)) {
                        ret.put(c.getKey(), true);
                    }
                }

                complete[0] = true;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                error.toException().printStackTrace();
            }
        });

        while (!complete[0]) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    public DatabaseReference update(T obj) {
        if (obj == null || obj.getId() == null) {
            return null;
        }

        DatabaseReference objRef = reference.child(child);

        objRef.child(obj.getId()).setValueAsync(obj);
        return objRef;
    }

    public DatabaseReference syncUpdate(T obj) {
        if (obj == null || obj.getId() == null) {
            return null;
        }

        DatabaseReference objRef = reference.child(child);

        final Boolean[] complete = {false};
        objRef.child(obj.getId()).setValue(obj, (error, ref) -> {
            complete[0] = true;
        });
        while (!complete[0]) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return objRef;
    }

    public DatabaseReference delete(T obj) {
        DatabaseReference objRef = reference.child(child);

        objRef.child(obj.getId()).setValueAsync(null);
        return objRef;
    }

    public DatabaseReference syncDelete(T obj) {
        if (obj == null || obj.getId() == null) {
            return null;
        }

        DatabaseReference objRef = reference.child(child);

        final Boolean[] complete = {false};
        objRef.child(obj.getId()).setValue(null, (error, ref) -> {
            complete[0] = true;
        });
        while (!complete[0]) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return objRef;
    }

    public List<T> list() {
        throw new UnsupportedOperationException("Ainda não implementado");
    }

}
