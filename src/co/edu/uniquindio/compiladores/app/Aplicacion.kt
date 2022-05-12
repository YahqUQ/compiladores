package co.edu.uniquindio.compiladores.app

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
/*
 * @author: Jaime Nieto, Yiran Hernandez, Mauricio Duque
 */
class Aplicacion : Application() {

    override fun start(p0: Stage?) {
        val loader= FXMLLoader(Aplicacion::class.java.getResource("/inicio.fxml/"))
        print(Aplicacion::class.java.getResource("/inicio.fxml"))
        val parent:Parent = loader.load()
        val scena = Scene(parent)
        p0?.scene= scena
        p0?.title="Analizador Lexico"
        p0?.show()
    }

    companion object{
        @JvmStatic
        fun main (args :Array<String>)
        {

            launch(Aplicacion::class.java)
        }
    }

}