package com.tree.exclutionrules.dao;

public class Item {
	private int itemId;
	private String itm_Desc2;
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getItm_Desc2() {
		return itm_Desc2;
	}
	public void setItm_Desc2(String itm_Desc2) {
		this.itm_Desc2 = itm_Desc2;
	}
	@Override
	public String toString() {
		return "Item [itemId=" + itemId + ", itm_Desc2=" + itm_Desc2 + "]";
	}
}
