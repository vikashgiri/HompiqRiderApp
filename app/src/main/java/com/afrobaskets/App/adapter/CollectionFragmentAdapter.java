package com.afrobaskets.App.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afrobaskets.App.activity.DeliveryDetailsActivity;
import com.afrobaskets.App.bean.OrderCollectionBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webistrasoft.org.ecommerce.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hh on 13-Nov-17.
 */

public class CollectionFragmentAdapter extends RecyclerView.Adapter<CollectionFragmentAdapter.MyViewHolder> {
    Context context;
    RecyclerView recyclerView;
    String status_type;

    ArrayList<OrderCollectionBean> orderCollectionBeen;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date,patient_name,patient_address,total_test;
        ImageView image;
        public MyViewHolder(View view) {
            super(view);
            patient_name=(TextView)view.findViewById(R.id.patient_name);
            patient_address=(TextView)view.findViewById(R.id.patient_address);
            total_test=(TextView)view.findViewById(R.id.total_test);
            date=(TextView)view.findViewById(R.id.txt_p_date);

        }

    }
    public CollectionFragmentAdapter(Context context, String status_type,ArrayList<OrderCollectionBean> orderCollectionBeen,RecyclerView recyclerView) {
        this.orderCollectionBeen = orderCollectionBeen;
        this.context=context;
        this.recyclerView=recyclerView;
        this.status_type=status_type;
    }

    @Override
    public CollectionFragmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collectionfragmentadapter, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<OrderCollectionBean>>() {}.getType();
                String json = gson.toJson(orderCollectionBeen, type);
                Intent intent=new Intent(context,DeliveryDetailsActivity.class);
                intent.putExtra("data",json);
                intent.putExtra("type",status_type);
                intent.putExtra("position",""+recyclerView.getChildAdapterPosition(itemView));
                context.startActivity(intent);
            }
        });
        return new CollectionFragmentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CollectionFragmentAdapter.MyViewHolder holder, int position) {
      holder.patient_name.setText(orderCollectionBeen.get(position).getShippingAddressListBeansArrayList().get(0).getContact_name());
        holder.patient_address.setText(orderCollectionBeen.get(position).getShippingAddressListBeansArrayList().get(0).getHouse_number());
      holder.date.setText(orderCollectionBeen.get(position).getCreated_date());
       holder.total_test.setText(""+orderCollectionBeen.get(position).getOrderItemListBeansArrayList().size()+" Items");
    }

    @Override
    public int getItemCount() {
        return orderCollectionBeen.size();
    }

}
