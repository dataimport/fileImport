package com.xxx.common.web.springmvc;

import java.io.Writer;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerExceptionHandler implements TemplateExceptionHandler {

	public void handleTemplateException(TemplateException te, Environment env, Writer out) throws TemplateException {
		System.out.println("freemarker模板异常了 " + te.getMessage());
		// throw te;
	}

}
