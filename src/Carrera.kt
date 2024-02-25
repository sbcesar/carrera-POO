import kotlin.random.Random

/**
 * Clase que gestiona el seguimiento de movimientos, posiciones de los vehículos y el historial de acciones en una carrera.
 *
 * @property nombreCarrera El nombre de la carrera para identificación.
 * @property distanciaTotal La distancia total que los vehículos deben recorrer para completar la carrera.
 * @property participantes Una lista que contiene todos los vehículos participantes en la carrera.
 * @property estadoCarrera Un indicador de si la carrera está en curso o ha finalizado.
 * @property historialAcciones Un mapa para registrar el historial de acciones de cada vehículo. La clave es el nombre del vehículo y el valor es una lista de strings describiendo sus acciones.
 * @property posiciones Un mapa para mantener un registro de la posición y los kilómetros recorridos por cada vehículo. Cada elemento es un par donde el primer valor es el nombre del vehículo y el segundo su kilometraje acumulado.
 */
class Carrera(val nombreCarrera: String, val distanciaTotal: Float, val participantes: List<Vehiculo>) {

    var estadoCarrera: Boolean = false
    val historialAcciones: MutableMap<String, MutableList<String>> = mutableMapOf()
    var posiciones: MutableMap<String, Int> = mutableMapOf()

    var vecesRepostado = 0

    init {
        require(distanciaTotal >= 1000f) { "La distancia total de la carrera debe ser igual o mayor que 1000." }
    }

    /**
     * Inicia la carrera, estableciendo estadoCarrera a true y comenzando el ciclo de iteraciones donde los vehículos avanzan y realizan acciones.
     */
    fun iniciarCarrera() {
        estadoCarrera = true
        while (estadoCarrera) {
            val vehiculoRandom = participantes.random()
            avanzarVehiculo(vehiculoRandom)
            realizaPosiciones()
            determinarGanador()
        }
    }

    /**
     * Identificado el vehículo, le hace avanzar una distancia aleatoria entre 10 y 200 km. Si el vehículo necesita repostar, se llama al método repostarVehiculo() antes de que pueda continuar. Este método llama a realizar filigranas.
     */
    fun avanzarVehiculo(vehiculo: Vehiculo) {
        var distanciaAleatoria = Random.nextInt(10,201)
        val realizarFiligramaAleatorio = (1..3).random()
        var numeroFilogramas = distanciaAleatoria / 20

        registrarAccion(vehiculo.nombre, "Iniciar viaje: A recorrer $distanciaAleatoria kms (${vehiculo.kilometrosActuales} kms y ${vehiculo.combustibleActual} L actuales)")
        while (distanciaAleatoria > 0) {

            vehiculo.combustibleActual = 0f
            repostarVehiculo(vehiculo,vehiculo.capacidadCombustible)
            vecesRepostado++

            if (numeroFilogramas >= 1) {
                when (realizarFiligramaAleatorio) {
                    1 -> realizarFiligrana(vehiculo)
                    2 -> {
                        realizarFiligrana(vehiculo)
                        realizarFiligrana(vehiculo)
                    }
                }
                numeroFilogramas -= 1
            }

            distanciaAleatoria -= 20
            vehiculo.kilometrosActuales += 20
            registrarAccion(vehiculo.nombre, "Avance tramo: Recorrido 20 kms.")
        }

        val kmLitros = if (vehiculo is Motocicleta) 20 else if (vehiculo is Automovil) 15 else 10

        vehiculo.combustibleActual = vehiculo.capacidadCombustible - distanciaAleatoria / kmLitros
        vehiculo.kilometrosActuales += distanciaAleatoria

        registrarAccion(vehiculo.nombre, "Recorrido $distanciaAleatoria kms.")
        registrarAccion(vehiculo.nombre, "Finaliza viaje: Total recorrido $distanciaAleatoria kms (${vehiculo.kilometrosActuales} kms y ${vehiculo.combustibleActual} L actuales)")
    }

    /**
     * Reposta el vehículo seleccionado, incrementando su combustibleActual y registrando la acción en historialAcciones.
     */
    fun repostarVehiculo(vehiculo: Vehiculo, cantidad: Float) {
        vehiculo.repostar(cantidad)
        registrarAccion(vehiculo.nombre,"Repostaje tramo: ${vehiculo.capacidadCombustible}")
        realizarFiligrana(vehiculo)
    }

