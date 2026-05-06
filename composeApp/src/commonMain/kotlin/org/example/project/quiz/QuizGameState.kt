package org.example.project.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.Saver
import org.example.project.model.QuizBank

class QuizGameState {
    private val questions = QuizBank.questions
    val questionCount = questions.size

    var currentIndex by mutableIntStateOf(0)
    var isCheater by mutableStateOf(false)

    // Списки храним как Set<Int> в изменяемом состоянии – удобно для сохранения
    var blockedQuestions by mutableStateOf(setOf<Int>())
    var correctAnswers by mutableStateOf(setOf<Int>())

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
     * Проверяет ответ и возвращает строковый ключ сообщения ("correct_toast", "incorrect_toast", "judgment_toast")
     * или null, если вопрос уже отвечен.
     */
    fun checkAnswer(userAnswer: Boolean): String? {
        if (isCurrentBlocked) return null

        val resultKey = when {
            isCheater -> "judgment_toast"
            userAnswer == currentQuestion.answer -> {
                correctAnswers = correctAnswers + currentIndex
                "correct_toast"
            }
            else -> "incorrect_toast"
        }
        blockedQuestions = blockedQuestions + currentIndex
        isCheater = false
        return resultKey
    }

    fun getCorrectPercent(): Int {
        return (correctAnswers.size * 100) / questionCount
    }

    fun resetStatistics() {
        blockedQuestions = emptySet()
        correctAnswers = emptySet()
    }

    // ─── Saver для rememberSaveable ───
    companion object {
        val Saver: Saver<QuizGameState, List<Any>> = Saver(
            save = { state ->
                listOf(
                    state.currentIndex,
                    state.isCheater,
                    state.blockedQuestions.joinToString(","),   // сохраняем set как строку
                    state.correctAnswers.joinToString(",")
                )
            },
            restore = { saved ->
                QuizGameState().apply {
                    currentIndex = (saved[0] as Int)
                    isCheater = (saved[1] as Boolean)
                    blockedQuestions = if ((saved[2] as String).isNotEmpty())
                        (saved[2] as String).split(",").map { it.toInt() }.toSet()
                    else emptySet()
                    correctAnswers = if ((saved[3] as String).isNotEmpty())
                        (saved[3] as String).split(",").map { it.toInt() }.toSet()
                    else emptySet()
                }
            }
        )
    }
}