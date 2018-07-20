package com.afrobaskets.App.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afrobaskets.App.bean.OrderItemListBeans;
import com.webistrasoft.org.ecommerce.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asdfgh on 10/13/2017.
 */

public class CollectionDetailAdapter extends RecyclerView.Adapter<CollectionDetailAdapter.MyViewHolder> {
    private List<OrderItemListBeans> OrderItemListBeansArrayList;
    Context context;
    String image_url;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView items;
        public TextView quantity;
        public TextView price;
        public TextView name;

        public MyViewHolder(View view)
        {
            super(view);
            name=(TextView)  view.findViewById(R.id.test_name);
            price=(TextView)  view.findViewById(R.id.price);

                   }
    }

    public CollectionDetailAdapter(Context context, ArrayList<OrderItemListBeans> OrderItemListBeansArrayList,String image_url)
    {
        this.OrderItemListBeansArrayList = OrderItemListBeansArrayList;
        this.context=context;
        this.image_url=image_url;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collectiondetailadapters, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {

        holder.name.setText(OrderItemListBeansArrayList.get(position).getProductDetailBeansArrayList().get(position).getTest_name());
        holder.price.setText("₹ "+OrderItemListBeansArrayList.get(position).getProductDetailBeansArrayList().get(position).getPrice());

    }

    @Override
    public int getItemCount()
    {
        return OrderItemListBeansArrayList.size();
    }

}
