package cat.urv.deim;

import cat.urv.deim.exceptions.VertexNoTrobat;
import cat.urv.deim.exceptions.ArestaNoTrobada;
import cat.urv.deim.exceptions.ElementNoTrobat;


public class Graf<K extends Comparable<K>, V extends Comparable<V>, E> implements IGraf<K, V, E>{
    private int numAristas;
    IHashMap<K,NodeVertex> taulaVertexs;
    Arista fant;
    public class Arista {
        private NodeVertex v1, v2;
        private E pes;
        private Arista seguent;

        public Arista(E p, NodeVertex v1, NodeVertex v2){
            pes = p;
            this.v1 = v1;
            this.v2 = v2;
        }


    }

    public class NodeVertex implements Comparable<NodeVertex>{
        private V info;
        private K key;
        private Arista arista = null;


        public NodeVertex(V value, K k){
            info = value;
            key = k;
        }

        @Override
        public int compareTo (NodeVertex v2){
            if (key instanceof Comparable) {
                return ((Comparable<V>) info).compareTo(v2.info);
            } else {
                throw new UnsupportedOperationException("La clave no es comparable");
            }
        }


    }

    public Graf (int mida){
        numAristas = 0;
        taulaVertexs = new HashMapIndirecte<K,NodeVertex>(mida);
        fant = new Arista(null, null, null);


    }

////////////////////////////////////////////////////////////////////////////////////
    // Operacions per a treballar amb els vertexs

    // Metode per insertar un nou vertex al graf. El valor de K es l'identificador del vertex i V es el valor del vertex
    public void inserirVertex(K key, V value){
        NodeVertex newNode = new NodeVertex(value, key);
        taulaVertexs.inserir(key, newNode);
        //numElements se incrementa en la taula de hashing
    }

    // Metode per a obtenir el valor d'un vertex del graf a partir del seu identificador
    public V consultarVertex(K key) throws VertexNoTrobat{
        try {
            return taulaVertexs.consultar(key).info;
        } catch (Exception e) {
            throw new VertexNoTrobat();
        }
    }

    // Metode per a esborrar un vertex del graf a partir del seu identificador
    // Aquest metode tambe ha d'esborrar totes les arestes associades a aquest vertex

