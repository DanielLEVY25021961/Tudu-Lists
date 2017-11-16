package tudu.service.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;

import javax.persistence.EntityManager;

import org.easymock.EasyMock;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import tudu.domain.Todo;
import tudu.domain.TodoList;
import tudu.domain.User;
import tudu.security.PermissionDeniedException;
import tudu.service.UserService;

/**
 * class TodoListsServiceImplTest :<br/>
 * .<br/>
 * <br/>
 *
 * - Exemple d'utilisation :<br/>
 *<br/>
 * 
 * - Mots-clé :<br/>
 * <br/>
 *
 * - Dépendances :<br/>
 * <br/>
 *
 *
 * @author daniel.levy Lévy
 * @version 1.0
 * @since 16 nov. 2017
 *
 */
public class TodoListsServiceImplTest {

    static String todoListBackup = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<todolist>" + " <title>test list</title>" + " <rss>true</rss>"
            + " <users>" + "  <this.user>test</this.user>" + " </users>" + " <todos>"
            + "  <todo id=\"0001\">"
            + "   <creationDate>1127860040000</creationDate>"
            + "   <description>test todo</description>"
            + "   <priority>10</priority>" + "   <completed>false</completed>"
            + "  </todo>" + " </todos>" + "</todolist>";

    
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
     * entityManager : EntityManager :<br/>
     * .<br/>
     */
    EntityManager entityManager = null;
    
    
    /**
     * userService : UserService :<br/>
     * .<br/>
     */
    UserService userService = null;

    
    /**
     * todoListsService : TodoListsServiceImpl :<br/>
     * .<br/>
     */
    TodoListsServiceImpl todoListsService = new TodoListsServiceImpl();

    
    /**
     * method before() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    @Before
    public void before() {
        this.todoList.setListId("001");
        this.todoList.setName("Test Todo List");
        this.todoList.setRssAllowed(false);

        this.user.setLogin("test_user");
        this.user.setFirstName("First name");
        this.user.setLastName("Last name");

        this.entityManager = createMock(EntityManager.class);
        this.userService = createMock(UserService.class);

        ReflectionTestUtils.setField(this.todoListsService, "this.entityManager", this.entityManager);
        ReflectionTestUtils.setField(this.todoListsService, "this.userService", this.userService);
    }

    
    
    /**
     * method after() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    @After
    public void after() {
        verify(this.entityManager);
        verify(this.userService);
    }

    
        
    /**
     * method replay() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    private void replay() {
        EasyMock.replay(this.entityManager);
        EasyMock.replay(this.userService);
    }

    
    
    /**
     * method testCreateTodoList() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    @Test
    public void testCreateTodoList() {
        expect(this.userService.getCurrentUser()).andReturn(this.user);
        this.entityManager.persist(this.todoList);

        replay();

        this.todoListsService.createTodoList(this.todoList);

        assertTrue(this.user.getTodoLists().contains(this.todoList));
    }

    
    
    /**
     * method testFindTodoList() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    @Test
    public void testFindTodoList() {
        this.todoList.getUsers().add(this.user);
        this.user.getTodoLists().add(this.todoList);
        expect(this.entityManager.find(TodoList.class, "001")).andReturn(this.todoList);
        expect(this.userService.getCurrentUser()).andReturn(this.user);

        replay();
        try {
            TodoList testTodoList = this.todoListsService.findTodoList("001");
            assertEquals(this.todoList, testTodoList);
        } catch (PermissionDeniedException pde) {
            fail("Permission denied when looking for Todo.");
        }
    }

    
    
    /**
     * method testFailedFindTodoList() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    @Test
    public void testFailedFindTodoList() {
        expect(this.entityManager.find(TodoList.class, "001")).andReturn(this.todoList);
        expect(this.userService.getCurrentUser()).andReturn(this.user);

        replay();

        try {
            this.todoListsService.findTodoList("001");
            fail("A PermissionDeniedException should have been thrown");
        } catch (PermissionDeniedException pde) {

        }
    }

    
    
    /**
     * method testUpdateTodoList() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    @Test
    public void testUpdateTodoList() {

        replay();

        this.todoListsService.updateTodoList(this.todoList);
    }

    
    
    
    /**
     * method testDeleteTodoList() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    @Test
    public void testDeleteTodoList() {
        this.todoList.getUsers().add(this.user);
        this.user.getTodoLists().add(this.todoList);
        expect(this.userService.getCurrentUser()).andReturn(this.user);
        expect(this.entityManager.find(TodoList.class, "001")).andReturn(this.todoList);
        this.entityManager.remove(this.todoList);

        replay();

        this.todoListsService.deleteTodoList("001");

        assertFalse(this.user.getTodoLists().contains(this.todoList));
    }

    
    
    
    /**
     * method testAddTodoListUser() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    @Test
    public void testAddTodoListUser() {
        this.todoList.getUsers().add(this.user);
        this.user.getTodoLists().add(this.todoList);
        expect(this.userService.getCurrentUser()).andReturn(this.user);
        expect(this.entityManager.find(TodoList.class, "001")).andReturn(this.todoList);
        User user2 = new User();
        user2.setLogin("another_user");
        expect(this.userService.findUser("another_user")).andReturn(user2);

        replay();

        this.todoListsService.addTodoListUser("001", "another_user");

        assertTrue(this.todoList.getUsers().contains(user2));
        assertTrue(user2.getTodoLists().contains(this.todoList));
    }

    
    
    /**
     * method testDeleteTodoListUser() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    @Test
    public void testDeleteTodoListUser() {
        this.todoList.getUsers().add(this.user);
        this.user.getTodoLists().add(this.todoList);
        User user2 = new User();
        user2.setLogin("another_user");
        user2.getTodoLists().add(this.todoList);
        this.todoList.getUsers().add(user2);
        expect(this.userService.getCurrentUser()).andReturn(this.user);
        expect(this.entityManager.find(TodoList.class, "001")).andReturn(this.todoList);
        expect(this.userService.findUser("another_user")).andReturn(user2);

        replay();

        this.todoListsService.deleteTodoListUser("001", "another_user");

        assertFalse(this.todoList.getUsers().contains(user2));
        assertFalse(user2.getTodoLists().contains(this.todoList));
    }

    
    
    /**
     * method testBackupTodoList() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    @Test
    public void testBackupTodoList() {
        this.todoList.getUsers().add(this.user);
        this.user.getTodoLists().add(this.todoList);

        Todo todo = new Todo();
        todo.setTodoId("0001");
        Calendar creationCal = Calendar.getInstance();
        creationCal.clear();
        creationCal.set(Calendar.YEAR, 2005);
        todo.setCreationDate(creationCal.getTime());
        todo.setDescription("Backup Test description");
        todo.setPriority(0);
        todo.setCompleted(false);

        this.todoList.getTodos().add(todo);

        expect(this.entityManager.find(TodoList.class, "001")).andReturn(this.todoList);
        expect(this.userService.getCurrentUser()).andReturn(this.user);

        replay();

        Document doc = this.todoListsService.backupTodoList("001");

        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlContent = outputter.outputString(doc);

        assertTrue(xmlContent.indexOf("<title>Test Todo List</title>") > 0);
        assertTrue(xmlContent.indexOf("<todo id=\"0001\">") > 0);
        assertTrue(xmlContent.indexOf("<creationDate>"
                + creationCal.getTimeInMillis() + "</creationDate>") > 0);
        assertTrue(xmlContent
                .indexOf("<description>Backup Test description</description>") > 0);
        assertTrue(xmlContent.indexOf("<priority>0</priority>") > 0);
        assertTrue(xmlContent.indexOf("<completed>false</completed>") > 0);
    }

    
    
    /**
     * method testRestoreTodoListCreate() :<br/>
     * .<br/>
     * <br/>
     *
     * @throws Exception : void :  .<br/>
     */
    @Test
    public void testRestoreTodoListCreate() throws Exception {
        InputStream content = new ByteArrayInputStream(todoListBackup
                .getBytes());

        expect(this.userService.getCurrentUser()).andReturn(this.user);
        TodoList todoList2 = new TodoList();
        this.entityManager.persist(todoList2);
        Todo todo = new Todo();
        this.entityManager.persist(todo);

        replay();

        this.todoListsService.restoreTodoList("create", "001", content);
    }

    
    
