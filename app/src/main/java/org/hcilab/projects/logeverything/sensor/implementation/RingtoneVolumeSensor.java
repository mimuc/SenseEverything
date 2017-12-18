package org.hcilab.projects.logeverything.sensor.implementation;

import java.io.IOException;

import org.hcilab.projects.logeverything.sensor.AbstractSensor;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import android.view.View;

public class RingtoneVolumeSensor extends AbstractSensor {

	private static final long serialVersionUID = 1L;
	
	public RingtoneVolumeSensor() {
		m_IsRunning = false;

		TAG = getClass().getName();
		SENSOR_NAME = "Ringtone Volume";
		FILE_NAME = "ringtone_volume.csv";
		m_FileHeader = "TimeUnix,Value";
	}
	
	@Override
	public View getSettingsView(Context context) {
		return null;
	}

	@Override
	public boolean isAvailable(Context context) {
		return true;
	}

	@Override
	public void start(Context context) {
		super.start(context);
		Long t = System.currentTimeMillis();
		if (!m_isSensorAvailable)
			return;

		AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int currentVolume = audio.getStreamVolume(AudioManager.STREAM_RING);
		try {		
			m_FileWriter.write(t + "," + currentVolume);
			m_FileWriter.write("\n");
			m_FileWriter.flush();			
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
		m_IsRunning = true;
	}

	@Override
	public void stop() {
		if(m_IsRunning) {
			m_IsRunning = false;
			try {
				m_FileWriter.flush();
				m_FileWriter.close();
				m_FileWriter = null;
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}		
		}
	}

}
