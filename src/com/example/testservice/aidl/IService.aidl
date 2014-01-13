package com.example.testservice.aidl;
import com.example.testservice.aidl.IServiceCallBack;
interface IService{
	void registerCallback(IServiceCallBack cb);
	void unregisterCallback(IServiceCallBack cb);
}