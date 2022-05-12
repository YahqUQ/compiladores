package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Categoria
import co.edu.uniquindio.compiladores.modelo.lexico.Token
import co.edu.uniquindio.compiladores.modelo.lexico.Error

import kotlin.collections.ArrayList
import kotlin.math.exp

/*
 * @author: Jaime Nieto, Yiran Hernandez, Mauricio Duque
 */
class AnalizadorSintactico(private var listaTokens: ArrayList<Token>) {


    private var posicionActual = 0
    private var tokenActual: Token = listaTokens[posicionActual]
    var listaErrores: ArrayList<Error> = ArrayList()

    /*
    Obtener el Siguiente Token de la Lista Generada por el Analizador Léxico
     */
    private fun obtenerSgteToken() {

        posicionActual++
        tokenActual = if (posicionActual < listaTokens.size) {
            listaTokens[posicionActual]
        } else {

            Token(0.toChar() + "", Categoria.FIN_CODIGO, tokenActual.fila, tokenActual.columna+1)
        }
    }

    /*
     * crea un error
     */
    private fun reportarError(mensaje: String) {
        val error = Error(mensaje, tokenActual)
        listaErrores.add(error)
    }

    /*
    <Unidad de Compilación> ::= <Lista de Elementos>
     */
    fun esUnidadDeCompilacion(): UnidadDeCompilacion? {
        val listaElementos: ArrayList<Elemento> = esListaElemento()
        if (listaElementos.size > 0) {
            return UnidadDeCompilacion(listaElementos)
        }
        return null
    }

    /*
    <Lista de Elementos> ::= <Elemento> [<Lista de Elementos>]
     */
    private fun esListaElemento(): ArrayList<Elemento> {

        val listaElementos = ArrayList<Elemento>()
        var elemento: Elemento? = esElemento()

        while (tokenActual.categoria != Categoria.FIN_CODIGO) {

            if (elemento == null) {

                while (tokenActual.categoria != Categoria.LLAVE_DER && tokenActual.categoria != Categoria.TERMINAL
                    && tokenActual.categoria != Categoria.FIN_CODIGO) {
                    obtenerSgteToken()
                }
                obtenerSgteToken()
                if (posicionActual >= listaTokens.size) {
                    break
                }
                elemento = esElemento()
            } else {
                obtenerSgteToken()
                if (posicionActual >= listaTokens.size) {
                    break
                }
                listaElementos.add(elemento)
                elemento = esElemento()
            }
        }
        return listaElementos
    }

    /*
    <Elemento> ::= <Función> | <Variable Global>
     */
    private fun esElemento(): Elemento? {

        var e:Elemento?= esFuncion()
        if (e != null) {
            println("Elemento Exitoso")
            return e

        }

        e = esVariableGlobal()
        if (e != null) {
            obtenerSgteToken()
            println("Elemento Exitoso")
            return e
        } else {
            reportarError("No es una Variable Global o una Función Válida")
        }


        return null
    }

