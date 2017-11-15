package tudu.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The license controller.
 * 
 * @author Julien Dubois
 */
@Controller
public class LicenseController {


    /**
     * method license() :<br/>
     * Shows the Tudu Lists' license.<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    @RequestMapping(value="/license", method = RequestMethod.GET)
    public String license() {
        return "license";
    }

    
    
}
