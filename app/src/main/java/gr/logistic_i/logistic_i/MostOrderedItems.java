package gr.logistic_i.logistic_i;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class MostOrderedItems extends PortraitActivity {

    ArrayList<String> selectedMtrl = new ArrayList<>();
    ArrayList<String> selectedQty = new ArrayList<>();
    String url = new String();
    String refid = new String();
    String clientid = new String();
    ArrayList<Mtrl> mtrList = new ArrayList<>();

    private MostOrderedItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.most_ordered_items);
        storeParams();

        GsonWorker gsonWorker = new GsonWorker(url);
        initRecyclerView();
        new Thread(()->{
            MtrlReq mtrlReq = new MtrlReq("SqlData", clientid, "1100", "GetCustomerFrequentlyOrderedItems", refid);
            gsonWorker.getFOI(mtrlReq);
            mtrList = gsonWorker.getMtrList();
            adapter.replaceList(mtrList);
            runOnUiThread((adapter::notifyDataSetChanged));




        }).start();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.details_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MostOrderedItemsAdapter(this, mtrList, url, clientid);
        recyclerView.setAdapter(adapter);

    }

    public void confirmVoucher(View view){
        Intent i = new Intent(this, ConfirmVoucher.class);
        i.putStringArrayListExtra("mtrls", selectedMtrl);
        i.putStringArrayListExtra("qtys", selectedQty);

        this.startActivity(i);

    }

    private void storeParams(){
        Intent i = getIntent();
        url = i.getStringExtra("url");
        refid = i.getStringExtra("refid");
        clientid = i.getStringExtra("clid");

    }


}
