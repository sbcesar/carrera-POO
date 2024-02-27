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
class Carrera(val nombreCarrera: String, private val distanciaTotal: Float, private val participantes: List<Vehiculo>) {

    var estadoCarrera: Boolean = false
    val historialAcciones: MutableMap<String, MutableList<String>> = mutableMapOf()
    var posiciones: MutableMap<String, Int> = mutableMapOf()

    private var vecesRepostado = 0

    init {
        require(distanciaTotal >= 1000f) { "La distancia total de la carrera debe ser igual o mayor que 1000." }
    }

    companion object {
        const val KM_TRAMO = 20f
    }

    fun iniciarCarrera() {
        estadoCarrera = true
        while (estadoCarrera) {
            val vehiculoRandom = participantes.random()
            avanzarVehiculo(vehiculoRandom)
            realizaPosiciones()
            determinarGanador()
        }
    }

    private fun cajaSorpresa(vehiculo: Vehiculo) {
        val cajaRandom = (1..10).random()

        when (cajaRandom) {
            1 -> {
                registrarAccion(vehiculo.nombre,"PREMIO!! - Obtienes 10 km mas!!")
                sumar10(vehiculo)
            }
            2 -> {
                registrarAccion(vehiculo.nombre,"PREMIO!! - Avanzas 100 km!!")
                teletransporte(vehiculo)
            }
            3 -> {
                registrarAccion(vehiculo.nombre,"PREMIO!! - Retrasas 100 km al resto de participantes!!")
                retrasarTodos(vehiculo)
            }
            4 -> {
                registrarAccion(vehiculo.nombre,"PREMIO O PENALIZACION?? - Se retrasa un vehiculo random al inicio de la carrera!!")
                vehiculoAlInicio()
            }
            5 -> {
                registrarAccion(vehiculo.nombre,"PENALIZACION!! - Volvemos al inicio de la carrera!! Oh no!!!")
                casillaDeSalida(vehiculo)
            }
            6 -> {
                registrarAccion(vehiculo.nombre,"PENALIZACION!! - Oh no!! Volvemos atras!!")
                restar5(vehiculo)
            }
            7 -> {
                registrarAccion(vehiculo.nombre,"Caja vacia (No realiza ninguna accion)")
            }
            8 -> {
                registrarAccion(vehiculo.nombre,"Caja vacia (No realiza ninguna accion)")
            }
            9 -> {
                registrarAccion(vehiculo.nombre,"Caja vacia (No realiza ninguna accion)")
            }
            10 -> {
                registrarAccion(vehiculo.nombre,"Caja vacia (No realiza ninguna accion)")
            }
        }
    }

    private fun sumar10(vehiculo: Vehiculo) {
        vehiculo.kilometrosActuales += vehiculo.combustibleActual * 10
    }

    private fun teletransporte(vehiculo: Vehiculo) {
        val distanciaRestante = distanciaTotal - vehiculo.kilometrosActuales
        vehiculo.kilometrosActuales += minOf(distanciaRestante,100f)
    }

    private fun retrasarTodos(vehiculo: Vehiculo) {
        participantes.filter { it != vehiculo }
            .forEach { it.kilometrosActuales -= minOf(it.kilometrosActuales, 100f) }
    }

    private fun vehiculoAlInicio() {
        val vehiculoPerjudicado = participantes.random()
        vehiculoPerjudicado.kilometrosActuales = 0f
    }

    private fun casillaDeSalida(vehiculo: Vehiculo) {
        vehiculo.kilometrosActuales = 0f
    }

    private fun restar5(vehiculo: Vehiculo) {
        vehiculo.combustibleActual -= vehiculo.combustibleActual * 5
    }

