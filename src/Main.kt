import kotlin.math.pow
import kotlin.math.roundToInt

fun Float.redondear(digitos: Int): Float {
    val factor = 10.0.pow(digitos.toDouble()).toFloat()
    return (this * factor).roundToInt() / factor
}

/**
 * Funcion String que elimina el espacio en blanco sobrante al inicio y al final de la cadena y cambia a mayusculas cada palabra del string
 *
 * @return devuelve la palabra formateada
 */
fun String.formatearEspaciosCapitalizar(): String {
    val nuevaPalabra = this.trim().split(" ").map { it.capitalize() }
    return nuevaPalabra.joinToString(" ")
}

fun main() {

    val aurora = Automovil("Aurora", "Seat", "Panda", 50f, 50f * 0.1f, 0f, true,false) // Coche eléctrico con capacidad de 50 litros, inicia con el 10%
    val boreal = Automovil("Boreal", "BMW", "M8", 80f, 80f * 0.1f, 0f, false,true) // SUV híbrido con capacidad de 80 litros, inicia con el 10%
    val cefiro = Motocicleta("Céfiro", "Derbi", "Motoreta", 15f, 15f * 0.1f, 0f, 500) // Motocicleta de gran cilindrada con capacidad de 15 litros, inicia con el 10%
    val dinamo = Automovil("Dinamo", "Cintroen", "Sor", 70f, 70f * 0.1f, 0f, true,false) // Camioneta eléctrica con capacidad de 70 litros, inicia con el 10%
    val eclipse = Automovil("Eclipse", "Renault", "Espacio", 60f, 60f * 0.1f, 0f, false,true) // Coupé deportivo con capacidad de 60 litros, inicia con el 10%
    val fenix = Motocicleta("Fénix", "Honda", "Vital", 20f, 20f * 0.1f, 0f, 250) // Motocicleta eléctrica con capacidad de 20 litros, inicia con el 10%

    val participantes = listOf(aurora,boreal,cefiro,dinamo,eclipse,fenix)

    val carrera = Carrera("     gran carrera de filigranas     ", 1000f, participantes)



    println("*** ${carrera.nombreCarrera.formatearEspaciosCapitalizar()} ***")
    println()
    println("¡Comienza la carrera!")
    println("••••••••••••••••••••••••••••")
    println("¡Carrera finalizada!")
    carrera.iniciarCarrera()
    val ganador = carrera.obtenerResultado()
    println()
    println("¡¡¡ ENHORABUENA ${carrera.determinarGanador()} !!!")
    println()
    println("* Clasificacion: ")
    println()
    carrera.posiciones.toList()
        .sortedByDescending { (_, kmRecorridos) -> kmRecorridos }
        .forEachIndexed { index, (nombre, kmRecorridos) ->
            println("${index + 1} -> $nombre ($kmRecorridos km)")
    }
    println()
    ganador.forEach { println(it) }
    println()
    println("* Historial Detallado: ")
    println()
    carrera.historialAcciones.toList()
        .forEachIndexed { index, (nombre, historial) ->
            println("${index + 1} -> $nombre\n" +
                    historial.joinToString("\n")
            )
        }
}