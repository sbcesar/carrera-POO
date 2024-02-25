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
        const val KM_POR_LITRO = 20
    }

    override fun calcularAutonomia(): Float {
        val rendimiento = if (cilindrada >= 1000) KM_POR_LITRO else KM_POR_LITRO - (cilindrada / 1000)
        return rendimiento * combustibleActual
    }

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

    fun realizarCaballito(): Float {
        val combustibleConsumido = 6.5f / (KM_POR_LITRO)
        combustibleActual -= combustibleConsumido
        println("La $modelo está haciendo el caballito... BOP BOP")
        return combustibleActual
    }

    override fun toString(): String {
        return "${super.toString()} Cilindrada: $cilindrada"
    }
}