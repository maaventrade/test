package com.example.testmoving;

import android.graphics.*;
import android.util.*;

public class Court {
	private static int rect[][];
	
	private static final int SIZE = 100;
	private static int sizeX = SIZE;
	private static int sizeY = SIZE;
	
	private static int left = 100;
	private static int top = 100;
	private static int k = 4;

	private static int radius = 50;
	
	private static int yCenter;
	private static int xCenter;
	
	private static int yCol = -1;
	private static int xCol = -1;

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
		
		yCenter = 50; 
		xCenter = 50; 
		
		for (int i = 0; i< 100; i++)
			for (int j = 0; j < 100; j++)
				if ( Math.sqrt((i-50)*(i-50) + (j-50)*(j-50)) > 48)
					rect[i][j] = 1;
				else 
					rect[i][j] = 0;
	}
	
	public static void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.YELLOW);
		
		for (int i = 0; i < 100; i++)
			for (int j = 0; j < 100; j++)
				if (rect[i][j] == 1)
					canvas.drawPoint(i*k + left, j*k + top, paint);
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
		//Log.d("", "ycol "+yCol);
		return (radius - yCol)/(float)radius;
	}
	
	public static float getCos() {
		return (xCol - radius)/(float)radius;
	}
}
