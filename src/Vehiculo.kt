import kotlin.math.pow
import kotlin.math.roundToInt

abstract class Vehiculo(
    val nombre: String,
    val marca: String,
    val modelo: String,
    val capacidadCombustible: Float,
    var combustibleActual: Float,
    var kilometrosActuales: Float
    ) {

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

    //Distancia maxima que el vehiculo soporta con el combustible que tiene
    open fun calcularAutonomia(): Float = KM_POR_LITRO * combustibleActual

    fun obtenerInformacion(): String {
        return "KM que el vehiculo puede recorrer: ${calcularAutonomia()}"
    }

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

    fun Float.redondear(digitos: Int): Float {
        val factor = 10.0.pow(digitos.toDouble()).toFloat()
        return (this * factor).roundToInt() / factor
    }

    open override fun toString(): String {
        return "Nombre: $nombre Marca: $marca Modelo: $modelo Capacidad combustible: $capacidadCombustible Combustible actual: $combustibleActual Kilometros actuales: $kilometrosActuales"
    }
}