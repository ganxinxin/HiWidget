package com.hisense.pos.aidl;

interface Sys{
	int sysBeep(int level,int beepTimes);
	String sysReadSN();
	String sysGetVersion();
}