package com.example.testservice;

public class Log {
	public static void i(String tag, String info){
		android.util.Log.i(tag, "threadId: "+Thread.currentThread().getId()+"\t"+info);
	}
	public static void d(String tag, String info){
		android.util.Log.d(tag, "threadId: "+Thread.currentThread().getId()+"\t"+info);
	}
	public static void v(String tag, String info){
		android.util.Log.v(tag, "threadId: "+Thread.currentThread().getId()+"\t"+info);
	}
	public static void w(String tag, String info){
		android.util.Log.w(tag, "threadId: "+Thread.currentThread().getId()+"\t"+info);
	}
	public static void e(String tag, String info){
		android.util.Log.e(tag, "threadId: "+Thread.currentThread().getId()+"\t"+info);
	}
}
