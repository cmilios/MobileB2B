package gr.logistic_i.logistic_i;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends AppCompatActivity {

    String name;
    String pass;
    String curl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText n = (EditText)findViewById(R.id.username);
                EditText p = (EditText)findViewById(R.id.password);
                EditText c = (EditText)findViewById(R.id.connectionurl);
                name = n.getText().toString();
                pass = p.getText().toString();
                curl = c.getText().toString();
                APICalls a = new APICalls();
                UserData us;

                Creds c1 = new Creds(name, pass, curl);
                a.execute(c1);

                boolean flag = false;
                if(flag == true){
                    Intent i = new Intent(LoginActivity.this, MainMenu.class);
                    startActivity(i);

                }





            }
        });
    }


}
