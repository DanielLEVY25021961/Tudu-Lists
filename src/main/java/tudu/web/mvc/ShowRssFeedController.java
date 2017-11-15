package tudu.web.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;
import tudu.domain.TodoList;
import tudu.service.TodoListsService;

import javax.servlet.http.HttpServletRequest;

/**
 * Generate the RSS feed.
 * 
 * @author Julien Dubois
 */
@Controller
public class ShowRssFeedController {

    /**
     * log : Log :<br/>
     * .<br/>
     */
    private final Log log = LogFactory.getLog(ShowRssFeedController.class);

    
    /**
     * todoListsService : TodoListsService :<br/>
     * .<br/>
     */
    @Autowired
    private TodoListsService todoListsService;

    
    
    
    /**
     * method showRss() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pListId
     * @param pRequest
     * @return ModelAndView
     * @throws Exception :  :  .<br/>
     */
    @RequestMapping("/rss")
    public ModelAndView showRss(@RequestParam String pListId, HttpServletRequest pRequest)
            throws Exception {

        ModelAndView mv = new ModelAndView();
        TodoList todoList = this.todoListsService.unsecuredFindTodoList(pListId);

        if (todoList.isRssAllowed()) {
            mv.addObject("todoList", todoList);
            mv.addObject("link", pRequest.getScheme() + "://"
                    + pRequest.getServerName() + ":" + pRequest.getServerPort()
                    + pRequest.getContextPath() + "/tudu/lists");

            mv.setView(new InternalResourceView("/servlet/rss"));
        } else {
            if (this.log.isDebugEnabled()) {
                this.log.debug("Rendering RSS feed for Todo List ID '"
                        + todoList.getListId() + "' is not allowed");
            }
            mv.setViewName("rss_not_allowed");
        }
        return mv;
    }
    
    
    
}
