package de.mimuc.senseeverything.service;

import java.util.List;

import de.mimuc.senseeverything.sensor.AbstractSensor;
import de.mimuc.senseeverything.sensor.SensorList;

import android.content.Intent;
import android.util.Log;

public class LogService extends AbstractService {

	private List<AbstractSensor> sensorList = null;

	@Override
	public void onCreate() {
		TAG = getClass().getName();
		super.onCreate();
	}	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int ret = super.onStartCommand(intent, flags, startId);
		
		sensorList = SensorList.getList(this);
		
		Log.d(TAG, "size: "+sensorList.size());
		for(AbstractSensor sensor : sensorList) {
			if (sensor.isEnabled() && sensor.isAvailable(this))
			{
				sensor.start(this);
				//if(sensor instanceof MyAccelerometerSensor) ((MyAccelerometerSensor)sensor).start(this);
				//if(sensor instanceof AppSensor) ((AppSensor)sensor).start(this);

				Log.d(TAG, sensor.getSensorName() + " turned on");
			}
			else
			{
				Log.w(TAG, sensor.getSensorName() + " turned off");
			}
		}
		
		return ret;
	}

	@Override
	public void onDestroy() {
		for(AbstractSensor sensor : sensorList) {
			if (sensor.isRunning())
			{
				sensor.stop();
			}
		}
		super.onDestroy();
	}
}