    /**
     * Determina aleatoriamente si un vehículo realiza una filigrana (derrape o caballito) y registra la acción.
     */
    fun realizarFiligrana(vehiculo: Vehiculo) {
        val accionRandom = (1..2).random()

        when (accionRandom) {
            1 -> if (vehiculo is Motocicleta) {
                vehiculo.realizarCaballito()
                registrarAccion(vehiculo.nombre, "Caballito: Combustible restante ${(vehiculo.capacidadCombustible - vehiculo.combustibleActual).redondear(2)} L.")
            }
            2 -> if (vehiculo is Automovil) {
                vehiculo.realizarDerrape()
                registrarAccion(vehiculo.nombre, "Derrape: Combustible restante ${(vehiculo.capacidadCombustible - vehiculo.combustibleActual).redondear(2)} L.")
            }
        }
    }

    /**
     * Actualiza posiciones con los kilómetros recorridos por cada vehículo después de cada iteración, manteniendo un seguimiento de la competencia.
     */
    fun realizaPosiciones() {
        posiciones = mutableMapOf()
        //Ordena los participantes segun la distancia recorrida de mayor a menor
        val participantesOrdenados = participantes.sortedByDescending { vehiculo -> vehiculo.kilometrosActuales }

        //Actualiza en base al orden
        participantesOrdenados.forEachIndexed { index, vehiculo -> posiciones[vehiculo.nombre] = vehiculo.kilometrosActuales.toInt() }

        /**
         * val nombresOrdenados = mutableListOf<String>()
         *
         * for (vehiculo in participantes) {
         *      val distanciaRecorrida = posiciones[vehiculo.nombre]
         *      var index = 0
         *      while (index < nombresOrdenados.size && posiciones[nombresOrdenados[index]] > distanciaRecorrida) {
         *          index++
         *      }
         *      nombresOrdenados.add(index, vehiculo.nombre)
         * }
         *
         * for ((index, nombre) in nombresOrdenados.withIndex()) {
         *      posiciones[nombre] = index + 1
         * }
         */
    }

    /**
     * Revisa posiciones para identificar al vehículo (o vehículos) que haya alcanzado o superado la distanciaTotal, estableciendo el estado de la carrera a finalizado y determinando el ganador.
     */
    fun determinarGanador(): String {
        val ganador =  posiciones.filter { it.value > distanciaTotal }
            posiciones.forEach { (nombreJugador, kmRecorridos) -> if (kmRecorridos >= distanciaTotal) estadoCarrera = false }.toString()
        return ganador.keys.toString().replace("[","").replace("]","")
    }

    /**
     * Añade una acción al historialAcciones del vehículo especificado.
     */
    fun registrarAccion(nombreVehiculo: String, accion: String) {
        if (historialAcciones.contains(nombreVehiculo)) {
            historialAcciones[nombreVehiculo]?.add(accion)
        } else {
            historialAcciones[nombreVehiculo] = mutableListOf(accion)
        }
    }

    /**
     * Devuelve una clasificación final de los vehículos, cada elemento tendrá el nombre del vehiculo, posición ocupada, la distancia total recorrida, el número de paradas para repostar y el historial de acciones. La collección estará ordenada por la posición ocupada.
     */
    fun obtenerResultado(): MutableList<ResultadoCarrera> {
        var posicion = 1
        val resultadoCarrera = mutableListOf<ResultadoCarrera>()

        posiciones.forEach { (key, value) ->
            val vehiculo = participantes.find { key == it.nombre }

            resultadoCarrera.add(ResultadoCarrera(
                vehiculo!!,posicion,value.toFloat(),vecesRepostado,
                historialAcciones[key]?.toList() ?: emptyList()
            ))

            posicion ++
        }
        return resultadoCarrera
    }

    /**
     * Representa el resultado final de un vehículo en la carrera, incluyendo su posición final, el kilometraje total recorrido,
     * el número de paradas para repostar, y un historial detallado de todas las acciones realizadas durante la carrera.
     *
     * @property vehiculo El Vehiculo al que pertenece este resultado.
     * @property posicion La posición final del vehículo en la carrera, donde una posición menor indica un mejor rendimiento.
     * @property kilometraje El total de kilómetros recorridos por el vehículo durante la carrera.
     * @property paradasRepostaje El número de veces que el vehículo tuvo que repostar combustible durante la carrera.
     * @property historialAcciones Una lista de cadenas que describen las acciones realizadas por el vehículo a lo largo de la carrera, proporcionando un registro detallado de su rendimiento y estrategias.
     */
    data class ResultadoCarrera(
        val vehiculo: Vehiculo,
        val posicion: Int,
        val kilometraje: Float,
        val paradasRepostaje: Int,
        val historialAcciones: List<String>
    )
}