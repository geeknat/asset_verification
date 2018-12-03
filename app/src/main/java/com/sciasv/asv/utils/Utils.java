package com.sciasv.asv.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sciasv.asv.R;
import com.sciasv.asv.models.AssetItem;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Geek Nat on 5/16/2016.
 */
public class Utils {

    static String customerType;

    public static final String IMAGE_DIRECTORY_NAME = "PASS_GRADE";
    public static final String ATTRIBUTE_SEPARATOR = ",";
    private String paymentMethod = "";

    public static String sanitizePhoneNumber(String phone) {

        if (phone.equals("")) {
            return "";
        }

        if (phone.length() < 11 & phone.startsWith("0")) {
            String p = phone.replaceFirst("^0", "254");
            return p;
        }

        if (phone.length() == 13 && phone.startsWith("+")) {
            return phone.substring(1);
        }

        return phone;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void customizeToolBar(Activity a) {
        Toolbar toolbar = a.findViewById(R.id.toolbar);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                TypefaceHelper.getInstance().setTypeface(tv, a.getString(R.string.custom_font));
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                //tv.setAllCaps(true);
                break;
            }
        }
    }

//    public static void setUpAutoLinkText(final Context context, AutoLinkTextView autoLinkTextView, String text) {
//        autoLinkTextView.addAutoLinkMode(
//                AutoLinkMode.MODE_EMAIL, AutoLinkMode.MODE_URL, AutoLinkMode.MODE_PHONE);
//        autoLinkTextView.setPhoneModeColor(ContextCompat.getColor(context, R.color.blue_400));
//        autoLinkTextView.setUrlModeColor(ContextCompat.getColor(context, R.color.blue_400));
//        autoLinkTextView.setMentionModeColor(ContextCompat.getColor(context, R.color.blue_400));
//        autoLinkTextView.setHashtagModeColor(ContextCompat.getColor(context, R.color.blue_400));
//        autoLinkTextView.setAutoLinkText(text);
//        autoLinkTextView.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
//            @Override
//            public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
//                switch (autoLinkMode) {
//                    case MODE_EMAIL:
//                        Intent emailIntent = new Intent(Intent.ACTION_VIEW);
//                        emailIntent.setData(Uri.parse("mailto:" + matchedText.trim()));
//                        context.startActivity(emailIntent);
//                        break;
//                    case MODE_PHONE:
//                        Intent callIntent = new Intent(Intent.ACTION_VIEW);
//                        callIntent.setData(Uri.parse("tel:" + matchedText.trim()));
//                        context.startActivity(callIntent);
//                        break;
//                    case MODE_URL:
//                        matchedText = matchedText.trim();
//                        if (!matchedText.startsWith("http://") && !matchedText.startsWith("https://")) {
//                            matchedText = "http://" + matchedText;
//                        }
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse(matchedText));
//                        if (intent.resolveActivity(context.getPackageManager()) != null) {
//                            context.startActivity(intent);
//                        } else {
//                            Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show();
//                        }
//                        break;
//                }
//            }
//        });
//    }

    public static String getText(EditText editText) {
        return editText.getText().toString().trim();
    }


    public static float roundToDouble(String d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_EVEN);
        return bd.floatValue();
    }


    public static void setProgressBarColor(ProgressBar progressBar) {
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#039BE5"), android.graphics.PorterDuff.Mode.SRC_ATOP);
    }


    public static boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }


    private static String getDecimalValue(String value) {
        if (value.length() == 2) {
            return value;
        }

        if (value.length() < 2) {
            return value + "0";
        }

        int finalValue = Integer.parseInt(value.substring(0, 3));

        int thirdValue = Integer.parseInt(value.substring(2, 3));

        if (thirdValue > 4) {
            finalValue = finalValue + 10;
        }

        return String.valueOf(finalValue).substring(0, 2);

    }

    public static String formatNumber(String initialValue) {
        String value = initialValue;
        if (initialValue == null || initialValue.isEmpty()) {
            return initialValue;
        }

        if (initialValue.startsWith("-")) {
            value = initialValue.substring(1);
        }

        String pointValue = "";
        try {
            String[] values = value.split("\\.");
            value = values[0];
            pointValue = "." + getDecimalValue(values[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            pointValue = ".00";
        }

        if (value.length() < 3) {
            return value + pointValue;
        }
        int commasToPlace = value.length() / 3;
        String newString = "";
        int x = value.length() - 3;
        for (int i = 0; i < commasToPlace; i++) {
            if (newString != "") {
                newString = value.substring(x, x + 3) + "," + newString;
            } else {
                newString = value.substring(x, x + 3) + newString;
            }
            x = x - 3;
        }

        String finalString = value.substring(0, value.length() - (commasToPlace * 3)) + "," + newString;

        if (initialValue.startsWith("-")) {
            return (String.valueOf(finalString.charAt(0)).equals(",")) ? "-" + finalString.replaceFirst(",", "") + pointValue : "-" + finalString + pointValue;
        }

        return (String.valueOf(finalString.charAt(0)).equals(",")) ? finalString.replaceFirst(",", "") + pointValue : finalString + pointValue;
    }


    public static String imageToBase64(String imagePath) {

        if (imagePath == null) {
            return "";
        }

        if (imagePath.startsWith("images/")) {
            return imagePath;
        }

        if (imagePath.isEmpty()) {
            return "";
        }

        if (imagePath.equals("")) {
            return "";
        }

        String imageDataString = "";
        File file = new File(imagePath);
        try {
            FileInputStream imageInFile = new FileInputStream(file);
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);
            imageDataString = Base64.encodeToString(imageData, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageDataString;
    }

    public static String checkString(String string) {
        return (string == null || string.isEmpty() || string.equals("null") || string.equals("NULL")) ? "" : string.replace('"', ' ');
    }

    public static String getOutputMediaFile() {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath());
        return mediaFile.getAbsolutePath();
    }

//    public static String compressImage(Context context, String imageFile) {
//        // return Compressor.getDefault(context).compressToFile(new File(imageFile)).getAbsolutePath();
//        return new Compressor.Builder(context)
//                .setQuality(100)
//                .setCompressFormat(Bitmap.CompressFormat.PNG)
//                .setDestinationDirectoryPath(getOutputMediaFile())
//                .build()
//                .compressToFile(new File(imageFile)).getAbsolutePath();
//    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static int getFactorial(int i) {
        int initial = 1;
        for (int x = initial; x <= i; x++) {
            initial = initial * x;
        }
        return initial;
    }


    public static int getPossibleCombinations(int totalValues, int valuesToCombine) {
        /**
         * n!/(n-r)!r!
         *
         */
        return getFactorial(totalValues) / (getFactorial(totalValues - valuesToCombine) * getFactorial(valuesToCombine));
    }

    public static ArrayList<int[]> permute(Object[] elements, int K) {
        ArrayList<int[]> arrayList = new ArrayList<>();

        // get the length of the array
        // e.g. for {'A','B','C','D'} => N = 4
        int N = elements.length;

        if (K > N) {
            return arrayList;
        }
        // calculate the possible combinations
        // e.g. c(4,2)
        getPossibleCombinations(elements.length, K);

        // get the combination by index
        // e.g. 01 --> AB , 23 --> CD
        int combination[] = new int[K];

        // position of current index
        //  if (r = 1)				r*
        //	index ==>		0	|	1	|	2
        //	element ==>		A	|	B	|	C
        int r = 0;
        int index = 0;

        while (r >= 0) {
            // possible indexes for 1st position "r=0" are "0,1,2" --> "A,B,C"
            // possible indexes for 2nd position "r=1" are "1,2,3" --> "B,C,D"

            // for r = 0 ==> index < (4+ (0 - 2)) = 2
            if (index <= (N + (r - K))) {
                combination[r] = index;

                // if we are at the last position print and increase the index
                if (r == K - 1) {

                    //do something with the combination e.g. add to list or print
                    arrayList.add(combination);
                    index++;
                } else {
                    // select index for next position
                    index = combination[r] + 1;
                    r++;
                }
            } else {
                r--;
                if (r > 0)
                    index = combination[r] + 1;
                else
                    index = combination[0] + 1;
            }
        }
        return arrayList;
    }

//    public static void displayImage(Context context, ImageView imageView, String imagePath) {
//        if (imagePath != null && !imagePath.equals("")) {
//            if (imagePath.startsWith("images/")) {
//                ImageLoader.getInstance().displayImage(Connect.url + imagePath, imageView);
//            } else {
//                if (imagePath.startsWith("/")) {
//                    imageView.setImageURI(Uri.fromFile(new File(imagePath)));
//                } else {
//                    imageView.setImageResource(R.mipmap.ic_account_circle_black_48dp);
//                }
//            }
//        } else {
//            imageView.setImageResource(R.mipmap.ic_account_circle_black_48dp);
//        }
//    }

    public static String convertToTwo(int numberToConvert) {
        if (String.valueOf(numberToConvert).length() >= 2) {
            return String.valueOf(numberToConvert);
        }
        return "0" + String.valueOf(numberToConvert);
    }

    public static boolean isDateAGreaterThanB(String dateA, String dateB) {
        int startDay = Integer.parseInt(dateA.split("\\-")[2]);
        int startMonth = Integer.parseInt(dateA.split("\\-")[1]);
        int startYear = Integer.parseInt(dateA.split("\\-")[0]);
        int endDay = Integer.parseInt(dateB.split("\\-")[2]);
        int endMonth = Integer.parseInt(dateB.split("\\-")[1]);
        int endYear = Integer.parseInt(dateB.split("\\-")[0]);

        if (startYear > endYear) {
            return true;
        }

        if (startYear < endYear) {
            return false;
        }

        if (startYear == endYear) {
            if (startMonth > endMonth) {
                return true;
            }

            if (startMonth < endMonth) {
                return false;
            }

            if (startMonth == endMonth) {
                if (startDay > endDay) {
                    return true;
                }
            }
        }

        return false;
    }

    public static long downloadFile(Context context, String url) {

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
            return 1;
        }

        ResponseHandler responseHandler = new ResponseHandler(context);
        responseHandler.showToast("Downloading...");
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadURL = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(downloadURL);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url.substring(4));
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        return downloadManager.enqueue(request);
    }

    public static ArrayList<String> getMonths() {
        ArrayList<String> monthsArray = new ArrayList<>();
        monthsArray.add("Jan");
        monthsArray.add("Feb");
        monthsArray.add("Mar");
        monthsArray.add("Apr");
        monthsArray.add("May");
        monthsArray.add("Jun");
        monthsArray.add("Jul");
        monthsArray.add("Aug");
        monthsArray.add("Sep");
        monthsArray.add("Oct");
        monthsArray.add("Nov");
        monthsArray.add("Dec");
        return monthsArray;
    }

    public static ArrayList<String> getMonthsColor() {
        ArrayList<String> monthsArray = new ArrayList<>();
        monthsArray.add("#B71C1C");
        monthsArray.add("#311B92");
        monthsArray.add("#FFD600");
        monthsArray.add("#43A047");
        monthsArray.add("#E64A19");
        monthsArray.add("#880E4F");
        monthsArray.add("#33691E");
        monthsArray.add("#4E342E");
        monthsArray.add("#EF6C00");
        monthsArray.add("#AFB42B");
        monthsArray.add("#00897B");
        monthsArray.add("#2196F3");
        return monthsArray;
    }

    public static ArrayList<String> getDays() {
        ArrayList<String> monthsArray = new ArrayList<>();
        monthsArray.add("Sun");
        monthsArray.add("Mon");
        monthsArray.add("Tue");
        monthsArray.add("Wed");
        monthsArray.add("Thu");
        monthsArray.add("Fri");
        monthsArray.add("Sat");
        return monthsArray;
    }

    public static ArrayList<String> getDaysColor() {
        ArrayList<String> monthsArray = new ArrayList<>();
        monthsArray.add("#B71C1C");
        monthsArray.add("#311B92");
        monthsArray.add("#FFD600");
        monthsArray.add("#43A047");
        monthsArray.add("#E64A19");
        monthsArray.add("#880E4F");
        monthsArray.add("#33691E");
        return monthsArray;
    }

    public static ArrayList<String> getYears() {
        ArrayList<String> monthsArray = new ArrayList<>();
        monthsArray.add("2016");
        monthsArray.add("2017");
        monthsArray.add("2018");
        monthsArray.add("2019");
        return monthsArray;
    }

    public static ArrayList<String> getHours() {
        ArrayList<String> monthsArray = new ArrayList<>();
        monthsArray.add("00");
        monthsArray.add("01");
        monthsArray.add("02");
        monthsArray.add("03");
        monthsArray.add("04");
        monthsArray.add("05");
        monthsArray.add("06");
        monthsArray.add("07");
        monthsArray.add("08");
        monthsArray.add("09");
        monthsArray.add("10");
        monthsArray.add("11");
        monthsArray.add("12");
        monthsArray.add("13");
        monthsArray.add("14");
        monthsArray.add("15");
        monthsArray.add("16");
        monthsArray.add("17");
        monthsArray.add("18");
        monthsArray.add("19");
        monthsArray.add("20");
        monthsArray.add("21");
        monthsArray.add("22");
        monthsArray.add("23");
        return monthsArray;
    }

    public static ArrayList<String> getHoursColor() {
        ArrayList<String> monthsArray = new ArrayList<>();
        monthsArray.add("#B71C1C");
        monthsArray.add("#311B92");
        monthsArray.add("#FFD600");
        monthsArray.add("#43A047");
        monthsArray.add("#E64A19");
        monthsArray.add("#880E4F");
        monthsArray.add("#33691E");
        monthsArray.add("#4E342E");
        monthsArray.add("#EF6C00");
        monthsArray.add("#AFB42B");
        monthsArray.add("#00897B");
        monthsArray.add("#2196F3");
        monthsArray.add("#B71C1C");
        monthsArray.add("#311B92");
        monthsArray.add("#FFD600");
        monthsArray.add("#43A047");
        monthsArray.add("#E64A19");
        monthsArray.add("#880E4F");
        monthsArray.add("#33691E");
        monthsArray.add("#4E342E");
        monthsArray.add("#EF6C00");
        monthsArray.add("#AFB42B");
        monthsArray.add("#00897B");
        monthsArray.add("#2196F3");
        return monthsArray;
    }


    public static ArrayList<String> getColors() {
        ArrayList<String> monthsArray = new ArrayList<>();
        monthsArray.add("#B71C1C");
        monthsArray.add("#311B92");
        monthsArray.add("#FFD600");
        monthsArray.add("#43A047");
        monthsArray.add("#E64A19");
        monthsArray.add("#880E4F");
        monthsArray.add("#33691E");
        monthsArray.add("#4E342E");
        monthsArray.add("#EF6C00");
        monthsArray.add("#AFB42B");
        monthsArray.add("#00897B");
        monthsArray.add("#2196F3");
        monthsArray.add("#B71C1C");
        monthsArray.add("#311B92");
        monthsArray.add("#FFD600");
        monthsArray.add("#43A047");
        monthsArray.add("#E64A19");
        monthsArray.add("#880E4F");
        monthsArray.add("#33691E");
        monthsArray.add("#4E342E");
        monthsArray.add("#EF6C00");
        monthsArray.add("#AFB42B");
        monthsArray.add("#00897B");
        monthsArray.add("#2196F3");
        monthsArray.add("#B71C1C");
        monthsArray.add("#311B92");
        monthsArray.add("#FFD600");
        monthsArray.add("#43A047");
        monthsArray.add("#E64A19");
        monthsArray.add("#880E4F");
        monthsArray.add("#33691E");
        monthsArray.add("#4E342E");
        monthsArray.add("#EF6C00");
        monthsArray.add("#AFB42B");
        monthsArray.add("#00897B");
        monthsArray.add("#2196F3");
        return monthsArray;
    }

    private static File getAppDataFolder(Context context) {
        return context.getFilesDir();
    }

    public static String getDisplaySectionName(String name) {
        switch (name) {
            case "Squirrels_3_6yrs":
                return "Squirrels (3 - 6 yrs)";
            case "Sunguras_7_11yrs":
                return "Sunguras (7 - 11 yrs)";
            case "Chipukizi_12_15yrs":
                return "Chipukizi (12 - 15yrs)";
            case "Mwambas_16_18yrs":
                return "Mwambas (16 - 18yrs)";
            case "Rovers_Jasiri_18_26yrs":
                return "Rovers/Jasiri (18 - 26yrs)";
            case "Adults__x003E_26yrs":
                return "Adults > (26yrs)";
            default:
                return null;
        }
    }


    public static void viewAsset(Context context, AssetItem assetItem) {
        final Dialog dialog = new Dialog(context, R.style.AppThemeLight);
        dialog.setContentView(R.layout.dialog_view_asset);
        dialog.setCancelable(false);
        dialog.show();

        TextView tTitle = dialog.findViewById(R.id.title);
        tTitle.setText(assetItem.getAssetTag());

        ImageButton btnNegative = dialog.findViewById(R.id.btnCancel);
        final ImageButton btnDone = dialog.findViewById(R.id.btnCreate);

        btnDone.setVisibility(View.INVISIBLE);

        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView tDetails = dialog.findViewById(R.id.itemSubName);
        tDetails.setText("Asset Tag : " + assetItem.getAssetTag() + "\n\n" +
                "Status : " + assetItem.getStatus() + "\n\n" +
                "Email : " + assetItem.getEmail() + "\n\n" +
                "Serial : " + assetItem.getSerialNumber() + "\n\n" +
                "Model Name : " + assetItem.getModelNo() + "\n\n" +
                "Purchase Date : " + assetItem.getPurchaseDate() + "\n\n" +
                "Purchase Cost : " + assetItem.getPurchaseCost() + "\n\n" +
                "SOF : " + assetItem.getSof() + "\n\n" +
                "Project Code : " + assetItem.getProjectCode() + "\n\n" +
                "DEA : " + assetItem.getDEA() + "\n\n" +
                "Account Code : " + assetItem.getAccountCode() + "\n\n" +
                "Supplier : " + assetItem.getSupplier() + "\n\n" +
                "Warranty : " + assetItem.getWarranty() + "\n\n" +
                "Comments : " + assetItem.getComments() + "\n\n" +
                "Created at : " + assetItem.getCreatedAt() + "\n\n" +
                "Default Office : " + assetItem.getDefaultOffice() + "\n\n" +
                "Location : " + assetItem.getLocation() + "\n\n"
        );


    }
}
