package com.dilaj.stripes.web;

import java.io.StringReader;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
/**
 * 
 * @author Dilaj
 *
 */
@UrlBinding("/post")
public class PostActionBean extends BaseActionBean {


	@DefaultHandler
	public Resolution main() {
		return new  StreamingResolution("text", new StringReader(data));
	}
}
