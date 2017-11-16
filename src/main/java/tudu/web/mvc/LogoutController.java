package tudu.web.mvc;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The Log out controller.
 * 
 * @author Julien Dubois
 */
@Controller
@RequestMapping("/logout")
public class LogoutController {

    /**
     * method logout() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pRequest
     * @param pResponse
     * @return : String :  .<br/>
     */
    @RequestMapping(method = RequestMethod.GET)
    public String logout(HttpServletRequest pRequest, HttpServletResponse pResponse) {

        SecurityContextHolder.clearContext();

        // Remove all session data
        HttpSession session = pRequest.getSession();
        for (Enumeration<String> e = session.getAttributeNames(); e.hasMoreElements();) {
            session.removeAttribute(e.nextElement());
        }

        // Remove the cookie
        Cookie terminate = new Cookie(
                AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY,
                null);
        terminate.setMaxAge(-1);
        terminate.setPath(pRequest.getContextPath() + "/");
        pResponse.addCookie(terminate);
        return "logout";
    }
    
    
}
