package gr.logistic_i.logistic_i;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AddProductActivity extends PortraitActivity {



    ArrayList<MtrLine> mtrLines = new ArrayList<>();
    Mtrl mtrl = new Mtrl(null,null,null,null,null,null, null);
    TextView title;
    ImageView mtrlIcon;
    TextView code;
    TextView manufacturer;
    TextView mtrunit;
    EditText qty;
    String cameFrom;
    String url;
    String refid;
    String clientid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        title = findViewById(R.id.title);
        manufacturer = findViewById(R.id.manufacturer);
        code = findViewById(R.id.mtrlcode);
        mtrlIcon = findViewById(R.id.image_icon1);
        mtrunit = findViewById(R.id.mtrunit);
        qty = findViewById(R.id.selected_qty);


        storeVariables();
        setViews();

        mtrlIcon.setOnClickListener(v -> {

            Drawable d = mtrl.getImage();
            Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();

            Intent intent = new Intent(this, ShowImage.class);
            intent.putExtra("picture", b);
            startActivity(intent);

        });


    }


    private void storeVariables(){
        Intent i = getIntent();
        cameFrom = i.getStringExtra("id");

        mtrl = i.getParcelableExtra("mtrl");
        mtrLines = i.getParcelableArrayListExtra("lines");
        url = i.getStringExtra("url");
        refid = i.getStringExtra("refid");
        clientid = i.getStringExtra("clid");

    }

    public void setViews(){
        new Thread( ()->{
            mtrl.loadImage();
            runOnUiThread(() -> mtrlIcon.setImageDrawable(mtrl.getImage()));
        }).start();

        title.setText(mtrl.getName());
        code.setText(mtrl.getCode());
        manufacturer.setText(mtrl.getManufacturer());
        mtrunit.setText(mtrl.getMtrunit());


    }




    public void addMtrline(View view){
        MtrLine line = new MtrLine(mtrl.getCode(),mtrl.getName(),qty.getText().toString(),qty.getText().toString(), null,null,null,null);

        if (mtrLines == null){
            mtrLines = new ArrayList<>();

        }
        mtrLines.add(line);
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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


}
