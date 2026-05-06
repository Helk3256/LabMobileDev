package org.example.project.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import org.example.project.model.QuizBank

class QuizGameState {
    private val questions = QuizBank.questions
    val questionCount = questions.size

    var currentIndex by mutableIntStateOf(0)
        private set

    var isCheater by mutableStateOf(false)

    val blockedQuestions = mutableStateListOf<Int>()
    val correctAnswers = mutableStateListOf<Int>()

    val currentQuestion get() = questions[currentIndex]

    val isCurrentBlocked: Boolean
        get() = blockedQuestions.contains(currentIndex)

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionCount
        isCheater = false
    }

    fun moveToPrevious() {
        currentIndex = if (currentIndex == 0) questionCount - 1 else currentIndex - 1
        isCheater = false
    }

    /**
     * Проверяет ответ и возвращает ключ сообщения (например "correct_toast").
     * Если вопрос уже отвечен, возвращает null.
     */
    fun checkAnswer(userAnswer: Boolean): String? {
        if (isCurrentBlocked) return null

        val resultKey = when {
            isCheater -> "judgment_toast"
            userAnswer == currentQuestion.answer -> {
                correctAnswers.add(currentIndex)
                "correct_toast"
            }
            else -> "incorrect_toast"
        }
        blockedQuestions.add(currentIndex)
        isCheater = false
        return resultKey
    }

    fun getCorrectPercent(): Int {
        return (correctAnswers.size * 100) / questionCount
    }

    fun resetStatistics() {
        blockedQuestions.clear()
        correctAnswers.clear()
    }
}