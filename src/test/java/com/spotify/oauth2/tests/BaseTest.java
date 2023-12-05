package com.spotify.oauth2.tests;

import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public class BaseTest {

    @BeforeMethod
    public void beforeMethod(Method method) {
        System.out.println("starting test: " + method.getName());
        System.out.println("thread id is: " + Thread.currentThread().getId());
    }
}
