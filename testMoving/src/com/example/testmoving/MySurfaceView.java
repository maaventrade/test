package com.example.testmoving;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.*;

public class MySurfaceView extends SurfaceView  implements SurfaceHolder.Callback {
	Context mContext;
	
	//DrawThread drawThread;
	//CalcThread calcThread;
	
	private Handler handlerDraw = new Handler();
	private Handler handlerCalcSprites = new Handler();
	
	
	//private int height;
	//private int width;
	

	// Mask for a cutting triangles from the original images
	private Bitmap mask = null;
	// Size of the Mask
	public int maskWidth = 221;
	public int maskHeight = 190; //maskWidth/1.157894737f;  //190f;
	// Triangles can be scaled
	private float scale = 1;
	
    private Bitmap chip = null;
    private Rect rectChip = null;
	private Canvas canvasChip;
	
	private Paint paint;	
	// Bitmap with Sprites
	private Bitmap glass;
	private Canvas canvasGlass;
	
	private float deltaX = 0;
	private float deltaY = 0;
	
    private Bitmap background = null;
    private Canvas canvasBackground;
	private Bitmap original = null;
	
	
	void init() {
		Scene.init(mContext);
		getHolder().addCallback(this);
	}
	
	public void start() {
		handlerCalcSprites.postDelayed(calcSpritesTask, 10);
		handlerDraw.postDelayed(drawTask, 10);
	}

	private Runnable drawTask = new Runnable() {
		public void run() {

			Canvas canvas = null;
			try {
				canvas = getHolder().lockCanvas(null);
				synchronized (getHolder()) {
					draw(canvas);
				}

			} finally {
				if (canvas != null) {
					getHolder().unlockCanvasAndPost(canvas);
				}
			}

			handlerDraw.postDelayed(this, 20);
		}
	};

