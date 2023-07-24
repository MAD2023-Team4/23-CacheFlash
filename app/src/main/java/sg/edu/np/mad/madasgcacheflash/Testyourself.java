package sg.edu.np.mad.madasgcacheflash;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;



import android.content.DialogInterface;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class Testyourself extends AppCompatActivity {

    int score = 0;
    final String TITLE = "Testyourself";
    int currentIndex = 0;
    List<String> questions = new ArrayList<>();
    List<String> answers = new ArrayList<>();
    List<Integer> answeredQuestions = new ArrayList<>(); // New list to track answered questions
    boolean isAnswered = false;

    String username;
    Flashcard flashcard;

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private List<ImageView> imageViews;

    private int animationDuration = 100;
    private int pauseDuration = 500; // Adjust the pause duration here
    private int roundCount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_testyourself);
        ConstraintLayout mainLayout = findViewById(R.id.activity_testyourself);
        View shufflingCardLayout = getLayoutInflater().inflate(R.layout.activity_shuffle_card, mainLayout, false);
        mainLayout.addView(shufflingCardLayout);

        // Initialize the ImageView variables using the IDs from the shuffling card layout
        imageView1 = shufflingCardLayout.findViewById(R.id.one);
        imageView2 = shufflingCardLayout.findViewById(R.id.two);
        imageView3 = shufflingCardLayout.findViewById(R.id.three);

        imageViews = new ArrayList<>();
        imageViews.add(imageView1);
        imageViews.add(imageView2);
        imageViews.add(imageView3);

        startShufflingAnimation(shufflingCardLayout,mainLayout);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("flashcard")
                && intent.hasExtra("Username")) {
            flashcard = intent.getParcelableExtra("flashcard");
            username = intent.getStringExtra("Username");
            Log.v("Test Yourself",username);

            // Retrieve the questions from the flashcard object
            questions = flashcard.getQuestions();
            answers = flashcard.getAnswers();
            // Shuffle the questions and answers lists together
            List<String> combinedList = new ArrayList<>();
            for(int i = 0; i < questions.size(); i++){
                combinedList.add(questions.get(i) + "|" + answers.get(i));
            }
            Collections.shuffle(combinedList);
            // Clear the original questions and answers lists
            questions.clear();
            answers.clear();

            // Separate the shuffled combined list into questions and answers lists
            for (String item : combinedList) {
                String[] parts = item.split("\\|");
                questions.add(parts[0]);
                answers.add(parts[1]);
            }

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
                        score++;
                        Log.v("Score", String.valueOf(score));
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
                    currentIndex++;
                    if(currentIndex == flashcard.getQuestions().size()){
                        //make a toast message of the score
                        double percentage = (double) score / flashcard.getQuestions().size() * 100.0;

                        Log.v("Percentage", String.valueOf(flashcard.getQuestions().size()));
                        //updatePercentage(username, percentage);
                        Log.v("Quiz Finished", String.valueOf(percentage));
                        showAlert("Quiz Finished", "Your score: " + percentage
                                + "%", percentage, flashcard.getQuestions().size());
                        updatePercentage(username, percentage, flashcard);
                    }
                    // Update the percentage of the flashcard in the Firebase Realtime Database


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

    private void startShufflingAnimation(View shufflingCardLayout, ConstraintLayout mainLayout) {
        final int totalImages = imageViews.size();

        List<Animator> animatorList = new ArrayList<>();

        // Create the animation for each image
        for (int i = 0; i < totalImages; i++) {
            ImageView currentImage = imageViews.get(i);
            ImageView nextImage = imageViews.get((i + 1) % totalImages);

            // Calculate the translation distance based on the image width
            int translationDistance = nextImage.getLeft() - currentImage.getLeft();

            // Create ObjectAnimator for translationX property
            ObjectAnimator animX = ObjectAnimator.ofFloat(currentImage, "translationX", 0, translationDistance);
            animX.setDuration(animationDuration);

            // Add the animator to the list
            animatorList.add(animX);
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Swap positions of imageViews in the list
                ImageView firstImage = imageViews.get(0);
                imageViews.remove(0);
                imageViews.add(firstImage);

                // Update the layout params to reflect the new positions
                for (int i = 0; i < totalImages; i++) {
                    ImageView imageView = imageViews.get(i);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();

                    if (i == 0) {
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    } else if (i == 1) {
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    } else if (i == 2) {
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    }

                    imageView.setTranslationX(0);
                }

                roundCount++;

                // Start displaying the questions after one round
                if (roundCount == totalImages) {
                    currentIndex = 0; // Reset the currentIndex
                    mainLayout.removeView(shufflingCardLayout);
                    displayQuestion(); // Call displayQuestion() after a delay

                } else {
                    // Start the next shuffling animation after a delay
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startShufflingAnimation(shufflingCardLayout,mainLayout);
                        }
                    }, pauseDuration);
                }
            }
        });

        animatorSet.start();
    }

    private void displayQuestion() {
        Log.v("displayQuestion", "Displaying question");
        TextView qcard = findViewById(R.id.QCard);
        EditText input = findViewById(R.id.Userinput);
        Button submit = findViewById(R.id.button4);
        Button next = findViewById(R.id.button2);

        qcard.setText(questions.get(currentIndex));
        input.setText(""); // Clear the input field
        input.setEnabled(true); // Enable the input field

        submit.setEnabled(true); // Enable the submit button
        next.setEnabled(false); // Disable the next button

        isAnswered = false; // Reset the answered flag
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

    private void showAlert(String title, String text, double percentage, int total) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(text)
                .setPositiveButton("Back to home", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do something when the "OK" button is clicked
                        //Intent intent = new Intent(Testyourself.this, MainActivity.class);
                        //intent.putExtra("Flashcard", flashcard);
                        //intent.putExtra("Score", percentage);
                        //intent.putExtra("Total", total);
                        //startActivity(intent);
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do something when the "Cancel" button is clicked
                    }
                })
                .show();
    }

    private void updatePercentage(String username, double percentage, Flashcard f2){
        if (username == null) {
            Log.e("UpdatePercentage", "Username is null. Cannot proceed.");
            return;
        }
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(username)
                .child("categories");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    for (DataSnapshot flashcardSnapshot : categorySnapshot.getChildren()) {
                        Flashcard f = flashcardSnapshot.getValue(Flashcard.class);
                        if (f.getTitle().equals(f2.getTitle())) {
                            DatabaseReference flashcardRef = flashcardSnapshot.child("percentage").getRef();
                            flashcardRef.setValue(percentage);
                            break; // Found the flashcard, exit the loop
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }


}