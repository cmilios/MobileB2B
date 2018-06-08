package gr.logistic_i.logistic_i;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainMenuActivity extends AppCompatActivity {

    private ArrayList<String> finums = new ArrayList<>();
    private ArrayList<String> dts = new ArrayList<>();
    private String url;
    private String clientID;
    private String sourceDate;
    private LoginActivity loginActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        storeParams();

        GetSqlDataTask g = new GetSqlDataTask(getApplicationContext());
        //todo might need improvement on params to set date pickers
        g.execute(url, "SqlData", clientID, "1100", "Orders", sourceDate);


        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.orderlist);

        recyclerView.setHasFixedSize(true);
        MainMenuAdapter adapter = new MainMenuAdapter(this, finums, dts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    public void storeParams(){
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        clientID = intent.getStringExtra("clID");
    }

    public void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd / MM / yyyy ");
        sourceDate = mdformat.format(calendar.getTime());

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, LoginActivity.class);
        this.startActivity(i);
    }
}
