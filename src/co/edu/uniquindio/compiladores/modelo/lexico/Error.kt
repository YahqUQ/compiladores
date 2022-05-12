package co.edu.uniquindio.compiladores.modelo.lexico
/*
 * @author: Jaime Nieto
 */
class Error {

    var error : String=""
    var fila : Int = 0
    var columna: Int = 0

    constructor(error:String , fila:Int,  columna:Int){
        this.error=error
        this.fila=fila
        this.columna=columna
    }

    constructor(error:String,toke:Token){
        this.error=error
        this.fila=toke.fila
        this.columna=toke.columna
    }

    override fun toString(): String {
        return "Error(error='$error',fila=$fila,columna=$columna)"


    }
}