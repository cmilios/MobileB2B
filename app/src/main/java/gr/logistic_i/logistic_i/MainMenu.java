package gr.logistic_i.logistic_i;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    private ArrayList<String> itemnames = new ArrayList<>();
    private ArrayList<String> itemq = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.main_menu_list_adapter);

        recyclerView.setHasFixedSize(true);
        MainMenuAdapter adapter = new MainMenuAdapter( itemnames, itemq,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}
