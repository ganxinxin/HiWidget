package com.hisense.pos.aidl;

interface Scanner{

	//scanner
	boolean openScanner(int type);
	boolean closeScanner();
	int readScanner(out byte []value,int len);
	int startScan();
	int stopScan();
	
}