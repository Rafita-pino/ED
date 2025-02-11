package cat.urv.deim;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.PilaBuida;
import cat.urv.deim.exceptions.PilaPlena;


public class PilaDinamica implements TADPila {
    private Node cim;
    private int numElem;
    private int maxElem;

    private class Node {
        private Persona info;
        private Node nextNode;

        public Node(Persona p){
            info = p;
            nextNode = cim;
        }

    }

    public PilaDinamica(int maxElem){
        cim = new Node(null);
        this.maxElem = maxElem;
        numElem = 0;
    }
    @Override
    public void apilar(Persona p) throws PilaPlena {
        if (esPlena()){
            throw new PilaPlena();
        }else{
            Node newNode = new Node(p);
            cim = newNode;
            numElem++;
        }
    }

    @Override
    public void desapilar() throws PilaBuida {
        if (esBuida()) {
            throw new PilaBuida();
        }else{
            cim = cim.nextNode;
            numElem -= 1;
        }

    }

    @Override
    public Persona cim() throws PilaBuida {
        if (esBuida()){
            throw new PilaBuida();
        }else{
            return cim.info;
        }

    }

    @Override
    public int numElem() {
        return numElem;
    }

    @Override
    public boolean esBuida() {
        return (numElem==0);
    }

    @Override
    public boolean esPlena() {
        return (numElem == maxElem);
    }

    @Override
    public Persona seguent(Persona p) throws ElementNoTrobat {
        if (esBuida()) {
            throw new ElementNoTrobat();
        }else{
            int i = 0;
            Node actual = cim;
            Node anterior = null;
            while (i <= numElem) {
                if (actual.info == p) {
                    if (anterior != null) {
                        return anterior.info;
                    }else{
                        return null;
                    }
                }
                anterior = actual;
                actual = actual.nextNode;
                i++;
            }
            throw new ElementNoTrobat();
        }
    }

    @Override
    public Persona anterior(Persona p) throws ElementNoTrobat {
        if (esBuida()) {
            throw new ElementNoTrobat();
        }else{
            int i = 0;
            Node actual = cim;
            while (i<=numElem) {
                if (actual.info.equals(p)) {
                    if (actual.equals(cim)) {
                        return null;
                    }else{
                        return actual.nextNode.info;
                    }

                }
                actual = actual.nextNode;
                i++;
            }
            throw new ElementNoTrobat();
        }
    }
}
