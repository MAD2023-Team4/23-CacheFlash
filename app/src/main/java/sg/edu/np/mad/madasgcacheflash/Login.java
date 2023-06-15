package sg.edu.np.mad.madasgcacheflash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    String title = "Main activity";
    /*
    private String GLOBAL_PREF = "MyPrefs";
    private String MY_USERNAME = "MyUserName";
    private String MY_PASSWORD = "MyPassword";
    SharedPreferences sharedPreferences;
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.i(title, "Starting App Login Page");
        TextView newUser = findViewById(R.id.textView4);

        newUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(Login.this, Signup.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return false;
            }
        });

        Button loginButton = findViewById(R.id.button);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                EditText etUsername = findViewById(R.id.editTextText);
                EditText etPassword = findViewById(R.id.editTextText2);

                if(!(etUsername.getText().toString().isEmpty() ||
                        etPassword.getText().toString().isEmpty())){
                    if(isValidCredentials(etUsername.getText().toString(),
                            etPassword.getText().toString())){
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }

                    else{
                        Toast.makeText(Login.this, "Invalid username/password!",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                /*
                else if (etUsername.getText().toString() == null ||
                        etPassword.getText().toString() == null) {
                    Toast.makeText(MainActivity.this, "Username/password is empty!",
                            Toast.LENGTH_SHORT).show();
                }
*/


                else{
                    Toast.makeText(Login.this,"Username/password is empty!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    MyDBHandler myDBHandler = new MyDBHandler(this, null);
    public boolean isValidCredentials(String username, String password){
        /*
        sharedPreferences = getSharedPreferences(GLOBAL_PREF, MODE_PRIVATE);
        String sharedUsername = sharedPreferences.getString(MY_USERNAME,"");
        String sharedPassword = sharedPreferences.getString(MY_PASSWORD,"");

        if (sharedUsername.equals(username) && sharedPassword.equals(password)){
            return true;
        }
        return false; */

        User dbData = myDBHandler.findUser(username);
        if (dbData != null) {
            if (dbData.getUsername().equals(username) && dbData.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}