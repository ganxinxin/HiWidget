package com.hisense.pos.aidl;

import com.hisense.pos.aidl.Scanner;
import com.hisense.pos.aidl.CashBox;
import com.hisense.pos.aidl.MagCard;
import com.hisense.pos.aidl.PiccCard;
import com.hisense.pos.aidl.Printer;
import com.hisense.pos.aidl.Uart;
import com.hisense.pos.aidl.LD;
import com.hisense.pos.aidl.Sys;
import com.hisense.pos.aidl.VfdCd;

interface hk716Api{
	
	Scanner getScanner();
	CashBox getCashBox();
	MagCard getMagCard();
	PiccCard getPiccCard();
	Printer getPrinter();
	Uart getUart();
	LD getLd();
	Sys getSystem();
	VfdCd getVfdCD();	
	
}