package views;

import controllers.GestionEtudiants;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Etudiant;
import models.Module;

/**
 * Vue Gestion des Notes
 * Selectionner un etudiant et gerer ses modules/notes.
 */
public class NotesView {

    private Stage stage;
    private GestionEtudiants gestion;
    private Etudiant selected;
    private VBox modulesBox;
    private Label moyLabel, statutLabel, mentionLabel, meilleurLabel, pireLabel;
    private BarChart<String, Number> chart;
    private TextArea releveArea;
    private ComboBox<Etudiant> etudiantCombo;

    public NotesView(Stage stage, GestionEtudiants gestion) {
        this.stage = stage;
        this.gestion = gestion;
    }

    public void show() {
        stage.setTitle("Systeme de Gestion Academique - Notes");

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
            HBox item = buildMenuItem(icons[i], menus[i], i == 2);
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

    private HBox buildContent() {
        HBox content = new HBox(14);
        content.setPadding(new Insets(20));

        // Panneau gauche
        VBox left = new VBox(12);
        left.setPrefWidth(340);
        left.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);");

        Label pageTitle = new Label("Gestion des Notes");
        pageTitle.setFont(Font.font("Cambria", FontWeight.BOLD, 18));
        pageTitle.setStyle("-fx-text-fill: #1B2A4A;");

        // Selectionner etudiant
        Label selLabel = new Label("Selectionner un etudiant :");
        selLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1B2A4A;");

        etudiantCombo = new ComboBox<>(FXCollections.observableArrayList(gestion.getTousEtudiants()));
        etudiantCombo.setPromptText("Choisir un etudiant...");
        etudiantCombo.setMaxWidth(Double.MAX_VALUE);
        etudiantCombo.setStyle("-fx-font-size: 13px;");
        etudiantCombo.setOnAction(e -> {
            selected = etudiantCombo.getValue();
            refresh();
        });

        // Formulaire ajout module
        Label addTitle = new Label("Ajouter un Module :");
        addTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #1B2A4A;");

        TextField nomField   = styledField("Nom du module");
        TextField noteField  = styledField("Note (0-20)");
        TextField coeffField = styledField("Coefficient");
        Label errLabel = new Label("");
        errLabel.setStyle("-fx-text-fill: #C62828; -fx-font-size: 11px;");

        Button addBtn = new Button("Ajouter le Module");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setStyle("-fx-background-color: #2A5298; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 8 0 8 0;");

        addBtn.setOnAction(e -> {
            errLabel.setText("");
            if (selected == null) { errLabel.setText("Selectionnez d'abord un etudiant."); return; }
            try {
                String nom  = nomField.getText().trim();
                double note = Double.parseDouble(noteField.getText().trim());
                int coeff   = Integer.parseInt(coeffField.getText().trim());
                if (nom.isEmpty()) throw new Exception("Nom obligatoire.");
                if (note < 0 || note > 20) throw new Exception("Note entre 0 et 20.");
                if (coeff <= 0) throw new Exception("Coefficient > 0.");
                selected.ajouterModule(new Module(nom, note, coeff));
                gestion.sauvegarderCSV();
                nomField.clear(); noteField.clear(); coeffField.clear();
                refresh();
            } catch (NumberFormatException ex) {
                errLabel.setText("Note et coefficient doivent etre des nombres.");
            } catch (Exception ex) {
                errLabel.setText(ex.getMessage());
            }
        });

        // Liste modules
        Label listTitle = new Label("Modules de l'etudiant :");
        listTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #1B2A4A;");
        modulesBox = new VBox(5);
        ScrollPane scroll = new ScrollPane(modulesBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scroll.setPrefHeight(180);

        Button clearBtn = new Button("Effacer tous les modules");
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setStyle("-fx-background-color: #B71C1C; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 8 0 8 0;");
        clearBtn.setOnAction(e -> {
            if (selected == null) return;
            selected.supprimerTousModules();
            gestion.sauvegarderCSV();
            refresh();
        });

        left.getChildren().addAll(
            pageTitle, selLabel, etudiantCombo,
            new Separator(), addTitle,
            new Label("Module :"), nomField,
            new Label("Note :"), noteField,
            new Label("Coefficient :"), coeffField,
            errLabel, addBtn,
            new Separator(), listTitle, scroll, clearBtn
        );

        // Panneau droit
        VBox right = new VBox(12);
        HBox.setHgrow(right, Priority.ALWAYS);

        // Stats
        GridPane stats = new GridPane();
        stats.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 14; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);");
        stats.setHgap(12); stats.setVgap(8);

        moyLabel     = statCard("Moyenne", "--", "#1B2A4A");
        statutLabel  = statCard("Statut",  "--", "#7A8BA6");
        mentionLabel = statCard("Mention", "--", "#C9A84C");
        meilleurLabel= statCard("Meilleur", "--", "#1A5C1A");
        pireLabel    = statCard("A Ameliorer", "--", "#B71C1C");

        stats.add(moyLabel, 0, 0); stats.add(statutLabel, 1, 0); stats.add(mentionLabel, 2, 0);
        stats.add(meilleurLabel, 0, 1); stats.add(pireLabel, 1, 1);
        ColumnConstraints cc = new ColumnConstraints(); cc.setPercentWidth(33.33);
        stats.getColumnConstraints().addAll(cc, cc, cc);

        // Graphique
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis(0, 20, 2);
        chart = new BarChart<>(x, y);
        chart.setTitle("Notes par Module");
        chart.setLegendVisible(false);
        chart.setPrefHeight(200);
        chart.setStyle("-fx-background-color: white; -fx-background-radius: 8;");

        // Releve
        releveArea = new TextArea();
        releveArea.setEditable(false);
        releveArea.setFont(Font.font("Consolas", 11));
        releveArea.setPrefHeight(150);
        releveArea.setStyle("-fx-control-inner-background: #1E1E2E; -fx-text-fill: #A6E3A1; -fx-font-size: 11px;");

        right.getChildren().addAll(stats, chart, releveArea);
        content.getChildren().addAll(left, right);
        return content;
    }

