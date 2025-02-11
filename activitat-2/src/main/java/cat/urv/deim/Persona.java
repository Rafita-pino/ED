package cat.urv.deim;

public class Persona implements Comparable<Persona>{
    private int id_persona;
    private String nom;
    private String cognom;
    private int edat;
    private int pes;
    private int alsada;

    public Persona(int id_persona, int edat, String nom, String cognom, int alsada, int pes) {
        this.id_persona = id_persona;
        this.nom = nom;
        this.cognom = cognom;
        this.edat = edat;
        this.pes = pes;
        this.alsada = alsada;
    }

    public int getId_persona() {
        return id_persona;
    }

    public String getNom() {
        return nom;
    }

    public String getCognom() {
        return cognom;
    }

    public int getEdat() {
        return edat;
    }

    public int getPes() {
        return pes;
    }

    public int getAlsada() {
        return alsada;
    }

    @Override
    public int compareTo(Persona p) {
        if (this.getCognom().equals(p.getCognom())){
            return this.nom.compareTo(p.nom);
        }
        return this.cognom.compareTo(p.getCognom());
    }

    // Dues persones son iguals si tenen el mateix ID
    public boolean equals(Persona p) {
        return (this.getId_persona()==p.getId_persona());
    }
}

