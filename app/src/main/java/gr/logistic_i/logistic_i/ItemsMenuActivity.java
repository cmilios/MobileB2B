package gr.logistic_i.logistic_i;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
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
    private LinearLayoutManager MyLayoutManager;
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
        BootstrapButton confirmButton = findViewById(R.id.confirm);
        refLayout = findViewById(R.id.refreshLayout);


        refLayout.setRefreshHeader(new ClassicsHeader(this));
        refLayout.setRefreshFooter(new ClassicsFooter(this));
        refLayout.setOnLoadMoreListener(refreshLayout -> {
            s.setClickable(false);

            if (isChecked) {
                counter+=50;
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "GetCustomerFrequentlyOrderedItems", refid,counter, url);
                    ArrayList<Mtrl> results = gsonWorker.getFOI(mtrlReq);
                    mtrList.addAll(results);
                    adapter.replaceList(mtrList);
                    runOnUiThread(adapter::notifyDataSetChanged);
                    if (results.size()<50){
                        runOnUiThread(refreshLayout::finishLoadMoreWithNoMoreData);
                    }
                    else{
                        runOnUiThread(()->refreshLayout.finishLoadMore(1000));
                    }
                    runOnUiThread(()->s.setClickable(true));
                }).start();
            } else {
                counterall+=50;
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "FindProductsByName", searchString,counterall, url);
                    ArrayList<Mtrl> results = gsonWorker.getFOI(mtrlReq);
                    mtrList.addAll(results);
                    adapter.replaceList(mtrList);
                    runOnUiThread(adapter::notifyDataSetChanged);
                    if (results.size()<50){
                        runOnUiThread(refreshLayout::finishLoadMoreWithNoMoreData);
                    }
                    else{
                        runOnUiThread(()->refreshLayout.finishLoadMore(1000));
                    }
                    runOnUiThread(()->s.setClickable(true));
                }).start();
            }
        });
        refLayout.setOnRefreshListener(refreshlayout -> {
            s.setClickable(false);
            if (isChecked) {
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "GetCustomerFrequentlyOrderedItems", refid,0, url);
                    ArrayList<Mtrl> results = gsonWorker.getFOI(mtrlReq);
                    mtrList.clear();
                    mtrList.addAll(results);
                    adapter.replaceList(mtrList);
                    runOnUiThread((adapter::notifyDataSetChanged));
                    runOnUiThread(()->refLayout.finishRefresh(1000));
                    runOnUiThread(()->s.setClickable(true));
                }).start();
            } else {
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "FindProductsByName", " ",0, url);
                    ArrayList<Mtrl> results = gsonWorker.getFOI(mtrlReq);
                    mtrList.clear();
                    mtrList.addAll(results);
                    adapter.replaceList(mtrList);
                    runOnUiThread(adapter::notifyDataSetChanged);
                    runOnUiThread(()->refLayout.finishRefresh(2000));
                    runOnUiThread(()->s.setClickable(true));
                }).start();
            }
        });
        storeParams();
        clearmtrlines.setOnClickListener(v -> {
            if (mtrLines != null) {
                mtrLines.clear();
                ((App)this.getApplication()).setMtrLines(mtrLines);
                ad.notifyDataSetChanged();
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                recyclerView.setAdapter(null);
                recyclerView.setAdapter(adapter);
                confirmButton.setEnabled(false);
                confirmButton.setFocusable(false);
                confirmButton.setClickable(false);
            }
        });
        confirmButton.setOnClickListener(v -> {
            if (mtrLines != null && !(mtrLines.size() == 0)) {
                Intent i = new Intent(ItemsMenuActivity.this, ConfirmVoucher.class);
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
                            if (!(MyLayoutManager.findFirstCompletelyVisibleItemPosition()==0)){
                                gotfab.show();
                            }


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
                confirmButton.setEnabled(true);
                confirmButton.setClickable(true);
                confirmButton.setFocusable(true);

            }
            if (mtrLines == null){
                confirmButton.setEnabled(false);
                confirmButton.setClickable(false);
                confirmButton.setFocusable(false);
            }
        });
        gotfab.setOnClickListener(v -> recyclerView.smoothScrollToPosition(0));

        setSearchListeners();
    }

    private void initRecyclerView() {
        MyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.details_list);
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
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getApplicationContext(), AddProductActivity.class);
                i.putExtra("mtrl",mtrList.get(position));
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        adapter = new ItemsMenuAdapter(this, mtrList, mtrLines);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(MyLayoutManager);

    }

    private void initBasketRV() {
        RecyclerView rv = findViewById(R.id.basket_rview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        ad = new BasketAdapter(mtrLines, this);
        rv.setAdapter(ad);
    }

    private void storeParams() {

        mtrLines = ((App)this.getApplication()).getMtrLines();
        url = ((App)this.getApplication()).getUrl();
        refid = ((App)this.getApplication()).getRefID();
        clientid = ((App)this.getApplication()).getClientID();


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
                        ((App)this.getApplication()).setMtrLines(mtrLines);


                    }
                    Intent i = new Intent(this, MainMenuActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        initRecyclerView();
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


        s.setOnCheckedChangeListener((buttonView, isChecked) -> {

            this.isChecked = isChecked;
            refLayout.setNoMoreData(false);


            //reset adapter so all data clear
            mtrList.clear();
            adapter = new ItemsMenuAdapter(this, mtrList, mtrLines);
            recyclerView.setAdapter(adapter);


            if (isChecked) {
                counter=0;
                Objects.requireNonNull(getSupportActionBar()).setTitle("Τα είδη μου");
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "GetCustomerFrequentlyOrderedItems", refid,counter, url);
                    mtrList = gsonWorker.getFOI(mtrlReq);
                    adapter.replaceList(mtrList);
                    runOnUiThread((adapter::notifyDataSetChanged));
                }).start();
            } else {
                counterall=0;
                Objects.requireNonNull(getSupportActionBar()).setTitle("Είδη αποθήκης");
                GsonWorker gsonWorker = new GsonWorker(url);
                new Thread(() -> {
                    MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "FindProductsByName", " ",counterall, url);
                    mtrList = gsonWorker.getFOI(mtrlReq);
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
                mtrList.clear();
                adapter = new ItemsMenuAdapter(getApplicationContext(), mtrList, mtrLines);
                recyclerView.setAdapter(adapter);
                refLayout.setNoMoreData(false);
                searchString = query;
                if (isChecked) {
                    GsonWorker gson = new GsonWorker(url);
                    new Thread(() -> {
                        MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "GetCustomerFrequentlyOrderedItems", refid,0, url);
                        ArrayList<Mtrl> results = gson.getFOI(mtrlReq);
                        mtrList.clear();
                        adapter.replaceList(mtrList);
                        runOnUiThread((adapter::notifyDataSetChanged));
                        ArrayList<Mtrl> sResults = new ArrayList<>();
                        for (Mtrl m:results){
                            if(m.getName().toLowerCase().contains(searchString.toLowerCase())){
                                sResults.add(m);
                            }
                        }
                        mtrList = sResults;

                        adapter.replaceList(mtrList);
                        runOnUiThread((adapter::notifyDataSetChanged));
                    }).start();
                } else {
                    GsonWorker gson = new GsonWorker(url);
                    new Thread(() -> {
                        MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "FindProductsByName", query,0, url);
                        ArrayList<Mtrl> results = gson.getFOI(mtrlReq);
                        mtrList.clear();
                        adapter.replaceList(mtrList);
                        runOnUiThread((adapter::notifyDataSetChanged));
                        mtrList.addAll(results);
                        adapter.replaceList(mtrList);
                        runOnUiThread(adapter::notifyDataSetChanged);
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


    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }
}

