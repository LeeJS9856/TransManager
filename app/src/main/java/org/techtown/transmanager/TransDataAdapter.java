package org.techtown.transmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransDataAdapter extends RecyclerView.Adapter<TransDataAdapter.ViewHolder> {
    @NonNull
    @Override
    public TransDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getViewSrc(viewType), parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TransDataAdapter.ViewHolder holder, int position) {
        holder.bind(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private int viewType;
        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
        }
        public void bind(TransData item) {
            if(viewType==TYPE_CHANGE_DATE) {
                bindChangeDate(item);
            }
            else if(viewType==TYPE_UNCHANGED_DATE) {
                bindUnChangedDate(item);
            }
        }
        private void bindChangeDate(TransData item) {
            TextView date = itemView.findViewById(R.id.text_date);
            TextView data = itemView.findViewById(R.id.text_data);
            date.setText(item.getMonth()+"."+item.getDay());
            data.setText(item.getProduct()+" / "+item.getStart()+" / "+item.getEnd()+" / "+item.getQuantity());
        }
        private void bindUnChangedDate(TransData item) {
            TextView data = itemView.findViewById(R.id.text_data);
            data.setText(item.getProduct()+" / "+item.getStart()+" / "+item.getEnd()+" / "+item.getQuantity());
        }
    }


    private ArrayList<TransData> dataSet = new ArrayList();
    public void submitData(ArrayList<TransData> newData) {
        dataSet = newData;
        notifyDataSetChanged();
    }

    //ViewType에 따라 xml파일 연결하는 메서드
    private int TYPE_CHANGE_DATE = 101;
    private int TYPE_UNCHANGED_DATE = 102;
    private int getViewSrc(int viewType) {
        if(viewType==TYPE_CHANGE_DATE) {
            return R.layout.recycler_trans_date;
        }
        else {
            return R.layout.recycler_trans_list;
        }
    }


    public int getItemViewType(int position) {
        String transData = dataSet.get(position).getDay();
        String beforeTransData = "";
        if(position>0){ beforeTransData = dataSet.get(position-1).getDay(); }
        if(transData.equals(beforeTransData)) {
            return TYPE_UNCHANGED_DATE;
        }
        else return TYPE_CHANGE_DATE;
    }

}
