package gr.logistic_i.logistic_i;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainMenuAdapter extends RecyclerView.Adapter<ViewHolder> {


    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mfinum = new ArrayList<>();
    private ArrayList<String> mdate = new ArrayList<>();
    private Context mContext;

    public MainMenuAdapter(Context mContext, ArrayList<String> mfinum, ArrayList<String> mdate) {
        this.mfinum = mfinum;
        this.mdate = mdate;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_menu_list_adapter, parent ,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
        // TODO: 5/22/2018  

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView parnum;
        TextView dt;
        ConstraintLayout parent_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            parnum = itemView.findViewById(R.id.finum);
            dt = itemView.findViewById(R.id.findate);
            parent_layout = itemView.findViewById(R.id.main_menu_parent);


        }
    }
}
