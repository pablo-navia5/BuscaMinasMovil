package com.example.buscaminas

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun PantallaInicial(navController: NavHostController) {
    var textoCarga by remember { mutableStateOf("") }

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
            .height(30.dp))

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