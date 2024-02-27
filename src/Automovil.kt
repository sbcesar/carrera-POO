/**
 * Clase que representa un automóvil.
 * @param nombre El nombre del automóvil.
 * @param marca La marca del automóvil.
 * @param modelo El modelo del automóvil.
 * @param capacidadCombustible La capacidad de combustible del automóvil en litros.
 * @param combustibleActual La cantidad de combustible actual en el automóvil en litros.
 * @param kilometrosActuales Los kilómetros actuales recorridos por el automóvil.
 * @param esHibrido Indica si el automóvil es híbrido o no.
 * @param condicionBritanica Indica si el automóvil está configurado para conducir en condiciones británicas o no.
 */
class Automovil(
    nombre: String,
    marca: String,
    modelo: String,
    capacidadCombustible: Float,
    combustibleActual: Float,
    kilometrosActuales: Float,
    private var esHibrido: Boolean
): Vehiculo(nombre, marca, modelo, capacidadCombustible, combustibleActual, kilometrosActuales) {

    companion object{

        const val KM_POR_LITRO = 15

        var condicionBritanica: Boolean = false

        /**
         * Cambia la condición británica del automóvil.
         * @param nuevaCondicion La nueva condición británica.
         */
        fun cambiarCondicionBritanica(nuevaCondicion: Boolean) {
            condicionBritanica = nuevaCondicion
            println("Se ha cambiado la condicion británica")
        }
    }

    /**
     * Modifica el cálculo de autonomía asumiendo un rendimiento de 5 km más por litro si es híbrido.
     * @return La autonomía del automóvil en kilómetros.
     */
    override fun calcularAutonomia(): Float {
        return if (esHibrido) {
            KM_POR_LITRO * combustibleActual
        } else {
            super.calcularAutonomia()
        }
    }

    override fun actualizarCombustible(distancia: Float) {
        combustibleActual -= (distancia / KM_POR_LITRO).redondear(2)
    }

    /**
     * Realiza un derrape, gastando 6.25 km si es híbrido y 7.5 km si no lo es.
     * @return El combustible restante después del derrape.
     */
    fun realizarDerrape(): Float {
        val distanciaDerrape = if (esHibrido) 6.25f else 7.5f
        val combustibleConsumido = distanciaDerrape / (KM_POR_LITRO)
        combustibleActual -= combustibleConsumido
        return combustibleActual
    }

    override fun toString(): String {
        return "${super.toString()} ¿Hibrido? -> ${if (esHibrido) "Sí" else "No"} ¿Conducción Britanica? -> ${if (condicionBritanica) "Sí" else "No"}"
    }

}