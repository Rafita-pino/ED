/************************************************************************************************************
*************************************** GRAF NO DIRIGIT NI ETIQUETAT ****************************************
*************************************************************************************************************
*   SE HAN ELIMINADO LAS ETIQUETAS DE LAS ARISTAS Y CAMBIADO EL CONTENIDO DE ARISTA, EN VEZ DE GUARDAR
*   VERTICES ENTEROS GUARDA SOLAMENTE LA KEY DE LOS VERTICES A LA QUE PERTENECE LA ARISTA. 
*/
package cat.urv.deim;

import cat.urv.deim.exceptions.*;
import java.time.*;


public class Graf<K extends Comparable<K>, V extends Comparable<V>> implements IGraf<K, V>{
    private int numAristas, numComunitats = 0;
    private double modularitat = -100.0;
    IHashMap<K,NodeVertex> taulaVertexs;
    Arista fant;
    public class Arista {
        private K v1, v2;
        private Arista seguent;

        public Arista(K v1, K v2){
            this.v1 = v1;
            this.v2 = v2;
        }
    }

    public class NodeVertex implements Comparable<NodeVertex>{
        private V info;
        private K key;
        private int comunitat;
        private Arista arista = null;
        public NodeVertex(V value, K k){
            info = value;
            key = k;
            comunitat = numComunitats +1;

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
        fant = new Arista(null, null);
    }

////////////////////////////////////////////////////////////////////////////////////
    // Operacions per a treballar amb els vertexs

