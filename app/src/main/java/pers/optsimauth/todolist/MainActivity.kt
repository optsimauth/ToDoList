package pers.optsimauth.todolist

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pers.optsimauth.todolist.database.TaskDatabase
import pers.optsimauth.todolist.entity.Task
import pers.optsimauth.todolist.ui.component.dialog.AddCalendarTaskDialog
import pers.optsimauth.todolist.ui.component.dialog.AddFourQuadrantTaskDialog
import pers.optsimauth.todolist.ui.screen.FourQuadrant
import pers.optsimauth.todolist.ui.screen.ToDoCalendar
import pers.optsimauth.todolist.ui.theme.ToDoListTheme
import pers.optsimauth.todolist.viewmodel.CalendarTaskViewModel
import pers.optsimauth.todolist.viewmodel.FourQuadrantTaskViewModel
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initializeAndCleanup()

        setContent {
            ToDoListTheme {
                App(viewModel)
            }
        }
    }
}

class MainViewModel(
    val calendarTaskViewModel: CalendarTaskViewModel,
    val fourQuadrantTaskViewModel: FourQuadrantTaskViewModel,
) : ViewModel() {
    var focusedDate by mutableStateOf(LocalDate.now())
        private set

    var focusedQuadrant by mutableIntStateOf(1)
        private set

    var showToDoCalendarAddTaskDialog by mutableStateOf(false)
        private set

    var showFourQuadrantAddTaskDialog by mutableStateOf(false)
        private set

    fun onDateFocused(date: LocalDate) {
        focusedDate = date
    }

    fun onQuadrantFocused(quadrant: Int) {
        focusedQuadrant = quadrant
    }

    fun showCalendarDialog() {
        showToDoCalendarAddTaskDialog = true
    }

    fun showQuadrantDialog() {
        showFourQuadrantAddTaskDialog = true
    }

    fun hideCalendarDialog() {
        showToDoCalendarAddTaskDialog = false
    }

    fun hideQuadrantDialog() {
        showFourQuadrantAddTaskDialog = false
    }

    fun addCalendarTask(task: Task.CalendarTask) {
        calendarTaskViewModel.insert(task)
        hideCalendarDialog()
    }

    fun addQuadrantTask(task: Task.FourQuadrantTask) {
        fourQuadrantTaskViewModel.insert(task)
        hideQuadrantDialog()
    }

    fun initializeAndCleanup() {
        calendarTaskViewModel.deleteAllCheckedTasks()
        fourQuadrantTaskViewModel.deleteAllCheckedTasks()
    }
}

class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val database = TaskDatabase.getDatabase(application)

            val calendarTaskViewModel = CalendarTaskViewModel(database.calendarTaskDao())
            val fourQuadrantTaskViewModel =
                FourQuadrantTaskViewModel(database.fourQuadrantTaskDao())

            @Suppress("UNCHECKED_CAST")
            return MainViewModel(calendarTaskViewModel, fourQuadrantTaskViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun App(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomNavItem(
                    route = "ToDoCalendar",
                    icon = Icons.Default.CalendarMonth,
                    label = "日历",
                    currentRoute = currentRoute,
                    navController = navController
                )
                BottomNavItem(
                    route = "FourQuadrant",
                    icon = Icons.Default.BorderAll,
                    label = "四象限",
                    currentRoute = currentRoute,
                    navController = navController
                )
            }
        },
        floatingActionButton = {
            AddTaskButton(
                currentRoute = currentRoute,
                onShowCalendarDialog = viewModel::showCalendarDialog,
                onShowQuadrantDialog = viewModel::showQuadrantDialog
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "ToDoCalendar",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("ToDoCalendar") {
                ToDoCalendar(
                    onDateFocused = viewModel::onDateFocused,
                    calendarTaskViewModel = viewModel.calendarTaskViewModel
                )
            }
            composable("FourQuadrant") {
                FourQuadrant(
                    onQuadrantFocused = viewModel::onQuadrantFocused,
                    fourQuadrantTaskViewModel = viewModel.fourQuadrantTaskViewModel
                )
            }
        }
    }

    if (viewModel.showToDoCalendarAddTaskDialog) {
        AddCalendarTaskDialog(
            focusedDate = viewModel.focusedDate,
            onConfirm = viewModel::addCalendarTask,
            onDismiss = viewModel::hideCalendarDialog
        )
    }

    if (viewModel.showFourQuadrantAddTaskDialog) {
        AddFourQuadrantTaskDialog(
            focusedQuadrant = viewModel.focusedQuadrant,
            onConfirm = viewModel::addQuadrantTask,
            onDismiss = viewModel::hideQuadrantDialog
        )
    }
}

@Composable
private fun RowScope.BottomNavItem(
    route: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    currentRoute: String?,
    navController: NavController,
) {
    NavigationBarItem(
        icon = { Icon(icon, contentDescription = null) },
        label = { Text(label) },
        selected = currentRoute == route,
        onClick = { navController.navigate(route) }
    )
}

@Composable
private fun AddTaskButton(
    currentRoute: String?,
    onShowCalendarDialog: () -> Unit,
    onShowQuadrantDialog: () -> Unit,
) {
    FloatingActionButton(
        shape = CircleShape,
        modifier = Modifier.padding(8.dp),
        onClick = {
            when (currentRoute) {
                "ToDoCalendar" -> onShowCalendarDialog()
                "FourQuadrant" -> onShowQuadrantDialog()
            }
        }
    ) {
        Icon(Icons.Default.Add, contentDescription = null)
    }
}





