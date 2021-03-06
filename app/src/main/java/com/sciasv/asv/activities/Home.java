package com.sciasv.asv.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;
import com.sciasv.asv.R;
import com.sciasv.asv.fragments.HistoryFragment;
import com.sciasv.asv.fragments.ScanFragment;
import com.sciasv.asv.models.ProfileHolder;

import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static com.sciasv.asv.activities.LogIn.CLIENT_ID;

public class Home extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ProfileHolder profileHolder;
    private PublicClientApplication sampleApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        profileHolder = new ProfileHolder(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        sampleApp = new PublicClientApplication(
                this.getApplicationContext(),
                CLIENT_ID);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

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
                    .setTitle("Log out?")
                    .setMessage("Please confirm that you want to log out.")
                    .addButton(
                            "CANCEL",
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
                                    onSignOutClicked();
                                    profileHolder.logOut();
                                }
                            }
                    )
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void onSignOutClicked() {

        /* Attempt to get a user and remove their cookies from cache */
        List<User> users = null;

        try {
            users = sampleApp.getUsers();

            if (users == null) {
                /* We have no users */

            } else if (users.size() == 1) {
                /* We have 1 user */
                /* Remove from token cache */
                sampleApp.remove(users.get(0));
            } else {
                /* We have multiple users */
                for (int i = 0; i < users.size(); i++) {
                    sampleApp.remove(users.get(i));
                }
            }

            Toast.makeText(getBaseContext(), "Signed Out!", Toast.LENGTH_SHORT)
                    .show();

        } catch (MsalClientException e) {

        } catch (IndexOutOfBoundsException e) {
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ScanFragment();
                case 1:
                    return new HistoryFragment();
            }
            return new ScanFragment();
        }

        @Override
        public int getCount() {
            return 2;
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
}
