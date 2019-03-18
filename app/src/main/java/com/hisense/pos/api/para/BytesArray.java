package com.hisense.pos.api.para;

import android.os.Parcel;
import android.os.Parcelable;

import com.hisense.utils.BytesUtil;


/**
 * Project Name: HsAssistant
 * Author: Ge
 * Created by:
 */
public class BytesArray implements Parcelable {

    public BytesArray() {
        // TODO Auto-generated constructor stub
    }


    public BytesArray(Parcel in) {
        readFromParcel(in);
    }

    private byte[] data = new byte[0];

    /**
     * @Title: ${enclosing_method}
     * @Description: 设置数据
     * @param: ${tags}
     * @return:
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 获取数据
     * @param: ${tags}
     * @return:
     */
    public byte[] getData() {
        return this.data;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 转换为16进制字符串, 若data==null 或length=0,返回""
     * @param: ${tags}
     * @return:
     */
    public String toHexString() {
        if (this.data == null || data.length == 0) {
            return "";
        }
        return BytesUtil.bytes2HexString(this.data);
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 获取字节字数组
     * @param: offset 偏移量 偏移位置。0~最大长度
     * @param: len 长度，-1表示从offset开始的所有数据
     * @return: 返回子数据
     */
    public BytesArray subBytes(int offset, int len) {
        BytesArray buffer = new BytesArray();
        buffer.setData(BytesUtil.subBytes(this.data, offset, len));
        return buffer;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 判断是否包含指定的数据
     * @param: data 数据
     * @return: true表示已包含，false表示不包含
     */
    public boolean contains(byte[] data) {
        return indexOf(data) > -1;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 判读是否以指定的数据开始
     * @param: data 数据
     * @return: true表示以指定的数据开始，false则相反
     */
    public boolean startsWith(byte[] data) {
        return indexOf(data) == 0;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 是否以指定的数据结尾
     * @param: data
     * @return: true表示以指定的数据结尾，false则相反
     */
    public boolean endsWith(byte[] data) {
        return lastIndexOf(data) + data.length == this.data.length;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 查找最后一个子数据位置
     * @param: data
     * @return: 找不到返回-1，成功返回>=0的数字
     */
    public int lastIndexOf(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("param is null!");
        }

        if ((this.data == null) || (this.data.length < data.length)) {
            return -1;
        }

        byte last = data[(data.length - 1)];
        int secondLen = data.length - 1;
        for (int i = this.data.length - 1; i >= secondLen; i--) {
            if (this.data[i] == last) {
                int n = 0;
                for (; n < secondLen; n++) {
                    if (this.data[(i - data.length + n + 1)] != data[n]) {
                        break;
                    }
                }

                if (n == secondLen) {
                    return i - data.length + 1;
                }
            }
        }
        return -1;
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 查找子数据位置
     * @param: ${tags}
     * @return: 找不到返回-1，成功返回>=0的数字
     */
    public int indexOf(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("param is null!");
        }

        if ((this.data == null) || (this.data.length < data.length)) {
            return -1;
        }

        int limit = this.data.length - data.length + 1;
        for (int i = 0; i < limit; i++) {
            if (this.data[i] == data[0]) {
                int n = 1;
                for (n = 1; n < data.length; n++) {
                    if (this.data[(i + n)] != data[n]) {
                        break;
                    }
                }
                if (n == data.length) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void readFromParcel(Parcel in) {
        // TODO Auto-generated method stub

        this.data = new byte[in.readInt()];
        in.readByteArray(this.data);

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(this.data.length);
        dest.writeByteArray(this.data);
    }

    /**
     * @Title: ${enclosing_method}
     * @Description: 添加一个静态成员, 名为CREATOR, 该对象实现了Parcelable.Creator接口
     * @param: ${tags}
     * @return: ${return_type}
     */
    public static final Creator<BytesArray> CREATOR = new Creator<BytesArray>() {

        @Override
        public BytesArray createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new BytesArray(source);
        }

        @Override
        public BytesArray[] newArray(int size) {
            // TODO Auto-generated method stub
            return new BytesArray[size];
        }

    };
}

