package com.example.testmoving;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.*;

public class MySurfaceView extends SurfaceView  implements SurfaceHolder.Callback {
	private Item[] mItems;
	
	Context mContext;
	
	DrawThread drawThread;
	
	//CalcThread calcThread;
	
	private Handler handler = new Handler();
	
	private void init() {
		/*mItems = new Item[]{ new Item(mContext), 
			new Item(mContext), 
			new Item(mContext), 
			new Item(mContext), 
			new Item(mContext), 
			new Item(mContext)};
		*/
		
		mItems = new Item[]{ new Item(mContext), 
			new Item(mContext)};
		Court.init(100, 100, 4);

		getHolder().addCallback(this);
	}
	
	public void start() {
		handler.postDelayed(updateTimeTask, 10);
	}

	private Runnable updateTimeTask = new Runnable() { 
		public void run() { 
			for (Item i : mItems)
				i.calc();
			handler.postDelayed(this, 5);
		} 
	};        
	
	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public MySurfaceView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public void draw(Canvas canvas) {
		
		if (canvas == null) return;
		
		canvas.drawColor(Color.BLACK);
		
		Court.draw(canvas);
		
		synchronized (mItems) {
			for (Item i : mItems)
				i.draw(canvas);
		}
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		drawThread = new DrawThread(getHolder(), this);
		drawThread.setRunnable(true);
		drawThread.start();
		
		//calcThread = new CalcThread(mItems);
		//calcThread.setRunnable(true);
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		handler.removeCallbacks(updateTimeTask);
		
		boolean retry = true;
		// finish the thread wirking
		drawThread.setRunnable(false);
		while (retry) {
			try {
				drawThread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again ang again
			}
		}
		drawThread = null;
		
		retry = true;
		// finish the thread wirking
		/*calcThread.setRunnable(false);
		while (retry) {
			try {
				calcThread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again ang again
			}
		}
		calcThread = null;
		*/
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

}
