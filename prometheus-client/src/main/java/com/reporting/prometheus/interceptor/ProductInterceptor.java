package com.reporting.prometheus.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ProductInterceptor implements HandlerInterceptor {


	@Override
	public boolean preHandle
	(HttpServletRequest request, HttpServletResponse response, Object handler) 
			throws Exception {
		System.out.println("Strating of the Pre Handle method :: " + System.currentTimeMillis() );
		System.out.println("RequestURL :: " + request.getRequestURL());
		/*
		 * System.out.println("PathInfo :: " + request.getPathInfo());
		 * System.out.println("ContextPath :: " + request.getContextPath());
		 * System.out.println("Method :: " + request.getMethod());
		 * System.out.println("QueryString :: " + request.getQueryString());
		 * System.out.println("RemoteAddr :: " + request.getRemoteAddr());
		 * System.out.println("RemotePort :: " + request.getRemotePort());
		 * System.out.println("RemoteUser :: " + request.getRemoteUser());
		 * System.out.println("RemoteHost :: " + request.getRemoteHost());
		 * System.out.println("RequestURL :: " + request.getRequestURL());
		 * System.out.println("RequestURI :: " + request.getRequestURI());
		 */
		System.out.println("Inside the Pre Handle method");
		return true;
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler, ModelAndView modelAndView) throws Exception {

		System.out.println("Inside the Post Handle method");
	}
	@Override
	public void afterCompletion
	(HttpServletRequest request, HttpServletResponse response, Object 
			handler, Exception exception) throws Exception {

		System.out.println("After completion of request and response");
	}
}