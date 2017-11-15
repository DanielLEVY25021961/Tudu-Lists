package tudu.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tudu.domain.Todo;
import tudu.domain.TodoList;
import tudu.domain.User;
import tudu.security.PermissionDeniedException;
import tudu.service.TodoListsService;
import tudu.service.UserService;

/**
 * Implementation of the tudu.service.TodoListsService interface.
 *
 * @author Julien Dubois
 */
@Service
@Transactional
public class TodoListsServiceImpl implements TodoListsService {

    /**
     * log : Log :<br/>
     * .<br/>
     */
    private final Log log = LogFactory.getLog(TodoListsServiceImpl.class);
    
    

    /**
     * entityManager : EntityManager :<br/>
     * .<br/>
     */
    @PersistenceContext
    private EntityManager entityManager;

    
    
    /**
     * userService : UserService :<br/>
     * .<br/>
     */
    @Autowired
    private UserService userService;

    
    
    /**
     * Create a new Todo List.
     *
     * @see tudu.service.TodoListsService#createTodoList(tudu.domain.TodoList)
     */
    /**
     * {@inheritDoc}
     */
    @Override
	public void createTodoList(final TodoList todoList) {
    	
        if (this.log.isDebugEnabled()) {
            this.log.debug("Creating a new Todo List with name "
                    + todoList.getName());
        }
        todoList.setLastUpdate(Calendar.getInstance().getTime());
        User user = this.userService.getCurrentUser();
        todoList.getUsers().add(user);
        this.entityManager.persist(todoList);
        user.getTodoLists().add(todoList);
    }


    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public TodoList findTodoList(
    		final String listId) {
    	
        TodoList todoList = this.entityManager.find(TodoList.class, listId);
        
        if (todoList == null) {
            throw new ObjectRetrievalFailureException(TodoList.class, listId);
        }
        User user = this.userService.getCurrentUser();
        if (!user.getTodoLists().contains(todoList)) {
            if (this.log.isInfoEnabled()) {
                this.log.info("Permission denied when finding Todo List ID '"
                        + listId + "' for User '" + user.getLogin() + "'");
            }
            throw new PermissionDeniedException(
                    "Permission denied to access this Todo List.");
        }
        return todoList;
    }


    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public TodoList unsecuredFindTodoList(
    		final String listId) {
        TodoList todoList = this.entityManager.find(TodoList.class, listId);
        Hibernate.initialize(todoList.getTodos());
        return todoList;
    }

    
    
