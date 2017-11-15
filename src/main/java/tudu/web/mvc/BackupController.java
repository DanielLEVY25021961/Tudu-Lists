package tudu.web.mvc;


import javax.servlet.http.HttpSession;

import org.jdom2.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import tudu.service.TodoListsService;

/**
 * Backup a Todo List.
 * 
 * @author Julien Dubois
 */
@Controller
public class BackupController {

    /**
     * todoListsService : TodoListsService :<br/>
     * .<br/>
     */
    @Autowired
    private TodoListsService todoListsService;

    
    
    /**
     * method backup() :<br/>
     * Backup a Todo List.<br/>
     * <br/>
     *
     * @param pListId
     * @param pSession
     * @return ModelAndView
     * 
     * @throws Exception :  :  .<br/>
     */
    @RequestMapping("/backup")
    public ModelAndView backup(
    		@RequestParam String pListId
    			, HttpSession pSession)
    						throws Exception {

        final Document doc = this.todoListsService.backupTodoList(pListId);
        
        pSession.setAttribute("todoListDocument", doc);
        ModelAndView mv = new ModelAndView();
        mv.setView(new InternalResourceView("/servlet/tudu_lists_backup.xml"));
        return mv;
        
    }
    
    
    
}
