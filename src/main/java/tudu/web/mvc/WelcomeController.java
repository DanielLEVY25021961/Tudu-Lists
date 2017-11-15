package tudu.web.mvc;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import tudu.domain.RolesEnum;

/**
 * The Welcome controller.
 * 
 * @author Julien Dubois
 */
@Controller
@RequestMapping("/welcome")
public class WelcomeController {

    private final Log log = LogFactory.getLog(WelcomeController.class);

    
    
    /**
     * method welcome() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pRequest
     * @param pAuthentication
     * @return :  :  .<br/>
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView welcome(HttpServletRequest pRequest,
                          @RequestParam(required = false) String pAuthentication) {

        ModelAndView mv = new ModelAndView();
        if (pRequest.isUserInRole(RolesEnum.ROLE_USER.name())) {
            mv.setViewName("redirect:/tudu/lists");
        } else {
            mv.addObject("authentication", pAuthentication);
            mv.setViewName("welcome");
        }
        return mv;
    }
}
