package tudu.web.dwr.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import tudu.domain.TodoList;
import tudu.domain.User;
import tudu.service.TodoListsService;
import tudu.service.UserService;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class TodoListsDwrImplTest {

    TodoList todoList = new TodoList();

    TodoListsService todoListsService = null;
    UserService userService = null;

    TodoListsDwrImpl todoListsDwr = new TodoListsDwrImpl();

    @Before
    public void before() {
    	
        this.todoList.setListId("001");
        this.todoList.setName("Description");
        this.todoList.setRssAllowed(false);

        this.todoListsService = createMock(TodoListsService.class);

        this.userService = createMock(UserService.class);

        ReflectionTestUtils.setField(this.todoListsDwr, "todoListsService", this.todoListsService);
        ReflectionTestUtils.setField(this.todoListsDwr, "userService", this.userService);
    }

    @After
    public void after() {
        verify(this.todoListsService);
        verify(this.userService);
    }

    private void replay_() {
        replay(this.todoListsService);
        replay(this.userService);
    }

    @Test
    public void testGetTodoListName() {
        expect(this.todoListsService.findTodoList("001")).andReturn(this.todoList);

        replay_();

        String name = this.todoListsDwr.getTodoList("001").getName();
        assertEquals("Description", name);
    }

    @Test
    public void testGetTodoListRss() {
    	
    	this.todoList.setRssAllowed(true);
        expect(this.todoListsService.findTodoList("001")).andReturn(this.todoList);

        replay_();

        boolean rss = this.todoListsDwr.getTodoList("001").isRssAllowed();
        assertEquals(true, rss);
    }

    
    
    @Test
    public void testGetTodoListRss2() {
    	
    	this.todoList.setRssAllowed(false);
        expect(this.todoListsService.findTodoList("001")).andReturn(this.todoList);

        replay_();

        boolean rss = this.todoListsDwr.getTodoList("001").isRssAllowed();
        assertEquals(false, rss);
    }

    
    
    @Test
    public void testGetTodoListUsers() {
    	
        User user = new User();
        user.setLogin("test_user");
        expect(this.userService.getCurrentUser()).andReturn(user);

        this.todoList.getUsers().add(user);

        User user1 = new User();
        user1.setLogin("BBB");
        this.todoList.getUsers().add(user1);

        User user2 = new User();
        user2.setLogin("AAA");
        this.todoList.getUsers().add(user2);

        User user3 = new User();
        user3.setLogin("CCC");
        this.todoList.getUsers().add(user3);

        expect(this.todoListsService.findTodoList("001")).andReturn(this.todoList);

        replay_();

        String[] logins = this.todoListsDwr.getTodoListUsers("001");

        assertEquals(3, logins.length);
        assertEquals("AAA", logins[0]);
        assertEquals("BBB", logins[1]);
        assertEquals("CCC", logins[2]);
    }

    
    
    @Test
    public void testAddTodoListUser() {
    	
    	this.todoListsService.addTodoListUser("001", "test_user");

        replay_();

        this.todoListsDwr.addTodoListUser("001", "test_user");
    }

    
    
    @Test
    public void testDeleteTodoListUser() {
    	this.todoListsService.deleteTodoListUser("001", "test_user");

        replay_();

        this.todoListsDwr.deleteTodoListUser("001", "test_user");
    }

    
    
    @Test
    public void testEditTodoList() {
    	
        expect(this.todoListsService.findTodoList("001")).andReturn(this.todoList);
        this.todoListsService.updateTodoList(this.todoList);

        replay_();

        this.todoListsDwr.editTodoList("001", "edit name", "1");

        assertEquals("edit name", this.todoList.getName());
    }
    
    
}
