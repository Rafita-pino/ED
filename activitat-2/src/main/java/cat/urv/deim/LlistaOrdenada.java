package cat.urv.deim;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.PosicioForaRang;

public class LlistaOrdenada<E extends Comparable<E>> implements ILlistaGenerica<E> {

    public int numElem;
    public Node fantasma;
    public class Node {
        private E info;
        private Node nextNode;

        public Node(E e){
            info = e;
            nextNode = null;
        }
    }

    public LlistaOrdenada(){
        fantasma = new Node(null);
        numElem = 0;

    }
    @Override
    public void inserir(E e) {
        Node newN = new Node(e);
        Node actual = fantasma.nextNode;
        Node anterior = fantasma;
        if (actual == null || e.compareTo(actual.info) < 0){ //primer element o mes petit de tots
            newN.nextNode = fantasma.nextNode;
            fantasma.nextNode = newN;
            numElem++;
        }else{
            while (actual != null && actual.info.compareTo(e) < 0) {
                anterior = actual;
                actual = actual.nextNode;
            }
            newN.nextNode = actual;
            anterior.nextNode = newN;
            numElem++;
        }
    }


    @Override
    public void esborrar(E e) throws ElementNoTrobat {
        Node anterior = fantasma;
        Node actual = fantasma.nextNode;
        while (actual != null && !actual.info.equals(e)) {
            anterior = actual;
            actual = actual.nextNode;
        }
        if (actual == null) {
            throw new ElementNoTrobat();
        }else if (anterior == fantasma) {
            fantasma.nextNode = actual.nextNode;
        }
        anterior.nextNode = actual.nextNode;
        numElem -=1;
    }

    @Override
    public E consultar(int pos) throws PosicioForaRang {

        if (pos<0 || pos >= numElem) {
            throw new PosicioForaRang();
        }else{
            Node actual = fantasma.nextNode;
            for (int i = 0; i < pos; i++) {
                actual = actual.nextNode;
            }
            return (E) actual.info;
        }
    }

    @Override
    public int buscar(E e) throws ElementNoTrobat {
        int inicio = 0, fin = numElem -1;
        int medio = (inicio + fin) /2;

        while (inicio<=fin) {
            try {
                medio = (inicio + fin) /2;
                E infoMedio = consultar(medio);
                if (infoMedio.equals(e)) {
                    return medio;
                }else if (infoMedio.compareTo(e)<0){ //infoMedio < e
                    inicio = medio + 1;
                }else{
                    fin = medio -1;//infoMedio > e
                }

            } catch (PosicioForaRang ex) {
                fin = medio-1;
            }
        }
        throw new ElementNoTrobat();
    }

    @Override
    public boolean existeix(E e) {
        try {
            int pos = buscar(e);
            return true;
        } catch (ElementNoTrobat ex) {
            return false;
        }

    }


    @Override
    public boolean esBuida() {
        return (numElem == 0);
    }

    @Override
    public int numElements() {
        return numElem;
    }

    @Override
    public Object[] elements() {
        Object[] array = new Object[numElem];
        Node actual = fantasma.nextNode;
        for (int i = 0; i < numElem; i++) {
            array[i] = actual.info;
            actual=actual.nextNode;
        }
        return array;
    }

}
