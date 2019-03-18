package com.hisense.pos.api.para;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Project Name: HsAssistant
 * Author: sunsitao
 * Created by:
 * <p>
 * Describe:保存读取磁卡的结果，通过MagCard的public int Mag_Read(TrackResult result)方法
 * 会将读取磁卡三个磁道的结果保存在此类实例中。如果要获取某轨道数据，应先通过本类提供的方法检测此轨道
 * 是否有效，如果数据有效，再通过本类提供的方法，获取对应轨道的数据
 */

public class TrackResult implements Parcelable {

    private byte[] trackData1 = new byte[0];
    private byte[] trackData2 = new byte[0];
    private byte[] trackData3 = new byte[0];

    public TrackResult() {
        // TODO Auto-generated constructor stub
    }

    public TrackResult(Parcel in) {
        readFromParcel(in);
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 保存某轨道数据
     * @param: data 某轨道数据
     * @param: TrackNo 轨道号 ，只能取1、2、3
     * @return: ${return_type}
     */
    public void setTrackData(int TrackNo, byte[] data) {
        switch (TrackNo) {
            case 1:
                trackData1 = data;
                break;
            case 2:
                trackData2 = data;
                break;
            case 3:
                trackData3 = data;
                break;
            default:
                break;
        }
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 获取某轨道数据，通常应先通过 boolean isTrackValid(int TrackNo)
     * 检测此轨道数据是个否有效，如果有效，再通过此方法获取轨道数据
     * @param: TrackNo 轨道号 ，只能取1、2、3
     * @return: 返回轨道数据
     */
    public byte[] getTrackData(int TrackNo) {
        switch (TrackNo) {
            case 1:
                return trackData1;
            case 2:
                return trackData2;
            case 3:
                return trackData3;
            default:
                return new byte[0];
        }
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub

        dest.writeInt(this.trackData1.length);
        dest.writeByteArray(this.trackData1);

        dest.writeInt(this.trackData2.length);
        dest.writeByteArray(this.trackData2);

        dest.writeInt(this.trackData3.length);
        dest.writeByteArray(this.trackData3);

        /**
         * A shorter solution without storing the byte arrays length:
         dest.writeByteArray(byteArray);
         byteArray = in.createByteArray();
         */
    }

    public void readFromParcel(Parcel in) {
        // TODO Auto-generated method stub

        this.trackData1 = new byte[in.readInt()];
        in.readByteArray(this.trackData1);

        this.trackData2 = new byte[in.readInt()];
        in.readByteArray(this.trackData2);

        this.trackData3 = new byte[in.readInt()];
        in.readByteArray(this.trackData3);

    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 添加一个静态成员, 名为CREATOR, 该对象实现了Parcelable.Creator接口
     * @param: ${tags}
     * @return: ${return_type}
     */
    public static final Creator<TrackResult> CREATOR = new Creator<TrackResult>() {
        @Override
        public TrackResult createFromParcel(Parcel source) {
            // TODO Auto-generated method stub

            return new TrackResult(source);
        }

        @Override
        public TrackResult[] newArray(int size) {
            // TODO Auto-generated method stub
            return new TrackResult[size];
        }
    };

}