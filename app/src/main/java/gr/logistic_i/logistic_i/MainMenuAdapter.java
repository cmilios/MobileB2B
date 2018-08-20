package gr.logistic_i.logistic_i;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder> {



    private ArrayList<Order> orderlist;
    private Context mContext;
    private SimpleDateFormat dpformat = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat sqlResFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private Date formattedDate = new Date();


    MainMenuAdapter(Context mContext, ArrayList<Order> orderlist) {
        this.mContext = mContext;
        this.orderlist = orderlist;
    }

    @NonNull
    @Override
    public  ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_menu_list_adapter, parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        switch (orderlist.get(position).getState()) {
            case "1":
                holder.mmparent.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorWhite));
                break;
            case "2":
                holder.mmparent.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorTransparentYellow));
                break;
            case "3":
                holder.mmparent.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorTransparentGreen));
                break;
            default:
                holder.mmparent.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorTransparentRed));
                break;
        }


        holder.parnum.setText(orderlist.get(position).getFincode());
        try {
            formattedDate = sqlResFormat.parse(orderlist.get(position).getTrndate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.dt.setText(dpformat.format(formattedDate));
        holder.sumamnt.setText(orderlist.get(position).getSumamnt()+ "€");

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

        TextInputEditText parnum;
        TextInputEditText dt;
        RelativeLayout mmparent;
        protected TextInputEditText sumamnt;

        ViewHolder(View itemView) {
            super(itemView);
            mmparent = itemView.findViewById(R.id.mm_parent);
            parnum = itemView.findViewById(R.id.fincode);
            dt = itemView.findViewById(R.id.trndate);
            sumamnt = itemView.findViewById(R.id.sumamnt);


        }
    }
}
