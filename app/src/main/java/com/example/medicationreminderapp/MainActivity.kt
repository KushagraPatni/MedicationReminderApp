    package com.example.medicationreminderapp

    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Add
    import androidx.compose.material3.Button
    import androidx.compose.material3.Card
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.ModalBottomSheet
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Surface
    import androidx.compose.material3.Switch
    import androidx.compose.material3.Text
    import androidx.compose.material3.TimePicker
    import androidx.compose.material3.TopAppBar
    import androidx.compose.material3.rememberModalBottomSheetState
    import androidx.compose.material3.rememberTimePickerState
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.rememberCoroutineScope
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.window.Dialog
    import androidx.hilt.navigation.compose.hiltViewModel
    import com.example.medicationreminderapp.domain.model.Reminder
    import com.example.medicationreminderapp.presentation.MainViewModel
    import com.example.medicationreminderapp.presentation.cancelAlarm
    import com.example.medicationreminderapp.presentation.setUpAlarm
    import com.example.medicationreminderapp.presentation.setUpPeriodicAlarm
    import com.example.medicationreminderapp.ui.theme.MedicationReminderAppTheme
    import dagger.hilt.android.AndroidEntryPoint
    import kotlinx.coroutines.launch
    import java.lang.reflect.Array
    import java.lang.reflect.Array.set
    import java.text.SimpleDateFormat
    import java.util.Calendar
    import java.util.Locale

    @AndroidEntryPoint
    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                MedicationReminderAppTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val viewModel= hiltViewModel<MainViewModel>()
                        MainScreen(viewModel  )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen(viewModel: MainViewModel){
        val uiState by viewModel.uiState.collectAsState()
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        val context= LocalContext.current
        val isTimePickerVisible = remember {mutableStateOf(false)}
        val timePickerState= rememberTimePickerState()
        val format= remember {SimpleDateFormat("hh:mm a", Locale.getDefault())}
        val timeInMillis= remember {
            mutableStateOf(0L)
        }
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.show()
                }
            },
            sheetState = sheetState
            //,Form(time = "", onTimeClick = {} )
        )
        {
            Form(time = format.format(timeInMillis.value), onTimeClick = { isTimePickerVisible.value=true } ){name,dosage,check->
                val reminder = Reminder(
                    name,dosage,timeInMillis.value,isTaken=false,
                    isRepeat=check
                )
                viewModel.insert(reminder)
                if(check){
                    setUpPeriodicAlarm(context,reminder)
                }
                else{
                    setUpAlarm(context,reminder)
                }
                scope.launch {
                    sheetState.hide()
                }
            }
        Scaffold(topBar = { TopAppBar(title = {Text(text = "Medication Reminder")},
            actions ={
                IconButton(onClick = {
                    scope.launch{sheetState.show()}
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription =null )
                }
            })}) {
            if(isTimePickerVisible.value){
                Dialog(onDismissRequest = {}) {
                    Column {
                        TimePicker(state = timePickerState)
                        Row {
                            Button(onClick = {
                                isTimePickerVisible.value=!isTimePickerVisible.value
                            } ) {
                                Text(text = "Cancel")
                            }

                            Button(onClick = {
                                val calendar=Calendar.getInstance().apply{
                                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                    set(Calendar.MINUTE,timePickerState.minute)
                                }
                                timeInMillis.value = calendar.timeInMillis
                                isTimePickerVisible.value=false
                            }) {
                                Text(text = "Confirm")
                            }
                        }
                    }
                }
            }


                if(uiState.data.isEmpty()){
                        Box(modifier = Modifier
                            .padding(it)
                            .fillMaxSize(), contentAlignment = Alignment.Center){
                            Text(text = "Nothing Found")
                        }
                    }
                    else{
                        LazyColumn(modifier = Modifier
                            .padding(it)
                            .fillMaxSize()){
                            items(uiState.data){
                                Card(modifier = Modifier.padding(8.dp)) {
                                    Row (modifier = Modifier.padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically){
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text =it.name)
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(text = it.dosage)
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(text = format.format(it.timeInMillis))
                                        }
                                        if(it.isRepeat){
                                            IconButton(onClick = {
                                                cancelAlarm(context,it)
                                                viewModel.update(it.copy(isTaken = true, isRepeat = false))
                                            }) {
                                                Icon(painter = painterResource(id = R.drawable.ic_schedule), contentDescription =null )
                                            }
                                        }
                                        IconButton(onClick = {
                                            cancelAlarm(context,it)
                                            viewModel.delete(it)}) {
                                            Icon(painter = painterResource(id = R.drawable.ic_delete),
                                                contentDescription =null )
                                        }
                                    }
                                }
                            }
                        }
                    }

        }
        }
    }

    @Composable
    fun Form(time:String,onTimeClick:()->Unit,onClick:(String,String,Boolean)->Unit= { _, _, _ -> }){
        val name = remember {mutableStateOf("") }
        val dosage = remember {mutableStateOf("") }
        val isChecked = remember {mutableStateOf(false)}
        Column(modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
        , horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(value =name.value , onValueChange ={
                name.value=it
            }, modifier = Modifier.fillMaxWidth(), singleLine = true )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value =dosage.value , onValueChange ={
                dosage.value=it
            }, modifier = Modifier.fillMaxWidth(), singleLine = true )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value =time , onValueChange ={
            }, modifier = Modifier
                .fillMaxWidth()
                .clickable { onTimeClick.invoke() }
                , enabled = false )
            Spacer(modifier = Modifier.height(8.dp))

            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                    Text(text = "Schedule")
                    Spacer(modifier = Modifier.width(12.dp))
                    Switch(checked =isChecked.value , onCheckedChange ={
                        isChecked.value=it
                    } )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = {
                onClick.invoke(name.value,dosage.value,isChecked.value)
            }) {
                Text(text = "Save")
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }


    @Preview(showBackground = true)
    @Composable
    @ExperimentalMaterial3Api
    fun GreetingPreview() {
        MedicationReminderAppTheme {

        }
    }