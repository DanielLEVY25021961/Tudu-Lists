package tudu.web.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Julien Dubois
 */
@Controller
public class ErrorController {

    /**
     * log : Log :<br/>
     * .<br/>
     */
    private final Log log = LogFactory.getLog(ErrorController.class);

    
    
    /**
     * method pageNotFound() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pRequest
     * @return :  :  .<br/>
     */
    @RequestMapping("/404")
    public String pageNotFound(
    		final HttpServletRequest pRequest) {
    	
        if (this.log.isInfoEnabled()) {
            this.log.info("404 error");
        }
        
        return "404";
    }

    
    
    /**
     * method internatServerError() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pRequest
     * @return :  :  .<br/>
     */
    @RequestMapping("/500")
    public String internatServerError(
    		final HttpServletRequest pRequest) {
    	
        if (this.log.isInfoEnabled()) {
            this.log.info("500 error");
        }
        
        return "500";
    }
    
    
    
}
