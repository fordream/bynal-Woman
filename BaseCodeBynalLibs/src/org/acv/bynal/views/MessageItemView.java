package org.acv.bynal.views;

import org.acv.bynal.fragment.MessageFragment;
import org.acv.bynal.fragment.SearchFragment;
import org.acv.bynal.message.MessageActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import app.bynal.woman.R;

import com.acv.libs.base.BaseItem;
import com.acv.libs.base.BaseView;
import com.acv.libs.base.ImageLoaderUtils;

public class MessageItemView extends BaseView implements OnClickListener {
	Bitmap avata_default = BitmapFactory.decodeResource(getResources(), R.drawable.default_user2_circle2);
	public MessageItemView(Context context) {
		super(context);
		init(R.layout.messageitem);
	}

	public MessageItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.messageitem);
	}

	@Override
	public void init(int res) {
		super.init(res);
		findViewById(R.id.mesageitem_btn).setOnClickListener(this);
	}

	@Override
	public void refresh() {
		super.refresh();
		setText(((BaseItem) getData()).getString("user_name"), R.id.messageitem_uername);
		setText(((BaseItem) getData()).getString("date"), R.id.messageitem_date);
		setText(((BaseItem) getData()).getString("message"), R.id.messageitem_content);

		String block_flg = ((BaseItem) getData()).getString("block_flg");
		String spam_flg = ((BaseItem) getData()).getString("spam_flg");
		String hide_flg = ((BaseItem) getData()).getString("hide_flg");

		if ("1".equals(block_flg) || "1".equals(spam_flg) || "1".equals(hide_flg)) {
			findViewById(R.id.v3_mesage_item_bg).setBackgroundResource(R.drawable.v3_msage_m1_bg);
			findViewById(R.id.mesage_main_icon_f).setBackgroundResource(R.drawable.message_2_1);
		} else {
			findViewById(R.id.v3_mesage_item_bg).setBackgroundResource(R.drawable.v3_new_and_event_bg);
			findViewById(R.id.mesage_main_icon_f).setBackgroundResource(R.drawable.message_2);
		}

		String user_img = ((BaseItem) getData()).getString("user_img") ;
		ImageView mesage_main_icon = (ImageView) findViewById(R.id.mesage_main_icon);
//		ImageLoaderUtils.getInstance(getContext()).DisplayImage(user_img, mesage_main_icon, BitmapFactory.decodeResource(getResources(), R.drawable.default_user2_circle2));
		
//		MessageFragment.imageLoader.DisplayImage(user_img, mesage_main_icon);
		MessageFragment.imageLoader.DisplayImageAvata(user_img, mesage_main_icon, avata_default);		
	}

	@Override
	public void onClick(View v) {
		if (messageFragment != null) {
			messageFragment.showController(getTop(), (BaseItem) getData());
		}
	}

	private MessageFragment messageFragment;

	public void addMessageFragment(MessageFragment messageFragment) {
		this.messageFragment = messageFragment;
	}
}