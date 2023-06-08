public class Voiture {
    private String modele;
    private String marque;
    private String immatriculation;
    private boolean estLouee;

    public Voiture(String modele, String marque, String immatriculation, boolean estLouee) {
        this.modele = modele;
        this.marque = marque;
        this.immatriculation = immatriculation;
        this.estLouee = estLouee;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public boolean isEstLouee() {
        return estLouee;
    }

    public void setEstLouee(boolean estLouee) {
        this.estLouee = estLouee;
    }
}
