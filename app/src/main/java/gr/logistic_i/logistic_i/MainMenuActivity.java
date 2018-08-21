package gr.logistic_i.logistic_i;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class MainMenuActivity extends PortraitActivity {

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
    private android.support.v7.widget.Toolbar toolbarmain;
    private ImageButton sb;
    SimpleDateFormat sqlformat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat dpformat = new SimpleDateFormat("dd/MM/yyyy");
    private GsonWorker gson = new GsonWorker(null);
    private MainMenuAdapter adapter;
    private ArrayList<Order> orders = new ArrayList<>();
    private FloatingActionButton fab;


    private Handler inactivityHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable isInactive = () -> new AlertDialog.Builder(this)
            .setMessage("Είστε ανενεργός αρκετη ώρα, θελετε να γίνει ανανέωση;")
            .setNegativeButton("ΟΧΙ", (dialog, which) -> {})
            .setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    initSearch(sb);
                }
            })
            .setIcon(R.drawable.ic_info_outline_black_24dp)
            .setTitle("Ειδοποίηση.")
            .show();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        toolbarmain = findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbarmain);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Οι Παραγγελίες μου");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarmain.setNavigationOnClickListener(v -> onBackPressed());
        RecyclerView rv = findViewById(R.id.orderlist);
        fab = findViewById(R.id.additem);
        NestedScrollView nv = findViewById(R.id.nestedscrollview);
        ViewCompat.setNestedScrollingEnabled(rv, false);
        nv.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                fab.hide();
            } else {
                fab.show();
            }
        });
        nv.setSmoothScrollingEnabled(true);
        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);
        results_section = findViewById(R.id.results_section);
        sb = findViewById(R.id.search);
        storeParams();
        setUpDatePickers();
        RelativeLayout focusLayout =  findViewById(R.id.RequestFocusLayout);
        focusLayout.requestFocus();



        gson = new GsonWorker(url);

        new Thread(() -> {

            SqlRequest sqlRequest = new SqlRequest("SqlData", clientId, "1100", "GetMobileOrders", sqlformat.format(fromCalendar.getTime()), sqlformat.format(toCalendar.getTime()), refid);
            orders = gson.getSqlOrders(sqlRequest);
            runOnUiThread(this::initRecyclerView);
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

        new Thread(()->{
            DatePickerDialog.OnDateSetListener fDateListener = (view, year, monthOfYear, dayOfMonth) -> {
                new Thread(()->{
                    fromCalendar.set(Calendar.YEAR, year);
                    fromCalendar.set(Calendar.MONTH, monthOfYear);
                    fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                }).start();

                updateFromLabel();
            };

            DatePickerDialog.OnDateSetListener tDateListener = (view, year, monthOfYear, dayOfMonth) -> {
                new Thread(()->{
                    toCalendar.set(Calendar.YEAR, year);
                    toCalendar.set(Calendar.MONTH, monthOfYear);
                    toCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                }).start();
                updateToLabel();
            };

            fromDate.setOnClickListener(v -> new DatePickerDialog(MainMenuActivity.this, fDateListener, fromCalendar
                    .get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH),
                    fromCalendar.get(Calendar.DAY_OF_MONTH)).show());

            toDate.setOnClickListener(v -> new DatePickerDialog(MainMenuActivity.this, tDateListener, toCalendar
                    .get(Calendar.YEAR), toCalendar.get(Calendar.MONTH),
                    toCalendar.get(Calendar.DAY_OF_MONTH)).show());



        }).start();



    }

    private void initRecyclerView(){

        RecyclerView recyclerView = findViewById(R.id.orderlist);

        new Thread(()->{
            recyclerView.setHasFixedSize(true);
            runOnUiThread(()->recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)));
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState ==  SCROLL_STATE_IDLE){
                        if (fab.getVisibility()!=View.VISIBLE){
                            fab.show();
                        }
                    }
                    else{
                        fab.hide();
                    }
                }
            });
            Looper.prepare();
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent i = new Intent(getApplicationContext(), VoucherDetailsActivity.class);
                    i.putExtra("order", orders.get(position));
                    i.putExtra("url", url);
                    i.putExtra("clID", clientId);
                    i.putExtra("refid", refid);
                    startActivity(i);
                }

                @Override
                public void onLongItemClick(View view, int position) {}
            }));
            adapter = new MainMenuAdapter(this, orders);
            runOnUiThread(()->recyclerView.setAdapter(adapter));
        }).start();

    }

    private void setUpDatePickers(){
            fromCalendar.add(Calendar.WEEK_OF_MONTH, -3);
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
        Intent intent = new Intent(this, ItemsMenuActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("refid", refid);
        intent.putExtra("clid", clientId);

        startActivity(intent);



    }

    public void initSearch(View view){

        RelativeLayout waitLay = findViewById(R.id.wait_lay_mm);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        waitLay.setVisibility(View.VISIBLE);


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


            runOnUiThread(()-> {
                waitLay.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            });


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
                    assert imm != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    private void updateFromLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        runOnUiThread(()->fromDate.setText(sdf.format(fromCalendar.getTime())));


    }

    private void updateToLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        runOnUiThread(()->toDate.setText(sdf.format(toCalendar.getTime())));

    }

    @Override
    public void onBackPressed() {

        // do something when the button is clicked
        new AlertDialog.Builder(this)
                .setMessage("Θα γίνει αποσύνδεση. Θέλετε να συνεχίσετε;")
                .setPositiveButton("ΝΑΙ", (arg0, arg1) -> finish())
                .setNegativeButton("ΟΧΙ", (arg0, arg1) -> {})
                .show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.settings:
                break;
            case R.id.logout:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    public static final long DISCONNECT_TIMEOUT = 600000; // 10 min = 10 * 60 * 1000 ms



    public void resetDisconnectTimer(){
        inactivityHandler.removeCallbacks(isInactive);
        inactivityHandler.postDelayed(isInactive, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer(){
        inactivityHandler.removeCallbacks(isInactive);
    }

    @Override
    public void onUserInteraction(){
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }
}



