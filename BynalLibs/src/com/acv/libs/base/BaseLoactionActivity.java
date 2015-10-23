package com.acv.libs.base;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class BaseLoactionActivity extends Activity {
	private LocationListener locationListener = null;
	private LocationManager locationManager = null;
	protected Location videoLocation = null;

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (locationListener != null && locationListener != null) {
			locationManager.removeUpdates(locationListener);
		}
	}

	protected void getVideoLocation() {
		this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);

		String provider = locationManager.getBestProvider(criteria, true);

		this.locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					videoLocation = location;
					double lat = location.getLatitude();
					double lng = location.getLongitude();

					locationManager.removeUpdates(this);
				} else {
				}
			}

			@Override
			public void onProviderDisabled(String provider) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
		};

		if (provider != null) {
			locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
		}
	}
}