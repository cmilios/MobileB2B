package gr.logistic_i.logistic_i;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
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
        LoginAuthenticateTask w = new LoginAuthenticateTask(this, this);
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


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

}
