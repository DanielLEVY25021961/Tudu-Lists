package tudu.web.dwr.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;

import tudu.domain.Todo;
import tudu.domain.TodoList;
import tudu.domain.User;
import tudu.domain.comparator.TodoByAssignedUserAscComparator;
import tudu.domain.comparator.TodoByAssignedUserComparator;
import tudu.domain.comparator.TodoByDescriptionAscComparator;
import tudu.domain.comparator.TodoByDescriptionComparator;
import tudu.domain.comparator.TodoByDueDateAscComparator;
import tudu.domain.comparator.TodoByDueDateComparator;
import tudu.domain.comparator.TodoByPriorityAscComparator;
import tudu.service.TodoListsService;
import tudu.service.TodosService;
import tudu.service.UserService;
import tudu.web.dwr.TodosDwr;
import tudu.web.dwr.bean.RemoteTodo;
import tudu.web.dwr.bean.RemoteTodoList;


/**
 * Implementation of the tudu.service.TodosService interface.
 * 
 * @author Julien Dubois
 */
public class TodosDwrImpl implements TodosDwr {

    /**
     * TODO_LIST_SORT_BY : String :<br/>
     * .<br/>
     */
    private static final String TODO_LIST_SORT_BY = "TODO_LIST_SORT_BY";

    /**
     * SORT_BY_PRIORITY : String :<br/>
     * .<br/>
     */
    private static final String SORT_BY_PRIORITY = "priority";

    /**
     * SORT_BY_PRIORITY_ASC : String :<br/>
     * .<br/>
     */
    private static final String SORT_BY_PRIORITY_ASC = "priority_asc";

    /**
     * SORT_BY_DESCRIPTION : String :<br/>
     * .<br/>
     */
    private static final String SORT_BY_DESCRIPTION = "description";

    /**
     * SORT_BY_DESCRIPTION_ASC : String :<br/>
     * .<br/>
     */
    private static final String SORT_BY_DESCRIPTION_ASC = "description_asc";

    /**
     * SORT_BY_DUE_DATE : String :<br/>
     * .<br/>
     */
    private static final String SORT_BY_DUE_DATE = "due_date";

    /**
     * SORT_BY_DUE_DATE_ASC : String :<br/>
     * .<br/>
     */
    private static final String SORT_BY_DUE_DATE_ASC = "due_date_asc";

    /**
     * SORT_BY_ASSIGNED_USER : String :<br/>
     * .<br/>
     */
    private static final String SORT_BY_ASSIGNED_USER = "assigned_user";

    /**
     * SORT_BY_ASSIGNED_USER_ASC : String :<br/>
     * .<br/>
     */
    private static final String SORT_BY_ASSIGNED_USER_ASC = "assigned_user_asc";

    /**
     * log : Log :<br/>
     * .<br/>
     */
    private final Log log = LogFactory.getLog(TodosDwrImpl.class);

    
    /**
     * userService : UserService :<br/>
     * .<br/>
     */
    @Autowired
    private UserService userService;

    
    /**
     * todosService : TodosService :<br/>
     * .<br/>
     */
    @Autowired
    private TodosService todosService;

    
    /**
     * todoListsService : TodoListsService :<br/>
     * .<br/>
     */
    @Autowired
    private TodoListsService todoListsService;

    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public RemoteTodoList[] getCurrentTodoLists(
    		final Date pMenuDate) {
    	
        final User user = this.userService.getCurrentUser();
        Collection<TodoList> todoLists = user.getTodoLists();

        if (pMenuDate != null) {
            boolean aListHasBeenUpdated = false;
            for (TodoList todoList : todoLists) {
                if (todoList.getLastUpdate().after(pMenuDate)) {
                    aListHasBeenUpdated = true;
                }
            }
            if (!aListHasBeenUpdated) {
                return null;
            }
        }

        Collection<RemoteTodoList> remoteTodoLists = new TreeSet<RemoteTodoList>();

        for (TodoList todoList : todoLists) {
            RemoteTodoList remoteTodoList = new RemoteTodoList();
            remoteTodoList.setListId(todoList.getListId());
            remoteTodoList.setName(todoList.getName());
            int completed = 0;
            for (Todo todo : todoList.getTodos()) {
                if (todo.isCompleted()) {
                    completed++;
                }
            }
            remoteTodoList.setDescription(todoList.getName() + " (" + completed
                    + "/" + todoList.getTodos().size() + ")");

            remoteTodoLists.add(remoteTodoList);
        }
        return remoteTodoLists.toArray(new RemoteTodoList[remoteTodoLists
                .size()]);
    }

    
    
