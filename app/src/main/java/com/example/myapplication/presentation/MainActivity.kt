/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.myapplication.presentation

import android.app.RemoteInput
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.wearableExtender
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.myapplication.R
import com.example.myapplication.presentation.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)
        setContent {
            WearInputExample()
        }


    }
}

@Composable
fun WearInputExample() {
    val context = LocalContext.current

    // Almacenar el texto ingresado
    var inputText by remember { mutableStateOf("Hello, Wear OS!") }

    // Key para identificar el input
    val inputTextKey = "user_input_key"

    // Configurar RemoteInput
    val remoteInput = RemoteInput.Builder(inputTextKey)
        .setLabel("Enter your input") // Etiqueta que se muestra al usuario
        .wearableExtender {
            setEmojisAllowed(true) // Permitir entrada de emojis
            setInputActionType(EditorInfo.IME_ACTION_DONE)
        }
        .build()

    // Lanzador para manejar resultados
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val results = RemoteInput.getResultsFromIntent(result.data)
        val receivedInput = results?.getCharSequence(inputTextKey)?.toString()
        inputText = receivedInput ?: "No input received"
    }

    // Configurar Intent para iniciar RemoteInput
    val intent = RemoteInputIntentHelper.createActionRemoteInputIntent().apply {
        RemoteInputIntentHelper.putRemoteInputsExtra(this, listOf(remoteInput))
    }

    // UI de la aplicación
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Input: $inputText", modifier = Modifier.padding(16.dp))

        Button(onClick = { launcher.launch(intent) }) {
            Text(text = "Enter Input")
        }
    }
}
