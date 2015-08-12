package com.xxx.common.web.springmvc;


public interface RealPathResolver {
	/**
	 * 获得绝对路径
	 * 
	 * @param path
	 * @return
	 * @see javax.servlet.ServletContext#getRealPath(String)
	 */
	public String get(String path);
	public String getTemplte(String path);
	public String getBakTemplte(String path);
}
