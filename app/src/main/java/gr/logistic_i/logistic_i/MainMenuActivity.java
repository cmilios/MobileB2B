package gr.logistic_i.logistic_i;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private String toDateString;
    private String fromDateString;
    private EditText fromDate;
    private EditText toDate;
    private TextView results_section;
    private Calendar fromCalendar = Calendar.getInstance();
    private Calendar toCalendar = Calendar.getInstance();
    SimpleDateFormat sqlformat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat dpformat = new SimpleDateFormat("dd/MM/yyyy");
    private MainMenuAdapter adapter;
    private ArrayList<Order> orders = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main_menu);
        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);
        results_section = findViewById(R.id.results_section);
        storeParams();

        setUpDatePickers();

        RelativeLayout focuslayout = (RelativeLayout) findViewById(R.id.RequestFocusLayout);
        focuslayout.requestFocus();



        GsonWorker g = new GsonWorker(url);
        initRecyclerView();
        new Thread(() -> {

            SqlRequest sqlRequest = new SqlRequest("SqlData", clientID, "1100", "GetMobileOrders", sqlformat.format(fromCalendar.getTime()), sqlformat.format(toCalendar.getTime()), refid);
            g.getSqlOrders(sqlRequest);
            orders = g.getSqlResponse();
            if(!orders.isEmpty()){
                adapter.replaceList(orders);
                runOnUiThread((adapter::notifyDataSetChanged));
                if(orders.size() == 1){
                    runOnUiThread(()->results_section.setText("Βρέθηκε "+orders.size()+" αποτελέσμα."));
                }
                else{
                    runOnUiThread(()->results_section.setText("Βρέθηκαν "+orders.size()+" αποτελέσματα."));
                }

            }
            else{
                runOnUiThread(()->results_section.setText("Δεν βρέθηκαν αποτελέσματα."));
            }


        }).start();





    }

    private void initRecyclerView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.orderlist);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MainMenuAdapter(this, orders);
        recyclerView.setAdapter(adapter);
    }


    private void setUpDatePickers(){
        fromCalendar.add(Calendar.MONTH, -3);

        toDateString = dpformat.format(toCalendar.getTime());
        fromDateString = dpformat.format(fromCalendar.getTime());
        fromDate.setText(fromDateString);
        toDate.setText(toDateString);


    }
    public void storeParams(){
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        clientID = intent.getStringExtra("clID");
        refid = intent.getStringExtra("refid");

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
