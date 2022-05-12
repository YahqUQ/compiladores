package co.edu.uniquindio.compiladores.modelo.lexico

/*
 * @author: Jaime Nieto, Yiran Hernandez, Mauricio Duque
 */

class AnalizadorLexico(var codigoFuente: String) {

    var posicionActual = 0
    var caracterActual = codigoFuente[0]
    var finCodigo = 0.toChar()
    var listaTokens = ArrayList<Token>()
    var listaErroresLexicos = ArrayList<Error>()
    var filaActual = 0
    var columnaActual = 0
    var listaPalabrasReservadas = listOf(
        "FUNCTION",
        "NUMBER_Zs",
        "NUMBER_Fs",
        "CHARs",
        "STRINGs",
        "BOOLEANs",
        "NUMBER_Z",
        "NUMBER_F",
        "CHAR",
        "STRING",
        "BOOLEAN",
        "NONE",
        "CONS",
        "true",
        "false",
        "GLOBAL",
        "IF",
        "WHILE",
        "PRINT",
        "RETURN"

    )

    /*
        Almacena un Token en una lista
        @lexema
        @categoria Categoria a la que pertenece el lexema
        @fila Fila en donde se encontro el lexema
        @columna Columna en donde se encontro el lexema
     */
    fun almacenarToken(lexema: String, categoria: Categoria, fila: Int, columna: Int) = listaTokens.add(
        Token(lexema, categoria, fila, columna)
    )

    /**
     * crea un error
     */
    fun reportarError(mensaje: String) {
        val error = Error(mensaje, filaActual, columnaActual-1)
        listaErroresLexicos.add(error)
    }

    /*
        Funcion que recorre el codigo fuente y realiza el analisis Lexico
     */
    fun analizar() {


        while (caracterActual != finCodigo) {
            if (caracterActual == ' ' || caracterActual == '\n' || caracterActual == '\t') {
                obtenerSiguienteCaracter()
                continue
            }
            if (esPalabraReservada()) continue
            if (esEntero()) continue
            if (esDecimal()) continue
            if (esCadenaCaracteres()) continue
            if (esIdentificador()) continue
            if (esOperadorAritmetico()) continue
            if (esOperadorRelacional()) continue
            if (esOperadorLogico()) continue
            if (esOperadorAsignacion()) continue
            if (esParentesis()) continue
            if (esLlave()) continue
            if (esTerminal()) continue
            if (esSeparador()) continue
            if (esComentarioLinea()) continue
            if (esIncremento()) continue
            if (esDecremento()) continue
            if (punto()) continue
            if (dosPuntos()) continue
            if (comentarioBloque()) continue
            if (esCaracter()) continue
            if (esCorchete()) continue


            almacenarToken("" + caracterActual, Categoria.DESCONOCIDO, filaActual, columnaActual)
            obtenerSiguienteCaracter()
        }
    }


    /*
        Funcion que obtiene el caracter siguiente en el codigo Fuente
     */
    fun obtenerSiguienteCaracter() {
        if (posicionActual == codigoFuente.length - 1) {
            caracterActual = finCodigo
        } else {
            if (caracterActual == '\n') {
                filaActual++
                columnaActual = 0
            } else {
                columnaActual++
            }
            posicionActual++
            caracterActual = codigoFuente[posicionActual]
        }
    }

    /*
        Funcion encargada de hacer Backtracking cuando no se logra identificar el segmento de codigo
     */
    fun pasoAtras(posicionInicial: Int, filaInicial: Int, columnaInicial: Int) {
        posicionActual = posicionInicial
        filaActual = filaInicial
        columnaActual = columnaInicial
        caracterActual = codigoFuente[posicionActual]
    }

    /*
        Funcion encargada de verificar si el segmento de codigo es un numero Entero
     */
    fun esEntero(): Boolean {
        if (caracterActual == 'Z') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            val posicionInicial = posicionActual
            lexema += caracterActual
            obtenerSiguienteCaracter()
            while (caracterActual.isDigit()) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }

