package pers.optsimauth.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BorderAll
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
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

//        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RouteManager.Main,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { activityEnterTransition() },
            exitTransition = { activityExitTransition() },
            popEnterTransition = { activityPopEnterTransition() },
            popExitTransition = { activityPopExitTransition() }
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
                    onDelete = {
                        viewModel.noteViewModel.delete(viewModel.noteEntity!!)
                        navController.popBackStack()
                        viewModel.setNoteEntity(null)
                    },
                    onBack = { newNoteEntity ->
                        if (newNoteEntity.title.isNotEmpty() || newNoteEntity.content.isNotEmpty()) {
                            viewModel.saveNote(newNoteEntity)
                        }

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

data class PageItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToNoteEdit: () -> Unit
) {


    val pages = listOf(
        PageItem(RouteManager.ToDoCalendar, "日历", Icons.Default.CalendarMonth),
        PageItem(RouteManager.FourQuadrant, "四象限", Icons.Default.BorderAll),
        PageItem(RouteManager.Note, "笔记", Icons.Default.NoteAlt)
    )
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    var currentRoute = remember { RouteManager.ToDoCalendar }

    Scaffold(
//        contentWindowInsets = WindowInsets(0, 0, 0, 0),

        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            ) {
                pages.forEachIndexed { index, pageItem ->
                    NavigationBarItem(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index, animationSpec = tween(400))
                            }
                        },
                        icon = { Icon(pageItem.icon, contentDescription = pageItem.title) },
                        label = { Text(pageItem.title) }
                    )
                }
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
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            userScrollEnabled = false
        ) { page ->
            currentRoute = pages[page].route
            when (pages[page].route) {
                RouteManager.ToDoCalendar -> {
                    ToDoCalendar(
                        onDateFocused = viewModel::onDateFocused,
                        calendarTaskViewModel = viewModel.calendarTaskViewModel,
                        onWeekButtonFiveClick = viewModel::launchFilePicker,
                    )
                }

                RouteManager.FourQuadrant -> {
                    FourQuadrant(
                        onQuadrantFocused = viewModel::onQuadrantFocused,
                        fourQuadrantTaskViewModel = viewModel.fourQuadrantTaskViewModel
                    )
                }

                RouteManager.Note -> {
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
}


object RouteManager {
    const val ToDoCalendar = "ToDoCalendar"
    const val FourQuadrant = "FourQuadrant"
    const val Note = "Note"
    const val NoteEdit = "NoteEdit"
    const val Main = "Main"
}


private const val DEFAULT_ENTER_DURATION = 300
private const val DEFAULT_EXIT_DURATION = 220


private fun AnimatedContentTransitionScope<NavBackStackEntry>.activityEnterTransition(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Start,
        animationSpec = tween(DEFAULT_ENTER_DURATION, easing = LinearOutSlowInEasing),
        initialOffset = { it }
    )
}

@Suppress("UnusedReceiverParameter")
private fun AnimatedContentTransitionScope<NavBackStackEntry>.activityExitTransition(): ExitTransition {
    return scaleOut(
        animationSpec = tween(DEFAULT_ENTER_DURATION),
        targetScale = 0.96F
    )
}


@Suppress("UnusedReceiverParameter")
private fun AnimatedContentTransitionScope<NavBackStackEntry>.activityPopEnterTransition(): EnterTransition {
    return scaleIn(
        animationSpec = tween(DEFAULT_EXIT_DURATION),
        initialScale = 0.96F
    )
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.activityPopExitTransition(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.End,
        animationSpec = tween(DEFAULT_EXIT_DURATION, easing = FastOutLinearInEasing),
        targetOffset = { it }
    )
}