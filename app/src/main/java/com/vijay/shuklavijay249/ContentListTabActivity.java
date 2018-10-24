package com.vijay.shuklavijay249;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.vijay.shuklavijay249.UploadTest.UploadActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ContentListTabActivity extends AppCompatActivity {

    private static final String TAG = "";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public FloatingActionButton btnPlus;
    public int GET_MY_PERMISSION = 1, permission;
    String gmail = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollable_tabs);

        try {
            if (Build.VERSION.SDK_INT >= 23) {

                permission = ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.READ_SMS);

                if (permission != PackageManager.PERMISSION_GRANTED) {//Direct Permission without disclaimer dialog
                    ActivityCompat.requestPermissions(ContentListTabActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_PHONE_STATE},
                            GET_MY_PERMISSION);

                } else {
                    apiCall();
                }
            } else {
                apiCall();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(ContentListTabActivity.this,
                new String[]{Manifest.permission.READ_SMS},
                GET_MY_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length <= 0) {
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    apiCall();
                } else {
                    //not granted
//                    Log.e(MainApplication.TAG, "not granted: Dashboard " + grantResults[0]);
                    {
                        // Permission denied.
                        // Notify the user via a SnackBar that they have rejected a core permission for the
                        // app, which makes the Activity useless. In a real app, core permissions would
                        // typically be best requested during a welcome-screen flow.
                        // Additionally, it is important to remember that a permission might have been
                        // rejected without asking the user for permission (device policy or "Never ask
                        // again" prompts). Therefore, a user interface affordance is typically implemented
                        // when permissions are denied. Otherwise, your app could appear unresponsive to
                        // touches or interactions which have required permissions.
                        //                    Toast.makeText(this, R.string.permission_denied_explanation, Toast.LENGTH_LONG).show();
                        //                    finish();
                        Snackbar.make(
                                findViewById(R.id.rootview),
                                R.string.permission_denied_explanation,
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.settings, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Build intent that displays the App settings screen.
                                        Intent intent = new Intent();
                                        intent.setAction(
                                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package",
                                                BuildConfig.APPLICATION_ID, null);
                                        intent.setData(uri);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }
                }
                break;
        }
    }

    private void getEmails() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;

        // Getting all registered Google Accounts;
        // Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");

        // Getting all registered Accounts;
        Account[] accounts = AccountManager.get(ContentListTabActivity.this).getAccounts();

        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                Log.d(TAG, String.format("%s - %s", account.name, account.type));
            }
        }
    }

    public void apiCall() {
        try {
//            TelephonyManager tMgr = (TelephonyManager) ContentListTabActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            String mPhoneNumber = tMgr.getLine1Number();
//            getEmails();
            btnPlus = (FloatingActionButton) findViewById(R.id.btnPlus);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Instant Sanction");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_leftarrow);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.textcolordark));
//        }
//        toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);

            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ContentListTabActivity.this, UploadActivity.class);
                    ContentListTabActivity.this.startActivity(intent);
                }
            });
        } catch (Exception e) {

        }

    }

    public void onBackPressed() {
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new EnglishFragment(), "English");
        adapter.addFrag(new MarathiFragment(), "Marathi");
        adapter.addFrag(new HindiFragment(), "Hindi");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
