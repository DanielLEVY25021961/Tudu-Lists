package tudu.web.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tudu.domain.User;
import tudu.service.UserService;

/**
 * Recover a user's lost password.
 * 
 * @author Julien Dubois
 */
@Controller
@RequestMapping("/recoverPassword")
public class RecoverPasswordController {

    /**
     * log : Log :<br/>
     * .<br/>
     */
    private final Log log = LogFactory.getLog(RecoverPasswordController.class);

    
    
    /**
     * userService : UserService :<br/>
     * .<br/>
     */
    @Autowired
    private UserService userService;
    
    

    /**
     * method formBackingObject() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    @ModelAttribute("user")
    public User formBackingObject() {
        return new User();
    }

    
    
    /**
     * method display() :<br/>
     * Show the recover password page action.<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    @RequestMapping(method = RequestMethod.GET)
    public String display() {
        return "recover_password";
    }

    
    
    /**
     * method sendMail() :<br/>
     * Send an email with the new password to the user.<br/>
     * <br/>
     *
     * @param pUser
     * @param pResult
     * @return :  :  .<br/>
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView sendMail(
    		@ModelAttribute User pUser
    			, BindingResult pResult) {
    	
        final ModelAndView mv = new ModelAndView();
        
        mv.addObject(pUser);
        mv.setViewName("recover_password");
        String login = pUser.getLogin().toLowerCase();
        try {
            pUser = this.userService.findUser(login);
        } catch (ObjectRetrievalFailureException orfe) {
            pResult.rejectValue("login", "recover.password.no.user");
            return mv;
        }
        if (pUser.getEmail() == null || pUser.getEmail().equals("")) {
            pResult.rejectValue("login", "recover.password.no.email");
            return mv;
        }
        try {
            this.userService.sendPassword(pUser);
            mv.addObject("success", "true");
        } catch (RuntimeException e) {
            pResult.rejectValue("login", "recover.password.smtp.error");
            this.log.warn("Mail sending error : " + e.getMessage());
        }
        return mv;
    }
    
    
}
