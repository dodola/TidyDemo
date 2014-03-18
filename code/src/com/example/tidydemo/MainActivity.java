package com.example.tidydemo;

import com.nineoldandroids.view.ViewHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	private View overlayLayout;
	private Drawable overlayBackground;
	private DrawerLayout drawLayout;
	private Bitmap scaled;
	private View contentFrame;
	private View menuFrame;

	private DrawerListener drawerListner = new DrawerListener() {

		@Override
		public void onDrawerStateChanged(int newState) {

		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {

			if (slideOffset > 0.0f) {
				setBlurAlpha(slideOffset);
			} else {
				clearBlurImage();
			}
		}

		@Override
		public void onDrawerOpened(View drawerView) {

		}

		@Override
		public void onDrawerClosed(View drawerView) {
			clearBlurImage();
		}
	};

	private void setBlurAlpha(float slideOffset) {
		if (overlayLayout.getVisibility() != View.VISIBLE) {
			setBlurImage();

		}
		ViewHelper.setAlpha(overlayLayout, easyOut(slideOffset, 0, 1, 1f));
	}

	public float easyOut(float t, float b, float c, float d) {
		return c * ((t = t / d - 1) * t * t + 1) + b;
	}

	public void setBlurImage() {
		scaled = null;
		scaled = BlurUtils.drawViewToBitmap(scaled, contentFrame, contentFrame.getMeasuredWidth(), contentFrame.getMeasuredHeight(), 5,
				overlayBackground);
		Bitmap blured = BlurUtils.apply(this, scaled, 15);
		overlayLayout.setBackground(new BitmapDrawable(blured));
		overlayLayout.setVisibility(View.VISIBLE);
	}

	public void clearBlurImage() {
		scaled = null;
		overlayLayout.setVisibility(View.GONE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		overlayLayout = this.findViewById(R.id.overlayLayout);
		menuFrame = this.findViewById(R.id.menu_frame);
		int[] attrs = { android.R.attr.windowBackground };
		TypedValue outValue = new TypedValue();
		this.getTheme().resolveAttribute(android.R.attr.windowBackground, outValue, true);

		TypedArray style = this.getTheme().obtainStyledAttributes(outValue.resourceId, attrs);
		overlayBackground = style.getDrawable(0);
		contentFrame = findViewById(R.id.content_frame);
		drawLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
		drawLayout.setDrawerListener(drawerListner);
		drawLayout.setScrimColor(Color.TRANSPARENT);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
