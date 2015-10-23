package org.acv.bynal.dialog;

import libs.wheel.WheelView;
import libs.wheel.WheelViewAdapter;

import org.acv.bynal.animation.BynalAnimationUtils;
import org.acv.bynal.animation.BynalAnimationUtils.AnimationAction;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import app.bynal.woman.R;

import com.acv.libs.nbase.MbaseAdialog;

public abstract class TvChooserDialog extends MbaseAdialog {
	private String data[];
	private String title_dialog = null;

	public TvChooserDialog(Context context, String data[]) {
		super(context);
		this.data = data;
	}

	public TvChooserDialog(Context context, String data[], String title) {
		// TODO Auto-generated constructor stub
		super(context);
		this.data = data;
		this.title_dialog = title;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(title_dialog != null){
			TextView header = (TextView) findViewById(R.id.poplist_baseTextView1_header);
			header.setText(title_dialog);
			header.setBackgroundDrawable(null);
		}
		if (data != null) {
			((WheelView) findViewById(R.id.listView1)).setViewAdapter(new WheelViewAdapter() {

				@Override
				public void unregisterDataSetObserver(DataSetObserver observer) {

				}

				@Override
				public void registerDataSetObserver(DataSetObserver observer) {

				}

				@Override
				public int getItemsCount() {
					return data.length;
				}

				@Override
				public View getItem(int position, View convertView, ViewGroup parent) {
					// if (convertView == null)
					// convertView = ((LayoutInflater)
					// parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.poplist_item,
					// null);
					// TextView textView = (TextView)
					// convertView.findViewById(R.id._baseTextView1);
					TextView textView = new TextView(parent.getContext());
					textView.setGravity(Gravity.CENTER);
					textView.setText(data[position].toString());
					textView.setTextSize(parent.getContext().getResources().getDimension(R.dimen.dimen_12dp));
					textView.setTextColor(Color.BLACK);

					convertView = textView;
					LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, (int) parent.getContext().getResources().getDimension(R.dimen.dimen_45dp));
					convertView.setLayoutParams(layoutParams);
					return convertView;
				}

				@Override
				public View getEmptyItem(View convertView, ViewGroup parent) {
					return null;
				}
			});

		}

		findViewById(R.id.poplist_baseTextView1_done).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// dismiss();
				closePopActivity(getContext(), findViewById(R.id.piplist_main_item), new AnimationAction() {
					@Override
					public void onAnimationEnd() {
						int position = ((WheelView) findViewById(R.id.listView1)).getCurrentItem();
						TvChooserDialog.this.onItemClick(position, data[position]);

						dismiss();
					}
				});
			}
		});
		findViewById(R.id.piplist_main).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// dismiss();
				// BynalAnimationUtils.closePopActivity(getContext(),
				// findViewById(R.id.piplist_main_item), new AnimationAction() {
				//
				// @Override
				// public void onAnimationEnd() {
				// dismiss();
				// }
				// });
			}
		});

		findViewById(R.id.poplist_baseTextView1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// dismiss();
				closePopActivity(getContext(), findViewById(R.id.piplist_main_item), new AnimationAction() {

					@Override
					public void onAnimationEnd() {
						dismiss();
					}
				});
			}
		});

		openPopActivity(findViewById(R.id.piplist_main_item));
	}

	public abstract void onItemClick(int position, String text);

	@Override
	public int getLayout() {
		return R.layout.poplist;
	}
}