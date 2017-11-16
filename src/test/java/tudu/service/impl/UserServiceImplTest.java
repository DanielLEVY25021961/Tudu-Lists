package tudu.service.impl;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import tudu.domain.*;
import tudu.service.UserAlreadyExistsException;

import javax.persistence.EntityManager;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

/**
 * class UserServiceImplTest :<br/>
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
public class UserServiceImplTest {

    /**
     * user : User :<br/>
     * .<br/>
     */
    User user = new User();

    
    /**
     * this.userService : UserServiceImpl :<br/>
     * .<br/>
     */
    UserServiceImpl userService = new UserServiceImpl();

    
    /**
     * entityManager : EntityManager :<br/>
     * .<br/>
     */
    EntityManager entityManager = null;

    
    
    /**
     * method before() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    @Before
    public void before() {
        this.user.setLogin("test_user");
        this.user.setFirstName("First name");
        this.user.setLastName("Last name");

        this.entityManager = createMock(EntityManager.class);

        ReflectionTestUtils.setField(this.userService, "this.entityManager", this.entityManager);
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
    }

    
    
    /**
     * method replay() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    private void replay() {
        EasyMock.replay(this.entityManager);
    }

    
      
    /**
     * method testFindUser() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    @Test
    public void testFindUser() {
        expect(this.entityManager.find(User.class, "test_user")).andReturn(this.user);

        replay();

        User testUser = this.userService.findUser("test_user");
        assertEquals(testUser, this.user);
    }

    
    
    /**
     * method testUpdateUser() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    @Test
    public void testUpdateUser() {
        expect(this.entityManager.merge(this.user)).andReturn(null);
        replay();
        this.userService.updateUser(this.user);
    }

    
    
    /**
     * method testCreateUser() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    @Test
    public void testCreateUser() {
        expect(this.entityManager.find(User.class, "test_user")).andReturn(null);
        Role role = new Role();
        role.setRole(RolesEnum.ROLE_USER.name());
        expect(this.entityManager.find(Role.class, RolesEnum.ROLE_USER.name())).andReturn(role);
        this.entityManager.persist(this.user);
        TodoList todoList = new TodoList();
        Todo todo = new Todo();
        this.entityManager.persist(todo);
        this.entityManager.persist(todoList);

        replay();

        try {
            this.userService.createUser(this.user);
            assertTrue(this.user.isEnabled());
            assertNotNull(this.user.getCreationDate());
            assertNotNull(this.user.getLastAccessDate());
            assertEquals(1, this.user.getRoles().size());
            Role testRole = this.user.getRoles().iterator().next();
            assertEquals(RolesEnum.ROLE_USER.toString(), testRole.getRole());
            assertEquals(1, this.user.getTodoLists().size());
            TodoList testTodoList = this.user.getTodoLists().iterator().next();
            assertNotNull(testTodoList.getLastUpdate());
            assertEquals(1, testTodoList.getTodos().size());
        } catch (UserAlreadyExistsException e) {
            fail("A UserAlreadyExistsException should not have been thrown.");
        }
    }

    
    
    /**
     * method testFailedCreateUser() :<br/>
     * .<br/>
     * <br/>
     * : void :  .<br/>
     */
    @Test
    public void testFailedCreateUser() {
        expect(this.entityManager.find(User.class, "test_user")).andReturn(this.user);

        replay();

        try {
            this.userService.createUser(this.user);
            fail("A UserAlreadyExistsException should have been thrown.");
        } catch (UserAlreadyExistsException e) {
            assertNotNull(this.user);
        }
    }
    
    
    
}
