package com.example.onthi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onthi.MainActivity;
import com.example.onthi.R;
import com.example.onthi.modal.Xemay;

import java.util.List;

public class XemayAdapter extends RecyclerView.Adapter<XemayAdapter.viewholder> {

    private  List<Xemay> list;
    private  Context context;

    MainActivity main;
    private XeClick xeClick;

    public XemayAdapter(List<Xemay> list, Context context, XeClick xeClick) {
        this.list = list;
        this.context = context;
        this.xeClick = xeClick;
        main = (MainActivity) context;
    }

    public interface XeClick {
        void edit (Xemay xemay);
        void delete(Xemay xemay);
    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_xemay,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        if (position >= 0 && position < list.size()){
            Xemay xe = list.get(position);
            holder.txtTenXe.setText("Tên xe : "+ xe.getTen_xe_ph42693());
            holder.txtMauSac.setText("Màu : " + xe.getMau_sac_ph42693());
            holder.txtGiaTien.setText("Giá : " +xe.getGia_ban_ph42693());
            holder.txtMoTa.setText("Mô tả : "+xe.getMo_ta_ph42693());
            Glide.with(holder.itemView)
                    .load(xe.getHinh_anh_ph42693())
                    .circleCrop()
                    .centerCrop()
                    .into(holder.imgHinhAnh);

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    xeClick.delete(xe);
                }
            });
            holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    xeClick.edit(xe);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() > 0){
            return list.size();
        }
        return 0;
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        TextView txtTenXe,txtMauSac,txtGiaTien,txtMoTa;
        ImageView imgHinhAnh;
        Button btnUpdate,btnDelete;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            imgHinhAnh = itemView.findViewById(R.id.imgHinhAnh);
            txtTenXe = itemView.findViewById(R.id.txtTenXe);
            txtMauSac = itemView.findViewById(R.id.txtMauSac);
            txtGiaTien = itemView.findViewById(R.id.txtGiaTien);
            txtMoTa = itemView.findViewById(R.id.txtMoTa);
        }
    }
}
