package com.example.testmoving;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

public class Item {

	private float x;
	private float y;

	private int xInt;
	private int yInt;

	private int rect[][];

	private final int SIZE = 15;
	private final int SIZEH = 7;

	private float vy;
	private float vx;
	
	private int index;
	
	int N = 0;

	private Bitmap bitmap;
	
	Item items[];

	public Item(Context context, Item pItems[], int pindex) {
		bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);
		
		items = pItems;
		
		vx = (float)Math.random();
		vy = (float)Math.random();
		
		//while (true) {
			
			double m = Math.random(); //0.999f;
			double m1 = Math.random();
			//m = 0.99999f;
			//m1 = 0.8f;
			//m = 0;
			
			
			//Log.d("", "m "+m);
			//Log.d("", "m1 "+m1);
			
			int r = (int) (m * Court.getRadius() - Math.hypot(SIZE, SIZE));
			int angle = (int) (m1 * Math.toRadians(360));
			
			x = (float)(r * Math.cos(angle) + Court.getRadius());
			y = (float)(r * Math.sin(angle) + Court.getRadius());
			
			if (pindex == 0){
				x = 50;
				y = 50;
				vx = 0.1f;
				vy = 0;
			}else{
				x = 65;
				y = 50;
				vx = -0.1f;
				vy = 0;
			}
			
			
			
			//if (! intersectRects(x, y, items))
				//break;
			
		//};
		
		xInt = (int) x;
		yInt = (int) y;
		 
		rect = new int[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				rect[i][j] = 1;
		
		index = pindex;

	}

	public boolean intersectRects(float x, float y, Item items[]) {
		for (Item i: items) 
			if (i != null){
				RectF rectNew = new RectF(x , y , x + SIZE , y + SIZE);
				if (rectNew.intersect(i.getX(), i.getY(), i.getX() + SIZE, i.getY() + SIZE)) return true;
		}
			
		return false;
	}
	
	// Log.d("", "xNew "+xNew+" xInt "+xInt+" dx "+dx);

	public void calc() {
		float sinNA = 0;
		float cosNA = 0;
		N++;
		if (N >= 10) return;
		
		float xNew = x + vx;
		float yNew = y + vy;

		int dx = (int) (xNew - xInt);
		int dy = (int) (yNew - yInt);

		// Log.d("", "dx "+dx);
		// Log.d("", "dy "+dy);

		boolean on = false;
		for (int i = 0; i < SIZE; i++) {
			//if (on)
			//	break;

			for (int j = 0; j < SIZE; j++) {
				// (x+dx, y+dy) is internal point - do nothing
				if (i + dx >= 0 && i + dx < SIZE && j + dy >= 0
						&& j + dy < SIZE && rect[i + dx][j + dy] == 1)
					;

				else {
					on = Court.isOn(
							xInt + i + dx - SIZEH, 
							yInt + j + dy - SIZEH);
					if (on){
						sinNA = Court.getSin();
						cosNA = Court.getCos();
						

						float nSpeed = vx * cosNA - vy * sinNA; 
						float tSpeed = vx * sinNA + vy * cosNA; 

						nSpeed = -nSpeed;

						vx = tSpeed * sinNA + nSpeed * cosNA;
						vy = tSpeed * cosNA - nSpeed * sinNA;
						
					}
					Log.d(""," "+(xInt + i + dx - SIZEH)+" "+(yInt + j + dy - SIZEH));
					
					for (Item t: items){
						if ( t != this){
							Log.d("","t "+t.getX()+" "+t.getY());
							on = t.isOn(
								xInt + i + dx, 
								yInt + j + dy);
Log.d("","on "+on);
							if (on){
								vx = t.vx;
								vy = t.vy;
							}
						}
					}
					
					//if (on) 
					//	break;
				}
			}
		}

		if (!on){
			x = xNew;
			y = yNew;

			xInt = xInt + dx;
			yInt = yInt + dy;
			
		} else {
			
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
					canvas.drawPoint(
							i * k + left + (x - SIZEH) * k, 
							j * k + top + (y - SIZEH) * k, 
							paint);

		paint.setColor(Color.RED);
		paint.setTextSize(25);
		canvas.drawText(""+index, left+x*k, top+y*k, paint);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	
	public boolean isOn(int x, int y) {
		if  (x >= 0 && x < SIZE && y >= 0
			 && y < SIZE && rect[x][y] == 1){
			
			return true;
		} else {
			
			return false;
		}
	}

/*
	public static float getSin() {
		return (radius - yCol)/(float)radius;
	}

	public static float getCos() {
		return (xCol - radius)/(float)radius;
	}
	*/
}