            almacenarToken(lexema, Categoria.ENTERO, filaInicial, columnaInicial)

            return true
        }


        return false
    }

    fun esDecimal(): Boolean {

        if (caracterActual == 'F') {
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            val posicionInicial = posicionActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == '.') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual.isDigit()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    while (caracterActual.isDigit()) {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                    }
                    almacenarToken(lexema, Categoria.DECIMAL, filaInicial, columnaInicial)
                    return true
                } else {
                    reportarError("Decimal no válido(Faltan números antes o despúes de .)")
                    return true
                }
            }

            if (caracterActual.isDigit()) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                while (caracterActual.isDigit()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }
                if (caracterActual == '.') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    while (caracterActual.isDigit()) {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                    }
                    almacenarToken(lexema, Categoria.DECIMAL, filaInicial, columnaInicial)
                    return true
                } else {
                    reportarError("Decimal no válido(Debe llevar .)")
                    return true
                }

            }

            if (caracterActual is Char && (!caracterActual.isDigit() || caracterActual != '.')) {

                reportarError("Decimal no válido(Faltan números o .)")
                return true
            }
        }

        return false
    }

    /*
       Funcion encargada de verificar si el segmento de codigo es un identificador
    */
    fun esIdentificador(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        val posicionInicial = posicionActual

        if (caracterActual == '#') {
            lexema += caracterActual
            var cantidadCaracteres = 0
            obtenerSiguienteCaracter()
            if (caracterActual.isLetter() || caracterActual.isDigit()) {
                while (caracterActual.isLetter() || caracterActual.isDigit()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    cantidadCaracteres++
                }
                if (cantidadCaracteres >= 10) {
                    reportarError("Identificador no válido (Máx 10 números o letras)")
                    return true
                } else {
                    almacenarToken(lexema, Categoria.IDENTIFICADOR, filaInicial, columnaInicial)
                    return true
                }
            } else {
                reportarError("Identificador no válido (Faltan Letras o Números)")
                return true
            }


        }
        return false
    }

    /*
       Funcion encargada de verificar si el segmento de codigo es un Operador Aritmetico
    */
    fun esOperadorAritmetico(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        val posicionInicial = posicionActual

        if (caracterActual == '+' || caracterActual == '-' || caracterActual == '*' || caracterActual == '/' || caracterActual == '%') {
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == ':') {
                pasoAtras(posicionInicial, filaInicial, columnaInicial)
                return false
            } else {
                almacenarToken(lexema, Categoria.OPERADOR_ARITMETICO, filaInicial, columnaInicial)
                return true
            }

        }

        return false
    }

    /*
       Funcion encargada de verificar si el segmento de codigo es un OperadorRelacional
    */
    fun esOperadorRelacional(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        val posicionInicial = posicionActual

        if (caracterActual == ':' || caracterActual == '¬') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == ':') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial)
                return true
            } else {
                pasoAtras(posicionInicial, filaInicial, columnaInicial)
                return false
            }
        }

        if (caracterActual == '>') {
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == '>') {
                lexema += caracterActual
                obtenerSiguienteCaracter()

                if (caracterActual == ':') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }

                almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial)
                return true
            } else {
                reportarError("Operadores relacionales son >>, >>:, <<, <<:, ¬:, ::")
                return true
            }
        }

        if (caracterActual == '<') {
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == '<') {
                lexema += caracterActual
                obtenerSiguienteCaracter()

                if (caracterActual == ':') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }

                almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial)
                return true
            } else {
                reportarError("Operadores relacionales son >>, >>:, <<, <<:, ¬:, ::")
                return true
            }
        }

        return false
    }

    /*
       Funcion encargada de verificar si el segmento de codigo es un OperadorLogico
    */
    fun esOperadorLogico(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        val posicionInicial = posicionActual

        if (caracterActual == 'Y' || caracterActual == 'O') {
            lexema += caracterActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.OPERADOR_LOGICO, filaInicial, columnaInicial)
            return true

        }

        if (caracterActual == '¬') {
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == ':') {
                pasoAtras(posicionInicial, filaInicial, columnaInicial)
                return false
            } else {
                almacenarToken(lexema, Categoria.OPERADOR_LOGICO, filaInicial, columnaInicial)
                return true
            }
        }

        return false
    }

    /*
       Funcion encargada de verificar si el segmento de codigo es un Operador de Asignacion
    */
    fun esOperadorAsignacion(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        val posicionInicial = posicionActual

        if (caracterActual == '+' || caracterActual == '-' || caracterActual == '%' || caracterActual == '*' || caracterActual == '/') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == ':') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(lexema, Categoria.OPERADOR_ASIGNACION, filaInicial, columnaInicial)
                return true
            } else {
                pasoAtras(posicionInicial, filaInicial, columnaInicial)
                return false
            }
        }

        if (caracterActual == ':') {
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == ':') {

                pasoAtras(posicionInicial, filaInicial, columnaInicial)
                return false

            } else {

                almacenarToken(lexema, Categoria.OPERADOR_ASIGNACION, filaInicial, columnaInicial)
                return true
            }
        }

        return false
    }

    /*
      Funcion encargada de verificar si el segmento de codigo es un paréntesis
   */
    fun esParentesis(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual

        if (caracterActual == '(') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.PARENTESIS_IZQ, filaInicial, columnaInicial)
            return true
        }
        if (caracterActual == ')') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.PARENTESIS_DER, filaInicial, columnaInicial)
            return true
        }


        return false
    }

    /*
        Funcion encargada de verificar si el segmento de codigo es una llave
     */
    fun esLlave(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        posicionActual

        if (caracterActual == '{') {
            lexema += caracterActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.LLAVE_IZQ, filaInicial, columnaInicial)
            return true

        }
        if (caracterActual == '}') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.LLAVE_DER, filaInicial, columnaInicial)
            return true
        }
        return false
    }

    /*
       Funcion encargada de verificar si el segmento de codigo es un fin de sentencia
    */
    fun esTerminal(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual

        if (caracterActual == '|') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.TERMINAL, filaInicial, columnaInicial)
            return true
        }

        return false
    }

    /*
       Funcion encargada de verificar si el segmento de codigo es un Separador
    */
    fun esSeparador(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual

        if (caracterActual == '_') {
            lexema += caracterActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.SEPARADOR, filaInicial, columnaInicial)
            return true
        }

        return false
    }

    /*
           Funcion encargada de verificar si el segmento de codigo es una cadena de caracteres
        */
    fun esCadenaCaracteres(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        val posicionInicial = posicionActual

        if (caracterActual == '°') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '°') {

                lexema += caracterActual
                obtenerSiguienteCaracter()

                while (caracterActual != '°' ) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()

                    if(caracterActual==finCodigo){
                        reportarError("Cadena de Caracteres no válida(Debe terminar en °°)")
                        return true
                    }
                }

                if (caracterActual == '°') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()

                    if (caracterActual == '°') {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        almacenarToken(lexema, Categoria.CADENA_CARACTERES, filaInicial, columnaInicial)
                        return true
                    } else {
                        reportarError("Cadena de Caracteres no válida(Debe terminar en °°)")
                        return true
                    }
                }

            } else {
                pasoAtras(posicionInicial, filaInicial, columnaInicial)
                return false
            }

        }

        return false
    }

    /*
       Funcion encargada de verificar si el segmento de codigo es un Comentario de Linea
    */
    fun esComentarioLinea(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual

        if (caracterActual == '@') {
            lexema += caracterActual
            obtenerSiguienteCaracter()

            while (caracterActual != '\n' && caracterActual != finCodigo) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }

            lexema += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.COMENTARIOLINEA, filaInicial, columnaInicial)
            return true
        }

        return false
    }


    /*
     Funcion encargada de verificar si el segmento de codigo es un Comentario de bloque
  */
    fun comentarioBloque(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual

        if (caracterActual == '"') {

            lexema += caracterActual
            obtenerSiguienteCaracter()

            while (caracterActual != '"') {

                if(caracterActual== finCodigo){
                    reportarError("Comentario bloque no válido(Cierre el comentario)")
                    return true
                }
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }

            lexema += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.COMENTARIO_BLOQUE, filaInicial, columnaInicial)
            return true
        }

        return false

    }

    /*
       Funcion encargada de verificar si el segmento de codigo es una Palabra Reservada
    */
    fun esPalabraReservada(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        val posicionInicial = posicionActual

        for (palabra in listaPalabrasReservadas) {
            if (caracterActual == palabra[0]) {

                var centinela = true

                for (caracter in palabra) {
                    if (caracter == caracterActual) {
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                    } else {
                        centinela = false
                        pasoAtras(posicionInicial, filaInicial, columnaInicial)
                        break
                    }
                }
                if (centinela) {
                    almacenarToken(lexema, Categoria.PALABRA_RESERVADA, filaInicial, columnaInicial)
                    return true
                }

            }

        }


        return false
    }

    /*
       Funcion encargada de verificar si el segmento de codigo es un Operador de Incremento
    */
    fun esIncremento(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        val posicionInicial = posicionActual

        if (caracterActual == 'i') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'n') {
                lexema += caracterActual
                obtenerSiguienteCaracter()

                if (caracterActual == 'c') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()

                    almacenarToken(lexema, Categoria.INCREMENTO, filaInicial, columnaInicial)
                    return true
                }else{
                    reportarError("Operador de incremento no válido (INC)")
                    return true
                }
            } else {
                reportarError("Operador de incremento no válido (INC)")
                return true
            }
        }
        return false
    }

    /*
           Funcion encargada de verificar si el segmento de codigo es un Operador de Decremento
        */
    fun esDecremento(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        val posicionInicial = posicionActual

        if (caracterActual == 'd') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'e') {
                lexema += caracterActual
                obtenerSiguienteCaracter()

                if (caracterActual == 'c') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()

                    almacenarToken(lexema, Categoria.DECREMENTO, filaInicial, columnaInicial)
                    return true
                }else{
                    reportarError("Operador de decremento no válido (DEC)")
                    return true
                }
            }else{
                reportarError("Operador de decremento no válido (DEC)")
                return true
            }
        }
        return false
    }

    /*
     Funcion encargada de verificar si el segmento de codigo es un punto
  */
    fun punto(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual

        if (caracterActual == '.') {
            lexema += caracterActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.PUNTO, filaInicial, columnaInicial)
            return true

        }
        return false
    }

    /*
     Funcion encargada de verificar si el segmento de codigo son DosPuntos
  */
    fun dosPuntos(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual

        if (caracterActual == ';') {
            lexema += caracterActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.DOS_PUNTOS, filaInicial, columnaInicial)
            return true

        }
        return false
    }

    fun esCaracter(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        val posicionInicial = posicionActual

        if (caracterActual == '°') {
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == '°') {
                pasoAtras(posicionInicial, filaInicial, columnaInicial)
                return false
            } else {
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }

            if (caracterActual == '°') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(lexema, Categoria.CARACTER, filaInicial, columnaInicial)
                return true
            } else {
                reportarError("Caracter no válido (Debe terminar en °)")
                return true
            }
        }
        return false

    }

    fun esCorchete(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val columnaInicial = columnaActual
        posicionActual

        if (caracterActual == '[') {
            lexema += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.CORCHETE_IZQ, filaInicial, columnaInicial)
            return true
        }
        if (caracterActual == ']') {

            lexema += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.CORCHETE_DER, filaInicial, columnaInicial)
            return true


        }

        return false

    }


}