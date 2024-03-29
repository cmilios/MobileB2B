package gr.logistic_i.logistic_i;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class ItemsMenuAdapter extends RecyclerView.Adapter<ItemsMenuAdapter.ViewHolder> {

    private ArrayList<Mtrl> mmtrList;
    private Context mContext;
    private ArrayList<MtrLine> mtrLines;



    ItemsMenuAdapter(Context mContext, ArrayList<Mtrl> mtrList, ArrayList<MtrLine> mtrLines) {
        this.mmtrList = mtrList;
        this.mContext = mContext;
        this.mtrLines = mtrLines;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_ordered_items_adapter, parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Mtrl m = mmtrList.get(position);
        if(m.getName()!=null){
            holder.itemName.setText(m.getName());
        }

        if (m.getImgURL()!=null){
            Uri uri = Uri.parse("https://"+m.getCorrespondingBase()+".oncloud.gr/s1services/?filename="+m.getImgURL());
            holder.draweeView.setImageURI(uri);
        }

        if (mtrLines!=null || mtrLines.size()!=0) {
            for (MtrLine mt:mtrLines) {
                if (mt.getDescription().equals(mmtrList.get(position).getName())) {
                    holder.pos.setText("x"+mt.getQty1());
                }
            }
        }


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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        protected ConstraintLayout parent_layout;
        SimpleDraweeView draweeView;
        TextView pos;

        ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            parent_layout = itemView.findViewById(R.id.main_menu_parent);
            draweeView = itemView.findViewById(R.id.my_image_view);

            pos = itemView.findViewById(R.id.pos);


        }
    }
}
