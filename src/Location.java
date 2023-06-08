import java.time.LocalDate;

public class Location {
    private String nomClient;
    private String adresseClient;
    private String modeleVoitureLouee;
    private String immatVoitureLouee;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private double montantPaye;

    public Location(String nomClient, String adresseClient, String modeleVoitureLouee, String immatVoitureLouee,
                    LocalDate dateDebut, LocalDate dateFin, double montantPaye) {
        this.nomClient = nomClient;
        this.adresseClient = adresseClient;
        this.modeleVoitureLouee = modeleVoitureLouee;
        this.immatVoitureLouee = immatVoitureLouee;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montantPaye = montantPaye;
    }

    public Location(String text, String text2, Voiture value, LocalDate dateDebut2, LocalDate dateFin2,
            double parseDouble) {
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getAdresseClient() {
        return adresseClient;
    }

    public void setAdresseClient(String adresseClient) {
        this.adresseClient = adresseClient;
    }

    public String getModeleVoitureLouee() {
        return modeleVoitureLouee;
    }

    public void setModeleVoitureLouee(String modeleVoitureLouee) {
        this.modeleVoitureLouee = modeleVoitureLouee;
    }

    public String getImmatVoitureLouee() {
        return immatVoitureLouee;
    }

    public void setImmatVoitureLouee(String immatVoitureLouee) {
        this.immatVoitureLouee = immatVoitureLouee;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public double getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(double montantPaye) {
        this.montantPaye = montantPaye;
    }
}
