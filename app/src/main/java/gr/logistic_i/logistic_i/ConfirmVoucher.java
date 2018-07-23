package gr.logistic_i.logistic_i;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ConfirmVoucher extends PortraitActivity {

    private ArrayList<MtrLine> mtrLines = new ArrayList<>();
    private ConfirmVoucherAdapter adapter;
    private String refid;
    private String url;
    private String clid;
    private String mtrLinesResponse;
    private Toolbar tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_voucher);
        tool = findViewById(R.id.confirm_voucher_toolbar);
        tool.setTitle("Confirm Voucher");
        setSupportActionBar(tool);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        storeVars();
        if (mtrLines!=null) {
            GsonWorker gsonWorker = new GsonWorker(url);
            new Thread(() -> {
                mtrLinesResponse = gsonWorker.calculateLinePrice(serCalcObj());
                runOnUiThread(this::deserMtrLinesResponse);
                runOnUiThread(adapter::notifyDataSetChanged);
            }).start();
            initRecyclerView();
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


    }

    public void deserMtrLinesResponse(){
        try {
            JSONObject response = new JSONObject(mtrLinesResponse);

            JSONObject data = response.getJSONObject("data");
            JSONArray itelines = data.getJSONArray("ITELINES");
            for(int i=0; i<itelines.length(); i++){
                for (MtrLine m:mtrLines){
                    if (m.getCode().equals(itelines.getJSONObject(i).getString("MTRL"))){
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
            ss.put("TRDR", refid);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONArray saldoc = new JSONArray();
        saldoc.put(ss);

        JSONObject data = new JSONObject();
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




}
