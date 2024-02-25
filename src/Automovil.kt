class Automovil(
    nombre: String,
    marca: String,
    modelo: String,
    capacidadCombustible: Float,
    combustibleActual: Float,
    kilometrosActuales: Float,
    var esHibrido: Boolean,
    var condicionBritanica: Boolean
): Vehiculo(nombre, marca, modelo, capacidadCombustible, combustibleActual, kilometrosActuales) {

    companion object{
        const val KM_POR_LITRO = 15
    }

    /**
     * Modifica el cálculo de autonomía asumiendo un rendimiento de 5 km más por litro si es híbrido.
     */
    override fun calcularAutonomia(): Float {
        return if (esHibrido) {
            KM_POR_LITRO * combustibleActual
        } else {
            super.calcularAutonomia()
        }
    }

    fun cambiarCondicionBritanica(nuevaCondicion: Boolean) {
        condicionBritanica = nuevaCondicion
        println("Se ha cambiado la condicion británica")
    }

    /**
     * Realiza un derrape, gastando 6.25 km si es híbrido y 7.5 km si no lo es.
     *
     * @return Retorna el combustible restante después del derrape.
     */
    fun realizarDerrape(): Float {
        val distanciaDerrape = if (esHibrido) 6.25f else 7.5f
        val combustibleConsumido = distanciaDerrape / (KM_POR_LITRO)
        combustibleActual -= combustibleConsumido
        println("El $modelo ha derrapado... BRUM BRUM")
        return combustibleActual
    }

    override fun toString(): String {
        return "${super.toString()} ¿Hibrido? -> ${if (esHibrido) "Sí" else "No"} ¿Conducción Britanica? -> ${if (condicionBritanica) "Sí" else "No"}"
    }

}