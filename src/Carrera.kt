import kotlin.random.Random

//No es companion object
//Carrera(nombre,distancia,participantes)
//      estado
//      historial   <nombre,historial(MutableList<String>)>
//      posiciones

//Actualizar la carrera por tramos para hacer el filograma (entre 20)

/**
 * Clase Carrera que gestiona el seguimiento de movimientos, posiciones de los vehiculos y el historial de acciones.
 *
 * @property nombreCarrera: String - El nombre de la carrera para identificación.
 * @property distanciaTotal: Float - La distancia total que los vehículos deben recorrer para completar la carrera.
 * @property participantes: List<Vehiculo> - Una lista que contiene todos los vehículos participantes en la carrera.
 * @property estadoCarrera: Boolean - Un indicador de si la carrera está en curso o ha finalizado.
 * @property historialAcciones: MutableMap<String, MutableList<String>> - Un mapa para registrar el historial de acciones de cada vehículo. La clave es el nombre del vehículo y el valor es una lista de strings describiendo sus acciones.
 * @property posiciones: MutableMap<String, Int> - Una lista o diccionario para mantener un registro de la posición y los kilómetros recorridos por cada vehículo. Cada elemento es un par donde el primer valor es el nombre del vehículo y el segundo su kilometraje acumulado.
 */
class Carrera(val nombreCarrera: String, val distanciaTotal: Float, val participantes: List<Vehiculo>, var estadoCarrera: Boolean = false, val historialAcciones: MutableMap<String, MutableList<String>>, val posiciones: MutableMap<String, Int>) {

    /**
     * Inicia la carrera, estableciendo estadoCarrera a true y comenzando el ciclo de iteraciones donde los vehículos avanzan y realizan acciones.
     */
    fun iniciarCarrera() {
        estadoCarrera = true
        while (estadoCarrera) {

            estadoCarrera = false   //La carrera ha finalizado
        }
    }

    /**
     * Identificado el vehículo, le hace avanzar una distancia aleatoria entre 10 y 200 km. Si el vehículo necesita repostar, se llama al método repostarVehiculo() antes de que pueda continuar. Este método llama a realizar filigranas.
     */
    fun avanzarVehiculo(vehiculo: Vehiculo) {
        val distanciaAleatoria = Random.nextInt(10,200)
        val distanciaMaximaVehiculo = vehiculo.calcularAutonomia()
        if (distanciaAleatoria > distanciaMaximaVehiculo) {
            vehiculo.combustibleActual = 0f
            vehiculo.repostar(vehiculo.capacidadCombustible)
        }
        if (vehiculo.combustibleActual == 0f) {
            vehiculo.repostar(vehiculo.capacidadCombustible)
        }
    }

    /**
     * Reposta el vehículo seleccionado, incrementando su combustibleActual y registrando la acción en historialAcciones.
     */
    fun repostarVehiculo(vehiculo: Vehiculo, cantidad: Float) {

    }

    /**
     * Determina aleatoriamente si un vehículo realiza una filigrana (derrape o caballito) y registra la acción.
     */
    fun realizarFiligrana(vehiculo: Vehiculo) {

    }

    /**
     * Actualiza posiciones con los kilómetros recorridos por cada vehículo después de cada iteración, manteniendo un seguimiento de la competencia.
     */
    fun realizaPosiciones() {

    }

    /**
     * Revisa posiciones para identificar al vehículo (o vehículos) que haya alcanzado o superado la distanciaTotal, estableciendo el estado de la carrera a finalizado y determinando el ganador.
     */
    fun determinarGanador() {

    }

    /**
     * Devuelve una clasificación final de los vehículos, cada elemento tendrá el nombre del vehiculo, posición ocupada, la distancia total recorrida, el número de paradas para repostar y el historial de acciones. La collección estará ordenada por la posición ocupada.
     */
    //La idea principal es que la función obtenerResultados() devuelva una lista de objetos ResultadoCarrera, donde cada objeto contiene la información detallada mencionada anteriormente sobre un vehículo específico que participó en la carrera. Estos resultados están ordenados por la posición ocupada por cada vehículo al final de la carrera, de manera que el primero en la lista será el vehículo que llegó en primer lugar, y así sucesivamente.
    fun obtenerResultado() {

    }

    /**
     * Añade una acción al historialAcciones del vehículo especificado.
     */
    fun registrarAccion(vehiculo: String, accion: String) {

    }

    /**
     * Representa el resultado final de un vehículo en la carrera, incluyendo su posición final, el kilometraje total recorrido,
     * el número de paradas para repostar, y un historial detallado de todas las acciones realizadas durante la carrera.
     *
     * @property vehiculo El [Vehiculo] al que pertenece este resultado.
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