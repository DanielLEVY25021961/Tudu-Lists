package tudu.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import tudu.domain.*;
import tudu.service.TodoListsService;
import tudu.service.TodosService;
import tudu.service.UserAlreadyExistsException;
import tudu.service.UserService;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/META-INF/spring/application-context-test.xml"})
public class IntegrationTest {

	
    /**
     * log : Log :<br/>
     * .<br/>
     */
    private final Log log = LogFactory.getLog(IntegrationTest.class);

    
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
     * todosService : TodosService :<br/>
     * .<br/>
     */
    @Autowired
    private TodosService todosService;

    
    /**
     * userDetailsService : UserDetailsService :<br/>
     * .<br/>
     */
    @Autowired
    private UserDetailsService userDetailsService;

    
    /**
     * authenticationManager : AuthenticationManager :<br/>
     * .<br/>
     */
    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    
    
    
    /**
     * method createUser() :<br/>
     * .<br/>
     * <br/>
     * :  :  .<br/>
     */
    @Test
    @Transactional
    public void createUser() {
        try {
        	this.userService.findUser("test_user");
            fail("User already exists in the database.");
        } catch (ObjectRetrievalFailureException orfe) {
            // User should not already exist in the database.
        }

        User user = new User();
        user.setLogin("test_user");
        user.setFirstName("First name");
        user.setLastName("Last name");
        user.setPassword("password");
        user.setVerifyPassword("password");
        
        try {
        	
        	this.userService.createUser(user);
            assertTrue(user.isEnabled());
            assertNotNull(user.getCreationDate());
            assertNotNull(user.getLastAccessDate());
            assertEquals(1, user.getRoles().size());
            Role testRole = user.getRoles().iterator().next();
            assertEquals(RolesEnum.ROLE_USER.toString(), testRole.getRole());
            assertEquals(1, user.getTodoLists().size());
            TodoList testTodoList = user.getTodoLists().iterator().next();
            assertNotNull(testTodoList.getLastUpdate());
            assertEquals(1, testTodoList.getTodos().size());
            
        } catch (UserAlreadyExistsException e) {
            fail("User already exists in the database.");
        }

        try {
            User userFoundInDatabase = this.userService.findUser("test_user");
            assertEquals("First name", userFoundInDatabase.getFirstName());
            assertEquals("Last name", userFoundInDatabase.getLastName());
        } catch (ObjectRetrievalFailureException orfe) {
            fail("User should have been found in the database.");
        }
    }

    
    
    /**
     * method createTodoList() :<br/>
     * .<br/>
     * <br/>
     * :  :  .<br/>
     */
    @Test
    @Transactional
    public void createTodoList() {
        createAuthenticatedUser();

        TodoList todoList = new TodoList();
        todoList.setName("test_list");

        assertEquals(1, this.userService.getCurrentUser().getTodoLists().size());

        this.todoListsService.createTodoList(todoList);

        assertEquals(1, todoList.getUsers().size());
        assertEquals("test_user", todoList.getUsers().iterator().next()
                .getLogin());
        assertEquals(2, this.userService.getCurrentUser().getTodoLists().size());

        TodoList todoListFromDatabase = this.todoListsService.findTodoList(todoList
                .getListId());
        assertEquals("test_list", todoListFromDatabase.getName());
    }

    
    
    /**
     * method testDeleteTodoList() :<br/>
     * .<br/>
     * <br/>
     * :  :  .<br/>
     */
    @Test
    @Transactional
    public void testDeleteTodoList() {
    	
        createAuthenticatedUser();

        TodoList todoList = new TodoList();
        todoList.setName("test_list");

        assertEquals(1, this.userService.getCurrentUser().getTodoLists().size());
        this.todoListsService.createTodoList(todoList);
        assertEquals(2, this.userService.getCurrentUser().getTodoLists().size());
        this.todoListsService.deleteTodoList(todoList.getListId());
        assertEquals(1, this.userService.getCurrentUser().getTodoLists().size());

        try {
        	this.todoListsService.findTodoList(todoList.getListId());
            fail("The todo list should have been deleted");
        } catch (ObjectRetrievalFailureException orfe) {
            // The todo list should not exist.
        }
    }

    
       
    /**
     * method createTodo() :<br/>
     * .<br/>
     * <br/>
     * :  :  .<br/>
     */
    @Test
    @Transactional
    public void createTodo() {
        createAuthenticatedUser();

        TodoList todoList = new TodoList();
        this.todoListsService.createTodoList(todoList);

        Todo todo = new Todo();
        todo.setDescription("test_todo");

        this.todosService.createTodo(todoList.getListId(), todo);
        assertNotNull(todo.getCreationDate());
        assertEquals(1, todoList.getTodos().size());
    }

    
    
    /**
     * method deleteTodo() :<br/>
     * .<br/>
     * <br/>
     * :  :  .<br/>
     */
    @Test
    @Transactional
    public void deleteTodo() {
        createAuthenticatedUser();

        TodoList todoList = new TodoList();
        todoList.setName("My List");
        this.todoListsService.createTodoList(todoList);
        assertEquals(0, todoList.getTodos().size());

        Todo todo = new Todo();
        todo.setDescription("test_todo");
        this.todosService.createTodo(todoList.getListId(), todo);
        assertNotNull(this.todosService.findTodo(todo.getTodoId()));
        assertEquals(1, todoList.getTodos().size());

        this.todosService.deleteTodo(todo);
        TodoList todoList2 = this.todoListsService.findTodoList(todoList.getListId());
        assertNull(this.todosService.findTodo(todo.getTodoId()));
        assertEquals(0, todoList2.getTodos().size());
    }

    
    
    /**
     * method sharedList() :<br/>
     * .<br/>
     * <br/>
     * :  :  .<br/>
     */
    @Test
    @Transactional
    public void sharedList() {
    	
        createAuthenticatedUser();
        User user2 = new User();
        user2.setLogin("test_user2");
        user2.setPassword("test_password");
        
        try {
        	this.userService.createUser(user2);
        } catch (UserAlreadyExistsException e) {
            fail("User already exists in the database.");
        }
        
        TodoList todoList = new TodoList();
        this.todoListsService.createTodoList(todoList);
        this.todoListsService.addTodoListUser(todoList.getListId(), "test_user2");
        assertEquals(2, todoList.getUsers().size());
        assertEquals(2, user2.getTodoLists().size());

        Todo todo = new Todo();
        todo.setDescription("test_todo");
        this.todosService.createTodo(todoList.getListId(), todo);
        assertEquals(1, todoList.getTodos().size());

        this.todoListsService.deleteTodoList(todoList.getListId());
        assertEquals(1, this.userService.getCurrentUser().getTodoLists().size());
        assertEquals(1, user2.getTodoLists().size());
    }

    
    
    /**
     * method createAuthenticatedUser() :<br/>
     * Authenticate the current user using Acegi Security.<br/>
     * <br/>
     * :  :  .<br/>
     */
    private void createAuthenticatedUser() {
    	
        final User user = new User();
        user.setLogin("test_user");
        user.setPassword("test_password");
        user.setFirstName("Test");
        user.setLastName("User");
        
        try {
        	this.userService.createUser(user);
        } catch (UserAlreadyExistsException e) {
            fail("User already exists in the database.");
        }

        SecurityContextImpl secureContext = new SecurityContextImpl();

        UserDetails userDetails = this.userDetailsService
                .loadUserByUsername("test_user");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails, "test_password");

        this.authenticationManager.authenticate(token);
        secureContext.setAuthentication(token);
        SecurityContextHolder.setContext(secureContext);
    }
    
    
    
}
