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
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

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
                EditText n = findViewById(R.id.username);
                EditText p = findViewById(R.id.password);
                EditText c = findViewById(R.id.connectionurl);
                name = n.getText().toString();
                pass = p.getText().toString();
                curl = c.getText().toString();
                JSONObject json = new JSONObject();
                WebDial w = new WebDial(getApplicationContext());
                Creds c1 = new Creds(name, pass, curl);
                String serObj = c1.serObj();
                String res = null;
                try {
                    json = new JSONObject(serObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                w.execute(c1.getCurl(), "login", json,res);

            }
        });
    }


}
