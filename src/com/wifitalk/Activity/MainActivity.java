package com.wifitalk.Activity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.http.conn.util.InetAddressUtils;

import com.wifitalk.R;
import com.wifitalk.Config.AppConfig;
import com.wifitalk.Utils.NetDealUtil;
import com.wifitalk.Utils.NetWorkInfoUtil;
import com.wifitalk.Utils.dataPacket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
//www.javaapk.com
@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends Activity
{
	private Button speakButton;// 按住说话
	private TextView message;
	private SendSoundsThread sendSoundsThread = new SendSoundsThread();
	private ReceiveSoundsThread receiveSoundsThread = new ReceiveSoundsThread();
	private boolean isFirst = true;

	// 设备信息：手机名+Android版本
	private String DevInfo = android.os.Build.MODEL + " Android " + android.os.Build.VERSION.RELEASE;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		message = (TextView) findViewById(R.id.Message);

		speakButton = (Button) findViewById(R.id.speakButton);
		speakButton.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					message.setText("松开结束");

					if (isFirst)
					{
						sendSoundsThread.start();
						receiveSoundsThread.start();
						isFirst = false;
					}
					sendSoundsThread.setRunning(true);
					receiveSoundsThread.setRunning(false);
				}
				else if (event.getAction() == MotionEvent.ACTION_UP)
				{
					message.setText("按住说话");
					sendSoundsThread.setRunning(false);
					receiveSoundsThread.setRunning(true);
				}
				return false;
			}
		});
	}

	class SendSoundsThread extends Thread
	{
		private AudioRecord recorder = null;
		private boolean isRunning = false;
		private byte[] recordBytes = new byte[640];

		public SendSoundsThread()
		{
			super();

			// 录音机
			int recordBufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT);
			recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT, recordBufferSize);
		}

		@Override
		public synchronized void run()
		{
			super.run();
			recorder.startRecording();

			while (true)
			{
				if (isRunning)
				{
					try
					{
						DatagramSocket clientSocket = new DatagramSocket();
						DhcpInfo dhcpInfo = NetWorkInfoUtil.getDhcpInfo(MainActivity.this);
						WifiInfo wifiInfo = NetWorkInfoUtil.getWifiInfo(MainActivity.this);
						InetAddress IP = InetAddress.getByName(NetDealUtil.getBrocastAddress(wifiInfo,dhcpInfo));// 向这个网络广播
						
						// 获取音频数据
						recorder.read(recordBytes, 0, recordBytes.length);

						// 构建数据包 头+体
						dataPacket dataPacket = new dataPacket(DevInfo.getBytes(), recordBytes);

						// 构建数据报
						DatagramPacket sendPacket = new DatagramPacket(dataPacket.getAllData(),
								dataPacket.getAllData().length, IP, AppConfig.Port);

						// 发送
						clientSocket.send(sendPacket);
						clientSocket.close();
					}
					catch (SocketException e)
					{
						e.printStackTrace();
					}
					catch (UnknownHostException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		public void setRunning(boolean isRunning)
		{
			this.isRunning = isRunning;
		}
	}

	class ReceiveSoundsThread extends Thread
	{
		private AudioTrack player = null;
		private boolean isRunning = false;
		private byte[] recordBytes = new byte[670];

		public ReceiveSoundsThread()
		{
			// 播放器
			int playerBufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO,
					AudioFormat.ENCODING_PCM_16BIT);
			player = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO,
					AudioFormat.ENCODING_PCM_16BIT, playerBufferSize, AudioTrack.MODE_STREAM);
		}

		@Override
		public synchronized void run()
		{
			super.run();

			try
			{
				@SuppressWarnings("resource")
				DatagramSocket serverSocket = new DatagramSocket(AppConfig.Port);
				while (true)
				{
					if (isRunning)
					{
						DatagramPacket receivePacket = new DatagramPacket(recordBytes, recordBytes.length);
						serverSocket.receive(receivePacket);

						byte[] data = receivePacket.getData();

						byte[] head = new byte[30];
						byte[] body = new byte[640];

						// 获得包头
						for (int i = 0; i < head.length; i++)
						{
							head[i] = data[i];
						}

						// 获得包体
						for (int i = 0; i < body.length; i++)
						{
							body[i] = data[i + 30];
						}

						// 获得头信息 通过头信息判断是否是自己发出的语音
						String thisDevInfo = new String(head).trim();
						System.out.println(thisDevInfo);
						
						if (!thisDevInfo.equals(DevInfo))
						{
							player.write(body, 0, body.length);
							player.play();
						}
					}
				}
			}
			catch (SocketException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		public void setRunning(boolean isRunning)
		{
			this.isRunning = isRunning;
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