	private Runnable calcSpritesTask = new Runnable() {
		public void run() { 
			Scene.calc();
			handlerCalcSprites.postDelayed(this, 5);
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

	
    /**
     * 
     * @param original - bitmap with the original image
     * @param mask - the mask for cropping the original image
     * This method crops a part from the center of the original image. We suggest what the mask is smaller when the original image. 
     * 
     */
	public void bitmapsToChips() { //synchronized
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    
        if (glass != null){
        	background = Bitmap.createBitmap((int)maskWidth, (int)maskHeight, Config.ARGB_8888 );
			canvasBackground = new Canvas(background);
			
			if (original != null){
				canvasBackground.drawBitmap(original, 0, 0, null);
			}
			
			paint.setColor(Color.DKGRAY);
			paint.setAlpha(220);
			
			canvasBackground.drawRect(0,0,maskWidth, maskHeight, paint);
			paint.setAlpha(255);

        	int n = (int) (maskHeight/1.5f);
        	canvasBackground.drawBitmap(glass, 
					   new Rect(0,
								  0, 
								  glass.getWidth(), 
								  glass.getHeight()),											  
					   new Rect(maskWidth/2-n,
					  maskHeight/2-n, 
					  maskWidth/2+n, 
					  maskHeight/2+n),
					  paint);
											  
		} else if (original == null) return;
			else background = original;
			
        
        canvasChip.drawARGB(0, 0, 0, 0);
        
        rectChip = new Rect(0, 0, chip.getWidth(), chip.getHeight());
        
        final Paint paint = new Paint();
        
        canvasChip.drawBitmap(mask, rectChip, rectChip, null);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvasChip.drawBitmap(background, rectChip, rectChip, paint);

        
		//0 1 2
		//3 4 5
        
	}

	
	public void draw(Canvas canvas) {
		
		if (canvas == null) return;
		if (canvasGlass == null) return;
		
		//canvas.drawColor(Color.BLUE);
		
		Scene.draw(canvasGlass);
		
    	bitmapsToChips();
		
    	if (chip == null){
			return;
		};
		//if(background != null)
		//	canvas.drawBitmap(background, 10, 10,null);
		
    	//int pixel = chip.getPixel(maskWidth/2, maskHeight/2);
        //canvas.drawColor(pixel);
        
		int dx = 0;
		for (int y = (int)(deltaY); y <= this.getHeight(); y = y + maskHeight){
			for (int x = (int)(deltaX) + dx; x <= this.getWidth(); x = (int)(x + maskWidth*3f)-1){
				canvas.save(Canvas.MATRIX_SAVE_FLAG);
		
				canvas.drawBitmap(chip, rectChip, new Rect(x, y, x+maskWidth, y+maskHeight), paint);

				canvas.rotate(-60, x+maskWidth, y+maskHeight);
				canvas.scale(-1f, 1f, x+maskWidth, y+maskHeight);
				
				canvas.drawBitmap(chip, rectChip, new Rect(x, y, x+maskWidth, y+maskHeight), paint);
				
				canvas.scale(-1f, 1f, x+maskWidth, y+maskHeight);
				canvas.rotate(-180, x+maskWidth, y+maskHeight);
				
				canvas.drawBitmap(chip, rectChip, new Rect(x, y, x+maskWidth, y+maskHeight), paint);
				
				canvas.scale(-1f, 1f, x+maskWidth, y+maskHeight);
				canvas.rotate(-60, x+maskWidth, y+maskHeight);
				canvas.drawBitmap(chip, rectChip, new Rect(x, y, x+maskWidth, y+maskHeight), paint);
				
				canvas.scale(-1f, 1f, x+maskWidth, y+maskHeight);
				canvas.rotate(60, x+maskWidth, y+maskHeight);
				canvas.drawBitmap(chip, rectChip, new Rect(x, y, x+maskWidth, y+maskHeight), paint);
				
				canvas.scale(1f, -1f, x+maskWidth, y+maskHeight);
				canvas.drawBitmap(chip, rectChip, new Rect(x, y, x+maskWidth, y+maskHeight), paint);
						
				canvas.restore(); 
			}
			if (dx == 0)
				dx = (int)(dx + maskWidth*1.5f);
			else	
				dx = 0;
		}
		
		/*
		if (showShadow && bitmapShadow != null)
			canvas.drawBitmap(bitmapShadow,0, 0, null);
		
		if (! hideButton){
			if (ShowIconPhoto)
				canvas.drawBitmap(bitmapCamera, rectCameraSrc, rectCameraDst, paintButton);
		}
		*/
		
		
	}

	private static final float CTG_60 = 0.57735026919f;
	private static final float TG_60 = 1.73205080757f;
	
	/*
	 * Fill "mask" with the white triangle on the transparent background  
	 */
	private void fillMask(){
		mask = Bitmap.createBitmap((int)(maskWidth*scale), (int)(maskHeight*scale), Config.ARGB_8888);
    	//Clear the canvas
    	Canvas canvas = new Canvas(mask);

    	Paint paint = new Paint();
    	
    	paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
    	canvas.drawRect(0, 0, maskWidth*scale, maskHeight*scale, paint);    	
    	paint.setXfermode(null);
		
    	// Add white triangle.
    	for (float x = 0 ; x < maskWidth*scale ; x++)
        	for (float y = 0; y < maskHeight*scale; y++)
        		// If the point locates in the triangle area
        		if (y <= x/CTG_60 && x <= maskWidth*scale/2  
        		|| y < maskWidth*scale*TG_60 - (x)/CTG_60 && x > maskWidth*scale/2
        		// "+ 2" for overlapping triangles  
        				) 
        			canvas.drawPoint(x, maskHeight*scale-y, paint);
    	
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		fillMask();
		
        chip = Bitmap.createBitmap((int)maskWidth, (int)maskHeight,  Config.ARGB_8888);
        canvasChip = new Canvas(chip);

        glass = Bitmap.createBitmap(400, 400,  Config.ARGB_8888);
        canvasGlass = new Canvas(glass);
        
        
		deltaX = -maskWidth/2;
		deltaY = -maskHeight;
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		handlerCalcSprites.removeCallbacks(calcSpritesTask);
		handlerDraw.removeCallbacks(drawTask);
		
		/*
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
		*/
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void setAcceleration(double gx, double gy) {
		Scene.setAcceleration(gx, gy);
		//for (Item i : mItems)
		//	if (i != null)
		//		i.setVelocity(gx / 10, gy / 10);
	}

}
