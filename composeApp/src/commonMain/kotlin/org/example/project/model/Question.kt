package org.example.project.model

import org.jetbrains.compose.resources.StringResource

data class Question(
    val textResId: StringResource,    // путь к ресурсу строки, например "question_australia"
    val answer: Boolean
)