package sg.edu.np.mad.madasgcacheflash;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Testyourself extends AppCompatActivity {

    int score = 0;
    final String TITLE = "Testyourself";
    int currentIndex = 0;
    List<String> questions = new ArrayList<>();
    List<String> answers = new ArrayList<>();
    List<Integer> answeredQuestions = new ArrayList<>(); // New list to track answered questions
    boolean isAnswered = false;
    Flashcard flashcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_testyourself);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("flashcard")) {
            flashcard = intent.getParcelableExtra("flashcard");

            // Retrieve the questions from the flashcard object
            questions = flashcard.getQuestions();
            answers = flashcard.getAnswers();

            TextView Title = findViewById(R.id.ftitle);
            TextView qcard = findViewById(R.id.QCard);
            EditText input = findViewById(R.id.Userinput);
            Button back = findViewById(R.id.button3);
            Button prev = findViewById(R.id.button);
            Button next = findViewById(R.id.button2);
            Button submit = findViewById(R.id.button4);

            Title.setText(flashcard.getTitle());
            qcard.setText(questions.get(currentIndex));

            next.setEnabled(false);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String answer = input.getText().toString();
                    String correctAnswer = answers.get(currentIndex);

                    if (answer.equals(correctAnswer)) {
                        Toast.makeText(getApplicationContext(), answer + " is correct.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect. The correct answer is: " + correctAnswer, Toast.LENGTH_SHORT).show();
                        input.setText(correctAnswer); // Display the correct answer
                    }

                    input.setEnabled(false); // Disable the input field
                    submit.setEnabled(false); // Disable the submit button
                    next.setEnabled(true); // Enable the next button
                    isAnswered = true;

                    answeredQuestions.add(currentIndex); // Add the answered question index to the list
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentIndex--;
                    if (currentIndex < 0) {
                        currentIndex = 0;
                    }
                    qcard.setText(questions.get(currentIndex));
                    input.setText(answers.get(currentIndex)); // Display the correct answer for the previous question
                    input.setEnabled(false); // Disable the input field

                    submit.setEnabled(false); // Disable the submit button
                    next.setEnabled(true); // Enable the next button

                    isAnswered = false; // Reset the answered flag
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    score++;
                    currentIndex++;
                    if(currentIndex == flashcard.getQuestions().size()){
                        //make a toast message of the score
                        showAlert("Quiz Finished", "Your score: " + score, score, flashcard.getQuestions().size());
                    }
                    if (currentIndex >= questions.size()) {
                        currentIndex = questions.size() - 1;
                    }

                    if (answeredQuestions.contains(currentIndex)) {
                        input.setText(answers.get(currentIndex)); // Display the correct answer for the already answered question
                        input.setEnabled(false); // Disable the input field
                        submit.setEnabled(false); // Disable the submit button
                        next.setEnabled(true); // Enable the next button
                        isAnswered = true;
                    } else {
                        qcard.setText(questions.get(currentIndex));
                        input.setText(""); // Clear the input field
                        input.setEnabled(true); // Enable the input field

                        submit.setEnabled(true); // Enable the submit button
                        next.setEnabled(false); // Disable the next button

                        isAnswered = false; // Reset the answered flag
                    }

                    if (currentIndex == questions.size() - 1) {
                        next.setEnabled(false); // Disable the next button at the last question
                    }
                }
            });

            Log.v(TITLE, "On Create!");
        }
    }

    @Override
    protected void onStart () {
        super.onStart();
        Log.v(TITLE, "On Start!");
    }


    @Override
    protected void onResume () {
        super.onResume();
        Log.v(TITLE, "On Resume!");


    }

    @Override
    protected void onStop () {
        super.onStop();
        Log.v(TITLE, "On Stop");
    }

    @Override
    protected void onPause () {
        super.onPause();
        Log.v(TITLE, "On pause");
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        Log.v(TITLE, "On Destroy");
    }

    private void showAlert(String title, String text, int score, int total) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(text)
                .setPositiveButton("Dashboard", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do something when the "OK" button is clicked
                        Intent intent = new Intent(Testyourself.this, Dashboard.class);
                        intent.putExtra("flashcards", flashcard);
                        intent.putExtra("Score", score);
                        intent.putExtra("Total", total);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do something when the "Cancel" button is clicked
                    }
                })
                .show();
    }

}