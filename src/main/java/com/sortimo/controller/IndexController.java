package com.sortimo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	/**
	 * Leitet einen Request von Application Root auf Swagger-UI um
	 * 
	 * @param response
	 * @param request
	 */
	@RequestMapping("/")
	public void method(HttpServletResponse response, HttpServletRequest request) {
		
		// Http Header fuer response vorbereiten
		try {
			URL url = new URL(request.getRequestURL().toString());
			String hostUrl = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + "/swagger-ui.html";
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.sendRedirect(hostUrl);
		} catch (MalformedURLException e) {
			System.err.println("Fehler beim zusammenbau der redirect URL!");
		}
		catch (IOException e) {
			System.err.println("Beim Redirect lief etwas schief!");
		}
    }
	
}
