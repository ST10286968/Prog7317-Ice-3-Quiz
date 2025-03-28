package com.beta.quizify

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var startQuizButton: Button
    private lateinit var viewScoresButton: Button
    private lateinit var nameEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        startQuizButton = findViewById(R.id.startQuizButton)
        viewScoresButton = findViewById(R.id.viewScoresButton)
        nameEditText = findViewById(R.id.editTextText)

        // Set up click listeners within onCreate
        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Start Quiz when clicked
        startQuizButton.setOnClickListener {
            // Optional: Save user name before starting quiz
            val userName = nameEditText.text.toString().trim()
            if (userName.isNotEmpty()) {
                updateUserName(userName)
            }

            val intent = Intent(this, Quiz::class.java)
            startActivity(intent)
        }

        // View scores when clicked
        viewScoresButton.setOnClickListener {
            val intent = Intent(this, Score::class.java)
            startActivity(intent)
        }
    }

    // Method for XML onClick attribute
    fun startQuiz(view: View) {
        val userName = nameEditText.text.toString().trim()
        if (userName.isNotEmpty()) {
            updateUserName(userName)
        }

        val intent = Intent(this, Quiz::class.java)
        startActivity(intent)
    }

    // Method for XML onClick attribute
    fun viewScores(view: View) {
        val intent = Intent(this, Score::class.java)
        startActivity(intent)
    }

    private fun updateUserName(userName: String) {
        val userRef = db.collection("User").document("User Information")
        userRef.update("User Name", userName)
            .addOnSuccessListener { println("User Name updated successfully") }
            .addOnFailureListener { e -> println("Error: ${e.localizedMessage}") }
    }

    private fun updateUserScore(newScore: Int) {
        val userRef = db.collection("User").document("User Information")
        userRef.update("Score", newScore)
            .addOnSuccessListener { println("Score updated successfully") }
            .addOnFailureListener { e -> println("Error: ${e.localizedMessage}") }
    }
}