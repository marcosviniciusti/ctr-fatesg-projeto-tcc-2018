package br.com.brainsflow.projetotcc.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import br.com.brainsflow.projetotcc.entities.Definition;

public class DefinitionDao {

    public String insert(Definition definicao) {
        String ret = "Definição armazenada com sucesso!";
        try {
            FileInputStream fileIn = new FileInputStream("/home/marcos/definitions");
            if (true) {

            } else {
                FileOutputStream fileOut = new FileOutputStream("/home/marcos/ArquivoTCC/definitions");
                ObjectOutputStream stream = new ObjectOutputStream(fileOut);
                stream.writeObject(definicao);
                stream.flush();
            }
        } catch (Exception erro) {
            ret = "Falha na gravação\n "+erro.toString();
        }
        return ret;
    }

    public static Definition search(int codigo) {
        try {
            FileInputStream file = new FileInputStream("/home/marcos/definitions");
            ObjectInputStream stream = new ObjectInputStream(file);
            return ((Definition) stream.readObject());
        } catch (Exception erro) {
            System.out.println("Erro na leitura\n "+erro.toString());
            return null;
        }
    }
}