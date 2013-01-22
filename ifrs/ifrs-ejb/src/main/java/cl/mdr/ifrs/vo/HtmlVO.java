package cl.mdr.ifrs.vo;


import java.io.Serializable;

import cl.mdr.ifrs.ejb.entity.Html;

public class HtmlVO implements Serializable{
	private static final long serialVersionUID = -1609264473806967744L;


	public HtmlVO() {
        super();
    }
    
    private Html html;


    public HtmlVO(Html html) {
        super();
        this.html = html;
    }

    public void setHtml(Html html) {
        this.html = html;
    }

    public Html getHtml() {
        return html;
    }
}
