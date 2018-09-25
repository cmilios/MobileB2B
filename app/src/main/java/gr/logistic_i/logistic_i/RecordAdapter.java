package gr.logistic_i.logistic_i;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class RecordAdapter extends BaseAdapter {

    private Context recordContext;
    private List<MtrLine> recordList;

    public void add(MtrLine record) {
        recordList.add(record);
        notifyDataSetChanged();
    }

    RecordAdapter(Context context, List<MtrLine> records) {
        recordList = records;
        recordContext = context;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }
    @Override
    public Object getItem(int i) {
        return recordList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RecordViewHolder holder;

        if (view ==null){
            LayoutInflater recordInflater = (LayoutInflater)
                    recordContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            assert recordInflater != null;
            view = recordInflater.inflate(R.layout.record, null);

            holder = new RecordViewHolder();
            holder.descView = view.findViewById(R.id.record_name);
            holder.qtyView = view.findViewById(R.id.record_age);
            holder.unitView = view.findViewById(R.id.record_position);
            view.setTag(holder);

        }else {
            holder = (RecordViewHolder) view.getTag();
        }


        MtrLine record = (MtrLine) getItem(i);
        holder.descView.setText(record.getDescription());
        holder.qtyView.setText(record.getQty1());
        holder.unitView.setText(record.getUnitSelectedName());
        return view;
    }

    private static class RecordViewHolder {

        TextView descView;
        TextView unitView;
        TextView qtyView;
    }
}

