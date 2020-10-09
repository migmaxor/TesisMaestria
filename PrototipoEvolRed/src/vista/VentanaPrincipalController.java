package vista;

import IA.Evolutivo;
import IA.RedNeuronal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * FXML Controller class
 *
 * @author Miguel Askar
 */
public class VentanaPrincipalController implements Initializable {

    @FXML
    private ChoiceBox<String> genero;
    @FXML
    private TextField edad;
    @FXML
    private TextField presion;
    @FXML
    private TextField colesterol;
    @FXML
    private ChoiceBox<String> azucar;
    @FXML
    private ChoiceBox<String> ecg;
    @FXML
    private TextField ritmo;
    @FXML
    private Label diagnosticoRNA;
    @FXML
    private Label diagnosticoAG;
    @FXML
    private Button botonDiagnosticar;
    @FXML
    private Button cargar;
    @FXML
    private Button procesar;
    @FXML
    private CheckBox columnaAdicional;
    
    //Los siguientes son los atributos de las matrices de confusión
    @FXML
    private Label r00;
    @FXML
    private Label r01;
    @FXML
    private Label r10;
    @FXML
    private Label r11;
    @FXML
    private Label e00;
    @FXML
    private Label e01;
    @FXML
    private Label e10;
    @FXML
    private Label e11;
    
    private Evolutivo evolutivo;
    private RedNeuronal redNeuronal;
    private String rutaCSV;
    @FXML
    private Label archivoCargado;
    @FXML
    private Label tiempoR;
    @FXML
    private Label tiempoE;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Asignación de valores a las listas desplegables.
        ObservableList<String> availableChoices = FXCollections.observableArrayList("Femenino", "Másculino"); 
        genero.setItems(availableChoices);
        genero.getSelectionModel().selectFirst();
        
        availableChoices = FXCollections.observableArrayList("Menor a 120 mg/dl", "Mayor a 120 mg/dL"); 
        azucar.setItems(availableChoices);
        azucar.getSelectionModel().selectFirst();
        
        availableChoices = FXCollections.observableArrayList("Normal", "Anormal");
        ecg.setItems(availableChoices);
        ecg.getSelectionModel().selectFirst();
        
        evolutivo= new Evolutivo();
        redNeuronal= new RedNeuronal();
    }    
    
    @FXML
    public void diagnosticarPaciente()
    {
        String datosEvolutivo= edad.getText() + "," +                
                genero.getSelectionModel().getSelectedIndex() + "," +
                presion.getText() + "," +
                colesterol.getText() + "," + 
                azucar.getSelectionModel().getSelectedIndex() + "," +
                ecg.getSelectionModel().getSelectedIndex() + "," +
                ritmo.getText() + ",0";                   
        boolean resultadoEvolutivo= evolutivo.analizarPaciente(datosEvolutivo);
        String datosRedNeuronal= ((1/(77-29))*(Double.parseDouble(edad.getText())-29)) + "," +                
                genero.getSelectionModel().getSelectedIndex() + "," +
                ((1/(180-94))*(Double.parseDouble(presion.getText())-94)) + "," +
                ((1/(564-131))*(Double.parseDouble(colesterol.getText())-131)) + "," + 
                azucar.getSelectionModel().getSelectedIndex() + "," +
                ecg.getSelectionModel().getSelectedIndex() + "," +
                ((1/(202-71))*(Double.parseDouble(ritmo.getText())-171)) + ",0";   
        boolean resultadoRedNeuronal= redNeuronal.analizarPaciente(datosRedNeuronal);
        
        if(!resultadoEvolutivo) 
        {
            diagnosticoAG.setText("En riesgo");
        }else
        {
            diagnosticoAG.setText("Sin riesgo");
        };
        if(!resultadoRedNeuronal)
        {
            diagnosticoRNA.setText("En riesgo");
        }else 
        {
           diagnosticoRNA.setText("Sin riesgo");
        }
        
    }
    
    @FXML
    public void cargarCSV()
    {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "CSV Archivos delimitados por comas", "csv", "CSV");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {            
            rutaCSV= chooser.getSelectedFile().getAbsolutePath();
            archivoCargado.setText("Cargado: " + chooser.getSelectedFile().getName());
        }
    }
    
    @FXML
    public void procesarCSV()
    {
        evolutivo.analizarArchivo(rutaCSV);
        redNeuronal.analizarArchivo(rutaCSV);
        if(columnaAdicional.isSelected())
        {
            r00.setText(String.valueOf(redNeuronal.getCasos0()));
            r01.setText(String.valueOf(redNeuronal.getFalsosCasos0()));
            r10.setText(String.valueOf(redNeuronal.getFalsosCasos1()));
            r11.setText(String.valueOf(redNeuronal.getCasos1()));
            
            e00.setText(String.valueOf(evolutivo.getCasos0()));
            e01.setText(String.valueOf(evolutivo.getFalsosCasos0()));
            e10.setText(String.valueOf(evolutivo.getFalsosCasos1()));
            e11.setText(String.valueOf(evolutivo.getCasos1()));
            
            tiempoR.setText(redNeuronal.getTiempoEjecucion() + "ms");
            tiempoE.setText(evolutivo.getTiempoEjecucion() + "ms");
        }else
        {
            r00.setText("-");
            r01.setText("-");
            r10.setText("-");
            r11.setText("-");
            
            e00.setText("-");
            e01.setText("-");
            e10.setText("-");
            e11.setText("-");
            
            tiempoR.setText(redNeuronal.getTiempoEjecucion() + "ms");
            tiempoE.setText(evolutivo.getTiempoEjecucion() + "ms");
        }
        evolutivo.crearArchivo(columnaAdicional.isSelected(), rutaCSV);
        evolutivo.agregarLineasArchivo();
    }
}
