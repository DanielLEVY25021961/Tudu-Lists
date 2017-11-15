package tudu.web.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tudu.domain.User;
import tudu.service.UserAlreadyExistsException;
import tudu.service.UserService;

import javax.validation.Valid;

/**
 * Register a new Tudu Lists user.
 *
 * @author Julien Dubois
 */
@Controller
@RequestMapping("/register")
public class RegisterController {

	
    /**
     * userService : UserService :<br/>
     * .<br/>
     */
    @Autowired
    private UserService userService;


    
    /**
     * method display() :<br/>
     * Show the "register a new user" page.<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView display() {
        ModelAndView mv = new ModelAndView("register");
        mv.addObject("user", new User());
        return mv;
    }


    
    /**
     * method register() :<br/>
     * Register a new user.<br/>
     * <br/>
     *
     * @param pUser
     * @param pResult
     * @return :  :  .<br/>
     */
    @RequestMapping(method = RequestMethod.POST)
    public String register(
    		@Valid final User pUser
    			, BindingResult pResult) {
    	
        if (pResult.hasErrors()) {
            return "register";
        }
        if (!pUser.getPassword().equals(pUser.getVerifyPassword())) {
            pResult.rejectValue("verifyPassword", "user.info.password.not.matching");
            return "register";
        }
        try {
            this.userService.createUser(pUser);
        } catch (UserAlreadyExistsException e) {
            pResult.rejectValue("login", "register.user.already.exists");
            return "register";
        }
        return "register_ok";
    }

 
    
    /**
     * method cancel() :<br/>
     * Cancel the action.<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public String cancel() {
        return "cancel";
    }
    
    
}
