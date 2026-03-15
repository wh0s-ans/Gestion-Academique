package views;

import controllers.GestionEtudiants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Etudiant;

/**
 * Vue Dashboard Admin
 * Tableau de bord principal avec statistiques de la promotion.
 */
public class DashboardView {

    private Stage stage;
    private GestionEtudiants gestion;

    public DashboardView(Stage stage) {
        this.stage = stage;
        this.gestion = new GestionEtudiants();
    }

    public DashboardView(Stage stage, GestionEtudiants gestion) {
        this.stage = stage;
        this.gestion = gestion;
    }

    public void show() {
        stage.setTitle("Systeme de Gestion Academique");
        stage.setMinWidth(1000);
        stage.setMinHeight(660);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F4F6FA;");
        root.setTop(buildHeader());
        root.setLeft(buildSidebar());
        root.setCenter(buildDashboard());

        Scene scene = new Scene(root, 1100, 680);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    // Header
    private HBox buildHeader() {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #1B2A4A;");
        header.setPadding(new Insets(14, 24, 14, 24));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(12);

        Text title = new Text("Systeme de Gestion Academique");
        title.setFont(Font.font("Cambria", FontWeight.BOLD, 20));
        title.setFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label adminLabel = new Label("Administrateur");
        adminLabel.setStyle("-fx-text-fill: #C9A84C; -fx-font-weight: bold; -fx-font-size: 13px;");

        Button logoutBtn = new Button("Deconnexion");
        logoutBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #C9A84C;" +
            "-fx-border-radius: 4;" +
            "-fx-text-fill: #C9A84C;" +
            "-fx-font-size: 12px;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 4 10 4 10;"
        );
        logoutBtn.setOnAction(e -> new LoginView(stage).show());

        header.getChildren().addAll(title, spacer, adminLabel, logoutBtn);
        return header;
    }

    // Sidebar navigation
    private VBox buildSidebar() {
        VBox sidebar = new VBox(4);
        sidebar.setStyle("-fx-background-color: #243654;");
        sidebar.setPadding(new Insets(20, 0, 20, 0));
        sidebar.setPrefWidth(200);

        String[] menus = {"Dashboard", "Etudiants", "Notes", "Export"};
        String[] icons = {"D", "E", "N", "X"};

        for (int i = 0; i < menus.length; i++) {
            HBox item = buildMenuItem(icons[i], menus[i], i == 0);
            final int idx = i;
            item.setOnMouseClicked(e -> handleNav(idx));
            sidebar.getChildren().add(item);
        }

        return sidebar;
    }

