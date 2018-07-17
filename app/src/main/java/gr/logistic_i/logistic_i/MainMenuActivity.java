package gr.logistic_i.logistic_i;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    private String url;
    private String clientId;
    private String refid;
    private String toDateString;
    private String fromDateString;
    private EditText fromDate;
    private EditText toDate;
    private TextView results_section;
    private Calendar fromCalendar = Calendar.getInstance();
    private Calendar toCalendar = Calendar.getInstance();
    android.support.v7.widget.Toolbar toolbarmain;
    SimpleDateFormat sqlformat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat dpformat = new SimpleDateFormat("dd/MM/yyyy");
    private GsonWorker gson = new GsonWorker(null);
    private MainMenuAdapter adapter;
    private ArrayList<Order> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        toolbarmain = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarmain);
        getSupportActionBar().setTitle("My Orders");

        

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

            SqlRequest sqlRequest = new SqlRequest("SqlData", clientId, "1100", "GetMobileOrders", sqlformat.format(fromCalendar.getTime()), sqlformat.format(toCalendar.getTime()), refid);
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
            fromCalendar.set(Calendar.YEAR, year);
            fromCalendar.set(Calendar.MONTH, monthOfYear);
            fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateFromLabel();
        };

        DatePickerDialog.OnDateSetListener tDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            toCalendar.set(Calendar.YEAR, year);
            toCalendar.set(Calendar.MONTH, monthOfYear);
            toCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateToLabel();
        };

        fromDate.setOnClickListener(v -> new DatePickerDialog(MainMenuActivity.this, fDateListener, fromCalendar
                .get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH),
                fromCalendar.get(Calendar.DAY_OF_MONTH)).show());

        toDate.setOnClickListener(v -> {
            new DatePickerDialog(MainMenuActivity.this, tDateListener, toCalendar
                    .get(Calendar.YEAR), toCalendar.get(Calendar.MONTH),
                    toCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.orderlist);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MainMenuAdapter(this, orders, url,clientId);
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
        clientId = intent.getStringExtra("clID");
        refid = intent.getStringExtra("refid");

    }

    public void initAddIntent(View view){
        Intent intent = new Intent(this, MostOrderedItems.class);
        intent.putExtra("url", url);
        intent.putExtra("refid", refid);
        intent.putExtra("clid", clientId);

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
            SqlRequest sqlRequest = new SqlRequest("SqlData", clientId, "1100", "GetMobileOrders", sqlformat.format(finalFDate), sqlformat.format(finalTDate), refid);
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

        fromDate.setText(sdf.format(fromCalendar.getTime()));
    }

    private void updateToLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        toDate.setText(sdf.format(toCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {

        // do something when the button is clicked
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Θα γίνει αποσύνδεση. Θέλετε να συνεχίσετε;")
                .setPositiveButton("ΝΑΙ", (arg0, arg1) -> {


                    //close();
                    finish();



                })
                .setNegativeButton("ΟΧΙ", (arg0, arg1) -> {
                })
                .show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        String msg=" ";
        switch (item.getItemId()){
            case R.id.settings:
                msg = "Settings";
                break;
            case R.id.user:
                msg = "User";
                break;
            case R.id.login:
                msg = "Login";
                break;
            case R.id.logout:
                msg = "Logout";
                break;
        }
        Toast.makeText(this,msg+" Checked", Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }


}
