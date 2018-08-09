package gr.logistic_i.logistic_i;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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
    private VoucherDetailsAdapter adapter;
    Boolean b = false;

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
        android.support.v7.widget.Toolbar dtoolbar = findViewById(R.id.details_toolbar);
        dtoolbar.setTitle("Λεπτομέρειες Παραστατικού");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                new AlertDialog.Builder(this)
                        .setMessage("Θελετε να γίνει ακύρωση της παραγγελίας")
                        .setPositiveButton("ΝΑΙ", (dialog, which) -> {

                            Boolean made = makeDeleteVoucherProcess();


                        })
                        .setNegativeButton("ΟΧΙ", (dialog, which) -> {

                        })
                        .show();
        }


        return super.onOptionsItemSelected(item);
    }

    private Boolean makeDeleteVoucherProcess(){
        Boolean state = false;
        doDelete();
        return state;
    }

    private void doDelete(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("service", "getData");
            jsonObject.put("clientID", clientId);
            jsonObject.put("appID", 1100);
            jsonObject.put("OBJECT", "SALDOC");
            jsonObject.put("KEY", o.getFindoc());
            jsonObject.put("LOCATEINFO","SALDOC:FINSTATES");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GsonWorker gson = new GsonWorker(url);

        new Thread(()->{
           String state = gson.getIfAbleToDelete(jsonObject);
           if (state.equals("1|Σε αναμονή")){
               JSONObject json = new JSONObject();
               JSONArray saldoc = new JSONArray();
               JSONObject salobj = new JSONObject();
               JSONObject finstates = new JSONObject();
               try {
                   finstates.put("FINSTATES", 4);
                   saldoc.put(finstates);
                   salobj.put("SALDOC", saldoc);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               try {

                   json.put("service", "setData");
                   json.put("clientID", clientId);
                   json.put("appID", 1100);
                   json.put("OBJECT", "SALDOC");
                   json.put("KEY", o.getFindoc());
                   json.put("service", "setData");
                   json.put("data", salobj);

               } catch (JSONException e) {
                   e.printStackTrace();
               }

               GsonWorker g = new GsonWorker(url);
               b = g.setDeleteFinstate(jsonObject);




           }
           else if(state.equals("4|Ακυρώθηκε απο πελάτη")){
               runOnUiThread(()-> new AlertDialog.Builder(this)
                       .setMessage("Η παραγγελία είναι ήδη ακυρωμένη απο εσάς")
                       .setNeutralButton("OK", (dialog, which) -> {})
                       .show());


           }
           else if(state.equals("2|Σε εξέλιξη")){
               runOnUiThread(()-> new AlertDialog.Builder(this)
                       .setMessage("Η παραγγελία βρίσκεται σε εξέλιξη, παρακαλώ επικοινωνήστε με τον αρμόδιο πωλητή αμα όντως επιθυμείτε την ακύρωση της!")
                       .setNeutralButton("OK", (dialog, which) -> {

                       })
                       .show());

           }
           else{
               runOnUiThread(()->
               new AlertDialog.Builder(this)
                       .setMessage("Η παραγγελία αυτή έχει ολοκληρωθεί")
                       .setNeutralButton("OK", (dialog, which) -> {

                       })
                       .show());
           }
        }).start();
    }
}
