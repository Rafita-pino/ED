package cat.urv.deim;

import cat.urv.deim.exceptions.ElementNoTrobat;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class GrafPersones {
    Graf<Integer, Persona, Integer> graf;

    public GrafPersones(int mida){
        graf = new Graf<>(mida);
    }

    public GrafPersones(int mida, String filename, String filnameAmistats) throws NumberFormatException, IOException {
        graf = new Graf<>(mida);
        String[] parts;
        try (BufferedReader r = new BufferedReader(new FileReader(filename))) {
            String line = r.readLine();
            Persona p;
            while (line != null) {
                parts = line.split(",");
                p = new Persona(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), parts[2], parts[3],
                        Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
                graf.inserirVertex(Integer.parseInt(parts[0]), p);
                line = r.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try (BufferedReader r = new BufferedReader(new FileReader(filnameAmistats))) {
            String line = r.readLine();
            while (line != null) {
                parts = line.split(",");
                graf.inserirAresta(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                line = r.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodes per a guardar persones

    public void inserirPersona(Persona p) {
        graf.inserirVertex(p.getId_persona(), p);
    }

    public Persona consultarPersona(int id) throws ElementNoTrobat {
        try {
            return graf.consultarVertex(id);
        } catch (Exception e) {
            throw new ElementNoTrobat();
        }
    }

    public void esborrarPersona(int id) throws ElementNoTrobat {
        try{
            graf.esborrarVertex(id);
        }catch (Exception e){
            throw new ElementNoTrobat();
        }
    }

    public int numPersones() {
        return graf.numVertex();
    }

    public boolean esBuida() {
        return graf.esBuida();
    }

    public ILlistaGenerica<Integer> obtenirPersonesIDs() {
        return graf.obtenirVertexIDs();
    }

    // Metodes per a guardar amistats

    public void inserirAmistat(Persona p1, Persona p2) throws ElementNoTrobat  {
            try {
                graf.inserirAresta(p1.getId_persona(), p2.getId_persona());
            } catch (Exception e) {
               throw new ElementNoTrobat();
            }
    }



    public void inserirAmistat(Persona p1, Persona p2, int intensitat) throws ElementNoTrobat  {
        try {
            graf.inserirAresta(p1.getId_persona(), p2.getId_persona(), intensitat);
        } catch (Exception e) {
            throw new ElementNoTrobat();
        }

    }

    public void esborrarAmistat(Persona p1, Persona p2) throws ElementNoTrobat  {
        try {
            graf.esborrarAresta(p1.getId_persona(), p2.getId_persona());
        } catch (Exception e) {
            throw new ElementNoTrobat();
        }
    }

    public boolean existeixAmistat(Persona p1, Persona p2) throws ElementNoTrobat {
        try {
            return graf.existeixAresta(p1.getId_persona(), p2.getId_persona());
        } catch (Exception e) {
            return false;
        }

    }

    public int intensitatAmistat(Persona p1, Persona p2) throws ElementNoTrobat {
        try {
            return graf.consultarAresta(p1.getId_persona(), p2.getId_persona());
        } catch (Exception e) {
            throw new ElementNoTrobat();
        }
    }

    public int numAmistats() {
        return graf.numArestes();
    }

    public int numAmistats(Persona p) throws ElementNoTrobat {
        try {
            return graf.numVeins(p.getId_persona());
        } catch (Exception e) {
            throw new ElementNoTrobat();
        }

    }
    //retorna cert si vertex aillat no es cert
    public boolean teAmistats(Persona p) throws ElementNoTrobat{
        try {
            return !graf.vertexAillat(p.getId_persona());
        } catch (Exception e) {
            throw new ElementNoTrobat();
        }

    }


    public ILlistaGenerica<Integer> obtenirAmistats(Persona p) throws ElementNoTrobat {
        try {
            return graf.obtenirVeins(p.getId_persona());
        } catch (Exception e) {
            throw new ElementNoTrobat();
        }

    }

    // Aquest metode busca totes les persones del grup d'amistats de p que tenen alguna connexio amb p
    // ja sigui directament, o be perque son amics d'amics, o amics de amics de amics, etc.
    // Retorna una llista amb els ID de les persones del grup
    public ILlistaGenerica<Integer> obtenirGrupAmistats(Persona p) throws ElementNoTrobat {
        try {
            return graf.obtenirNodesConnectats(p.getId_persona());
        } catch (Exception e) {
            throw new ElementNoTrobat();
        }
    }

    // Aquest metode busca el grup d'amistats mes gran del graf, es a dir, el que te major nombre
    // de persones que estan connectades entre si. Retorna una llista amb els ID de les persones del grup
    public ILlistaGenerica<Integer> obtenirGrupAmistatsMesGran() {
        return null;
    }

}
