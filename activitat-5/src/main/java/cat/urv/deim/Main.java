package cat.urv.deim;

import java.io.IOException;
import java.util.Scanner;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.PosicioForaRang;
import cat.urv.deim.exceptions.VertexNoTrobat;

public class Main {

    public static void main(String[] args) throws NumberFormatException, IOException, PosicioForaRang, ElementNoTrobat, VertexNoTrobat {
        Scanner scanner = new Scanner(System.in);
        int opcio;
        String filename = "";
        boolean creat = false;
        GrafPersones graf = null;

        do {
            System.out.println("OPTIMITZACIO DE GRAFS:");
            System.out.print("Escull una opcio: ");
            System.out.println( "\n1. Carregar un graf d'un fitxer determinat");
            System.out.println("2. Calcular la modularitat actual");
            System.out.println("3. Optimitzar el graf");
            System.out.println("4. Sortir SENSE GUARDAR (PREMER 3 PER OPTIMITZAR I GUARDAR)");
            opcio = Integer.parseInt(scanner.nextLine());

            switch (opcio) {
                case 1:             //Carregar
                    if (creat){
                        System.out.println("Vols sobreescriure el graf existent? S/N");
                        String resposta = scanner.nextLine();
                        if (resposta.equalsIgnoreCase("S")) creat = false;
                    }
                    if (!creat){
                        System.out.println("Introdueix el nom del fitxer del qual en vols llegir el graf: ");
                        filename = scanner.nextLine();
                        graf = new GrafPersones(1500, filename);       
                        creat = true;
                    }
                    break;
                case 2:
                    if (creat) System.out.println("La modularitat del graf es: " + graf.modularitat());     //Calcular modularitat
                    else System.out.println("Primer has de carregar un graf.");
                    break;
                case 3:
                    if (creat) {
                        System.out.println("Optimitzant...");
                        graf.optimitzar();              //Optimitzar graf
                        graf.escriure(filename);        //Guardar graf
                        System.out.println("Graf optimitzat.");
                        System.out.println("Graf guardat en el fitxer");

                    } else System.out.println("Primer has de carregar un graf.");
                    break;
                case 4:
                    System.out.println("Sortint del programa...");
                    break;
                default:
                    System.out.println("Opcio no valida. Torna a introduir un numero.");
                    break;
            }
        } while (opcio != 4);

        scanner.close();
    }
}