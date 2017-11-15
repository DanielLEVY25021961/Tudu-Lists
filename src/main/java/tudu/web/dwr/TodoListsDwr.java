package tudu.web.dwr;

import tudu.web.dwr.bean.RemoteTodoList;

/**
 * Todo Lists actions presented with DWR.
 * 
 * @author Julien Dubois
 */
public interface TodoListsDwr {

	
    /**
     * Get a Todo List by ID.
     * 
     * @param listId
     *            The ID of the Todo List
     * @return RemoteTodoList
     */
    RemoteTodoList getTodoList(String listId);

 
    
    /**
     * Get an array containing the logins of the users currently sharing the
     * Todo List.
     * 
     * @param listId
     *            The ID of the Todo List
     * @return String[]
     */
    String[] getTodoListUsers(String listId);

    
    
    /**
     * Add a User to a Todo List
     * 
     * @param listId
     *            The ID of the Todo List
     * @param login
     *            The user login
     * @return String
     */
    String addTodoListUser(String listId, String login);

    
    
    /**
     * Delete a User from a Todo List
     * 
     * @param listId
     *            The ID of the Todo List
     * @param login
     *            The user login
     */
    void deleteTodoListUser(String listId, String login);

    /**
     * Add a new Todo List.
     * 
     * @param name
     *            The list name
     * @param rssAllowed
     *            If the RSS feed is enabled for this list
     */
    void addTodoList(String name, String rssAllowed);

    /**
     * Edit a Todo List.
     * 
     * @param listId
     *            The ID of the Todo List
     * @param name
     *            The list name
     * @param rssAllowed
     *            If the RSS feed is enabled for this list
     */
    void editTodoList(String listId, String name, String rssAllowed);

    /**
     * Delete a Todo List.
     * 
     * @param listId
     *            The ID of the Todo List
     */
    void deleteTodoList(String listId);
}
