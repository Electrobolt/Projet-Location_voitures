import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LocationVoituresApp extends Application {
    public VoitureDao voitureDao = new VoitureDao();
    public LocationDao locationDao = new LocationDao();
    private Connection conn;

    public static void main(String[] args) {
        launch(args);
    }

    public LocationVoituresApp() {
        String url = "jdbc:sqlite:/C:\\Users\\Pharell\\Documents\\myPrograms\\Java\\JavaFx\\location_voitures.db";
        try {
            this.conn = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue lors de la connection à la base de données!");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // GUI de la connexion admin
        Stage login = new Stage();
        TextField txtnom = new TextField();
        PasswordField txtpass = new PasswordField();
        txtnom.setPromptText("nom");
        txtpass.setPromptText("mot de passe");
        Button logbtn = new Button("Valider");

        // Ajout des champs au formulaire
        GridPane gridlog = new GridPane();
        gridlog.setHgap(10);
        gridlog.setVgap(10);
        gridlog.setPadding(new Insets(20, 20, 20, 20));
        gridlog.add(new Label("Nom :"), 0, 0);
        gridlog.add(txtnom, 1, 0);
        gridlog.add(new Label("Mot de passe :"), 0, 1);
        gridlog.add(txtpass, 1, 1);
        gridlog.add(logbtn, 2, 2);

        logbtn.setOnAction( new EventHandler<ActionEvent>() {
            
            @Override 
            public void handle(ActionEvent e){
                int test=0;
                try {
                String query = "SELECT * FROM admin";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    if(rs.getString("nom").equals(txtnom.getText()) && rs.getString("password").equals(txtpass.getText())){
                        primaryStage.show();
                        login.close();
                        test=1;
                        break;
                    }
                }
                if(test == 0){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Mauvaises infos");
                    alert.setHeaderText("Informations incorrectes !");
                    alert.setContentText("Veuillez rééssayer");
                    alert.showAndWait();
                    txtnom.clear();
                    txtpass.clear();
                }
                st.close();
                } catch (SQLException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Une erreur est survenue lors de la connection !");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }
        }});
        
        Scene zero = new Scene(gridlog);
        login.setTitle("LOGIN ADMIN");
        login.setScene(zero);

        // Création des objets pour la GUI
        TabPane tabPane = new TabPane();
        Tab tabVoitures = new Tab("Voitures");
        Tab tabLocations = new Tab("Locations");

        // Création du contenu pour l'onglet "Voitures"
        GridPane gridVoitures = new GridPane();
        gridVoitures.setAlignment(Pos.CENTER);
        gridVoitures.setHgap(10);
        gridVoitures.setVgap(10);
        gridVoitures.setPadding(new Insets(25, 25, 25, 25));
    
        TextField carsearch = new TextField();
        carsearch.setPromptText("Rechercher");

        TableView<Voiture> tblVoitures = new TableView<>();
        TableColumn<Voiture, String> colModele = new TableColumn<>("Modèle");
        colModele.setCellValueFactory(new PropertyValueFactory<Voiture, String>("modele"));
        TableColumn<Voiture, String> colMarque = new TableColumn<>("Marque");
        colMarque.setCellValueFactory(new PropertyValueFactory<Voiture, String>("marque"));
        TableColumn<Voiture, String> colImmatriculation = new TableColumn<>("Immatriculation");
        colImmatriculation.setCellValueFactory(new PropertyValueFactory<Voiture, String>("immatriculation"));
        TableColumn<Voiture, String> colEstLouee = new TableColumn<>("Statut");

        colEstLouee.setCellValueFactory(cellData -> {
            if (cellData.getValue().isEstLouee()) {
                return new SimpleStringProperty("Louée");
            } else {
                return new SimpleStringProperty("Libre");
            }
        });

        tblVoitures.getColumns().add(colModele);
        tblVoitures.getColumns().add(colMarque);
        tblVoitures.getColumns().add(colImmatriculation);
        tblVoitures.getColumns().add(colEstLouee);
        tblVoitures.getItems().addAll(voitureDao.getAllVoitures());

        Button btnAjouterVoiture = new Button("Ajouter une voiture");
        Button btnSupprimerVoiture = new Button("Supprimer une voiture");
        Button btnModifierVoiture = new Button("Modifier une voiture");
        Button btnmaj = new Button("🔁");

        btnAjouterVoiture.setOnAction(event -> ajouterVoiture(tblVoitures));
        btnSupprimerVoiture.setOnAction(event -> supprimerVoiture(tblVoitures));
        btnModifierVoiture.setOnAction(event -> modifierVoiture(tblVoitures));
        btnmaj.setOnAction(event -> mise(tblVoitures));

        gridVoitures.add(tblVoitures, 0, 0, 4, 1);
        gridVoitures.add(btnAjouterVoiture, 0, 2);
        gridVoitures.add(btnSupprimerVoiture, 1, 2);
        gridVoitures.add(btnModifierVoiture, 2, 2);
        gridVoitures.add(btnmaj, 3, 2);

        // Création du contenu pour l'onglet "Locations"
        GridPane gridLocations = new GridPane();
        gridLocations.setAlignment(Pos.CENTER);
        gridLocations.setHgap(10);
        gridLocations.setVgap(10);
        gridLocations.setPadding(new Insets(25, 25, 25, 25));

        TableView<Location> tblLocations = new TableView<>();
        TableColumn<Location, String> colNomClient = new TableColumn<>("Nom du client");
        colNomClient.setCellValueFactory(new PropertyValueFactory<Location, String>("nomClient"));

        TableColumn<Location, String> colAdresseClient = new TableColumn<>("Adresse du client");
        colAdresseClient.setCellValueFactory(new PropertyValueFactory<Location, String>("adresseClient"));

        TableColumn<Location, String> colModeleVoitureLouee = new TableColumn<>("Modèle de voiture");
        colModeleVoitureLouee.setCellValueFactory(new PropertyValueFactory<Location, String>("modeleVoitureLouee"));

        TableColumn<Location, String> colImmatVoitureLouee = new TableColumn<>("Immatriculation");
        colImmatVoitureLouee.setCellValueFactory(new PropertyValueFactory<Location, String>("immatVoitureLouee"));

        TableColumn<Location, LocalDate> colDateDebut = new TableColumn<>("Date de début");
        colDateDebut.setCellValueFactory(new PropertyValueFactory<Location, LocalDate>("dateDebut"));

        TableColumn<Location, LocalDate> colDateFin = new TableColumn<>("Date de fin");
        colDateFin.setCellValueFactory(new PropertyValueFactory<Location, LocalDate>("dateFin"));

        TableColumn<Location, Integer> colMontantPaye = new TableColumn<>("Montant payé");
        colMontantPaye.setCellValueFactory(new PropertyValueFactory<Location, Integer>("montantPaye"));

        tblLocations.getColumns().add(colNomClient);
        tblLocations.getColumns().add(colAdresseClient);
        tblLocations.getColumns().add(colModeleVoitureLouee);
        tblLocations.getColumns().add(colImmatVoitureLouee);
        tblLocations.getColumns().add(colDateDebut);
        tblLocations.getColumns().add(colDateFin);
        tblLocations.getColumns().add(colMontantPaye);
        tblLocations.getItems().addAll(locationDao.getAllLocations());

        Button btnAjouterLocation = new Button("Ajouter une location");
        Button btnSupprimerLocation = new Button("Supprimer une location");
        Button btnModifierLocation = new Button("Modifier une location");
        btnAjouterLocation.setOnAction(event -> ajouterLocation(tblLocations));
        btnSupprimerLocation.setOnAction(event -> supprimerLocation(tblLocations));
        btnModifierLocation.setOnAction(event -> modifierLocation(tblLocations));

        gridLocations.add(tblLocations, 0, 0, 4, 1);
        gridLocations.add(btnAjouterLocation, 0, 2);
        gridLocations.add(btnSupprimerLocation, 1, 2);
        gridLocations.add(btnModifierLocation, 2, 2);

        // Ajout des onglets à la TabPane
        tabVoitures.setContent(gridVoitures);
        tabLocations.setContent(gridLocations);
        tabPane.getTabs().add(tabVoitures);
        tabPane.getTabs().add(tabLocations);

        // Mise en forme de la GUI
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(0, 0, 25, 0));
        root.add(tabPane, 0, 0);

        // Affichage de la GUI
        Scene scene = new Scene(root, 610, 500);
        primaryStage.setTitle("Gestion de la location de voitures");
        primaryStage.setScene(scene);
        login.show();
    }

    private void mise(TableView<Voiture> table){table.refresh();}

    private void ajouterVoiture(TableView<Voiture> tblVoitures) {
        Dialog<Voiture> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une voiture");

        // Création des champs pour le formulaire
        TextField txtModele = new TextField();
        txtModele.setPromptText("Modèle");
        TextField txtMarque = new TextField();
        txtMarque.setPromptText("Marque");
        TextField txtImmatriculation = new TextField();
        txtImmatriculation.setPromptText("Immatriculation");

        // Ajout des champs au formulaire
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.add(new Label("Modèle :"), 0, 0);
        grid.add(txtModele, 1, 0);
        grid.add(new Label("Marque :"), 0, 1);
        grid.add(txtMarque, 1, 1);
        grid.add(new Label("Immatriculation :"), 0, 2);
        grid.add(txtImmatriculation, 1, 2);

        // Création des boutons pour le formulaire
        ButtonType btnTypeValider = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnTypeAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnTypeValider, btnTypeAnnuler);

        // Validation du formulaire lors de la soumission
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnTypeValider) {
                try {
                    Voiture voiture = new Voiture(txtModele.getText(), txtMarque.getText(), txtImmatriculation.getText(),false);
                    voitureDao.addVoiture(voiture);
                    tblVoitures.getItems().add(voiture);
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Une erreur est survenue lors de l'ajout de la voiture !");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
            return null;
        });

        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait();
    }

