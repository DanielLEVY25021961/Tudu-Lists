package tudu.web.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tudu.Constants;
import tudu.domain.User;
import tudu.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * class AccountController :<br/>
 * Manage the user information.<br/>
 * <br/>
 *
 * - Exemple d'utilisation :<br/>
 *<br/>
 * 
 * - Mots-clé :<br/>
 * <br/>
 *
 * - Dépendances :<br/>
 * <br/>
 *
 *
 * @author Julien Dubois
 * @version 1.0
 * @since 14 nov. 2017
 *
 */
@Controller
@RequestMapping("/account")
public class AccountController {

	
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
    	
        final User user = this.userService.getCurrentUser();
        user.setVerifyPassword(user.getPassword());
        return user;
    }

    
    
    /**
     * method display() :<br/>
     * Display the "my user info" page.<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    @RequestMapping(method = RequestMethod.GET)
    public String display() {
        return "account";
    }

    
    
    /**
     * method update() :<br/>
     * Update user information.<br/>
     * <br/>
     *
     * @param pUser
     * @param pResult
     * @param pRequest
     * @return :  :  .<br/>
     */
    @RequestMapping(method = RequestMethod.POST)
    public String update(
    		@Valid User pUser
    			, BindingResult pResult
    				, HttpServletRequest pRequest) {
    	
        if (pResult.hasErrors()) {
            return "account";
        }
        if (!pUser.getPassword().equals(pUser.getVerifyPassword())) {
            pResult.rejectValue("verifyPassword", "user.info.password.not.matching");
            return "account";
        }

        // If the user hacked the drop-down list, defaults to US date format
        if (pUser.getDateFormat() == null
                || (!pUser.getDateFormat().equals(Constants.DATEFORMAT_US)
                        && !pUser.getDateFormat().equals(Constants.DATEFORMAT_US_SHORT)
                        && !pUser.getDateFormat().equals(Constants.DATEFORMAT_FRENCH) && !pUser.getDateFormat()
                        .equals(Constants.DATEFORMAT_FRENCH_SHORT))) {

            pUser.setDateFormat(Constants.DATEFORMAT_US);
        }
        
        this.userService.updateUser(pUser);
        pRequest.setAttribute("updated", "ok");
        return "account";
    }
    
    
}
