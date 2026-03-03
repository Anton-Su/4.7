package com.example.a47

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a47.ui.theme._47Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _47Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding), this,
                    )
                }
            }
        }
    }
}
//viewModel: ViewModelBind = ViewModelBind()
@Composable
fun Greeting(modifier: Modifier = Modifier, context: ComponentActivity) {
    val bound = remember { mutableStateOf(false) }
    val serviceConnection = remember { mutableStateOf<ServiceConnection?>(null) }
    //val number = viewModel.number.collectAsState()
    val numberState = remember { mutableStateOf(0) }
    // val number = Generator.number.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (bound.value){
        Text(
            text = "${numberState.value}",
            fontSize = 70.sp,
            fontWeight = FontWeight.Bold
        )}
        else
            Text(
                text = "—",
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold
            )
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            if (!bound.value) {
                Button(modifier = Modifier.size(width = 300.dp, height = 80.dp), onClick = {
                    serviceConnection.value = object : ServiceConnection {
                        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                            val binder = binder as BindGenerator.RandomBinder
                            val service = binder.service
                            CoroutineScope(Dispatchers.Main).launch {
                                service.number.collect {
                                    numberState.value = it
                                }
                            }
                            bound.value = true
                        }
                        override fun onServiceDisconnected(name: ComponentName?) {
                            bound.value = false
                        }
                    }
                    val intent = Intent(context, BindGenerator::class.java)
                    context.bindService(
                        intent,
                        serviceConnection.value as ServiceConnection,
                        Context.BIND_AUTO_CREATE
                    )
                }
                ) {
                    Text("Подключиться")
                }
            }
            else
                Button(modifier = Modifier.size(width = 300.dp, height = 80.dp), onClick = {
                    context.unbindService(serviceConnection.value!!)
                    bound.value = false
                }) {
                    Text("Отключиться")
                }
            }
        }
    }


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    _47Theme {
//        Greeting("Android")
//    }
//}