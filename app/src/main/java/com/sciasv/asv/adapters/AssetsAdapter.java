package com.sciasv.asv.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sciasv.asv.R;
import com.sciasv.asv.models.AssetItem;
import com.sciasv.asv.utils.Utils;

import java.util.ArrayList;


/**
 * @author Geek Nat
 * On 6/24/2016.
 */
public class AssetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<AssetItem> assetItems;
    private Context context;
    private static final int EMPTY_VIEW = 100;
    private static final int DATA_VIEW = 200;

    public AssetsAdapter(Context context, ArrayList<AssetItem> assetItems) {
        this.context = context;
        this.assetItems = assetItems;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return assetItems.size() == 0 ? EMPTY_VIEW : DATA_VIEW;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case DATA_VIEW:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.asset_item, parent, false));
            case EMPTY_VIEW:
                return new EmptyViewHolder(LayoutInflater.from(context).inflate(R.layout.empty_item, parent, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EmptyViewHolder) {
            EmptyViewHolder emptyHolder = (EmptyViewHolder) holder;
            emptyHolder.tItemName.setText("No assets found...");
            emptyHolder.tSub.setText("Assets you scan will appear in your history\n\nUse the SCAN page to scan an asset...");
        }

        if (holder instanceof ViewHolder) {
            final AssetItem assetItem = assetItems.get(position);
            ((ViewHolder) holder).tName.setText("Asset Tag : " + assetItem.getAssetTag());

            ((ViewHolder) holder).tSubItem.setText(
                            "Model Name : " + assetItem.getModelNo());

            ((ViewHolder) holder).card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.viewAsset(context, assetItem);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return assetItems.size() == 0 ? 1 : assetItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tName, tSubItem;
        CardView card;
        View view;
        Button btnAction;

        ViewHolder(View productView) {
            super(productView);
            tName = productView.findViewById(R.id.itemName);
            tSubItem = productView.findViewById(R.id.itemSubName);
            card = productView.findViewById(R.id.card_view);
            view = productView.findViewById(R.id.view);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        TextView tItemName, tSub;

        EmptyViewHolder(View itemView) {
            super(itemView);
            tItemName = itemView.findViewById(R.id.itemName);
            tSub = itemView.findViewById(R.id.subItemName);
        }
    }

}
