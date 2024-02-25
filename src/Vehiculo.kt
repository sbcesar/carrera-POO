/**
 * Clase abstracta que representa un vehículo genérico.
 *
 * @property nombre Nombre del vehículo.
 * @property marca Marca del vehículo.
 * @property modelo Modelo del vehículo.
 * @property capacidadCombustible Capacidad total de combustible del vehículo.
 * @property combustibleActual Cantidad actual de combustible en el vehículo.
 * @property kilometrosActuales Kilómetros recorridos por el vehículo.
 */
abstract class Vehiculo(
    val nombre: String,
    val marca: String,
    val modelo: String,
    capacidadCombustible: Float,
    combustibleActual: Float,
    kilometrosActuales: Float
    ) {

    var capacidadCombustible: Float = capacidadCombustible.redondear(2)
        set(value) {
            field = value.redondear(2)
        }

    var combustibleActual: Float = combustibleActual.redondear(2)
        set(value) {
            field = value.redondear(2)
        }

    var kilometrosActuales: Float = kilometrosActuales.redondear(2)
        set(value) {
            field = value.redondear(2)
        }

    companion object {
        var KM_POR_LITRO = 10
        val nombreVehiculos = mutableSetOf<String>()
    }

    init {
        if (nombreVehiculos.contains(nombre)) {
            throw IllegalArgumentException("El nombre $nombre ya está en uso.")
        } else {
            nombreVehiculos.add(nombre)
        }
    }

    /**
     * Calcula la autonomía del vehículo en kilómetros.
     *
     * @return La distancia máxima que el vehículo puede recorrer con el combustible actual.
     */
    open fun calcularAutonomia(): Float = KM_POR_LITRO * combustibleActual

    /**
     * Obtiene la información del vehículo.
     *
     * @return La información del vehículo en formato de cadena.
     */
    fun obtenerInformacion(): String {
        return "KM que el vehiculo puede recorrer: ${calcularAutonomia()}"
    }

    /**
     * Realiza un viaje con el vehículo.
     *
     * @param distancia La distancia a recorrer en kilómetros.
     * @return La distancia restante si el vehículo se queda sin combustible durante el viaje, de lo contrario, 0.
     */
    open fun realizarViaje(distancia: Float): Float {
        val distanciaMaxima = calcularAutonomia()
        if (distancia <= distanciaMaxima) {
            val combustibleConsumido = distancia / KM_POR_LITRO
            combustibleActual -= combustibleConsumido
            kilometrosActuales += distancia
            return 0f   //Ha terminado el viaje
        } else {
            //Si la distancia es mayor que la autonomia, el vehiculo se queda sin combustible
            combustibleActual = 0f
            kilometrosActuales += distanciaMaxima
            return distancia - distanciaMaxima      //Distancia restante
        }
    }

    /**
     * Reposta combustible en el vehículo.
     *
     * @param cantidad La cantidad de combustible a repostar en litros.
     * @return La cantidad de combustible que se ha repostado en litros.
     */
    fun repostar(cantidad: Float): Float {
        if (cantidad < 0f) {
            return 0f
        } else if (cantidad + combustibleActual >= capacidadCombustible) {
            //Si la cantidad llena el tanque
            val repostado = capacidadCombustible - combustibleActual
            combustibleActual = capacidadCombustible
            return repostado
        } else {
            //Si no llena el tanque
            combustibleActual += cantidad
            return cantidad
        }
    }

    override fun toString(): String {
        return "Nombre: $nombre Marca: $marca Modelo: $modelo Capacidad combustible: $capacidadCombustible Combustible actual: $combustibleActual Kilometros actuales: $kilometrosActuales"
    }
}