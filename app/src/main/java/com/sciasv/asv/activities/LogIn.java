package com.sciasv.asv.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.MsalException;
import com.microsoft.identity.client.MsalServiceException;
import com.microsoft.identity.client.MsalUiRequiredException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;
import com.sciasv.asv.R;
import com.sciasv.asv.handlers.JSONHandler;
import com.sciasv.asv.models.ProfileHolder;
import com.sciasv.asv.network.Connect;
import com.sciasv.asv.utils.ResponseHandler;
import com.sciasv.asv.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class LogIn extends AppCompatActivity {

    EditText eUsername, ePassword;
    ResponseHandler responseHandler;
    ProfileHolder profileHolder;
    Context context;
    final static String CLIENT_ID = "06684fc4-fe2e-414b-9455-6dc138a196e8";
    final static String SCOPES[] = {"https://graph.microsoft.com/User.Read"};
    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";

    /* Azure AD Variables */
    private PublicClientApplication sampleApp;
    private AuthenticationResult authResult;
    private static final String TAG = LogIn.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        context = this;
        responseHandler = new ResponseHandler(this);

        profileHolder = new ProfileHolder(this);
        if (profileHolder.userHasLoggedIn()) {
            startActivity(new Intent(this, Home.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        eUsername = findViewById(R.id.userName);
        ePassword = findViewById(R.id.password);

        sampleApp = new PublicClientApplication(
                this.getApplicationContext(),
                CLIENT_ID);

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        findViewById(R.id.msBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallGraphClicked();
            }
        });

    }


    public Activity getActivity() {
        return this;
    }

    /* Callback method for acquireTokenSilent calls
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */
    private AuthenticationCallback getAuthSilentCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                /* Successfully got a token, call Graph now */
                Log.d(TAG, "Successfully authenticated");

                /* Store the authResult */
                authResult = authenticationResult;

                /* call graph */
                callGraphAPI();

                /* update the UI to post call Graph state */
                updateSuccessUI();
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                } else if (exception instanceof MsalUiRequiredException) {
                    /* Tokens expired or no session, retry with interactive */
                }
            }

            @Override
            public void onCancel() {
                /* User cancelled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

    /* Callback used for interactive request.  If succeeds we use the access
     * token to call the Microsoft Graph. Does not check cache
     */
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");
                Log.d(TAG, "ID Token: " + authenticationResult.getIdToken());

                /* Store the auth result */
                authResult = authenticationResult;

                /* call Graph */
                callGraphAPI();

                /* update the UI to post call Graph state */
                updateSuccessUI();
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            @Override
            public void onCancel() {
                /* User cancelled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

    /* Set the UI for successful token acquisition data */
    private void updateSuccessUI() {
        responseHandler.showToast("Welcome, " + authResult.getUser().getName());
    }

    /* Use MSAL to acquireToken for the end-user
     * Callback will call Graph api w/ access token & update UI
     */
    private void onCallGraphClicked() {
        List<User> users = null;

        try {
            users = sampleApp.getUsers();

            if (users != null && users.size() == 1) {
                /* We have 1 user */

                sampleApp.acquireTokenSilentAsync(SCOPES, users.get(0), getAuthSilentCallback());
            } else {
                /* We have no user */

                /* Let's do an interactive request */
                sampleApp.acquireToken(this, SCOPES, getAuthInteractiveCallback());
            }
        } catch (MsalClientException e) {
            Log.d(TAG, "MSAL Exception Generated while getting users: " + e.toString());

        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "User at this position does not exist: " + e.toString());
        }


    }

    /* Handles the redirect from the System Browser */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        sampleApp.handleInteractiveRequestRedirect(requestCode, resultCode, data);
    }


    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    private void callGraphAPI() {
        Log.d(TAG, "Starting volley request to graph");

        /* Make sure we have a token to send to graph */
        if (authResult.getAccessToken() == null) {
            return;
        }


        final ProgressDialog progressDialog = new ProgressDialog(context, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.get(MSGRAPH_URL)
                .addQueryParameter("key", "value")
                .addHeaders("Authorization", "Bearer " + authResult.getAccessToken())
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        /* Successfully called graph, process data and send to UI */
                        Log.d(TAG, "Response: " + response.toString());

                        updateGraphUI(response);
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


    private void loginOrRegister(String email, String fName, String lName) {
        final ProgressDialog progressDialog = new ProgressDialog(context, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.post(Connect.url + Connect.ms)
                .addBodyParameter("email", email)
                .addBodyParameter("f_name", fName)
                .addBodyParameter("l_name", lName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        Log.v(Connect.tag, response);

                        progressDialog.dismiss();

                        JSONHandler jsonHandler = new JSONHandler(context);

                        jsonHandler.login(response);

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

    /* Sets the Graph response */
    private void updateGraphUI(JSONObject responseObject) {
        try {
            loginOrRegister(responseObject.getString("userPrincipalName"),
                    responseObject.getString("givenName"),
                    responseObject.getString("surname"));

        } catch (JSONException e) {
            Log.v(TAG, e.getMessage());
        }

    }


    @Override
    public void onBackPressed() {
        final PrettyDialog pDialog = new PrettyDialog(this);
        pDialog.setCancelable(false);
        pDialog.setIcon(
                R.drawable.pdlg_icon_info,
                R.color.pdlg_color_green, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        pDialog.dismiss();
                    }
                })
                .setTitle("Exit app?")
                .setMessage("Do you really want to exit app?")
                .addButton(
                        "NO",
                        R.color.pdlg_color_white,
                        R.color.pdlg_color_green,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                pDialog.dismiss();
                            }
                        }
                )
                .addButton(
                        "YES",
                        R.color.pdlg_color_white,
                        R.color.pdlg_color_red,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                Intent startMain = new Intent(Intent.ACTION_MAIN);
                                startMain.addCategory(Intent.CATEGORY_HOME);
                                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(startMain);
                            }
                        }
                )
                .show();
    }

    private void logIn() {

        if (Utils.isEmpty(eUsername)) {
            responseHandler.showToast("Username is required");
            return;
        }


        if (Utils.isEmpty(ePassword)) {
            responseHandler.showToast("Password is required");
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(context, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.post(Connect.url + Connect.login)
                .addBodyParameter("username", Utils.getText(eUsername))
                .addBodyParameter("password", Utils.getText(ePassword))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        JSONHandler jsonHandler = new JSONHandler(context);

                        jsonHandler.login(response);

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

}
