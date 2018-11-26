package com.sciasv.asv.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.sciasv.asv.R;
import com.sciasv.asv.adapters.AssetsAdapter;
import com.sciasv.asv.handlers.JSONHandler;
import com.sciasv.asv.models.AssetItem;
import com.sciasv.asv.models.ProfileHolder;
import com.sciasv.asv.network.Connect;
import com.sciasv.asv.utils.ResponseHandler;

import java.util.ArrayList;


/**
 * Created by @GeekNat on 3/30/17.
 */

public class HistoryFragment extends Fragment {

    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    ResponseHandler responseHandler;
    RecyclerView recyclerView;
    ProfileHolder profileHolder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        responseHandler = new ResponseHandler(context);
        profileHolder = new ProfileHolder(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.activity_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });

        return view;

    }

    void load() {
        swipeRefreshLayout.setRefreshing(true);

        AndroidNetworking.get(Connect.url + Connect.fetchUserHistory(profileHolder.getUserId()))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        swipeRefreshLayout.setRefreshing(false);

                        JSONHandler jsonHandler = new JSONHandler(context);

                        ArrayList<AssetItem> assetItems = jsonHandler.getAssets(response);

                        recyclerView.setAdapter(new AssetsAdapter(context, assetItems));

                    }

                    @Override
                    public void onError(ANError anError) {

                        swipeRefreshLayout.setRefreshing(false);

                        // handle error
                        Log.d(Connect.tag, anError.toString());
                        responseHandler.showToast("We encountered an error");

                    }
                });

    }


    @Override
    public void onResume() {
        super.onResume();

        load();
    }

}
