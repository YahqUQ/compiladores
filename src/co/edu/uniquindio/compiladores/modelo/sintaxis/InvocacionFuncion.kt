package co.edu.uniquindio.compiladores.modelo.sintaxis

class InvocacionFuncion : Sentencia {

    override var sentencia: Sentencia

    init {
        this.sentencia=this

    }

    constructor(nombre:String,listaArgumentos:ArrayList<Argumento>)

}
