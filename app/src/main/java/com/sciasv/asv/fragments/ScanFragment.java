package com.sciasv.asv.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.sciasv.asv.R;
import com.sciasv.asv.handlers.JSONHandler;
import com.sciasv.asv.models.AssetItem;
import com.sciasv.asv.models.ProfileHolder;
import com.sciasv.asv.network.Connect;
import com.sciasv.asv.utils.ResponseHandler;
import com.sciasv.asv.utils.Utils;

import java.util.ArrayList;


/**
 * Created by @GeekNat on 3/30/17.
 */

public class ScanFragment extends Fragment {

    Context context;
    ResponseHandler responseHandler;
    CodeScanner mCodeScanner;
    ProfileHolder profileHolder;
    String tag = "";

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        load(result.getText());
                    }
                });

            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        return view;

    }

    void load(final String tagResult) {
        final ProgressDialog progressDialog = new ProgressDialog(context, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setMessage("Loading asset...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (tagResult.contains("http")) {
            String[] splitTag = tagResult.split("/");
            tag = splitTag[splitTag.length - 1];
        } else {
            tag = tagResult;
        }

        responseHandler.showToast(tag);

        AndroidNetworking.get(Connect.url + Connect.fetchAsset)
                .addQueryParameter("user_id", profileHolder.getUserId())
                .addQueryParameter("tag", tag)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        Log.v(Connect.tag, response);

                        progressDialog.dismiss();

                        JSONHandler jsonHandler = new JSONHandler(context);

                        ArrayList<AssetItem> assetItems = jsonHandler.getAssets(response);

                        if (assetItems.size() == 0) {
                            responseHandler.showDialog("Oops!!!", "No asset found matching the scanned tag - " + tag);
                        } else {
                            Utils.viewAsset(context, assetItems.get(0));
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        progressDialog.dismiss();

                        // handle error
                        Log.d(Connect.tag, anError.toString());
                        responseHandler.showToast("We encountered an error");

                    }
                });

    }




    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