    /*
    <Variable Global> ::= GLOBAL <Declaración-Asignación>
     */
    private fun esVariableGlobal(): VariableGlobal? {

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "GLOBAL") {
            obtenerSgteToken()
            if (esDeclaracionAsignacion() != null) {
                println("Variable Global Exitosa")
                return VariableGlobal()
            }
        }
        return null
    }

    /*
    <Declaración-Asignación> ::=  [CONST ]  Identificador “;” <Tipo de Dato> “:” <Expresión> “|”
     */
    private fun esDeclaracionAsignacion(): DeclaracionAsignacion? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "CONST") {
            val tipoVariable = "INMUTABLE"
            obtenerSgteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                val nombre = tokenActual.lexema
                obtenerSgteToken()
                if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                    obtenerSgteToken()
                    if (esTipoDato() != null) {
                        val tipoDato = esTipoDato()
                        if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION) {
                            obtenerSgteToken()
                            if (esExpresion() != null) {
                                obtenerSgteToken()
                                if(tokenActual.categoria==Categoria.TERMINAL){
                                    println("Declaracion Asignacion exitosa")
                                    return DeclaracionAsignacion( nombre, tipoVariable, tipoDato, Expresion())
                                }else{
                                    reportarError("Se esperaba fin de sentencia '|'")
                                }
                            }
                        } else {
                            reportarError("Se esperaba ':'")
                        }
                    }else{
                        reportarError("Falta tipo Dato o Array de variable")
                    }
                } else {
                    reportarError("Se esperaba ';'")
                }
            } else {
                reportarError("Falta Identificador")
            }
        } else if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
            val tipoVariable = "MUTABLE"
            val nombre = tokenActual.lexema
            obtenerSgteToken()
            if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                obtenerSgteToken()
                if (esTipoDato() != null||esTipoArray() != null) {
                    val tipoDato = esTipoDato()
                    obtenerSgteToken()
                    if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION) {
                        obtenerSgteToken()
                        if (esExpresion() != null) {
                            obtenerSgteToken()
                            if(tokenActual.categoria==Categoria.TERMINAL){
                                println("Declaracion Asignacion exitosa")
                                return DeclaracionAsignacion( nombre, tipoVariable, tipoDato, Expresion())
                            }else{
                                reportarError("Se esperaba fin de sentencia")
                            }
                        }
                    } else {
                        reportarError("Se esperaba ':'")
                    }
                }else{
                    reportarError("Falta Tipo Dato o Array de variable ")
                }
            } else {
                reportarError("Se esperaba ';'")
            }
        } else {
            reportarError("Falta Identificador")
        }

        return null
    }

    /*
    <Tipo de Dato> ::=  CHAR | NUMBER_Z | NUMBER_F | STRING | BOOLEAN
     */
    private fun esTipoDato(): TipoDato? {
        return if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && (tokenActual.lexema == "CHAR"
                    || tokenActual.lexema == "NUMBER_F" || tokenActual.lexema == "NUMBER_Z"
                    || tokenActual.lexema == "BOOLEAN" || tokenActual.lexema == "STRING")) {
            val tipoDato = tokenActual
            TipoDato(tipoDato)
        } else {
            null
        }
    }

    /*
    <Función> ::= FUNCTION Identificador “(“ [<Lista Parámetros>] “)” “;” (<Tipo de Dato> | NONE)  “{“ [<Lista Sentencias>] “}”
     */
    private fun esFuncion(): Funcion? {

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "FUNCTION") {
            obtenerSgteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                val nombre: Token = tokenActual
                obtenerSgteToken()
                if (tokenActual.categoria == Categoria.PARENTESIS_IZQ) {
                    obtenerSgteToken()
                    val listaParametros = esListaParametros()
                    if (listaParametros != null && listaParametros.size > 0) {
                        if (tokenActual.categoria == Categoria.PARENTESIS_DER) {
                            obtenerSgteToken()
                            if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                                obtenerSgteToken()
                                if (tokenActual.lexema == "NONE" || esTipoDato() != null) {
                                    obtenerSgteToken()
                                    if (tokenActual.categoria == Categoria.LLAVE_IZQ) {
                                        obtenerSgteToken()
                                        val listaSentencia=esListaSentencias()
                                        if(listaSentencia!=null && listaSentencia.size > 0) {
                                            obtenerSgteToken()
                                            if (tokenActual.categoria == Categoria.LLAVE_DER) {
                                                println("Funcion Exitosa")

                                                return Funcion(
                                                    nombre,
                                                   listaParametros,
                                                    TipoDato(tokenActual),
                                                    listaSentencia
                                                )
                                            } else {
                                                reportarError("Se esperaba '}'")
                                            }
                                        }
                                    } else {
                                        reportarError("Se esperaba '{'")
                                    }
                                }else{
                                    reportarError("Tipo de Dato de Retorno Inválido")
                                }
                            } else {
                                reportarError("Se esperaba ';'")
                            }
                        } else {
                            reportarError("Se esperaba ')'")
                        }
                    }
                } else {
                    reportarError("Se esperaba '('")
                }
            } else {
                reportarError("Falta Identificador")
            }
        }
        return null
    }


    /*
    <Lista Parámetros> ::= <Parámetro> [Separador <Lista Parámetros>]
     */
    private fun esListaParametros(): ArrayList<Parametro>? {
        val listaParametro = ArrayList<Parametro>()
        var parametro: Parametro? = esParametro()

        while (parametro != null) {
            if (listaParametro.size >= 1) {
                obtenerSgteToken()
                if (tokenActual.categoria == Categoria.SEPARADOR) {
                    obtenerSgteToken()
                    listaParametro.add(parametro)

                    if (tokenActual.categoria == Categoria.PARENTESIS_DER) {
                        break
                    }
                    parametro = esParametro()
                } else {
                    reportarError("Falta Separador '_' entre parametros")
                    return null
                }
            } else {
                obtenerSgteToken()
                listaParametro.add(parametro)

                if (tokenActual.categoria == Categoria.PARENTESIS_DER) {
                    break
                }
                parametro = esParametro()
            }

        }
        return listaParametro

    }

    /*
    <Parámetro> ::= Identificador ";" (<Tipo de Dato> | <Tipo de Array>)
     */
    private fun esParametro(): Parametro? {
        if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
            val nombre = tokenActual
            obtenerSgteToken()
            if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                obtenerSgteToken()

                if (esTipoArray() != null) {
                    val tipoArray = esTipoArray()
                    println("Parametro Exitoso")
                    return Parametro(nombre, tipoArray)
                } else if (esTipoDato() != null) {
                    val tipoDato = esTipoDato()
                    println("Parametro Exitoso")
                    return Parametro(nombre, tipoDato)
                } else {
                    reportarError("No es un tipo de Dato o Array Válido")
                }
            } else {
                reportarError("Se esperaba ';")
            }
        } else {
            reportarError("Falta Identificador")
        }
        return null
    }

    /*
    <Tipo de Dato> ::=  CHARs | NUMBER_Zs | NUMBER_Fs | STRINGs | BOOLEANs
     */
    private fun esTipoArray(): TipoArray? {
        return if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && (tokenActual.lexema == "CHARs"
                    || tokenActual.lexema == "NUMBER_Fs" || tokenActual.lexema == "NUMBER_Zs"
                    || tokenActual.lexema == "BOOLEANs" || tokenActual.lexema == "STRINGs")) {
            TipoArray(tokenActual)
        } else {
            null
        }
    }

    /*
    <Lista Sentencias> ::= <Sentencia> [ Lista Sentencias]
     */
    private fun esListaSentencias(): ArrayList<Sentencia>? {

        val listaSentencia = ArrayList<Sentencia>()
        var sentencia: Sentencia? = esSentencia()

        while (sentencia != null) {
                obtenerSgteToken()
                listaSentencia.add(sentencia)

                if (tokenActual.categoria == Categoria.LLAVE_DER) {
                    break
                }
                sentencia = esSentencia()
        }
        return listaSentencia
    }

    /*
    <Sentencia> :: = <Decisión> | <Declaración de Variable> | <Asignación de Variable> | Declaración-Asignación
                | <Impresión> | <Ciclo While> | <Retorno> | <Lectura> | <Invocación de Función>
                | <Declaración Array> | <Inicialización Array>
     */
    private fun esSentencia(): Sentencia? {

        var s :Sentencia? = esDecision()
        if(s!=null){
            return s
        }
        s= esDeclaracion()
        if(s!=null){
            return s
        }
        s=esAsignacion()
        if(s!=null){
            return s
        }
        s=esImpresion()
        if(s!=null){
            return s
        }
        s=esCicloWhile()
        if(s!=null){
            return s
        }
        s=esRetorno()
        if(s!=null){
            return s
        }
        s=esLectura()
        if(s!=null){
            return s
        }
        s=esInvocacionDeFuncion()
        if(s!=null){
            return s
        }
        s=esDeclaracionArray()
        if(s!=null){
            return s
        }
        s=esAsignacionArray()
        if(s!=null){
            return s
        }
        return null
    }

    /*
    <Decisión>::= IF “(“ <Expresión Lógica> “)” “{“  Lista Sentencias “}” [ ELSE ”{“ Lista Sentencias “}” ]
     */
    private fun esDecision(): Sentencia? {

        if(tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="IF"){
            obtenerSgteToken()
            if(tokenActual.categoria==Categoria.PARENTESIS_IZQ){
                obtenerSgteToken()
                var condicion=esExpresionLogica()
                if(condicion!=null){
                    obtenerSgteToken()
                    if(tokenActual.categoria==Categoria.PARENTESIS_DER){
                        obtenerSgteToken()
                        if(tokenActual.categoria==Categoria.LLAVE_IZQ){
                            obtenerSgteToken()
                            val listaSentenciasIF=esListaSentencias()
                            if(listaSentenciasIF!=null){
                                obtenerSgteToken()
                                if(tokenActual.categoria==Categoria.LLAVE_DER) {
                                    obtenerSgteToken()
                                    if(tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="ELSE"){
                                        obtenerSgteToken()
                                        if(tokenActual.categoria==Categoria.LLAVE_IZQ){
                                            obtenerSgteToken()
                                            val listaSentenciasElSE=esListaSentencias()
                                            if(listaSentenciasElSE!=null){
                                                obtenerSgteToken()
                                                if(tokenActual.categoria==Categoria.LLAVE_DER) {
                                                    println("IF EXITOSO")
                                                    return Decision(condicion, listaSentenciasIF, listaSentenciasElSE)
                                                }
                                            }
                                        }else{

                                        }
                                    }else{
                                        println("IF EXITOSO")
                                        return Decision(condicion,listaSentenciasIF)
                                    }
                                }else{
                                    reportarError("Se esperaba '}'")
                                }
                            }
                        }else{
                            reportarError("Se esperaba '{'")
                        }
                    }else{
                        reportarError("Se esperaba '('")
                    }
                }
            }else{
                reportarError("Se esperaba '('")
            }
        }

        return null
    }

    /*
    <Ciclo While> ::= WHILE “(“ <Expresión Lógica> “)” “{“ Lista Sentencias “}”
     */

    private fun esCicloWhile(): Sentencia? {
        if(tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="WHILE"){
            obtenerSgteToken()
            if(tokenActual.categoria==Categoria.PARENTESIS_IZQ){
                obtenerSgteToken()
                val condicion=esExpresionLogica()
                if(condicion!=null){
                    obtenerSgteToken()
                    if(tokenActual.categoria==Categoria.PARENTESIS_DER){
                        obtenerSgteToken()
                        if(tokenActual.categoria==Categoria.LLAVE_IZQ){
                            obtenerSgteToken()
                            val listaSentencia= esListaSentencias()
                            if(listaSentencia!=null){
                                obtenerSgteToken()
                                if(tokenActual.categoria==Categoria.LLAVE_DER){
                                    println("WHILE EXITOSO")
                                    return While(condicion,listaSentencia)
                                }else{
                                    reportarError("Se esperaba '}'")
                                }
                            }
                        }else{
                            reportarError("Se esperaba '{'")
                        }
                    }else{
                        reportarError("Se esperaba ')'")
                    }
                }
            }else{
                reportarError("Se esperaba '('")
            }
        }
        return null
    }

    /*
    <Declaración de Variable> ::= [CONST ]  Identificador “;” <Tipo de Dato> “|”
     */
    private fun esDeclaracion(): Sentencia? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "CONST") {
            val tipoVariable = "INMUTABLE"
            obtenerSgteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                val nombre = tokenActual.lexema
                obtenerSgteToken()
                if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                    obtenerSgteToken()
                    if (esTipoDato() != null) {
                        val tipoDato = esTipoDato()
                                if(tokenActual.categoria==Categoria.TERMINAL){
                                    println("Declaracion Asignacion exitosa")
                                    return DeclaracionAsignacion( nombre, tipoVariable, tipoDato, Expresion())
                                }else{
                                    reportarError("Se esperaba fin de sentencia '|'")
                                }
                    }else{
                        reportarError("Falta tipo Dato o Array de variable")
                    }
                } else {
                    reportarError("Se esperaba ';'")
                }
            } else {
                reportarError("Falta Identificador")
            }
        } else if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
            val tipoVariable = "MUTABLE"
            val nombre = tokenActual.lexema
            obtenerSgteToken()
            if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                obtenerSgteToken()
                if (esTipoDato() != null||esTipoArray() != null) {
                    val tipoDato = esTipoDato()
                    obtenerSgteToken()
                    if(tokenActual.categoria==Categoria.TERMINAL){
                        println("Declaracion Asignacion exitosa")
                        return DeclaracionAsignacion( nombre, tipoVariable, tipoDato, Expresion())
                    }else {
                        reportarError("Se esperaba fin de sentencia")
                    }
                }else{
                    reportarError("Falta Tipo Dato o Array de variable ")
                }
            } else {
                reportarError("Se esperaba ';'")
            }
        } else {
            reportarError("Falta Identificador")
        }

        return null
    }

    /*
   <Asignación de Variable> ::=  Identificador “:” ( <Expresión> | <Invocación de Función> | Identificador) “|”
    */
    private fun esAsignacion(): Sentencia? {

        if(tokenActual.categoria==Categoria.IDENTIFICADOR){
            val nombre= tokenActual.lexema
            obtenerSgteToken()
            if(tokenActual.categoria==Categoria.OPERADOR_ASIGNACION){
                obtenerSgteToken()
                val expresion= esExpresion()
                if(expresion !=null){
                    obtenerSgteToken()
                    if(tokenActual.categoria==Categoria.TERMINAL){
                        println("Asignacion EXITOSA")
                        return Asignacion(nombre,expresion)
                    }else{
                        reportarError("Se esperaba fin de sentencia '|'")
                    }
                }else{
                    val invocacionDeFuncion = esInvocacionDeFuncion()
                    if(invocacionDeFuncion!=null){
                        obtenerSgteToken()
                        if(tokenActual.categoria==Categoria.TERMINAL){
                            return Asignacion(nombre, invocacionDeFuncion)
                        }else{
                            reportarError("Se esperaba fin de sentencia '|'")
                        }
                    } else {
                        if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                            val nombreAsig= tokenActual.lexema
                            obtenerSgteToken()
                            if(tokenActual.categoria==Categoria.TERMINAL){
                                return Asignacion(nombre,nombreAsig)
                            }else{
                                reportarError("Se esperaba fin de sentencia '|'")
                            }
                        }
                    }
                }
            }else{
                reportarError("Se esperaba ':'")
            }
        }
        return null
    }


    private fun esExpresionLogica(): ExpresionLogica? {

        return null
    }

    private fun esAsignacionArray(): Sentencia? {

    }

    private fun esDeclaracionArray(): Sentencia? {

    }

    private fun esInvocacionDeFuncion(): InvocacionFuncion? {

    }

    private fun esLectura(): Sentencia? {

    }

    private fun esRetorno(): Sentencia? {

    }


    private fun esImpresion(): Sentencia? {

    }






    private fun esExpresion(): Expresion? {

        return Expresion()
    }


}