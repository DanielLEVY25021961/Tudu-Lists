package tudu.web.mvc;

import java.io.IOException;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.support.StringMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import tudu.domain.TodoList;
import tudu.service.TodoListsService;



/**
 * class RestoreController :<br/>
 * .<br/>
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
 * @author dan Lévy
 * @version 1.0
 * @since 14 nov. 2017
 *
 */
@Controller
@RequestMapping("/restore")
public class RestoreController {

    /**
     * log : Log :<br/>
     * .<br/>
     */
    private final Log log = LogFactory.getLog(RestoreController.class);

    
    
    /**
     * todoListsService : TodoListsService :<br/>
     * .<br/>
     */
    @Autowired
    private TodoListsService todoListsService;

  
    
    /**
     * method initBinder() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pBinder :  :  .<br/>
     */
    @InitBinder
	public void initBinder(
			final WebDataBinder pBinder) {
        pBinder.registerCustomEditor(String.class, new StringMultipartFileEditor());
	}


    
    /**
     * method display() :<br/>
     * Display the main screen for restoring a Todo List..<br/>
     * <br/>
     *
     * @param pListId
     * @return : ModelAndView :  .<br/>
     */
    @RequestMapping(value="/{pListId}", method = RequestMethod.GET)
    public ModelAndView display(
    		@PathVariable final String pListId) {
        ModelAndView mv = new ModelAndView();
        TodoList todoList = this.todoListsService.findTodoList(pListId);
        mv.addObject("todoList", todoList);
        RestoreTodoListModel restoreTodoListModel = new RestoreTodoListModel();
        restoreTodoListModel.setRestoreChoice("create");
        restoreTodoListModel.setListId(pListId);
        mv.addObject("restoreTodoListModel", restoreTodoListModel);
        mv.setViewName("restore");
        return mv;
    }


    
    /**
     * method restore() :<br/>
     * Restore a Todo List.<br/>
     * <br/>
     *
     * @param pRestoreTodoListModel
     * @param pResult
     * @return :  :  .<br/>
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView restore(
    		@Valid final RestoreTodoListModel pRestoreTodoListModel
    			, BindingResult pResult) {
    	
        this.log.debug("Execute RestoreController");
        ModelAndView mv = new ModelAndView();
        if (pRestoreTodoListModel.getListId() == null) {
            mv.setViewName("redirect:/tudu/lists");
            return mv;
        }
        TodoList todoList = this.todoListsService.findTodoList(pRestoreTodoListModel.getListId());
        mv.addObject("todoList", todoList);
        mv.addObject("restoreTodoListModel", pRestoreTodoListModel);
        if (pResult.hasErrors()) {
            mv.setViewName("restore");
            return mv;
        }
        if (pRestoreTodoListModel.getBackupFile().isEmpty()) {
            pResult.rejectValue("backupFile", "restore.file.empty", "Empty file.");
            mv.setViewName("restore");
            return mv;
        }
        try {
            this.todoListsService.restoreTodoList(pRestoreTodoListModel.getRestoreChoice(),
                    pRestoreTodoListModel.getListId(),
                    pRestoreTodoListModel.getBackupFile().getInputStream());

            mv.setViewName("redirect:/tudu/lists");
        } catch (JDOMException e) {
            this.log.info("JDOMException : " + e.getMessage());
            mv.setViewName("restore");
        } catch (IOException e) {
            this.log.info("FileNotFoundException : " + e.getMessage());
            mv.setViewName("restore");
        }
        return mv;
    }

    
}
