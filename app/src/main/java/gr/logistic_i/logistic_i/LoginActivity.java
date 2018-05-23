package gr.logistic_i.logistic_i;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    String name = new String();
    String pass = new String();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText n = (EditText)findViewById(R.id.username);
                EditText p = (EditText)findViewById(R.id.password);
                name = n.getText().toString();
                pass = n.getText().toString();

                Creds c = new Creds(name,pass);

                Intent i = new Intent(LoginActivity.this, MainMenu.class);
                startActivity(i);




            }
        });
    }


}