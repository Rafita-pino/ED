package cat.urv.deim;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.PosicioForaRang;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class LlistaPersones {
    ILlistaGenerica<Persona> llista;

    // Constructor que crea una llista de persones buida del tipus especificat en el boolea (ordenada o no)
    public LlistaPersones(boolean ordenada) {
        if (ordenada) {
            llista = new LlistaOrdenada<Persona>();
        }else{
            llista = new LlistaNoOrdenada<Persona>();
        }
    }

    //Constructor que crea una llista del tipus especificat i hi carrega totes les dades del fitxer
    public LlistaPersones(boolean ordenada, String filename) throws IOException{
        if (ordenada) {
            llista = new LlistaOrdenada<Persona>();
        }else{
            llista = new LlistaNoOrdenada<Persona>();
        }
        try(BufferedReader r = new BufferedReader(new FileReader(filename))){
            String line;
            String [] parts;
            Persona p;
            while ((line = r.readLine())!= null) {
                parts = line.split(",");
                p = new Persona(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), parts[2], parts[3],
                                Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
                llista.inserir(p);
            }

        }   catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    //Afegeix una nova persona a la llista que tenim inicialitzada
    public void inserir(Persona p) {
        llista.inserir(p);
    }

    //Metode per a consultar una persona a partir de la seva posicio
    public Persona consultar(int pos) throws PosicioForaRang {
        return llista.consultar(pos);
    }

    //Metode per a saber si una persona existeix a l'estructura
    public boolean existeix(Persona p) {
        return llista.existeix(p);
    }

    //Metode per a esborrar una persona de l'estructura
    public void esborrar(Persona e) throws ElementNoTrobat {
        llista.esborrar(e);
    }

    //Metode que ens indica en quina posicio de la llista hi ha la persona que es passa per parametre
    public int posicioPersona(Persona persona) throws ElementNoTrobat {
        return llista.buscar(persona);
    }

    //Metode per a saber si la llista esta buida
    public boolean esBuida() {
        return llista.esBuida();
    }

    //Metode per a saber el nombre d'elements de la llista
    public int numElements() {
        return llista.numElements();
    }

   //Metode per a obtenir un array amb tots els elements de la llista
    public Persona[] elements() {
        Object[] elObj = new Object[llista.numElements()];
        Persona[] elPer = new Persona[llista.numElements()];
        elObj = llista.elements();
        for (int i = 0; i < llista.numElements(); i++) {
            elPer[i] = (Persona)elObj [i];
        }
        return elPer;
    }

    //Metode per a obtenir una persona a partir del seu id
    public Persona buscarPerId(int id) throws ElementNoTrobat {
        Persona[] persones = elements();
        for (Persona p : persones){
            if (p.getId_persona() == id) {
                return p;
            }
        }
        throw new ElementNoTrobat();
    }

    //Metode per a obtenir un array amb totes les persones que tenen un pes inferior al valor que es passa per parametre
    public Persona[] personesPesInferior(int pes) {
        Persona[] persones = new Persona[numElements()];
        persones = elements();
        int i = 0;
        for (Persona p : persones) {
            if (p.getPes()<pes) {
                i++;
            }
        }
        Persona[] array = new Persona[i];
        i = 0;
        for (Persona p : persones){
                if (p.getPes()<pes) {
                    array[i] = p;
                    i++;
                }
        }
        return array;
    }
}
