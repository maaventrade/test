package com.example.testmoving;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Item {

	private float x;
	private float y;

	private int xInt;
	private int yInt;

	private int rect[][];

	private final int SIZE = 11;
	private int sizeX = SIZE;
	private int sizeY = SIZE;

	private float vy;
	private float vx;

	private Bitmap bitmap;

	public Item(Context context) {
		bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);
		
		vx = 0.5f - (float)Math.random();
		vy = 0.5f - (float)Math.random();
		
		//x = (float) Math.random() * 100;
		//y = (float) Math.random() * 100;
		/*while (){
			x = (float) Math.random() * 100;
			y = (float) Math.random() * 100;
		}
		*/
		
		int r = (int) (Math.random() * (Court.getRadius() - SIZE/2 - 1));
		int angle = (int) (Math.random() * 360);
		
		Log.d("", "r "+r);
		Log.d("", "an "+angle);
		
		x = (float)(r * Math.cos(angle) + Court.getRadius() - SIZE/2 - 1);
		y = (float)(r * Math.sin(angle) + Court.getRadius() - SIZE/2 - 1);
		
		xInt = (int) x;
		yInt = (int) y;
		 
		rect = new int[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
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
			
			//Log.d("", "sin "+sinNA);
			//Log.d("", "cos "+cosNA);
			
			
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

		int left = Court.getLeft();
		int top = Court.getTop();
		int k = Court.getK();
		
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				if (rect[i][j] == 1)
					canvas.drawPoint(i * k + left + x * k, j * k + top
							+ y * k, paint);

		// canvas.drawBitmap(bitmap, x, y, null);
	}
}
