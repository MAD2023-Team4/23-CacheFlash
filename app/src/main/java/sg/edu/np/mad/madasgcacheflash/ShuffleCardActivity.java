package sg.edu.np.mad.madasgcacheflash;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class ShuffleCardActivity extends AppCompatActivity {

    private List<ImageView> imageViews;
    private int currentIndex = 0;
    private int animationDuration = 100;
    private int pauseDuration = 500; // Adjust the pause duration here
    private int roundCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuffle_card);

        // Initialize the ImageViews
        ImageView imageView1 = findViewById(R.id.one);
        ImageView imageView2 = findViewById(R.id.two);
        ImageView imageView3 = findViewById(R.id.three);

        imageViews = new ArrayList<>();
        imageViews.add(imageView1);
        imageViews.add(imageView2);
        imageViews.add(imageView3);

        // Start the shuffling animation
        startShufflingAnimation();
    }

    private  void startShufflingAnimation() {
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
        animatorSet.addListener(new Animator.AnimatorListener() {
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

                // Start the FlashcardActivity after one round
                if (roundCount == totalImages) {
                    startFlashcardActivity();
                } else {
                    // Start the next shuffling animation after a delay
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startShufflingAnimation();
                        }
                    }, pauseDuration);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        animatorSet.start();
    }

    private void startFlashcardActivity() {
        Intent flashcardIntent;
        Class<?> targetActivity = (Class<?>) getIntent().getSerializableExtra("targetActivity");
        if (targetActivity == null) {
            // Default target activity
            flashcardIntent = new Intent(this, LearnYourself.class);
        } else {
            // Use the specified target activity
            flashcardIntent = new Intent(this, targetActivity);
        }

        // Retrieve the flashcard from the intent
        Flashcard flashcard = getIntent().getParcelableExtra("flashcard");

        // Pass the flashcard to the target activity
        flashcardIntent.putExtra("flashcard", flashcard);
        // Retrieve the username from the original intent
        String username = getIntent().getStringExtra("Username");
        flashcardIntent.putExtra("Username", username); // Add the username extra to the intent

        // Clear the activity stack and start the target activity
        flashcardIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(flashcardIntent);
        finish();
    }
}