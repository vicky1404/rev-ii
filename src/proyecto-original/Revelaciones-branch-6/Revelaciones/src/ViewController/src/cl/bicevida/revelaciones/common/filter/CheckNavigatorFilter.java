package cl.bicevida.revelaciones.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filtro que verifica en cada peticion de URL /faces/* si el navegador es soportado por la aplicacion,
 * en el caso de ADF soporta hasta MSIE 9 pero por la forma de exponer las app a traves de un 
 * iframe en portal este se comporta incorrectamente
 * los navegadores soportados estan en el arreglo String[] supportedBrowsers
 * @author rodrigo.reyes@bicevida.cl
 * @link http://cl.linkedin.com/in/rreyesc
 * 
 */
public class CheckNavigatorFilter implements Filter {
    private FilterConfig _filterConfig = null;
    private static final String[] supportedBrowsers = {"Chrome", "Firefox", "Safari", "MSIE 8"};
    private static final String BROWSER_ERROR_PAGE = "/pages/error/browser-error.jspx";
    
    public void init(FilterConfig filterConfig) throws ServletException {
        _filterConfig = filterConfig;
    }

    public void destroy() {
        _filterConfig = null;
    }

    /** 
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {  
        //ejecuta la validacion solo si el request url es diferente a la pagina de error de browser, para evitar un bucle infinito.
        if(!(((HttpServletRequest) request).getRequestURI().indexOf(BROWSER_ERROR_PAGE) > 0)){
            final String userAgent = ((HttpServletRequest) request).getHeader("User-Agent");
            for (String supportedBrowser : supportedBrowsers) {
                if (userAgent.contains(supportedBrowser)) {
                    chain.doFilter(request, response);
                    return;
                }
            }
            // Unsupported browser
            ((HttpServletResponse)response).sendRedirect(((HttpServletRequest) request).getContextPath().concat(BROWSER_ERROR_PAGE));
        }
        
    }
}
