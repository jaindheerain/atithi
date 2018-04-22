package com.halfdotfull.atithi.marketplace;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.halfdotfull.atithi.R;
import com.halfdotfull.atithi.Shop;

import java.util.ArrayList;

/**
 * Created by nexflare on 21/03/18.
 */

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ShopsViewHolder>{

    Context context;
    ArrayList<Shop> shopArrayList;

    ShopsAdapter(Context context,ArrayList<Shop> shopArrayList){
        this.context=context;
        this.shopArrayList=shopArrayList;
    }


    @Override
    public ShopsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_shop,parent,false);
        return new ShopsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ShopsViewHolder holder, int position) {
        holder.locality.setText(shopArrayList.get(position).getLocality());
        holder.shopName.setText(shopArrayList.get(position).getShop_name());
        holder.shopDescription.setText(shopArrayList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        Log.d("TAGGER", "getItemCount: "+shopArrayList.size());
        return shopArrayList.size();
    }

    public class ShopsViewHolder extends RecyclerView.ViewHolder{

        TextView locality,shopName,shopDescription;
        ImageView shopImage;

        public ShopsViewHolder(View itemView) {
            super(itemView);
            locality=itemView.findViewById(R.id.locality);
            shopName=itemView.findViewById(R.id.shopName);
            shopDescription=itemView.findViewById(R.id.shopDescription);
            shopImage=itemView.findViewById(R.id.imageShop);
        }
    }

    public void updateArrayList(ArrayList<Shop> shopArrayList){
        this.shopArrayList=shopArrayList;
    }
}
