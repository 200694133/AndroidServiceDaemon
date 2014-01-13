package com.example.testservice.messenger;

import java.util.Random;

import com.example.testservice.Log;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * ʹ�����ַ�������service��activity��ֻ�ܴ������ݣ�����ֱ�ӵ���activity��callback��
 * �ڿ��̵߳�ʹ���У����ݵ�Meessage�Ķ�����ֱ�ӽ����ݷŵ�msg.data�У�����ʹ��Bundle��ʹ��msg.setdata()������
 * @author yananh
 */
public class MessengerService extends Service{
	private static final String TAG = MessengerService.class.getSimpleName();
	
	static HandlerThread WORK_THREAD = new HandlerThread("Messenger Service Thread");
	Handler mWorkHander = null;
	static{
		WORK_THREAD.start();
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		mWorkHander = new Handler(WORK_THREAD.getLooper()){
			@Override
			public void dispatchMessage(Message msg) {
				Log.i(TAG, TAG + " dispatchMessage");
				MessengerService.this.handleMessage(msg);
			}
		};
		Log.i(TAG, TAG + " onCreate");
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.i(TAG, TAG + " onDestroy");
	}
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, TAG + " onBind");
		return (new Messenger(mWorkHander)).getBinder();
	}
	

	public boolean handleMessage(Message msg) {
		Log.i(TAG, "handleMessage");
		try {
			callCallback(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	Random random = new Random();
	private  void callCallback(Message msg) throws RemoteException{
		Messenger megr = msg.replyTo;
		Message msg1 = Message.obtain();
		msg.arg1 = random.nextInt();
		megr.send(msg1);
	}
}
