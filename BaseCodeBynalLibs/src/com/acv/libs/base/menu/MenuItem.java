package com.acv.libs.base.menu;

import java.util.ArrayList;
import java.util.List;

public class MenuItem extends Menu {
	public MenuItem(String resName, int resImgge, boolean isHeader) {
		super(resName, resImgge, isHeader);
	}

	private List<MenuChildItem> childItems = new ArrayList<MenuChildItem>();

	public void addChildItem(MenuChildItem childItem) {
		childItems.add(childItem);
	}

	public MenuChildItem getMenuChildItem(int index) {
		return childItems.get(index);
	}

	public int sizeOfChild() {
		return childItems.size();
	}
}