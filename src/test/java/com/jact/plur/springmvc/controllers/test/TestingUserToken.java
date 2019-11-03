package com.jact.plur.springmvc.controllers.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@Component
public class TestingUserToken implements RequestPostProcessor {

    @Override
    public MockHttpServletRequest postProcessRequest(
            MockHttpServletRequest mockHttpServletRequest) {
        mockHttpServletRequest.setParameter("user", "testinguser");
        return mockHttpServletRequest;
    }
}
