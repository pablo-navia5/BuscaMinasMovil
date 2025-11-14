package com.example.buscaminas
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buscaminas.ui.theme.BuscaminasTheme
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    var CANTIDADBOMBAS = 10
    var TAMANO = 10
    var TOTAL = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BuscaminasTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "inicial",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable("juego") { BuscaMinas(navController) }
                        composable("inicial") { PantallaInicial(navController) }
                    }
                }

            }
        }
    }

    @SuppressLint("MutableCollectionMutableState")
    @Composable
    fun BuscaMinas(navController: NavController) {
        var textos by remember { mutableStateOf(MutableList(TOTAL) { "" }) }
        var minas by remember { mutableStateOf(generarMinas()) }
        var textoFinal by remember { mutableStateOf("") }
        var juegoTerminado by remember { mutableStateOf(false) }
        var ponerBandera by remember { mutableStateOf(false) }
        var colores by remember { mutableStateOf(MutableList(TOTAL) { Color.DarkGray }) }

        Column(
            modifier = Modifier
                .padding(4.dp, 60.dp, 4.dp, 4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    "🧨💣 Buscaminas 💣🧨", fontSize = 25.sp
                )

                Spacer(
                    modifier = Modifier
                        .height(70.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(onClick = {
                    textos = MutableList(TOTAL) { "" }
                    minas = generarMinas()
                    juegoTerminado = false
                    textoFinal = ""
                    colores = MutableList(TOTAL) { Color.DarkGray }
                }) {
                    Text("Reiniciar juego", fontSize = 15.sp)
                }

                Spacer(
                    modifier = Modifier
                        .height(50.dp)
                )
            }

            Row {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(TAMANO),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.padding(4.dp)
                ) {
                    items(TOTAL) { index ->
                        Button(
                            onClick = {
                                if (juegoTerminado) return@Button

                                val nuevosTextos = textos.toMutableList()
                                val nuevosColores = colores.toMutableList()

                                if (ponerBandera) {
                                    nuevosTextos[index] =
                                        if (nuevosTextos[index] == "🚩") "" else "🚩" // toggle bandera
                                    textos = nuevosTextos
                                    return@Button
                                }

                                if (minas[index]) {
                                    juegoTerminado = true
                                    nuevosTextos[index] = "💣"
                                    textoFinal = "Has tocado una bomba. Perdiste"
                                    mostrarBombas(nuevosTextos, minas)
                                } else {
                                    revelarCasillas(index, nuevosTextos, minas, nuevosColores)
                                }

                                colores = nuevosColores
                                textos = nuevosTextos

                                if (juegoGanado(nuevosTextos)) {
                                    textoFinal = "Juego terminado has ganado"
                                    mostrarBombas(nuevosTextos, minas)
                                    juegoTerminado = true
                                }
                            },
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colores[index],
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .aspectRatio(1f)
                        ) {
                            Text(textos[index])
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier
                    .height(40.dp)
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        ponerBandera = !ponerBandera
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (ponerBandera) Color.Green else Color.Blue
                    )

                ) {
                    Text(
                        "Pulsa para poner bandera 🚩",
                        fontSize = 15.sp
                    )
                }

            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    textoFinal,
                    fontSize = 25.sp,
                )
            }
        }
    }

    @Composable
    fun PantallaInicial(navController: NavHostController) {
        var textoCarga by remember { mutableStateOf("") }
        var nombre by remember { mutableStateOf("") }
        var expanded by remember {mutableStateOf(false)}
        var textoTablero by remember {mutableStateOf("Selecciona tamaño")}

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp, 60.dp, 4.dp, 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ){
            Text(
                "Juego del buscaminas", fontSize = 20.sp
            )

            Spacer(modifier = Modifier
                .height(30.dp)
            )

            TextField (
                value = nombre,
                onValueChange ={ nombre = it },
                placeholder ={Text("Nombre")},
                textStyle = TextStyle(fontSize = 15.sp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {

                Button(
                    onClick = {
                        expanded = true
                    }
                ) {
                    Text(textoTablero, fontSize = 18.sp)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Tamaño 3x3") },
                        onClick = {
                            expanded = false
                            TAMANO = 3
                            CANTIDADBOMBAS = 3
                            TOTAL = 3 * 3
                            textoTablero = "Tamaño 3x3"
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Tamaño 5x5") },
                        onClick = {
                            expanded = false
                            TAMANO = 5
                            CANTIDADBOMBAS = 5
                            TOTAL = 5 * 5
                            textoTablero = "Tamaño 5x5"
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Tamaño 10x10") },
                        onClick = {
                            expanded = false
                            TAMANO = 10
                            CANTIDADBOMBAS = 10
                            TOTAL = 10 * 10
                            textoTablero = "Tamaño 10x10"
                        }
                    )
                }
            }

            Button(
                onClick = {
                    textoCarga = "Cargando ..."
                    navController.navigate("juego")
                }
            ){
                Text("Iniciar juego", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text (textoCarga, fontSize = 20.sp)
        }
    }

    // Dado un index y el array de textos pone cuantas minas hay alrededor y llama de forma recursiva en caso de que no haya ninguna cerca.
    fun revelarCasillas(
        index: Int,
        textosActuales: MutableList<String>,
        minas: MutableList<Boolean>,
        colores: MutableList<Color>
    ) {
        // Si ya está revelada vuelve
        if (textosActuales[index].isNotEmpty()) return

        // Calcula cuántas minas hay alrededor
        val cantidad = cantidadMinasVecinas(index, minas)

        if (cantidad == 0) {
            textosActuales[index] = " "
            colores[index] = Color.Gray
        } else {
            textosActuales[index] = cantidad.toString() //pone en los textos cuantas minas hay
            colores[index] = Color.Gray
        }

        // Si no hay minas vecinas revela sus vecinas
        if (cantidad == 0) {
            val fila = index / TAMANO
            val columna = index % TAMANO

            //doble for que recorre las casillas vecinas
            for (filaRelativa in -1..1) {
                for (columnaRelativa in -1..1) {
                    if (filaRelativa == 0 && columnaRelativa == 0) continue
                    val nuevaFila = fila + filaRelativa
                    val nuevaColumna = columna + columnaRelativa

                    if (nuevaFila in 0 until TAMANO && nuevaColumna in 0 until TAMANO) {
                        val nuevoIndex = nuevaFila * TAMANO + nuevaColumna
                        if (!minas[nuevoIndex]) { //si no hay una mina llama recursivamente a la funcion
                            revelarCasillas(nuevoIndex, textosActuales, minas, colores)
                        }
                    }
                }
            }
        }
    }

    fun mostrarBombas(nuevosTextos: MutableList<String>, minas: MutableList<Boolean>) {
        for (i in 0..minas.size - 1) {
            if (minas[i]) nuevosTextos[i] = "💣"
        }
    }

    fun juegoGanado(textos: MutableList<String>): Boolean {
        var cantidadPulsados = 0
        for (i in 0..textos.size - 1) {
            if (textos[i].isNotEmpty() && textos[i] != "🚩") cantidadPulsados++
        }
        if (cantidadPulsados >= (TOTAL - CANTIDADBOMBAS)) return true
        else return false
    }

    fun generarMinas(): MutableList<Boolean> {
        val lista = MutableList(TOTAL) { false }
        repeat(CANTIDADBOMBAS) {
            var pos = Random.nextInt(0, TOTAL)
            while (lista[pos]) pos = Random.nextInt(0, TOTAL)
            lista[pos] = true
        }
        return lista
    }

    //Funcion que devuelve cuantas minas hay alrededor de un index.
    fun cantidadMinasVecinas(index: Int, minas: MutableList<Boolean>): Int {
        var cantidadMinas = 0

        val fila = index / TAMANO
        val columna = index % TAMANO

        // Recorremos las 8 posiciones vecinas
        for (filaRelativa in -1..1) { //recorre las 3 filas
            for (columnaRelativa in -1..1) { //por cada fila recorre las 3 columnas
                if (filaRelativa == 0 && columnaRelativa == 0) continue // si estamos en la 0,0 significa que es la actual asi que pasamos a la siguiente

                //comvertimos la posicion en la fila y columna del tablero.
                val nuevaFila = fila + filaRelativa
                val nuevaColumna = columna + columnaRelativa

                // Verifica que este dentro de los limites del tablero
                if (nuevaFila in 0 until TAMANO && nuevaColumna in 0 until TAMANO) {
                    val nuevoIndex =
                        nuevaFila * TAMANO + nuevaColumna //creo el nuevo index del array la fila es *10 porque cada fila tiene 10 columnas.
                    if (minas[nuevoIndex]) cantidadMinas++ //si hay mina se suma
                }
            }
        }
        return cantidadMinas
    }
}
