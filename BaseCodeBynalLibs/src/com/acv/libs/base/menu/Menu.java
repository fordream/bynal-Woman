package com.acv.libs.base.menu;

public class Menu {

	private String name;
	private int resImgge;
	public boolean isHeader = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getResImgge() {
		return resImgge;
	}

	public void setResImgge(int resImgge) {
		this.resImgge = resImgge;
	}

	public Menu(String string, int resImgge, boolean isHeader) {
		super();
		this.name = string;
		this.resImgge = resImgge;
		this.isHeader = isHeader;
	}

}
