package cat.urv.deim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;


import cat.urv.deim.exceptions.ElementNoTrobat;

public class HashMapPersones {
    HashMapIndirecte<Integer, Persona> llista;
    public HashMapPersones(int mida){
        llista = new HashMapIndirecte<Integer, Persona>(mida);
    }

    public HashMapPersones(int mida, String filename) throws NumberFormatException, IOException {
        llista = new HashMapIndirecte<Integer, Persona>(mida);
        try(BufferedReader r = new BufferedReader(new FileReader(filename))){
            String line = r.readLine();
            String [] parts;
            Persona p;
            while (line!= null) {
                parts = line.split(",");
                p = new Persona(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), parts[2], parts[3],
                                Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
                //la key és la id_persona % mida taula que ja es fa en el metode inserir
                llista.inserir(Integer.parseInt(parts[0]), p);
                line = r.readLine();
                System.out.println(line);
            }
            r.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void inserir(Persona p) {
        llista.inserir(p.getId_persona(), p);
    }

    public void esborrar(int id) throws ElementNoTrobat {
        llista.esborrar(id);
    }

    public int numElements() {
        return llista.numElements();
    }

    public Persona consultar(int id) throws ElementNoTrobat {
        return llista.consultar(id);
    }

    public boolean buscar(Persona p) {
        return(llista.buscar(p.getId_persona()));
    }

    public boolean esBuida() {
        return llista.esBuida();
    }

    public int mida() {
        return llista.midaTaula();
    }

    public float factorCarrega() {
        return llista.factorCarrega();
    }

    public Persona[] personesPesInferior(int pes) {
        Persona[] persones = elements();
        int count = 0;
        for (Persona p : persones) {
            if (p.getPes() < pes) { // Aseguramos que p no sea nulo antes de acceder a su peso
                count++;
            }
        }
        Persona[] array = new Persona[count];
        int index = 0;
        for (Persona p : persones) {
            if (p.getPes() < pes) {
                array[index] = p;
                index++;
            }
        }
        return array;
    }



    public int[] obtenirIDs() {
        Object[] claus = llista.obtenirClaus();
        int[] clausInt = new int [claus.length];
        for (int i = 0; i < claus.length; i++) {
            if (claus[i]!=null)clausInt[i] = (int) claus[i];
        }
        return clausInt;
    }

    public Persona[] elements() {
        int i = 0;
        Persona[] elem = new Persona[llista.numElements()];
        Iterator<Persona> iterator = llista.iterator();
        while (iterator.hasNext()) {
                elem[i] = (Persona)iterator.next();
                i++;
        }
        return elem;
    }

}
