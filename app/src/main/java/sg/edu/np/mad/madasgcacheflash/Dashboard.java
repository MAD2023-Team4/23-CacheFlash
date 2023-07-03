package sg.edu.np.mad.madasgcacheflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Dashboard extends AppCompatActivity {
    private String username;
    int score;
    int total;
    Flashcard flashcard;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();
        username = intent.getStringExtra("Username"); //get username
        if (intent.hasExtra("Score") && intent.hasExtra("Total")) {
            // The intent contains the "score" key
            flashcard = intent.getParcelableExtra("flashcard");
            score = intent.getIntExtra("Score", 0); // Retrieve the value of "score" key, use 0 as default value if not found
            total = intent.getIntExtra("Total",0);
        }


        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item != null) {
                    int id = item.getItemId();

                    if (id == R.id.dashboard) {
                        return true;
                    }
                    else if (id == R.id.home) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    else if (id == R.id.about){
                        Intent intent = new Intent(getApplicationContext(), Profile.class);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                }
                return false;
            }
        });

    }
}