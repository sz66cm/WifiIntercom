package com.wifitalk.Utils;

import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.util.Log;

/**
 * ��Ϣ������
 * @author chenming
 *
 */
public class NetDealUtil {
	public static final String TAG = NetDealUtil.class.getSimpleName();
	/**
	 * ����DHCP��Ϣ������㲥��ַ
	 * @param info
	 * @return
	 */
	public static String getBrocastAddress(DhcpInfo info) {
		//��ǰip��ַ
		int ip = reverseInt(info.ipAddress);
		//��������
		int mask = info.netmask;
		//��ȡ�㲥��ַ
		int ib = ( ip & mask) | (~mask);
		String strBrocastAddress = FormatString(ib);
		Log.i(TAG, "getBrocastAddress() -> "+strBrocastAddress);
		return strBrocastAddress;
	}
	/**
	 * ����WifiInfo��Ϣ������㲥��ַ
	 * @param info
	 * @return
	 */
	public static String getBrocastAddress(WifiInfo info, DhcpInfo dinfo) {
		
		//��ǰip��ַ, �ߵ�λ�任�� �����ַ -> �������ַ
		int oip = info.getIpAddress();
		int ip = reverseInt(oip);
		//��������,��ʱ�̶�255.255.255.0
		int mask = 0xFFFFFF00;
		//��ȡ�㲥��ַ
		int ib = ( ip & mask) | (~mask);
		String strBrocastAddress = FormatString(ib);
		Log.i(TAG, "getBrocastAddress() -> "+strBrocastAddress);
		return strBrocastAddress;
	}
	/**
	 * ��ʽ��ip��ַ
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
	 * ����ת�ֽ�����
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
	 * �ֽ�����ת����
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
	 * �����ֽڷ�ת���θߵ�λ˳��ߵ�.
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
