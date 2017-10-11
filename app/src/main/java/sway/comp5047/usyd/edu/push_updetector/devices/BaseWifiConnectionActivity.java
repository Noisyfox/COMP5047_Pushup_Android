package sway.comp5047.usyd.edu.push_updetector.devices;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import sway.comp5047.usyd.edu.push_updetector.AccessPoint;
import sway.comp5047.usyd.edu.push_updetector.R;

/**
 * Created by noisyfox on 2017/10/8.
 */

public abstract class BaseWifiConnectionActivity extends AppCompatActivity {

    private WifiManager mWifiManager;
    private final HashMap<String, String> mSavedPassword = new HashMap<>();
    private ArrayAdapter<AccessPoint> mAdapter;
    private ListView mWifiList;
    private ProgressBar mProgressBar;

    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> mScanResults = mWifiManager.getScanResults();

                mAdapter.clear();
                mAdapter.addAll(mapToAccessPoint(mScanResults));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        onSetContentView(savedInstanceState);

        mWifiList = (ListView) findViewById(android.R.id.list);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                mProgressBar.setProgress((int) (100 * interpolatedTime));
            }
        };
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                mWifiManager.startScan();
            }
        });
        animation.setDuration(5000);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setInterpolator(new LinearInterpolator());
        mProgressBar.startAnimation(animation);

        mAdapter = new ArrayAdapter<>(this, R.layout.view_wifi, R.id.label_wifi);
        mWifiList.setAdapter(mAdapter);
        mWifiList.setOnItemClickListener((parent, view, position, id) -> {
            onAPSelected(mAdapter.getItem(position));
        });

        checkPermission();

        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mAdapter.addAll(mapToAccessPoint(mWifiManager.getScanResults()));
    }

    private List<AccessPoint> mapToAccessPoint( List<ScanResult> scanResults )
    {
        return scanResults
                .stream()
                .map( r ->
                {
                    AccessPoint ap = new AccessPoint( r );
                    ap.password = mSavedPassword.getOrDefault( ap.SSID, null );
                    return ap;
                } )
                .filter( BaseWifiConnectionActivity.this::filterAccessPoint )
                .distinct()
                .collect( Collectors.toList() );
    }

    protected abstract void onSetContentView(Bundle savedInstanceState);

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mWifiScanReceiver);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    private void onAPSelected(AccessPoint ap) {
        if (ap.security != AccessPoint.OPEN) {
            // Ask for a password
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter Wifi Password");

            // Set up the input
            final EditText input = new EditText(this);
            if (ap.password != null) {
                input.setText(ap.password);
            }
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);//| InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("Submit", (dialog, which) -> {
                mSavedPassword.put(ap.SSID, ap.password);
                ap.password = input.getText().toString();
                connectToAP(ap);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        } else {
            connectToAP(ap);
        }
    }

    private void connectToAP(AccessPoint ap) {
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + ap.SSID + "\"";

        switch (ap.security) {
            case AccessPoint.OPEN:
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                break;
            case AccessPoint.WPA:
            case AccessPoint.WPA2:
                conf.preSharedKey = "\"" + ap.password + "\"";
                break;
            case AccessPoint.WEP:
                conf.wepKeys[0] = "\"" + ap.password + "\"";
                conf.wepTxKeyIndex = 0;
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                break;
            default:
                return;
        }

        int netId = mWifiManager.addNetwork(conf);
        mWifiManager.disconnect();
        mWifiManager.enableNetwork(netId, true);
        mWifiManager.reconnect();

        ProgressDialog pd = ProgressDialog.show(this, null, "Connecting to " + conf.SSID + " WiFi (30)", true, true);
        Animation checkConnectionAnimation = new Animation() {
        };
        checkConnectionAnimation.setRepeatCount(Animation.INFINITE);
        checkConnectionAnimation.setDuration(1000);
        checkConnectionAnimation.setAnimationListener(new Animation.AnimationListener() {
            int countDown = 30;

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(
                        Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (wifiNetwork != null && wifiNetwork.isConnected()) {
                    pd.dismiss();
                    onAPConnected(ap);
                    return;
                }

                countDown--;
                pd.setMessage("Connecting to " + conf.SSID + " WiFi (" + countDown + ")");
                if (countDown <= 0) {
                    pd.cancel();
                }
            }
        });
        pd.findViewById(android.R.id.progress).startAnimation(checkConnectionAnimation);

    }

    protected abstract boolean filterAccessPoint(AccessPoint ap);

    protected abstract void onAPConnected(AccessPoint ap);
}
