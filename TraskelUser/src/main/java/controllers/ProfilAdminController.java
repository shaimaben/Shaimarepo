package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import entities.Utilisateur;
import entities.enums.Role;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.UtilisateurCrud;
import tools.MyConnection;


public class ProfilAdminController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_annul;

    @FXML
    private Button btn_deconn;

    @FXML
    private Button btn_deco1;

    @FXML
    private Button btn_modif;

    @FXML
    private Button btn_membres;

    @FXML
    private Button btn_enregismodif;

    @FXML
    private RadioButton rbmembre;

    @FXML
    private RadioButton rbproprietaire;

    @FXML
    private TextField tfcin;

    @FXML
    private TextField tfemail;
    @FXML
    private TextField tfid;

    @FXML
    private PasswordField tfmdp;

    @FXML
    private TextField tfnom;

    @FXML
    private TextField tfnum_tel;

    @FXML
    private TextField tfprenom;
    private Utilisateur admin;
    private String email;
    private String password;
    private UtilisateurCrud utilisateurCrud = new UtilisateurCrud();
    @FXML
    private TableView<Utilisateur> tableView;
    @FXML
    private TableColumn<Utilisateur, Integer> colCin;

    @FXML
    private TableColumn<Utilisateur, Integer> colNumTel;

    @FXML
    private TableColumn<Utilisateur, Integer> colId;

    @FXML
    private TableColumn<Utilisateur, String> colNom;

    @FXML
    private TableColumn<Utilisateur, String> colPrenom;

    @FXML
    private TableColumn<Utilisateur, String> colEmail;

    @FXML
    private TableColumn<Utilisateur, Role> colRole;

    @FXML
    private TableColumn<Utilisateur, Void> colAction;
    @FXML
    private Label nomUtilisateur;

    private Utilisateur utilisateur;
    public void setAdmin(Utilisateur admin) throws SQLException {
        this.admin = admin;
        if (this.admin != null) {
            nomUtilisateur.setText(this.admin.getNom_user());
        }
        afficherDetailsProfil(admin);  // Populate the fields when the admin is set
    }
    public void initData(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public void afficherDetailsProfil(Utilisateur admin) throws SQLException {
        Connection cnx = MyConnection.getInstance().getCnx();
        String req = "SELECT * from user WHERE id =?";

        PreparedStatement pst = cnx.prepareStatement(req);
        pst.setInt(1,MyConnection.instance.getId());
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            tfid.setText(String.valueOf(admin.getId()));
            tfcin.setText(String.valueOf(admin.getCin()));
            tfnum_tel.setText(String.valueOf(admin.getTel_user()));
            tfnom.setText(admin.getNom_user());
            tfprenom.setText(admin.getPrenom_user());
            tfemail.setText(admin.getEmail());
            tfmdp.setText(admin.getPassword());
            nomUtilisateur.setText(tfnom.getText());

        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void initialize() {

        // Effectuer la liaison bidirectionnelle entre le texte de tfnom et le texte de nomUtilisateur
        nomUtilisateur.textProperty().bindBidirectional(tfnom.textProperty());

        utilisateur = utilisateurCrud.getUtilisateurById(MyConnection.instance.getId());
        if (utilisateur != null) {
            tfid.setText(String.valueOf(utilisateur.getId()));
            tfcin.setText(String.valueOf(utilisateur.getCin()));
            tfnum_tel.setText(String.valueOf(utilisateur.getTel_user()));
            tfnom.setText(utilisateur.getNom_user());
            tfprenom.setText(utilisateur.getPrenom_user());
            tfemail.setText(utilisateur.getEmail());
            tfmdp.setText(utilisateur.getPassword());

        } else {
            // Handle the case where user details are not found
            System.out.println("User details not found.");
        }
        btn_deconn.setOnAction(event -> {
            // Display a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment vous déconnecter ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // If the user clicks "OK", close the current window and open the authentication page
                Stage stage = (Stage) btn_deconn.getScene().getWindow();
                stage.close();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/authentification.fxml"));
                    Parent root = loader.load();
                    Stage authStage = new Stage();
                    authStage.setScene(new Scene(root));
                    authStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // maysir chay
            }
        });

        btn_enregismodif.setOnAction(event -> modifierProfil());

        btn_membres.setOnAction(event -> {
            try {
                // Charger le fichier FXML de la page ListeUtilisateurs
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeUtilisateurs.fxml"));
                Parent root = loader.load();

                // Créer une nouvelle scène avec le contenu chargé du fichier FXML
                Scene scene = new Scene(root);

                // Créer une nouvelle fenêtre (stage) et définir sa scène
                Stage stage = new Stage();
                stage.setScene(scene);

                // Afficher la fenêtre
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        btn_deco1.setOnAction(event -> {
            afficherProprietairesSalles();
        });
    }

    @FXML
    void afficherProprietairesSalles() {
        ObservableList<Utilisateur> utilisateurs = utilisateurCrud.getUtilisateursByRole(Role.PROPRIETAIRE);
// Mettre à jour les éléments affichés dans la TableView
        tableView.setItems(utilisateurs);
    }
    @FXML
    private void modifierProfil() {
        // Récupérer les valeurs des champs de texte
        int id=Integer.parseInt(tfid.getText());
        int cin = Integer.parseInt(tfcin.getText());
        int tel_user = Integer.parseInt(tfnum_tel.getText());
        String nom_user = tfnom.getText();
        String prenom_user = tfprenom.getText();
        String email = tfemail.getText();
        String password = utilisateur.getPassword();

        // Créer un nouvel objet Utilisateur avec les valeurs récupérées
        Utilisateur u = new Utilisateur(id,cin, tel_user, nom_user, prenom_user, email, password);

        // Appeler la méthode de mise à jour appropriée du CRUD Utilisateur
        utilisateurCrud.modifierEntite(u);
    }

}