    // Metode per insertar un nou vertex al graf. El valor de K es l'identificador del vertex i V es el valor del vertex
    public void inserirVertex(K key, V value){
        NodeVertex newNode = new NodeVertex(value, key);
        taulaVertexs.inserir(key, newNode);
        try {
            taulaVertexs.consultar(key).comunitat=numComunitats; //creem tantes comunitats com vertexs tenim
            numComunitats++;
        } catch (ElementNoTrobat e) {
            System.out.println("Element no trobat");
        }
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

    public void esborrarVertex(K key) throws VertexNoTrobat, ArestaNoTrobada, PosicioForaRang{
        try {
            // Eliminar el vertex de la taula de vertexs
            NodeVertex vertexAEsborrar = taulaVertexs.consultar(key);
            Arista arista = vertexAEsborrar.arista;
            while (arista!=null) {
                if (arista.v1 == vertexAEsborrar) {
                    esborrarAresta(vertexAEsborrar.key, arista.v2);
                } else if (arista.v2 == vertexAEsborrar){
                    esborrarAresta(vertexAEsborrar.key, arista.v1);
                }
                arista = arista.seguent;
            }
        // Una vez eliminadas todas las aristas asociadas al vértice, eliminar el vértice de la tabla de vértices
        if (numElemComunitat(vertexAEsborrar) <= 1) {
            numComunitats--; //si solament tenim un unic element en la comunitat, optimitzem per reassignar comunitats
            System.out.println("optimitzant graf...");
            optimitzar(600000);
        }
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
    public void inserirAresta(K v1, K v2) throws VertexNoTrobat {
        try {
            if (taulaVertexs.buscar(v1) && taulaVertexs.buscar(v2)) {
                NodeVertex vertex1 = taulaVertexs.consultar(v1);
                NodeVertex vertex2 = taulaVertexs.consultar(v2);
                if (!existeixAresta(v1, v2)) {
                    Arista novaArista1 = new Arista(vertex1.key, vertex2.key);
                    novaArista1.seguent = vertex1.arista; // Conectar la nueva arista al inicio de la lista de aristas de vertex1
                    vertex1.arista = novaArista1;

                    Arista novaArista2 = new Arista(vertex2.key, vertex1.key);
                    novaArista2.seguent = vertex2.arista; // Conectar la nueva arista al inicio de la lista de aristas de vertex2
                    vertex2.arista = novaArista2;

                    // Actualizar el puntero fant solo si es el primer enlace
                    if (fant.seguent == null) {
                        fant.seguent = novaArista1;
                    }
                    numAristas++;
                //si ya existe la arist ignoramos
                }
            } else {
                throw new VertexNoTrobat();
            }
        } catch (ElementNoTrobat e) {
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
                if (actual.v1.equals(v1)) {
                    vecinos.inserir(actual.v2);
                } else {
                    vecinos.inserir(actual.v1);
                }
                actual = actual.seguent;
            }
            return vecinos;
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }

    }

    
    public double modularitat() throws PosicioForaRang, ElementNoTrobat, VertexNoTrobat{
        double q = 0.0;
        ILlistaGenerica<K> ll = taulaVertexs.obtenirClaus();
        for (int i = 0; i < ll.numElements(); i++) { 
            K key = ll.consultar(i);
            double m = numAristas;
            int comunitat = taulaVertexs.consultar(key).comunitat;
            double L_c = (double) numArestesComunitat(comunitat); // Número de aristas dentro de la comunidad
            double k_c = (double) grausComunitat(comunitat); // Suma de los grados de los nodos en la comunidad
            double e = L_c / m;
            double d = (k_c / (2.0 * m)) * (k_c / (2.0 * m)); // Utiliza 2.0 para asegurar la división decimal
    
            q = q + (e - d);
        }
        modularitat = q;
        return q;
    }
    public double getModularitat() throws PosicioForaRang, ElementNoTrobat, VertexNoTrobat{
        if (modularitat == -100.0) {//modularitat predefinida
            modularitat = modularitat();
        }
        return this.modularitat;
    }

    // Metode per a comptar quantes arestes te una comunitat en total
    public int numArestesComunitat(int comunitat) {
        int num = 0;
        try {
            Arista act = fant;
            // Iterar sobre cada vertex per comprovar les seves arestes
            while(act.seguent!=null) {
                act = act.seguent;
                NodeVertex v1 = taulaVertexs.consultar(act.v1);
                NodeVertex v2 = taulaVertexs.consultar(act.v2);
                // Comptar l'aresta només si ambdós extrems de l'aresta estan a la mateixa comunitat
                if (v1.comunitat == comunitat && v2.comunitat == comunitat) {
                    num++;
                }
            }

        } catch (ElementNoTrobat e) {
            System.out.println("Element no trobat");
        }

        return num;
    }

    public int grausComunitat(int comunitat) throws PosicioForaRang, ElementNoTrobat, VertexNoTrobat{
        int grausComunitat = 0;
        ILlistaGenerica<K> vertexIDs = taulaVertexs.obtenirClaus();
    
        for (int i = 0; i < vertexIDs.numElements(); i++) {
            K vertexID = vertexIDs.consultar(i);
            NodeVertex vertex = taulaVertexs.consultar(vertexID);
            if (vertex.comunitat == comunitat) {
                grausComunitat += numVeins(vertexID);
            }
        }
        return grausComunitat;
    }

    public int getComunitat(K key) throws ElementNoTrobat{
        return taulaVertexs.consultar(key).comunitat;
    }


    public int numElemComunitat(NodeVertex vertexE) throws VertexNoTrobat, PosicioForaRang, ElementNoTrobat{
        int n = 0;
        ILlistaGenerica<K> ll = taulaVertexs.obtenirClaus();
        for (int i = 0; i < ll.numElements(); i++) {
            K nodeID = ll.consultar(i);
            NodeVertex vertex = taulaVertexs.consultar(nodeID);
            if (vertex.comunitat == vertexE.comunitat) {
                n++;
            }
        } 

        return n;
    }


    public void setComunitat(K key, Integer c) throws ElementNoTrobat{
        taulaVertexs.consultar(key).comunitat = c;
    }

    public void optimitzar(long milisTime) throws ElementNoTrobat, PosicioForaRang, VertexNoTrobat {
        ILlistaGenerica<K> vertexs = obtenirVertexIDs();
        double modularitatInicial = modularitat();
        Instant startTime = Instant.now();
        int iteracions = 0;
    
        while (Duration.between(startTime, Instant.now()).toMillis() < milisTime && iteracions < 5) {
            for (int i = 0; i < vertexs.numElements(); i++) {
                if (Duration.between(startTime, Instant.now()).toMillis() >= milisTime) {
                    System.out.println("TEMPS MÀXIM");
                    // Terminar la optimización si se ha alcanzado el tiempo límite
                    return;
                }
    
                K vertexActual = vertexs.consultar(i);
                int comunitatActual = getComunitat(vertexActual);
                double millorGanancia = 0.0;
                int millorComunitat = comunitatActual;
                ILlistaGenerica<K> veins = obtenirVeins(vertexActual);
    
                for (int j = 0; j < veins.numElements(); j++) {
                    if (Duration.between(startTime, Instant.now()).toMillis() >= milisTime) {
                        // Terminar la optimización si se ha alcanzado el tiempo límite
                        System.out.println("TEMPS MÀXIM");
                        return;
                    }
    
                    K vei = veins.consultar(j);
                    int comunitatVei = getComunitat(vei);
    
                    if (comunitatVei != comunitatActual) {
                        setComunitat(vertexActual, comunitatVei);
                        double newModularitat = modularitat();
                        double gananciaActual = newModularitat - modularitatInicial;
    
                        if (gananciaActual > millorGanancia) {
                            millorGanancia = gananciaActual;
                            millorComunitat = comunitatVei;
                        }
    
                        setComunitat(vertexActual, comunitatActual);
                    }
                }
    
                if (millorComunitat != comunitatActual) {
                    setComunitat(vertexActual, millorComunitat);
                    
                }
                modularitatInicial = modularitat();
            }
            iteracions++;
        }
        modularitat(); // Calcula la modularidad final
    }
    
    


    ////////////////////////////////////////////////////////////////////////////////////
    // Metodes OPCIONALS - Si es fa la part obligatoria la nota maxima sera un 8
    // Si s'implementen aquests dos metodes correctament es podra obtenir fins a 2 punts addicionals

    // Metode per a obtenir tots els nodes que estan connectats a un vertex
    // es a dir, nodes als que hi ha un cami directe des del vertex
    // El node que es passa com a parametre tambe es retorna dins de la llista!
    public ILlistaGenerica<K> obtenirNodesConnectats(K v1) throws VertexNoTrobat, PosicioForaRang {
        try {
            ILlistaGenerica<K> nodesConnectats = new LlistaOrdenada<>();
            nodesConnectats.inserir(v1);
            
            ILlistaGenerica<K> veins = obtenirVeins(v1);
            for (int i = 0; i < veins.numElements(); i++) {
                nodesConnectats.inserir(veins.consultar(i));
            }
            
            return nodesConnectats;
        } catch (VertexNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }
    

    @Override
    public ILlistaGenerica<K> obtenirComponentConnexaMesGran() {
        throw new UnsupportedOperationException("Unimplemented method 'obtenirComponentConnexaMesGran'");
    }


}
