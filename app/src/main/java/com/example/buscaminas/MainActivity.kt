package com.example.buscaminas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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

    val minas = remember {
        val lista = MutableList(100) { false }
        repeat(10) {
            var posMarcar = Random.nextInt(0, 100)
            while (lista[posMarcar]) {
                posMarcar = Random.nextInt(0, 100)
            }
            lista[posMarcar] = true
        }
        lista
    }

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
                    textos = textos.toMutableList().also { //El .also() es como hacer una copia y luego actualizar el original con la copia.
                        it[index] = if (minas[index]) "1" else "0"
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


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BuscaminasTheme {
        BuscaMinas()
    }
}
