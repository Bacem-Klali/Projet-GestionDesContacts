package contactmanager;

public class Contact {
    private int id;
    private String nom;
    private String telephone;
    private String email;

    public Contact(String nom, String telephone, String email) {
        this.nom = nom;
        this.telephone = telephone;
        this.email = email;
    }

    public Contact(int id, String nom, String telephone, String email) {
        this.id = id;
        this.nom = nom;
        this.telephone = telephone;
        this.email = email;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getTelephone() { return telephone; }
    public String getEmail() { return email; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return nom + " - " + telephone + " - " + email;
    }
}
