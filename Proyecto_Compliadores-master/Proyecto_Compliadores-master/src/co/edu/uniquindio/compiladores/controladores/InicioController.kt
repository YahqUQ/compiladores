package co.edu.uniquindio.compiladores.controladores

import co.edu.uniquindio.compiladores.lexico.AnalizadorLexico
import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventType
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextArea

import javafx.scene.control.cell.PropertyValueFactory
import java.net.URL
import java.util.*



/*
 * @author: Jaime Nieto, Yiran Hernandez, Mauricio Duque
 */
class InicioController : Initializable {


    /*
        Representa componente grafico de donde se extrae el condigo fuente
     */
    @FXML lateinit var codigoFuente: TextArea

    /*
        Tabla en donde se presentan los Tokens identificados
     */
    @FXML  lateinit var tablaTokens: TableView<Token>
    /*
        Cada una de las columnas de la tabla tokens
     */
    @FXML lateinit var CLexema: TableColumn<Token,String>

    @FXML lateinit var Ccategoria : TableColumn<Token,String>

    @FXML lateinit var Cfila : TableColumn<Token,Int>

    @FXML lateinit var Ccolumna : TableColumn<Token,Int>

    /*
       Tabla donde se identifican los errores
    */
    @FXML lateinit var tablaErrores: TableView<Error>

    /*
       Cada una de las columnas de la tabla de erroes
    */
    @FXML lateinit var cErrorMsj: TableColumn<Error,String>

    @FXML lateinit var cErrorFila: TableColumn<Error,Int>

    @FXML lateinit var cErrorColumna: TableColumn<Error,Int>


    override fun initialize(location: URL?, resources: ResourceBundle?)
    {
        CLexema.cellValueFactory= PropertyValueFactory("Lexema")
        Ccategoria.cellValueFactory = PropertyValueFactory("Categoria")
        Cfila.cellValueFactory = PropertyValueFactory("Fila")
        Ccolumna.cellValueFactory = PropertyValueFactory("Columna")

        cErrorMsj.cellValueFactory= PropertyValueFactory("Error")
        cErrorFila.cellValueFactory= PropertyValueFactory("Fila")
        cErrorColumna.cellValueFactory= PropertyValueFactory("Columna")


    }

    /*
        Accion del boton "Analizar Codigo" en donde se analiza el codigo fuente
     */
    @FXML
    fun analizarCodigo(e: ActionEvent)
    {

        if(codigoFuente.text.length>0)
        {

            try {
                var lexico = AnalizadorLexico(codigoFuente = codigoFuente.text)
                lexico.analizar()
                tablaTokens.items = FXCollections.observableArrayList(lexico.listaTokens)
                tablaErrores.items = FXCollections.observableArrayList(lexico.listaErroresLexicos)


            }catch (exception: Exception){
                var al: Alert = Alert(Alert.AlertType.ERROR,"Hay errores Léxicos, Revise la Tabla")
                al.showAndWait()
            }


        }
        else
        {
            print("El texto esta vacio")
        }



    }


}