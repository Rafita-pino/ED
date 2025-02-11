package cat.urv.deim;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateGraphML {

    public static void main(String[] args) {
        String filename = "GrafProva3000.graphml";
        int numNodes = 3000;
        int numEdges = 20000; // Ajusta este número según tus necesidades

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\">\n");
            writer.write("  <key id=\"comunitat\" for=\"node\" attr.name=\"comunitat\" attr.type=\"string\"/>\n");
            writer.write("  <graph id=\"Graph\" edgedefault=\"undirected\">\n");

            // Generar nodos
            for (int i = 0; i < numNodes; i++) {
                writer.write("    <node id=\"" + i + "\"/>\n");
            }

            // Generar aristas aleatorias
            Random random = new Random();
            for (int i = 0; i < numEdges; i++) {
                int source = random.nextInt(numNodes);
                int target = random.nextInt(numNodes);
                if (source != target) { // Evitar bucles
                    writer.write("    <edge source=\"" + source + "\" target=\"" + target + "\"/>\n");
                }
            }

            writer.write("  </graph>\n");
            writer.write("</graphml>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Archivo GraphML generado: " + filename);
    }
}

