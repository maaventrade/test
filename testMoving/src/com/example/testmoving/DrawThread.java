package com.example.testmoving;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DrawThread  extends Thread {

private SurfaceHolder mSurfaceHolder;
private MySurfaceView mySurfaceView;

private Canvas canvas;
private boolean mRun = false;

public DrawThread(SurfaceHolder holder,
		MySurfaceView surfaceView) {
	mSurfaceHolder = holder;
	mySurfaceView = surfaceView;
}

public void setRunnable(boolean run) {
    mRun = run;
}

public void run() {

    while(mRun) {

        canvas = null;

        try {

            canvas = mSurfaceHolder.lockCanvas(null);

            synchronized(mSurfaceHolder) {

            	mySurfaceView.draw(canvas);
            }

        } finally {

            if(canvas != null) {

            	mSurfaceHolder.unlockCanvasAndPost(canvas);
            }

        }

    }
}

public Canvas getCanvas() {

    if(canvas != null) {

        return canvas;

    } else {

        return null;
    }
}
}
