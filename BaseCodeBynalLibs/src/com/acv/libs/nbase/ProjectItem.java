package com.acv.libs.nbase;

public class ProjectItem {
	private int id;
	private String title;
	private String img;
	private String total_money_support;
	private String total_date;
	private String percent;
	private String desc;
	private String person;

	public ProjectItem(){

	}

	public ProjectItem(int id,String title, String img ,String total_money_support, String total_date, String percent,String desc, String person){
		this.id = id;
		this.title=title;
        this.img=img;
        this.total_money_support = total_money_support;
        this.total_date = total_date;
        this.percent = percent;
        this.desc = desc;
        this.person = person;
	}

	public int getId() {
		return id;
	}

	public void setTitle(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getTotalMoney() {
		return total_money_support;
	}

	public void setTotalMoney( String total_money_support) {
		this.total_money_support = total_money_support;
	}

	public String getTotalDate() {
		return total_date;
	}

	public void setTotalDate(String total_date) {
		this.total_date = total_date;
	}
	
	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}
	
	public String getDetail() {
		return desc;
	}

	public void setDetail(String desc) {
		this.desc = desc;
	}
	
	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

}