package views;

import controllers.GestionEtudiants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Etudiant;

/**
 * Vue Gestion des Etudiants
 * Tableau avec CRUD complet et recherche.
 */
public class EtudiantsView {

    private Stage stage;
    private GestionEtudiants gestion;
    private TableView<Etudiant> table;
    private ObservableList<Etudiant> data;
    private TextField searchField;

    public EtudiantsView(Stage stage, GestionEtudiants gestion) {
        this.stage = stage;
        this.gestion = gestion;
    }

    public void show() {
        stage.setTitle("Systeme de Gestion Academique - Etudiants");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F4F6FA;");
        root.setTop(buildHeader());
        root.setLeft(buildSidebar());
        root.setCenter(buildContent());

        Scene scene = new Scene(root, 1100, 680);
        stage.setScene(scene);
    }

    private HBox buildHeader() {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #1B2A4A;");
        header.setPadding(new Insets(14, 24, 14, 24));
        header.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Systeme de Gestion Academique");
        title.setFont(Font.font("Cambria", FontWeight.BOLD, 20));
        title.setFill(Color.WHITE);

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Label adminLabel = new Label("Administrateur");
        adminLabel.setStyle("-fx-text-fill: #C9A84C; -fx-font-weight: bold;");

        Button logoutBtn = new Button("Deconnexion");
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #C9A84C; -fx-border-radius: 4; -fx-text-fill: #C9A84C; -fx-cursor: hand; -fx-padding: 4 10 4 10;");
        logoutBtn.setOnAction(e -> new LoginView(stage).show());

        header.getChildren().addAll(title, spacer, adminLabel, logoutBtn);
        return header;
    }

    private VBox buildSidebar() {
        VBox sidebar = new VBox(4);
        sidebar.setStyle("-fx-background-color: #243654;");
        sidebar.setPadding(new Insets(20, 0, 20, 0));
        sidebar.setPrefWidth(200);

        String[] menus = {"Dashboard", "Etudiants", "Notes", "Export"};
        String[] icons = {"D", "E", "N", "X"};

        for (int i = 0; i < menus.length; i++) {
            HBox item = buildMenuItem(icons[i], menus[i], i == 1);
            final int idx = i;
            item.setOnMouseClicked(e -> {
                switch (idx) {
                    case 0: new DashboardView(stage, gestion).show(); break;
                    case 1: new EtudiantsView(stage, gestion).show(); break;
                    case 2: new NotesView(stage, gestion).show(); break;
                    case 3: new ExportView(stage, gestion).show(); break;
                }
            });
            sidebar.getChildren().add(item);
        }
        return sidebar;
    }

