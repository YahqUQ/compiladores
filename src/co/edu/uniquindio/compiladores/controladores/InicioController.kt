package co.edu.uniquindio.compiladores.controladores

import co.edu.uniquindio.compiladores.modelo.lexico.AnalizadorLexico
import co.edu.uniquindio.compiladores.modelo.lexico.Error
import co.edu.uniquindio.compiladores.modelo.lexico.Token
import co.edu.uniquindio.compiladores.modelo.sintaxis.AnalizadorSintactico
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
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

    @FXML
    private var arbolSintactico: TreeView<String> = TreeView<String>()


    override fun initialize(location: URL?, resources: ResourceBundle?)
    {
        CLexema.cellValueFactory= PropertyValueFactory("Lexema")
        Ccategoria.cellValueFactory = PropertyValueFactory("Categoria")
        Cfila.cellValueFactory = PropertyValueFactory("Fila")
        Ccolumna.cellValueFactory = PropertyValueFactory("Columna")

        cErrorMsj.cellValueFactory= PropertyValueFactory("Error")
        cErrorFila.cellValueFactory= PropertyValueFactory("Fila")
        cErrorColumna.cellValueFactory= PropertyValueFactory("Columna")

        arbolSintactico.cellFactory = null

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

                var sintactico = AnalizadorSintactico(lexico.listaTokens)
                val uniCompilacion= sintactico.esUnidadDeCompilacion()

                if (uniCompilacion != null) {
                   arbolSintactico.root = uniCompilacion.getArbolVisual()
                }

                for(error in sintactico.listaErrores){
                    println(error)
                }


            }catch (exception: Exception){
                var al: Alert = Alert(Alert.AlertType.ERROR,"Hay errores , Revise la tabla")
                al.showAndWait()
            }


        }
        else
        {
            print("El texto esta vacio")
        }



    }


}