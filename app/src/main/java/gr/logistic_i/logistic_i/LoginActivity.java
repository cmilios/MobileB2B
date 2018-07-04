package gr.logistic_i.logistic_i;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class LoginActivity extends PortraitActivity {

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
        GsonWorker gson = new GsonWorker(curl);

       Creds c1 = new Creds(name, pass, curl);
        Handler h = new Handler() {
            public void handleMessage(Message msg){
                if(msg.what == 0){
                    Toast.makeText(getApplicationContext(), "Wrong Credentials!", Toast.LENGTH_SHORT).show();
                }
                if (msg.what == 1){
                    Toast.makeText(getApplicationContext(), "No Network Connection Detected", Toast.LENGTH_SHORT).show();

                }
            }
        };
//        String serObj = c1.serObjLogin();
//        try {
//            json = new JSONObject(serObj);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        w.execute(c1.getCurl(), "login", json);

        new Thread(() -> {

            if (isOnline()){
                gson.makeLogin(c1);
                if (gson.getAuthenticationFlag()){
                    Intent i = new Intent(this, MainMenuActivity.class);
                    i.putExtra("url", gson.getUrl());
                    i.putExtra("clID", gson.getAuthenticateClID());
                    i.putExtra("refid", gson.getRefID());
                    this.startActivity(i);
                    runOnUiThread(()->setAllToNormal());
                }
                else{
                    h.sendEmptyMessage(0);
                    runOnUiThread(() -> setAllToNormal());

                }
            }
            else{

                h.sendEmptyMessage(1);
                runOnUiThread(() -> setAllToNormal());
            }



        }).start();









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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



}
