package com.riskGame.builder;

import java.io.IOException;

public abstract class ProductBuilder {
	protected Product product;

	public Product getProduct() {
		return product;
	}
	
	public void createNewProduct() {
		product = new Product();
	}
	
	abstract String buildGame(String command) throws IOException;
}