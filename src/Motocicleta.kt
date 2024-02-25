/**
 * Clase que representa una motocicleta.
 * @param nombre El nombre de la motocicleta.
 * @param marca La marca de la motocicleta.
 * @param modelo El modelo de la motocicleta.
 * @param capacidadCombustible La capacidad de combustible de la motocicleta en litros.
 * @param combustibleActual La cantidad de combustible actual en la motocicleta en litros.
 * @param kilometrosActuales Los kilómetros actuales recorridos por la motocicleta.
 * @param cilindrada La cilindrada de la motocicleta en centímetros cúbicos.
 */
class Motocicleta(
    nombre: String,
    marca: String,
    modelo: String,
    capacidadCombustible: Float,
    combustibleActual: Float,
    kilometrosActuales: Float,
    var cilindrada: Int
): Vehiculo(nombre, marca, modelo, capacidadCombustible, combustibleActual, kilometrosActuales) {

    companion object {
        /**
         * Kilómetros por litro de combustible para la motocicleta.
         */
        const val KM_POR_LITRO = 20
    }

    /**
     * Calcula la autonomía de la motocicleta en base a su rendimiento y la cantidad de combustible actual.
     * @return La autonomía de la motocicleta en kilómetros.
     */
    override fun calcularAutonomia(): Float {
        val rendimiento = if (cilindrada >= 1000) KM_POR_LITRO else KM_POR_LITRO - (cilindrada / 1000)
        return rendimiento * combustibleActual
    }

    /**
     * Realiza un viaje con la motocicleta, actualizando el combustible y los kilómetros actuales.
     * @param distancia La distancia a recorrer en kilómetros.
     * @return La distancia restante si la motocicleta se queda sin combustible, de lo contrario 0.
     */
    override fun realizarViaje(distancia: Float): Float {
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
     * Realiza un caballito con la motocicleta, consumiendo combustible.
     * @return El combustible actual después de realizar el caballito.
     */
    fun realizarCaballito(): Float {
        val combustibleConsumido = 6.5f / (KM_POR_LITRO)
        combustibleActual -= combustibleConsumido
        return combustibleActual
    }

    override fun toString(): String {
        return "${super.toString()} Cilindrada: $cilindrada"
    }
}