    private void refresh() {
        modulesBox.getChildren().clear();
        if (selected == null) return;

        for (Module m : selected.getModules()) {
            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-background-color: #F4F6FA; -fx-background-radius: 4; -fx-padding: 5 8 5 8;");
            Label nomL  = new Label(m.getNom()); nomL.setStyle("-fx-font-weight: bold; -fx-text-fill: #1B2A4A;"); nomL.setPrefWidth(120);
            Label noteL = new Label(String.format("%.1f/20", m.getNote())); noteL.setStyle("-fx-text-fill: " + noteColor(m.getNote()) + ";");
            Label coeffL= new Label("C" + m.getCoefficient()); coeffL.setStyle("-fx-text-fill: #7A8BA6; -fx-font-size: 11px;");
            Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
            Button del = new Button("X"); del.setStyle("-fx-background-color: transparent; -fx-text-fill: #B71C1C; -fx-cursor: hand; -fx-font-weight: bold;");
            del.setOnAction(e -> { selected.supprimerModule(m); gestion.sauvegarderCSV(); refresh(); });
            row.getChildren().addAll(nomL, noteL, coeffL, sp, del);
            modulesBox.getChildren().add(row);
        }

        if (selected.getModules().isEmpty())
            modulesBox.getChildren().add(new Label("Aucun module."));

        // Stats
        double moy = selected.calculerMoyenne();
        updateCard(moyLabel,      "Moyenne",       String.format("%.2f/20", moy), "#1B2A4A");
        updateCard(statutLabel,   "Statut",        selected.estAdmis() ? "Admis" : "Ajourne", selected.estAdmis() ? "#1A5C1A" : "#B71C1C");
        updateCard(mentionLabel,  "Mention",       selected.getMention(), "#C9A84C");
        updateCard(meilleurLabel, "Meilleur",      selected.getMeilleurModule(), "#1A5C1A");
        updateCard(pireLabel,     "A Ameliorer",   selected.getPireModule(), "#B71C1C");

        // Graphique
        chart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Module m : selected.getModules())
            series.getData().add(new XYChart.Data<>(m.getNom(), m.getNote()));
        chart.getData().add(series);

        releveArea.setText(selected.genererReleve());
    }

    private Label statCard(String title, String value, String color) {
        Label lbl = new Label(title + "\n" + value);
        lbl.setStyle("-fx-background-color: #F4F6FA; -fx-background-radius: 6; -fx-padding: 10 14 10 14; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + color + "; -fx-alignment: center;");
        lbl.setMaxWidth(Double.MAX_VALUE); lbl.setAlignment(Pos.CENTER);
        GridPane.setFillWidth(lbl, true);
        return lbl;
    }

    private void updateCard(Label lbl, String title, String value, String color) {
        lbl.setText(title + "\n" + value);
        lbl.setStyle("-fx-background-color: #F4F6FA; -fx-background-radius: 6; -fx-padding: 10 14 10 14; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + color + "; -fx-alignment: center;");
    }

    private String noteColor(double note) {
        if (note >= 14) return "#1A5C1A";
        if (note >= 10) return "#C9A84C";
        return "#B71C1C";
    }

    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color: #F4F6FA; -fx-border-color: #D0D8E8; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 6 10 6 10; -fx-font-size: 12px;");
        return tf;
    }
}
