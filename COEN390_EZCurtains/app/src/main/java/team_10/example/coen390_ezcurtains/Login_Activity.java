package team_10.example.coen390_ezcurtains;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Locale;

import team_10.example.coen390_ezcurtains.Controllers.SqlLiteUserAdapter;
import team_10.example.coen390_ezcurtains.Models.User;

public class Login_Activity extends AppCompatActivity {
    TextInputEditText txt_username;
    TextInputEditText txt_password;

    Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        login_button = findViewById(R.id.login_button);
        txt_username = findViewById(R.id.txt_userId);
        txt_password = findViewById(R.id.txt_password);

        // Login process
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = txt_username.getText().toString();
                String password = txt_password.getText().toString();

                if (user.equals("1") && password.equals("1")) {
                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(intent);
                } else
                    Toast.makeText(getBaseContext(), R.string.text_error_user, Toast.LENGTH_SHORT).show();
            }
            });

        }
    }

