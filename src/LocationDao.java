import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LocationDao {
    private Connection conn;

    public LocationDao() {
        String url = "jdbc:sqlite:/C:\\Users\\Pharell\\Documents\\myPrograms\\Java\\JavaFx\\location_voitures.db";
        try {
            this.conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public List<Location> getAllLocations() throws SQLException {
        List<Location> locations = new ArrayList<>();
        String query = "SELECT * FROM location";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            String nomClient = rs.getString("nom_client");
            String adresseClient = rs.getString("adresse_client");
            String modeleVoitureLouee = rs.getString("modele_voiture_louee");
            String immatVoitureLouee = rs.getString("immat_voiture_louee");
            LocalDate dateDebut = rs.getDate("date_debut").toLocalDate();
            LocalDate dateFin = rs.getDate("date_fin").toLocalDate();
            double montantPaye = rs.getDouble("montant_paye");
            Location location = new Location(nomClient, adresseClient, modeleVoitureLouee, immatVoitureLouee, dateDebut, dateFin, montantPaye);
            locations.add(location);
        }
        st.close();
        return locations;
    }

    public void addLocation(Location location) throws SQLException {
        String query = "INSERT INTO location (nom_client, adresse_client, modele_voiture_louee, immat_voiture_louee, date_debut, date_fin, montant_paye) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, location.getNomClient());
        pst.setString(2, location.getAdresseClient());
        pst.setString(3, location.getModeleVoitureLouee());
        pst.setString(4, location.getImmatVoitureLouee());
        pst.setDate(5, Date.valueOf(location.getDateDebut()));
        pst.setDate(6, Date.valueOf(location.getDateFin()));
        pst.setDouble(7, location.getMontantPaye());
        pst.execute();
        pst.close();
    }

    public void removeLocation(Location location) throws SQLException {
        String query = "DELETE FROM location WHERE nom_client = ? AND modele_voiture_louee = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, location.getNomClient());
        pst.setString(2, location.getModeleVoitureLouee());
        pst.executeUpdate();
        pst.close();
    }

    public void updateLocation(Location location) throws SQLException {
        String query = "UPDATE location SET adresse_client = ?, date_debut = ?, date_fin = ?, montant_paye = ? WHERE nom_client = ? AND modele_voiture_louee = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, location.getAdresseClient());
        pst.setDate(2, Date.valueOf(location.getDateDebut()));
        pst.setDate(3, Date.valueOf(location.getDateFin()));
        pst.setDouble(4, location.getMontantPaye());
        pst.setString(5, location.getNomClient());
        pst.setString(6, location.getModeleVoitureLouee());
        pst.executeUpdate();
        pst.close();
    }
}
