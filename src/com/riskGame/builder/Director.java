package com.riskGame.builder;

import java.io.IOException;

public class Director {
	private ProductBuilder builder;

	public void setBuilder(ProductBuilder builder) {
		this.builder = builder;
	}

	public ProductBuilder getBuilder() {
		return builder;
	}
	public String constructProduct(String command) throws IOException {
		return builder.buildGame(command);
	}
}
