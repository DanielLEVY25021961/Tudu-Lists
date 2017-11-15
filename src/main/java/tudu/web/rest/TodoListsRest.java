package tudu.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tudu.domain.Todo;
import tudu.domain.TodoList;
import tudu.domain.User;
import tudu.service.TodoListsService;
import tudu.service.UserService;

import java.util.Collection;


/**
 * REST API for accessing Todo Lists.
 */
@Controller
public class TodoListsRest {

    /**
     * userService : UserService :<br/>
     * .<br/>
     */
    @Autowired
    private UserService userService;

    /**
     * todoListsService : TodoListsService :<br/>
     * .<br/>
     */
    @Autowired
    private TodoListsService todoListsService;

    
    
    /**
     * method lists() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    @RequestMapping(value = "/rest/lists.json", method = RequestMethod.GET)
    public Collection<TodoList> lists() {
        User user = this.userService.getCurrentUser();
        return user.getTodoLists();
    }

    
    
    /**
     * method todos() :<br/>
     * .<br/>
     * <br/>
     *
     * @param listId
     * @return :  :  .<br/>
     */
    @RequestMapping(value = "/rest/lists/{listId}/todos.json", method = RequestMethod.GET)
    public Collection<Todo> todos(@PathVariable("listId") String listId) {
        TodoList todoList = this.todoListsService.findTodoList(listId);
        return todoList.getTodos();
    }

    
    
}
