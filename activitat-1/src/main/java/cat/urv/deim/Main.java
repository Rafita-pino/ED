package cat.urv.deim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import cat.urv.deim.exceptions.PilaPlena;

public class Main {
    TADPila persones;

    public Main() {
    }

    private void carregarDades(String filename) throws IOException, PilaPlena {
        try(BufferedReader r = new BufferedReader(new FileReader(filename))){
            String line;
            String [] parts;
            Persona p;
            while ((line = r.readLine())!= null) {
                parts = line.split(",");
                p = new Persona(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), parts[2], parts[3],
                                Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
                persones.apilar(p);
            }

        }   catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    // Metode per carregar les dades en una pila estatica
    public TADPila carregarFitxerEstatica(String filename, int maxElem){
        persones = new PilaEstatica(maxElem);
        try {
            carregarDades(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return persones;
    }

    // Metode per carregar les dades en una pila dinamica
    public TADPila carregarFitxerDinamica(String filename, int maxElem) {
        persones = new PilaDinamica(maxElem);
        try {
            carregarDades(filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PilaPlena e) {
            e.printStackTrace();
        }
        return persones;
    }

    // Metode per carregar les dades en una pila java.util.stack
    public TADPila carregarFitxerStack(String filename, int maxElem) {
        persones = new PilaStack(maxElem);
        try {
            carregarDades(filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PilaPlena e) {
            e.printStackTrace();
        }
        return persones;
    }

    public static void main(String[] args) {
        System.out.println("No cal que executis aixo, mira els tests!");
    }
}
