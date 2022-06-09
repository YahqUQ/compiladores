package co.edu.uniquindio.compiladores.controladores

import co.edu.uniquindio.compiladores.modelo.lexico.AnalizadorLexico
import co.edu.uniquindio.compiladores.modelo.lexico.Error
import co.edu.uniquindio.compiladores.modelo.lexico.Token
import co.edu.uniquindio.compiladores.modelo.semantica.AnalizadorSemantico
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
       Tabla donde se identifican los errores léxicos
    */
    @FXML lateinit var tablaErrores: TableView<Error>

    @FXML lateinit var tablaErroresS: TableView<Error>
    /*
       Cada una de las columnas de la tabla de errores léxicos
    */
    @FXML lateinit var cErrorMsj: TableColumn<Error,String>

    @FXML lateinit var cErrorFila: TableColumn<Error,Int>

    @FXML lateinit var cErrorColumna: TableColumn<Error,Int>

    /*
        Cada una de las columnas de la tabla de errores sintácticos
     */
    @FXML lateinit var cErrorMsjS: TableColumn<Error,String>

    @FXML lateinit var cErrorFilaS: TableColumn<Error,Int>

    @FXML lateinit var cErrorColumnaS: TableColumn<Error,Int>




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

        cErrorMsjS.cellValueFactory= PropertyValueFactory("Error")
        cErrorFilaS.cellValueFactory= PropertyValueFactory("Fila")
        cErrorColumnaS.cellValueFactory= PropertyValueFactory("Columna")


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
                tablaErroresS.items= FXCollections.observableArrayList(sintactico.listaErrores)


                if (uniCompilacion != null) {
                   arbolSintactico.root = uniCompilacion.getArbolVisual()

                    println("---------")

                    var semantico= AnalizadorSemantico(uniCompilacion)
                    semantico.llenarTablaSimbolos()
                    semantico.analizarSemantica()

                    for (s in semantico.tablaSimbolos.listaSimbolos) {
                        println(s)
                    }

                    for (e in semantico.tablaSimbolos.listaErrores) {
                        println(e)
                    }


                }else{
                    arbolSintactico.root= TreeItem("Unidad de Compilación")
                    var al: Alert = Alert(Alert.AlertType.ERROR,"Hay errores , Revise las tablas")
                    al.showAndWait()
                }



            }catch (exception: Exception){
                var al: Alert = Alert(Alert.AlertType.ERROR,"Hay errores , Revise las tablas")
                al.showAndWait()
            }


        }
        else
        {
            print("El texto esta vacio")
        }



    }


}