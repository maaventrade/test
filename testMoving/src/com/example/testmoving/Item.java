package com.example.testmoving;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Item {
	private int left = 100;
	private int top = 100;
	private int k = 4;
	
	private int x = 50;
	private float y = 50;
	
	private int rect[][];
	
	
	private float v = 0.2f;

	private Bitmap bitmap;

	public Item(Context context){
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		
		rect = new int[10][10];
		for (int i = 0; i< 10; i++)
			for (int j = 0; j < 10; j++)
				rect[i][j] = 1;
		
	}
	
	public void calc() {
		//x++;
		y = y + v;
	}

	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				if (rect[i][j] == 1)
					canvas.drawPoint(i*k + left + x * k, j*k + top + y * k, paint);
		
		//canvas.drawBitmap(bitmap, x, y, null);
	}
}
