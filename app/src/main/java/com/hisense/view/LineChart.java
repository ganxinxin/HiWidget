package com.hisense.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class LineChart extends View {

	//布局宽高
	int height;
	int width;

	//坐标轴原点的位置
	private int xPoint;
	private int yPoint;

	//刻度长度
	private int xScale;
	private int yScale;

	//x与y坐标轴的长度
	private int xLength;
	private int yLength;

	//横坐标  最多可绘制的点
	private int MaxDataSize;
	//存放 纵坐标 所描绘的点
	private List<Integer> data = new ArrayList<Integer>();
	//Y轴的刻度上显示字的集合
	private String[] yLabel = new String[5];
	//Y轴的刻度上显示字的集合
	private String xlable = "60秒";

	private Paint paint = new Paint();
	private Paint paint2 = new Paint();

	public LineChart(Context context, AttributeSet arrs) {
		super(context, arrs);

		yLabel[0] = "0";
		yLabel[1] = "";
		yLabel[2] = "50%";
		yLabel[3] = "";
		yLabel[4] = "100%";
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) { //在线程中不断往集合中增加数据
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					data.add((int) (getCPURateDesc() * 0.8)); //生成1-6的随机数
					if (data.size() > MaxDataSize) { //判断集合的长度是否大于最大绘制长度
						data.remove(0); //删除头数据
					}
				}
			}
		}).start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		width = 990;
		height = 432;
		xPoint = sp2px(60);
		yPoint = height - sp2px(40);
		xScale = (width - sp2px(100)) / 10; //8个单位构成一个刻度
		yScale = (height - sp2px(100)) / 4;
		xLength = width - sp2px(100);
		yLength = height - sp2px(100);
		MaxDataSize = xLength / xScale + 1;

		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
		paint.setColor(0xFF818492);
		paint.setTextSize(sp2px(16));
		//绘制Y轴
		canvas.drawLine(xPoint, yPoint - yLength, xPoint, yPoint, paint);
		//Y轴上的刻度与文字
		for (int i = 0; i * yScale <= yLength; i++) {
			canvas.drawLine(xPoint, yPoint - i * yScale, xPoint + xLength, yPoint - i * yScale, paint); //刻度
			if (i == 0) {
				canvas.drawText(yLabel[i], xPoint - sp2px(15), yPoint - i * yScale, paint);//文字
			} else if (i == 2) {
				canvas.drawText(yLabel[i], xPoint - sp2px(35), yPoint - i * yScale, paint);//文字
			} else {
				canvas.drawText(yLabel[i], xPoint - sp2px(45), yPoint - i * yScale, paint);//文字
			}

		}

		//X轴
		canvas.drawLine(xPoint, yPoint, xPoint + xLength, yPoint, paint);
		canvas.drawText(xlable, xPoint + xLength - sp2px(30), yPoint + sp2px(18), paint);//文字
		paint.setStrokeWidth(2);
		paint.setColor(0xFF288fe2);

		paint2.setColor(0xFF205373);
		paint2.setStyle(Paint.Style.FILL);
		if (data.size() > 1) {
			Path path = new Path();
			Path path2 = new Path();
			path.moveTo(xPoint, yPoint - data.get(0) * 2);
			path2.moveTo(xPoint, yPoint);
			for (int i = 0; i < data.size(); i++) {
				path.lineTo(xPoint + i * xScale, yPoint - data.get(i) * 2);
				path2.lineTo(xPoint + i * xScale, yPoint - data.get(i) * 2);
			}
			path2.lineTo(xPoint + (data.size() - 1) * xScale, yPoint);
			canvas.drawPath(path2, paint2);
			canvas.drawPath(path, paint);
		}
		postInvalidateDelayed(1000);
	}

	private int sp2px(int value) {
		float v = getContext().getResources().getDisplayMetrics().scaledDensity;
		return (int) (v * value + 0.5f);
	}

	public static int getCPURateDesc() {
		String path = "/proc/stat";// 系统CPU信息文件
		long totalJiffies[] = new long[2];
		long totalIdle[] = new long[2];
		int firstCPUNum = 0;//设置这个参数，这要是防止两次读取文件获知的CPU数量不同，导致不能计算。这里统一以第一次的CPU数量为基准
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		Pattern pattern = Pattern.compile(" [0-9]+");
		for (int i = 0; i < 2; i++) {
			totalJiffies[i] = 0;
			totalIdle[i] = 0;
			try {
				fileReader = new FileReader(path);
				bufferedReader = new BufferedReader(fileReader, 8192);
				int currentCPUNum = 0;
				String str;
				while ((str = bufferedReader.readLine()) != null && (i == 0 || currentCPUNum < firstCPUNum)) {
					if (str.toLowerCase().startsWith("cpu")) {
						currentCPUNum++;
						int index = 0;
						Matcher matcher = pattern.matcher(str);
						while (matcher.find()) {
							try {
								long tempJiffies = Long.parseLong(matcher.group(0).trim());
								totalJiffies[i] += tempJiffies;
								if (index == 3) {//空闲时间为该行第4条栏目
									totalIdle[i] += tempJiffies;
								}
								index++;
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
						}
					}
					if (i == 0) {
						firstCPUNum = currentCPUNum;
						try {//暂停50毫秒，等待系统更新信息。
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		double rate = -1;
		if (totalJiffies[0] > 0 && totalJiffies[1] > 0 && totalJiffies[0] != totalJiffies[1]) {
			rate = 1.0 * ((totalJiffies[1] - totalIdle[1]) - (totalJiffies[0] - totalIdle[0]))
					/ (totalJiffies[1] - totalJiffies[0]);
		}
		return (int) (rate * 100);
	}
}