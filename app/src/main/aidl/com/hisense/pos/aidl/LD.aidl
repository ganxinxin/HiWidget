package com.hisense.pos.aidl;

interface LD{

	int initLD(int baudrate);
	int cls();
	int clsLine();
	int selScreeMode(int mode );
	int displayText(int x,int y, in byte[]text);
	void closeLD();
	int sendComm(in byte[] array, int len);
}