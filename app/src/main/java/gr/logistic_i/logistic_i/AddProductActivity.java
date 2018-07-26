package gr.logistic_i.logistic_i;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class AddProductActivity extends PortraitActivity {


    private ArrayList<MtrLine> mtrLines = new ArrayList<>();
    private Mtrl mtrl = new Mtrl(null,null, null, null, null, null, null, null, null, null, null, null);
    private TextView title;
    private ImageView mtrlIcon;
    private TextView code;
    private TextView manufacturer;
    private Spinner unitsp;
    private EditText qty;
    private String cameFrom;
    private String url;
    private String refid;
    private String clientid;
    private HashMap<Integer, String> unitlist = new HashMap<>();
    private ArrayList<String> showList = new ArrayList<>();
    private ImageView backimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        title = findViewById(R.id.title);
        manufacturer = findViewById(R.id.manufacturer);
        code = findViewById(R.id.mtrlcode);
        mtrlIcon = findViewById(R.id.image_icon1);
        qty = findViewById(R.id.selected_qty);
        unitsp = findViewById(R.id.unitspn);
        backimg = findViewById(R.id.back_img);
        storeVariables();
        setViews();
        mtrlIcon.setOnClickListener(v -> {
            Drawable d = mtrl.getImage();
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            Intent intent = new Intent(this, ShowImage.class);
            intent.putExtra("picture", b);
            startActivity(intent);
        });
    }

    private void storeVariables() {
        Intent i = getIntent();
        cameFrom = i.getStringExtra("id");

        mtrl = i.getParcelableExtra("mtrl");
        mtrLines = i.getParcelableArrayListExtra("lines");
        url = i.getStringExtra("url");
        refid = i.getStringExtra("refid");
        clientid = i.getStringExtra("clid");

    }

    public void setViews() {
        new Thread(() -> {
            mtrl.loadImage();
            runOnUiThread(() -> mtrlIcon.setImageDrawable(mtrl.getImage()));
        }).start();


        backimg.setOnClickListener(v -> onBackPressed());

        title.setText(mtrl.getName());
        code.setText(mtrl.getCode());
        manufacturer.setText(mtrl.getManufacturer());
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
                    if (mtrl.getUnitList().indexOf(s) == m.getmUnit()){
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
            if (m.getCode().equals(mtrl.getCode())){
                itemExistsFlag = true;
                if (qty.getText().toString().equals("0") || qty.getText().toString().equals("")){
                    indexOfItemToDelete = mtrLines.indexOf(m);
                    toDeleteFlag=true;
                }
                else{
                    m.setQty(mtrl.getQuantityToFirstMtrUnit(index,qty.getText().toString()));
                    m.setQty1(qty.getText().toString());
                    m.setsUnit(unitsp.getSelectedItem().toString());
                    m.setmUnit(index);
                }

            }
        }
        if(toDeleteFlag){
            mtrLines.remove(mtrLines.get(indexOfItemToDelete));
        }



        if (!qty.getText().toString().equals("") && !qty.getText().toString().equals("0") && !itemExistsFlag) {
            line = new MtrLine(mtrl.getMtrl(), mtrl.getCode(),mtrl.getName(),mtrl.getQuantityToFirstMtrUnit(index,qty.getText().toString()),qty.getText().toString(), null,null,null,null, index, unit);
            mtrLines.add(line);
        }






        Intent i = null;
        try {
            i = new Intent(this, Class.forName("gr.logistic_i.logistic_i." + cameFrom));
            i.putExtra("id", this.getClass().getSimpleName());
            i.putParcelableArrayListExtra("lines", mtrLines);
            i.putExtra("url", url);
            i.putExtra("refid", refid);
            i.putExtra("clid", clientid);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        startActivity(i);
        finish();


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
        Intent i = null;
        try {
            i = new Intent(this, Class.forName("gr.logistic_i.logistic_i." + cameFrom));
            i.putExtra("id", this.getClass().getSimpleName());
            i.putParcelableArrayListExtra("lines", mtrLines);
            i.putExtra("url", url);
            i.putExtra("refid", refid);
            i.putExtra("clid", clientid);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        startActivity(i);
        finish();

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




        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, showList);

        unitsp.setAdapter(adapter);
        if (showList.size()==1) {
            unitsp.setClickable(false);
            unitsp.setFocusable(false);
            unitsp.setEnabled(false);

        }

    }
}
