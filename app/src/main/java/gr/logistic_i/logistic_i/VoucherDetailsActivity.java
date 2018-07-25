package gr.logistic_i.logistic_i;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Objects;

public class VoucherDetailsActivity extends PortraitActivity {


    private String url;
    private String clientId;
    private Order o;
    private ArrayList<MtrLine> mtrLines = new ArrayList<>();
    private EditText dcode;
    private EditText dfindoc;
    private EditText dfincode;
    private EditText dtrndate;
    private EditText dtrdrName;
    private EditText dsumamnt;
    private android.support.v7.widget.Toolbar dtoolbar;
    private VoucherDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_details);
        dcode = findViewById(R.id.dcode);
        dfindoc = findViewById(R.id.dfindoc);
        dfincode = findViewById(R.id.dfincode);
        dtrndate = findViewById(R.id.dtrndate);
        dtrdrName = findViewById(R.id.dtrdrname);
        dsumamnt = findViewById(R.id.dsumamnt);
        dtoolbar = findViewById(R.id.details_toolbar);
        dtoolbar.setTitle("Confirm Voucher");
        dtoolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(dtoolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        dtoolbar.setNavigationOnClickListener(v -> onBackPressed());
        setTexts();
        GsonWorker gsonWorker = new GsonWorker(url);

        initRecyclerView();
        new Thread(()->{
            MtrLinesReq mtrLinesReq = new MtrLinesReq("SqlData", clientId, "1100", "GetMtrLines", o.getFindoc());
            gsonWorker.getMtrLines(mtrLinesReq);
            mtrLines = gsonWorker.getMtrLines();
            adapter.replaceList(mtrLines);
            runOnUiThread(adapter::notifyDataSetChanged);



        }).start();






    }

    public void setTexts(){
        Intent i = getIntent();
        o = i.getParcelableExtra("order");
        url = i.getStringExtra("url");
        clientId = i.getStringExtra("clID");
        dcode.setText(o.getCode());
        dfindoc.setText(o.getFindoc());
        dfincode.setText(o.getFincode());
        dtrndate.setText(o.getTrndate());
        dtrdrName.setText(o.getTrdrName());
        dsumamnt.setText(o.getSumamnt());


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

    public void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.mtrdetails);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new VoucherDetailsAdapter(this, mtrLines);
        recyclerView.setAdapter(adapter);
    }

}
