package org.techtown.transmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DispatchAdapter extends RecyclerView.Adapter<DispatchAdapter.ViewHolder> {

    private Context context;
    public DispatchAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public DispatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_dispatch, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull DispatchAdapter.ViewHolder holder, int position) {
        holder.bind(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton btn_edit, btn_delete;
        LinearLayout recy_layout;

        private int viewType;
        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;

            btn_edit = itemView.findViewById(R.id.button_edit);
            btn_delete = itemView.findViewById(R.id.button_delete);
            recy_layout = itemView.findViewById(R.id.recy_layout);
        }
        public void bind(TransData item) {
            TextView trans = itemView.findViewById(R.id.text_trans);
            TextView product = itemView.findViewById(R.id.text_product);
            TextView agency = itemView.findViewById(R.id.text_agency);
            TextView quantity = itemView.findViewById(R.id.text_quantity);
            trans.setText(item.getStart()+" - "+item.getEnd());
            product.setText(item.getProduct());
            agency.setText(item.getAgency());
            quantity.setText(item.getQuantity());
        }
    }

    //리사이클러뷰 속 데이터 수정, 삭제 버튼 이벤트 처리하기
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        //카드뷰 누르면 운송등록으로 바로 이동
        holder.recy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String product = dataSet.get(position).getProduct();
                String start = dataSet.get(position).getStart();
                String end = dataSet.get(position).getEnd();
                String quantity = dataSet.get(position).getQuantity();
                String agency = dataSet.get(position).getAgency();

                Intent intent = new Intent(context, RegistTrans.class);
                intent.putExtra("product", product);
                intent.putExtra("start", start);
                intent.putExtra("end", end);
                intent.putExtra("quantity", quantity);
                intent.putExtra("agency", agency);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private ArrayList<TransData> dataSet = new ArrayList();
    public void submitData(ArrayList<TransData> newData) {
        dataSet = newData;
        notifyDataSetChanged();
    }


}
