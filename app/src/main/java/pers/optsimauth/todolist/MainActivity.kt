package pers.optsimauth.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BorderAll
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pers.optsimauth.todolist.database.CalendarTaskDatabase
import pers.optsimauth.todolist.entity.FourQuadrantTask
import pers.optsimauth.todolist.ui.component.dialog.AddTaskDialog
import pers.optsimauth.todolist.ui.screen.FourQuadrant
import pers.optsimauth.todolist.ui.screen.ToDoCalendar
import pers.optsimauth.todolist.ui.theme.ToDoListTheme
import pers.optsimauth.todolist.viewmodel.CalendarTaskViewModel
import pers.optsimauth.todolist.viewmodel.CalendarTaskViewModelFactory
import pers.optsimauth.todolist.viewmodel.FourQuadrantTaskViewModel
import pers.optsimauth.todolist.viewmodel.FourQuadrantTaskViewModelFactory
import java.time.LocalDate

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val calendarTaskDao = CalendarTaskDatabase.getDatabase(this).calendarTaskDao()
        val viewModelFactory = CalendarTaskViewModelFactory(calendarTaskDao)
        val calendarTaskViewModel =
            ViewModelProvider(this, viewModelFactory)[CalendarTaskViewModel::class.java]


        val fourQuadrantTaskDao = CalendarTaskDatabase.getDatabase(this).fourQuadrantTaskDao()
        val fourQuadrantViewModelFactory = FourQuadrantTaskViewModelFactory(fourQuadrantTaskDao)
        val fourQuadrantTaskViewModel =
            ViewModelProvider(
                this,
                fourQuadrantViewModelFactory
            )[FourQuadrantTaskViewModel::class.java]


        calendarTaskViewModel.deleteAllCheckedTasks()


        setContent {
            ToDoListTheme {
                App(calendarTaskViewModel, fourQuadrantTaskViewModel)
            }
        }


    }
}

@Composable
fun App(
    calendarTaskViewModel: CalendarTaskViewModel,
    fourQuadrantTaskViewModel: FourQuadrantTaskViewModel,
) {

    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    var focusedDate by remember { mutableStateOf(LocalDate.now()) }
    var showToDoCalendarAddTaskDialog by remember { mutableStateOf(false) }

    var showFourQuadrantAddTaskDialog by remember { mutableStateOf(false) }
    var focusedQuadrant by remember { mutableIntStateOf(1) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                    label = { Text("日历") },
                    selected = currentRoute == "ToDoCalendar",
                    onClick = { navController.navigate("ToDoCalendar") })
                NavigationBarItem(
                    icon = { Icon(Icons.Default.BorderAll, contentDescription = null) },
                    label = { Text("四象限") },
                    selected = currentRoute == "Camera",
                    onClick = { navController.navigate("FourQuadrant") })
            }
        },
        floatingActionButton = {
            FloatingActionButton(shape = CircleShape, modifier = Modifier.padding(8.dp),
                onClick = {
                    when (currentRoute) {
                        "ToDoCalendar" -> showToDoCalendarAddTaskDialog = true
                        "FourQuadrant" -> showToDoCalendarAddTaskDialog = true
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "ToDoCalendar",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("ToDoCalendar") {
                ToDoCalendar(onDateFocused = {
                    focusedDate = it

                }, calendarTaskViewModel = calendarTaskViewModel)
            }
            composable("FourQuadrant") {
                FourQuadrant(
                    onQuadrantFocused = { focusedQuadrant = it },
                    fourQuadrantTaskViewModel
                )
            }
        }
    }

    if (showToDoCalendarAddTaskDialog) {
        AddTaskDialog(
            focusedDate = focusedDate,
            onConfirm = { calendarTask ->
                calendarTaskViewModel.insert(calendarTask)
                showToDoCalendarAddTaskDialog = false

            },
            onDismiss = { showToDoCalendarAddTaskDialog = false })
    }

    if (showFourQuadrantAddTaskDialog) {
        AddTaskDialog(
            focusedQuadrant = focusedQuadrant,
            onConfirm = { fourQuadrantTask: FourQuadrantTask ->
                fourQuadrantTaskViewModel.insert(fourQuadrantTask)
                showFourQuadrantAddTaskDialog = false
            },
            onDismiss = { showFourQuadrantAddTaskDialog = false })
    }

}





