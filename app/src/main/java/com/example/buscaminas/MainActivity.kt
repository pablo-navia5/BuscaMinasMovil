package com.example.buscaminas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buscaminas.ui.theme.BuscaminasTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BuscaminasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BuscaMinas(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable

fun BuscaMinas(modifier: Modifier = Modifier) {
    var textos by remember { mutableStateOf(MutableList(100){ "" }) }
    var minas by remember { mutableStateOf(generarMinas())  }

    Column (
        modifier = Modifier
            .padding(4.dp, 60.dp, 4.dp, 4.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
                text = "Buscaminas",
            )

            Spacer(
                modifier = Modifier
                    .height(70.dp)
            )
        }

        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ){
            Button(onClick = {
                textos = MutableList(100) { "" }
                minas = generarMinas()
            }) {
                Text("Reiniciar juego")
            }

            Spacer(
                modifier = Modifier
                    .height(50.dp)
            )
        }

        Row {
            LazyVerticalGrid(
                columns = GridCells.Fixed(10),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = modifier.padding(4.dp)
            ) {
                items(100) { index ->
                    Button(
                        onClick = {
                            textos = textos.toMutableList().also { nuevosTextos ->
                                if (minas[index]) {
                                    nuevosTextos[index] = "💣"
                                } else {
                                    revelarCasillas(index, nuevosTextos, minas)
                                }
                            }
                        },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .aspectRatio(1f)
                    ) {
                        Text (textos[index])
                    }
                }
            }
        }
    }
}

// Dado un index y el array de textos pone cuantas minas hay alrededor y llama de forma recursiva en caso de que no haya ninguna cerca.
fun revelarCasillas (index: Int, textosActuales: MutableList<String>, minas: MutableList<Boolean>) {
    // Si ya está revelada vuelve
    if (textosActuales[index].isNotEmpty()) return

    // Calcula cuántas minas hay alrededor
    val cantidad = cantidadMinasVecinas(index, minas)
    textosActuales[index] = cantidad.toString() //pone en los textos cuantas minas hay

    // Si no hay minas vecinas revela sus vecinas
    if (cantidad == 0) {
        val fila = index / 10
        val columna = index % 10

        //doble for que recorre las casillas vecinas
        for (filaRelativa in -1..1) {
            for (columnaRelativa in -1..1) {
                if (filaRelativa == 0 && columnaRelativa == 0) continue
                val nuevaFila = fila + filaRelativa
                val nuevaColumna = columna + columnaRelativa

                if (nuevaFila in 0..9 && nuevaColumna in 0..9) {
                    val nuevoIndex = nuevaFila * 10 + nuevaColumna
                    if (!minas[nuevoIndex]) { //si no hay una mina llama recursivamente a la funcion
                        revelarCasillas(nuevoIndex, textosActuales, minas)
                    }
                }
            }
        }
    }
}

fun generarMinas(): MutableList<Boolean> {
    val lista = MutableList(100) { false }
    repeat(15) {
        var pos = Random.nextInt(0, 100)
        while (lista[pos]) pos = Random.nextInt(0, 100)
        lista[pos] = true
    }
    return lista
}

//Funcion que devuelve cuantas minas hay alrededor de un index.
fun cantidadMinasVecinas(index: Int, minas: MutableList<Boolean>): Int {
    var cantidadMinas = 0

    val fila = index / 10
    val columna = index % 10

    // Recorremos las 8 posiciones vecinas
    for (filaRelativa in -1..1) { //recorre las 3 filas
        for (columnaRelativa in -1..1) { //por cada fila recorre las 3 columnas
            if (filaRelativa == 0 && columnaRelativa == 0) continue // si estamos en la 0,0 significa que es la actual asi que pasamos a la siguiente

            //comvertimos la posicion en la fila y columna del tablero.
            val nuevaFila = fila + filaRelativa
            val nuevaColumna = columna + columnaRelativa

            // Verifica que este dentro de los limites del tablero
            if (nuevaFila in 0..9 && nuevaColumna in 0..9) {
                val nuevoIndex = nuevaFila * 10 + nuevaColumna //creo el nuevo index del array la fila es *10 porque cada fila tiene 10 columnas.
                if (minas[nuevoIndex]) cantidadMinas++ //si hay mina se suma
            }
        }
    }
    return cantidadMinas
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BuscaminasTheme {
        BuscaMinas()
    }
}
