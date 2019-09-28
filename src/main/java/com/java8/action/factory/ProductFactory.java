package com.java8.action.factory;

import com.java8.action.entity.Bond;
import com.java8.action.entity.Loan;
import com.java8.action.entity.Product;
import com.java8.action.entity.Stock;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class ProductFactory {

	private static Map<String, Supplier<? extends Product>> factory = new HashMap<>();

	static {
		factory.put("bond", Bond::new);
		factory.put("loan", Loan::new);
		factory.put("stock", Stock::new);
	}

	public static Product createProduct(String name) {
		Supplier<? extends Product> supplier = factory.get(name);
		if (Objects.nonNull(supplier)) return supplier.get();
		throw new IllegalArgumentException("no such element");
	}
}