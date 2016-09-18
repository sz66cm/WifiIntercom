package com.wifitalk.Utils;

import android.annotation.SuppressLint;
import android.media.AudioRecord;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.util.Log;
/**
 * �������ƹ�����
 * @author chenming
 *
 */
@SuppressLint("NewApi")
public class AECUtil {
	/**
	 * ���
	 */
	private static final String TAG = AECUtil.class.getSimpleName();
	/**
	 * ����ʵ��
	 */
	private static final AECUtil instance = new AECUtil();
	
	private AECUtil(){};
	
	public static AECUtil getInstance() {
		return instance;
	}
	
	private AcousticEchoCanceler canceler;
	
	/**
	 * �Ƿ�֧��AEC
	 * @return
	 */
	public static boolean isSupported() {
		boolean result = AcousticEchoCanceler.isAvailable();
		if(!result) {
			Log.w(TAG, "The Device didn't support AEC!");
		}
		return result;
	}
	/**
	 * ��ʼ��AEC
	 * @param sessionId
	 * @return
	 */
	public boolean initAEC(int sessionId) {
		if (!isSupported()) {
			return false;
		}
		canceler = AcousticEchoCanceler.create(sessionId);
		canceler.setEnabled(true);
		return canceler.getEnabled();
	}
	/**
	 * ʹAEC�ܷ�
	 * @param isEnabled
	 * @return
	 */
	public boolean setEnableAEC(boolean isEnabled) {
		if(null == canceler) {
			return false;
		}
		canceler.setEnabled(isEnabled);
		return canceler.getEnabled();
	}
	/**
	 * �ͷ�AEC
	 * @return
	 */
	public boolean release() {
		if(null == canceler) {
			return true;
		}
		canceler.setEnabled(false);
		canceler.release();
		return true;
	}
	/**
	 * ��������
	 * @param ar
	 */
	public static void aec(AudioRecord ar) {
		if (AcousticEchoCanceler.isAvailable()) {
	        AcousticEchoCanceler aec = AcousticEchoCanceler.create(ar.getAudioSessionId());
	        if (aec != null && !aec.getEnabled()) {
	        	aec.setEnabled(true);
	        }
		}
		if (NoiseSuppressor.isAvailable()) {
	        NoiseSuppressor noise = NoiseSuppressor.create(ar.getAudioSessionId());
	        if (noise != null && !noise.getEnabled()) {
	        	noise.setEnabled(true);
	        }
		}
	}
	
}
