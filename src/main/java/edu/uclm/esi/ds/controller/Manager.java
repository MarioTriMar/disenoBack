package edu.uclm.esi.ds.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import edu.uclm.esi.ds.ws.Wrapper;


@Component
public class Manager {
	private ConcurrentHashMap<String, Wrapper> wrapperSessionsByHttp = new ConcurrentHashMap<>();
	
	public void addWrapperSession(Wrapper ws) {
		this.wrapperSessionsByHttp.put(ws.getHttpSessionId(), ws);
	}
	public Wrapper findWrapperSessionByHttp(String httpSession) {
		return wrapperSessionsByHttp.get(httpSession);
	}
	private static class ManagerHolder {
		static Manager singleton=new Manager();
		}
	@Bean
	public static Manager get() {
		return ManagerHolder.singleton;
	}
	public String readFile(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		try (InputStream fis = classLoader.getResourceAsStream(fileName)) {
			byte[] b = new byte[fis.available()];
			fis.read(b);
			return new String(b);
		}
	}
}
