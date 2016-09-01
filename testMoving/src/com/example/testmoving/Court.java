package com.example.testmoving;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Court {
	private int rect[][];
	private int left;
	private int top;
	private int k;

	public Court(int pleft, int ptop, int pk){
		left = pleft;
		top = ptop;
		k = pk;
		
		rect = new int[100][100];
		for (int i = 0; i< 100; i++)
			for (int j = 0; j < 100; j++)
				if ( Math.sqrt((i-50)*(i-50) + (j-50)*(j-50)) > 48)
					rect[i][j] = 1;
				else 
					rect[i][j] = 0;
	}
	
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.YELLOW);
		
		for (int i = 0; i < 100; i++)
			for (int j = 0; j < 100; j++)
				if (rect[i][j] == 1)
					canvas.drawPoint(i*k + left, j*k + top, paint);
	}
}
