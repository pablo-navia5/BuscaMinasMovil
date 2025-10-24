package com.example.buscaminas

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
                    val color = if (minas[index]) Color.Red else Color.DarkGray
                    //val color = Color.Gray

                    Button(
                        onClick = {
                            textos = textos.toMutableList().also {
                                var cantidadMinas = cantidadMinasVecinas(index, minas)
                                it[index] = cantidadMinas.toString()

                                if (cantidadMinas == 0) {

                                }
                            }
                        },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = color,
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

fun generarMinas(): MutableList<Boolean> {
    val lista = MutableList(100) { false }
    repeat(15) {
        var pos = Random.nextInt(0, 100)
        while (lista[pos]) pos = Random.nextInt(0, 100)
        lista[pos] = true
    }
    return lista
}

fun cantidadMinasVecinas(index: Int, minas: MutableList<Boolean>): Int {
    var cantidadMinas = 0

    //esto calcula en que fila y columna
    val fila = index / 10
    val columna = index % 10

    // Recorremos las 8 posiciones vecinas haciendo que la posicion sea relativa (es decir 0,0 es la actual y 0,1 sera la derecha)
    for (filaRelativa in -1..1) { //recorre las 3 filas (-1 es la de arriba, 0 es la que esta el boton y  1 es la de abajo)
        for (columnaRelativa in -1..1) { //recorre las 3 columnas (-1 es la de izquierda, 0 es la del boton y 1 es la de derecha)
            if (filaRelativa == 0 && columnaRelativa == 0) continue // si estamos en la 0,0 es la actual asi que no la miramos.

            //comvertimos la posicion relativa al boton en la posicion absoluta en el tablero.
            val nuevaFila = fila + filaRelativa
            val nuevaColumna = columna + columnaRelativa

            // Verificar que esté dentro de los límites del tablero
            if (nuevaFila in 0..9 && nuevaColumna in 0..9) {
                val nuevoIndex = nuevaFila * 10 + nuevaColumna //el nuevo index del array la fila es *10 porque cada fila tiene 10 columnas.
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
