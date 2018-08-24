package gr.logistic_i.logistic_i;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ConfirmVoucherAdapter extends RecyclerView.Adapter<ConfirmVoucherAdapter.ViewHolder> {

    private ArrayList<MtrLine> mtrLines;
    private Context mContext;
    private DecimalFormat priceFormat = new DecimalFormat("#.##");

    ConfirmVoucherAdapter(Context mContext, ArrayList<MtrLine> mtrLines){
        this.mContext = mContext;
        this.mtrLines = mtrLines;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_voucher_adapter, parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.desc.setText(mtrLines.get(position).getDescription());
        holder.units.setText(mtrLines.get(position).getUnitSelectedName().substring(0,3));
        holder.price.setText(String.valueOf(priceFormat.format(Double.parseDouble(mtrLines.get(position).getPrice())))+"€");
        holder.qty.setText(mtrLines.get(position).getQty1());

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mtrLines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        protected TextView desc;
        protected TextView units;
        TextView qty;
        TextView price;

        ViewHolder(View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.namedis);
            units = itemView.findViewById(R.id.mm);
            qty = itemView.findViewById(R.id.qq);
            price = itemView.findViewById(R.id.pprice);


        }
    }
}
