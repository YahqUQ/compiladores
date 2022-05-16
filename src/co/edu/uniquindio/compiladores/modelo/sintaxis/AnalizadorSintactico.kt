package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Categoria
import co.edu.uniquindio.compiladores.modelo.lexico.Token
import co.edu.uniquindio.compiladores.modelo.lexico.Error


import kotlin.collections.ArrayList

/*
 * @author: Jaime Nieto, Yiran Hernandez, Mauricio Duque
 */
class AnalizadorSintactico(private var listaTokens: ArrayList<Token>) {


    private var posicionActual = 0
    private var posBack = 0
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
    Obtiene el token de la posicion dada
     */
    private fun obtenerTokenPosicion(pos:Int) {

        posicionActual= pos
        tokenActual = if (posicionActual < listaTokens.size) {
            listaTokens[posicionActual]
        } else {
            Token(0.toChar() + "", Categoria.FIN_CODIGO, tokenActual.fila, tokenActual.columna+1)
        }
    }

    /*
    Ignora todos los tokens hasta un token de escape
    */
    private fun ignorarTokensHasta(escape: ArrayList<String>){
        while (!(tokenActual.lexema in escape)) {
            obtenerSgteToken()
            if(tokenActual.categoria==Categoria.FIN_CODIGO){
                reportarError("No tiene sentido este código")
                break
            }
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
            val escapes:ArrayList<String> = ArrayList()
            escapes.add("FUNCTION")
            escapes.add("GLOBAL")
            if (elemento == null) {
                ignorarTokensHasta(escapes)
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
    private fun esDeclaracionAsignacion(): DeclaracionAsignacionVariable? {

        posBack=posicionActual
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
                        obtenerSgteToken()

                        if(tokenActual.categoria==Categoria.TERMINAL){
                            obtenerTokenPosicion(posBack)
                            return null
                        }

                        if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION) {
                            obtenerSgteToken()
                            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                                val nombreAsig = tokenActual.lexema
                                posBack=posicionActual
                                obtenerSgteToken()

                                if(tokenActual.categoria==Categoria.PARENTESIS_IZQ){
                                    obtenerTokenPosicion(posBack)
                                    val invocacionDeFuncion = esInvocacionDeFuncion()
                                    if (invocacionDeFuncion != null) {
                                        if (tokenActual.categoria == Categoria.TERMINAL) {
                                            println("Asignación Exitosa")
                                            return DeclaracionAsignacionVariable(nombre,tipoVariable,tipoDato,invocacionDeFuncion)
                                        } else {
                                            reportarError("Se esperaba fin de sentencia '|'")
                                        }
                                    }
                                    return null
                                }

                                if(tokenActual.categoria==Categoria.OPERADOR_ARITMETICO||tokenActual.categoria==Categoria.OPERADOR_RELACIONAL
                                    ||tokenActual.categoria==Categoria.OPERADOR_LOGICO){
                                    obtenerTokenPosicion(posBack)
                                    val expresion = esExpresion()
                                    if (expresion != null) {
                                        if (tokenActual.categoria == Categoria.TERMINAL) {
                                            println("Asignacion EXITOSA")
                                            return DeclaracionAsignacionVariable(nombre,tipoVariable,tipoDato,expresion)
                                        } else {
                                            reportarError("Se esperaba fin de sentencia '|'")
                                        }
                                    }
                                    return null
                                }

                                if (tokenActual.categoria == Categoria.TERMINAL) {
                                    println("Asignación Exitosa")
                                    return DeclaracionAsignacionVariable(nombre,tipoVariable,tipoDato,nombreAsig)
                                } else {
                                    reportarError("Se esperaba fin de sentencia '|'")
                                }
                            } else {

                                reportarError("No es una expresion ni una invocacion de funcion ni una variable")

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
        }

        if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
            val tipoVariable = "MUTABLE"
            val nombre = tokenActual.lexema
            obtenerSgteToken()

            if(tokenActual.categoria==Categoria.OPERADOR_ASIGNACION||tokenActual.categoria==Categoria.PARENTESIS_IZQ
                ||tokenActual.categoria==Categoria.INCREMENTO||tokenActual.categoria==Categoria.DECREMENTO){
                obtenerTokenPosicion(posBack)
                return null
            }

            if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                obtenerSgteToken()
                if (esTipoDato() != null) {
                    val tipoDato = esTipoDato()
                    obtenerSgteToken()

                    if(tokenActual.categoria==Categoria.TERMINAL){
                        obtenerTokenPosicion(posBack)
                        return null
                    }

                    if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION) {
                        obtenerSgteToken()
                        if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                            val nombreAsig = tokenActual.lexema
                            posBack=posicionActual
                            obtenerSgteToken()

                            if(tokenActual.categoria==Categoria.PARENTESIS_IZQ){
                                obtenerTokenPosicion(posBack)
                                val invocacionDeFuncion = esInvocacionDeFuncion()
                                if (invocacionDeFuncion != null) {
                                    if (tokenActual.categoria == Categoria.TERMINAL) {
                                        println("Asignación Exitosa")
                                        return DeclaracionAsignacionVariable(nombre,tipoVariable,tipoDato,invocacionDeFuncion)
                                    } else {
                                        reportarError("Se esperaba fin de sentencia '|'")
                                    }
                                }
                                return null
                            }

                            if(tokenActual.categoria==Categoria.OPERADOR_ARITMETICO||tokenActual.categoria==Categoria.OPERADOR_RELACIONAL
                                ||tokenActual.categoria==Categoria.OPERADOR_LOGICO){
                                obtenerTokenPosicion(posBack)
                                val expresion = esExpresion()
                                if (expresion != null) {
                                    if (tokenActual.categoria == Categoria.TERMINAL) {
                                        println("Asignacion EXITOSA")
                                        return DeclaracionAsignacionVariable(nombre,tipoVariable,tipoDato,expresion)
                                    } else {
                                        reportarError("Se esperaba fin de sentencia '|'")
                                    }
                                }
                                return null
                            }

                            if (tokenActual.categoria == Categoria.TERMINAL) {
                                println("Asignación Exitosa")
                                return DeclaracionAsignacionVariable(nombre,tipoVariable,tipoDato,nombreAsig)
                            } else {
                                reportarError("Se esperaba fin de sentencia '|'")
                            }
                        } else {

                            reportarError("No es una expresion ni una invocacion de funcion ni una variable")

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
                    if (listaParametros != null) {
                        if (tokenActual.categoria == Categoria.PARENTESIS_DER) {
                            obtenerSgteToken()
                            if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                                obtenerSgteToken()
                                val tipoDato= esTipoDato()
                                if (tokenActual.lexema == "NONE" || tipoDato != null) {
                                    obtenerSgteToken()
                                    if (tokenActual.categoria == Categoria.LLAVE_IZQ) {
                                        obtenerSgteToken()
                                        val listaSentencia=esListaSentencias()
                                        if(listaSentencia!=null) {
                                            if (tokenActual.categoria == Categoria.LLAVE_DER) {
                                                println("Funcion Exitosa")

                                                return Funcion(
                                                    nombre,
                                                   listaParametros,
                                                    tipoDato,
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
            listaParametro.add(parametro)

            obtenerSgteToken()

            if (tokenActual.categoria == Categoria.PARENTESIS_DER) {
                break
            }

            if (tokenActual.categoria == Categoria.SEPARADOR) {
                obtenerSgteToken()
            }else {
                reportarError("Falta Separador '_' entre parametros")
                return null
            }
            parametro = esParametro()
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
   <Tipo de Dato> ::=  CHARs^ | NUMBER_Zs^ | NUMBER_Fs^ | STRINGs^ | BOOLEANs^
    */
    private fun esTipoArrayB(): TipoArrayB? {
        return if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && (tokenActual.lexema == "CHARs^"
                    || tokenActual.lexema == "NUMBER_Fs^" || tokenActual.lexema == "NUMBER_Zs^"
                    || tokenActual.lexema == "BOOLEANs^" || tokenActual.lexema == "STRINGs^")) {
            TipoArrayB(tokenActual)
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
        s=esAsignacion()
        if(s!=null){
            return s
        }
        s= esDeclaracion()
        if(s!=null){
            return s
        }
        s= esDeclaracionAsignacion()
        if(s!=null){
            return s
        }
        s=esIncremento()
        if(s!=null){
            return s
        }
        s=esDecremento()
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
/*
        s=esDeclaracionArrayB()
        if(s!=null){
            return s
        }
 */

        s=esAsignacionArrayB()
        if(s!=null){
            return s
        }

        return null
    }

    /*
    <Decremento> ::= Identificador DEC “|”
     */
    private fun esDecremento(): Sentencia? {
        posBack=posicionActual
        if(tokenActual.categoria==Categoria.IDENTIFICADOR){
            val variable=tokenActual.lexema
            obtenerSgteToken()
            if(tokenActual.categoria==Categoria.OPERADOR_ASIGNACION||tokenActual.categoria==Categoria.PARENTESIS_IZQ
                ||tokenActual.categoria==Categoria.INCREMENTO||tokenActual.categoria==Categoria.DOS_PUNTOS){
                obtenerTokenPosicion(posBack)
                return null
            }

            if(tokenActual.categoria==Categoria.DECREMENTO){
                obtenerSgteToken()
                if(tokenActual.categoria==Categoria.TERMINAL){
                    println("Decremento Exitoso")
                    return Decremento(variable)
                }else{
                    reportarError("Se esperaba fin de sentencia '|'")
                }
            }else{
                reportarError("No es decremento válido")
            }
        }
        return null
    }

    /*
    <Incremento> ::= Identificador INC “|”
     */
    private fun esIncremento(): Sentencia? {
        posBack=posicionActual
        if(tokenActual.categoria==Categoria.IDENTIFICADOR){
            val variable=tokenActual.lexema
            obtenerSgteToken()
            if(tokenActual.categoria==Categoria.OPERADOR_ASIGNACION||tokenActual.categoria==Categoria.PARENTESIS_IZQ
                ||tokenActual.categoria==Categoria.DECREMENTO||tokenActual.categoria==Categoria.DOS_PUNTOS){
                obtenerTokenPosicion(posBack)
                return null
            }

            if(tokenActual.categoria==Categoria.INCREMENTO){
                obtenerSgteToken()
                if(tokenActual.categoria==Categoria.TERMINAL){
                    println("Incremento Exitoso")
                    return Incremento(variable)
                }else{
                    reportarError("Se esperaba fin de sentencia '|'")
                }
            }else{
                reportarError("No es incremento válido")
            }
        }
        return null
    }

    /*
    <Decisión>::= IF “(“ (<Expresión Lógica> | <Expresión Relacional>) “)” “{“  Lista Sentencias “}” [ ELSE ”{“ Lista Sentencias “}” ]
     */
    private fun esDecision(): Sentencia? {

        if(tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="IF") {
            obtenerSgteToken()
            if (tokenActual.categoria == Categoria.PARENTESIS_IZQ) {
                obtenerSgteToken()
                val condicionLogica = esExpresionLogica()
                if (condicionLogica != null) {
                    if (tokenActual.categoria == Categoria.PARENTESIS_DER) {
                        obtenerSgteToken()
                        if (tokenActual.categoria == Categoria.LLAVE_IZQ) {
                            obtenerSgteToken()
                            val listaSentenciasIF = esListaSentencias()
                            if (listaSentenciasIF != null) {
                                if (tokenActual.categoria == Categoria.LLAVE_DER) {
                                    obtenerSgteToken()
                                    if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "ELSE") {
                                        obtenerSgteToken()
                                        if (tokenActual.categoria == Categoria.LLAVE_IZQ) {
                                            obtenerSgteToken()
                                            val listaSentenciasElSE = esListaSentencias()
                                            if (listaSentenciasElSE != null) {
                                                if (tokenActual.categoria == Categoria.LLAVE_DER) {
                                                    println("IF EXITOSO")
                                                    return Decision(condicionLogica, listaSentenciasIF, listaSentenciasElSE)
                                                } else {
                                                    reportarError("Se esperaba")
                                                }
                                            }
                                        } else {
                                            reportarError("Se esperaba {")
                                        }
                                    } else {
                                        obtenerTokenPosicion(posicionActual-1)
                                        println("IF EXITOSO")
                                        return Decision(condicionLogica, listaSentenciasIF)
                                    }
                                } else {
                                    reportarError("Se esperaba '}'")
                                }
                            }
                        } else {
                            reportarError("Se esperaba '{'")
                        }
                    } else {
                        reportarError("Se esperaba '('")
                    }
                } else {
                   val condicionRelacional = esExpresionRelacional()
                    if (condicionRelacional != null) {
                        if (tokenActual.categoria == Categoria.PARENTESIS_DER) {
                            obtenerSgteToken()
                            if (tokenActual.categoria == Categoria.LLAVE_IZQ) {
                                obtenerSgteToken()
                                val listaSentenciasIF = esListaSentencias()
                                if (listaSentenciasIF != null) {
                                    if (tokenActual.categoria == Categoria.LLAVE_DER) {
                                        obtenerSgteToken()
                                        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "ELSE") {
                                            obtenerSgteToken()
                                            if (tokenActual.categoria == Categoria.LLAVE_IZQ) {
                                                obtenerSgteToken()
                                                val listaSentenciasElSE = esListaSentencias()
                                                if (listaSentenciasElSE != null) {
                                                    if (tokenActual.categoria == Categoria.LLAVE_DER) {
                                                        println("IF Exitoso")
                                                        return Decision(
                                                            condicionRelacional,
                                                            listaSentenciasIF,
                                                            listaSentenciasElSE
                                                        )
                                                    } else {
                                                        reportarError("Se esperaba")
                                                    }
                                                }
                                            } else {
                                                reportarError("Se esperaba {")
                                            }
                                        } else {
                                            println("IF Exitoso")
                                            return Decision(condicionRelacional, listaSentenciasIF)
                                        }
                                    } else {
                                        reportarError("Se esperaba '}'")
                                    }
                                }
                            } else {
                                reportarError("Se esperaba '{'")
                            }
                        } else {
                            reportarError("Se esperaba '('")
                        }
                    } else {
                        reportarError("Se esperaba '('")
                    }
                }
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
                val condicionLogica=esExpresionLogica()
                if(condicionLogica!=null){
                    if(tokenActual.categoria==Categoria.PARENTESIS_DER){
                        obtenerSgteToken()
                        if(tokenActual.categoria==Categoria.LLAVE_IZQ){
                            obtenerSgteToken()
                            val listaSentencia= esListaSentencias()
                            if(listaSentencia!=null){
                                if(tokenActual.categoria==Categoria.LLAVE_DER){
                                    println("WHILE EXITOSO")
                                    return CicloWhile(condicionLogica,listaSentencia)
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
                }else {
                    val condicionRelacional = esExpresionRelacional()
                    if (condicionRelacional != null) {
                        obtenerSgteToken()
                        if (tokenActual.categoria == Categoria.PARENTESIS_DER) {
                            obtenerSgteToken()
                            if (tokenActual.categoria == Categoria.LLAVE_IZQ) {
                                obtenerSgteToken()
                                val listaSentencia = esListaSentencias()
                                if (listaSentencia != null) {
                                    obtenerSgteToken()
                                    if (tokenActual.categoria == Categoria.LLAVE_DER) {
                                        println("WHILE EXITOSO")
                                        return CicloWhile(condicionRelacional, listaSentencia)
                                    } else {
                                        reportarError("Se esperaba '}'")
                                    }
                                }
                            } else {
                                reportarError("Se esperaba '{'")
                            }
                        } else {
                            reportarError("Se esperaba ')'")
                        }
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
        posBack=posicionActual
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "CONST") {
            val tipoVariable = "INMUTABLE"
            obtenerSgteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                val nombre = tokenActual.lexema
                obtenerSgteToken()

                if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                    obtenerSgteToken()
                    val tipoDato=esTipoDato()
                    if (tipoDato != null) {
                        obtenerSgteToken()
                        if(tokenActual.categoria==Categoria.OPERADOR_ASIGNACION){
                            obtenerTokenPosicion(posBack)
                            return null
                        }
                        if(tokenActual.categoria==Categoria.TERMINAL){
                            println("Declaracion exitosa")
                            return DeclaracionVariable( nombre, tipoVariable, tipoDato)
                        }else{
                            reportarError("Se esperaba fin de sentencia '|'")
                        }
                    }else{
                        reportarError("Falta tipo Dato variable")
                    }
                } else {
                    reportarError("Se esperaba ';'")
                }
            } else {
                reportarError("Falta Identificador")
            }
        }

        posBack=posicionActual
        if (tokenActual.categoria == Categoria.IDENTIFICADOR) {

            val tipoVariable = "MUTABLE"
            val nombre = tokenActual.lexema
            obtenerSgteToken()

            if(tokenActual.categoria==Categoria.OPERADOR_ASIGNACION||tokenActual.categoria==Categoria.PARENTESIS_IZQ
                ||tokenActual.categoria==Categoria.INCREMENTO||tokenActual.categoria==Categoria.DECREMENTO){
                obtenerTokenPosicion(posBack)
                return null
            }

            if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                obtenerSgteToken()
                val tipoDato = esTipoDato()
                if (esTipoDato() != null) {
                    obtenerSgteToken()
                    if(tokenActual.categoria==Categoria.OPERADOR_ASIGNACION){
                        obtenerTokenPosicion(posBack)
                        return null
                    }
                    if(tokenActual.categoria==Categoria.TERMINAL){
                        println("Declaracion  exitosa")
                        return DeclaracionVariable( nombre, tipoVariable, tipoDato)
                    }else {
                        reportarError("Se esperaba fin de sentencia")
                    }
                }else{
                    reportarError("Falta Tipo Dato ")
                }
            } else {
                reportarError("Se esperaba ';'")
            }
        }
        return null
    }

    /*
   <Asignación de Variable> ::=  Identificador “:” ( <Expresión> | <Invocación de Función> | Identificador) “|”
    */
    private fun esAsignacion(): Sentencia? {

        posBack=posicionActual

        if(tokenActual.categoria==Categoria.IDENTIFICADOR){
            val nombre= tokenActual.lexema
            obtenerSgteToken()

            if(tokenActual.categoria==Categoria.DOS_PUNTOS||tokenActual.categoria==Categoria.PARENTESIS_IZQ
                ||tokenActual.categoria==Categoria.INCREMENTO||tokenActual.categoria==Categoria.DECREMENTO){
                obtenerTokenPosicion(posBack)
                return null
            }


            if(tokenActual.categoria==Categoria.OPERADOR_ASIGNACION) {
                obtenerSgteToken()

                if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                    val nombreAsig = tokenActual.lexema
                    posBack=posicionActual
                    obtenerSgteToken()

                    if(tokenActual.categoria==Categoria.PARENTESIS_IZQ){
                        obtenerTokenPosicion(posBack)
                        val invocacionDeFuncion = esInvocacionDeFuncion()
                        if (invocacionDeFuncion != null) {
                            if (tokenActual.categoria == Categoria.TERMINAL) {
                                println("Asignación Exitosa")
                                return AsignacionVariable(nombre, invocacionDeFuncion)
                            } else {
                                reportarError("Se esperaba fin de sentencia '|'")
                            }
                        }
                        return null
                    }

                    if(tokenActual.categoria==Categoria.OPERADOR_ARITMETICO||tokenActual.categoria==Categoria.OPERADOR_RELACIONAL
                        ||tokenActual.categoria==Categoria.OPERADOR_LOGICO){
                        obtenerTokenPosicion(posBack)
                        val expresion = esExpresion()
                        if (expresion != null) {
                            if (tokenActual.categoria == Categoria.TERMINAL) {
                                println("Asignacion EXITOSA")
                                return AsignacionVariable(nombre, expresion)
                            } else {
                                reportarError("Se esperaba fin de sentencia '|'")
                            }
                        }
                        return null
                    }

                    if (tokenActual.categoria == Categoria.TERMINAL) {
                        println("Asignación Exitosa")
                        return AsignacionVariable(nombre, nombreAsig)
                    } else {
                        reportarError("Se esperaba fin de sentencia '|'")
                    }
                } else {

                    reportarError("No es una expresion ni una invocacion de funcion ni una variable")

                }

            }else{
                reportarError("Se esperaba ':'")
            }
        }
        return null
    }

    /*
    <Impresión> ::= PRINT “(“  <Expresión Cadena>  “)” “|”
    */
    private fun esImpresion(): Sentencia? {
        if(tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="PRINT"){
            obtenerSgteToken()
            if(tokenActual.categoria==Categoria.PARENTESIS_IZQ){
                obtenerSgteToken()
                val expresionCadena=esExpresionCadena()
                if(expresionCadena!=null) {
                    if (tokenActual.categoria == Categoria.PARENTESIS_DER) {
                        obtenerSgteToken()
                        if (tokenActual.categoria == Categoria.TERMINAL) {
                            println("Impresion EXITOSA")

                            return Impresion(expresionCadena)
                        } else {
                            reportarError("Se esperaba un fin de sentencia '|'")
                        }
                    } else {
                        reportarError("Se esperaba un ')'")
                    }
                }
            }else {
                reportarError("Se esperaba un '('")
            }
        }
        return null
    }

    /*
    <Retorno> ::= RETURN ( Identificador | <Expresión> ) “|”
     */
    private fun esRetorno(): Sentencia? {

        if(tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="RETURN"){
            obtenerSgteToken()
            if(tokenActual.categoria==Categoria.PARENTESIS_IZQ){
                obtenerSgteToken()
                if(tokenActual.categoria==Categoria.IDENTIFICADOR){
                    val identificadorRetorno=tokenActual.lexema
                    obtenerSgteToken()
                    if(tokenActual.categoria==Categoria.PARENTESIS_DER){
                        obtenerSgteToken()
                        if(tokenActual.categoria==Categoria.TERMINAL){
                            println("RETORNO EXITOSO")
                            return Retorno(identificadorRetorno)
                        }else{
                            reportarError("Se esperaba fin de sentencia '|'")
                        }
                    }else{
                        reportarError("Se esperaba ')'")
                    }
                }else{
                    val expresionRetornada= esExpresion()
                    if(expresionRetornada!=null){
                        println("RETORNO EXITOSO")
                        return Retorno(expresionRetornada)
                    }else{
                        reportarError("No es un identificador o una expresion válida")
                    }
                }
            }else{
                reportarError("Se esperaba')")
            }
        }
        return null
    }

    /*
    <Lectura> ::= READ “(“ Cadena de Caracteres “)” “|”
     */
    private fun esLectura(): Lectura? {

        if(tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="READ"){
            obtenerSgteToken()
            if(tokenActual.categoria==Categoria.PARENTESIS_IZQ){
                obtenerSgteToken()
                if(tokenActual.categoria==Categoria.CADENA_CARACTERES) {
                    val cadenaCaracteresLeida=tokenActual.lexema
                    obtenerSgteToken()
                    if (tokenActual.categoria == Categoria.PARENTESIS_DER) {
                        obtenerSgteToken()
                        if (tokenActual.categoria == Categoria.TERMINAL) {
                            println("LECTURA EXITOSA")

                            return Lectura(cadenaCaracteresLeida)
                        } else {
                            reportarError("Se esperaba un fin de sentencia '|'")
                        }
                    } else {
                        reportarError("Se esperaba un ')'")
                    }
                }
            }else {
                reportarError("Se esperaba un '('")
            }
        }
        return null
    }

    /*
    <Invocación de Función> ::= Identificador “(“ <Lista Argumentos> “)” “|”
     */
    private fun esInvocacionDeFuncion(): InvocacionFuncion? {
        if(tokenActual.categoria==Categoria.IDENTIFICADOR){
            val nombreFuncion= tokenActual.lexema
            obtenerSgteToken()
            if(tokenActual.categoria==Categoria.PARENTESIS_IZQ) {
                obtenerSgteToken()
                val listaArgumentos= esListaArgumentos()
                if(listaArgumentos!=null){
                    if(tokenActual.categoria==Categoria.PARENTESIS_DER){
                        obtenerSgteToken()
                        if(tokenActual.categoria==Categoria.TERMINAL){
                            println("INVOCACION FUNCION EXITOSA")
                            return InvocacionFuncion(nombreFuncion,listaArgumentos)
                        }else{
                            reportarError("Se esperaba fin de sentencia '|'")
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
    <Lista Argumentos> ::= <Argumento> [“_” <Lista Argumentos> ]
     */
    private fun esListaArgumentos(): ArrayList<Argumento>? {
        val listaArgumento = ArrayList<Argumento>()
        var argumento: Argumento? = esArgumento()

        while(argumento!=null){
            listaArgumento.add(argumento)
            obtenerSgteToken()

            if (tokenActual.categoria == Categoria.PARENTESIS_DER) {
                break
            }

            if (tokenActual.categoria == Categoria.SEPARADOR) {
                obtenerSgteToken()
            }else {
                reportarError("Falta Separador '_' entre argumentos")
                return null
            }

            argumento = esArgumento()
        }
        return listaArgumento
    }

    /*
    <Argumento> ::= Identificador | <Expresión>
     */
    private fun esArgumento(): Argumento? {

        posBack=posicionActual

        if(tokenActual.categoria==Categoria.IDENTIFICADOR){
            var nombre= tokenActual.lexema
            obtenerSgteToken()

            if(tokenActual.categoria==Categoria.SEPARADOR||tokenActual.categoria==Categoria.PARENTESIS_DER){
                obtenerTokenPosicion(posBack)
                println("Argumento EXITOSO")
                return Argumento(nombre)
            }else {
                obtenerTokenPosicion(posBack)
                val expresion = esExpresion()
                if (expresion != null) {
                    println("Argumento EXITOSO")
                    return Argumento(expresion)
                } else {
                    reportarError("No es un identificador ni una expresión válida")
                }
            }
        }
        return null
    }

    /*
    <Expresión> ::= <Expresión Lógica> | <Expresión Cadena> | < Expresión Aritmética> | <Expresión Relacional>
     */
    private fun esExpresion(): Expresion? {
        var e:Expresion?= esExpresionLogica()
        if(e!=null){
            return e
        }

        e= esExpresionCadena()
        if(e!=null){
            return e
        }

        e= esExpresionRelacional()
        if(e!=null){
            return e
        }

        e= esExpresionArtimetica()
        if(e!=null){
            return e
        }
        return null
    }

    /*
    <Expresión Lógica> ::= ( “¬”  <Expresión Lógica> | “(“<Expresión Lógica>”)” | TRUE | FALSE
    | Identificador ) [ (“Y” | “O”) <Expresión Lógica> ]
     */
    private fun esExpresionLogica(): ExpresionLogica? {

        if(tokenActual.categoria==Categoria.OPERADOR_LOGICO&&tokenActual.lexema=="¬"){
            obtenerSgteToken()
            val expresionLogicaIzquierda=esExpresionLogica()
            if(expresionLogicaIzquierda!=null){
                if(tokenActual.categoria==Categoria.OPERADOR_LOGICO&&(tokenActual.lexema=="Y"||tokenActual.lexema=="O")){
                    val operadorL=tokenActual.lexema
                    obtenerSgteToken()
                    val expresionLogicaDerecha=esExpresionLogica()
                    if(expresionLogicaDerecha!=null){
                        println("Expresion Lógica Exitosa")
                        return ExpresionLogica("¬",ExpresionLogica(expresionLogicaIzquierda,operadorL,expresionLogicaDerecha))
                    }else{
                        reportarError("Expresion Lógica invalida")
                    }
                }else{
                    println("Expresion Lógica Exitosa")
                    return ExpresionLogica("¬",expresionLogicaIzquierda)
                }
            }else{
                reportarError("Expresion Lógica invalida")
            }
        }else if(tokenActual.categoria==Categoria.PARENTESIS_IZQ){
            obtenerSgteToken()
            val expresionLogicaIzquierda=esExpresionLogica()
            if(expresionLogicaIzquierda!=null){
                obtenerSgteToken()
                if(tokenActual.categoria==Categoria.PARENTESIS_DER){
                    obtenerSgteToken()
                    if(tokenActual.categoria==Categoria.OPERADOR_LOGICO&&(tokenActual.lexema=="Y"||tokenActual.lexema=="O")){
                        val operadorL=tokenActual.lexema
                        obtenerSgteToken()
                        val expresionLogicaDerecha=esExpresionLogica()
                        if(expresionLogicaDerecha!=null){
                            obtenerSgteToken()
                            println("Expresion Lógica Exitosa")
                            return ExpresionLogica(expresionLogicaIzquierda,operadorL,expresionLogicaDerecha)
                        }else{
                            reportarError("Expresion Lógica invalida")
                        }
                    }else{
                        obtenerSgteToken()
                        println("Expresion Lógica Exitosa")
                        return ExpresionLogica(expresionLogicaIzquierda)
                    }
                }else{
                    reportarError("Se esperaba ')'")
                }
            }else{
                reportarError("Expresion Lógica invalida")
            }

        }else if((tokenActual.categoria==Categoria.PALABRA_RESERVADA&&(tokenActual.lexema=="TRUE"
                    ||tokenActual.lexema=="FALSE"))||tokenActual.categoria==Categoria.IDENTIFICADOR){
            val expresionLogicaIzquierda=ExpresionLogica(tokenActual.lexema)
            obtenerSgteToken()
            if(tokenActual.categoria==Categoria.OPERADOR_LOGICO&&(tokenActual.lexema=="Y"||tokenActual.lexema=="O")){
                var operadorL=tokenActual.lexema
                obtenerSgteToken()
                val expresionLogicaDerecha=esExpresionLogica()
                if(expresionLogicaDerecha!=null){
                    println("Expresion Lógica Exitosa")
                    return ExpresionLogica(expresionLogicaIzquierda,operadorL,expresionLogicaDerecha)
                }else{
                    reportarError("Expresion Lógica invalida")
                }
            }else{
                println("Expresion Lógica Exitosa")
                return expresionLogicaIzquierda
            }
        }
        return null
    }

    /*
    <Expresión Aritmética>::=  ( Entero | Decimal | Identificador | “(“ <Expresión Aritmética> “)” ) [ Operador Aritmético <Expresión Artimética> ]
     */
    private fun esExpresionArtimetica(): ExpresionArtimetica? {

        if (tokenActual.categoria == Categoria.PARENTESIS_IZQ) {
            obtenerSgteToken()
            val expresionAritmeticaIzquierda = esExpresionArtimetica()
            if (expresionAritmeticaIzquierda != null) {
                obtenerSgteToken()
                if(tokenActual.categoria==Categoria.PARENTESIS_DER){
                    obtenerSgteToken()
                    if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO) {
                        val operadorAritmetico=tokenActual.lexema
                        obtenerSgteToken()
                        val expresionArtimeticaDerecha = esExpresionArtimetica()
                        if (expresionArtimeticaDerecha != null) {
                            obtenerSgteToken()
                            println("Expresión Aritmética Exitosa")
                            return ExpresionArtimetica(expresionAritmeticaIzquierda,operadorAritmetico,expresionArtimeticaDerecha)
                        }else{
                            reportarError("Expresión Aritmética inválida")
                        }
                    } else{
                        obtenerSgteToken()
                        println("Expresión Aritmética Exitosa")
                        return ExpresionArtimetica(expresionAritmeticaIzquierda)
                    }
                }else{
                    reportarError("Se esperaba ')'")
                }
            } else {
                reportarError("Expresion Artimética invalida")
            }

        } else if (tokenActual.categoria==Categoria.DECIMAL||tokenActual.categoria==Categoria.ENTERO
            ||tokenActual.categoria==Categoria.IDENTIFICADOR) {

            val expresionAritmeticaIzquierda = ExpresionArtimetica(tokenActual.lexema)
                obtenerSgteToken()
                if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO) {
                    val operadorAritmetico=tokenActual.lexema
                    obtenerSgteToken()
                    val expresionArtimeticaDerecha = esExpresionArtimetica()
                    if (expresionArtimeticaDerecha != null) {
                        obtenerSgteToken()
                        println("Expresión Aritmética Exitosa")
                        return ExpresionArtimetica(expresionAritmeticaIzquierda,operadorAritmetico,expresionArtimeticaDerecha)

                    }else{
                        reportarError("Expresión Aritmética inválida")
                    }
                } else{
                    obtenerSgteToken()
                    println("Expresión Aritmética Exitosa")
                    return expresionAritmeticaIzquierda
                }
        }
        return null
    }

    /*
   <Expresión Relacional> ::=  ( Identificador | “(“ <Expresión Relacional> “)” )
   [ Operador Relacional   <Expresión> ]
     */
    private fun esExpresionRelacional(): ExpresionRelacional? {

        if(tokenActual.categoria==Categoria.IDENTIFICADOR){
            val expresionRelacionalIzq= ExpresionRelacional(tokenActual.lexema)
            obtenerSgteToken()
            if(tokenActual.categoria==Categoria.OPERADOR_RELACIONAL){
                val operadorRelacional= tokenActual.lexema
                obtenerSgteToken()
                val expresionDer= esExpresion()
                if(expresionDer!=null){
                    println("Expresion Relacional Exitosa")
                    return ExpresionRelacional(expresionRelacionalIzq,operadorRelacional,expresionDer)
                }else{
                    reportarError("Expresion Relacional inválida")
                }
            }else{
                println("Expresion Relacional Exitosa")
                return expresionRelacionalIzq
            }
        }else if (tokenActual.categoria == Categoria.PARENTESIS_IZQ) {
            obtenerSgteToken()
            val expresionRelacionalIzq = esExpresionRelacional()
            if (expresionRelacionalIzq != null) {
                obtenerSgteToken()
                if (tokenActual.categoria == Categoria.PARENTESIS_DER) {
                    obtenerSgteToken()
                    if(tokenActual.categoria==Categoria.OPERADOR_RELACIONAL){
                        val operadorRelacional= tokenActual.lexema
                        obtenerSgteToken()
                        val expresionDer= esExpresion()
                        if(expresionDer!=null){
                            println("Expresion Relacional Exitosa")
                            return ExpresionRelacional(expresionRelacionalIzq,operadorRelacional,expresionDer)
                        }else{
                            reportarError("Expresion Relacional inválida")
                        }
                    }else{
                        println("Expresion Relacional Exitosa")
                        return expresionRelacionalIzq
                    }
                }
            }
        }

        return null
    }


    /*
     <Expresión Cadena> ::=  (Cadena de Caracteres | Identificador ) [ “+”<Expresión Cadena> ]
     */
    private fun esExpresionCadena(): ExpresionCadena? {

        if(tokenActual.categoria==Categoria.CADENA_CARACTERES||tokenActual.categoria==Categoria.IDENTIFICADOR){
            val cadenaIzq= ExpresionCadena(tokenActual.lexema)
            obtenerSgteToken()
            if(tokenActual.categoria==Categoria.OPERADOR_ARITMETICO&&tokenActual.lexema=="+"){
                obtenerSgteToken()
                val cadenaDer= esExpresionCadena()
                if(cadenaDer!=null){
                    println("Expresion Caden Exitosa")
                    return ExpresionCadena(cadenaIzq,cadenaDer)
                }else{
                    reportarError("Expresión Cadena no Válida")
                }
            }else{
                println("Expresión Cadena Exitosa")
                return cadenaIzq
            }
        }
        return null
    }

    private fun esAsignacionArray(): Sentencia? {

        return null
    }

    private fun esDeclaracionArray(): Sentencia? {

        return null
    }


    private fun esAsignacionArrayB(): Sentencia? {
        return null

    }
/*
    /*
    <Declaración arrayB> ::= Identificador “;” Tipo de arrayB “|”
     */
    private fun esDeclaracionArrayB(): Sentencia? {
        var tipoVariable = ""
        var nombre = ""
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "CONST") {
            tipoVariable = "INMUTABLE"
            obtenerSgteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                nombre = tokenActual.lexema
                obtenerSgteToken()
                if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                    obtenerSgteToken()
                    val tipoDato = esTipoArrayB()
                    if (tipoDato != null) {
                        if (tokenActual.categoria == Categoria.TERMINAL) {
                            println("Declaracion tipoArrayB exitosa")
                            return DeclaracionAsignacionArrayB(nombre, tipoVariable, tipoDato)
                        } else {
                            reportarError("Se esperaba fin de sentencia '|'")
                        }
                    } else {
                        reportarError("Falta tipo Dato o Array de variable")
                    }
                } else {
                    reportarError("Se esperaba ';'")
                }
            }
        } else if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
            tipoVariable = "MUTABLE"
            nombre = tokenActual.lexema
            obtenerSgteToken()
            if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                obtenerSgteToken()
                val tipoDato = esTipoArrayB()
                if (tipoDato != null) {
                    if (tokenActual.categoria == Categoria.TERMINAL) {
                        println("Declaracion tipoArrayB exitosa")
                        return DeclaracionAsignacionArrayB(nombre, tipoVariable, tipoDato)
                    } else {
                        reportarError("Se esperaba fin de sentencia '|'")
                    }
                } else {
                    reportarError("Falta tipo Dato o Array de variable")
                }
            } else {
                reportarError("Se esperaba ';'")
            }
        }
        return null
    }


 */

}
