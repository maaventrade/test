package com.example.testmoving;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class CalcThread extends Thread {

	private Item[] mItems;
	private boolean mRun = false;

	public CalcThread(Item[] items) {
		mItems = items;
	}

	public void setRunnable(boolean run) {
		mRun = run;
	}

	public void run() {

		while (mRun) {

			synchronized (mItems) {
				for (Item i : mItems)
					i.calc();
			}

		}
	}

}