    public void esborrarVertex(K key) throws VertexNoTrobat, ArestaNoTrobada{
        try {
            // Eliminar el vertex de la taula de vertexs
            NodeVertex vertexAEsborrar = taulaVertexs.consultar(key);
            Arista arista = vertexAEsborrar.arista;
            while (arista!=null) {
                if (arista.v1 == vertexAEsborrar) {
                    esborrarAresta(vertexAEsborrar.key, arista.v2.key);
                } else if (arista.v2 == vertexAEsborrar){
                    esborrarAresta(vertexAEsborrar.key, arista.v1.key);
                }
                arista = arista.seguent;
            }
        // Una vez eliminadas todas las aristas asociadas al vértice, eliminar el vértice de la tabla de vértices
        taulaVertexs.esborrar(key);
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }




    // Metode per a comprovar si hi ha algun vertex introduit al graf
    public boolean esBuida(){
        return taulaVertexs.numElements()==0;
    }

    // Metode per a comprovar el nombre de vertexs introduits al graf
    public int numVertex(){
        return taulaVertexs.numElements();
    }

    // Metode per a obtenir tots els ID de vertex de l'estrucutra
    public ILlistaGenerica<K> obtenirVertexIDs(){
        return taulaVertexs.obtenirClaus();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    // Metode per a insertar una aresta al graf. Els valors de vertex 1 i vertex 2 son els vertex a connectar i E es el pes de la aresta

    // Si ja existeix l'aresta se li actualitza el seu pes
    public void inserirAresta(K v1, K v2, E pes) throws VertexNoTrobat {
        try {
            if (taulaVertexs.buscar(v1) && taulaVertexs.buscar(v2)) {
                NodeVertex vertex1 = taulaVertexs.consultar(v1);
                NodeVertex vertex2 = taulaVertexs.consultar(v2);
                if (!existeixAresta(v1, v2)) {
                    Arista novaArista1 = new Arista(pes, vertex1, vertex2);
                    novaArista1.seguent = vertex1.arista; // Conectar la nueva arista al inicio de la lista de aristas de vertex1
                    vertex1.arista = novaArista1;

                    Arista novaArista2 = new Arista(pes, vertex2, vertex1);
                    novaArista2.seguent = vertex2.arista; // Conectar la nueva arista al inicio de la lista de aristas de vertex2
                    vertex2.arista = novaArista2;

                    // Actualizar el puntero fant solo si es el primer enlace
                    if (fant.seguent == null) {
                        fant.seguent = novaArista1;
                    }
                    numAristas++;
                } else { // Si la arista ya existe, actualizar su peso
                    Arista actual = vertex1.arista;
                    while (actual != null) {
                        if (actual.v1.equals(vertex1) && actual.v2.equals(vertex2)) {
                            actual.pes = pes;
                        }
                        actual = actual.seguent;
                    }
                }
            } else {
                throw new VertexNoTrobat();
            }
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }


    // Metode equivalent a l'anterior, afegint com a pes el valor null
    public void inserirAresta(K v1, K v2) throws VertexNoTrobat{
        try {
            inserirAresta(v1, v2, null);
        } catch (Exception e) {
            throw new VertexNoTrobat();
        }

    }

    // Metode per a saber si una aresta existeix a partir dels vertex que connecta
    public boolean existeixAresta(K v1, K v2) throws VertexNoTrobat{
        try {
            NodeVertex vertex1 = taulaVertexs.consultar(v1);
            NodeVertex vertex2 = taulaVertexs.consultar(v2);
            if (taulaVertexs.buscar(v1) && taulaVertexs.buscar(v2)) {
                Arista actual = vertex1.arista;
                while (actual != null) {
                    if (actual.v1.equals(vertex1) && actual.v2.equals(vertex2)) {
                        return true;
                    }
                    actual = actual.seguent;
                }
                return false;
            } else {
                throw new VertexNoTrobat();
            }
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    // Metode per a obtenir el pes d'una aresta a partir dels vertex que connecta
    public E consultarAresta(K v1, K v2) throws VertexNoTrobat, ArestaNoTrobada{
            try {
                if (existeixAresta(v1, v2)) {
                    NodeVertex vertex1 = taulaVertexs.consultar(v1);
                    NodeVertex vertex2 = taulaVertexs.consultar(v2);
                    if (taulaVertexs.buscar(v1) && taulaVertexs.buscar(v2)) {
                        Arista actual = vertex1.arista;
                        while (actual != null) {
                            if (actual.v1.equals(vertex1) && actual.v2.equals(vertex2)) {
                                return actual.pes;
                            }
                            actual = actual.seguent;
                        }
                        throw new ArestaNoTrobada();
                    }
                    throw new VertexNoTrobat();
                }
                throw new ArestaNoTrobada();
            }catch (Exception e) {
                throw new VertexNoTrobat();
            }
        }


    // Metode per a esborrar una aresta a partir dels vertex que connecta
    public void esborrarAresta(K v1, K v2) throws VertexNoTrobat, ArestaNoTrobada {
        try {
            if (existeixAresta(v1, v2)) {
                NodeVertex vertex1 = taulaVertexs.consultar(v1);
                NodeVertex vertex2 = taulaVertexs.consultar(v2);
                // Eliminar la arista desde vertex1
                Arista anterior = null;
                Arista actual = vertex1.arista;
                boolean borrat = false;
                while (actual != null && !borrat) {
                    if (actual.v1.equals(vertex1) && actual.v2.equals(vertex2)) {
                        if (actual.seguent == null || anterior == null) {//ultima hoja o primera
                            actual = null;
                        }else{
                            anterior.seguent = actual.seguent;
                        }
                        borrat = true; // Terminar el bucle una vez que se haya eliminado la arista
                    }
                    if (!borrat) {
                        anterior = actual;
                        actual = actual.seguent;
                    }

                }
                //numAristas--;
                // Eliminar la arista desde vertex2
                anterior = null;
                actual = vertex2.arista;
                borrat = false;

                while (actual != null && !borrat) {
                    if (actual.v1.equals(vertex2) && actual.v2.equals(vertex1)) {
                        if (actual.seguent == null || anterior == null) {
                            actual = null;
                        }else{
                            anterior.seguent = actual.seguent;
                        }
                        borrat = true;
                    }
                    if (!borrat) {
                        anterior = actual;
                        actual = actual.seguent;
                    }
                }
                numAristas--;
            } else {
                throw new ArestaNoTrobada();
            }
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    // Metode per a comptar quantes arestes te el graf en total
    public int numArestes(){
        return numAristas;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    // Metodes auxiliars per a treballar amb el graf
    // Metode per a saber si un vertex te veins
    //Retorna true si esta aillat
    public boolean vertexAillat(K v1) throws VertexNoTrobat{
            if (taulaVertexs.buscar(v1)) {
                try {
                    return taulaVertexs.consultar(v1).arista==null;
                } catch (Exception e) {
                    throw new VertexNoTrobat();
                }
            } else return false;
    }

    // Metode per a saber quants veins te un vertex
    public int numVeins(K v1) throws VertexNoTrobat{
        try {
            int numVecinos = 0;
            NodeVertex vertex = taulaVertexs.consultar(v1);
            Arista actual = vertex.arista;
            while (actual != null) {
                numVecinos++;
                actual = actual.seguent;
            }
            return numVecinos;
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    // Metode per a obtenir tots els ID de vertex veins d'un vertex
    public ILlistaGenerica<K> obtenirVeins(K v1) throws VertexNoTrobat{
        try {
            LlistaOrdenada<K> vecinos = new LlistaOrdenada<K>();
            NodeVertex vertex = taulaVertexs.consultar(v1);
            Arista actual = vertex.arista;
            while (actual != null) {
                if (actual.v1.key.equals(v1)) {
                    vecinos.inserir(actual.v2.key);
                } else {
                    vecinos.inserir(actual.v1.key);
                }
                actual = actual.seguent;
            }
            return vecinos;
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////
    // Metodes OPCIONALS - Si es fa la part obligatoria la nota maxima sera un 8
    // Si s'implementen aquests dos metodes correctament es podra obtenir fins a 2 punts addicionals

    // Metode per a obtenir tots els nodes que estan connectats a un vertex
    // es a dir, nodes als que hi ha un cami directe des del vertex
    // El node que es passa com a parametre tambe es retorna dins de la llista!
    public ILlistaGenerica<K> obtenirNodesConnectats(K v1) throws VertexNoTrobat{
        try {

            ILlistaGenerica<K> nodesConnectats = new LlistaOrdenada<>();
            nodesConnectats.inserir(v1);
            ILlistaGenerica<K> veins = obtenirVeins(v1);
            int i = 0;
            while (i<veins.numElements()) {
                nodesConnectats.inserir(veins.consultar(i));
                i++;
            }
            return nodesConnectats;
        } catch (Exception e) {
            throw new VertexNoTrobat();
        }

    }

    // Metode per a obtenir els nodes que composen la Component Connexa mes gran del graf
    public ILlistaGenerica<K> obtenirComponentConnexaMesGran(){
        return null;

    }


}
