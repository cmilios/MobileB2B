package gr.logistic_i.logistic_i;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MostOrderedItems extends PortraitActivity {

    private ArrayList<MtrLine> mtrLines = new ArrayList<>();
    String url = new String();
    String refid = new String();
    String clientid = new String();
    ArrayList<Mtrl> mtrList = new ArrayList<>();
    private Intent i;
    android.support.v7.widget.Toolbar toolbarmostord;

    private MostOrderedItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.most_ordered_items);
        toolbarmostord = (android.support.v7.widget.Toolbar) findViewById(R.id.mostordtool);
        setSupportActionBar(toolbarmostord);
        getSupportActionBar().setTitle("Most Ordered Items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        i = getIntent();
        storeParams();

        GsonWorker gsonWorker = new GsonWorker(url);
        initRecyclerView();
        new Thread(() -> {
            MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "GetCustomerFrequentlyOrderedItems", refid, url);
            gsonWorker.getFOI(mtrlReq);
            mtrList = gsonWorker.getMtrList();
            //double dataset replace  in order to display interface earlier
            adapter.replaceList(mtrList);
            runOnUiThread((adapter::notifyDataSetChanged));
            for (Mtrl m : mtrList) {
                m.loadImage();
            }
            adapter.replaceList(mtrList);
            runOnUiThread((adapter::notifyDataSetChanged));


        }).start();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.details_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MostOrderedItemsAdapter(this, mtrList, url, clientid, refid, mtrLines);
        recyclerView.setAdapter(adapter);

    }

    public void confirmVoucher(View view) {
        Intent i = new Intent(this, ConfirmVoucher.class);

        i.putParcelableArrayListExtra("lines", mtrLines);
//        i.putExtra("url", url);
//        i.putExtra("refid", refid);
//        i.putExtra("clid", clientid);


        this.startActivity(i);

    }

    private void storeParams() {

        mtrLines = i.getParcelableArrayListExtra("lines");
        url = i.getStringExtra("url");
        refid = i.getStringExtra("refid");
        clientid = i.getStringExtra("clid");


    }

    @Override
    public void onBackPressed() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Θέλετε να ακυωσετε την καταχώρηση νέου παραστατικού")
                .setPositiveButton("ΝΑΙ", (arg0, arg1) -> {


                    if (mtrLines != null) {
                        mtrLines.clear();


                    }
                    Intent i = new Intent(this, MainMenuActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("clID", clientid);
                    i.putExtra("url", url);
                    i.putExtra("refid", refid);
                    this.startActivity(i);
                    finish();


                })
                .setNegativeButton("ΟΧΙ", (arg0, arg1) -> {
                })
                .show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mostordered, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        String msg=" ";
        switch (item.getItemId()){
            case R.id.confirmVoucher:
                Intent i = new Intent(this, ConfirmVoucher.class);

                i.putParcelableArrayListExtra("lines", mtrLines);
//              i.putExtra("url", url);
//              i.putExtra("refid", refid);
//              i.putExtra("clid", clientid);


                this.startActivity(i);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}

