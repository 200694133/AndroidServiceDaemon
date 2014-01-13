package com.example.testservice;

import com.example.testservice.aidl.AIDLService;
import com.example.testservice.aidl.IService;
import com.example.testservice.aidl.IServiceCallBack;
import com.example.testservice.messenger.MessengerService;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mMessengerConnection, Context.BIND_AUTO_CREATE);
        
        //bind to aidl service
		Intent intent1 = new Intent(MainActivity.this, AIDLService.class);
        bindService(intent1, mAIDLServiceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////
    private ServiceConnection mMessengerConnection = new ServiceConnection(){
		@Override
		public void onServiceConnected(ComponentName paramComponentName,
				IBinder paramIBinder) {
			Log.i(TAG, "mMessengerConnection.onServiceConnected");
			Messenger serviceMessenger = new Messenger(paramIBinder);
			Messenger clientMessenger = new Messenger(new Handler(Looper.getMainLooper()){
					@Override
		        	public void handleMessage(Message msg){
						Log.i(TAG, "mMessengerConnection.handleMessage");
						unbindService(mMessengerConnection);
					}
		        });
			Message msg = Message.obtain();//不能使用Message.obtain(handler), 必须将Messenger传递过去
			msg.replyTo = clientMessenger;
			Bundle data = new Bundle();
			data.putSerializable("data","mMessengerConnection");
			msg.setData(data);
			try {
				serviceMessenger.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void onServiceDisconnected(ComponentName paramComponentName) {
			Log.i(TAG, "mMessengerConnection.onServiceDisconnected");
		}
    };
//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////    
    private IServiceCallBack.Stub  mAIDLCallBack = new IServiceCallBack.Stub(){
		@Override
		public void onCallback(String info) throws RemoteException {
			Log.i(TAG, "onCallback " +info);
			unbindService(mAIDLServiceConnection);
		}
    };
    private ServiceConnection mAIDLServiceConnection = new ServiceConnection(){
    	IService sv = null;
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(TAG, "mAIDLServiceConnection.onServiceConnected");
			sv = IService.Stub.asInterface(service);
			try {
				sv.registerCallback(mAIDLCallBack);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i(TAG, "mAIDLServiceConnection.onServiceDisconnected");
			try {
				sv.unregisterCallback(mAIDLCallBack);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			sv = null;
		}
    };
}
