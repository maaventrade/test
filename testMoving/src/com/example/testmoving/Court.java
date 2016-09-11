package com.example.testmoving;

import android.content.Context;
import android.graphics.*;
import android.util.*;

public class Court {
	private static int rect[][];
	
	private static final int SIZE = 200;
	private static int SIZEH = 100;
	
	private static int sizeX = SIZE;
	private static int sizeY = SIZE;
	
	private static int left = 0;
	private static int top = 0;
	private static int k = 2;
	
	private static int yCol = -1;
	private static int xCol = -1;
	
	private static float mGx = 0;
	private static float mGy = 0;

	public static void setAcceleration(double gx, double gy)
	{
		//mGx = (float) gx / 100;
		//mGy = (float) gy / 100;
	}

	public static int getLeft()
	{
		return left;
	}

	public static int getTop()
	{
		return top;
	}

	public static int getK()
	{
		return k;
	}
	
	public static int getRadius()
	{
		return SIZE / 2;
	}
	
	public static void init(int pleft, int ptop, int pk){
		left = pleft;
		top = ptop;
		k = pk;
		
		rect = new int[sizeX][sizeY];
		
		for (int i = 0; i< SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				if ( Math.sqrt((i-SIZEH)*(i-SIZEH) + (j-SIZEH)*(j-SIZEH)) > SIZEH-2)
					rect[i][j] = 1;
				else 
					rect[i][j] = 0;
	}
	
	public static void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.YELLOW);
		
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				if (rect[i][j] == 1)
					canvas.drawPoint(i*k + left, j*k + top, paint);
		
		canvas.drawLine(left, top, left + SIZE * k, top + SIZE * k, paint);
		canvas.drawLine(left, top + SIZE * k, left + SIZE * k, top, paint);
		
		paint.setColor(Color.RED);
		paint.setTextSize(25);
		canvas.drawText(""+mGx, left, top, paint);
		canvas.drawText(""+mGy, left, top+35, paint);
		
	}

	public static boolean isOn(int x, int y) {
		if  (x >= 0 && x < sizeX && y >= 0
				&& y < sizeY && rect[x][y] == 1){
			yCol = y;
			xCol = x;
			return true;
		} else {
			yCol = -1;
			xCol = -1;
			return false;
		}
	}

	public static float getSin() {
		return (SIZEH - yCol)/(float)SIZEH;
	}
	
	public static float getCos() {
		return (xCol - SIZEH)/(float)SIZEH;
	}

	public static float getAccelerationX() {
		return mGx;
	}

	public static float getAccelerationY() {
		// TODO Auto-generated method stub
		return mGy;
	}

	

}
