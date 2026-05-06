package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

import org.example.project.quiz.QuizGameState
import org.example.project.ui.CheatScreen
import org.example.project.ui.QuizScreen


@Composable
@Preview
fun App() {
    // Создаём состояние игры один раз
    val gameState = remember { QuizGameState() }

    // Флаг: показывать ли экран читера
    var showCheat by remember { mutableStateOf(false) }

    // Тема Material 3 (можно настроить под себя)
    MaterialTheme {
        if (showCheat) {
            CheatScreen(
                answerIsTrue = gameState.currentQuestion.answer,
                onBack = {
                    // Помечаем, что игрок сжульничал для текущего вопроса
                    gameState.isCheater = true
                    // Возвращаемся к квизу
                    showCheat = false
                }
            )
        } else {
            QuizScreen(
                state = gameState,
                onShowCheat = {
                    showCheat = true
                }
            )
        }
    }
}