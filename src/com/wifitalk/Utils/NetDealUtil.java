package com.wifitalk.Utils;

import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.util.Log;

/**
 * 信息处理类
 * @author chenming
 *
 */
public class NetDealUtil {
	public static final String TAG = NetDealUtil.class.getSimpleName();
	/**
	 * 根据DHCP信息计算出广播地址
	 * @param info
	 * @return
	 */
	public static String getBrocastAddress(DhcpInfo info) {
		//当前ip地址
		int ip = reverseInt(info.ipAddress);
		//子网掩码
		int mask = info.netmask;
		//获取广播地址
		int ib = ( ip & mask) | (~mask);
		String strBrocastAddress = FormatString(ib);
		Log.i(TAG, "getBrocastAddress() -> "+strBrocastAddress);
		return strBrocastAddress;
	}
	/**
	 * 根据WifiInfo信息计算出广播地址
	 * @param info
	 * @return
	 */
	public static String getBrocastAddress(WifiInfo info, DhcpInfo dinfo) {
		
		//当前ip地址, 高低位变换成 网络地址 -> 计算机地址
		int oip = info.getIpAddress();
		int ip = reverseInt(oip);
		//子网掩码,暂时固定255.255.255.0
		int mask = 0xFFFFFF00;
		//获取广播地址
		int ib = ( ip & mask) | (~mask);
		String strBrocastAddress = FormatString(ib);
		Log.i(TAG, "getBrocastAddress() -> "+strBrocastAddress);
		return strBrocastAddress;
	}
	/**
	 * 格式化ip地址
	 * @param value
	 * @return
	 */
	public static String FormatString(int value) {
		StringBuilder strb = new StringBuilder();
		byte[] ary = intToByteArray(value);
		for (int i = 0; i < ary.length; i++) {
			strb.append(ary[i] & 0xFF);
			if(i < ary.length - 1) {
				strb.append(".");
			}
		}
		return strb.toString();
	}
	/**
	 * 整型转字节数组
	 * @param value
	 * @return
	 */
	public static byte[] intToByteArray(int value) {
		byte[] b = new byte[4];
		for (int i = 0; i < b.length; i++) {
			int offset = (b.length -1 -i) * 8;
			b[i] = (byte) ((value >>> offset) & 0xFF);
		}
		return b;
	}
	/**
	 * 字节数组转整型
	 * @param bytes
	 * @return
	 */
	public static int byteArrayToInt(byte[] bytes) {
		int i = 0;
		for (int j = 0; j < bytes.length; j++) {
			if(j > 0) {
				i = i << 8;
			}
			i |= (bytes[j] & 0xFF);
		}
		return i;
	}
	/**
	 * 按照字节翻转整形高低位顺序颠倒.
	 * @param ori
	 * @return
	 */
	public static int reverseInt(int ori) {
		int result = 0;
		byte[] oribyte = intToByteArray(ori);
		int startIndex = 0;
		int endIndex = oribyte.length - 1;
		while (startIndex < endIndex) {
			byte temp = oribyte[startIndex];
			oribyte[startIndex] = oribyte[endIndex]; 
			oribyte[endIndex] = temp;
			startIndex++;
			endIndex--;
		}
		result = byteArrayToInt(oribyte);
		return result;
	}
}
