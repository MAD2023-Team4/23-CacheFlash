package sg.edu.np.mad.madasgcacheflash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

//Source from: https://abhiandroid.com/programming/splashscreen
//__________________________________________________________________________________________
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        // Delay the splash screen for 3 seconds.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity after the splash screen delay.
                startActivity(new Intent(SplashActivity.this, Login.class));
                finish();
            }
        }, 3000);
    }
}