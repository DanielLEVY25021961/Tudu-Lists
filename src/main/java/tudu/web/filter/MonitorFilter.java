package tudu.web.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StopWatch;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Julien Dubois
 */

public class MonitorFilter implements Filter {

    /**
     * log : Log :<br/>
     * .<br/>
     */
    private final Log log = LogFactory.getLog("monitoring");

    
    /**
     * isMonitored : boolean :<br/>
     * .<br/>
     */
    private boolean isMonitored = false;
    
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig pConfig) 
    		throws ServletException {
    	
        if (this.log.isDebugEnabled()) {
            this.isMonitored = true;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(
    		final ServletRequest pRequest
    			, final ServletResponse pResponse
    				, final FilterChain pChain) 
    							throws ServletException, IOException {
    	
        if (this.isMonitored) {
            StopWatch watch = new StopWatch();
            watch.start();
            pChain.doFilter(pRequest, pResponse);
            watch.stop();

            long time = watch.getLastTaskTimeMillis();

            StringBuffer sb = new StringBuffer();
            if (pRequest instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest) pRequest;
                int length = request.getRequestURL().length();
                if (length > 60) {
                    sb.append(request.getRequestURL().substring(length - 60, length));
                } else {
                    sb.append(request.getRequestURL());
                }
            }
            sb.append(" | ");
            sb.append(time);
            sb.append(" | ");
            String userName;
            Authentication authent = SecurityContextHolder.getContext().getAuthentication();
            if (authent != null) {
                Object principal = authent.getPrincipal();
                if (principal instanceof String) {
                    userName = "anonymous";
                } else {
                    User springSecurityUser = (User) principal;
                    userName = springSecurityUser.getUsername();
                }
                sb.append(userName);
            }
            sb.append(" | ");
            sb.append(pRequest.getRemoteAddr());
            this.log.debug(sb.toString());

        } else {
            pChain.doFilter(pRequest, pResponse);
        }
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
    	return;
    }

    
    
}
