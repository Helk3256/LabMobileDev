package org.example.project.model

import labmobiledev.composeapp.generated.resources.Res
import labmobiledev.composeapp.generated.resources.question_africa
import labmobiledev.composeapp.generated.resources.question_americas
import labmobiledev.composeapp.generated.resources.question_asia
import labmobiledev.composeapp.generated.resources.question_australia
import labmobiledev.composeapp.generated.resources.question_mideast
import labmobiledev.composeapp.generated.resources.question_oceans
import org.example.project.model.Question

object QuizBank {
    val questions = listOf(
        Question(Res.string.question_australia, true),
        Question(Res.string.question_oceans, true),
        Question(Res.string.question_mideast, false),
        Question(Res.string.question_africa, false),
        Question(Res.string.question_americas, true),
        Question(Res.string.question_asia, true)
    )
}