package com.hisense.pos.aidl;
import com.hisense.pos.api.para.ApduResp;
import com.hisense.pos.api.para.ApduSend;
import com.hisense.pos.api.para.BytesArray;

interface PiccCard{
	
	//picc
	int piccGetErrCode();
	int  piccOpen();
	void piccClose();
	int  piccDetect(byte mode,
           out byte []cardType,
           out byte []serialInfo, 
           out byte []cid, 
           out byte []other);
    int piccRemove(byte mode,  byte cid);
    int piccIsoCommand(byte cid, in ApduSend apduSend, inout ApduResp apduRecv);
    
    //M card
    int piccMfAuthBlock(byte type, byte blockaddr,in byte[] keydata, in byte[] serialInfo);
    int piccMfReadBlock(byte blockaddr, inout BytesArray blockdata);
    int piccMfWriteBlock(byte blockaddr, in byte[] data);
    int piccMfInitValue(byte blockaddr, int value);
    int piccMfAddValue(byte blockaddr,  int value);
    int piccMfReduceValue(byte blockaddr, int value);
    byte piccGetCardType();
}