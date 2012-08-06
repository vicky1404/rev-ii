package cl.mdr.ifrs.modules.seguridad.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.ejb.entity.Usuario;

/**
 * Servlet Filter implementation class SeguridadFilter
 */
public class SeguridadFilter implements Filter {
	private transient Logger logger = Logger.getLogger(this.getClass().getName());
	private static final String LOGIN_PAGE = "/login.jsf";
	private static final String INDEX_PAGE = "/index.jsp";

    /**
     * Default constructor. 
     */
    public SeguridadFilter() {
        super();
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		RequestWrapper requestWrapper = new RequestWrapper(((HttpServletRequest) request));
		if(!(requestWrapper.getRequestURI().indexOf(LOGIN_PAGE) > 0) || !(requestWrapper.getRequestURI().indexOf(INDEX_PAGE) > 0)){
			if(requestWrapper.getSession().getAttribute(Usuario.class.getName()) != null){
				chain.doFilter(requestWrapper, response);
			}else{
				logger.error("Usuario No autorizado");
				if (!this.isAjax(requestWrapper)) {
					((HttpServletResponse) response).sendRedirect(requestWrapper.getContextPath().concat(LOGIN_PAGE.concat("?unauthorized=1")));
				}else{
					((HttpServletResponse) response).getWriter().print(this.ajaxRedirect(requestWrapper, requestWrapper.getContextPath().concat(LOGIN_PAGE.concat("?session_expired=1")) ));
					((HttpServletResponse) response).flushBuffer();  
				}
			}		
		}
	}
	
	private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
	
	private String ajaxRedirect(HttpServletRequest request, String page) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version='1.0' encoding='UTF-8'?>");
        sb.append("<partial-response><redirect url=\"").append(request.getContextPath()).append(request.getServletPath()).append(page).append("\"/></partial-response>");
        return sb.toString();
    }

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