    /**
     * method testRestoreTodoListReplace() :<br/>
     * .<br/>
     * <br/>
     *
     * @throws Exception : void :  .<br/>
     */
    @Test
    public void testRestoreTodoListReplace() throws Exception {
        InputStream content = new ByteArrayInputStream(todoListBackup
                .getBytes());

        this.todoList.getUsers().add(this.user);
        this.user.getTodoLists().add(this.todoList);
        Todo todo = new Todo();
        todo.setTodoId("0001");
        todo.setTodoList(this.todoList);
        this.todoList.getTodos().add(todo);
        expect(this.entityManager.find(TodoList.class, "001")).andReturn(this.todoList);
        expect(this.userService.getCurrentUser()).andReturn(this.user);
        this.entityManager.remove(todo);
        Todo createdTodo = new Todo();
        this.entityManager.persist(createdTodo);

        replay();

        this.todoListsService.restoreTodoList("replace", "001", content);
    }

    
    
    /**
     * method testRestoreTodoListMerge() :<br/>
     * .<br/>
     * <br/>
     *
     * @throws Exception : void :  .<br/>
     */
    @Test
    public void testRestoreTodoListMerge() throws Exception {
        InputStream content = new ByteArrayInputStream(todoListBackup
                .getBytes());

        this.todoList.getUsers().add(this.user);
        this.user.getTodoLists().add(this.todoList);
        expect(this.entityManager.find(TodoList.class, "001")).andReturn(this.todoList);
        expect(this.userService.getCurrentUser()).andReturn(this.user);
        Todo createdTodo = new Todo();
        this.entityManager.persist(createdTodo);

        replay();

        this.todoListsService.restoreTodoList("merge", "001", content);

        assertNotNull(this.todoList.getLastUpdate());
    }
    
    
    
}
