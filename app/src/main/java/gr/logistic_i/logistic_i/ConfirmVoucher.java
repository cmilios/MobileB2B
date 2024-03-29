package gr.logistic_i.logistic_i;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.beardedhen.androidbootstrap.BootstrapButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ConfirmVoucher extends PortraitActivity {

    private RelativeLayout onemom;

    private ArrayList<MtrLine> mtrLines = new ArrayList<>();
    private String refid;
    private String url;
    private String clid;
    private String mtrLinesResponse;
    private Date c;
    private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat sqlformat = new SimpleDateFormat("yyyy-MM-dd");
    private JSONObject data;
    boolean setState = false;
    private String res;
    private BootstrapButton setFindocButton;
    private ProgressBar pbar;
    private TextInputEditText comms;
    private RelativeLayout rq;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_voucher);
        onemom = findViewById(R.id.momentlay);
        onemom.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        rq = findViewById(R.id.rq);
        rq.requestFocus();
        TextView dt = findViewById(R.id.current_date);
        Toolbar tool = findViewById(R.id.confirm_voucher_toolbar);
        pbar = findViewById(R.id.setBar);
        comms = findViewById(R.id.comm_section);
        tool.setTitle("Επισκόπηση παραστατικού");
        tool.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tool);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        tool.setNavigationOnClickListener(v -> onBackPressed());
        c = Calendar.getInstance().getTime();
        dt.setText(df.format(c));
        setFindocButton = findViewById(R.id.setf);
        setFindocButton.setOnClickListener(v -> {
            pbar.setVisibility(View.VISIBLE);
            setFindocButton.setVisibility(View.GONE);
            serializeCalculateRequest();
            JSONObject setDataJson = serializeSetDataRequest();
            GsonWorker gson = new GsonWorker(url);
            new Thread(()->{
                res = gson.setData(setDataJson);
                if (res!=null){
                    setState = true;
                }
                runOnUiThread(this::goToOrders);
            }).start();
        });

        storeVars();
        if (mtrLines!=null) {
            GsonWorker gsonWorker = new GsonWorker(url);
            new Thread(() -> {
                mtrLinesResponse = gsonWorker.calculatePrice(serializeCalculateRequest());
                runOnUiThread(this::deserMtrLinesResponse);
                runOnUiThread(this::initRecyclerView);
                runOnUiThread(()-> {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    onemom.setVisibility(View.GONE);
                });
            }).start();
        }
    }

    public void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.lines_to_confirm);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        BasketAdapter adapter = new BasketAdapter( mtrLines,this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    public void storeVars(){
        mtrLines = ((App)this.getApplication()).getMtrLines();
        clid = ((App)this.getApplication()).getClientID();
        url = ((App)this.getApplication()).getUrl();
        refid = ((App)this.getApplication()).getRefID();
        key = ((App)this.getApplication()).getKey();
    }

    public void deserMtrLinesResponse(){
        try {
            JSONObject response = new JSONObject(mtrLinesResponse);

            JSONObject data = response.getJSONObject("data");
            JSONArray itelines = data.getJSONArray("ITELINES");
            for(int i=0; i<itelines.length(); i++){
                for (MtrLine m:mtrLines){
                    if (m.getMtrl().equals(itelines.getJSONObject(i).getString("MTRL"))){
                        if(itelines.getJSONObject(i).has("LINEVAL")) {
                            m.setCleanValue(itelines.getJSONObject(i).getString("LINEVAL"));
                        }
                        else{
                            m.setCleanValue("0");
                        }
                        if (itelines.getJSONObject(i).has("VATAMNT")){
                            m.setFpaValue(itelines.getJSONObject(i).getString("VATAMNT"));
                        }
                        else{
                            m.setFpaValue("0");
                        }
                        m.setPrice(String.valueOf(Double.parseDouble(m.getCleanValue()) + Double.parseDouble(m.getFpaValue())));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject serializeCalculateRequest(){
        JSONArray sermtrlines = new JSONArray();
        for(MtrLine m:mtrLines){
            sermtrlines.put(m.serCalcLine(mtrLines.indexOf(m)+1));
        }
        JSONObject ss = new JSONObject();
        try {
            ss.put("SERIES", 7021);
            ss.put("TRNDATE", sqlformat.format(c));
            ss.put("TRDR", refid);
            ss.put("COMMENTS", comms.getText().toString());
            ss.put("FINSTATES", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray saldoc = new JSONArray();
        saldoc.put(ss);
        data = new JSONObject();
        try {
            data.put("SALDOC", saldoc);
            data.put("ITELINES", sermtrlines);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        try {
            json.put("service", "calculate");
            json.put("clientID", clid);
            json.put("appID", "1100");
            json.put("OBJECT", "SALDOC");
            json.put("Key", "");
            json.put("LOCATEINFO", "ITELINES:MTRL,QTY1,DISC1PRC,DISC2PRC,DISC3PRC,PRICE,LINEVAL,VATAMNT");
            json.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }



    public JSONObject serializeSetDataRequest(){
        JSONObject setData = new JSONObject();

        try {
            setData.put("service", "setData");
            setData.put("clientID", clid);
            setData.put("appID", "1100");
            setData.put("OBJECT", "SALDOC");
            if (key!=null){
                setData.put("KEY", key);
            }

            setData.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return setData;
    }

    public void goToOrders(){
        if (setState){
            ((App)this.getApplication()).setMtrLines(new ArrayList<>());
            Intent i = new Intent(this, MainMenuActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(i);
        }
        else{
            pbar.setVisibility(View.GONE);
            setFindocButton.setVisibility(View.VISIBLE);
            new AlertDialog.Builder(this)
                    .setMessage("Υπήρξε πρόβλημα κατα την καταχώρηση")
                    .setNeutralButton("OK", (dialog, which) -> {
                    })
                    .show();
        }
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
                    rq.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}
