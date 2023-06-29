package sg.edu.np.mad.madasgcacheflash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LearnYourself extends AppCompatActivity {

    final String TITLE = "Flash Card Questions";
    private List<String> questions;

    private List<String> answers;
    private int currentQuestionIndex;
    // Declare a boolean variable to track the state of each card
    private boolean isQuestionShowing = true;
    private MotionLayout motionLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.learn_yourself);
        Button btnBackToHome = findViewById(R.id.btnBackToHome);
        updateTextViewClickListener();
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate back to the home activity
                Intent intent = new Intent(LearnYourself.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView textView = findViewById(R.id.textView);
        Button buttonNext = findViewById(R.id.button2);
        Button buttonPrev = findViewById(R.id.button);
        // Get the flashcard from the intent
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        TextView titleTextView = findViewById(R.id.titleTextView);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("flashcard")) {
            Flashcard flashcard = intent.getParcelableExtra("flashcard");

            // Retrieve the questions from the flashcard object
            questions = flashcard.getQuestions();
            answers = flashcard.getAnswers();

            // Shuffle the questions and answers lists together
            List<String> combinedList = new ArrayList<>();
            for (int i = 0; i < questions.size(); i++) {
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

            titleTextView.setText(flashcard.getTitle());



            // Initialize the current question index
            currentQuestionIndex = 0;

            // Display the first question
            textView.setText(questions.get(currentQuestionIndex));
        }

        motionLayout = findViewById(R.id.motionLayout);

        motionLayout.transitionToEnd();


        buttonPrev.setEnabled(false);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to the previous question
                Log.w("main activity", String.valueOf(currentQuestionIndex));
                currentQuestionIndex--;

                // Check if the current question index is within the bounds of the questions list
                if (currentQuestionIndex < 0) {
                    // If it's an invalid index, set it to the last question
                    currentQuestionIndex = 0;
                }

                // Update the text with the previous question
                textView.setText(questions.get(currentQuestionIndex));

                isQuestionShowing = true;

                // Apply the slide in animation from right to left to the textView
                Animation slideInAnimation = AnimationUtils.loadAnimation(LearnYourself.this, R.anim.slide_right);
                textView.startAnimation(slideInAnimation);
                // Stop the animation at the first question
                if (currentQuestionIndex == 0) {
                    slideInAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // Set the visibility of next button to INVISIBLE
                            buttonPrev.setEnabled(false);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
                else {
                    slideInAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            // Set the visibility of next button to INVISIBLE
                            buttonPrev.setEnabled(true);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }

                // Set the visibility of next button to VISIBLE
                buttonNext.setEnabled(true);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to the next question
                currentQuestionIndex++;

                // Check if the current question index is within the bounds of the questions list
                if (currentQuestionIndex >= questions.size()) {
                    // If it's an invalid index, set it to the last question
                    currentQuestionIndex = questions.size() - 1;
                }

                // Update the text with the next question
                textView.setText(questions.get(currentQuestionIndex));

                isQuestionShowing = true;

                // Apply the slide in animation from left to right to the textView
                Animation slideInAnimation = AnimationUtils.loadAnimation(LearnYourself.this, R.anim.slide_left);
                textView.startAnimation(slideInAnimation);
                // Stop the animation at the last question
                if (currentQuestionIndex == questions.size() - 1) {
                    slideInAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // Set the visibility of next button to INVISIBLE
                            buttonNext.setEnabled(false);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
                else{
                    slideInAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // Set the visibility of previous button to VISIBLE
                            buttonPrev.setEnabled(true);
                            Log.i("Hi","visible");
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });

                }

            }
        });


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentText = textView.getText().toString();
                String newText = isQuestionShowing ? getAnswerForQuestion(currentText) : getQuestionForAnswer(currentText);

                if (newText != null) {
                    // Toggle the state of isQuestionShowing
                    isQuestionShowing = !isQuestionShowing;

                    // Apply the rotation animation
                    applyRotationAnimation(textView);

                    // Set the new text in the TextView
                    textView.setText(newText);
                }
            }
        });

    }
    private String getAnswerForQuestion(String question) {
        if (questions.contains(question)) {
            int index = questions.indexOf(question);
            return answers.get(index);
        }
        return null;
    }

    private String getQuestionForAnswer(String answer) {
        int index = 0;
        if (answers.contains(answer)) {
            index = answers.indexOf(answer);
            return questions.get(index);
        }
        else {
            if (answers.get(index).equals(answer)) {
                return questions.get(index);
            }
        }
        return null;
    }
    private void applyRotationAnimation(final View view) {
        ObjectAnimator rotationY = ObjectAnimator.ofFloat(view, "rotationY", 0f, 360f);
        rotationY.setDuration(500);
        rotationY.setInterpolator(new AccelerateDecelerateInterpolator());
        rotationY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                // Animation start
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // Animation end
                // Show the answer content
                // ...
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Animation cancel
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                // Animation repeat
            }
        });
        rotationY.start();
    }
    private void updateTextViewClickListener() {
        TextView textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentText = textView.getText().toString();
                String newText = isQuestionShowing ? getAnswerForQuestion(currentText) : getQuestionForAnswer(currentText);

                if (newText != null) {
                    // Toggle the state of isQuestionShowing
                    isQuestionShowing = !isQuestionShowing;

                    // Apply the rotation animation
                    applyRotationAnimation(textView);

                    // Set the new text in the TextView
                    textView.setText(newText);
                }
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TITLE, "On Start!");
    }


    @Override
    protected void onStop(){
        super.onStop();
        Log.v(TITLE,  "On Stop");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.v(TITLE, "On pause");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.v(TITLE, "On Destroy");
    }
}