package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Vue Login
 * Ecran de connexion de l'administrateur.
 */
public class LoginView {

    private Stage stage;
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "1234";

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        stage.setTitle("Systeme de Gestion Academique - Connexion");
        stage.setResizable(false);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1B2A4A;");

        // Card centrale
        VBox card = new VBox(18);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40, 50, 40, 50));
        card.setMaxWidth(380);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 4);"
        );

        // Logo / Titre
        Text logo = new Text("A");
        logo.setFont(Font.font("Cambria", FontWeight.BOLD, 42));
        logo.setFill(Color.web("#1B2A4A"));

        StackPane logoCircle = new StackPane(logo);
        logoCircle.setStyle(
            "-fx-background-color: #F4F6FA;" +
            "-fx-background-radius: 50;" +
            "-fx-min-width: 70; -fx-min-height: 70;" +
            "-fx-max-width: 70; -fx-max-height: 70;"
        );

        Text title = new Text("Gestion Academique");
        title.setFont(Font.font("Cambria", FontWeight.BOLD, 20));
        title.setFill(Color.web("#1B2A4A"));

        Text subtitle = new Text("Espace Administrateur");
        subtitle.setFont(Font.font("Arial", 13));
        subtitle.setFill(Color.web("#7A8BA6"));

        // Champs
        TextField userField = new TextField();
        userField.setPromptText("Nom d'utilisateur");
        userField.setStyle(fieldStyle());
        userField.setPrefHeight(40);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Mot de passe");
        passField.setStyle(fieldStyle());
        passField.setPrefHeight(40);

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #C62828; -fx-font-size: 12px;");

        Button loginBtn = new Button("Se Connecter");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(42);
        loginBtn.setStyle(
            "-fx-background-color: #1B2A4A;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;"
        );

        Label hint = new Label("admin / 1234");
        hint.setStyle("-fx-text-fill: #AABBCC; -fx-font-size: 11px;");

        // Action login
        Runnable doLogin = () -> {
            String user = userField.getText().trim();
            String pass = passField.getText().trim();
            if (user.equals(ADMIN_USER) && pass.equals(ADMIN_PASS)) {
                new DashboardView(stage).show();
            } else {
                errorLabel.setText("Identifiants incorrects. Reessayez.");
                passField.clear();
            }
        };

        loginBtn.setOnAction(e -> doLogin.run());
        passField.setOnAction(e -> doLogin.run());
        userField.setOnAction(e -> passField.requestFocus());

        card.getChildren().addAll(
            logoCircle, title, subtitle,
            new Separator(),
            new Label("Utilisateur :"), userField,
            new Label("Mot de passe :"), passField,
            errorLabel, loginBtn, hint
        );

        StackPane center = new StackPane(card);
        center.setAlignment(Pos.CENTER);
        root.setCenter(center);

        Scene scene = new Scene(root, 480, 560);
        stage.setScene(scene);
        stage.show();
    }

    private String fieldStyle() {
        return "-fx-background-color: #F4F6FA;" +
               "-fx-border-color: #D0D8E8;" +
               "-fx-border-radius: 5;" +
               "-fx-background-radius: 5;" +
               "-fx-padding: 8 12 8 12;" +
               "-fx-font-size: 13px;";
    }
}