    /**
     * @see tudu.web.dwr.TodosDwr#forceGetCurrentTodoLists()
     */
    @Override
    public RemoteTodoList[] forceGetCurrentTodoLists() {
        return this.getCurrentTodoLists(null);
    }

 
    
    /**
     * {@inheritDoc}
     */
    @Override
    public RemoteTodo getTodoById(
    		final String pTodoId) {
    	
        final Todo todo = this.todosService.findTodo(pTodoId);
        RemoteTodo remoteTodo = new RemoteTodo();
        String unescapedDescription = StringEscapeUtils.unescapeHtml(todo
                .getDescription());
        remoteTodo.setDescription(unescapedDescription);
        remoteTodo.setPriority(todo.getPriority());
        if (todo.getDueDate() != null) {
            String formattedDate = getDateFormatter().format(todo.getDueDate());
            remoteTodo.setDueDate(formattedDate);
        } else {
            remoteTodo.setDueDate("");
        }
        remoteTodo.setHasNotes(todo.isHasNotes());
        if (remoteTodo.isHasNotes()) {
            remoteTodo.setNotes(todo.getNotes());
        }
        if (todo.getAssignedUser() != null) {
            remoteTodo.setAssignedUserLogin(todo.getAssignedUser().getLogin());
        }
        return remoteTodo;
    }

    
 
    /**
     * {@inheritDoc}
     */
    @Override
    public String renderTodos(
    		final String pListId
    			, final Date pTableDate) {
    	
        HttpServletRequest request = WebContextFactory.get()
                .getHttpServletRequest();

        if (pListId != null && !pListId.equals("")) {
            TodoList todoList = this.todoListsService.findTodoList(pListId);
            if (pTableDate != null && todoList.getLastUpdate().before(pTableDate)) {
                return "";
            }

            request.setAttribute("todoList", todoList);
            Set<Todo> todos = todoList.getTodos();
            String sorter = (String) request.getSession().getAttribute(
                    TODO_LIST_SORT_BY);

            Set<Todo> sortedTodos;
            String descriptionClass = "sortable";
            String priorityClass = "sortable";
            String dueDateClass = "sortable";
            String assignedUserClass = "sortable";
            if (sorter != null) {
                if (sorter.equals(SORT_BY_DESCRIPTION)) {
                    descriptionClass = "sorted";
                    sortedTodos = new TreeSet<Todo>(
                            new TodoByDescriptionComparator());
                } else if (sorter.equals(SORT_BY_DESCRIPTION_ASC)) {
                    descriptionClass = "sorted_asc";
                    sortedTodos = new TreeSet<Todo>(
                            new TodoByDescriptionAscComparator());
                } else if (sorter.equals(SORT_BY_DUE_DATE)) {
                    dueDateClass = "sorted";
                    sortedTodos = new TreeSet<Todo>(
                            new TodoByDueDateComparator());
                } else if (sorter.equals(SORT_BY_DUE_DATE_ASC)) {
                    dueDateClass = "sorted_asc";
                    sortedTodos = new TreeSet<Todo>(
                            new TodoByDueDateAscComparator());
                } else if (sorter.equals(SORT_BY_ASSIGNED_USER)) {
                    assignedUserClass = "sorted";
                    sortedTodos = new TreeSet<Todo>(
                            new TodoByAssignedUserComparator());
                } else if (sorter.equals(SORT_BY_ASSIGNED_USER_ASC)) {
                    assignedUserClass = "sorted_asc";
                    sortedTodos = new TreeSet<Todo>(
                            new TodoByAssignedUserAscComparator());
                } else if (sorter.equals(SORT_BY_PRIORITY_ASC)) {
                    priorityClass = "sorted_asc";
                    sortedTodos = new TreeSet<Todo>(
                            new TodoByPriorityAscComparator());
                } else {
                    priorityClass = "sorted";
                    sortedTodos = new TreeSet<Todo>();
                }
            } else {
                priorityClass = "sorted";
                sortedTodos = new TreeSet<Todo>();
            }

            String hideOlderTodos = (String) request.getSession().getAttribute(
                    "hideOlderTodos");

            if (hideOlderTodos == null) {
                hideOlderTodos = "true";
                request.getSession().setAttribute("hideOlderTodos",
                        hideOlderTodos);
            }
            if (hideOlderTodos.equals("true")) {
                Calendar oneDayAgo = Calendar.getInstance();
                oneDayAgo.set(Calendar.DATE, oneDayAgo.get(Calendar.DATE) - 1);
                oneDayAgo.set(Calendar.HOUR, 0);
                oneDayAgo.set(Calendar.MINUTE, 0);
                oneDayAgo.set(Calendar.SECOND, 0);
                int hiddenTodos = 0;
                for (Todo todo : todos) {
                    if (todo.getCompletionDate() == null
                            || todo.getCompletionDate().after(
                                    oneDayAgo.getTime())) {

                        sortedTodos.add(todo);
                    } else {
                        hiddenTodos++;
                    }
                }
                request.setAttribute("hiddenTodos", hiddenTodos);
            } else {
                sortedTodos.addAll(todos);
            }
            request.setAttribute("todos", sortedTodos);
            request.setAttribute("descriptionClass", descriptionClass);
            request.setAttribute("priorityClass", priorityClass);
            request.setAttribute("dueDateClass", dueDateClass);
            request.setAttribute("assignedUserClass", assignedUserClass);

            int nbCompleted = 0;
            for (Todo todo : todos) {
                if (todo.isCompleted()) {
                    nbCompleted++;
                }
            }
            if (todos.size() != 0) {
                request.setAttribute("completion", nbCompleted * 100
                        / todos.size());
            } else {
                request.setAttribute("completion", 100);
            }
        } else {
            return "";
        }
        try {
            return WebContextFactory.get().forwardToString(
                    "/WEB-INF/fragments/todos_table.jsp");
        } catch (ServletException e) {
            this.log.error("ServletException : " + e);
            return "";
        } catch (IOException ioe) {
            this.log.error("IOException : " + ioe);
            return "";
        }
    }


    
    /**
     * {@inheritDoc}
     */
    @Override
    public String renderNextDays() {
        HttpServletRequest request = WebContextFactory.get()
                .getHttpServletRequest();

        request.setAttribute("filter", "nextDays");
        request.setAttribute("todos", this.todosService.findUrgentTodos());
        return renderFilter();
    }

 
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String renderAssignedToMe() {
    	
        HttpServletRequest request = WebContextFactory.get()
                .getHttpServletRequest();

        request.setAttribute("filter", "assignedToMe");
        request.setAttribute("todos", this.todosService.findAssignedTodos());
        return renderFilter();
    }

    
    
