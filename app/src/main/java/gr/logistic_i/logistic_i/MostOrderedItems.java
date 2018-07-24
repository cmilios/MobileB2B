package gr.logistic_i.logistic_i;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

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
        toolbarmostord = findViewById(R.id.mostordtool);
        setSupportActionBar(toolbarmostord);
        getSupportActionBar().setTitle("Most Ordered Items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarmostord.setNavigationOnClickListener(v -> onBackPressed());
        i = getIntent();
        storeParams();

        GsonWorker gsonWorker = new GsonWorker(url);
        initRecyclerView();
        new Thread(() -> {
            MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "GetCustomerFrequentlyOrderedItems", refid, url);
            gsonWorker.getFOI(mtrlReq);
            mtrList = gsonWorker.getMtrList();

            for (Mtrl m:mtrList) {
                new Thread(() -> {
                    m.loadImage();
                    adapter.replaceList(mtrList);
                    runOnUiThread((adapter::notifyDataSetChanged));
                }).start();
            }
            adapter.replaceList(mtrList);
            runOnUiThread((adapter::notifyDataSetChanged));
            for (Mtrl m : mtrList) {
                m.loadImage();
            }
        }).start();

        View slideView = findViewById(R.id.slideView);
        FloatingActionButton fab = findViewById(R.id.fabsee);
        View dim = findViewById(R.id.dim);

        SlideUp slideUp = new SlideUpBuilder(slideView)
                .withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.BOTTOM)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                        dim.setAlpha(1 - (percent / 100));
                    }
                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE && fab.getVisibility() == View.GONE){
                            fab.show();
                        }
                    }
                })
                .build();
        slideUp.hideImmediately();
        fab.setOnClickListener(v -> {
            fab.hide();
            slideUp.show();
            if (mtrLines!=null){
                initBasketRV();
            }


        });



    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.details_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MostOrderedItemsAdapter(this, mtrList, url, clientid, refid, mtrLines);
        recyclerView.setAdapter(adapter);

    }

    private void initBasketRV(){
        RecyclerView rv = findViewById(R.id.basket_rview);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        BasketAdapter ad = new BasketAdapter(mtrLines, this);
        rv.setAdapter(ad);

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
                .setMessage("Θέλετε να ακυρώσετε την καταχώρηση νέου παραστατικού")
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
        switch (item.getItemId()){
            case R.id.confirmVoucher:
                Intent i = new Intent(this, ConfirmVoucher.class);

                i.putParcelableArrayListExtra("lines", mtrLines);
                i.putExtra("url", url);
                i.putExtra("refid", refid);
                i.putExtra("clid", clientid);
                i.putParcelableArrayListExtra("mtrl", mtrList);



                this.startActivity(i);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}

