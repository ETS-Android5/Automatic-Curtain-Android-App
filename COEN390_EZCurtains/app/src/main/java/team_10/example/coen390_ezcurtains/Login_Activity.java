package team_10.example.coen390_ezcurtains;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import team_10.example.coen390_ezcurtains.Controllers.SqlLiteUserAdapter;
import team_10.example.coen390_ezcurtains.Models.User;

public class Login_Activity extends AppCompatActivity {
    TextInputEditText txt_username;
    TextInputEditText txt_password;

    Button login_button;

    SqlLiteUserAdapter db;
    final String db_name = "Class02DB.db";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        login_button = findViewById(R.id.login_button);
        txt_username = findViewById(R.id.txt_userId);
        txt_password = findViewById(R.id.txt_password);


        // Login process
        login_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                String user = txt_username.getText().toString();
                String password = txt_password.getText().toString();
                String SqlCommand = "select username, password from users " + "where username = '" + user + "' AND password = '" + password + "'";
                List<User> users = db.Select(SqlCommand);

                if (users.size() > 0) {
                    Intent intent_home = new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(intent_home);
                } else {
                    Toast.makeText(getBaseContext(), R.string.text_error_user, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
        private void init_database ()
        {
            try {
                SqlLiteUserAdapter db = new SqlLiteUserAdapter(this, db_name);
                String sql = "select username, password from users where username = 'Zeineb'";
                List<User> users = db.Select(sql);
                if (users.size() > 0)
                {
                    Toast.makeText(getBaseContext(), R.string.text_default_user_found, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), R.string.text_default_user_not_found, Toast.LENGTH_LONG).show();
                    User user = new User();
                    user.setUserName("Zeineb");
                    user.setPassword("Team10!");
                    boolean IsSuccess = db.Insert(user);
                    if (IsSuccess) {
                        Toast.makeText(getBaseContext(), R.string.text_default_user_found, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception exception) {
                Log.i("Database error:", exception.getMessage());
            }
        }

}
