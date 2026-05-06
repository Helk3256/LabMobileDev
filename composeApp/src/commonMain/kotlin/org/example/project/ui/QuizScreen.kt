package org.example.project.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import labmobiledev.composeapp.generated.resources.*
import org.example.project.quiz.QuizGameState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun QuizScreen(
    state: QuizGameState,
    onShowCheat: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    cheatEnabled: Boolean = true
) {
    var toastKey by remember { mutableStateOf<String?>(null) }

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

    Box(modifier = modifier.fillMaxSize()) {
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
                Button(
                    onClick = onShowCheat,
                    enabled = cheatEnabled
                ) {
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
    }
}