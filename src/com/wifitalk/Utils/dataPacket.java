package com.wifitalk.Utils;
//www.javaapk.com
public class dataPacket
{
	public static final int RECOMMEND_SIZE = 320; 
	public static final int HEAD_LENGTH = 30;
	public static final int BODY_LENGTH = RECOMMEND_SIZE - HEAD_LENGTH;
	
	private byte[] recordBytes = new byte[HEAD_LENGTH + BODY_LENGTH];

	public dataPacket(byte[] headInfo, byte[] bodyBytes)
	{
		for (int i = 0; i < headInfo.length; i++)
		{
			recordBytes[i] = headInfo[i];
		}
		for (int i = 0; i < bodyBytes.length; i++)
		{
			recordBytes[i + HEAD_LENGTH] = bodyBytes[i];
		}
	}

	public byte[] getHeadInfo()
	{
		byte[] head = new byte[HEAD_LENGTH];
		for (int i = 0; i < head.length; i++)
		{
			head[i] = recordBytes[i];
		}
		return head;
	}

	public byte[] getBody()
	{
		byte[] body = new byte[BODY_LENGTH];
		for (int i = 0; i < body.length; i++)
		{
			body[i] = recordBytes[i + HEAD_LENGTH];
		}
		return body;
	}

	public byte[] getAllData()
	{
		byte[] data = new byte[HEAD_LENGTH + BODY_LENGTH];
		for (int i = 0; i < data.length; i++)
		{
			data[i] = recordBytes[i];
		}
		return data;
	}
}