    private HBox buildMenuItem(String icon, String label, boolean active) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(12, 20, 12, 20));
        item.setStyle(active ? "-fx-background-color: #1B2A4A; -fx-cursor: hand;" : "-fx-background-color: transparent; -fx-cursor: hand;");

        Label iconLbl = new Label(icon);
        iconLbl.setStyle("-fx-background-color: " + (active ? "#C9A84C" : "#3A5278") + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px; -fx-min-width: 28; -fx-min-height: 28; -fx-max-width: 28; -fx-max-height: 28; -fx-background-radius: 4; -fx-alignment: center;");

        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: " + (active ? "white" : "#AABBCC") + "; -fx-font-size: 13px;");

        item.getChildren().addAll(iconLbl, lbl);
        item.setOnMouseEntered(e -> { if (!active) item.setStyle("-fx-background-color: #1E3050; -fx-cursor: hand;"); });
        item.setOnMouseExited(e -> { if (!active) item.setStyle("-fx-background-color: transparent; -fx-cursor: hand;"); });
        return item;
    }

    @SuppressWarnings("unchecked")
    private VBox buildContent() {
        VBox content = new VBox(14);
        content.setPadding(new Insets(20));

        // Titre
        Label pageTitle = new Label("Gestion des Etudiants");
        pageTitle.setFont(Font.font("Cambria", FontWeight.BOLD, 22));
        pageTitle.setStyle("-fx-text-fill: #1B2A4A;");

        // Barre d'outils
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Rechercher par nom...");
        searchField.setPrefWidth(260);
        searchField.setStyle("-fx-background-color: white; -fx-border-color: #D0D8E8; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 7 12 7 12;");
        searchField.textProperty().addListener((obs, old, val) -> filtrer(val));

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addBtn = new Button("+ Ajouter Etudiant");
        addBtn.setStyle("-fx-background-color: #1B2A4A; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
        addBtn.setOnAction(e -> showAjouterDialog(null));

        toolbar.getChildren().addAll(searchField, spacer, addBtn);

        // Tableau
        data = FXCollections.observableArrayList(gestion.getTousEtudiants());
        table = new TableView<>(data);
        table.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<Etudiant, String> colNum = new TableColumn<>("Numero");
        colNum.setCellValueFactory(new PropertyValueFactory<>("numeroEtudiant"));
        colNum.setPrefWidth(100);

        TableColumn<Etudiant, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colNom.setPrefWidth(120);

        TableColumn<Etudiant, String> colPrenom = new TableColumn<>("Prenom");
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colPrenom.setPrefWidth(120);

        TableColumn<Etudiant, String> colFiliere = new TableColumn<>("Filiere");
        colFiliere.setCellValueFactory(new PropertyValueFactory<>("filiere"));
        colFiliere.setPrefWidth(120);

        TableColumn<Etudiant, Integer> colSem = new TableColumn<>("Semestre");
        colSem.setCellValueFactory(new PropertyValueFactory<>("semestre"));
        colSem.setPrefWidth(80);

        TableColumn<Etudiant, String> colMoy = new TableColumn<>("Moyenne");
        colMoy.setCellValueFactory(cd -> {
            double moy = cd.getValue().calculerMoyenne();
            return new javafx.beans.property.SimpleStringProperty(String.format("%.2f/20", moy));
        });
        colMoy.setPrefWidth(90);

        TableColumn<Etudiant, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(cd ->
            new javafx.beans.property.SimpleStringProperty(
                cd.getValue().estAdmis() ? "Admis" : "Ajourne"
            )
        );
        colStatut.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else {
                    setText(item);
                    setStyle(item.equals("Admis")
                        ? "-fx-text-fill: #1A5C1A; -fx-font-weight: bold;"
                        : "-fx-text-fill: #B71C1C; -fx-font-weight: bold;");
                }
            }
        });
        colStatut.setPrefWidth(80);

        // Colonne Actions
        TableColumn<Etudiant, Void> colActions = new TableColumn<>("Actions");
        colActions.setPrefWidth(150);
        colActions.setCellFactory(col -> new TableCell<>() {
            final Button editBtn = new Button("Modifier");
            final Button delBtn  = new Button("Supprimer");
            {
                editBtn.setStyle("-fx-background-color: #2A5298; -fx-text-fill: white; -fx-font-size: 11px; -fx-background-radius: 4; -fx-cursor: hand;");
                delBtn.setStyle("-fx-background-color: #B71C1C; -fx-text-fill: white; -fx-font-size: 11px; -fx-background-radius: 4; -fx-cursor: hand;");
                editBtn.setOnAction(e -> showAjouterDialog(getTableView().getItems().get(getIndex())));
                delBtn.setOnAction(e -> {
                    Etudiant etudiant = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                        "Supprimer " + etudiant.getPrenom() + " " + etudiant.getNom() + " ?",
                        ButtonType.YES, ButtonType.NO);
                    confirm.showAndWait().ifPresent(bt -> {
                        if (bt == ButtonType.YES) {
                            gestion.supprimerEtudiant(etudiant.getNumeroEtudiant());
                            refreshTable();
                        }
                    });
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    HBox box = new HBox(6, editBtn, delBtn);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });

        table.getColumns().addAll(colNum, colNom, colPrenom, colFiliere, colSem, colMoy, colStatut, colActions);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        content.getChildren().addAll(pageTitle, toolbar, table);
        return content;
    }

    private void filtrer(String query) {
        if (query == null || query.isEmpty()) {
            data.setAll(gestion.getTousEtudiants());
        } else {
            data.setAll(gestion.rechercherParNom(query));
        }
    }

    private void refreshTable() {
        data.setAll(gestion.getTousEtudiants());
    }

    private void showAjouterDialog(Etudiant existing) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(existing == null ? "Ajouter un Etudiant" : "Modifier l'Etudiant");
        dialog.setResizable(false);

        VBox form = new VBox(12);
        form.setPadding(new Insets(24));
        form.setStyle("-fx-background-color: white;");
        form.setPrefWidth(360);

        Label title = new Label(existing == null ? "Nouvel Etudiant" : "Modifier Etudiant");
        title.setFont(Font.font("Cambria", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #1B2A4A;");

        TextField nomField     = styledField("Nom");
        TextField prenomField  = styledField("Prenom");
        TextField filiereField = styledField("Filiere");
        TextField semField     = styledField("Semestre (1-6)");

        if (existing != null) {
            nomField.setText(existing.getNom());
            prenomField.setText(existing.getPrenom());
            filiereField.setText(existing.getFiliere());
            semField.setText(String.valueOf(existing.getSemestre()));
        }

        Label errLabel = new Label("");
        errLabel.setStyle("-fx-text-fill: #C62828; -fx-font-size: 11px;");

        Button saveBtn = new Button(existing == null ? "Ajouter" : "Enregistrer");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setStyle("-fx-background-color: #1B2A4A; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 9 0 9 0;");

        saveBtn.setOnAction(e -> {
            try {
                String nom     = nomField.getText().trim();
                String prenom  = prenomField.getText().trim();
                String filiere = filiereField.getText().trim();
                int sem        = Integer.parseInt(semField.getText().trim());

                if (nom.isEmpty() || prenom.isEmpty() || filiere.isEmpty())
                    throw new Exception("Tous les champs sont obligatoires.");
                if (sem < 1 || sem > 6)
                    throw new Exception("Semestre entre 1 et 6.");

                if (existing == null) {
                    Etudiant e2 = new Etudiant(nom, prenom, gestion.genererNumero(), filiere, sem);
                    gestion.ajouterEtudiant(e2);
                } else {
                    existing.setNom(nom);
                    existing.setPrenom(prenom);
                    existing.setFiliere(filiere);
                    existing.setSemestre(sem);
                    gestion.modifierEtudiant(existing);
                }
                refreshTable();
                dialog.close();
            } catch (NumberFormatException ex) {
                errLabel.setText("Semestre doit etre un nombre.");
            } catch (Exception ex) {
                errLabel.setText(ex.getMessage());
            }
        });

        form.getChildren().addAll(
            title,
            new Label("Nom :"), nomField,
            new Label("Prenom :"), prenomField,
            new Label("Filiere :"), filiereField,
            new Label("Semestre :"), semField,
            errLabel, saveBtn
        );

        dialog.setScene(new Scene(form));
        dialog.showAndWait();
    }

    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color: #F4F6FA; -fx-border-color: #D0D8E8; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 7 12 7 12; -fx-font-size: 13px;");
        tf.setPrefHeight(38);
        return tf;
    }
}
