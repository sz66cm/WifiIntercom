package com.wifitalk.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * ��ȡ������Ϣ������
 * @author chenming
 *
 */
public class NetWorkInfoUtil {
	/**
	 * ��ȡDHCP��Ϣ
	 * @param ctx
	 * @return
	 */
	public static DhcpInfo getDhcpInfo(Context ctx) {
		DhcpInfo info = null;
		WifiManager wfm = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
		info = wfm.getDhcpInfo();
		return info;
	}
	/**
	 * ��ȡwifi������Ϣ
	 * @param ctx
	 * @return
	 */
	public static WifiInfo getWifiInfo(Context ctx) {
		WifiInfo info = null;
		WifiManager wfm = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
		info = wfm.getConnectionInfo();
		//�����ж�
		if(!isConnected(ctx)){
			Toast.makeText(ctx, "��������wifi", 0).show();
		}
		return info;
	}
	/**
	 * �ж�Wifi�Ƿ�����
	 * @return
	 */
	public static boolean isConnected(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetWorkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifiNetWorkInfo.isConnected();
	}
}
