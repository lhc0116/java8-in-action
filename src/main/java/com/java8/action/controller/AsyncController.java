package com.java8.action.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.concurrent.CompletableFuture;

/**
 * @author huaichuanli
 * @version 1.0
 * @date 2019/11/5
 */
@RestController
public class AsyncController {

	@GetMapping("/forward")
	public CompletableFuture<ModelAndView> forward() {
		return CompletableFuture.supplyAsync(() -> {
			this.delay();
			RedirectView redirectView = new RedirectView("https://www.cnblogs.com/qingshanli/");
			redirectView.addStaticAttribute("hint", "CompletableFuture组装ModelAndView视图,异步返回结果");
			return new ModelAndView(redirectView);
		});
	}

	@GetMapping("/async")
	public CompletableFuture<String> async() {
		return CompletableFuture.supplyAsync(() -> {
			this.delay();
			return "CompletableFuture作为Controller的返回值,异步返回结果";
		});
	}

	public void delay() {
		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
