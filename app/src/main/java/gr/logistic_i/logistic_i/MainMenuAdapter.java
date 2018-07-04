package gr.logistic_i.logistic_i;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder> {


    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Order> orderlist = new ArrayList<>();
    private Context mContext;
    private String url;
    private String clientId;
    SimpleDateFormat dpformat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sqlResFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private Date formattedDate = new Date();


    public MainMenuAdapter(Context mContext, ArrayList<Order> orderlist, String url, String clientId) {
        this.mContext = mContext;
        this.url = url;
        this.clientId = clientId;
        this.orderlist = orderlist;
    }

    @Override
    public  ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_menu_list_adapter, parent ,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.parnum.setText(orderlist.get(position).getFincode());
        try {
            formattedDate = sqlResFormat.parse(orderlist.get(position).getTrndate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.dt.setText(dpformat.format(formattedDate));
        holder.sumamnt.setText(orderlist.get(position).getSumamnt()+ "€");
        holder.detailsButton.setOnClickListener(v -> {
            Intent i = new Intent(mContext, VoucherDetailsActivity.class);
            i.putExtra("order", orderlist.get(position));
            i.putExtra("url", url);
            i.putExtra("clID", clientId);
            mContext.startActivity(i);
        });

    }

    public void replaceList(ArrayList<Order> orders) {
        orderlist.clear();
        orderlist.addAll(orders);
    }



    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        protected TextView parnum;
        protected TextView dt;
        protected TextView sumamnt;
        protected  Button detailsButton;

        public ViewHolder(View itemView) {
            super(itemView);
            parnum = itemView.findViewById(R.id.fincode);
            dt = itemView.findViewById(R.id.trndate);
            sumamnt = itemView.findViewById(R.id.sumamnt);
            detailsButton = itemView.findViewById(R.id.details);


        }
    }
}
