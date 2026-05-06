package org.example.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import labmobiledev.composeapp.generated.resources.Res
import labmobiledev.composeapp.generated.resources.arrow_left
import labmobiledev.composeapp.generated.resources.arrow_right
import labmobiledev.composeapp.generated.resources.back_button
import labmobiledev.composeapp.generated.resources.cheat_button
import labmobiledev.composeapp.generated.resources.correct_toast
import labmobiledev.composeapp.generated.resources.false_button
import labmobiledev.composeapp.generated.resources.incorrect_toast
import labmobiledev.composeapp.generated.resources.judgment_toast
import labmobiledev.composeapp.generated.resources.next_button
import labmobiledev.composeapp.generated.resources.true_button
import org.example.project.quiz.QuizGameState
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun QuizScreen(
    state: QuizGameState,
    onShowCheat: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Ключ сообщения, которое нужно показать (устанавливается при клике на кнопку)
    var toastKey by remember { mutableStateOf<String?>(null) }

    // Преобразуем ключ в реальную строку, используя stringResource в composable-контексте
    val toastMessage = toastKey?.let { key ->
        when (key) {
            "correct_toast" -> stringResource(Res.string.correct_toast)
            "incorrect_toast" -> stringResource(Res.string.incorrect_toast)
            "judgment_toast" -> stringResource(Res.string.judgment_toast)
            else -> ""
        }
    }


    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            toastKey = null
        }
    }

    LaunchedEffect(state.blockedQuestions.size) {
        if (state.blockedQuestions.size == state.questionCount && state.questionCount > 0) {
            val percent = state.getCorrectPercent()
            snackbarHostState.showSnackbar("You got $percent% correct!")
            state.resetStatistics()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(state.currentQuestion.textResId),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(24.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Button(
                    onClick = {
                        val key = state.checkAnswer(true)
                        if (key != null) toastKey = key
                    },
                    enabled = !state.isCurrentBlocked
                ) {
                    Text(stringResource(Res.string.true_button))
                }
                Button(
                    onClick = {
                        val key = state.checkAnswer(false)
                        if (key != null) toastKey = key
                    },
                    enabled = !state.isCurrentBlocked
                ) {
                    Text(stringResource(Res.string.false_button))
                }
            }
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = state::moveToPrevious) {
                    Icon(
                        painter = painterResource(Res.drawable.arrow_left),
                        contentDescription = stringResource(Res.string.back_button)
                    )
                }
                Button(onClick = onShowCheat) {
                    Text(stringResource(Res.string.cheat_button))
                }
                IconButton(onClick = state::moveToNext) {
                    Icon(
                        painter = painterResource(Res.drawable.arrow_right),
                        contentDescription = stringResource(Res.string.next_button)
                    )
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}