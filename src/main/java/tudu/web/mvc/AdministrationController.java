package tudu.web.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import tudu.domain.Property;
import tudu.domain.User;
import tudu.service.ConfigurationService;
import tudu.service.UserService;

import java.util.List;


/**
 * class AdministrationController :<br/>
 * Application administration controler.<br/>
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
@RequestMapping("/admin")
public class AdministrationController {

	
    /**
     * configurationService : ConfigurationService :<br/>
     * .<br/>
     */
    @Autowired
    private ConfigurationService configurationService;

    
    /**
     * userService : UserService :<br/>
     * .<br/>
     */
    @Autowired
    private UserService userService;

    
    
    /**
     * method display() :<br/>
     * Show the administration page action.<br/>
     * <br/>
     *
     * @param pPage
     * @return :  :  .<br/>
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView display(
    		@RequestParam(required = false) String pPage) {

        ModelAndView mv = new ModelAndView();
        AdministrationModel model = new AdministrationModel();

        if (pPage == null || pPage.equals("")) {
            pPage = "configuration";
        }

        if (pPage.equals("configuration")) {
            mv.addObject("page", "configuration");

            Property propertyStaticPath = this.configurationService.getProperty(
                    "application.static.path");
            if (propertyStaticPath != null) {
                model.setPropertyStaticPath(propertyStaticPath.getValue());
            }

            Property propertyGoogleAnalyticsKey = this.configurationService.getProperty(
                    "google.analytics.key");
            if (propertyGoogleAnalyticsKey != null) {
                model.setGoogleAnalyticsKey(propertyGoogleAnalyticsKey.getValue());
            }

            String smtpHost = this.configurationService.getProperty("smtp.host")
                    .getValue();
            model.setSmtpHost(smtpHost);

            String smtpPort = this.configurationService.getProperty("smtp.port")
                    .getValue();
            model.setSmtpPort(smtpPort);

            String smtpUser = this.configurationService.getProperty("smtp.user")
                    .getValue();
            model.setSmtpUser(smtpUser);

            String smtpPassword = this.configurationService.getProperty(
                    "smtp.password").getValue();
            model.setSmtpPassword(smtpPassword);

            String smtpFrom = this.configurationService.getProperty("smtp.from")
                    .getValue();
            model.setSmtpFrom(smtpFrom);


        } else if (pPage.equals("users")) {
            mv.addObject("page", "users");
            model.setSearchLogin("");
            mv.addObject("numberOfUsers", this.userService.getNumberOfUsers());

        }
        mv.addObject("administrationModel", model);
        mv.setViewName("administration");
        return mv;
    }

 
    
    /**
     * method update() :<br/>
     * Update the application configuration.<br/>
     * <br/>
     *
     * @param pModel
     * @return :  :  .<br/>
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView update(
    		@ModelAttribute AdministrationModel pModel) {
    	
        ModelAndView mv = new ModelAndView();
        
        if ("configuration".equals(pModel.getAction())) {
            this.configurationService.updateApplicationProperties(pModel.getPropertyStaticPath(),
                    pModel.getGoogleAnalyticsKey());

            this.configurationService.updateEmailProperties(pModel.getSmtpHost(), pModel.getSmtpPort(),
                    pModel.getSmtpUser(), pModel.getSmtpPassword(), pModel.getSmtpFrom());

            mv = this.display("configuration");
            mv.addObject("success", "true");
        } else {
            if ("disableUser".equals(pModel.getAction())) {
                String login = pModel.getLogin();
                this.userService.disableUser(login);
            }
            if ("enableUser".equals(pModel.getAction())) {
                String login = pModel.getLogin();
                this.userService.enableUser(login);
            }
            if (pModel.getSearchLogin() == null) {
                pModel.setSearchLogin("");
            }
            List<User> users = this.userService.findUsersByLogin(pModel.getSearchLogin());
            mv.addObject("users", users);
            mv.addObject("page", "users");
            mv.addObject("administrationModel", pModel);
            mv.setViewName("administration");
        }

        return mv;
    }
    
    
}
