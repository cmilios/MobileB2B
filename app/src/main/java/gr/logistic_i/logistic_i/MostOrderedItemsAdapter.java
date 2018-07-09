package gr.logistic_i.logistic_i;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MostOrderedItemsAdapter extends RecyclerView.Adapter<MostOrderedItemsAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Mtrl> mmtrList;
    private Context mContext;
    private String clientID;
    private String url;
    private ArrayList<MtrLine> mtrLines;
    private String refid;

    public MostOrderedItemsAdapter(Context mContext, ArrayList<Mtrl> mtrList, String url, String clientId,String refid, ArrayList<MtrLine> mtrLines) {
        this.mmtrList = mtrList;
        this.mContext = mContext;
        this.url = url;
        this.clientID = clientId;
        this.mtrLines = mtrLines;
        this.refid = refid;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_ordered_items_adapter, parent ,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemName.setText(mmtrList.get(holder.getAdapterPosition()).getName());
        if (mmtrList.get(holder.getAdapterPosition()).getImgURL()!=null){
            if (mmtrList.get(holder.getAdapterPosition()).getImage()!=null){
                holder.img.setImageDrawable(mmtrList.get(holder.getAdapterPosition()).getImage());
                holder.pbar_layout.setVisibility(View.INVISIBLE);
                holder.img.setVisibility(View.VISIBLE);

            }
        }
        else{
            holder.img.setImageResource(R.drawable.ic_baseline_image_24px);
            holder.pbar_layout.setVisibility(View.INVISIBLE);
            holder.img.setVisibility(View.VISIBLE);

        }
        if (mtrLines !=null){
            for(MtrLine m : mtrLines){
                if (m.getCode().equals(mmtrList.get(holder.getAdapterPosition()).getCode())){
                    if ((!m.getQty().equals("")) && !m.getQty().equals("0")){
                        holder.qty_not.setText("x"+m.getQty());
                        holder.qty_not.setVisibility(View.VISIBLE);
                    }

                }

            }

        }




        holder.btn.setOnClickListener(v -> {
            Intent i = new Intent(mContext, AddProductActivity.class);
            i.putParcelableArrayListExtra("lines", mtrLines);
            i.putExtra("mtrl",mmtrList.get(holder.getAdapterPosition()));
            i.putExtra("id", mContext.getClass().getSimpleName());
            i.putExtra("url", url);
            i.putExtra("refid", refid);
            i.putExtra("clid", clientID);
            mContext.startActivity(i);


        });




    }

    @Override
    public int getItemCount() {
        return mmtrList.size();
    }

    public void replaceList(ArrayList<Mtrl> mtrList) {
        mmtrList.clear();
        mmtrList.addAll(mtrList);
    }




    public class ViewHolder extends RecyclerView.ViewHolder{


        protected ImageView img;
        protected TextView itemName;
        protected Button btn;
        protected RelativeLayout pbar_layout;
        protected TextView selected_qty;
        protected ConstraintLayout parent_layout;
        protected TextView qty_not;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_picture);
            btn = itemView.findViewById(R.id.button);
            itemName = itemView.findViewById(R.id.item_name);
            parent_layout = itemView.findViewById(R.id.main_menu_parent);
            pbar_layout = itemView.findViewById(R.id.pbar_layout);
            qty_not = itemView.findViewById(R.id.quantities_notation);



        }
    }
}
