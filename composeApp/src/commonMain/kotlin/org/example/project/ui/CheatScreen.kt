package org.example.project.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import labmobiledev.composeapp.generated.resources.Res
import labmobiledev.composeapp.generated.resources.false_button
import labmobiledev.composeapp.generated.resources.show_answer_button
import labmobiledev.composeapp.generated.resources.true_button
import labmobiledev.composeapp.generated.resources.warning_text
import org.jetbrains.compose.resources.stringResource

@Composable
fun CheatScreen(
    answerIsTrue: Boolean,
    onBack: () -> Unit
) {
    var answerShown by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.warning_text),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(24.dp)
            )
            if (answerShown) {
                Text(
                    text = if (answerIsTrue) stringResource(Res.string.true_button)
                    else stringResource(Res.string.false_button),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(24.dp)
                )
            } else {
                Spacer(Modifier.height(48.dp))
            }
            Button(onClick = {
                answerShown = true
            }) {
                Text(stringResource(Res.string.show_answer_button))
            }
            if (answerShown) {
                Button(onClick = onBack) {
                    Text("Back to Quiz")
                }
            }
        }
    }
}