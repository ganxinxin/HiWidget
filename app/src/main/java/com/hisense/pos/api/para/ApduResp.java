package com.hisense.pos.api.para;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Project Name: HsAssistant
 * Author: Ge
 * Created by:
 */
public class ApduResp implements Parcelable {
    //private short  lenOut = 0;
    private byte[] dataOut = new byte[0]; //256
    private byte SWA = 0;
    private byte SWB;

    public ApduResp(Parcel in) {
        readFromParcel(in);
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 返回数据
     * @param: ${tags}
     * @return: ${return_type}
     */
    public byte[] getDataOut() {
        return dataOut;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 设置数据  ,长度为0数据或null表示无数据
     * @param: ${tags}
     * @return: ${return_type}
     */
    public void setDataOut(byte[] data) {
        if (data == null) {
            dataOut = new byte[0];
        } else {
            dataOut = data;
        }
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 获取SWA
     * @param: ${tags}
     * @return: ${return_type}
     */
    public byte getSWA() {
        return SWA;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 设置SWA
     * @param: ${tags}
     * @return: ${return_type}
     */
    public void setSWA(byte aSWA) {
        SWA = aSWA;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 获取SWB
     * @param: ${tags}
     * @return: ${return_type}
     */
    public byte getSWB() {
        return SWB;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 设置SWB
     * @param: ${tags}
     * @return: ${return_type}
     */
    public void setSWB(byte aSWB) {
        SWB = aSWB;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 把javanbean中的数据写到Parcel
     * @param: ${tags}
     * @return: ${return_type}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeByte(this.SWA);
        dest.writeByte(this.SWB);

        dest.writeInt(this.dataOut.length);
        dest.writeByteArray(this.dataOut);

    }

    public void readFromParcel(Parcel in) {
        // TODO Auto-generated method stub
        this.SWA = in.readByte();
        this.SWB = in.readByte();

        this.dataOut = new byte[in.readInt()];
        in.readByteArray(this.dataOut);

    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 添加一个静态成员, 名为CREATOR, 该对象实现了Parcelable.Creator接口
     * @param: ${tags}
     * @return: ${return_type}
     */
    public static final Creator<ApduResp> CREATOR = new Creator<ApduResp>() {
        @Override
        public ApduResp createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            //从Parcel中读取数据，返回person对象
            return new ApduResp(source);
        }

        @Override
        public ApduResp[] newArray(int size) {
            // TODO Auto-generated method stub
            return new ApduResp[size];
        }
    };

}
