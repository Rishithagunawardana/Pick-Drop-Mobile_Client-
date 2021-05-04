package com.example.pickdrop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<User> list;


    public MyAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v  = LayoutInflater.from(context).inflate(R.layout.activity_item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        User user = list.get(position);
        holder.drmobile.setText(user.getDrmobile());
        holder.drname.setText(user.getDrname());
        holder.fee.setText(user.getFee());
        holder.nme.setText(user.getNme());
        holder.status.setText(user.getStatus());
        holder.type.setText(user.getType());


        holder.PAY.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent start = new Intent(context,payment.class);
                context.startActivity(start);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView  drmobile,drname,fee,nme,status,type;
        Button PAY;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
           nme  = itemView.findViewById(R.id.pname);
           status  = itemView.findViewById(R.id.ststus);
            type = itemView.findViewById(R.id.vtype);
            fee = itemView.findViewById(R.id.fee);
            drname = itemView.findViewById(R.id.Dname);
            drmobile = itemView.findViewById(R.id.Dmobile);
            PAY = itemView.findViewById(R.id.pay);
        }
    }

}
