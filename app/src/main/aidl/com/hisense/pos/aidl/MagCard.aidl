package com.hisense.pos.aidl;

import com.hisense.pos.api.para.TrackResult;
interface MagCard{
	
	//mag
	int getMagErrCode();
	int openMag();
	int closeMag();
	int resetMag();
	int swipedMag();
	int readTrack(out TrackResult result);
	boolean isTrackValid(int TrackNo);
	String getErrorString(int error);
}