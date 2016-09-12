package com.wifitalk.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * 获取网络信息工具类
 * @author chenming
 *
 */
public class NetWorkInfoUtil {
	/**
	 * 获取DHCP信息
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
	 * 获取wifi连接信息
	 * @param ctx
	 * @return
	 */
	public static WifiInfo getWifiInfo(Context ctx) {
		WifiInfo info = null;
		WifiManager wfm = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
		info = wfm.getConnectionInfo();
		//连接判断
		if(!isConnected(ctx)){
			Toast.makeText(ctx, "请先连接wifi", 0).show();
		}
		return info;
	}
	/**
	 * 判断Wifi是否连接
	 * @return
	 */
	public static boolean isConnected(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetWorkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifiNetWorkInfo.isConnected();
	}
}
