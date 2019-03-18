package com.hisense.pos.api.para;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Project Name: HsAssistant
 * Author: Ge
 * Created by:
 */
public class ApduSend implements Parcelable {

    private byte[] command = new byte[0]; //4
    private short lc = 0;
    private byte[] dataIn = new byte[0]; //255
    private short le = 0;

    public ApduSend(Parcel in) {
        readFromParcel(in);
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 返回 命令头CLA INS P1 P2
     * @param: ${tags}
     * @return: ${return_type}
     */
    public byte[] getCommand() {
        return command;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 设置 命令头CLA INS P1 P2
     * @param: ${tags}
     * @return: ${return_type}
     */
    public void setCommand(byte[] cmd) {
        command = cmd;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 返回数据data
     * @param: ${tags}
     * @return: ${return_type}
     */
    public byte[] getDataIn() {
        return dataIn;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 设置数据data 长度范围0-255,无数据返送可设置null或长度为0的数组
     * @param: ${tags}
     * @return: ${return_type}
     */
    public void setDataIn(byte[] data) {
        if (data == null) {
            data = new byte[0];
        } else {
            dataIn = data;
        }
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 返回Le
     * @param: ${tags}
     * @return: ${return_type}
     */
    public short getLe() {
        return le;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 设置Le
     * @param: ${tags}
     * @return: ${return_type}
     */
    public void setLe(short aLe) {
        le = aLe;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 返回Lc
     * @param: ${tags}
     * @return: ${return_type}
     */
    public short getLc() {
        return lc;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 设置Lc
     * @param: ${tags}
     * @return: ${return_type}
     */
    public void setLc(short aLc) {
        lc = aLc;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(this.lc);
        dest.writeInt(this.le);

        dest.writeInt(this.command.length);
        dest.writeByteArray(this.command);

        dest.writeInt(this.dataIn.length);
        dest.writeByteArray(this.dataIn);
    }

    public void readFromParcel(Parcel in) {
        // TODO Auto-generated method stub
        this.lc = (short) in.readInt();
        this.le = (short) in.readInt();

        this.command = new byte[in.readInt()];
        in.readByteArray(this.command);

        this.dataIn = new byte[in.readInt()];
        in.readByteArray(this.dataIn);

    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 添加一个静态成员, 名为CREATOR, 该对象实现了Parcelable.Creator接口
     * @param: ${tags}
     * @return: ${return_type}
     */
    public static final Creator<ApduSend> CREATOR = new Creator<ApduSend>() {

        @Override
        public ApduSend createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new ApduSend(source);
        }

        @Override
        public ApduSend[] newArray(int size) {
            // TODO Auto-generated method stub
            return new ApduSend[size];
        }
    };
}
