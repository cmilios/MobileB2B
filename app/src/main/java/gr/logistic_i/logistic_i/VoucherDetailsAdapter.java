package gr.logistic_i.logistic_i;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class VoucherDetailsAdapter extends RecyclerView.Adapter<VoucherDetailsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MtrLine> mtrLines;

    VoucherDetailsAdapter(Context mContext, ArrayList<MtrLine> mtrLines) {
        this.mContext = mContext;
        this.mtrLines = mtrLines;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_details_adapter, parent ,false);
        return new VoucherDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.desc.setText(mtrLines.get(position).getDescription());
        holder.cleanval.setText(mtrLines.get(position).getCleanValue());
        holder.fpaval.setText(mtrLines.get(position).getFpaValue());
        holder.fullval.setText(mtrLines.get(position).getPrice());
        holder.qty.setText(mtrLines.get(position).getQty1());


    }

    public void replaceList(ArrayList<MtrLine> mmtrLines){
        ArrayList<MtrLine> ml = new ArrayList<>();
        ml.clear();
        ml.addAll(mmtrLines);
        mtrLines = ml;
    }

    @Override
    public int getItemCount() {
        return mtrLines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        protected TextView desc;
        TextView qty;
        protected TextView fpaval;
        TextView cleanval;
        TextView fullval;

        ViewHolder(View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.perigrafi);
            qty = itemView.findViewById(R.id.line_qty);
            fpaval = itemView.findViewById(R.id.fpaval);
            cleanval = itemView.findViewById(R.id.kathari);
            fullval = itemView.findViewById(R.id.fullprice);



        }
    }
}
