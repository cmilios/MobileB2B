package gr.logistic_i.logistic_i;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {

    private ArrayList<MtrLine> mtrLines;
    private Context mContext;

    public BasketAdapter(ArrayList<MtrLine> mtrLines, Context mContext) {
        this.mtrLines = mtrLines;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_rview_adapter, parent ,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BasketAdapter.ViewHolder holder, int position) {
        holder.item_qty.setText(mtrLines.get(position).getQty());
        holder.item_name.setText(mtrLines.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
            return mtrLines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView item_name;
        protected TextView item_qty;


        public ViewHolder(View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.ordered_item_title);
            item_qty = itemView.findViewById(R.id.ordered_item_qty);

        }
    }
}
