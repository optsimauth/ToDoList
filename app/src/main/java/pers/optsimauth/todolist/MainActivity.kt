package pers.optsimauth.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Note
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pers.optsimauth.todolist.ui.component.dialog.AddCalendarTaskDialog
import pers.optsimauth.todolist.ui.component.dialog.AddFourQuadrantTaskDialog
import pers.optsimauth.todolist.ui.screen.FourQuadrant
import pers.optsimauth.todolist.ui.screen.Note
import pers.optsimauth.todolist.ui.screen.NoteContentEdit
import pers.optsimauth.todolist.ui.screen.ToDoCalendar
import pers.optsimauth.todolist.ui.theme.ToDoListTheme
import pers.optsimauth.todolist.viewmodel.ContextHolder
import pers.optsimauth.todolist.viewmodel.MainViewModel
import pers.optsimauth.todolist.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ContextHolder.initialize(this)
        viewModel.initializeAndCleanup(this)



        setContent {
            ToDoListTheme {
                App(viewModel)


            }
        }
    }


}

@Composable
fun App(viewModel: MainViewModel) {

    val navController = rememberNavController()

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(innerPadding)
        ) {
            // 主导航图，包含底部导航栏的页面
            composable(RouteManager.Main) {
                MainScreen(
                    viewModel = viewModel,
                    onNavigateToNoteEdit = {
                        navController.navigate(RouteManager.NoteEdit)
                    }
                )
            }

            // 笔记编辑页面，独立于主导航
            composable(RouteManager.NoteEdit) {
                NoteContentEdit(
                    noteEntity = viewModel.noteEntity,
                    onSave = { newNoteEntity ->
                        viewModel.saveNote(newNoteEntity)

                        navController.popBackStack()
                        viewModel.setNoteEntity(null)
                    },
                    onDelete = {
                        viewModel.noteViewModel.delete(viewModel.noteEntity!!)
                        navController.popBackStack()
                        viewModel.setNoteEntity(null)
                    },
                    onBack = {
                        navController.popBackStack()
                        viewModel.setNoteEntity(null)
                    }
                )
            }
        }
    }

    // 显示对话框
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
fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToNoteEdit: () -> Unit
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route


    var noteClickCount by remember { mutableIntStateOf(0) }
    var lastClickTime by remember { mutableLongStateOf(0L) }

    var fourQuadrantClickCount by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == RouteManager.ToDoCalendar,
                    onClick = { navController.navigate(RouteManager.ToDoCalendar) },
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                    label = { Text("日历") }
                )

                NavigationBarItem(
                    selected = currentRoute == RouteManager.FourQuadrant,
                    onClick = {
                        navController.navigate(RouteManager.FourQuadrant)
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastClickTime > 1000) {
                            fourQuadrantClickCount = 1
                        } else {
                            fourQuadrantClickCount++
                        }
                        lastClickTime = currentTime

                        if (fourQuadrantClickCount >= 5) {
                            fourQuadrantClickCount = 0
                            viewModel.launchFilePicker()


                        }

                    },
                    icon = { Icon(Icons.Default.BorderAll, contentDescription = null) },
                    label = { Text("四象限") }
                )

                NavigationBarItem(
                    selected = currentRoute == RouteManager.Note,
                    onClick = {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastClickTime > 1000) {
                            noteClickCount = 1
                        } else {
                            noteClickCount++
                        }
                        lastClickTime = currentTime

                        if (noteClickCount >= 5) {
                            noteClickCount = 0
//                            DatabaseUtils.importDatabase(ContextHolder.appContext)
                        }
                        navController.navigate(RouteManager.Note)
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.Note, contentDescription = null) },
                    label = { Text("笔记") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                modifier = Modifier.padding(8.dp),
                onClick = {
                    when (currentRoute) {
                        RouteManager.ToDoCalendar -> {
                            viewModel.showCalendarDialog()
                        }

                        RouteManager.FourQuadrant -> {
                            viewModel.showQuadrantDialog()
                        }

                        RouteManager.Note -> {
                            onNavigateToNoteEdit()
                        }
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RouteManager.ToDoCalendar,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(RouteManager.ToDoCalendar) {
                ToDoCalendar(
                    onDateFocused = viewModel::onDateFocused,
                    calendarTaskViewModel = viewModel.calendarTaskViewModel
                )
            }
            composable(RouteManager.FourQuadrant) {
                FourQuadrant(
                    onQuadrantFocused = viewModel::onQuadrantFocused,
                    fourQuadrantTaskViewModel = viewModel.fourQuadrantTaskViewModel
                )
            }
            composable(RouteManager.Note) {
                Note(
                    noteViewModel = viewModel.noteViewModel,
                    onNoteItemClick = { noteEntity ->
                        viewModel.setNoteEntity(noteEntity)
                        onNavigateToNoteEdit()
                    },
                    onNoteItemDelete = {},
                )
            }
        }
    }
}

object RouteManager {
    const val ToDoCalendar = "ToDoCalendar"
    const val FourQuadrant = "FourQuadrant"
    const val Note = "Note"
    const val NoteEdit = "NoteEdit"
    const val Main = "Main"
}





