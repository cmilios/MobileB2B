package gr.logistic_i.logistic_i;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {

    private ArrayList<String> finums = new ArrayList<>();
    private ArrayList<String> dts = new ArrayList<>();
    private String url = new String();
    private String clientID = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        GetSqlDataTask g = new GetSqlDataTask(getApplicationContext());
        //todo might need improvement on params to set date pickers
        g.execute(url, "SqlData", clientID, "1100", "Orders");

        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.orderlist);

        recyclerView.setHasFixedSize(true);
        MainMenuAdapter adapter = new MainMenuAdapter(this, finums, dts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    public void storeParam(String url, String clientID){
        this.url = url;
        this.clientID = clientID;

    }
}
