package com.learning.web.service.dummy;

import javax.ejb.Stateless;

@Stateless
public class DummyClass {

	public String sayHello() {
		return "hello";
	}

}
