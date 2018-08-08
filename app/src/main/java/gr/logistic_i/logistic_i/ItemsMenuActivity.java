package gr.logistic_i.logistic_i;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
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

    private String searchString = " ";

    private MaterialSearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.most_ordered_items);
        android.support.v7.widget.Toolbar toolbarmostord = findViewById(R.id.mostordtool);
        searchView =findViewById(R.id.search_view);
        gotfab = findViewById(R.id.got_fab);
        setSupportActionBar(toolbarmostord);
        toolbarmostord.setTitle("Είδη αποθήκης");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbarmostord.setNavigationOnClickListener(v -> onBackPressed());
        BootstrapButton clearmtrlines = findViewById(R.id.clearall);
        BootstrapButton cc = findViewById(R.id.confirm);
        refLayout = findViewById(R.id.refreshLayout);
        refLayout.setRefreshHeader(new ClassicsHeader(this));
        refLayout.setRefreshFooter(new ClassicsFooter(this));
        refLayout.setOnLoadMoreListener(refreshLayout -> {
            if (isChecked) {
                counter+=50;
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "GetCustomerFrequentlyOrderedItems", refid,counter, url);
                    ArrayList<Mtrl> results = gsonWorker.getFOI(mtrlReq);
                    for (Mtrl m:results){
                        new Thread(()-> {
                                mtrList.add(m);
                                adapter.replaceList(mtrList);
                                runOnUiThread((adapter::notifyDataSetChanged));
                        }).start();
                    }

                    if (results.size()<50){
                        runOnUiThread(refreshLayout::finishLoadMoreWithNoMoreData);
                    }
                    else{
                        runOnUiThread(refreshLayout::finishLoadMore);
                    }
                }).start();
            } else {
                counterall+=50;
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "FindProductsByName", searchString,counterall, url);
                    ArrayList<Mtrl> results = gsonWorker.getFOI(mtrlReq);
                    for (Mtrl m:results){
                        new Thread(()-> {
                            mtrList.add(m);
                            adapter.replaceList(mtrList);
                            runOnUiThread((adapter::notifyDataSetChanged));
                        }).start();
                    }
                    if (results.size()<50){
                        runOnUiThread(refreshLayout::finishLoadMoreWithNoMoreData);
                    }
                    else{
                        runOnUiThread(refreshLayout::finishLoadMore);
                    }
                }).start();
            }
        });
        refLayout.setOnRefreshListener(refreshlayout -> {
            if (isChecked) {
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "GetCustomerFrequentlyOrderedItems", refid,0, url);
                    ArrayList<Mtrl> results = gsonWorker.getFOI(mtrlReq);
                    mtrList.clear();
                    for (Mtrl m:results){
                        new Thread(()-> {
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
                    ArrayList<Mtrl> results = gsonWorker.getFOI(mtrlReq);
                    mtrList.clear();
                    for (Mtrl m:results){
                        new Thread(()-> {
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

        initRecyclerView();
        clearmtrlines.setOnClickListener(v -> {
            if (mtrLines != null) {
                mtrLines.clear();
                ad.notifyDataSetChanged();
                cc.setEnabled(false);
                cc.setFocusable(false);
                cc.setClickable(false);
            }
        });
        cc.setOnClickListener(v -> {
            if (mtrLines != null && !(mtrLines.size() == 0)) {
                Intent i = new Intent(ItemsMenuActivity.this, ConfirmVoucher.class);
                i.putParcelableArrayListExtra("lines", mtrLines);
                i.putExtra("url", url);
                i.putExtra("refid", refid);
                i.putExtra("clid", clientid);
                startActivity(i);

            } else {
                new AlertDialog.Builder(ItemsMenuActivity.this)
                        .setMessage("Το καλάθι σας ειναι άδειο, προσθέστε προιόντα στο καλάθι.")
                        .setNeutralButton("ΟΚ", (dialog, which) -> {

                        })
                        .show();
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
                            gotfab.show();
                        }
                    }
                })
                .build();
        slideUp.hideImmediately();
        fab.setOnClickListener(v -> {
            fab.hide();
            gotfab.hide();
            slideUp.show();
            if (mtrLines != null) {
                initBasketRV();
                cc.setEnabled(true);
                cc.setClickable(true);
                cc.setFocusable(true);

            }
            if (mtrLines == null){
                cc.setEnabled(false);
                cc.setClickable(false);
                cc.setFocusable(false);
            }
        });
        gotfab.setOnClickListener(v -> recyclerView.smoothScrollToPosition(0));

        setSearchListeners();
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
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        }
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
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;

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
                mtrList = gsonWorker.getFOI(mtrlReq);
                adapter.replaceList(mtrList);
                runOnUiThread((adapter::notifyDataSetChanged));
            }).start();
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Είδη αποθήκης");
            GsonWorker gsonWorker = new GsonWorker(url);
            new Thread(() -> {
                MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "FindProductsByName", " ",0, url);
                mtrList = gsonWorker.getFOI(mtrlReq);
                adapter.replaceList(mtrList);
                runOnUiThread((adapter::notifyDataSetChanged));
            }).start();
        }
        s.setOnClickListener(v -> {
            isChecked = !isChecked;
            checkable.setChecked(isChecked);
            s.setChecked(isChecked);
            if (isChecked) {
                counter=0;
                Objects.requireNonNull(getSupportActionBar()).setTitle("Τα είδη μου");
                refLayout.setNoMoreData(false);
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "GetCustomerFrequentlyOrderedItems", refid,counter, url);
                    mtrList = gsonWorker.getFOI(mtrlReq);
                        new Thread(() -> {
                            ArrayList<Mtrl>buffer = mtrList;
                            adapter.replaceList(buffer);
                            runOnUiThread((adapter::notifyDataSetChanged));
                        }).start();
                    adapter.replaceList(mtrList);
                    runOnUiThread((adapter::notifyDataSetChanged));
                }).start();
            } else {
                counterall=0;
                Objects.requireNonNull(getSupportActionBar()).setTitle("Είδη αποθήκης");
                refLayout.setNoMoreData(false);
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "FindProductsByName", " ",counterall, url);
                    mtrList = gsonWorker.getFOI(mtrlReq);
                        new Thread(() -> {
                            ArrayList<Mtrl> buffer = mtrList;
                            adapter.replaceList(buffer);
                            runOnUiThread((adapter::notifyDataSetChanged));
                        }).start();
                    adapter.replaceList(mtrList);
                    runOnUiThread((adapter::notifyDataSetChanged));

                }).start();
            }
        });
        return true;
    }

    private void setSearchListeners(){
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchString = query;
                if (isChecked) {
                    GsonWorker gson = new GsonWorker(url);
                    new Thread(() -> {
                        MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "GetCustomerFrequentlyOrderedItems", refid,0, url);
                        ArrayList<Mtrl> results = gson.getFOI(mtrlReq);
                        mtrList.clear();
                        adapter.replaceList(mtrList);
                        runOnUiThread((adapter::notifyDataSetChanged));
                        for (Mtrl m:results){
                            new Thread(()-> {
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
                    GsonWorker gson = new GsonWorker(url);
                    new Thread(() -> {
                        MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "FindProductsByName", query,0, url);
                        ArrayList<Mtrl> results = gson.getFOI(mtrlReq);
                        mtrList.clear();
                        adapter.replaceList(mtrList);
                        runOnUiThread((adapter::notifyDataSetChanged));
                        for (Mtrl m: results){
                            new Thread(()-> {
                                mtrList.add(m);
                                adapter.replaceList(mtrList);
                                runOnUiThread((adapter::notifyDataSetChanged));
                            }).start();
                        }
                    }).start();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        searchView.setVoiceSearch(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}

