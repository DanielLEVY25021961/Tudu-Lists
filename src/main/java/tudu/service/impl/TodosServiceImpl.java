package tudu.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tudu.domain.Todo;
import tudu.domain.TodoList;
import tudu.domain.User;
import tudu.domain.comparator.TodoByDueDateComparator;
import tudu.security.PermissionDeniedException;
import tudu.service.TodoListsService;
import tudu.service.TodosService;
import tudu.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Implementation of the tudu.service.TodosService interface.
 *
 * @author Julien Dubois
 */
@Service
@Transactional
public class TodosServiceImpl implements TodosService {


	/**
     * log : Log :<br/>
     * .<br/>
     */
    private final Log log = LogFactory.getLog(TodosServiceImpl.class);

    
    /**
     * entityManager : EntityManager :<br/>
     * .<br/>
     */
    @PersistenceContext
    private EntityManager entityManager;

    
    
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
    @Transactional(readOnly = true)
    @Override
    public Todo findTodo(final String todoId) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Finding Todo with ID " + todoId);
        }
        Todo todo = this.entityManager.find(Todo.class, todoId);
        if (todo == null) {
            if (this.log.isInfoEnabled()) {
                this.log.info("Todo ID '" + todoId
                        + "' does not exist!");
            }
            return null;
        }
        TodoList todoList = todo.getTodoList();
        User user = this.userService.getCurrentUser();
        if (!user.getTodoLists().contains(todoList)) {
            if (this.log.isInfoEnabled()) {
                this.log.info("Permission denied when finding Todo ID '" + todoId
                        + "' for User '" + user.getLogin() + "'");
            }

            throw new PermissionDeniedException(
                    "Permission denied to access this Todo.");

        }
        return todo;
    }


    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Collection<Todo> findUrgentTodos() {
        User user = this.userService.getCurrentUser();
        Calendar urgentCal = Calendar.getInstance();
        urgentCal.add(Calendar.DATE, 4);
        Date urgentDate = urgentCal.getTime();
        Set<Todo> urgentTodos = new TreeSet<Todo>(new TodoByDueDateComparator());
        for (TodoList todoList : user.getTodoLists()) {
            for (Todo todo : todoList.getTodos()) {
                if (todo.getDueDate() != null
                        && todo.getDueDate().before(urgentDate)
                        && !todo.isCompleted()) {

                    urgentTodos.add(todo);
                }
            }
        }
        return urgentTodos;
    }

 
    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Collection<Todo> findAssignedTodos() {
        User user = this.userService.getCurrentUser();
        Set<Todo> assignedTodos = new TreeSet<Todo>();
        for (TodoList todoList : user.getTodoLists()) {
            for (Todo todo : todoList.getTodos()) {
                if (todo.getAssignedUser() != null
                        && todo.getAssignedUser().equals(user)
                        && !todo.isCompleted()) {

                    assignedTodos.add(todo);
                }
            }
        }
        return assignedTodos;
    }

    

    /**
     * {@inheritDoc}
     */
    @Override
    public void createTodo(final String listId, final Todo todo) {
        Date now = Calendar.getInstance().getTime();
        todo.setCreationDate(now);
        TodoList todoList = this.todoListsService.findTodoList(listId);
        todo.setTodoList(todoList);
        this.entityManager.persist(todo);
        todoList.getTodos().add(todo);
        this.todoListsService.updateTodoList(todoList);
        if (this.log.isDebugEnabled()) {
            this.log.debug("Created Todo ID=" + todo.getTodoId());
        }
    }

 
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTodo(final Todo todo) {
        TodoList todoList = todo.getTodoList();
        Set<Todo> todos = todoList.getTodos();
        if (todos.contains(todo)) {
            todos.remove(todo);
            this.todoListsService.updateTodoList(todoList);
            if (this.log.isDebugEnabled()) {
                this.log.debug("Removed Todo ID=" + todo.getTodoId() + " - list size="
                        + todoList.getTodos().size());
            }
        } else {
            this.log.warn("Todo " + todo.getTodoId() + " should have been in List "
                    + todoList.getListId());

        }
        this.entityManager.remove(todo);
    }

 
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllCompletedTodos(
    		final String listId) {
        TodoList todoList = this.todoListsService.findTodoList(listId);
        List<Todo> todosToRemove = new ArrayList<Todo>();
        for (Todo todo : todoList.getTodos()) {
            if (todo.isCompleted()) {
                todosToRemove.add(todo);
            }
        }
        todoList.getTodos().removeAll(todosToRemove);
        for (Todo todo : todosToRemove) {
            this.entityManager.remove(todo);
        }
        this.todoListsService.updateTodoList(todoList);
    }


    
    /**
     * {@inheritDoc}
     */
    @Override
    public Todo completeTodo(
    		final String todoId) {
        Todo todo = this.findTodo(todoId);
        todo.setCompleted(true);
        todo.setCompletionDate(Calendar.getInstance().getTime());
        this.todoListsService.updateTodoList(todo.getTodoList());
        return todo;
    }


    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Todo reopenTodo(
    		final String todoId) {
        Todo todo = this.findTodo(todoId);
        todo.setCompleted(false);
        todo.setCompletionDate(null);
        this.todoListsService.updateTodoList(todo.getTodoList());
        return todo;
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTodo(
    		final Todo todo) {
        this.todoListsService.updateTodoList(todo.getTodoList());
        
        
    }
}
