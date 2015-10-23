package com.acv.libs.base;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class VNPResize {
	public interface ICompleteInit {
		public void complete();
	}

	private static final String LISTTEXTSIZE = "LISTTEXTSIZE";

	private static VNPResize instance = new VNPResize();

	private VNPResize() {
	}

	public static VNPResize getInstance() {
		return instance == null ? (instance = new VNPResize()) : instance;
	}

	public int getWidthScreen() {
		Display display = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		return display.getWidth();
	}

	public int getHeightScreen() {
		Display display = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		return display.getHeight();
	}

	private Context context;
	private int baseWidth = 320;
	private int baseHeight = 0;

	public void init(Context context, int baseWidth, int baseHeight,
			final ICompleteInit completeInit, TextView textView) {
		if (baseHeight > 0)
			this.baseHeight = baseHeight;

		if (baseWidth > 0)
			this.baseWidth = baseWidth;

		if (context != null) {
			this.context = context;
		}

		if (textView != null) {
			update(1, completeInit, textView, new JSONArray());
		}
	}

	public String getListTextSize() {
		DataStore.getInstance().init(context);
		return DataStore.getInstance().get(LISTTEXTSIZE, null);
	}

	/**
	 * 
	 * @param textView
	 * @param height
	 */
	public void setTextsizeListTextSize(View textView, int _height_) {
		float _height = _height_ * VNPResize.getInstance().getScale();
		double size = 0;
		try {
			JSONArray array = new JSONArray(getListTextSize());

			for (int i = 0; i < array.length(); i++) {
				int height = array.getJSONObject(i).getInt("height");
				if (height > _height) {
					break;
				}

				size = array.getJSONObject(i).getDouble("size");
			}

			if (textView != null && size > 0 && textView instanceof TextView) {
				((TextView) textView).setTextSize((float) size);
			}
		} catch (JSONException e) {
			LogUtils.e("ERROR", e);
		}
	}

	private void update(final int size, final ICompleteInit completeInit,
			final TextView textView, final JSONArray jsonArray) {

		if (getListTextSize() != null) {
			if (completeInit != null) {
				completeInit.complete();
			}

			return;
		}

		if (size > 150) {
			DataStore.getInstance().save(LISTTEXTSIZE, jsonArray.toString());
			if (completeInit != null) {
				completeInit.complete();
			}

			return;
		}

		final Runnable _runnable = new Runnable() {
			@Override
			public void run() {
				// LogUtils.e("ABCDE", String.format("%s %s %s ", size,
				// textView.getTextSize(), textView.getHeight()));
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("size", size);
					jsonObject.put("textsize", textView.getTextSize());
					jsonObject.put("height", textView.getHeight());
					jsonArray.put(jsonObject);
				} catch (JSONException e) {

				}

				update(size + 1, completeInit, textView, jsonArray);
			}
		};
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				textView.setTextSize(size);
				textView.post(_runnable);
			}
		};

		textView.post(runnable);
	}

	private float textSize = 1;

	public float getTextSize() {
		return textSize;
	}

	public float getScale() {
		try {
			float SCREEN_HIGHT = (float) baseHeight;
			float SCREEN_WIDTH = (float) baseWidth;
			float res_width = getWidthScreen();
			float res_height = getHeightScreen();

			if (SCREEN_HIGHT == 0) {
				return res_width / SCREEN_WIDTH;
			}

			float scale = res_height / SCREEN_HIGHT;

			if (SCREEN_HIGHT / res_height < SCREEN_WIDTH / res_width) {
				scale = res_width / SCREEN_WIDTH;
			}

			return scale;
		} catch (Exception exception) {
			return 1.0f;
		}
	}

	public void resizeSacle(View view, int width, int height) {
		float scale = getScale();
		if (width > 0) {
			width = (int) (width * scale);
		}

		if (height > 0) {
			height = (int) (height * scale);
		}

		resize(view, width, height);
	}

	/**
	 * 
	 * @param view
	 * @param width
	 * @param height
	 */
	public void resize(View view, int width, int height) {
		if (view != null && view.getLayoutParams() != null) {
			ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
			layoutParams.width = (int) (width);
			layoutParams.height = (int) (height);
			view.setLayoutParams(layoutParams);
		}
	}

	/**
	 * 
	 * @param view
	 * @param left
	 * @param top
	 */
	public void sendViewToPosition(View view, int left, int top) {
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		int _left = (int) (left);
		int _top = (int) (top);

		if (layoutParams instanceof RelativeLayout.LayoutParams) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					layoutParams.width, layoutParams.height);
			lp.setMargins(_left, _top, 0, 0);
			view.setLayoutParams(lp);
		} else if (layoutParams instanceof LinearLayout.LayoutParams) {
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					layoutParams.width, layoutParams.height);
			lp.setMargins(_left, _top, 0, 0);
			view.setLayoutParams(lp);
		} else if (layoutParams instanceof FrameLayout.LayoutParams) {
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
					layoutParams.width, layoutParams.height);
			lp.setMargins(_left, _top, 0, 0);
			view.setLayoutParams(lp);
		} else if (layoutParams instanceof TableRow.LayoutParams) {
			TableRow.LayoutParams lp = new TableRow.LayoutParams(
					layoutParams.width, layoutParams.height);
			lp.setMargins(_left, _top, 0, 0);
			view.setLayoutParams(lp);
		} else if (layoutParams instanceof TableLayout.LayoutParams) {
			TableLayout.LayoutParams lp = new TableLayout.LayoutParams(
					layoutParams.width, layoutParams.height);
			lp.setMargins(_left, _top, 0, 0);
			view.setLayoutParams(lp);
		}
	}

	/**
	 * 
	 * @param view
	 * @param heightTextView
	 */
	public void setTextsize(View view, int heightTextView) {
		setTextsizeListTextSize(view, heightTextView);
		/*
		 * if (view instanceof TextView) { double d =
		 * VNPConfigSizeTextViewManager.getInstance().getTextSize( (int)
		 * (heightTextView * getScale())); if (d > 0) { ((TextView)
		 * view).setTextSize((float) d); } else { ((TextView)
		 * view).setTextSize(heightTextView / textSize); } }
		 */
	}
}
