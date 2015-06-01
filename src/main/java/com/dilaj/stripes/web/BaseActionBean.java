package com.dilaj.stripes.web;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
/**
 * 
 * @author Dilaj
 *
 */
public abstract class BaseActionBean implements ActionBean {

	protected ActionBeanContext context;
	protected String data;

	@Override
	public ActionBeanContext getContext() {
		return context;
	}

	@Override
	public void setContext(ActionBeanContext context) {
		this.context = context;
	}
	
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