    private HBox buildMenuItem(String icon, String label, boolean active) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(12, 20, 12, 20));
        item.setStyle(active
            ? "-fx-background-color: #1B2A4A; -fx-cursor: hand;"
            : "-fx-background-color: transparent; -fx-cursor: hand;"
        );

        Label iconLbl = new Label(icon);
        iconLbl.setStyle(
            "-fx-background-color: " + (active ? "#C9A84C" : "#3A5278") + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 12px;" +
            "-fx-min-width: 28; -fx-min-height: 28;" +
            "-fx-max-width: 28; -fx-max-height: 28;" +
            "-fx-background-radius: 4;" +
            "-fx-alignment: center;"
        );

        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: " + (active ? "white" : "#AABBCC") + "; -fx-font-size: 13px;");

        item.getChildren().addAll(iconLbl, lbl);

        item.setOnMouseEntered(e -> {
            if (!active) item.setStyle("-fx-background-color: #1E3050; -fx-cursor: hand;");
        });
        item.setOnMouseExited(e -> {
            if (!active) item.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        });

        return item;
    }

    private void handleNav(int idx) {
        switch (idx) {
            case 0: new DashboardView(stage, gestion).show(); break;
            case 1: new EtudiantsView(stage, gestion).show(); break;
            case 2: new NotesView(stage, gestion).show(); break;
            case 3: new ExportView(stage, gestion).show(); break;
        }
    }

    // Dashboard principal
    private ScrollPane buildDashboard() {
        VBox content = new VBox(16);
        content.setPadding(new Insets(20));

        // Titre
        Label pageTitle = new Label("Tableau de Bord");
        pageTitle.setFont(Font.font("Cambria", FontWeight.BOLD, 22));
        pageTitle.setStyle("-fx-text-fill: #1B2A4A;");

        Label pageSubtitle = new Label("Vue d'ensemble de la promotion");
        pageSubtitle.setStyle("-fx-text-fill: #7A8BA6; -fx-font-size: 13px;");

        // Cartes statistiques
        HBox statsRow = new HBox(12);
        statsRow.getChildren().addAll(
            buildStatCard("Total Etudiants", String.valueOf(gestion.getTousEtudiants().size()), "#1B2A4A", "#E8EDF5"),
            buildStatCard("Admis", String.valueOf(gestion.getNombreAdmis()), "#1A5C1A", "#E8F5E9"),
            buildStatCard("Ajournes", String.valueOf(gestion.getNombreAjournes()), "#B71C1C", "#FFEBEE"),
            buildStatCard("Moyenne Promotion", String.format("%.2f/20", gestion.getMoyennePromotion()), "#C9A84C", "#FFF8E1")
        );
        statsRow.getChildren().forEach(n -> HBox.setHgrow(n, Priority.ALWAYS));

        // Meilleur etudiant
        Etudiant best = gestion.getMeilleurEtudiant();
        HBox bestBox = new HBox(10);
        bestBox.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 14 18 14 18;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);"
        );
        bestBox.setAlignment(Pos.CENTER_LEFT);
        Label bestTitle = new Label("Meilleur Etudiant : ");
        bestTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #1B2A4A; -fx-font-size: 14px;");
        Label bestName = new Label(best != null
            ? best.getPrenom() + " " + best.getNom() + " — " + String.format("%.2f/20", best.calculerMoyenne())
            : "Aucun etudiant enregistre");
        bestName.setStyle("-fx-text-fill: #C9A84C; -fx-font-weight: bold; -fx-font-size: 14px;");
        bestBox.getChildren().addAll(bestTitle, bestName);

        // Graphique distribution
        VBox chartBox = new VBox(8);
        chartBox.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);"
        );
        Label chartTitle = new Label("Distribution des Mentions");
        chartTitle.setFont(Font.font("Cambria", FontWeight.BOLD, 15));
        chartTitle.setStyle("-fx-text-fill: #1B2A4A;");

        BarChart<String, Number> chart = buildMentionsChart();
        chartBox.getChildren().addAll(chartTitle, chart);

        content.getChildren().addAll(pageTitle, pageSubtitle, statsRow, bestBox, chartBox);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scroll;
    }

    private VBox buildStatCard(String title, String value, String color, String bg) {
        VBox card = new VBox(6);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(16));
        card.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);"
        );

        Label val = new Label(value);
        val.setFont(Font.font("Cambria", FontWeight.BOLD, 28));
        val.setStyle("-fx-text-fill: " + color + ";");

        Label lbl = new Label(title);
        lbl.setStyle("-fx-text-fill: #7A8BA6; -fx-font-size: 12px;");

        card.getChildren().addAll(val, lbl);
        return card;
    }

    private BarChart<String, Number> buildMentionsChart() {
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        x.setLabel("Mention");
        y.setLabel("Nombre d'etudiants");

        BarChart<String, Number> chart = new BarChart<>(x, y);
        chart.setLegendVisible(false);
        chart.setPrefHeight(260);
        chart.setStyle("-fx-background-color: transparent;");

        int tresBien = 0, bien = 0, assezBien = 0, passable = 0, insuf = 0;
        for (Etudiant e : gestion.getTousEtudiants()) {
            switch (e.getMention()) {
                case "Tres Bien":  tresBien++;  break;
                case "Bien":       bien++;       break;
                case "Assez Bien": assezBien++; break;
                case "Passable":   passable++;   break;
                default:           insuf++;      break;
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Tres Bien", tresBien));
        series.getData().add(new XYChart.Data<>("Bien", bien));
        series.getData().add(new XYChart.Data<>("Assez Bien", assezBien));
        series.getData().add(new XYChart.Data<>("Passable", passable));
        series.getData().add(new XYChart.Data<>("Insuffisant", insuf));
        chart.getData().add(series);

        return chart;
    }
}
