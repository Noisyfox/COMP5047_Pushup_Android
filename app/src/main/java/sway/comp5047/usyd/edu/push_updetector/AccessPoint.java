package sway.comp5047.usyd.edu.push_updetector;

import android.net.wifi.ScanResult;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by noisyfox on 2017/10/7.
 */

public class AccessPoint implements Parcelable {

    // Constants used for different security types
    public static final String WPA2 = "WPA2";
    public static final String WPA = "WPA";
    public static final String WEP = "WEP";
    public static final String OPEN = "Open";

    /* For EAP Enterprise fields */
    public static final String WPA_EAP = "WPA-EAP";
    public static final String IEEE8021X = "IEEE8021X";

    public String SSID;
    public String security;
    public String password;

    public AccessPoint() {

    }

    public AccessPoint(ScanResult result) {
        SSID = result.SSID;
        security = getScanResultSecurity(result);
    }

    @Override
    public String toString() {
        return SSID;
    }

    public static String getScanResultSecurity(ScanResult scanResult) {
        final String cap = scanResult.capabilities;
        final String[] securityModes = {WEP, WPA, WPA2, WPA_EAP, IEEE8021X};
        for (int i = securityModes.length - 1; i >= 0; i--) {
            if (cap.contains(securityModes[i])) {
                return securityModes[i];
            }
        }

        return OPEN;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.SSID);
        dest.writeString(this.security);
        dest.writeString(this.password);
    }

    protected AccessPoint(Parcel in) {
        this.SSID = in.readString();
        this.security = in.readString();
        this.password = in.readString();
    }

    public static final Parcelable.Creator<AccessPoint> CREATOR = new Parcelable.Creator<AccessPoint>() {
        @Override
        public AccessPoint createFromParcel(Parcel source) {
            return new AccessPoint(source);
        }

        @Override
        public AccessPoint[] newArray(int size) {
            return new AccessPoint[size];
        }
    };
}
