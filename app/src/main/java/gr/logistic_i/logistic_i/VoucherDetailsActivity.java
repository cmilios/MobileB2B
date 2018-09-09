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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;

import fr.nelaupe.spreadsheetlib.AnnotationFields;
import fr.nelaupe.spreadsheetlib.SpreadSheetAdaptor;
import fr.nelaupe.spreadsheetlib.SpreadSheetView;
import fr.nelaupe.spreadsheetlib.view.ArrowButton;

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
    private RelativeLayout waitlay;
    private Boolean b = false;

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
        waitlay = findViewById(R.id.wait_lay);
        android.support.v7.widget.Toolbar dtoolbar = findViewById(R.id.details_toolbar);
        dtoolbar.setTitle("Λεπτομέρειες Παραστατικού");
        dtoolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(dtoolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        dtoolbar.setNavigationOnClickListener(v -> onBackPressed());
        setTexts();
        GsonWorker gsonWorker = new GsonWorker(url);

        initRecyclerView();
        new Thread(() -> {
            MtrLinesReq mtrLinesReq = new MtrLinesReq("SqlData", clientId, "1100", "GetMtrLines", o.getFindoc());
            gsonWorker.getMtrLines(mtrLinesReq);
            mtrLines = gsonWorker.getMtrLines();
            adapter.replaceList(mtrLines);
            runOnUiThread(adapter::notifyDataSetChanged);


        }).start();


    }

    public void setTexts() {
        Intent i = getIntent();
        o = i.getParcelableExtra("order");
        url = ((App)this.getApplication()).getUrl();
        clientId = ((App)this.getApplication()).getClientID();
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
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(imm).hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void initRecyclerView() {
        SpreadSheetAdaptor adapter = new SpreadSheetAdaptor(this, mtrLines) {
            @Override
            public View getCellView(AnnotationFields cell, Object object) {
                return null;
            }

            @Override
            public ArrowButton getHeaderCellView(AnnotationFields cell) {
                return null;
            }

            @Override
            public View getFixedHeaderView(String name) {
                return null;
            }

            @Override
            public View getFixedCellView(String name, int position) {
                return null;
            }
        };
        SpreadSheetView spreadSheetView =(SpreadSheetView)findViewById(R.id.mtrdetails);
        spreadSheetView.setAdaptor(adapter);
        spreadSheetView.invalidate();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                new AlertDialog.Builder(this)
                        .setMessage("Θελετε να γίνει ακύρωση της παραγγελίας")
                        .setPositiveButton("ΝΑΙ", (dialog, which) -> {

                            waitlay.setVisibility(View.VISIBLE);
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            doDelete();


                        })
                        .setNegativeButton("ΟΧΙ", (dialog, which) -> {

                        })
                        .show();
                break;
            case R.id.edit:
                new AlertDialog.Builder(this)
                        .setMessage("Θέλετε να προβείτε σε επεξεργασία του παραστατικού;")
                        .setPositiveButton("ΝΑΙ", (dialog, which) -> {

                            waitlay.setVisibility(View.VISIBLE);
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            initEditActivity();


                        })
                        .setNegativeButton("ΟΧΙ", (dialog, which) -> {

                        })
                        .show();
                break;

        }


        return super.onOptionsItemSelected(item);
    }


    private void doDelete() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("service", "getData");
            jsonObject.put("clientID", clientId);
            jsonObject.put("appID", 1100);
            jsonObject.put("OBJECT", "SALDOC");
            jsonObject.put("KEY", o.getFindoc());
            jsonObject.put("LOCATEINFO", "SALDOC:FINSTATES");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GsonWorker gson = new GsonWorker(url);

        new Thread(() -> {
            String state = gson.getIfAbleToDelete(jsonObject);


            switch (state) {
                case "1|Σε αναμονή":


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
                    b = g.setDeleteFinstate(json);
                    runOnUiThread(() -> {
                        waitlay.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    });
                    if (b) {
                        runOnUiThread(() -> new AlertDialog.Builder(this)
                                .setMessage("H παραγγελία σας ακυρώθηκε.")
                                .setNeutralButton("OK", (dialog, which) -> {
                                    Intent i = new Intent(this, MainMenuActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    this.startActivity(i);
                                    finish();
                                })
                                .show());
                    } else {
                        runOnUiThread(() -> new AlertDialog.Builder(this)
                                .setMessage("Κάτι πήγε λάθος κατα την ακύρωση.")
                                .setNeutralButton("OK", (dialog, which) -> {
                                })
                                .show());
                    }


                    break;
                case "4|Ακυρώθηκε απο πελάτη":
                    runOnUiThread(() -> {
                        waitlay.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    });
                    runOnUiThread(() -> new AlertDialog.Builder(this)
                            .setMessage("Η παραγγελία είναι ακυρωμένη απο εσάς.")
                            .setNeutralButton("OK", (dialog, which) -> {
                            })
                            .show());


                    break;
                case "2|Σε εξέλιξη":
                    runOnUiThread(() -> {
                        waitlay.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    });
                    runOnUiThread(() -> new AlertDialog.Builder(this)
                            .setMessage("Η παραγγελία βρίσκεται σε εξέλιξη, παρακαλώ επικοινωνήστε με τον αρμόδιο πωλητή αν όντως επιθυμείτε την ακύρωση της!")
                            .setNeutralButton("OK", (dialog, which) -> {

                            })
                            .show());

                    break;
                default:
                    runOnUiThread(() -> {
                        waitlay.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    });
                    runOnUiThread(() ->
                            new AlertDialog.Builder(this)
                                    .setMessage("Η παραγγελία αυτή έχει ολοκληρωθεί.")
                                    .setNeutralButton("OK", (dialog, which) -> {

                                    })
                                    .show());
                    break;
            }


        }).start();
    }

    private void initEditActivity() {

        GsonWorker g = new GsonWorker(url);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("service", "getData");
            jsonObject.put("clientID", clientId);
            jsonObject.put("appID", 1100);
            jsonObject.put("OBJECT", "SALDOC");
            jsonObject.put("KEY", o.getFindoc());
            jsonObject.put("LOCATEINFO", "SALDOC:FINSTATES");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            String state = g.getIfAbleToDelete(jsonObject);


            switch (state) {
                case "1|Σε αναμονή":

                    ((App)this.getApplication()).setMtrLines(mtrLines);
                    ((App)this.getApplication()).setKey(o.getFindoc());

                    Intent i = new Intent(this, ItemsMenuActivity.class);
                    startActivity(i);
                    finish();
                    break;
                case "4|Ακυρώθηκε απο πελάτη":
                    runOnUiThread(() -> {
                        waitlay.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    });
                    runOnUiThread(() -> new AlertDialog.Builder(this)
                            .setMessage("Η παραγγελία είναι ακυρωμένη απο εσάς.")
                            .setNeutralButton("OK", (dialog, which) -> {
                            })
                            .show());


                    break;
                case "2|Σε εξέλιξη":
                    runOnUiThread(() -> {
                        waitlay.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    });
                    runOnUiThread(() -> new AlertDialog.Builder(this)
                            .setMessage("Η παραγγελία βρίσκεται σε εξέλιξη, δεν έχετε δυνατότητα επεξεργασίας.")
                            .setNeutralButton("OK", (dialog, which) -> {

                            })
                            .show());

                    break;
                default:
                    runOnUiThread(() -> {
                        waitlay.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    });
                    runOnUiThread(() ->
                            new AlertDialog.Builder(this)
                                    .setMessage("Η παραγγελία αυτή έχει ολοκληρωθεί, δεν έχετε δικαίωμα επεξεργασίας")
                                    .setNeutralButton("OK", (dialog, which) -> {

                                    })
                                    .show());
                    break;
            }
        }).start();
    }
}

