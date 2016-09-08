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
	private float angle = 0f;

	private int xInt;
	private int yInt;

	private int rect0[][];
	private int rect[][];

	private final int SIZE = 15;
	private final int SIZEH = 7;

	private float vy;
	private float vx;
	private float vRot = 0f;

	private int index;

	int N = 0;

	private Bitmap bitmap;
	
	static class IAngle{
		static float sin;
		static float cos;
	}

	Item items[];

	public Item(Context context, Item pItems[], int pindex) {
		bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);

		items = pItems;

		// vx = (float)Math.random();
		// vy = (float)Math.random();

		while (true) {

			double m = Math.random(); // 0.999f;
			double m1 = Math.random();
			// m = 0.99999f;
			// m1 = 0.8f;
			// m = 0;

			// Log.d("", "m "+m);
			// Log.d("", "m1 "+m1);

			int r = (int) (m * Court.getRadius() - Math.hypot(SIZE, SIZE));
			int angle = (int) (m1 * Math.toRadians(360));

			x = (float) (r * Math.cos(angle) + Court.getRadius());
			y = (float) (r * Math.sin(angle) + Court.getRadius());
			/*
			 * if (pindex == 0){ x = 50; y = 50; //vx = 0.3f; //vy = 0; }else{ x
			 * = 70; y = 50; //vx = -0.3f; //vy = 0; }
			 */

			if (!intersectRects(x, y, items))
				break;

		}
		;

		xInt = (int) x;
		yInt = (int) y;

		rect0 = new int[SIZE][SIZE];
		rect = new int[SIZE][SIZE];

		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++) {
				rect0[i][j] = 1;
				rect[i][j] = 1;
			}

		index = pindex;

	}

	public boolean intersectRects(float x, float y, Item items[]) {
		for (Item i : items)
			if (i != null) {
				RectF rectNew = new RectF(x, y, x + SIZE, y + SIZE);
				if (rectNew.intersect(i.getX(), i.getY(), i.getX() + SIZE,
						i.getY() + SIZE))
					return true;
			}

		return false;
	}

	private void rotate() {
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++) {
				rect[i][j] = 0;
			}

		angle = angle + vRot;

		if (angle >= 360)
			angle = 0;

		double angleRad = Math.toRadians(angle);

		for (int i = -SIZEH; i < SIZEH; i++)
			for (int j = -SIZEH; j < SIZEH; j++) {

				if (rect0[i + SIZEH][j + SIZEH] == 1) {
					double hip = Math.hypot(i, j);

					int i2 = (int) (i * Math.cos(angleRad) - j
							* Math.sin(angleRad))
							+ SIZEH;
					int j2 = SIZEH
							- (int) (i * Math.sin(angleRad) + j
									* Math.cos(angleRad));
					if (i2 >= 0 && i2 < SIZE && j2 >= 0 && j2 < SIZE)
						rect[i2][j2] = 1;
				}

			}

		if (vRot > 0)
			vRot = Math.max(vRot - 0.01f, 0);
		else if (vRot < 0)
			vRot = Math.min(vRot + 0.01f, 0);
	}

	private void copyRect(int from[][], int to[][]) {
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++) {
				from[i][j] = to[i][j];
			}
	}

	private boolean testIntersection(int r[][], int dx, int dy) {
		boolean on = false;
		IAngle.sin = 0;
		IAngle.cos = 0;
		
		for (int i = 0; i < SIZE; i++) {
			if (on)
				return true;

			for (int j = 0; j < SIZE; j++) {
				// (x+dx, y+dy) is internal point - do nothing
				if (i + dx >= 0 && i + dx < SIZE && j + dy >= 0
						&& j + dy < SIZE && r[i + dx][j + dy] == 1)
					;

				else {
					if (r[i][j] == 1) {
						on = Court.isOn(xInt + i + dx - SIZEH, yInt + j + dy
								- SIZEH);
						if (on) {
							IAngle.sin = Court.getSin();
							IAngle.cos = Court.getCos();

							return true;
						}
						for (Item t : items) {
							if (t != null && t != this) {
								on = t.isOn(xInt + i + dx, yInt + j + dy);
								if (on) {
									IAngle.sin = t.getSin(xInt + i + dx, yInt + j
											+ dy);
									IAngle.cos = t.getCos(xInt + i + dx, yInt + j
											+ dy);
									return true;
								}
							}
						}
					}
					if (on)
						return true;
				}
			}
		}
		return false;
	}

	public void calc() {
		float angleSaved = angle;
		int rectSaved[][] = new int[SIZE][SIZE];
		copyRect(rect, rectSaved);

		rotate();

		boolean intersection = testIntersection(rect, 0, 0);
		
		if (intersection){
			copyRect(rectSaved, rect);
			angle = angleSaved;
			vRot = 0;
		}

		float xNew = x + vx; //
		float yNew = y + vy; //

		int dx = Math.round(xNew - xInt);
		int dy = Math.round(yNew - yInt);
		
		intersection = testIntersection(rect, dx, dy);
		
		if (!intersection) {
			x = xNew;
			y = yNew;

			xInt = xInt + dx;
			yInt = yInt + dy;

			vx = (float) (vx + Court.getAccelerationX());
			vy = (float) (vy + Court.getAccelerationY());

		} else {

			float nSpeed = vx * IAngle.cos - vy * IAngle.sin;
			float tSpeed = vx * IAngle.sin + vy * IAngle.cos;

			nSpeed = -nSpeed;

			vx = (tSpeed * IAngle.sin + nSpeed * IAngle.cos) / 2;
			vy = (tSpeed * IAngle.cos - nSpeed * IAngle.sin) / 2;

			vRot = vRot + 0.5f * nSpeed;

		}

		if (Math.abs(vx) < 0.01)
			vx = 0;
		if (Math.abs(vy) < 0.01)
			vy = 0;

	}

	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);

		int left = Court.getLeft();
		int top = Court.getTop();
		int k = Court.getK();
		/*
		 * paint.setColor(Color.YELLOW); canvas.drawRect(0 * k + left + (x -
		 * SIZEH) * k, 0 * k + top + (y - SIZEH) * k, SIZE * k + left + (x -
		 * SIZEH) * k, SIZE * k + top + (y - SIZEH) * k, paint);
		 */
		paint.setColor(Color.WHITE);
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				if (rect[i][j] == 1)
					canvas.drawPoint(i * k + left + (x - SIZEH) * k, j * k
							+ top + (y - SIZEH) * k, paint);

		// paint.setColor(Color.RED);
		// paint.setTextSize(25);
		// canvas.drawText(""+angle, left+x*k, top+y*k, paint);

		paint.setColor(Color.RED);
		paint.setTextSize(25);
		// canvas.drawText(""+index, left+x*k, top+y*k, paint);
		canvas.drawText("" + vx, left + x * k, top + y * k + 20, paint);
		// canvas.drawText(""+vy, left+x*k, top+y*k+40, paint);

	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public boolean isOn(int px, int py) {
		if (px - xInt >= 0 && px - xInt < SIZE && py - yInt >= 0
				&& py - yInt < SIZE && rect[px - xInt][py - yInt] == 1) {

			return true;
		} else {

			return false;
		}
	}

	public float getSin(int px, int py) {
		double hip = Math.hypot(px - (xInt + SIZEH), py - (yInt + SIZEH));

		return (float) ((Math.abs(yInt + SIZEH - py)) / (float) hip);
	}

	public float getCos(int px, int py) {
		double hip = Math.hypot(px - (xInt + SIZEH), py - (yInt + SIZEH));

		return (float) ((Math.abs(xInt + SIZEH - px)) / (float) hip);
	}

}
