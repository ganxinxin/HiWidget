package com.hisense.pos.aidl;

interface VfdCd{

   int vfdOpen(int baudrate);
   int sendComm(in byte[] array, int len);
   int vfdWrite(in byte []data);  
   void vfdClose();
   int vfdInitializeDisplay();
   int vfdClear();
   int vfdClearLine();
   int vfdMoveposition(int nX, int nY);
   int vfdOverwriteMode();
   int vfdVerticalScrollMode();
   int vfdHorizontalScrollMode();
   int vfdInternationalCodeSet(int nCodeSet);
   int vfdCharacterFontTable(int nFontTable);
   int vfdDisplayPeriod(in byte[] bytePeriod);
   int vfdDisplayComma( in byte[] byteComma);
   int vfdDisplayPeriodNComma( in byte[] bytecommaSemicolon);
   int vfdTurnReversedCharMode(boolean bReverseChar);
   int vfdCursor(boolean bOn);
   int vfdCursorLeft();
   int vfdCursorRight();
   int vfdCursorDown();
   int vfdCursorUp();
   int vfdCursorHome();
   int vfdCursorLeftMost();
   int vfdCursorRightMost();
   int vfdCursorBottom();
   int vfdBrightness(int nBrightness);
   int vfdBlinkInterval(int nBlink);
   int vfdAnnunciator(boolean bOn, int nColumn);
   int vfdSetTime(int nHour, int nMinute);
   int vfdisplayConti();
   
} 