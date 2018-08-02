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
import android.widget.Button;
import android.widget.Switch;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import java.util.ArrayList;
import java.util.Objects;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class ItemsMenuActivity extends PortraitActivity {

    private ArrayList<MtrLine> mtrLines = new ArrayList<>();
    private String url;
    private String refid;
    private String clientid;
    private ArrayList<Mtrl> mtrList = new ArrayList<>();
    private Intent i;
    private BasketAdapter ad;
    private boolean isChecked = true;
    private Switch s;
    private ItemsMenuAdapter adapter;
    private RefreshLayout refLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private FloatingActionButton gotfab;
    private long counter=0;
    private long counterall=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.most_ordered_items);
        android.support.v7.widget.Toolbar toolbarmostord = findViewById(R.id.mostordtool);
        setSupportActionBar(toolbarmostord);
        toolbarmostord.setTitle("Είδη αποθήκης");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbarmostord.setNavigationOnClickListener(v -> onBackPressed());
        Button clearmtrlines = findViewById(R.id.clearall);
        refLayout = findViewById(R.id.refreshLayout);
        refLayout.setRefreshHeader(new ClassicsHeader(this));
        refLayout.setRefreshFooter(new ClassicsFooter(this));


        refLayout.setOnLoadMoreListener(refreshLayout -> {
            if (isChecked) {
                counter+=50;
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "GetCustomerFrequentlyOrderedItems", refid,counter, url);
                    gsonWorker.getFOI(mtrlReq);
                    for (Mtrl m:gsonWorker.getMtrList()){
                        new Thread(()-> {
                                m.loadImage();
                                mtrList.add(m);
                        }).start();
                    }
                    adapter.replaceList(mtrList);
                    runOnUiThread((adapter::notifyDataSetChanged));
                    runOnUiThread(refreshLayout::finishLoadMore);
                }).start();
            } else {
                counterall+=50;
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "FindProductsByName", " ",counterall, url);
                    gsonWorker.getFOI(mtrlReq);
                    for (Mtrl m:gsonWorker.getMtrList()){
                        new Thread(()-> {
                            m.loadImage();
                            mtrList.add(m);
                        }).start();
                    }
                    adapter.replaceList(mtrList);
                    runOnUiThread((adapter::notifyDataSetChanged));
                    runOnUiThread(refreshLayout::finishLoadMore);
                }).start();
            }
        });



        refLayout.setOnRefreshListener(refreshlayout -> {
            if (isChecked) {
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "GetCustomerFrequentlyOrderedItems", refid,0, url);
                    gsonWorker.getFOI(mtrlReq);
                    mtrList.clear();
                    for (Mtrl m:gsonWorker.getMtrList()){
                        new Thread(()-> {
                            m.loadImage();
                            mtrList.add(m);
                            adapter.replaceList(mtrList);
                            runOnUiThread((adapter::notifyDataSetChanged));
                        }).start();
                    }
                    adapter.replaceList(mtrList);
                    runOnUiThread((adapter::notifyDataSetChanged));
                    runOnUiThread(refLayout::finishRefresh);
                }).start();
            } else {
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "FindProductsByName", " ",0, url);
                    gsonWorker.getFOI(mtrlReq);
                    mtrList.clear();
                    for (Mtrl m:gsonWorker.getMtrList()){
                        new Thread(()-> {
                            m.loadImage();
                            mtrList.add(m);
                            adapter.replaceList(mtrList);
                            runOnUiThread((adapter::notifyDataSetChanged));
                        }).start();
                    }

                    runOnUiThread(refLayout::finishRefresh);
                }).start();
            }



        });
        i = getIntent();
        storeParams();
        gotfab = findViewById(R.id.got_fab);
        initRecyclerView();
        clearmtrlines.setOnClickListener(v -> {
            if (mtrLines != null) {
                mtrLines.clear();
                ad.notifyDataSetChanged();
            }
        });

        View slideView = findViewById(R.id.slideView);
        fab = findViewById(R.id.fabsee);
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
                        if (visibility == View.GONE && fab.getVisibility() == View.GONE) {
                            fab.show();
                        }
                    }
                })
                .build();
        slideUp.hideImmediately();
        fab.setOnClickListener(v -> {
            fab.hide();
            slideUp.show();
            if (mtrLines != null) {
                initBasketRV();
            }
        });
        gotfab.setOnClickListener(v -> recyclerView.smoothScrollToPosition(0));


    }

    private void initRecyclerView() {
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.details_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState ==  SCROLL_STATE_IDLE){
                    s.setClickable(true);
                    if (fab.getVisibility()!=View.VISIBLE){
                        fab.show();
                    }
                }
                else{
                    s.setClickable(false);
                    fab.hide();
                }



            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibility = (MyLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) ? View.VISIBLE : View.GONE;
                gotfab.setVisibility(visibility);
            }

        });





        recyclerView.setLayoutManager(MyLayoutManager);
        adapter = new ItemsMenuAdapter(this, mtrList, url, clientid, refid, mtrLines, isChecked);
        recyclerView.setAdapter(adapter);
    }

    private void initBasketRV() {
        RecyclerView rv = findViewById(R.id.basket_rview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ad = new BasketAdapter(mtrLines, this);
        rv.setAdapter(ad);
    }

    private void storeParams() {

        mtrLines = i.getParcelableArrayListExtra("lines");
        url = i.getStringExtra("url");
        refid = i.getStringExtra("refid");
        clientid = i.getStringExtra("clid");
        isChecked = i.getBooleanExtra("isChecked", false);


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirmVoucher:
                if (mtrLines != null && !(mtrLines.size() == 0)) {
                    Intent i = new Intent(this, ConfirmVoucher.class);
                    i.putParcelableArrayListExtra("lines", mtrLines);
                    i.putExtra("url", url);
                    i.putExtra("refid", refid);
                    i.putExtra("clid", clientid);
                    i.putParcelableArrayListExtra("mtrl", mtrList);
                    this.startActivity(i);
                    return true;

                } else {
                    new AlertDialog.Builder(this)
                            .setMessage("Το καλάθι σας ειναι άδειο, προσθέστε προιόντα στο καλάθι.")
                            .setNeutralButton("ΟΚ", (dialog, which) -> {

                            })
                            .show();
                    return true;
                }

            default:
                return false;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable = menu.findItem(R.id.app_bar_switch);
        checkable.setChecked(isChecked);
        s = (Switch) checkable.getActionView();
        s.setChecked(isChecked);
        if (isChecked) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Τα είδη μου");
            GsonWorker gsonWorker = new GsonWorker(url);
            new Thread(() -> {
                MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "GetCustomerFrequentlyOrderedItems", refid,0, url);
                gsonWorker.getFOI(mtrlReq);
                mtrList = gsonWorker.getMtrList();

                for (Mtrl m : mtrList) {
                    new Thread(() -> {
                        m.loadImage();
                        adapter.replaceList(mtrList);
                        runOnUiThread((adapter::notifyDataSetChanged));
                    }).start();
                }
                adapter.replaceList(mtrList);
                runOnUiThread((adapter::notifyDataSetChanged));
            }).start();
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Είδη αποθήκης");
            GsonWorker gsonWorker = new GsonWorker(url);
            new Thread(() -> {
                MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "FindProductsByName", " ",0, url);
                gsonWorker.getFOI(mtrlReq);
                mtrList = gsonWorker.getMtrList();

                for (Mtrl m : mtrList) {
                    new Thread(() -> {
                        m.loadImage();
                        adapter.replaceList(mtrList);
                        runOnUiThread((adapter::notifyDataSetChanged));
                    }).start();
                }
                adapter.replaceList(mtrList);
                runOnUiThread((adapter::notifyDataSetChanged));
            }).start();
        }
        s.setOnClickListener(v -> {
            isChecked = !isChecked;
            checkable.setChecked(isChecked);
            s.setChecked(isChecked);
            if (isChecked) {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Τα είδη μου");
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "GetCustomerFrequentlyOrderedItems", refid,0, url);
                    gsonWorker.getFOI(mtrlReq);
                    mtrList = gsonWorker.getMtrList();

                    for (Mtrl m : mtrList) {
                        new Thread(() -> {
                            m.loadImage();
                            ArrayList<Mtrl>buffer = mtrList;
                            adapter.replaceList(buffer);
                            runOnUiThread((adapter::notifyDataSetChanged));
                        }).start();
                    }
                    adapter.replaceList(mtrList);
                    runOnUiThread((adapter::notifyDataSetChanged));
                }).start();
            } else {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Είδη αποθήκης");
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "FindProductsByName", " ",0, url);
                    gsonWorker.getFOI(mtrlReq);
                    mtrList = gsonWorker.getMtrList();

                    for (Mtrl m : mtrList) {
                        new Thread(() -> {
                            m.loadImage();
                            ArrayList<Mtrl> buffer = mtrList;
                            adapter.replaceList(buffer);
                            runOnUiThread((adapter::notifyDataSetChanged));
                        }).start();
                    }
                    adapter.replaceList(mtrList);
                    runOnUiThread((adapter::notifyDataSetChanged));

                }).start();
            }
        });
        return true;
    }
}

