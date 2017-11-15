package tudu.web.dwr.impl;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import tudu.domain.TodoList;
import tudu.domain.User;
import tudu.service.TodoListsService;
import tudu.service.UserService;
import tudu.web.dwr.TodoListsDwr;
import tudu.web.dwr.bean.RemoteTodoList;

import java.util.Collection;
import java.util.TreeSet;

/**
 * Implementation of the tudu.service.TodoListsService interface.
 * 
 * @author Julien Dubois
 */
public class TodoListsDwrImpl implements TodoListsDwr {

    /**
     * log : Log :<br/>
     * .<br/>
     */
    private final Log log = LogFactory.getLog(TodoListsDwrImpl.class);

    
    /**
     * todoListsService : TodoListsService :<br/>
     * .<br/>
     */
    @Autowired
    private TodoListsService todoListsService;

    
    /**
     * userService : UserService :<br/>
     * .<br/>
     */
    @Autowired
    private UserService userService;

 

    /**
     * {@inheritDoc}
     */
    @Override
	public RemoteTodoList getTodoList(
			final String pListId) {
    	
        final RemoteTodoList remoteTodoList = new RemoteTodoList();
        
        try {
            TodoList todoList = this.todoListsService.findTodoList(pListId);
            remoteTodoList.setListId(todoList.getListId());
            String unescapedName = StringEscapeUtils.unescapeHtml(todoList
                    .getName());
            remoteTodoList.setName(unescapedName);
            remoteTodoList.setRssAllowed(todoList.isRssAllowed());
        } catch (Throwable t) {
            remoteTodoList.setListId(null);
        }
        return remoteTodoList;
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
	public String[] getTodoListUsers(
    		final String pListId) {
    	
        try {
            TodoList todoList = this.todoListsService.findTodoList(pListId);
            String currentLogin = this.userService.getCurrentUser().getLogin();
            Collection<User> users = todoList.getUsers();
            Collection<String> logins = new TreeSet<String>();
            for (User user : users) {
                if (!currentLogin.equals(user.getLogin())) {
                    logins.add(user.getLogin());
                }
            }
            return logins.toArray(new String[logins.size()]);
        } catch (Throwable t) {
            return new String[0];
        }
    }


    
    /**
     * {@inheritDoc}
     */
    @Override
	public String addTodoListUser(
			final String pListId
				, String pLogin) {
    	
        pLogin = pLogin.toLowerCase();
        
        try {
        	this.todoListsService.addTodoListUser(pListId, pLogin);
        } catch (ObjectRetrievalFailureException orfe) {
            return "ObjectRetrievalFailureException";
        }
        return "";
    }

    
   
    /**
     * {@inheritDoc}
     */
    @Override
	public void deleteTodoListUser(
    		final String pListId
    			, String pLogin) {
    	
        pLogin = pLogin.toLowerCase();
        this.todoListsService.deleteTodoListUser(pListId, pLogin);
    }


    
    /**
     * {@inheritDoc}
     */
    @Override
	public void addTodoList(
			final String pName
				, final String pRssAllowed) {
    	
        boolean rssAllowedBool = false;
        if (pRssAllowed != null && pRssAllowed.equals("1")) {
            rssAllowedBool = true;
        }
        TodoList todoList = new TodoList();
        String escapedName = StringEscapeUtils.escapeHtml(pName);
        todoList.setName(escapedName);
        todoList.setRssAllowed(rssAllowedBool);
        this.todoListsService.createTodoList(todoList);
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
	public void editTodoList(
			final String pListId
				, final String pName
					, final String pRssAllowed) {
    	
        final TodoList todoList = this.todoListsService.findTodoList(pListId);
        
        if (pName != null && !pName.equals("")) {
            String escapedName = StringEscapeUtils.escapeHtml(pName);
            todoList.setName(escapedName);
        }
        if (pRssAllowed != null && !pRssAllowed.equals("")) {
            boolean rssAllowedBool = false;
            if (pRssAllowed.equals("1")) {
                rssAllowedBool = true;
            }
            todoList.setRssAllowed(rssAllowedBool);
        }
        this.todoListsService.updateTodoList(todoList);
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
	public void deleteTodoList(
			final String pListId) {
    	this.todoListsService.deleteTodoList(pListId);
    }
    
    
    
}
