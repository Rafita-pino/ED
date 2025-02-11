package cat.urv.deim;

import java.util.Stack;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.PilaBuida;
import cat.urv.deim.exceptions.PilaPlena;

public class PilaStack implements TADPila {
    Stack<Persona> pila;
    int maxElem, numElem;

    PilaStack(int maxElem){
       pila = new Stack<Persona>();
       this.maxElem = maxElem;
       numElem = 0;
    }
    @Override
    public void apilar(Persona p) throws PilaPlena {
        if (numElem == maxElem) {
            throw new PilaPlena();
        }
        pila.push(p);
        numElem++;
    }

    @Override
    public void desapilar() throws PilaBuida {
        if (pila.empty()) {
            throw new PilaBuida();
        }
        pila.pop();
        numElem -= 1;
    }

    @Override
    public Persona cim() throws PilaBuida {
            if (pila.empty()) {
                throw new PilaBuida();
            }
            return pila.peek();
    }

    @Override
    public int numElem() {
        return numElem;
    }

    @Override
    public boolean esBuida() {
        return pila.empty();
    }

    @Override
    public boolean esPlena() {
        return numElem==maxElem;
    }

    @Override
    public Persona anterior(Persona p) throws ElementNoTrobat {
        if (esBuida()) {
            throw new ElementNoTrobat();
        }
        int i = pila.indexOf(p);
        if (i == -1) {
            throw new ElementNoTrobat();
        }else if(i == 0){
            return null;
        }

        return pila.get(i-1);
    }

    @Override
    public Persona seguent(Persona p) throws ElementNoTrobat {
        if (esBuida()) {
            throw new ElementNoTrobat();
        }
        int i = pila.indexOf(p);
        if (i == -1) {
            throw new ElementNoTrobat();
        }else if(i == pila.size()-1){
            return null;
        }

        return pila.get(i+1);
    }

}
