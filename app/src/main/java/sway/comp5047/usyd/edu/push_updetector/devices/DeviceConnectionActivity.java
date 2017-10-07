package sway.comp5047.usyd.edu.push_updetector.devices;

import android.os.Bundle;

import sway.comp5047.usyd.edu.push_updetector.AccessPoint;
import sway.comp5047.usyd.edu.push_updetector.R;

public class DeviceConnectionActivity extends BaseWifiConnectionActivity {

    @Override
    protected void onSetContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_device_connection);
    }

    @Override
    protected boolean filterAccessPoint(AccessPoint ap) {
        return true;
    }

    @Override
    protected void onAPConnected(AccessPoint ap) {

    }
}
