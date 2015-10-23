package com.acv.libs.base;

import android.content.Context;

public class HeaderOption {
	public enum TYPEHEADER {
		NORMAL, CHECKBOX
	}

	private boolean showButtonLeft = false;
	private boolean showButtonRight = false;
	private boolean showButtonRight2 = false;
	private int resHeader = 0;
	private int resDrawableLeft = 0;
	private int resDrawableRight = 0;
	private int resDrawableRight2 = 0;
	
	public int getResDrawableLeft() {
		return resDrawableLeft;
	}

	public void setResDrawableLeft(int resDrawableLeft) {
		this.resDrawableLeft = resDrawableLeft;
	}

	public int getResDrawableRight() {
		return resDrawableRight;
	}

	public void setResDrawableRight(int resDrawableRight) {
		this.resDrawableRight = resDrawableRight;
	}
	
	public int getResDrawableRight2() {
		return resDrawableRight2;
	}

	public void setResDrawableRight2(int resDrawableRight2) {
		this.resDrawableRight2 = resDrawableRight2;
	}

	private Context context;
	private TYPEHEADER typeHeader = TYPEHEADER.NORMAL;

	public TYPEHEADER getTypeHeader() {
		return typeHeader;
	}

	public void setTypeHeader(TYPEHEADER typeHeader) {
		this.typeHeader = typeHeader;
	}

	public boolean isShowButtonLeft() {
		return showButtonLeft;
	}

	public void setShowButtonLeft(boolean showButtonLeft) {
		this.showButtonLeft = showButtonLeft;
	}

	public boolean isShowButtonRight() {
		return showButtonRight;
	}

	public void setShowButtonRight(boolean showButtonRight) {
		this.showButtonRight = showButtonRight;
	}
	
	public boolean isShowButtonRight2() {
		return showButtonRight2;
	}

	public void setShowButtonRight2(boolean showButtonRight2) {
		this.showButtonRight2 = showButtonRight2;
	}

	public int getResHeader() {
		return resHeader;
	}

	public void setResHeader(int resHeader) {
		this.resHeader = resHeader;
	}

	public HeaderOption(Context mContext, TYPEHEADER typeHeader) {
		this.typeHeader = typeHeader;
		context = mContext;
	}

	public void onClickButtonLeft() {

	}

	public void onClickButtonRight() {

	}
	
	public void onClickButtonRight2() {

	}
}