    /**
     * method renderFilter() :<br/>
     * Render the filtered data.<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    private String renderFilter() {
        try {
            return WebContextFactory.get().forwardToString(
                    "/WEB-INF/fragments/todos_table_filter.jsp");
        } catch (ServletException e) {
            this.log.error("ServletException : " + e);
            return "";
        } catch (IOException ioe) {
            this.log.error("IOException : " + ioe);
            return "";
        }
    }

 
        
    /**
     * {@inheritDoc}
     */
    @Override
	public String forceRenderTodos(
			final String pListId) {
        return this.renderTodos(pListId, null);
    }

    
    
    /**
     * Sort the List according to the "sorter" passed as parameter.
     * <p>
     * If the provided "sorter" is equals to the current list "sorter", then the
     * user must have clicked again on the sort button : in that case he wants
     * to sort the list the other way around (ascending).
     * </p>
     * 
     * @see tudu.web.dwr.TodosDwr#sortAndRenderTodos(java.lang.String,
     *      java.lang.String)
     */    
    @Override
	public String sortAndRenderTodos(
			final String pListId
				, String pSorter) {
    	
        HttpSession session = WebContextFactory.get()
                .getHttpServletRequest().getSession();

        String currentSorter = (String) session.getAttribute(TODO_LIST_SORT_BY);
        if (currentSorter != null && currentSorter.equals(pSorter)
                && !currentSorter.endsWith("_asc")) {

            pSorter += "_asc";
        } else if (currentSorter == null && pSorter.equals(SORT_BY_PRIORITY)) {
            pSorter += "_asc";
        }
        session.setAttribute(TODO_LIST_SORT_BY, pSorter);
        return this.forceRenderTodos(pListId);
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
	public String addTodo(
			final String pListId, final String pDescription
			, final String pPriority
			, final String pDueDate, final String pNotes
			, final String pAssignedUserLogin) {

        Todo todo = new Todo();
        String escapedDescription = StringEscapeUtils.escapeHtml(pDescription);
        todo.setDescription(escapedDescription);

        int priorityInt = 0;
        try {
            priorityInt = Integer.valueOf(pPriority);
        } catch (NumberFormatException e) {
            // The priority is not a number.
        }
        todo.setPriority(priorityInt);

        try {
            Date due = getDateFormatter().parse(pDueDate);
            todo.setDueDate(due);
        } catch (ParseException e) {
            // The date is not correct
        }

        inputNotes(todo, pNotes);
        inputAssignedUser(todo, pAssignedUserLogin);

        this.todosService.createTodo(pListId, todo);
        return forceRenderTodos(pListId);
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
	public String reopenTodo(
			final String pTodoId) {
        Todo todo = this.todosService.reopenTodo(pTodoId);
        return forceRenderTodos(todo.getTodoList().getListId());
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
	public String completeTodo(
			final String pTodoId) {
        Todo todo = this.todosService.completeTodo(pTodoId);
        return forceRenderTodos(todo.getTodoList().getListId());
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
	public String editTodo(
			final String pTodoId, final String pDescription
			, final String pPriority, final String pDueDate
			, final String pNotes
			, final String pAssignedUserLogin) {

        Todo todo = this.todosService.findTodo(pTodoId);
        String escapedDescription = StringEscapeUtils.escapeHtml(pDescription);
        todo.setDescription(escapedDescription);

        int priorityInt = 0;
        try {
            priorityInt = Integer.parseInt(pPriority);
        } catch (NumberFormatException e) {
            // The priority is not a number.
        }
        todo.setPriority(priorityInt);

        if (pDueDate == null || pDueDate.equals("")) {
            todo.setDueDate(null);
        } else {
            try {
                Date due = getDateFormatter().parse(pDueDate);
                todo.setDueDate(due);
            } catch (ParseException e) {
                // The date is not correct
            }
        }

        inputNotes(todo, pNotes);
        inputAssignedUser(todo, pAssignedUserLogin);

        this.todosService.updateTodo(todo);
        return forceRenderTodos(todo.getTodoList().getListId());
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
	public String quickEditTodo(
    		final String pTodoId, final String pDescription) {
    	
        final Todo todo = this.todosService.findTodo(pTodoId);
        
        String escapedDescription = StringEscapeUtils.escapeHtml(pDescription);
        todo.setDescription(escapedDescription);
        this.todosService.updateTodo(todo);
        return forceRenderTodos(todo.getTodoList().getListId());
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
	public String deleteTodo(
			final String pTodoId) {
    	
        final Todo todo = this.todosService.findTodo(pTodoId);
        
        String listId = todo.getTodoList().getListId();
        this.todosService.deleteTodo(todo);
        return forceRenderTodos(listId);
    }

    
 
    /**
     * {@inheritDoc}
     */
    @Override
	public String deleteAllCompletedTodos(
    		final String pListId) {
    	
        this.todosService.deleteAllCompletedTodos(pListId);
        return forceRenderTodos(pListId);
        
    }

    
 
    /**
     * {@inheritDoc}
     */
    @Override
	public String showOlderTodos(
    		final String pListId) {
        HttpSession session = WebContextFactory.get()
                .getHttpServletRequest().getSession();

        session.setAttribute("hideOlderTodos", "false");
        return forceRenderTodos(pListId);
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
	public String hideOlderTodos(
			final String pListId) {
    	
       final  HttpSession session = WebContextFactory.get()
                .getHttpServletRequest().getSession();

        session.setAttribute("hideOlderTodos", "true");
        return forceRenderTodos(pListId);
        
    }

    
    
    /**
     * method getDateFormatter() :<br/>
     * Find a Date Formatter.<br/>
     * <br/>
     *
     * @return : SimpleDateFormat : the Date formatter.<br/>
     */
    protected SimpleDateFormat getDateFormatter() {
        String dateFormat = null; //(String) WebContextFactory.get().getSession().getAttribute("dateFormat");

        dateFormat = this.userService.getCurrentUser().getDateFormat();
        //WebContextFactory.get().getSession().setAttribute("dateFormat", dateFormat);
       
        return new SimpleDateFormat(dateFormat);
    }

    
    
    /**
     * Insert notes into the todo.
     * 
     * @param pTodo
     *            The Todo
     * @param pNotes
     *            The notes
     */
    protected void inputNotes(
    		final Todo pTodo
    			, final String pNotes) {
    	
        if (pNotes != null && !pNotes.equals("")) {
            pTodo.setHasNotes(true);
            if (pNotes.length() > 10000) {
                pTodo.setNotes(pNotes.substring(0, 10000));
            } else {
                pTodo.setNotes(pNotes);
            }
        } else {
            pTodo.setHasNotes(false);
            pTodo.setNotes(null);
        }
    }

    
    
    /**
     * method inputAssignedUser() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pTodo : Todo : The Todo
     * @param pAssignedUserLogin : String : The login of the assigned user.<br/>
     */
    private void inputAssignedUser(
    		final Todo pTodo
    			, final String pAssignedUserLogin) {
    	
        if (pAssignedUserLogin != null && !pAssignedUserLogin.equals("")) {
            try {
                User assignedUser = this.userService.findUser(pAssignedUserLogin);
                pTodo.setAssignedUser(assignedUser);
            } catch (ObjectRetrievalFailureException orfe) {
                pTodo.setAssignedUser(null);
            }
        } else {
            pTodo.setAssignedUser(null);
        }
    }
    
    
    
}
