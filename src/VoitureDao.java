import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VoitureDao {
    private Connection conn;

    public VoitureDao() {
        String url = "jdbc:sqlite:/C:\\Users\\Pharell\\Documents\\myPrograms\\Java\\JavaFx\\location_voitures.db";
        try {
            this.conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ObservableList<Voiture> getAllVoitures() throws SQLException {
        ObservableList<Voiture> voitures = FXCollections.observableArrayList();
        String query = "SELECT * FROM voiture";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            String modele = rs.getString("modele");
            String marque = rs.getString("marque");
            String immatriculation = rs.getString("immatriculation");
            boolean estLouee = rs.getBoolean("est_louee");
            Voiture voiture = new Voiture(modele, marque, immatriculation, estLouee);
            voitures.add(voiture);
        }
        st.close();
        return voitures;
    }

    public void addVoiture(Voiture voiture) throws SQLException {
        String query = "INSERT INTO voiture (modele, marque, immatriculation, est_louee) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, voiture.getModele());
        pst.setString(2, voiture.getMarque());
        pst.setString(3, voiture.getImmatriculation());
        pst.setBoolean(4, voiture.isEstLouee());
        pst.execute();
        pst.close();
    }

    public void removeVoiture(Voiture voiture) throws SQLException {
        String query = "DELETE FROM voiture WHERE immatriculation = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, voiture.getImmatriculation());
        pst.executeUpdate();
        pst.close();
    }

    public void updateVoiture(Voiture voiture) throws SQLException {
        String query = "UPDATE voiture SET modele = ?, marque = ?, est_louee = ? WHERE immatriculation = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, voiture.getModele());
        pst.setString(2, voiture.getMarque());
        pst.setBoolean(3, voiture.isEstLouee());
        pst.setString(4, voiture.getImmatriculation());
        pst.executeUpdate();
        pst.close();
    }
}