private void supprimerVoiture(TableView<Voiture> tblVoitures) {
    Voiture selectedVoiture = tblVoitures.getSelectionModel().getSelectedItem();
    if (selectedVoiture != null) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Êtes-vous sûr(e) de vouloir supprimer cette voiture ?");
        alert.setContentText(selectedVoiture.getMarque() + " " + selectedVoiture.getModele() + " - " + selectedVoiture.getImmatriculation());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                voitureDao.removeVoiture(selectedVoiture);
                tblVoitures.getItems().remove(selectedVoiture);
            } catch (SQLException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText("Une erreur est survenue lors de la suppression de la voiture !");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
            }
        }
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Information");
        alert.setHeaderText("Aucune voiture sélectionnée !");
        alert.setContentText("Veuillez sélectionner une voiture à supprimer.");
        alert.showAndWait();
    }
}

private void modifierVoiture(TableView<Voiture> tblVoitures) {
    Voiture selectedVoiture = tblVoitures.getSelectionModel().getSelectedItem();
    if (selectedVoiture != null) {
        Dialog<Voiture> dialog = new Dialog<>();
        dialog.setTitle("Modifier une voiture");

        // Création des champs pour le formulaire
        TextField txtModele = new TextField(selectedVoiture.getModele());
        txtModele.setPromptText("Modèle");
        TextField txtMarque = new TextField(selectedVoiture.getMarque());
        txtMarque.setPromptText("Marque");
        TextField txtImmatriculation = new TextField(selectedVoiture.getImmatriculation());
        txtImmatriculation.setPromptText("Immatriculation");

        // Ajout des champs au formulaire
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.add(new Label("Modèle :"), 0, 0);
        grid.add(txtModele, 1, 0);
        grid.add(new Label("Marque :"), 0, 1);
        grid.add(txtMarque, 1, 1);
        grid.add(new Label("Immatriculation :"), 0, 2);
        grid.add(txtImmatriculation, 1, 2);

        // Création des boutons pour le formulaire
        ButtonType btnTypeValider = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnTypeAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnTypeValider, btnTypeAnnuler);

        // Validation du formulaire lors de la soumission
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnTypeValider) {
                try {
                    selectedVoiture.setModele(txtModele.getText());
                    selectedVoiture.setMarque(txtMarque.getText());
                    selectedVoiture.setImmatriculation(txtImmatriculation.getText());
                    voitureDao.updateVoiture(selectedVoiture);
                    tblVoitures.refresh();
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Une erreur est survenue lors de la modification de la voiture !");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
            return null;
        });

        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait();
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Information");
        alert.setHeaderText("Aucune voiture sélectionnée !");
        alert.setContentText("Veuillez sélectionner une voiture à modifier.");
        alert.showAndWait();
    }
}

