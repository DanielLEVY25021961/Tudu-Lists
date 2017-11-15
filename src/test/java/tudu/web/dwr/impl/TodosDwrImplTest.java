package tudu.web.dwr.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import tudu.domain.Todo;
import tudu.domain.TodoList;
import tudu.domain.User;
import tudu.service.TodosService;
import tudu.service.UserService;
import tudu.web.dwr.bean.RemoteTodo;
import tudu.web.dwr.bean.RemoteTodoList;

import java.util.Calendar;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class TodosDwrImplTest {

    Todo todo = new Todo();
    User user = new User();

    TodosService todosService = null;
    UserService userService = null;

    TodosDwrImpl todosDwrImpl = new TodosDwrImplTestable();

    @Before
    public void before() {
    	
    	this.todo.setTodoId("0001");
    	this.todo.setDescription("Test description");
    	this.todo.setPriority(0);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 30);
        cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
        cal.set(Calendar.YEAR, 2005);
        this.todo.setDueDate(cal.getTime());
        this.todo.setCompleted(false);

        TodoList todoList = new TodoList();
        todoList.setListId("01");
        this.todo.setTodoList(todoList);

        this.user.setLogin("test_user");
        this.user.setFirstName("First name");
        this.user.setLastName("Last name");

        this.userService = createMock(UserService.class);

        this.todosService = createMock(TodosService.class);

        ReflectionTestUtils.setField(this.todosDwrImpl, "todosService", this.todosService);
        ReflectionTestUtils.setField(this.todosDwrImpl, "userService", this.userService);

    }

    
    
    @After
    public void after() {
        verify(this.todosService);
        verify(this.userService);
    }

    
    
    private void replay_() {
        replay(this.todosService);
        replay(this.userService);
    }

    
    
    @Test
    public void testGetCurrentTodoLists() {
    	
        TodoList todoList1 = new TodoList();
        todoList1.setListId("001");
        todoList1.setName("BBB");
        Todo todo1 = new Todo();
        todo1.setTodoId("0001");
        todo1.setCompleted(false);
        todoList1.getTodos().add(todo1);
        this.user.getTodoLists().add(todoList1);

        TodoList todoList2 = new TodoList();
        todoList2.setListId("002");
        todoList2.setName("AAA");
        this.user.getTodoLists().add(todoList2);

        TodoList todoList3 = new TodoList();
        todoList3.setListId("003");
        todoList3.setName("AAA");
        Todo todo2 = new Todo();
        todo2.setTodoId("0002");
        todo2.setCompleted(false);
        todoList3.getTodos().add(todo2);
        Todo todo3 = new Todo();
        todo3.setTodoId("0003");
        todo3.setCompleted(true);
        todoList3.getTodos().add(todo3);
        this.user.getTodoLists().add(todoList3);

        TodoList todoList4 = new TodoList();
        todoList4.setListId("004");
        todoList4.setName("CCC");
        this.user.getTodoLists().add(todoList4);

        expect(this.userService.getCurrentUser()).andReturn(this.user);

        replay_();

        RemoteTodoList[] remoteTodoList = this.todosDwrImpl
                .getCurrentTodoLists(null);

        assertEquals("002", remoteTodoList[0].getListId());
        assertEquals("AAA (0/0)", remoteTodoList[0].getDescription());

        assertEquals("003", remoteTodoList[1].getListId());
        assertEquals("AAA (1/2)", remoteTodoList[1].getDescription());

        assertEquals("001", remoteTodoList[2].getListId());
        assertEquals("BBB (0/1)", remoteTodoList[2].getDescription());

        assertEquals("004", remoteTodoList[3].getListId());
        assertEquals("CCC (0/0)", remoteTodoList[3].getDescription());
    }

    
    
    public void testGetTodoById() {
        expect(this.todosService.findTodo("0001")).andReturn(this.todo);

        replay_();

        RemoteTodo remoteTodo = this.todosDwrImpl.getTodoById("0001");
        assertEquals(this.todo.getDescription(), remoteTodo.getDescription());
        assertEquals("09/30/2005", remoteTodo.getDueDate());
        assertEquals(this.todo.getPriority(), remoteTodo.getPriority());
    }

    
    
    public void testReopenTodo() {
    	
        expect(this.todosService.reopenTodo("001")).andReturn(this.todo);

        replay_();

        this.todosDwrImpl.reopenTodo("001");
    }

    
    
    public void testCompleteTodo() {
        expect(this.todosService.completeTodo("001")).andReturn(this.todo);

        replay_();

        this.todosDwrImpl.completeTodo("001");
    }

    
    
    public void testEditTodo() {
        expect(this.todosService.findTodo("001")).andReturn(this.todo);

        //todosService.updateTodo(todo);

        replay_();

        String description = "new description";
        String priority = "100";
        String dueDate = "01/01/2006";
        this.todosDwrImpl
                .editTodo("001", description, priority, dueDate, null, null);

        assertEquals("new description", this.todo.getDescription());
        assertEquals(100, this.todo.getPriority());
        Calendar testCal = Calendar.getInstance();
        testCal.setTime(this.todo.getDueDate());
        assertEquals(1, testCal.get(Calendar.DATE));
        assertEquals(Calendar.JANUARY, testCal.get(Calendar.MONTH));
        assertEquals(2006, testCal.get(Calendar.YEAR));
    }

    
    
    public void testEditTodoWithErrors() {
        expect(this.todosService.findTodo("001")).andReturn(this.todo);

        //todosService.updateTodo(todo);

        replay_();

        String description = "new description";
        String priority = "string";
        String dueDate = "string";
        this.todosDwrImpl
                .editTodo("001", description, priority, dueDate, null, null);

        assertEquals("new description", this.todo.getDescription());
        assertEquals(0, this.todo.getPriority());
        Calendar testCal = Calendar.getInstance();
        testCal.setTime(this.todo.getDueDate());
        assertEquals(30, testCal.get(Calendar.DATE));
        assertEquals(Calendar.SEPTEMBER, testCal.get(Calendar.MONTH));
        assertEquals(2005, testCal.get(Calendar.YEAR));
    }

    
    
    public void testInputNotes() {
    	
        replay_();

        String notes = null;
        this.todosDwrImpl.inputNotes(this.todo, notes);
        assertNull(this.todo.getNotes());
        assertFalse(this.todo.isHasNotes());

        notes = "123";
        this.todosDwrImpl.inputNotes(this.todo, notes);
        assertEquals("123", this.todo.getNotes());
        assertTrue(this.todo.isHasNotes());

        StringBuffer buffer = new StringBuffer();
        
        for (int i = 0; i < 1001; i++) {
            buffer.append("1234567890");
        }
        
        this.todosDwrImpl.inputNotes(this.todo, buffer.toString());
        assertEquals(10000, this.todo.getNotes().length());
        assertTrue(this.todo.isHasNotes());
    }

    
    
    public void testDeleteTodo() {
    	
        expect(this.todosService.findTodo("001")).andReturn(this.todo);

        this.todosService.deleteTodo(this.todo);

        replay_();

        this.todosDwrImpl.deleteTodo("001");
    }
    
    
}