    /**
     * Updates the Todo List last update date.
     *
     * @see tudu.service.TodoListsService#updateTodoList(tudu.domain.TodoList)
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTodoList(
    		final TodoList todoList) {
        todoList.setLastUpdate(Calendar.getInstance().getTime());
    }

    
    
    /**
     * Delete a Todo List.
     *
     * @see tudu.service.TodoListsService#deleteTodoList(java.lang.String)
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTodoList(
    		final String listId) {
        TodoList todoList = this.findTodoList(listId);
        for (User user : todoList.getUsers()) {
            user.getTodoLists().remove(todoList);
        }
        for (Todo todo : todoList.getTodos()) {
            this.entityManager.remove(todo);
        }
        this.entityManager.remove(todoList);
    }

    
    
    /**
     * @see tudu.service.TodoListsService#addTodoListUser(java.lang.String,
     *      java.lang.String)
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void addTodoListUser(
    		final String listId, final String login) {
        TodoList todoList = this.findTodoList(listId);
        User targetUser = this.userService.findUser(login);
        todoList.getUsers().add(targetUser);
        targetUser.getTodoLists().add(todoList);
        this.updateTodoList(todoList);
    }

    
    
    /**
     * @see tudu.service.TodoListsService#deleteTodoListUser(java.lang.String,
     *      java.lang.String)
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTodoListUser(
    		final String listId, final String login) {
        TodoList todoList = this.findTodoList(listId);
        User targetUser = this.userService.findUser(login);
        for (Todo todo : todoList.getTodos()) {
            if (todo.getAssignedUser() != null
                    && todo.getAssignedUser().equals(targetUser)) {

                todo.setAssignedUser(null);
            }
        }
        todoList.getUsers().remove(targetUser);
        targetUser.getTodoLists().remove(todoList);
        this.updateTodoList(todoList);
    }


    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Document backupTodoList(
    		final String listId) {
    	
        TodoList todoList = findTodoList(listId);
        Document doc = new Document();

        Element todoListElement = new Element("todolist");
        todoListElement.addContent(new Element("title").addContent(todoList
                .getName()));

        todoListElement.addContent(new Element("rss").addContent(String
                .valueOf(todoList.isRssAllowed())));

        Element todosElement = new Element("todos");
        for (Todo todo : todoList.getTodos()) {
            Element todoElement = new Element("todo");
            todoElement.setAttribute("id", todo.getTodoId());
            todoElement.addContent(new Element("creationDate").addContent(Long
                    .toString(todo.getCreationDate().getTime())));

            todoElement.addContent(new Element("description").addContent(todo
                    .getDescription()));

            todoElement.addContent(new Element("priority").addContent(Integer
                    .toString(todo.getPriority())));

            if (todo.getDueDate() != null) {
                todoElement.addContent(new Element("dueDate").addContent(Long
                        .toString(todo.getDueDate().getTime())));
            }
            todoElement.addContent(new Element("completed").addContent(Boolean
                    .toString(todo.isCompleted())));

            if (todo.isCompleted() && todo.getCompletionDate() != null) {
                todoElement.addContent(new Element("completionDate")
                        .addContent(Long.toString(todo.getCompletionDate()
                        .getTime())));
            }

            todoElement.addContent(new Element("notes").addContent(todo
                    .getNotes()));

            todosElement.addContent(todoElement);
        }
        todoListElement.addContent(todosElement);

        doc.addContent(todoListElement);

        return doc;
    }

    
    
    /**
     * @see tudu.service.TodoListsService#restoreTodoList(java.lang.String,
     *      java.lang.String, java.io.InputStream)
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void restoreTodoList(
    		final String restoreChoice, final String listId
    		, final InputStream todoListContent) throws JDOMException, IOException {

        SAXBuilder saxBuilder = new SAXBuilder();
        Document doc = saxBuilder.build(todoListContent);
        Element rootElement = doc.getRootElement();
        String title = rootElement.getChildText("title");
        String rss = rootElement.getChildText("rss");
        if (restoreChoice.equals("create")) {
            TodoList todoList = new TodoList();
            todoList.setName(title);
            todoList.setRssAllowed(Boolean.parseBoolean(rss));
            this.createTodoList(todoList);
            importTodosFromXml(todoList, rootElement);
        } else if (restoreChoice.equals("replace")) {
            TodoList todoList = this.findTodoList(listId);
            for (Todo todo : todoList.getTodos()) {
                this.entityManager.remove(todo);
            }
            todoList.getTodos().clear();
            todoList.setName(title);
            todoList.setRssAllowed(Boolean.parseBoolean(rss));
            importTodosFromXml(todoList, rootElement);
            this.updateTodoList(todoList);
        } else if (restoreChoice.equals("merge")) {
            TodoList todoList = this.findTodoList(listId);
            importTodosFromXml(todoList, rootElement);
            this.updateTodoList(todoList);
        } else {
            this.log.error("Wrong choice of RestoreController option");
        }
    }

    /**
     * Import Todos from a JDOM document.
     *
     * @param todoList    The current Todo List
     * @param rootElement The root element of the JDOM document.
     */
    private void importTodosFromXml(
    		final TodoList todoList
    			, final Element rootElement) {
    	
        final Element todosElement = rootElement.getChild("todos");
        
        List<Element> todos = todosElement.getChildren();
        
        for (Object todoObject : todos) {
            Element todoElement = (Element) todoObject;
            Todo todo = new Todo();
            Date creationDate = new Date(Long.parseLong(todoElement
                    .getChildText("creationDate")));
            todo.setCreationDate(creationDate);
            todo.setDescription(todoElement.getChildText("description"));
            todo.setPriority(Integer.valueOf(todoElement
                    .getChildText("priority")));
            todo.setCompleted(Boolean.parseBoolean(todoElement
                    .getChildText("completed")));
            String completionDate = todoElement.getChildText("completionDate");
            if (completionDate != null) {
                todo
                        .setCompletionDate(new Date(Long
                                .parseLong(completionDate)));
            }
            String dueDate = todoElement.getChildText("dueDate");
            if (dueDate != null) {
                todo.setDueDate(new Date(Long.parseLong(dueDate)));
            }
            String notes = todoElement.getChildText("notes");
            if (notes == null || notes.length() == 0) {
                todo.setHasNotes(false);
            } else {
                todo.setNotes(notes);
                todo.setHasNotes(true);
            }
            todo.setTodoList(todoList);
            todoList.getTodos().add(todo);
            this.entityManager.persist(todo);
        }
        
    }
    
}
