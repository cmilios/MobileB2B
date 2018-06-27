package gr.logistic_i.logistic_i;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    private Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat sqlformat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat dpformat = new SimpleDateFormat("dd/MM/yyyy");
    private GsonWorker gson = new GsonWorker(null);
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



        gson = new GsonWorker(url);
        initRecyclerView();
        new Thread(() -> {

            SqlRequest sqlRequest = new SqlRequest("SqlData", clientID, "1100", "GetMobileOrders", sqlformat.format(fromCalendar.getTime()), sqlformat.format(toCalendar.getTime()), refid);
            gson.getSqlOrders(sqlRequest);
            orders = gson.getSqlResponse();
            adapter.replaceList(orders);
            runOnUiThread((adapter::notifyDataSetChanged));
            if(!orders.isEmpty()){
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




        DatePickerDialog.OnDateSetListener fDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateFromLabel();
        };

        DatePickerDialog.OnDateSetListener tDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateToLabel();
        };


        fromDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(MainMenuActivity.this, fDateListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        toDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(MainMenuActivity.this, tDateListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });








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

        fromDateString = fromDate.getText().toString();
        toDateString = toDate.getText().toString();
        Date fDate = null;
        Date tDate = null;
        try {
            fDate = dpformat.parse(fromDateString);
            tDate = dpformat.parse(toDateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date finalFDate = fDate;
        Date finalTDate = tDate;
        new Thread(()->{
            SqlRequest sqlRequest = new SqlRequest("SqlData", clientID, "1100", "GetMobileOrders", sqlformat.format(finalFDate), sqlformat.format(finalTDate), refid);
            gson.getSqlOrders(sqlRequest);
            orders = gson.getSqlResponse();
            adapter.replaceList(orders);
            runOnUiThread((adapter::notifyDataSetChanged));
            if(!orders.isEmpty()){
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

    private void updateFromLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        fromDate.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateToLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        toDate.setText(sdf.format(myCalendar.getTime()));
    }



}
