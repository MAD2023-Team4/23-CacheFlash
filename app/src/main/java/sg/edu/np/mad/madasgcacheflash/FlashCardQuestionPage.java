package sg.edu.np.mad.madasgcacheflash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import java.util.List;

public class FlashCardQuestionPage extends AppCompatActivity {

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
        setContentView(R.layout.activity_flash_card_question_page);
        Button btnBackToHome = findViewById(R.id.btnBackToHome);
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate back to the home activity
                onBackPressed();
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

                // Apply the slide in animation from right to left to the textView
                Animation slideInAnimation = AnimationUtils.loadAnimation(FlashCardQuestionPage.this, R.anim.slide_right);
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

                // Apply the slide in animation from left to right to the textView
                Animation slideInAnimation = AnimationUtils.loadAnimation(FlashCardQuestionPage.this, R.anim.slide_left);
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
            switch (index) {
                case 0:
                    return answers.get(0);
                case 1:
                    return answers.get(1);
                case 2:
                    return answers.get(2);
            }
        }
        return null;
    }

    private String getQuestionForAnswer(String answer) {
        if (answers.contains(answer)) {
            int index = answers.indexOf(answer);
            switch (index) {
                case 0:
                    return questions.get(0);
                case 1:
                    return questions.get(1);
                case 2:
                    return questions.get(2);
            }
        } else {
            if (answers.get(0).equals(answer)) {
                return questions.get(0);
            } else if (answers.get(1).equals(answer)) {
                return questions.get(1);
            } else if (answers.get(2).equals(answer)) {
                return questions.get(2);
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