    private fun avanzarTramo(vehiculo: Vehiculo, distanciaTramo: Float) {
        var distanciaNoRecorrida = vehiculo.realizarViaje(distanciaTramo)
        registrarAccion(vehiculo.nombre, "Avance tramo: Recorrido ${(distanciaTramo - distanciaNoRecorrida).redondear(2)} kms.")

        while (distanciaNoRecorrida > 0) {
            repostarVehiculo(vehiculo, vehiculo.capacidadCombustible)
            vecesRepostado++

            val distanciaViaje = distanciaNoRecorrida
            distanciaNoRecorrida = vehiculo.realizarViaje(distanciaViaje)
            registrarAccion(vehiculo.nombre, "Avance tramo: Recorrido ${(distanciaViaje - distanciaNoRecorrida).redondear(2)} kms.")
        }

        val totalFiligranas = (1..2).random()
        repeat(totalFiligranas) {
            realizarFiligrana(vehiculo)
        }
    }

    private fun obtenerDistanciaAleatoria(vehiculo: Vehiculo): Float {
        val distancia = Random.nextInt(10,201)

        return if (distancia + vehiculo.kilometrosActuales > distanciaTotal) {
            (distanciaTotal - vehiculo.kilometrosActuales).redondear(2)
        }
        else {
            distancia.toFloat()
        }
    }

    private fun avanzarVehiculo(vehiculo: Vehiculo) {
        val distanciaAleatoria = obtenerDistanciaAleatoria(vehiculo)

        registrarAccion(vehiculo.nombre, "Iniciar viaje: A recorrer $distanciaAleatoria kms (${vehiculo.kilometrosActuales} kms y ${vehiculo.combustibleActual} L actuales)")

        var distanciaARecorrer = distanciaAleatoria
        while (distanciaARecorrer > 0) {

            val distanciaTramo = minOf(KM_TRAMO, distanciaARecorrer).redondear(2)

            avanzarTramo(vehiculo, distanciaTramo)

            cajaSorpresa(vehiculo)

            distanciaARecorrer = (distanciaARecorrer - distanciaTramo).redondear(2)
        }

        registrarAccion(vehiculo.nombre, "Finaliza viaje: Total recorrido $distanciaAleatoria kms (${vehiculo.kilometrosActuales} kms y ${vehiculo.combustibleActual} L actuales)")
    }

    private fun repostarVehiculo(vehiculo: Vehiculo, cantidad: Float) {
        vehiculo.repostar(cantidad)
        registrarAccion(vehiculo.nombre,"Repostaje tramo: ${vehiculo.capacidadCombustible}")
    }

    private fun realizarFiligrana(vehiculo: Vehiculo) {
        when (vehiculo) {
            is Automovil -> {
                vehiculo.realizarDerrape()
                registrarAccion(vehiculo.nombre, "Derrape: Combustible restante ${vehiculo.combustibleActual} L.")
            }
            is Motocicleta -> {
                vehiculo.realizarCaballito()
                registrarAccion(vehiculo.nombre, "Caballito: Combustible restante ${vehiculo.combustibleActual} L.")
            }
        }
    }

    private fun realizaPosiciones() {
        posiciones = mutableMapOf()

        val participantesOrdenados = participantes.sortedByDescending { vehiculo -> vehiculo.kilometrosActuales }

        participantesOrdenados.forEach { vehiculo -> posiciones[vehiculo.nombre] = vehiculo.kilometrosActuales.toInt() }
    }

    fun determinarGanador(): String {
        val ganador =  posiciones.filter { it.value > distanciaTotal }
            posiciones.forEach { (_, kmRecorridos) -> if (kmRecorridos >= distanciaTotal) estadoCarrera = false }.toString()
        return ganador.keys.toString().replace("[","").replace("]","")
    }

    private fun registrarAccion(nombreVehiculo: String, accion: String) {
        if (historialAcciones.contains(nombreVehiculo)) {
            historialAcciones[nombreVehiculo]?.add(accion)
        } else {
            historialAcciones[nombreVehiculo] = mutableListOf(accion)
        }
    }

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