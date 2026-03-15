import javafx.application.Application;
import javafx.stage.Stage;
import views.LoginView;

/**
 * Point d'entree principal de l'application.
 * Systeme de Gestion Academique
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        new LoginView(primaryStage).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
