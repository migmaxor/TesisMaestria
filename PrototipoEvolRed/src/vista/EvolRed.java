package vista;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author Miguel Askar
 */
public class EvolRed extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    
    
    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Evolred");

        //Por último se inicia la vista
        initRootLayout();
    }
    
    private void initRootLayout() //Primer inicio, carga la vista principal.
    {
        try {
            // Carga el root desde el archivo fxml.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(EvolRed.class.getResource("/vista/ventanaPrincipal.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            // Muestra la escena del layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            
            //VentanaPrincipalController controller = loader.getController();
            primaryStage.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            //System.out.println("Error en el sistema: " + ex.toString());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
