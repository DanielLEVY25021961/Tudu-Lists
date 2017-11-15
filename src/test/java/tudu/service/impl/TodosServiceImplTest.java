package tudu.service.impl;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import tudu.domain.Todo;
import tudu.domain.TodoList;
import tudu.domain.User;
import tudu.security.PermissionDeniedException;
import tudu.service.TodoListsService;
import tudu.service.UserService;

import javax.persistence.EntityManager;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class TodosServiceImplTest {

    /**
     * todo : Todo :<br/>
     * .<br/>
     */
    Todo todo = new Todo();

    
    /**
     * todoList : TodoList :<br/>
     * .<br/>
     */
    TodoList todoList = new TodoList();
    
    
    /**
     * user : User :<br/>
     * .<br/>
     */
    User user = new User();
    

    /**
     * todoListsService : TodoListsService :<br/>
     * .<br/>
     */
    TodoListsService todoListsService = null;
    
    
    /**
     * userService : UserService :<br/>
     * .<br/>
     */
    UserService userService = null;
    
    
    /**
     * entityManager : EntityManager :<br/>
     * .<br/>
     */
    EntityManager entityManager = null;

    
    /**
     * todosService : TodosServiceImpl :<br/>
     * .<br/>
     */
    TodosServiceImpl todosService = new TodosServiceImpl();

    
    @Before
    public void setUp() {
        this.todo.setTodoId("0001");
        this.todo.setDescription("Test description");
        this.todo.setPriority(0);
        this.todo.setCompleted(false);

        this.todoList.setListId("001");
        this.todoList.setName("Test Todo List");
        this.todoList.setRssAllowed(false);

        this.user.setLogin("test_user");
        this.user.setFirstName("First name");
        this.user.setLastName("Last name");

        this.entityManager = createMock(EntityManager.class);
        this.todoListsService = createMock(TodoListsService.class);
        this.userService = createMock(UserService.class);

        ReflectionTestUtils.setField(this.todosService, "this.entityManager", this.entityManager);
        ReflectionTestUtils.setField(this.todosService, "this.todoListsService", this.todoListsService);
        ReflectionTestUtils.setField(this.todosService, "this.userService", this.userService);
    }

    @After
    public void tearDown() {
        verify(this.entityManager);
        verify(this.todoListsService);
        verify(this.userService);
    }

    private void replay() {
        EasyMock.replay(this.entityManager);
        EasyMock.replay(this.todoListsService);
        EasyMock.replay(this.userService);
    }

    @Test
    public void testFindTodo() {
        this.todo.setTodoList(this.todoList);
        expect(this.entityManager.find(Todo.class, "0001")).andReturn(this.todo);
        this.user.getTodoLists().add(this.todoList);
        expect(this.userService.getCurrentUser()).andReturn(this.user);

        replay();

        try {
            Todo testTodo = this.todosService.findTodo("0001");
            assertEquals(this.todo, testTodo);
        } catch (PermissionDeniedException pde) {
            fail("Permission denied when looking for Todo.");
        }
    }

    @Test
    public void testFailedFindTodo() {
        expect(this.entityManager.find(Todo.class, "0001")).andReturn(this.todo);
        expect(this.userService.getCurrentUser()).andReturn(this.user);

        replay();

        try {
            this.todosService.findTodo("0001");
            fail("A PermissionDeniedException should have been thrown");
        } catch (PermissionDeniedException pde) {

        }
    }

    @Test
    public void testCreateTodo() {
        expect(this.todoListsService.findTodoList("001")).andReturn(this.todoList);
        this.todoListsService.updateTodoList(this.todoList);
        this.entityManager.persist(this.todo);

        replay();

        this.todosService.createTodo("001", this.todo);

        assertNotNull(this.todo.getCreationDate());
        assertEquals(this.todoList, this.todo.getTodoList());
        assertTrue(this.todoList.getTodos().contains(this.todo));
    }

    @Test
    public void testUpdateTodo() {
        this.todoListsService.updateTodoList(this.todo.getTodoList());

        replay();

        this.todo.setCompleted(true);
        this.todosService.updateTodo(this.todo);
        assertTrue(this.todo.isCompleted());
    }

    @Test
    public void testDeleteTodo() {
        this.todo.setTodoList(this.todoList);
        this.todoList.getTodos().add(this.todo);
        this.user.getTodoLists().add(this.todoList);
        this.entityManager.remove(this.todo);
        this.todoListsService.updateTodoList(this.todo.getTodoList());

        replay();

        this.todosService.deleteTodo(this.todo);

        assertFalse(this.todoList.getTodos().contains(this.todo));
    }

    @Test
    public void testCompleteTodo() {
    	
        this.todo.setTodoList(this.todoList);
        this.todoList.getTodos().add(this.todo);
        expect(this.entityManager.find(Todo.class, "0001")).andReturn(this.todo);

        this.user.getTodoLists().add(this.todoList);
        expect(this.userService.getCurrentUser()).andReturn(this.user);

        this.todoListsService.updateTodoList(this.todo.getTodoList());

        replay();

        Todo todo2 = this.todosService.completeTodo("0001");

        assertTrue(todo2.isCompleted());
        assertNotNull(todo2.getCompletionDate());
    }

    @Test
    public void testReopenTodo() {
        this.todo.setTodoList(this.todoList);
        this.todoList.getTodos().add(this.todo);
        expect(this.entityManager.find(Todo.class, "0001")).andReturn(this.todo);

        this.user.getTodoLists().add(this.todoList);
        expect(this.userService.getCurrentUser()).andReturn(this.user);

        this.todoListsService.updateTodoList(this.todo.getTodoList());

        replay();

        Todo todo2 = this.todosService.reopenTodo("0001");

        assertFalse(todo2.isCompleted());
        assertNull(todo2.getCompletionDate());
    }
}
