package sg.edu.np.mad.madasgcacheflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.core.content.ContextCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MCQuiz extends AppCompatActivity {
    final String TITLE = "TestyourselfMCQ";
    int currentIndex = 0;
    boolean hasFinished = false;
    int score=0;
    String username;
    Flashcard flashcard;
    List<String> questions = new ArrayList<>();
    List<String> answers = new ArrayList<>();
    List<String> answerscheck = new ArrayList<>();
    List<String> optionsList = new ArrayList<>();
    //private MotionLayout motionLayout;

    //boolean isAnswered = false;
    private int animationDuration = 100;
    private int pauseDuration = 500; // Adjust the pause duration here
    private int roundCount = 0;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private List<ImageView> imageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_mcquiz);
        ConstraintLayout mainLayout=findViewById(R.id.activity_mcq_quiz);
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
        if (intent != null && intent.hasExtra("flashcard") && intent.hasExtra("Username")) {
            Flashcard flashcard = intent.getParcelableExtra("flashcard");
            username = intent.getStringExtra("Username");

            // Retrieve the questions from the flashcard object
            questions = flashcard.getQuestions();
            answers = flashcard.getAnswers();

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
            answerscheck.addAll(answers);


            Log.v(TITLE, "hi" + answerscheck.size());
            TextView QuestionView = findViewById(R.id.QuestionViews);
            TextView Title = findViewById(R.id.FlashcardTitle);
            Button option1 = findViewById(R.id.option1);
            Button option2 = findViewById(R.id.option2);
            Button option3 = findViewById(R.id.option3);
            Button option4 = findViewById(R.id.option4);
            Button next = findViewById(R.id.button9);
            Title.setText(flashcard.getTitle());
            changequestion(QuestionView,next,option1,option2,option3,option4,answers,questions,answerscheck,optionsList);

            option1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getanswer(option1, option2, option3, option4, answerscheck, currentIndex, next);

                }

            });
            option2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getanswer(option2, option3, option4, option1, answerscheck, currentIndex, next);

                }
            });
            option3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getanswer(option3, option4, option1, option2, answerscheck, currentIndex, next);

                }
            });
            option4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getanswer(option4, option1, option2, option3, answerscheck, currentIndex, next);

                }
            });

            //--------------------------------------------------------------------------------------
            //When next button is pressed, and the user has finished answering all questions:
            //--------------------------------------------------------------------------------------
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentIndex++;
                    if(currentIndex == flashcard.getQuestions().size() && !hasFinished){
                        //make a toast message of the score
                        hasFinished = true;

                        double percentage = (double) score / flashcard.getQuestions().size() * 100.0;
                        int points = (int) percentage/20;
                        Log.v("Percentage", String.valueOf(flashcard.getQuestions().size()));
                        //updatePercentage(username, percentage);
                        Log.v("Quiz Finished", String.valueOf(percentage));

                        showAlert("You Finished on Time!", "Points earned: " + points,
                                percentage, flashcard.getQuestions().size());
                        //--------------------------------------------------------------------
                        //Disable the quiz, and the next button since it has ended
                        //--------------------------------------------------------------------
                        option1.setEnabled(false);
                        option2.setEnabled(false);
                        option3.setEnabled(false);
                        option4.setEnabled(false);
                        next.setEnabled(false);
                        Log.v("options disabled","options disable");

                        updatePercentage(username, percentage, flashcard);
                        updatePoints(username, points);


                        //Update the flashcard's score locally
                        // flashcard.setPercentage(percentage);
                        // Posting performance of the user into firebase

                    }
                    if (currentIndex >= questions.size()) {
                        currentIndex = questions.size() - 1;
                        //score
                    }
                    QuestionView.setText(questions.get(currentIndex));
                    changequestion(QuestionView,next,option1,option2,option3,option4,answers,questions,answerscheck,optionsList);


                    if (currentIndex == questions.size() - 1) {
                        next.setEnabled(false); // Disable the next button at the last question
                    }
                }
            });
        }
    }

    //--------------------------------------------------------------------------------------
    //Methods
    //--------------------------------------------------------------------------------------
    public void changequestion(TextView Qcard,Button next,Button option1,Button option2,Button option3,Button option4,List<String>AList,List<String>QList,List<String>SList,List<String>OList)
    {
        int blueColor = ContextCompat.getColor(this, R.color.stepIndicatorUnselectedColor);
        Qcard.setText(QList.get(currentIndex));
        option1.setEnabled(true);
        option2.setEnabled(true);
        option3.setEnabled(true);
        option4.setEnabled(true);
        option1.setBackgroundColor(blueColor);
        option2.setBackgroundColor(blueColor);
        option3.setBackgroundColor(blueColor);
        option4.setBackgroundColor(blueColor);
        Qcard.setText(QList.get(currentIndex));
        next.setEnabled(false);
        Random random = new Random();

        String coption = AList.get(currentIndex);
        OList.add(coption);
        AList.remove(coption);


        for (int i = 0; i < 3; i++) {
            //Do a checker if AList is empty, it will have a negative bound error
            if (AList.size() > 0) {
                int options = random.nextInt(AList.size());
                String option = AList.get(options);
                OList.add(option);
                AList.remove(option);
            }
            else {

            }

        }


        Log.v(TITLE, "Size: " + answers.size());
        Collections.shuffle(OList);
        for (int i = 0; i < optionsList.size(); i++) {
            String buttonText = optionsList.get(i);
            if (i == 0) {
                option1.setText(buttonText);
            } else if (i == 1) {
                option2.setText(buttonText);
            } else if (i == 2) {
                option3.setText(buttonText);
            } else if (i == 3) {
                option4.setText(buttonText);
            }
        }

        //Can consider to put it in OnDestroy
        AList.clear();
        AList.addAll(SList);
        OList.clear();

        Log.v(TITLE, "Size: " + answers.size());
    }


    //--------------------------------------------------------------------------------------
    //Check whether answer is correct
    //--------------------------------------------------------------------------------------
    public boolean checkAnswer(String correctAnswer, String guessedAnswer) {
        if (correctAnswer.equals(guessedAnswer)) {
            return true;
        }
        return false;
    }


    //--------------------------------------------------------------------------------------
    //Now, the getanswer method calls the checkanswer method, to see if it returns true,
    //set the background color to green, else red.
    //--------------------------------------------------------------------------------------
    public void getanswer(Button b,Button b2,Button b3,Button b4, List<String> AL,int i,Button next)
    {
        //Getting the integers of colors from the colors.xml file, into variables, then accessing them
        int greenColor = ContextCompat.getColor(this, R.color.green);
        int redColor = ContextCompat.getColor(this, R.color.lightRed);
        if(checkAnswer(AL.get(i),b.getText().toString()))
        {
            Log.v(TITLE,"here");
            b.setBackgroundColor(greenColor);
            score+=1;

        }
        else{
            Log.v(TITLE,"her");
            b.setBackgroundColor(redColor);
            if(checkAnswer(AL.get(i),b2.getText().toString()))
            {
                b2.setBackgroundColor(greenColor);
                Log.v("Back","Bg color set");
            }
            else if(checkAnswer(AL.get(i),b3.getText().toString()))
            {
                b3.setBackgroundColor(greenColor);
                Log.v("Back","Bg color set 2");
            }
            else if(checkAnswer(AL.get(i),b4.getText().toString()))
            {
                b4.setBackgroundColor(greenColor);
                Log.v("Back","Bg color set 3");
            }
        }

        b.setEnabled(false);
        b2.setEnabled(false);
        b3.setEnabled(false);
        b4.setEnabled(false);
        next.setEnabled(true);

    }

    private void showAlert(String title, String text, double percentage, int total) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(text)
                .setPositiveButton("Back to Home", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do something when the "OK" button is clicked
                        Intent intent = new Intent(MCQuiz.this, MainActivity.class);
                        intent.putExtra("flashcards", flashcard);
                        intent.putExtra("Username",username);
                        intent.putExtra("Score", percentage);
                        intent.putExtra("Total", total);
                        startActivity(intent);
                    }
                })
                .show();
    }

    //----------------------------------------------------------------------------------------------
    //Back-up feature: Update the performance of the flashcards as a whole (without Bryan's difficulty
    //levels, then using this to calculate the best recall time to recall the questions for flashcard.)
    //----------------------------------------------------------------------------------------------
    /*
    private void postPerformance(Flashcard flashcard, double percentage) {

        DatabaseReference flashcardsRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(username).child("flashcards");

        Query query = flashcardsRef.orderByChild("title").equalTo(flashcard.getTitle());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String flashcardKey = snapshot.getKey();
                        DatabaseReference flashcardRef = flashcardsRef.child(flashcardKey);
                        //then, update the percentage accordingly
                        flashcardRef.child("percentage").setValue(calculatePercentage(score, flashcard.getQuestions().size()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QuoteApp", "Error searching for flashcard scores", databaseError.toException());
            }
        });
    }

 */

    //----------------------------------------------------------------------------------------------
    //Back-up feature: Update the performance of the flashcards as a whole (without Bryan's difficulty
    //levels, then using this to calculate the best recall time to recall the questions for flashcard.)
    //----------------------------------------------------------------------------------------------
    /*
    private float calculatePercentage(int score, int totalQuestions) {
        return ((float) score / totalQuestions) * 100;
    }
     */

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

    //___________________________________________________________________________________
    private void updatePoints(String username, int pointsToAdd) {
        if (username == null) {
            Log.e("UpdatePoints", "Username is null. Cannot proceed.");
            return;
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(username);

        Log.d("UpdatePoints", "Updating points for user: " + username);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if the user exists in the database
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        // Get the current points of the user
                        int currentPoints = user.getPoints();

                        // Calculate the new points
                        int newPoints = currentPoints + pointsToAdd;

                        // Update the points in the user object
                        user.setPoints(newPoints);

                        // Save the updated user object back to the database
                        userRef.setValue(user);

                        Log.d("UpdatePoints", "Points updated for user: " + username + ". New points: " + newPoints);
                    }
                } else {
                    Log.e("UpdatePoints", "User does not exist in the database: " + username);

                    // Create a new User object with the given points
                    User newUser = new User();
                    newUser.setPoints(pointsToAdd);

                    // Save the new user object to the database
                    userRef.setValue(newUser);

                    Log.d("UpdatePoints", "User created with points: " + pointsToAdd);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UpdatePoints", "Database error: " + databaseError.getMessage());
            }
        });
    }



}