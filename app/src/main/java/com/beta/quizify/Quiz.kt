package com.beta.quizify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Quiz : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var option1Button: Button
    private lateinit var option2Button: Button
    private lateinit var option3Button: Button
    private lateinit var option4Button: Button

    private var questionList: MutableList<Question> = mutableListOf()
    private var currentQuestionIndex = 0
    private var score = 0
    private var totalQuestions = 0

    // Determine the quiz category passed from previous activity
    private lateinit var quizCategory: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Initialize UI components
        questionTextView = findViewById(R.id.questionTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        option1Button = findViewById(R.id.option1Button)
        option2Button = findViewById(R.id.option2Button)
        option3Button = findViewById(R.id.option3Button)
        option4Button = findViewById(R.id.option4Button)

        // Get the quiz category (default to a category if not passed)
        quizCategory = intent.getStringExtra("Category") ?: "General"

        // Fetch questions from Firestore
        fetchQuestions()
    }

    data class Question(
        val question: String,
        val options: List<String>,
        val answer: String,
        val points: Int
    )

    private fun fetchQuestions() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Quizzes").document(quizCategory)
            .get()
            .addOnSuccessListener { document ->
                val questions = document.get("questions") as? List<Map<String, Any>> ?: listOf()
                
                questionList = questions.map { questionMap ->
                    Question(
                        question = questionMap["question"] as? String ?: "",
                        options = questionMap["options"] as? List<String> ?: listOf(),
                        answer = questionMap["answer"] as? String ?: "",
                        points = (questionMap["points"] as? Long)?.toInt() ?: 0
                    )
                }.toMutableList()

                totalQuestions = questionList.size
                displayQuestion()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error loading questions", Toast.LENGTH_SHORT).show()
            }
    }

    private fun displayQuestion() {
        if (currentQuestionIndex < questionList.size) {
            val currentQuestion = questionList[currentQuestionIndex]

            questionTextView.text = currentQuestion.question
            option1Button.text = currentQuestion.options[0]
            option2Button.text = currentQuestion.options[1]
            option3Button.text = currentQuestion.options[2]
            option4Button.text = currentQuestion.options[3]

            // Update score display
            scoreTextView.text = "Score: $score"

            // Set click listeners for options
            setOptionClickListeners()
        } else {
            scoreTextView.text = "Quiz Completed! Your score is: $score"
        }
    }

    private fun setOptionClickListeners() {
        val optionButtons = listOf(option1Button, option2Button, option3Button, option4Button)
        optionButtons.forEach { button ->
            button.setOnClickListener {
                checkAnswer((it as Button).text.toString())
            }
        }
    }

    private fun checkAnswer(selectedOption: String) {
        val currentQuestion = questionList[currentQuestionIndex]
        
        if (selectedOption == currentQuestion.answer) {
            score += currentQuestion.points
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Wrong! Correct answer was: ${currentQuestion.answer}", Toast.LENGTH_SHORT).show()
        }

        currentQuestionIndex++
        displayQuestion()
    }

    /*private fun showResults() {
        // Create an intent to pass the score to the results screen
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("TOTAL_SCORE", score)
            putExtra("TOTAL_QUESTIONS", totalQuestions)
            putExtra("CATEGORY", quizCategory)
        }
        startActivity(intent)
        finish()
    }*/
}