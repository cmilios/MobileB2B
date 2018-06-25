package gr.logistic_i.logistic_i;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder> {


    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Order> orderlist = new ArrayList<>();
    private Context mContext;

    public MainMenuAdapter(Context mContext, ArrayList<Order> orderlist) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_menu_list_adapter, parent ,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.parnum.setText(orderlist.get(position).getFincode());
//        holder.dt.setText(orderlist.get(position).getTrndate());
//        holder.sumamnt.setText((orderlist.get(position).getSumamnt()));

    }



    @Override
    public int getItemCount() {
        return 15;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView parnum;
        TextView dt;
        TextView sumamnt;
        Button btn;
        ConstraintLayout parent_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            parnum = itemView.findViewById(R.id.fincode);
            dt = itemView.findViewById(R.id.trndate);
            sumamnt = itemView.findViewById(R.id.sumamnt);
            btn = itemView.findViewById(R.id.details);
            parent_layout = itemView.findViewById(R.id.main_menu_parent);


        }
    }
}
