package org.example.project.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import labmobiledev.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun CheatScreen(
    answerIsTrue: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var answerShown by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
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
            Button(onClick = { showDialog = true }) {
                Text(stringResource(Res.string.show_answer_button))
            }
            if (answerShown) {
                Button(onClick = onBack) {
                    Text("Back to Quiz")
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(Res.string.cheat_dialog_title)) },
            text = { Text(stringResource(Res.string.cheat_dialog_message)) },
            confirmButton = {
                TextButton(onClick = {
                    answerShown = true
                    showDialog = false
                }) {
                    Text(stringResource(Res.string.cheat_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(Res.string.cheat_dialog_dismiss))
                }
            }
        )
    }
}