package gr.logistic_i.logistic_i;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class AddProductActivity extends PortraitActivity {


    private ArrayList<MtrLine> mtrLines = new ArrayList<>();
    private Mtrl mtrl = new Mtrl(null,null, null, null, null, null, null, null, null, null, null, null, null);
    private TextView title;
    private SimpleDraweeView mtrlIcon;
    private TextView code;
    private TextView manufacturer;
    private Spinner unitsp;
    private TextInputEditText qty;
    private String url;
    private String clientid;
    private HashMap<Integer, String> unitlist = new HashMap<>();
    private ArrayList<String> showList = new ArrayList<>();
    private ImageView backimg;
    private String wayOfTransormation;
    private String resWay;
    private Uri uri;
    private int unitCode;

    private RelativeLayout rq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        title = findViewById(R.id.title);
        manufacturer = findViewById(R.id.manufacturer);
        code = findViewById(R.id.mtrlcode);
        mtrlIcon = findViewById(R.id.image_icon1);
        qty = findViewById(R.id.selected_qtytxt);
        unitsp = findViewById(R.id.unitspn);
        backimg = findViewById(R.id.back_img);
        rq = findViewById(R.id.rq_f);
        rq.requestFocus();
        storeVariables();
        setViews();
        mtrlIcon.setOnClickListener(v -> {

            Intent intent = new Intent(this, ShowImage.class);
            intent.putExtra("uri", uri.toString());
            startActivity(intent);
        });
    }

    private void storeVariables() {
        Intent i = getIntent();
        mtrl = i.getParcelableExtra("mtrl");
        mtrLines = ((App)this.getApplication()).getMtrLines();
        url = ((App)this.getApplication()).getUrl();
        clientid = ((App)this.getApplication()).getClientID();

    }

    public void setViews() {
        JSONObject jsonObject = serWayOfTransformation();
        new Thread(() -> {
            GsonWorker gsonWorker = new GsonWorker(url);
            resWay = gsonWorker.getWayOfTransformation(jsonObject);
            wayOfTransormation = deserWayOfTransformation(resWay);
        }).start();

        uri = Uri.parse("https://"+mtrl.getCorrespondingBase()+".oncloud.gr//s1services/?filename="+mtrl.getImgURL());
        mtrlIcon.setImageURI(uri);
        mtrlIcon.setController(
                Fresco.newDraweeControllerBuilder()
                        .setTapToRetryEnabled(true)
                        .setUri(uri)
                        .build());



        backimg.setOnClickListener(v -> onBackPressed());

        title.setText(mtrl.getName());
        code.setText(mtrl.getCode());
        if (mtrl.getManufacturer()!=null) {
            manufacturer.setText(mtrl.getManufacturer());
        }
        else{
            TextView kata = findViewById(R.id.kata);
            kata.setVisibility(View.GONE);
            manufacturer.setVisibility(View.GONE);
        }
        checkSpinnerView();


        if(mtrLines == null){
            unitsp.setSelection(mtrl.getUnitList().indexOf(0));
            return;
        }
        String setQ ;
        for (MtrLine m : mtrLines) {
            if (mtrl.getMtrl().equals(m.getMtrl())) {
                setQ = m.getQty1();
                qty.setText(setQ);
                for (String s: mtrl.getUnitList()){
                    if (mtrl.getUnitList().indexOf(s) == m.getUnitSpinnerPosition()){
                        for (Integer key:unitlist.keySet()){
                            if (s.equals(unitlist.get(key))){
                                for (String st: showList){
                                    if (s.equals(st)){
                                            unitsp.setSelection(showList.indexOf(st));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public void addMtrline(View view) {
        Boolean itemExistsFlag = false;
        Boolean toDeleteFlag = false;
        int index = -1;
        int indexOfItemToDelete =0;
        MtrLine line;


        String unit = unitsp.getSelectedItem().toString();
        for (Integer key : unitlist.keySet()) {
            if (unitlist.get(key).equals(unit)) {
                index = key;
            }
        }


        if (mtrLines == null) {
            mtrLines = new ArrayList<>();

        }
        for (MtrLine m :mtrLines){
            if (m.getMtrl()!=null) {
                if (m.getMtrl().equals(mtrl.getMtrl())) {
                    itemExistsFlag = true;
                    if (qty.getText().toString().equals("0") || qty.getText().toString().equals("")) {
                        indexOfItemToDelete = mtrLines.indexOf(m);
                        toDeleteFlag = true;
                    } else {
                        m.setQty(mtrl.getQuantityToFirstMtrUnit(index, qty.getText().toString(), wayOfTransormation));
                        m.setQty1(qty.getText().toString());
                        m.setUnitSelectedName(unitsp.getSelectedItem().toString());
                        m.setUnitSpinnerPosition(index);
                    }

                }
            }
            else{
                if (m.getMtrl().equals(mtrl.getMtrl())) {
                    itemExistsFlag = true;
                    if (qty.getText().toString().equals("0") || qty.getText().toString().equals("")) {
                        indexOfItemToDelete = mtrLines.indexOf(m);
                        toDeleteFlag = true;
                    } else {
                        m.setQty(mtrl.getQuantityToFirstMtrUnit(index, qty.getText().toString(), wayOfTransormation));
                        m.setQty1(qty.getText().toString());
                        m.setUnitSelectedName(unitsp.getSelectedItem().toString());
                        m.setUnitSpinnerPosition(index);
                    }
                }
            }
        }
        if(toDeleteFlag){
            mtrLines.remove(mtrLines.get(indexOfItemToDelete));
        }

        HashMap<Integer, String> unitsMap = mtrl.getUnitsMap();
       for(Integer i:unitsMap.keySet()){
           if (unitsMap.get(i).equals(unit)){
               unitCode = i;
           }
       }




        if (!qty.getText().toString().equals("") && !qty.getText().toString().equals("0") && !itemExistsFlag) {
            line = new MtrLine(mtrl.getMtrl(), mtrl.getCode(),mtrl.getName(),mtrl.getQuantityToFirstMtrUnit(index,qty.getText().toString(),wayOfTransormation),qty.getText().toString(), null,null,null,null, index, unit, unitCode, unitCode);
            line.setLinkedMtrl(mtrl);
            ((App)this.getApplication()).addLineToList(line);

        }
        onBackPressed();


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    rq.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void checkSpinnerView(){


        unitlist.put(0,mtrl.getUnitList().get(0));
        unitlist.put(1, mtrl.getUnitList().get(1));
        unitlist.put(2, mtrl.getUnitList().get(2));

        if (mtrl.getMu21()==null || mtrl.getMu21().equals("0")){
            unitlist.remove(1);
        }
        if (mtrl.getMu41()==null || mtrl.getMu41().equals("0")){
            unitlist.remove(2);
        }

        if (unitlist.containsKey(1) || unitlist.containsKey(2)){
            if (unitlist.get(0).equals(unitlist.get(1))){
                unitlist.remove(1);
            }
            if (unitlist.get(0).equals(unitlist.get(2))){
                unitlist.remove(2);
            }
            if (unitlist.containsKey(2)) {
                if (unitlist.get(1).equals(unitlist.get(2))) {
                    unitlist.remove(2);
                }
            }
        }


        for (Integer key:unitlist.keySet()){
            showList.add(unitlist.get(key));
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, showList);



        unitsp.setAdapter(adapter);
        if (showList.size()==1) {
            unitsp.setClickable(false);
            unitsp.setFocusable(false);
            unitsp.setEnabled(false);
        }

    }

    public JSONObject serWayOfTransformation(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("service", "getData");
            jsonObject.put("clientID", clientid);
            jsonObject.put("appID",1100);
            jsonObject.put("OBJECT", "ITEPPRMS");
            jsonObject.put("KEY", "51");
            jsonObject.put("LOCATEINFO", "ITEPPRMS:MTRMD");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String deserWayOfTransformation(String resObj){
        String parsed = "";
        try {
            JSONObject jsonObject = new JSONObject(resObj);
            parsed = jsonObject.getJSONObject("data").getJSONArray("ITEPPRMS").getJSONObject(0).getString("MTRMD");
            if(parsed.equals("0")){
                parsed = "*";
            }else{
                parsed = "/";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parsed;

    }
}
