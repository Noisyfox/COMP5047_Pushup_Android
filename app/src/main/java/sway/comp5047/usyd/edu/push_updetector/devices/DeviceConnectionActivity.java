package sway.comp5047.usyd.edu.push_updetector.devices;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import java.util.Objects;

import sway.comp5047.usyd.edu.push_updetector.AccessPoint;
import sway.comp5047.usyd.edu.push_updetector.MainActivity;
import sway.comp5047.usyd.edu.push_updetector.R;

public class DeviceConnectionActivity extends BaseWifiConnectionActivity {

    @Override
    protected void onSetContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_device_connection);
    }

    @Override
    protected boolean filterAccessPoint(AccessPoint ap) {
        return Objects.equals(ap.security, AccessPoint.OPEN) && ap.SSID.startsWith("PUSHUP_");
    }

    @Override
    protected void onAPConnected(AccessPoint ap) {
        AccessPoint targetAp = getIntent().getParcelableExtra("AP");
        new DeviceConfigurationTask().execute(targetAp);
    }

    private class DeviceConfigurationTask extends AsyncTask<AccessPoint, String, String> {

        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(AccessPoint... params) {
            try {
                AccessPoint ap = params[0];

                DeviceController controller = new DeviceController();
                publishProgress("Uploading SSID...");
                if (!controller.executeCommand("APSSID=" + ap.SSID)) {
                    return "Error: Unable to upload SSID.";
                }
                publishProgress("Uploading Password...");
                if (ap.password == null) {
                    ap.password = "";
                }
                if (!controller.executeCommand("APPSW=" + ap.password)) {
                    return "Error: Unable to upload password.";
                }
                publishProgress("Reboot...");
                if (!controller.executeCommand("RST")) {
                    return "Error: Unable to reboot device.";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error: " + e;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            progressDialog.setMessage(values[0]);
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DeviceConnectionActivity.this, "Configuring Devices", null, true, false);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if (s != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeviceConnectionActivity.this);
                builder.setMessage(s);
                builder.setPositiveButton("OK", (d, a) -> d.dismiss());
                builder.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeviceConnectionActivity.this);
                builder.setMessage("Success!");
                builder.setPositiveButton("OK", (d, a) -> {
                    d.dismiss();

                    Intent upIntent = new Intent(DeviceConnectionActivity.this, MainActivity.class);
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(upIntent);
                    finish();
                });
                builder.show();
            }
        }

        @Override
        protected void onCancelled() {
            progressDialog.dismiss();
        }
    }
}
