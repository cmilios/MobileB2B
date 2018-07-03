package gr.logistic_i.logistic_i;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MostOrderedItemsAdapter extends RecyclerView.Adapter<MostOrderedItemsAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Mtrl> mmtrList;
    private Context mContext;
    private String clientID;
    private String url;

    public MostOrderedItemsAdapter(Context mContext, ArrayList<Mtrl> mtrList, String url, String clientId) {
        this.mmtrList = mtrList;
        this.mContext = mContext;
        this.url = url;
        this.clientID = clientId;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_ordered_items_adapter, parent ,false);
        ViewHolder holder = new ViewHolder(view);
        //TODO make button to define qty for mtrl in order to add to findoc list
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemName.setText(mmtrList.get(position).getName());
        holder.img.setImageBitmap(mmtrList.get(position).makeStringToBitmap());

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
        protected ConstraintLayout parent_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_picture);
            btn = itemView.findViewById(R.id.confirm_button);
            itemName = itemView.findViewById(R.id.item_name);
            parent_layout = itemView.findViewById(R.id.main_menu_parent);


        }
    }
}
