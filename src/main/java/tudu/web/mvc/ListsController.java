package tudu.web.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tudu.Constants;
import tudu.domain.TodoList;
import tudu.domain.User;
import tudu.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.TreeSet;

/**
 * Show the Todos belonging to the current List.
 * 
 * @author Julien Dubois
 */
@Controller
@RequestMapping("/lists")
public class ListsController {

	
	
    /**
     * userService : UserService :<br/>
     * .<br/>
     */
    @Autowired
    private UserService userService;


    
    /**
     * method showTodos() :<br/>
     * Show the Todos main page.<br/>
     * <br/>
     *
     * @param pRequest
     * @return :  :  .<br/>
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showTodos(
    		final HttpServletRequest pRequest) {

        User user = this.userService.getCurrentUser();
        Collection<TodoList> todoLists = new TreeSet<TodoList>(user
                .getTodoLists());

        if (!todoLists.isEmpty()) {
            String listId = pRequest.getParameter("listId");
            if (listId != null) {
                pRequest.setAttribute("defaultList", listId);
            } else {
                pRequest.setAttribute("defaultList", todoLists.iterator().next()
                        .getListId());
            }
        }

        String dateFormat = user.getDateFormat();
        pRequest.getSession().setAttribute("dateFormat", dateFormat);
        String calendarDateFormat = "%m/%d/%Y";
        if (Constants.DATEFORMAT_US_SHORT.equals(dateFormat)) {
            calendarDateFormat = "%m/%d/%y";
        } else if (Constants.DATEFORMAT_FRENCH.equals(dateFormat)) {
            calendarDateFormat = "%d/%m/%Y";
        } else if (Constants.DATEFORMAT_FRENCH_SHORT.equals(dateFormat)) {
            calendarDateFormat = "%d/%m/%y";
        }
        pRequest.getSession().setAttribute("calendarDateFormat",
                calendarDateFormat);

        return new ModelAndView("todos");
    }
    
    
}
