package sg.edu.np.mad.madasgcacheflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private EditText editTextOldPassword;
    private EditText editTextNewPassword;
    private EditText editTextConfirmNewPassword;
    private Button button_change_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        editTextOldPassword = findViewById(R.id.oldPasswordEditText);
        editTextNewPassword = findViewById(R.id.newPasswordEditText);
        editTextConfirmNewPassword = findViewById(R.id.confirmNewPasswordEditText);
        button_change_password = findViewById(R.id.button5);

        button_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = editTextOldPassword.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();
                String confirmNewPassword = editTextConfirmNewPassword.getText().toString();

                if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                    Toast.makeText(ChangePassword.this, "One or more of the fields are not filled in", Toast.LENGTH_SHORT).show();
                } else {
                    // Validate old password
                    reauthenticateUser(oldPassword, newPassword, confirmNewPassword);
                }
            }
        });
    }

    private void reauthenticateUser(String oldPassword, String newPassword, String confirmNewPassword) {
        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), oldPassword);
        currentUser.reauthenticate(credential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(newPassword.length() < 6){
                            Toast.makeText(ChangePassword.this,"New Password set minimum length must be at least 6",Toast.LENGTH_SHORT).show();
                        }
                        else{
                        // User successfully reauthenticated, validate new and confirm passwords
                        if(newPassword.matches(".*\\d.*")){
                            if (newPassword.equals(confirmNewPassword)) {
                                updatePassword(newPassword);
                            } else {
                                Toast.makeText(ChangePassword.this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(ChangePassword.this, "New password must contain at least a number", Toast.LENGTH_SHORT).show();
                        }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Reauthentication failed, show error message
                        Toast.makeText(ChangePassword.this, "Failed to change password. Please check your old password.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updatePassword(String newPassword) {
        currentUser.updatePassword(newPassword)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Password successfully updated
                        Toast.makeText(ChangePassword.this, "Password updated successfully.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Password update failed, show error message
                        Toast.makeText(ChangePassword.this, "Failed to update password. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
