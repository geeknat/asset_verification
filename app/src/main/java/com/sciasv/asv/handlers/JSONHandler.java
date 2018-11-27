package com.sciasv.asv.handlers;

import android.content.Context;
import android.content.Intent;

import com.sciasv.asv.activities.Home;
import com.sciasv.asv.models.AssetItem;
import com.sciasv.asv.models.ProfileHolder;
import com.sciasv.asv.utils.ResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Geek Nat
 * On 9/20/2016.
 */
public class JSONHandler {

    private Context context;
    private ResponseHandler responseHandler;
    private ProfileHolder profileHolder;
    private String message;

    public JSONHandler(Context context) {
        this.context = context;
        responseHandler = new ResponseHandler(context);
        profileHolder = new ProfileHolder(context);
    }


    public static boolean isValidToken(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getInt("success") == -1) {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }


    public boolean login(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);

            if (jsonObject.getInt("success") == 1) {

                JSONObject responseObject = jsonObject.getJSONObject("message");

                ProfileHolder profileHolder = new ProfileHolder(context);
                profileHolder.setUserId(responseObject.getString("id"));
                profileHolder.setFirstName(responseObject.getString("first_name") + " " + responseObject.getString("last_name"));
                profileHolder.setEmailAddress(responseObject.getString("email"));
                profileHolder.setUserLoggedIn(true);

                context.startActivity(new Intent(context, Home.class));

                return true;
            }

            responseHandler.showToast(jsonObject.getString("message"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<AssetItem> getAssets(String result) {

        ArrayList<AssetItem> assetItems = new ArrayList<>();

        try {

            JSONObject responseObject = new JSONObject(result);

            if (responseObject.getInt("success") == 0) {
                return assetItems;
            }

            JSONArray jsonArray = responseObject.getJSONArray("message");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                AssetItem assetItem = new AssetItem();
                assetItem.setAccountCode("");
                assetItem.setName(object.getString("name"));
                assetItem.setSerialNumber(object.getString("serial"));
                assetItem.setEmail(object.getString("email"));
                assetItem.setManufacturer("");
                assetItem.setDefaultOffice(object.getString("location_name"));
                assetItem.setModelNo(object.getString("model_name") + ", " + object.getString("model_number"));
                assetItem.setPurchaseDate(object.getString("purchase_date"));
                assetItem.setSupplier(object.getString("supplier_name"));
                assetItem.setLocation(object.getString("issue_location_name"));
                assetItem.setStatus(object.getString("status_label"));
                assetItem.setSof(object.getString("_snipeit_sof"));
                assetItem.setAssetTag(object.getString("asset_tag"));
                assetItem.setPurchaseCost(object.getString("purchase_cost"));
                assetItem.setCreatedAt(object.getString("created_at"));
                assetItem.setComments(object.getString("notes"));
                assetItem.setWarranty(object.getString("warranty_months") + " months");
                assetItems.add(assetItem);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return assetItems;
    }

    public boolean isSuccess(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            setMessage(jsonObject.getString("message"));
            return jsonObject.getInt("success") == 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setMessage("We encountered an error");
        return true;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
