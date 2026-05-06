package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import labmobiledev.composeapp.generated.resources.*
import org.example.project.quiz.QuizGameState
import org.example.project.ui.QuizScreen
import org.example.project.ui.CheatScreen
import org.example.project.ui.AboutScreen
import org.example.project.ui.AppTheme
import org.jetbrains.compose.resources.stringResource

enum class Screen { Quiz, About }

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun App() {
    AppTheme {
        // Сохраняемые переменные для отдельных частей состояния
        var savedIndex by rememberSaveable { mutableIntStateOf(0) }
        var savedBlocked by rememberSaveable { mutableStateOf("") }
        var savedCorrect by rememberSaveable { mutableStateOf("") }
        var savedCheater by rememberSaveable { mutableStateOf(false) }

        // Игровое состояние
        val gameState = remember {
            QuizGameState().apply {
                currentIndex = savedIndex
                isCheater = savedCheater
                if (savedBlocked.isNotEmpty()) {
                    blockedQuestions = savedBlocked.split(",").map { it.toInt() }.toMutableSet()
                }
                if (savedCorrect.isNotEmpty()) {
                    correctAnswers = savedCorrect.split(",").map { it.toInt() }.toMutableSet()
                }
            }
        }

        // Синхронизация состояния в saved
        LaunchedEffect(gameState.currentIndex, gameState.blockedQuestions,
            gameState.correctAnswers, gameState.isCheater) {
            savedIndex = gameState.currentIndex
            savedBlocked = gameState.blockedQuestions.joinToString(",")
            savedCorrect = gameState.correctAnswers.joinToString(",")
            savedCheater = gameState.isCheater
        }

        var showCheat by remember { mutableStateOf(false) }
        var currentScreen by remember { mutableStateOf(Screen.Quiz) }
        val snackbarHostState = remember { SnackbarHostState() }

        // Адаптивность через BoxWithConstraints
        BoxWithConstraints {
            val isWideScreen = maxWidth >= 600.dp

            if (showCheat) {
                if (isWideScreen) {
                    Row {
                        QuizScreen(
                            state = gameState,
                            onShowCheat = { },
                            snackbarHostState = snackbarHostState,
                            modifier = Modifier.weight(1f),
                            cheatEnabled = false
                        )
                        CheatScreen(
                            answerIsTrue = gameState.currentQuestion.answer,
                            onBack = {
                                gameState.isCheater = true
                                showCheat = false
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    CheatScreen(
                        answerIsTrue = gameState.currentQuestion.answer,
                        onBack = {
                            gameState.isCheater = true
                            showCheat = false
                        }
                    )
                }
            } else {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(Res.string.app_name)) }
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = currentScreen == Screen.Quiz,
                                onClick = { currentScreen = Screen.Quiz },
                                icon = { },
                                label = { Text(stringResource(Res.string.nav_quiz)) }
                            )
                            NavigationBarItem(
                                selected = currentScreen == Screen.About,
                                onClick = { currentScreen = Screen.About },
                                icon = { },
                                label = { Text(stringResource(Res.string.nav_about)) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            Screen.Quiz -> QuizScreen(
                                state = gameState,
                                onShowCheat = { showCheat = true },
                                snackbarHostState = snackbarHostState
                            )
                            Screen.About -> AboutScreen()
                        }
                    }
                }
            }
        }
    }
}