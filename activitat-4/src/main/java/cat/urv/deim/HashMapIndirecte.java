/*
 * Buneno, despues de tener que cambiarlo todo porque el dia de antes en el lab me di cuenta de que estaba mal estructurado, no he podido hacer que funcione todo,
 * pero lo principal si que funciona.
 */
package cat.urv.deim;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import cat.urv.deim.exceptions.ElementNoTrobat;

public class HashMapIndirecte<K extends Comparable<K>, V extends Comparable<V>> implements IHashMap<K, V>{

    public class NodeHash {
        private V info;
        private NodeHash nextNode;
        private K key;

        public NodeHash(V value, K k){
            info = value;
            key = k;
            nextNode = null;

        }

    }
    public ArrayList<NodeHash> llista;
    public NodeHash fant = new NodeHash(null, null);
    public int numElem, midaFixe, indexAnt = -1;
    public static final float FACTOR_CARREGA = (float) 0.75;

    public HashMapIndirecte (int mida){
        if (mida<0) {
            throw new IllegalArgumentException();
        }
        midaFixe = mida;
        llista = new ArrayList<>(midaFixe);
        for (int i = 0; i < midaFixe; i++) {
            llista.add(null);
        }

        numElem = 0;
    }
    // Metode per insertar un element a la taula. Si existeix un element amb aquesta clau s'actualitza el valor
    @Override
    public void inserir(K key, V value) {
        int index = key.hashCode() % midaFixe; // % fa modul
        if (factorCarrega()> FACTOR_CARREGA){
            int novaMidaFixe = midaFixe*2;
            ArrayList<NodeHash> novaLlista = new ArrayList<>(novaMidaFixe);
            for (int i = 0; i < novaMidaFixe; i++) {
                novaLlista.add(null);
            }
            indexAnt = -1;
            fant = new NodeHash(null, null);
            for (int i = 0; i < midaFixe; i++) {//redistribuim les dades en les noves posicions
                NodeHash actual = llista.get(i);
                if (actual != null) {
                    int index2 = actual.key.hashCode() % novaMidaFixe;
                    novaLlista.set(index2, actual);
                    if (indexAnt != -1){
                        novaLlista.get(indexAnt).nextNode = actual;
                    }else if(indexAnt == -1){
                        fant.nextNode = actual;
                    }
                    indexAnt = index2;
                }
            }
            midaFixe = novaMidaFixe;
            llista = novaLlista;
        }
        NodeHash newNode = new NodeHash(value, key);
        NodeHash actual = llista.get(index);
        while (actual != null) {
            if (newNode.key.equals(actual.key)) {
                actual.info = newNode.info;
                return;
            }else{//modificacio respecte lanterior tasca
                actual=actual.nextNode;
                index++;
                actual = llista.get(index);
            }
        }
        llista.set(index, newNode);
        if (indexAnt != -1){
            llista.get(indexAnt).nextNode = newNode;
        }else if(indexAnt == -1){
            fant.nextNode = newNode;
        }
        indexAnt = index;
        numElem++;
    }


    // Metode per a obtenir element de K
    @Override
    public V consultar(K key) throws ElementNoTrobat {
        int index = key.hashCode() % midaFixe;
        NodeHash actual = llista.get(index);
        while (actual != null) {
            if (actual.key.equals(key)) {
                return actual.info;
            }
            actual = actual.nextNode;
        }
        throw new ElementNoTrobat();
    }

    // Metode per a esborrar un element de la taula de hash
    @Override
    public void esborrar(K key) throws ElementNoTrobat {
        int index = key.hashCode() % midaFixe;
        NodeHash actual = llista.get(index);
        NodeHash anterior = null;
        boolean trobat = false;
        if (actual == null) {
            throw new ElementNoTrobat();
        }
        while (actual!=null && !trobat) {
            if (actual.key.equals(key)) {
                if (anterior==null) {//es el primer
                    fant = actual.nextNode;
                    llista.set(index, null);
                }else{
                    anterior.nextNode=actual.nextNode;
                }
                numElem -=1;
                trobat = true;
            }
            if (!trobat) {
                anterior=actual;
                actual=actual.nextNode;
            }
        }
        if (!trobat) {
            throw new ElementNoTrobat();//si arribem aqui, vol dir que el element és null i no existeix.
        }

    }


    // Metode per a comprovar si un element esta a la taula de hash
    @Override
    public boolean buscar(K key) {
        int index = key.hashCode() % midaFixe;
        NodeHash actual = llista.get(index);
        boolean trobat = false;
        while (actual!=null && !trobat) {
            if (actual.key.equals(key)) {
                trobat = true;
            }
            actual=actual.nextNode;
        }
        return trobat;
    }



    // Metode per a comprovar si la taula te elements
    @Override
    public boolean esBuida() {
        return numElem==0;
    }

    // Metode per a obtenir el nombre d'elements de la llista
    @Override
    public int numElements() {
        return numElem;
    }

    // Metode per a obtenir les claus de la taula
    @Override
    public ILlistaGenerica<K> obtenirClaus() {
        ILlistaGenerica<K> claus = new LlistaOrdenada<K>();
        NodeHash aux = fant.nextNode;
        while (aux != null) {
            claus.inserir(aux.key);
            aux = aux.nextNode;
        }
        return claus;
    }
    // Metode per a saber el factor de carrega actual de la taula
    @Override
    public float factorCarrega() {
        return ((float) numElem / (float)midaFixe);
    }

    // Metode per a saber la mida actual de la taula (la mida de la part estatica)
    @Override
    public int midaTaula() {
        return midaFixe;
    }

    // Metode per a poder iterar pels elements de la taula
    // IMPORTANT: El recorregut s'ha de fer de forma ORDENADA SEGONS LA CLAU
    @Override
    public Iterator<V> iterator() {
        return new LlistaIterator();
    }
    private class LlistaIterator implements Iterator<V> {
        private int /*actualIndex,*/ contador = 0;
        private NodeHash actual;

        public LlistaIterator() {
            /*actualIndex = 0;
            actual = null;
            while (actualIndex < midaFixe && llista.get(actualIndex) == null) {
                actualIndex++;
            }
            if (actualIndex < midaFixe) {
                actual = llista.get(actualIndex);
            }*/
            actual = fant;
        }

        @Override
        public boolean hasNext() {
            return contador<numElem;
        }

        @Override
        public V next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            contador++;
            /*if (actual.nextNode!=null) {
                actual = actual.nextNode;
            } */
            return actual.nextNode.info;
        }
    }
}
