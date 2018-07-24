package gr.logistic_i.logistic_i;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ConfirmVoucher extends PortraitActivity {

    private ArrayList<MtrLine> mtrLines = new ArrayList<>();
    private ConfirmVoucherAdapter adapter;
    private String refid;
    private String url;
    private String clid;
    private String mtrLinesResponse;
    private Toolbar tool;
    private ArrayList<Mtrl> mtrList;
    private TextView finalp;
    private Date c;
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sqlformat = new SimpleDateFormat("yyyy-MM-dd");
    private TextView dt;
    private JSONObject data;
    boolean setState = false;
    private String res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_voucher);
        finalp = findViewById(R.id.f_price);
        dt = findViewById(R.id.current_date);
        tool = findViewById(R.id.confirm_voucher_toolbar);
        tool.setTitle("Confirm Voucher");
        tool.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tool);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        tool.setNavigationOnClickListener(v -> onBackPressed());
        c = Calendar.getInstance().getTime();
        dt.setText(df.format(c));




        storeVars();
        if (mtrLines!=null) {
            initRecyclerView();
            GsonWorker gsonWorker = new GsonWorker(url);
            new Thread(() -> {
                mtrLinesResponse = gsonWorker.calculatePrice(serCalcObj());
                runOnUiThread(this::deserMtrLinesResponse);
                runOnUiThread(adapter::notifyDataSetChanged);
                runOnUiThread(this::setSumAmnt);
            }).start();

        }





    }

    public void initRecyclerView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lines_to_confirm);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ConfirmVoucherAdapter(this, mtrLines);
        recyclerView.setAdapter(adapter);
    }

    public void storeVars(){
        Intent i = getIntent();
        mtrLines = i.getParcelableArrayListExtra("lines");
        clid = i.getStringExtra("clid");
        url = i.getStringExtra("url");
        refid = i.getStringExtra("refid");
        mtrList = i.getParcelableArrayListExtra("mtrl");


    }

    public void deserMtrLinesResponse(){
        try {
            JSONObject response = new JSONObject(mtrLinesResponse);

            JSONObject data = response.getJSONObject("data");
            JSONArray itelines = data.getJSONArray("ITELINES");
            for(int i=0; i<itelines.length(); i++){
                for (MtrLine m:mtrLines){
                    if (m.getMtrl().equals(itelines.getJSONObject(i).getString("MTRL"))){
                        m.setPrice(itelines.getJSONObject(i).getString("LINEVAL"));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject serCalcObj(){
        JSONArray sermtrlines = new JSONArray();
        for(MtrLine m:mtrLines){
            sermtrlines.put(m.serCalcLine());
        }


        JSONObject ss = new JSONObject();
        try {
            ss.put("SERIES", 7021);
            ss.put("TRNDATE", sqlformat.format(c));
            ss.put("TRDR", refid);
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
            json.put("LOCATEINFO", "ITELINES:MTRL,QTY1,DISC1PRC,DISC2PRC,DISC3PRC,PRICE,LINEVAL,PRCRULEDATA");
            json.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return json;
    }

    public  void setSumAmnt(){
        Double fp = 0.0;
        for (MtrLine m:mtrLines){
            fp = fp+Double.parseDouble(m.getPrice());
        }

        finalp.setText(fp.toString()+"€");
    }

    public void setFindoc(View view){
        JSONObject setDataJson = serSet();

        GsonWorker gson = new GsonWorker(url);
        new Thread(()->{
            res = gson.setData(setDataJson);
            if (res!=null){
                setState = true;
            }
            runOnUiThread(this::goToOrders);
        }).start();



    }

    public JSONObject serSet(){
        JSONObject setData = new JSONObject();

        try {
            setData.put("service", "setData");
            setData.put("clientID", clid);
            setData.put("appID", "1100");
            setData.put("OBJECT", "SALDOC");
            setData.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return setData;

    }

    public void goToOrders(){
        if (setState){
            Intent i = new Intent(this, MainMenuActivity.class);
            i.putExtra("url", url);
            i.putExtra("clid", clid);
            i.putExtra("refid", refid);
            this.startActivity(i);
        }
        else{
            AlertDialog alertbox = new AlertDialog.Builder(this)
                    .setMessage("Υπήρξε πρόβλημα κατα την καταχώρηση")
                    .setNeutralButton("OK", (dialog, which) -> {
                    })
                    .show();
        }
    }





}
