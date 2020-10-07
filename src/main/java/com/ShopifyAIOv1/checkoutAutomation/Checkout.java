package com.ShopifyAIOv1.checkoutAutomation;

public interface Checkout
{
	public final static String checkoutExtension = "/cart/add/";
	public void loadCheckoutVariant(clientInfo info) throws InterruptedException;
	public void setVariant(String variant);
	public String getVariant();
}
