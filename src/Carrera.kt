import java.awt.Component.BaselineResizeBehavior
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

    /**
     * Inicia la carrera, estableciendo estadoCarrera a true y comenzando el ciclo de iteraciones donde los vehículos avanzan y realizan acciones.
     */
    fun iniciarCarrera() {
        estadoCarrera = true
        while (estadoCarrera) {
            val vehiculoRandom = participantes.random()
            avanzarVehiculo(vehiculoRandom)
            cajaSorpresa(vehiculoRandom)
            realizaPosiciones()
            determinarGanador()
        }
    }

    private fun cajaSorpresa(vehiculo: Vehiculo) {
        val cajaRandom = (1..10).random()

        when (cajaRandom) {
            1 -> {
                println("PREMIO!!")
                sumar10(vehiculo)
            }
            2 -> {
                println("PREMIO!!")
                TODO("Funcion Teletransporte: Premio que nos teletransporta 100km más adelante en la carrera (sin superar la distancia total de la carrera). ")
            }
            3 -> {
                println("PREMIO!!")
                TODO("Funcion RetrasarTodos: Retrasar al resto de vehículos 100km (tope mínimo km 0).")
            }
            4 -> {
                println("PENALIZACION!!")
                TODO("Funcion VehiculoAlInicio: Retrasar un vehículo al azar al inicio (km 0)... puede ser el mismo vehículo al que le ha tocado la caja.")
            }
            5 -> {
                println("PENALIZACION!!")
                TODO("Funcion CasillaDeSalida: Retrasar nuestro vehículo al inicio (km 0).")
            }
            6 -> {
                println("PENALIZACION!!")
                TODO("Funcion Restar5: Penalización que resta 5km por litro para el siguiente avance en nuestro vehículo.")
            }
            7 -> {
                println("Caja vacia (No realiza ninguna accion)")
            }
            8 -> {
                println("Caja vacia (No realiza ninguna accion)")
            }
            9 -> {
                println("Caja vacia (No realiza ninguna accion)")
            }
            10 -> {
                println("Caja vacia (No realiza ninguna accion)")
            }
        }
    }

    private fun sumar10(vehiculo: Vehiculo) {
        vehiculo.combustibleActual += vehiculo.combustibleActual * 10
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

            distanciaARecorrer = (distanciaARecorrer - distanciaTramo).redondear(2)
        }

        registrarAccion(vehiculo.nombre, "Finaliza viaje: Total recorrido $distanciaAleatoria kms (${vehiculo.kilometrosActuales} kms y ${vehiculo.combustibleActual} L actuales)")
    }

    /**
     * Reposta el vehículo seleccionado, incrementando su combustibleActual y registrando la acción en historialAcciones.
     */
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

    /**
     * Actualiza posiciones con los kilómetros recorridos por cada vehículo después de cada iteración, manteniendo un seguimiento de la competencia.
     */
    private fun realizaPosiciones() {
        posiciones = mutableMapOf()
        //Ordena los participantes segun la distancia recorrida de mayor a menor
        val participantesOrdenados = participantes.sortedByDescending { vehiculo -> vehiculo.kilometrosActuales }

        //Actualiza en base al orden
        participantesOrdenados.forEach { vehiculo -> posiciones[vehiculo.nombre] = vehiculo.kilometrosActuales.toInt() }
    }

    /**
     * Revisa posiciones para identificar al vehículo (o vehículos) que haya alcanzado o superado la distanciaTotal, estableciendo el estado de la carrera a finalizado y determinando el ganador.
     */
    fun determinarGanador(): String {
        val ganador =  posiciones.filter { it.value > distanciaTotal }
            posiciones.forEach { (_, kmRecorridos) -> if (kmRecorridos >= distanciaTotal) estadoCarrera = false }.toString()
        return ganador.keys.toString().replace("[","").replace("]","")
    }

    /**
     * Añade una acción al historialAcciones del vehículo especificado.
     */
    private fun registrarAccion(nombreVehiculo: String, accion: String) {
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