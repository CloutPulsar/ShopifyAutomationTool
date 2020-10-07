package com.ShopifyAIOv1.checkoutAutomation;
import java.util.ArrayList;

public interface BasicSiteFunctions
{
	public String getUrl();
	public String getSites();
	public void setUrl(String url);
	public void mapDefaultSites();
	public void setTotalPages(String pages, String key);
	public String getKeywords();
	public void setKeywords(String[] keywords);
	public String getSizeOption();
	public void setSizeOption(String sizeOption);
	public int[] getPriceRange();
	public void setPriceRange(int[] range);
}
