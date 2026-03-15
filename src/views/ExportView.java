package views;

import controllers.GestionEtudiants;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import models.Etudiant;
import java.io.File;

/**
 * Vue Export
 * Exporter les releves individuels ou la liste complete.
 */
public class ExportView {

    private Stage stage;
    private GestionEtudiants gestion;

    public ExportView(Stage stage, GestionEtudiants gestion) {
        this.stage = stage;
        this.gestion = gestion;
    }

    public void show() {
        stage.setTitle("Systeme de Gestion Academique - Export");

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
            HBox item = buildMenuItem(icons[i], menus[i], i == 3);
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

    private VBox buildContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(28));

        Label pageTitle = new Label("Export des Donnees");
        pageTitle.setFont(Font.font("Cambria", FontWeight.BOLD, 22));
        pageTitle.setStyle("-fx-text-fill: #1B2A4A;");

        Label pageSubtitle = new Label("Exportez les releves individuels ou la liste complete de la promotion.");
        pageSubtitle.setStyle("-fx-text-fill: #7A8BA6; -fx-font-size: 13px;");

        // Export individuel
        VBox card1 = buildCard(
            "Releve Individuel",
            "Exporter le releve de notes d'un etudiant specifique en fichier .txt",
            "#2A5298"
        );

        ComboBox<Etudiant> combo = new ComboBox<>(FXCollections.observableArrayList(gestion.getTousEtudiants()));
        combo.setPromptText("Choisir un etudiant...");
        combo.setMaxWidth(Double.MAX_VALUE);
        combo.setStyle("-fx-font-size: 13px;");

        Label statusLabel1 = new Label("");
        statusLabel1.setStyle("-fx-font-size: 12px;");

        Button exportIndivBtn = new Button("Exporter en .txt");
        exportIndivBtn.setStyle("-fx-background-color: #2A5298; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 9 20 9 20;");
        exportIndivBtn.setOnAction(e -> {
            Etudiant selected = combo.getValue();
            if (selected == null) { statusLabel1.setText("Selectionnez un etudiant."); statusLabel1.setStyle("-fx-text-fill: #C62828;"); return; }
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Choisir le dossier de destination");
            File dir = dc.showDialog(stage);
            if (dir != null) {
                String path = dir.getAbsolutePath() + File.separator + "Releve_" + selected.getNumeroEtudiant() + ".txt";
                boolean ok = selected.exporterTxt(path);
                if (ok) { statusLabel1.setText("Exporte avec succes : " + path); statusLabel1.setStyle("-fx-text-fill: #1A5C1A; -fx-font-size: 11px;"); }
                else    { statusLabel1.setText("Erreur lors de l'export."); statusLabel1.setStyle("-fx-text-fill: #C62828;"); }
            }
        });

        card1.getChildren().addAll(new Label("Etudiant :"), combo, exportIndivBtn, statusLabel1);

        // Export promotion complete
        VBox card2 = buildCard(
            "Promotion Complete",
            "Exporter la liste de tous les etudiants avec leurs moyennes en .txt",
            "#C9A84C"
        );

        Label statusLabel2 = new Label("");
        statusLabel2.setStyle("-fx-font-size: 12px;");

        Button exportAllBtn = new Button("Exporter toute la promotion");
        exportAllBtn.setStyle("-fx-background-color: #C9A84C; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 9 20 9 20;");
        exportAllBtn.setOnAction(e -> {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Choisir le dossier de destination");
            File dir = dc.showDialog(stage);
            if (dir != null) {
                String path = dir.getAbsolutePath() + File.separator + "Promotion_Complete.txt";
                StringBuilder sb = new StringBuilder();
                sb.append("============================================================\n");
                sb.append("           LISTE COMPLETE DE LA PROMOTION\n");
                sb.append("============================================================\n\n");
                for (Etudiant etudiant : gestion.getTousEtudiants()) {
                    sb.append(etudiant.genererReleve()).append("\n");
                }
                sb.append(String.format("\nMoyenne Generale Promotion : %.2f/20\n", gestion.getMoyennePromotion()));
                sb.append(String.format("Admis : %d  |  Ajournes : %d\n", gestion.getNombreAdmis(), gestion.getNombreAjournes()));

                // Creer un etudiant temporaire juste pour utiliser exporterTxt
                try {
                    java.io.FileWriter fw = new java.io.FileWriter(path);
                    fw.write(sb.toString());
                    fw.close();
                    statusLabel2.setText("Exporte avec succes : " + path);
                    statusLabel2.setStyle("-fx-text-fill: #1A5C1A; -fx-font-size: 11px;");
                } catch (Exception ex) {
                    statusLabel2.setText("Erreur lors de l'export.");
                    statusLabel2.setStyle("-fx-text-fill: #C62828;");
                }
            }
        });

        card2.getChildren().addAll(exportAllBtn, statusLabel2);

        content.getChildren().addAll(pageTitle, pageSubtitle, card1, card2);
        return content;
    }

    private VBox buildCard(String title, String subtitle, String accentColor) {
        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);");

        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        Label accent = new Label("|");
        accent.setStyle("-fx-text-fill: " + accentColor + "; -fx-font-size: 22px; -fx-font-weight: bold;");
        VBox titleBox = new VBox(2);
        Label t = new Label(title);
        t.setFont(Font.font("Cambria", FontWeight.BOLD, 16));
        t.setStyle("-fx-text-fill: #1B2A4A;");
        Label s = new Label(subtitle);
        s.setStyle("-fx-text-fill: #7A8BA6; -fx-font-size: 12px;");
        titleBox.getChildren().addAll(t, s);
        titleRow.getChildren().addAll(accent, titleBox);
        card.getChildren().add(titleRow);
        return card;
    }
}
