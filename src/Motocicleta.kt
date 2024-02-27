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
    private var cilindrada: Int
): Vehiculo(nombre, marca, modelo, capacidadCombustible, combustibleActual, kilometrosActuales) {

    companion object {
        /**
         * Kilómetros por litro de combustible para la motocicleta.
         */
        const val KM_POR_LITRO = 20f
    }

    private fun obtenerKmLitro(): Float {
        return if (cilindrada >= 1000) KM_POR_LITRO else (KM_POR_LITRO - (cilindrada / 1000)).redondear(2)
    }

    /**
     * Calcula la autonomía de la motocicleta en base a su rendimiento y la cantidad de combustible actual.
     * @return La autonomía de la motocicleta en kilómetros.
     */
    override fun calcularAutonomia(): Float {
        return obtenerKmLitro() * combustibleActual
    }

    override fun actualizarCombustible(distancia: Float) {
        combustibleActual -= (distancia / obtenerKmLitro()).redondear(2)
    }

    /**
     * Realiza un caballito con la motocicleta, consumiendo combustible.
     * @return El combustible actual después de realizar el caballito.
     */
    fun realizarCaballito(): Float {
        val combustibleConsumido = 6.5f / obtenerKmLitro()
        combustibleActual -= combustibleConsumido
        return combustibleActual
    }

    override fun toString(): String {
        return "${super.toString()} Cilindrada: $cilindrada"
    }
}