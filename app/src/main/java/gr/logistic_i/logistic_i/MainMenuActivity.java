package gr.logistic_i.logistic_i;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainMenuActivity extends AppCompatActivity {

    private ArrayList<String> finums = new ArrayList<>();
    private ArrayList<String> dts = new ArrayList<>();
    private String url;
    private String clientID;
    private String sourceDate;
    private EditText fromDate;
    private EditText toDate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FloatingActionButton fab  = findViewById(R.id.fab);

        setContentView(R.layout.activity_main_menu);
        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);
        storeParams();
        setUpDatePickers();











        GetSqlDataTask g = new GetSqlDataTask(getApplicationContext());
        //todo might need improvement on params to set date pickers
        g.execute(url, "SqlData", clientID, "1100", "Orders", sourceDate);



        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.orderlist);

        recyclerView.setHasFixedSize(true);
        MainMenuAdapter adapter = new MainMenuAdapter(this, finums, dts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        final FloatingActionButton fab = findViewById(R.id.fab);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0 ||dy<0 && fab.isShown())
                {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    fab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });







    }


    private void setUpDatePickers(){
        getCurrentDate();
        fromDate.setText(sourceDate);
        toDate.setText(sourceDate);

    }
    public void storeParams(){
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        clientID = intent.getStringExtra("clID");
    }

    public void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
        sourceDate = mdformat.format(calendar.getTime());

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, LoginActivity.class);
        this.startActivity(i);
    }



    public void initAddIntent(View view){
        Intent intent = new Intent(this, AddVoucherActivity.class);
        startActivity(intent);

    }

    public void initSearch(View view){
        //todo make search between @fromDate and @toDate
    }



    //method that implements right cursor behavior on focused mode or not
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
