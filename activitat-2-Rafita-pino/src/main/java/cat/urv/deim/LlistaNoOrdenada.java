package cat.urv.deim;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.PosicioForaRang;

public class LlistaNoOrdenada<E extends Comparable<E>> implements ILlistaGenerica<E> {
    public int numElem;
    public Node fantasma;
    public class Node{
        private E info;
        private Node nextNode;

        public Node(E e){
            info = e;
            nextNode = null;
        }
    }
    public LlistaNoOrdenada(){
        fantasma = new Node(null); //creem element fantasma
        numElem = 0;

    }

    @Override
    public void inserir(E e) {
        Node newNode = new Node(e);
        newNode.nextNode=fantasma.nextNode;
        fantasma.nextNode=newNode;
        numElem++;
    }
    @Override
    public void esborrar(E e) throws ElementNoTrobat {
        if (esBuida()) {
            throw new ElementNoTrobat();
        }else{
            Node actual = fantasma.nextNode, ant=null;
            while (actual != null && !actual.info.equals(e)){
                ant = actual;
                actual = actual.nextNode;
            }
            if (ant == null) {
                fantasma=actual.nextNode; //eleminar el primer de la llista
            }else if (actual != null && actual.info.equals(e)) {
                ant.nextNode = actual.nextNode;
            }else{
                throw new ElementNoTrobat();
            }
            numElem -= 1;
        }
    }

    @Override
    public E consultar(int pos) throws PosicioForaRang {
        if (pos<numElem || pos > numElem) {
            throw new PosicioForaRang();
        }else{
            Node actual = fantasma;
            for (int i = 0; i < pos; i++) {
                actual = actual.nextNode;
            }
            return (E) actual.info;
        }


    }

    @Override
    public int buscar(E e) throws ElementNoTrobat {
        int pos = 0;
        Node actual = fantasma.nextNode;
        while (!actual.info.equals(e) && pos <numElem){
            pos++;
            actual=actual.nextNode;
        }
        if (pos>=numElem) {
            throw new ElementNoTrobat();
        }
        return pos;

    }

    @Override
    public boolean existeix(E e) {
        boolean trobat = false;
        if (esBuida()) {
            return trobat;
        }else{
            Node actual = fantasma.nextNode;
            while (actual != null && !actual.info.equals(e)){
                actual = actual.nextNode;
            }
            if (actual!=null  && actual.info.equals(e)) {
                trobat = true;
            }
            return trobat;
        }
    }

    @Override
    public boolean esBuida() {
        return (numElem==0);

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
