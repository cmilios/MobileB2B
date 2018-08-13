package gr.logistic_i.logistic_i;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
    RelativeLayout rq_fc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        rq_fc = findViewById(R.id.rq_fc);
        rq_fc.requestFocus();
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
        @SuppressLint("HandlerLeak") Handler h = new Handler() {
            public void handleMessage(Message msg){
                if(msg.what == 0){
                    Toast.makeText(getApplicationContext(), "Λανθασμένο όνομα χρήστη ή συνθηματικό!", Toast.LENGTH_SHORT).show();
                }
                if (msg.what == 1){
                    Toast.makeText(getApplicationContext(), "Δεν ανιχνεύτικε κάποια σύνδεση στο δίκτυο", Toast.LENGTH_SHORT).show();

                }
                if (msg.what==2){
                    Toast.makeText(getApplicationContext(),"Το URL δεν αντιστοιχεί σε κάποια βάση", Toast.LENGTH_SHORT).show();
                }
            }
         };

        new Thread(() -> {

            if (isOnline()){
                gson.makeLogin(c1);
                if (gson.isValidURL()) {
                    if (gson.getAuthenticationFlag()) {
                        Intent i = new Intent(this, MainMenuActivity.class);
                        i.putExtra("id", this.getClass().getSimpleName());
                        i.putExtra("url", gson.getUrl());
                        i.putExtra("clID", gson.getAuthenticateClID());
                        i.putExtra("refid", gson.getRefID());
                        this.startActivity(i);
                        runOnUiThread(this::setAllToNormal);
                    } else {
                        h.sendEmptyMessage(0);
                        runOnUiThread(this::setAllToNormal);

                    }
                }
                else{
                    h.sendEmptyMessage(2);
                    runOnUiThread(this::setAllToNormal);
                }
            }
            else{

                h.sendEmptyMessage(1);
                runOnUiThread(this::setAllToNormal);
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
                    rq_fc.requestFocus();
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

    @Override
    public void onBackPressed() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Η εφαρμογή θα τερματιστεί. Θέλετε να συνεχίσετε;")
                .setPositiveButton("ΝΑΙ", (arg0, arg1) -> {


                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    System.exit(0);



                })
                .setNegativeButton("ΟΧΙ", (arg0, arg1) -> {
                })
                .show();


    }


}
