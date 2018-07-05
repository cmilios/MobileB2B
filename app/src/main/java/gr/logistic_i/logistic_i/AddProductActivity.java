package gr.logistic_i.logistic_i;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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


    }


    private void storeVariables(){
        Intent i = getIntent();
        cameFrom = i.getStringExtra("id");

        mtrl = i.getParcelableExtra("mtrl");
        mtrLines = i.getParcelableArrayListExtra("lines");
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


    public void setMtrlToMtrLines(View view){
        MtrLine line = new MtrLine(mtrl.getCode(),mtrl.getName(),qty.getText().toString(),qty.getText().toString(), null,null,null,null);
        mtrLines.add(line);
        Intent i = new Intent(this,cameFrom.getClass());
        i.putParcelableArrayListExtra("lines", mtrLines);
        this.startActivity(i);



    }
}
