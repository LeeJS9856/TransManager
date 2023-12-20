package org.techtown.transmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class TransDataAdapter extends RecyclerView.Adapter<TransDataAdapter.ViewHolder> {

    private Context context;
    public TransDataAdapter(Context context) {
        this.context = context;
    }
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
        ImageButton btn_edit, btn_delete;


        private int viewType;
        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;

            btn_edit = itemView.findViewById(R.id.button_edit);
            btn_delete = itemView.findViewById(R.id.button_delete);
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
            TextView trans = itemView.findViewById(R.id.text_trans);
            TextView product = itemView.findViewById(R.id.text_product);
            TextView agency = itemView.findViewById(R.id.text_agency);
            TextView quantity = itemView.findViewById(R.id.text_quantity);

            date.setText(item.getMonth()+"."+item.getDay());
            trans.setText(item.getStart()+" - "+item.getEnd());
            product.setText(item.getProduct());
            agency.setText(item.getAgency());
            quantity.setText(item.getQuantity());

        }
        private void bindUnChangedDate(TransData item) {
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
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = dataSet.get(position).getId();
                String year = dataSet.get(position).getYear();
                String month = dataSet.get(position).getMonth();
                String day = dataSet.get(position).getDay();
                String vihiclenumber = dataSet.get(position).getVihiclenumber();
                String product = dataSet.get(position).getProduct();
                String start = dataSet.get(position).getStart();
                String end = dataSet.get(position).getEnd();
                String quantity = dataSet.get(position).getQuantity();
                String agency = dataSet.get(position).getAgency();

                Intent intent = new Intent(context, EditTrans.class);
                intent.putExtra("id", id);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                intent.putExtra("vihiclenumber", vihiclenumber);
                intent.putExtra("product", product);
                intent.putExtra("start", start);
                intent.putExtra("end", end);
                intent.putExtra("quantity", quantity);
                intent.putExtra("agency", agency);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
                alertBuilder.setTitle("경고");
                alertBuilder.setMessage("해당 운송정보를 삭제하시겠습니까?");
                alertBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Response.Listener<String> deleteResponseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        };
                        String id = Integer.toString(dataSet.get(position).getId());
                        String year = dataSet.get(position).getYear();
                        String month = dataSet.get(position).getMonth();
                        String day = dataSet.get(position).getDay();
                        String vihiclenumber = dataSet.get(position).getVihiclenumber();
                        String product = dataSet.get(position).getProduct();
                        String start = dataSet.get(position).getStart();
                        String end = dataSet.get(position).getEnd();
                        String quantity = dataSet.get(position).getQuantity();
                        String agency = dataSet.get(position).getAgency();
                        ListTransDeleteRequest deleteRequest = new ListTransDeleteRequest(id, year, month, day, vihiclenumber, product, start, end, quantity, agency, deleteResponseListener);
                        RequestQueue Queue = Volley.newRequestQueue(view.getContext());
                        Queue.add(deleteRequest);

                        dataSet.remove(position);
                        notifyItemRemoved(position);
                        notifyItemChanged(position, dataSet.size());
                    }
                });
                alertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
       });
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
