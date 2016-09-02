package com.example.testmoving;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Item {
	private int left = 100;
	private int top = 100;
	private int k = 4;

	private float x = 50;
	private float y = 50;

	private int xInt = 50;
	private int yInt = 50;

	private int rect[][];

	private int sizeX = 10;
	private int sizeY = 10;

	private float vy = 0.2f;
	private float vx = -0.2f;

	private Bitmap bitmap;

	public Item(Context context) {
		bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);

		rect = new int[10][10];
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				rect[i][j] = 1;

	}

	// Log.d("", "xNew "+xNew+" xInt "+xInt+" dx "+dx);

	public void calc() {
		float xNew = x + vx;
		float yNew = y + vy;

		int dx = (int) (xNew - xInt);
		int dy = (int) (yNew - yInt);

		// Log.d("", "dx "+dx);
		// Log.d("", "dy "+dy);

		boolean on = false;

		for (int i = 0; i < sizeX; i++) {
			if (on)
				break;

			for (int j = 0; j < sizeY; j++) {
				// (x+dx, y+dy) is internal point - do nothing
				if (i + dx >= 0 && i + dx < sizeX && j + dy >= 0
						&& j + dy < sizeY && rect[i + dx][j + dy] == 1)
					;

				else {
					on = Court.isOn(xInt + i + dx, yInt + j + dy);
					if (on) 
						break;
				}
			}
		}

		if (!on){
			x = xNew;
			y = yNew;

			xInt = xInt + dx;
			yInt = yInt + dy;
			
		} else {
			float sinNA = Court.getSin();
			float cosNA = Court.getCos();
			
			float nSpeed = vx * cosNA - vy * sinNA; 
			float tSpeed = vx * sinNA + vy * cosNA; 
			
			nSpeed = -nSpeed;
			
			vx = tSpeed * sinNA + nSpeed * cosNA; 
			vy = tSpeed * cosNA - nSpeed * sinNA; 
			
			//vx = -vx;
			//vy = -vy;
		}

	}

	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);

		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				if (rect[i][j] == 1)
					canvas.drawPoint(i * k + left + xInt * k, j * k + top
							+ yInt * k, paint);

		// canvas.drawBitmap(bitmap, x, y, null);
	}
}
