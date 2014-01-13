package com.example.testservice.aidl;

import com.example.testservice.Log;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

public class AIDLService extends Service{
	 private static final String TAG = AIDLService.class.getSimpleName();
	 private RemoteCallbackList<IServiceCallBack> mCallbacks = new RemoteCallbackList<IServiceCallBack>();
	 Handler mHandler = new Handler();
	 private IService.Stub mBinder = new IService.Stub() {
		@Override
		public void unregisterCallback(IServiceCallBack cb) throws RemoteException {
			if(cb!=null){
				mCallbacks.unregister(cb);
			}
		}
		@Override
		public void registerCallback(IServiceCallBack cb) throws RemoteException {
			if(cb!=null){
				mCallbacks.register(cb);
				callBack();
				//mHandler.postDelayed(r, delayMillis);
			}
		}
	};
	 
	public IBinder onBind(Intent intent){
		Log.i(TAG, "onBind");
		return mBinder;
	}
	 
	 @Override
	 public void onCreate() {
		 Log.i(TAG, " onCreate");
		 super.onCreate();
	 }
	 
	 @Override
	 public void onDestroy() {
		 Log.i(TAG, " onDestroy");
		 super.onCreate();
	 }
	 
	 private void callBack(){
		 int N = mCallbacks.beginBroadcast();
		 try{
			 for(int i = 0;i<N;++i){
				 mCallbacks.getBroadcastItem(i).onCallback(TAG+" "+i);
			 }
		 }catch(Exception e){
			 Log.i(TAG, " callBack "+e);
		 }
		 mCallbacks.finishBroadcast();
	 }
	 
	 
	 
}
