package com.beta.quizify

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Score : AppCompatActivity() {

    private lateinit var userNameTextView: TextView
    private lateinit var userScoreTextView: TextView
    private lateinit var highScoreTextView: TextView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        // Initialize TextViews
        userNameTextView = findViewById(R.id.userNameTextView)
        userScoreTextView = findViewById(R.id.userScoreTextView)
        highScoreTextView = findViewById(R.id.highScoreTextView)

        // Fetch and display user scores
        fetchUserScores()
    }

    private fun fetchUserScores() {
        val userRef = db.collection("User").document("User Information")

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userName = document.getString("User Name") ?: "Unknown User"
                    val userScore = document.getLong("Score")?.toInt() ?: 0

                    // Update UI with user's current score
                    userNameTextView.text = "Name: $userName"
                    userScoreTextView.text = "Current Score: $userScore"

                    // Fetch high scores (you might want to expand this later)
                    fetchHighScores(userScore)
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                userNameTextView.text = "Error: ${exception.localizedMessage}"
            }
    }

    private fun fetchHighScores(currentUserScore: Int) {
        // This is a placeholder for high score logic
        // In a real app, you'd fetch this from Firestore or a leaderboard
        val highScore = currentUserScore // For now, just use current user's score
        highScoreTextView.text = "Highest Score: $highScore"
    }
}