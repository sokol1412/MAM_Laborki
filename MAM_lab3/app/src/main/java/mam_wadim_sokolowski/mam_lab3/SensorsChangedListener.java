package mam_wadim_sokolowski.mam_lab3;

import android.location.Location;

public interface SensorsChangedListener {
    void onLocationChanged(Location currentLocation);
}