private void ajouterLocation(TableView<Location> tblLocations) {
    Dialog<Location> dialog = new Dialog<>();
    dialog.setTitle("Ajouter une location");

    // Création des champs pour le formulaire
    TextField txtNomClient = new TextField();
    txtNomClient.setPromptText("Nom du client");
    TextField txtAdresseClient = new TextField();
    txtAdresseClient.setPromptText("Adresse du client");
    ComboBox<Voiture> cbVoiture = new ComboBox<>();
    cbVoiture.setPromptText("Voiture louée");
    try {
        cbVoiture.setItems(voitureDao.getAllVoitures());
    } catch (SQLException e) {
        System.out.println("PB ligne 290");
    }
    DatePicker dpDateDebut = new DatePicker();
    dpDateDebut.setPromptText("Date de début");
    DatePicker dpDateFin = new DatePicker();
    dpDateFin.setPromptText("Date de fin");
    TextField txtMontantPaye = new TextField();
    txtMontantPaye.setPromptText("Montant payé");

    // Ajout des champs au formulaire
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 20, 20, 20));
    grid.add(new Label("Nom du client :"), 0, 0);
    grid.add(txtNomClient, 1, 0);
    grid.add(new Label("Adresse du client :"), 0, 1);
    grid.add(txtAdresseClient, 1, 1);
    grid.add(new Label("Voiture louée :"), 0, 2);
    grid.add(cbVoiture, 1, 2);
    grid.add(new Label("Date de début :"), 0, 3);
    grid.add(dpDateDebut, 1, 3);
    grid.add(new Label("Date de fin :"), 0, 4);
    grid.add(dpDateFin, 1, 4);
    grid.add(new Label("Montant payé :"), 0, 5);
    grid.add(txtMontantPaye, 1, 5);

    // Création des boutons pour le formulaire
    ButtonType btnTypeValider = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
    ButtonType btnTypeAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
    dialog.getDialogPane().getButtonTypes().addAll(btnTypeValider, btnTypeAnnuler);

    // Validation du formulaire lors de la soumission
    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == btnTypeValider) {
            try {
                LocalDate dateDebut = dpDateDebut.getValue();
                LocalDate dateFin = dpDateFin.getValue();
                if (dateDebut == null || dateFin == null || dateDebut.isAfter(dateFin)) {
                    throw new IllegalArgumentException("La date de début doit être avant la date de fin !");
                }
                Location location = new Location(txtNomClient.getText(), txtAdresseClient.getText(), cbVoiture.getValue().getModele(), cbVoiture.getValue().getImmatriculation(), dateDebut, dateFin, Double.parseDouble(txtMontantPaye.getText()));
                        
                locationDao.addLocation(location);
                LocalDate today = LocalDate.now();
                if(dateDebut.isBefore(today) && dateFin.isAfter(today)){
                    cbVoiture.getValue().setEstLouee(true);
                }else{
                    cbVoiture.getValue().setEstLouee(false);
                }
                voitureDao.updateVoiture(cbVoiture.getValue());
                tblLocations.getItems().add(location);
            } catch (SQLException | IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Une erreur est survenue lors de l'ajout de la location !");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
        return null;
    });

    dialog.getDialogPane().setContent(grid);
    dialog.showAndWait();
}

