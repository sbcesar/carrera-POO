fun main() {
    val listaVehiculos = listOf(
        Automovil(),
        Automovil()
    )

    println("-------AUTOMOVIL--------")
    println(automovil1.toString())
    println(automovil1.calcularAutonomia())
    automovil1.cambiarCondicionBritanica(true)
    automovil1.realizarDerrape()
    println(automovil1.toString())


    println("-------MOTOCICLETA--------")
    println(motocicleta1.toString())
    println(motocicleta1.calcularAutonomia())
    motocicleta1.realizarViaje(50f)
    println(motocicleta1.toString())
    motocicleta1.realizarCaballito()
    println(motocicleta1.toString())

}