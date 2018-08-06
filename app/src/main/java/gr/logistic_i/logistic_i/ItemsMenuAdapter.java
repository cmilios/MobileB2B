package gr.logistic_i.logistic_i;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class ItemsMenuAdapter extends RecyclerView.Adapter<ItemsMenuAdapter.ViewHolder> {

    private ArrayList<Mtrl> mmtrList;
    private Context mContext;
    private String clientID;
    private String url;
    private ArrayList<MtrLine> mtrLines;
    private String refid;
    private Boolean isChecked;


    ItemsMenuAdapter(Context mContext, ArrayList<Mtrl> mtrList, String url, String clientId, String refid, ArrayList<MtrLine> mtrLines, Boolean isChecked) {
        this.mmtrList = mtrList;
        this.mContext = mContext;
        this.url = url;
        this.clientID = clientId;
        this.mtrLines = mtrLines;
        this.refid = refid;
        this.isChecked = isChecked;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_ordered_items_adapter, parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(mmtrList.get(position).getName()!=null){
            holder.itemName.setText(mmtrList.get(holder.getAdapterPosition()).getName());
        }

        if (mmtrList.get(position).getImgURL()!=null){
            Uri uri = Uri.parse("https://"+mmtrList.get(holder.getAdapterPosition()).getCorrespondingBase()+"/s1services/?filename="+mmtrList.get(position).getImgURL());
            holder.draweeView.setImageURI(uri);
        }


        holder.btn.setOnClickListener(v -> {
            Intent i = new Intent(mContext, AddProductActivity.class);
            i.putParcelableArrayListExtra("lines", mtrLines);
            i.putExtra("mtrl",mmtrList.get(position));
            i.putExtra("id", mContext.getClass().getSimpleName());
            i.putExtra("url", url);
            i.putExtra("refid", refid);
            i.putExtra("clid", clientID);
            i.putExtra("isChecked", isChecked);
            mContext.startActivity(i);
            ((Activity)mContext).finish();


        });
    }



    @Override
    public int getItemCount() {
        return mmtrList.size();
    }

    public void replaceList(ArrayList<Mtrl> mtrList) {
        ArrayList<Mtrl> ml = new ArrayList<>();
        ml.clear();
        ml.addAll(mtrList);
        mmtrList = ml;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        Button btn;
        protected ConstraintLayout parent_layout;
        SimpleDraweeView draweeView;

        ViewHolder(View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.button);
            itemName = itemView.findViewById(R.id.item_name);
            parent_layout = itemView.findViewById(R.id.main_menu_parent);
            draweeView = itemView.findViewById(R.id.my_image_view);



        }
    }
}