private void supprimerLocation(TableView<Location> tblLocations) {
    Location selectedLocation = tblLocations.getSelectionModel().getSelectedItem();
    if (selectedLocation != null) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Êtes-vous sûr(e) de vouloir supprimer cette location ?");
        alert.setContentText(selectedLocation.getNomClient() + " - " + selectedLocation.getModeleVoitureLouee() + " " + selectedLocation.getImmatVoitureLouee());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                locationDao.removeLocation(selectedLocation);
                tblLocations.getItems().remove(selectedLocation);
            } catch (SQLException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText("Une erreur est survenue lors de la suppression de la location !");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
            }
        }
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Information");
        alert.setHeaderText("Aucune location sélectionnée !");
        alert.setContentText("Veuillez sélectionner une location à supprimer.");
        alert.showAndWait();
    }
}

private void modifierLocation(TableView<Location> tblLocations) {
    Location selectedLocation = tblLocations.getSelectionModel().getSelectedItem();
    if (selectedLocation != null) {
        Dialog<Location> dialog = new Dialog<>();
        dialog.setTitle("Modifier une location");

            // Création des champs pour le formulaire
            TextField txtNomClient = new TextField(selectedLocation.getNomClient());
            txtNomClient.setPromptText("Nom du client");
            TextField txtAdresseClient = new TextField(selectedLocation.getAdresseClient());
            txtAdresseClient.setPromptText("Adresse du client");
            ComboBox<Voiture> cbVoiture = new ComboBox<>();
            cbVoiture.setPromptText("Voiture louée");
            try {
                cbVoiture.setItems(voitureDao.getAllVoitures());
            } catch (SQLException e) {
                System.out.println("PB ligne 395");
            }
            DatePicker dpDateDebut = new DatePicker(selectedLocation.getDateDebut());
            dpDateDebut.setPromptText("Date de début");
            DatePicker dpDateFin = new DatePicker(selectedLocation.getDateFin());
            dpDateFin.setPromptText("Date de fin");
            TextField txtMontantPaye = new TextField(String.valueOf(selectedLocation.getMontantPaye()));
            txtMontantPaye.setPromptText("Montant payé");

            // Ajout des champs au formulaire
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 20, 20, 20));
            grid.add(new Label("Nom du client :"), 0, 0);
            grid.add(txtNomClient, 1, 0);
            grid.add(new Label("Adresse du client :"), 0, 1);
            grid.add(txtAdresseClient, 1, 1);
            grid.add(new Label("Voiture louée :"), 0, 2);
            grid.add(cbVoiture, 1, 2);
            grid.add(new Label("Date de début :"), 0, 3);
            grid.add(dpDateDebut, 1, 3);
            grid.add(new Label("Date de fin :"), 0, 4);
            grid.add(dpDateFin, 1, 4);
            grid.add(new Label("Montant payé :"), 0, 5);
            grid.add(txtMontantPaye, 1, 5);

            // Création des boutons pour le formulaire
            ButtonType btnTypeValider = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnTypeAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btnTypeValider, btnTypeAnnuler);

            // Validation du formulaire lors de la soumission
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnTypeValider) {
                    try {
                        LocalDate dateDebut = dpDateDebut.getValue();
                        LocalDate dateFin = dpDateFin.getValue();
                        if (dateDebut == null || dateFin == null || dateDebut.isAfter(dateFin)) {
                            throw new IllegalArgumentException("La date de début doit être avant la date de fin !");
                        }
                        selectedLocation.setNomClient(txtNomClient.getText());
                        selectedLocation.setAdresseClient(txtAdresseClient.getText());
                        selectedLocation.setImmatVoitureLouee(cbVoiture.getValue().getImmatriculation());
                        selectedLocation.setModeleVoitureLouee(cbVoiture.getValue().getModele());
                        selectedLocation.setDateDebut(dateDebut);
                        selectedLocation.setDateFin(dateFin);
                        selectedLocation.setMontantPaye(Double.parseDouble(txtMontantPaye.getText()));
                        locationDao.updateLocation(selectedLocation);
                        LocalDate today = LocalDate.now();
                        if(dateDebut.isBefore(today) && dateFin.isAfter(today)){
                            cbVoiture.getValue().setEstLouee(true);
                        }else{
                            cbVoiture.getValue().setEstLouee(false);
                        }
                        voitureDao.updateVoiture(cbVoiture.getValue());
                        tblLocations.refresh();
                    } catch (SQLException | IllegalArgumentException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Une erreur est survenue lors de la modification de la location !");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                }
                return null;
            });

            dialog.getDialogPane().setContent(grid);
            dialog.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText("Aucune location sélectionnée !");
            alert.setContentText("Veuillez sélectionner une location à modifier.");
            alert.showAndWait();
        }
    }

}
