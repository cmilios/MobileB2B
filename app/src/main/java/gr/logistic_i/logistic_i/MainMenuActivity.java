package gr.logistic_i.logistic_i;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainMenuActivity extends AppCompatActivity {

    private ArrayList<String> finums = new ArrayList<>();
    private ArrayList<String> dts = new ArrayList<>();
    private String url;
    private String clientID;
    private String refid;
    private String sourceDate;
    private EditText fromDate;
    private EditText toDate;
    private Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sqlformat = new SimpleDateFormat("yyyyMMdd");
    private ArrayList<Order> orders = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main_menu);
        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);
        storeParams();
        setUpDatePickers();

        RelativeLayout focuslayout = (RelativeLayout) findViewById(R.id.RequestFocusLayout);
        focuslayout.requestFocus();


        GetSqlDataTask g = new GetSqlDataTask(this,this);



        //todo might need improvement on params to set date pickers
       // g.execute(url, "SqlData", clientID, "1100", "GetMobileOrders", sqlformat.format(calendar.getTime()), sqlformat.format(calendar.getTime()), refid);
        initRecyclerView();





    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.orderlist);

        recyclerView.setHasFixedSize(true);
        MainMenuAdapter adapter = new MainMenuAdapter(this, orders);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));





    }


    private void setUpDatePickers(){
        getCurrentDate();
        SimpleDateFormat dpformat = new SimpleDateFormat("dd/MM/yyyy");
        sourceDate = dpformat.format(calendar.getTime());
        fromDate.setText(sourceDate);
        toDate.setText(sourceDate);

    }
    public void storeParams(){
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        clientID = intent.getStringExtra("clID");
        refid = intent.getStringExtra("refid");

    }
    public void getCurrentDate() {
        sourceDate = sqlformat.format(calendar.getTime());

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

    public void initDetailsIntent(View view){
        Toast.makeText(this, "on process", Toast.LENGTH_LONG).show();
    }

    public void setRecyclerTiles(){
        Intent in = getIntent();
        orders = in.getParcelableArrayListExtra("orderlist");
        if (orders!=null){
            initRecyclerView();
        }



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
