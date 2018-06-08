package gr.logistic_i.logistic_i;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

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
        setAllToNormal();




    }

    public void performLogin(View view){
        Button button = findViewById(R.id.loginButton);
        RelativeLayout pbar = findViewById(R.id.loadingPanel);
        button.setVisibility(View.INVISIBLE);

        pbar.setVisibility(View.VISIBLE);


        EditText n = findViewById(R.id.username);
        EditText p = findViewById(R.id.password);
        EditText c = findViewById(R.id.connectionurl);
        name = n.getText().toString();
        pass = p.getText().toString();
        curl = c.getText().toString();
        JSONObject json = new JSONObject();
        LoginAuthenticateTask w = new LoginAuthenticateTask(getApplicationContext(), this);
        Creds c1 = new Creds(name, pass, curl);
        String serObj = c1.serObjLogin();
        try {
            json = new JSONObject(serObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        w.execute(c1.getCurl(), "login", json);







    }


    public void setAllToNormal(){
        Button button = findViewById(R.id.loginButton);
        RelativeLayout pbar = findViewById(R.id.loadingPanel);
        pbar.setVisibility(View.INVISIBLE);
        button.setVisibility(View.VISIBLE);

    }


}
