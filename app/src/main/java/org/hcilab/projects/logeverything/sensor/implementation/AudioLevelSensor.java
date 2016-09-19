package org.hcilab.projects.logeverything.sensor.implementation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.hcilab.projects.logeverything.activity.CONST;
import org.hcilab.projects.logeverything.sensor.AbstractSensor;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;
import android.view.View;

public class AudioLevelSensor extends AbstractSensor {
	
	private static final long serialVersionUID = 1L;

	private static final int RECORDING_TIME = 1000;
	
	private final Handler handler = new Handler();
	private MediaRecorder mediaRecorder;
	
	public AudioLevelSensor() {
		m_IsRunning = false;
		TAG = getClass().getName();
		SENSOR_NAME = "Audio Level";
		FILE_NAME = "audiolevel.csv";
	}
		
	public View getSettingsView(Context context) {	
		return null;
	}
	
	public boolean isAvailable(Context context) {
		return true;
	}
	
	@Override
	public void start(Context context) {
		super.start(context);
		if (!m_isSensorAvailable)
			return;
		
		if (this.m_FileWriter == null)
		{
			try {
				m_FileWriter = new FileWriter(new File(getFilePath()), true);
				m_FileWriter.write("timestamp,value");
				m_FileWriter.write("\n");
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}	
		}		
		try {
			handler.postDelayed(new Runnable() {
				public void run() {
					if (initMediaRecorder()) {

						getNoiseLevel();
						if (m_IsRunning) {
							handler.postDelayed(this, RECORDING_TIME);
						} else if(mediaRecorder != null) {
							mediaRecorder.stop();
							mediaRecorder.reset();
							mediaRecorder.release();
							mediaRecorder = null;
						}
					}
				}
			}, 1000);
			m_IsRunning = true;
			m_FileWriter.flush();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		
	}
	
	private boolean initMediaRecorder() {
		try {
			if (mediaRecorder != null)
				return true;
			
			try {
				Log.d(TAG, "initMediaRecorder");
				mediaRecorder = new MediaRecorder();
				mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
				mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
				mediaRecorder.setOutputFile("/dev/null");			
				mediaRecorder.prepare();
				mediaRecorder.start();
				return true;
			} catch (IllegalStateException | IOException e) {
				Log.e(TAG, e.toString());
			}
		} catch(Exception e) {
			Log.d(TAG, e.toString());
		}
		return false;
	}
	
	private void getNoiseLevel() {
		if (mediaRecorder == null)
			return;
		
		try {
			int amplitude = mediaRecorder.getMaxAmplitude();
			if (m_FileWriter!=null)
			m_FileWriter.write(getTime()+","+amplitude+"\n");
			
		} catch (IOException e) {
			Log.d(TAG, e.toString());
		}
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