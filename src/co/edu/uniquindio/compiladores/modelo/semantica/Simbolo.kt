package co.edu.uniquindio.compiladores.modelo.semantica

class Simbolo {

    var nombre: String = ""
    var tipo: String = ""
    var fila = 0
    var columna = 0
    var ambito: String = ""
    var modificable: Boolean= false
    var tipoParametros: ArrayList<String>? = null

    /**
     * Constructor para Simbolos de tipo Declaracion
     */
    constructor(nombre: String, tipo: String,  modificable:Boolean, ambito: String, fila:Int, columna:Int){
        this.nombre = nombre
        this.tipo = tipo
        this.ambito = ambito
        this.fila = fila
        this.columna = columna
        this.modificable=modificable
    }

    /**
     * Constructor para Simbolos de tipo Función o Método
     */
    constructor(nombre: String, tipo: String, tipoParametros: ArrayList<String>) {
        this.nombre = nombre
        this.tipo = tipo
        this.tipoParametros = tipoParametros
        this.fila = fila
        this.columna =columna
    }

    override fun toString(): String {
        return "Simbolo(nombre='$nombre', tipo='$tipo', fila=$fila, columna=$columna, ambito='$ambito', modificable=$modificable, tipoParametros=$tipoParametros)"
    }

}