package a.com.acv.bynal.v3.dialog;

import a.com.acv.bynal.v3.views.MainListMenuView;
import a.com.acv.bynal.v3.views.MainListMenuView.MainListMenuViewListener;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import app.bynal.woman.R;

import com.acv.libs.base.db.AccountDB;
import com.acv.libs.base.tab.BaseTabActivity;
import com.acv.libs.base.util.ByUtils;
import com.acv.libs.nbase.MbaseAdialog;

public class MenuLeftDialog extends MbaseAdialog implements MainListMenuViewListener {
	private AccountDB accountDB;

	public MenuLeftDialog(Context context) {
		super(context);
		accountDB = new AccountDB(getContext());
	}

	private MainListMenuView mainListMenuView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mainListMenuView = new MainListMenuView(getContext()));
		mainListMenuView.setMainListMenuViewListener(this);
		mainListMenuView.findViewById(R.id.mMain).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.open_dialog_alpharepeat));
		mainListMenuView.findViewById(R.id.mMain1).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.opendialog));
	}

	@Override
	public int getLayout() {
		return 0;
	}

	@Override
	public void mOnClick(final int[] a) {

		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.closedialog);

		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				dismiss();
				if (a[0] == -1) {
					// goto home
					((BaseTabActivity) getmContext()).refreshMenuAndgotoHome();
				} else if (a[0] == 0) {
					// close
				} else {
					int id = a[1];

					if (id == R.string.menu_home_Login && !ByUtils.isBlank(accountDB.getToken())) {
						id = R.string.menu_home_profile;
					}
					
					BaseTabActivity.senBroadCast(getContext(), id);
				}
			}
		});
		mainListMenuView.findViewById(R.id.mMain1).startAnimation(animation);
		mainListMenuView.findViewById(R.id.mMain).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.close_dialog_alpharepeat));
	}

	@Override
	public void mOnItemClick(int[] a) {

	}
}