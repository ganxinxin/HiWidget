package com.hisense.pos.aidl;

interface Printer{

	//printer
	int initPrinter(int type);
	int printTextStr(String text,int fontSize,int pattern);
	int printImage(in Bitmap bitmap, int mode);
	int feedPaper(int value,int unit);
	int printerConfig(int baudrate,boolean ctrlOPen);
	int printBar(String barcode);
	int printQR(String barcode,int mode);
	void closePrinter();
	int printOpenCashBox();
	void cutPaper();
	int printSend(in byte[] data,int len);
	int printStatus();
	String getPrnType(int baudrate,int timeout);
}