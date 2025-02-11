package cat.urv.deim;

import cat.urv.deim.exceptions.*;

import java.io.*;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;




public class GrafPersones {
    Graf<Integer, Persona> graf;

    public GrafPersones(int mida){
        graf = new Graf<Integer, Persona>(mida);
    }

    
    public GrafPersones(int mida, String filename) throws NumberFormatException, IOException, VertexNoTrobat {
        graf = new Graf<Integer, Persona>(mida);
        if (filename.endsWith(".graphml")) {
            loadGraphML(filename);
        } else if (filename.endsWith(".net")) {
            loadPajekNet(filename);
        } else {
            throw new IllegalArgumentException("Unsupported file format");
        }
    }


    public void loadGraphML(String filename){
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            File inputFile = new File(filename);  // Carrega el fitxer XML
            Persona p;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();  // Crea una nova instància de DocumentBuilderFactory
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();  // Crea un nou DocumentBuilder
            Document doc = dBuilder.parse(inputFile);  // Analitza el fitxer XML i obtenim doc
            doc.getDocumentElement().normalize();  // Normalitza el Document XML (fora espais innecesaris i facilitacio de la lectura)
            
            NodeList nodeList = doc.getElementsByTagName("node");// creamos lista de nodos del document XML
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element node = (Element) nodeList.item(i);  
                int id_persona = Integer.parseInt(node.getAttribute("id"));  //cogemos el atributo "id" del node i
                int edat = -1;
                String nom = null;
                String cognom = null;
                int alsada = -1;
                int pes = -1;
                //inicializamos los valores por defecto para trabajar con personas (las clases ya estaban adaptadas a ellas y es mas facil)
                p = new Persona(id_persona, edat, nom, cognom, alsada, pes);
                graf.inserirVertex(id_persona, p);  // Afegeix el node (vertex) al graf
            }

            
            NodeList edgeList = doc.getElementsByTagName("edge"); // cogemos todas las arestas del documento XML
            for (int i = 0; i < edgeList.getLength(); i++) {
                Element edge = (Element) edgeList.item(i);  
                int k1 = Integer.parseInt(edge.getAttribute("source"));  // atribut "source" (V1) de l'aresta
                int k2 = Integer.parseInt(edge.getAttribute("target"));  // atribut "target" (v2) de l'aresta
                try {
                    graf.inserirAresta(k1, k2);  // Afegeix l'aresta al graf
                } catch (VertexNoTrobat e) {
                    e.printStackTrace();  
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }
    

    public void loadPajekNet(String filename) throws VertexNoTrobat {
        try (BufferedReader r = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean vertexsSection = false;
            boolean arestasSection = false;
            int numVertexs = 0, i = 0;
            Persona p;
    
            while ((line = r.readLine()) != null) {
                line = line.trim();
                String[] parts;
    
                if (line.startsWith("*Vertices")) {
                    parts = line.split("\\s+");
                    numVertexs = Integer.parseInt(parts[1]);
                    vertexsSection = true;
                    arestasSection = false;
                } else if (line.startsWith("*Edges") || line.startsWith("*Arcs")) {
                    vertexsSection = false;
                    arestasSection = true;
                } else if (vertexsSection && i < numVertexs) {
                    parts = line.split("\\s+");
                    if (parts.length >= 2) {
                        int id_persona = Integer.parseInt(parts[0]);  //cogemos el atributo "id" del node i
                        int edat = -1;
                        String nom = parts[1]; //nombre del vertice
                        String cognom = null;
                        int alsada = -1;
                        int pes = -1;
                        //inicializamos los valores por defecto para trabajar con personas (las clases ya estaban adaptadas a ellas y es mas facil)
                        p = new Persona(id_persona, edat, nom, cognom, alsada, pes);
                        graf.inserirVertex(id_persona, p);  // Afegeix el node (vertex) al graf
                    }
                    i++;
                } else if (arestasSection) {
                    parts = line.split("\\s+");
                    if (parts.length >= 2) {
                        int keyV1 = Integer.parseInt(parts[0]);
                        int keyV2 = Integer.parseInt(parts[1]);
                        graf.inserirAresta(keyV1, keyV2);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    


    public void escriure(String filename) throws ElementNoTrobat, VertexNoTrobat, PosicioForaRang, IOException { //TODO revisar todo
        graf.modularitat();//actualitzar modularitat actual
        if (filename.endsWith(".graphml")) {
            writeGraphml(filename);
        } else if (filename.endsWith(".net")) {
            writePajekNet(filename);
        } 
        
    }


    public void writeGraphml(String filename) throws ElementNoTrobat, VertexNoTrobat{
        filename = filename.replace(".graphml", "");
        try (BufferedWriter w = new BufferedWriter(new FileWriter(filename + "_" + graf.getModularitat() + ".graphml"))) {
            // Escribir el encabezado y la definición de la clave "comunitat"
            w.write("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\">\n");
            w.write(" <key id=\"comunitat\" for=\"node\" attr.name=\"comunitat\" attr.type=\"string\"/>\n");
            w.write(" <graph id=\"G\" edgedefault=\"undirected\">\n");
            ILlistaGenerica<Integer> ll = graf.obtenirVertexIDs();
            
            // Escribir los nodos con sus comunidades
            for (int i = 0; i < ll.numElements(); i++) {
                int idVertex = ll.consultar(i);
                int comunitat = graf.getComunitat(idVertex); // Obtener la comunidad del vértice
                w.write("  <node id=\"" + idVertex + "\">\n");
                w.write("   <data key=\"comunitat\">" + comunitat + "</data>\n");
                w.write("  </node>\n");
            }
    
            // Escribir las aristas
            for (int i = 0; i < ll.numElements(); i++) {
                int idVertex = ll.consultar(i);
                ILlistaGenerica<Integer> arestes = graf.obtenirVeins(idVertex);
                for (int j = 0; j < arestes.numElements(); j++) {
                    int idVeí = arestes.consultar(j);
                    if (idVertex < idVeí) { // Para evitar duplicar aristas, escribir solo una vez cada arista
                        w.write("  <edge source=\"" + idVertex + "\" target=\"" + idVeí + "\"/>\n");
                    }
                }
            }
    
            // Cerrar etiquetas de graph y graphml
            w.write(" </graph>\n");
            w.write("</graphml>\n");
    
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PosicioForaRang e) {
            e.printStackTrace();
        }
    }


    public void writePajekNet(String filename) throws IOException, PosicioForaRang, ElementNoTrobat, VertexNoTrobat{
        filename = filename.replace(".net", "");
        try (BufferedWriter w = new BufferedWriter(new FileWriter(filename+"_"+ graf.getModularitat() +".clu"))) {
            ILlistaGenerica<Integer> ll = graf.obtenirVertexIDs();
            w.write("*Vertices "+ll.numElements()+"\n");

            for (int i = 0; i < ll.numElements(); i++) {
                int idVertex = ll.consultar(i);
                int comunitat = graf.getComunitat(idVertex); // Obtener la comunidad del vértice
                w.write(comunitat + "\n"); // Escribir la comunidad en el archivo
            }
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

    /*public int intensitatAmistat(Persona p1, Persona p2) throws ElementNoTrobat {
        try {
            return graf.consultarAresta(p1.getId_persona(), p2.getId_persona());
        } catch (Exception e) {
            throw new ElementNoTrobat();
        }
    }*/

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

    public double modularitat() throws PosicioForaRang, ElementNoTrobat, VertexNoTrobat{
        return graf.modularitat(); //calcula
    }

    public double getModularitat() throws PosicioForaRang, ElementNoTrobat, VertexNoTrobat{
        return graf.getModularitat(); //retorna resultat guardat
    }

    public void optimitzar() throws ElementNoTrobat, PosicioForaRang, VertexNoTrobat{
        System.out.println("Optimitzant graf, aquest procès pot durar fins 10 minuts...");
        graf.optimitzar(600000);
    }

}
