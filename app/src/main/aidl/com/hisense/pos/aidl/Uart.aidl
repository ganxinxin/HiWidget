package com.hisense.pos.aidl;

interface Uart{

	//
	int openUart(int Slot);
	void closeUart(int Slot);
	int readUart(int Slot,out byte [] value,int len);
	int writeUart(int Slot,in byte [] value,int len);
	int flushUart(int Slot);
	int setBaudateRate(int port,int baudrate,boolean ctrlOPen);
}