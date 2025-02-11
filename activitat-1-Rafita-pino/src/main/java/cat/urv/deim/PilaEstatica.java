package cat.urv.deim;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.PilaBuida;
import cat.urv.deim.exceptions.PilaPlena;

public class PilaEstatica implements TADPila {
    private int numElem, maxElem;
    private Persona[] taula;

    public PilaEstatica(int maxElem){
        numElem = 0;
        this.maxElem = maxElem;
        taula = new Persona[maxElem];
    }
        /* METODES PER A GESTIONAR LA PILA */

	public void apilar(Persona p) throws PilaPlena{
        if (numElem == maxElem){
            throw new PilaPlena();
        }else{
            numElem++;
            taula[numElem-1] = p;
        }

    }

	public void desapilar() throws PilaBuida{
        if (this.esBuida()){
            throw new PilaBuida();
        }else{
            numElem = numElem -1;
        }
    }

	public Persona cim() throws PilaBuida{
        if (this.esBuida()){
            throw new PilaBuida();
        }else{
            return taula[numElem-1];
        }
    }

	public int numElem(){
        return numElem;
    }

    public boolean esBuida(){
        if (numElem == 0){
            return true;
        }else return false;
    }

	public boolean esPlena(){
        if (numElem == maxElem){
            return true;
        }else return false;

    }

        /* Aquest metode retorna l'element anterior a la pila, es a dir,
     * el que fa mes temps que hi es que el que passem per parametre. Per exemple, si fessim
     * apilar(p1), apilar(p2), apilar(p3), i cridem anterior de p2 ha de retornar p1.
     * Si cridem l'anterior de p1 (l'ultim element) ha de retornar null.
     */
	public Persona anterior(Persona p) throws ElementNoTrobat{

            int i = 0;
                while (i < numElem && taula[i] != p) {
                    i++;
                }
                if (i >= numElem){
                    throw new ElementNoTrobat();
                }else if(i == 0){
                    return null;
                }else{
                    return taula[i-1];
                }



    }

    /* Aquest metode retorna l'element seguent a la pila, es a dir,
     * el que fa menys temps que hi es que el que passem per parametre. Per exemple, si fessim
     * apilar(p1), apilar(p2), apilar(p3), i cridem seguent de p2 ha de retornar p3.
     * Si cridem el seguent de p3 (el primer element) ha de retornar null.
     *
     */	public Persona seguent(Persona p) throws ElementNoTrobat{
        int i = 0;
        while (i < numElem && taula[i] != p) {
            i++;
        }
        if (i >= numElem){
            throw new ElementNoTrobat();
        }else if(i == 0){
            return null;
        }else{
            return taula[i+1];
        }


     }
}
