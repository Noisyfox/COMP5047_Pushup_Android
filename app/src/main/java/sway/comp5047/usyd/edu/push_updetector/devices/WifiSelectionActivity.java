package sway.comp5047.usyd.edu.push_updetector.devices;

import android.content.Intent;
import android.os.Bundle;

import sway.comp5047.usyd.edu.push_updetector.AccessPoint;
import sway.comp5047.usyd.edu.push_updetector.R;

public class WifiSelectionActivity extends BaseWifiConnectionActivity {

    @Override
    protected void onSetContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wifi_selection);
    }

    @Override
    protected boolean filterAccessPoint(AccessPoint ap) {
        return true;
    }

    @Override
    protected void onAPConnected(AccessPoint ap) {
        removeConnectedAP();
        Intent intent = new Intent(this, DeviceConnectionActivity.class);
        intent.putExtra("AP", ap);
        startActivity(intent);
    }
}
