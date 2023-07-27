package sg.edu.np.mad.madasgcacheflash;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

public class WalkThrough extends AppCompatActivity {

    CardView nextCard;
    LinearLayout dotsLayout;
    ViewPager viewPager;

    TextView[] dots;
    int currentPosition;
    SaveState saveState ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        nextCard = findViewById(R.id.nextCard);
        dotsLayout = findViewById(R.id.dotsLayout);
        viewPager = findViewById(R.id.slider);
        dotsFunction(0);
        saveState = new SaveState(WalkThrough.this,"OB");
        if (saveState.getState() == 1){
            Intent i = new Intent(WalkThrough.this,MainActivity.class);
            startActivity(i);
            finish();
        }

        OnBoardingAdapter adapter = new OnBoardingAdapter(this);
        viewPager.setAdapter(adapter);
        nextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(currentPosition+1,true);
            }
        });
        viewPager.setOnPageChangeListener(onPageChangeListener);

    }

    private void dotsFunction(int pos) {
        dots = new TextView[4];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("•"));
            dots[i].setTextColor(ContextCompat.getColor(this, R.color.white)); // Use ContextCompat.getColor()
            dots[i].setTextSize(30);

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[pos].setTextColor(ContextCompat.getColor(this, R.color.teal_700)); // Use ContextCompat.getColor()
            dots[pos].setTextSize(40);
        }
    }
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            dotsFunction(position);
            currentPosition = position;
            if (currentPosition <= 2){
                nextCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(currentPosition+1);
                    }
                });
            }else{
                nextCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveState.setState(1);
                        Intent i = new Intent(WalkThrough.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}