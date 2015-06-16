package com.herongwang.p2p.website.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

public class MyFreeMarkerView extends FreeMarkerView {

	private static final String CONTEXT_PATH = "basePath";

	@Override
	protected void exposeHelpers(Map<String, Object> model,
			HttpServletRequest request) throws Exception {
		String path = request.getContextPath() + "/";
		model.put(CONTEXT_PATH, path);
		super.exposeHelpers(model, request);
	}
}
