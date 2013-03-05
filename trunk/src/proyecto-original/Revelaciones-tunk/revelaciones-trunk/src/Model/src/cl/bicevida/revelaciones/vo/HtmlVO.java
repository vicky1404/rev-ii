package cl.bicevida.revelaciones.vo;


import cl.bicevida.revelaciones.ejb.entity.Html;

import java.io.Serializable;

public class HtmlVO implements Serializable{
    
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
