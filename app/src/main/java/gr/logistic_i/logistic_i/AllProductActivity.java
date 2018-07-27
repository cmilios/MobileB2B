package gr.logistic_i.logistic_i;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Objects;


public class AllProductActivity extends AppCompatActivity {

    private Toolbar t;
    private MostOrderedItemsAdapter adapter;
    private  String url;
    private  String refid;
    private  String clid;
    ArrayList<MtrLine> mtrLines;
    ArrayList<Mtrl> mtrList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        t.findViewById(R.id.allp_toolbar);
        t.setTitle("All Products");
        setSupportActionBar(t);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        t.setNavigationOnClickListener(v -> onBackPressed());
        StoreVars();
        initRecyclerView();




    }

    public void StoreVars(){
        Intent i = getIntent();
        url = i.getStringExtra("url");
        clid = i.getStringExtra("clid");
        refid = i.getStringExtra("refid");
        mtrLines = i.getParcelableArrayListExtra("lines");
    }

    public void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.details_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MostOrderedItemsAdapter(this, mtrList, url, clid, refid, mtrLines);
        recyclerView.setAdapter(adapter);

    }
}
