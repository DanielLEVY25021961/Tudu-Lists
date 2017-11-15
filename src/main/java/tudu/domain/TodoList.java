package tudu.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Todo List.
 * 
 * @author Julien Dubois
 */
@Entity
@Table(name = "todo_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TodoList implements Serializable, Comparable<TodoList> {

    /**
     * The serialVersionUID.
     */
    private static final long serialVersionUID = 4048798961366546485L;

    
    /**
     * listId : String :<br/>
     * .<br/>
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String listId;

    
    /**
     * name : String :<br/>
     * .<br/>
     */
    private String name;

    /**
     * rssAllowed : boolean :<br/>
     * .<br/>
     */
    private boolean rssAllowed;

    private Date lastUpdate;

    @OneToMany(mappedBy = "todoList")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Todo> todos = new HashSet<Todo>();

    
    /**
     * users : Set<User> :<br/>
     * .<br/>
     */
    @ManyToMany(mappedBy = "todoLists")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<User> users = new HashSet<User>();

    
    
    /**
     * method getListId() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public String getListId() {
        return this.listId;
    }

    
    
    /**
     * method setListId() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pListId :  :  .<br/>
     */
    public void setListId(
    		final String pListId) {
        this.listId = pListId;
    }

    
    
    /**
     * method getName() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public String getName() {
        return this.name;
    }

    
    
    /**
     * method setName() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pName : String :  :  .<br/>
     */
    public void setName(
    		final String pName) {
        this.name = pName;
    }

    
    
    /**
     * method isRssAllowed() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public boolean isRssAllowed() {
        return this.rssAllowed;
    }

    
    
    /**
     * method setRssAllowed() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pRssAllowed :  :  .<br/>
     */
    public void setRssAllowed(
    		final boolean pRssAllowed) {
        this.rssAllowed = pRssAllowed;
    }

    
    
    /**
     * method getLastUpdate() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public Date getLastUpdate() {
        return this.lastUpdate;
    }

    
    
    /**
     * method setLastUpdate() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pLastUpdate :  :  .<br/>
     */
    public void setLastUpdate(
    		final Date pLastUpdate) {
        this.lastUpdate = pLastUpdate;
    }

    
    
    /**
     * method getTodos() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public Set<Todo> getTodos() {
        return this.todos;
    }

    
    
    /**
     * method setTodos() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pTodos :  :  .<br/>
     */
    public void setTodos(
    		final Set<Todo> pTodos) {
        this.todos = pTodos;
    }

    
    
    /**
     * method getUsers() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public Set<User> getUsers() {
        return this.users;
    }

    
    
    /**
     * method setUsers() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pUsers :  :  .<br/>
     */
    public void setUsers(
    		final Set<User> pUsers) {
        this.users = pUsers;
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
	public int compareTo(
			final TodoList pOther) {
        return (this.getName().toLowerCase() + this.getListId()).compareTo(pOther
                .getName().toLowerCase()
                + pOther.getListId());
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(
    		final Object pObject) {
    	
        if (this == pObject) {
            return true;
        }
        if (!(pObject instanceof TodoList)) {
            return false;
        }

        final TodoList other = (TodoList) pObject;

        if (this.listId != null ? !this.listId.equals(other.getListId())
                : other.getListId() != null) {
            return false;
        }

        return true;
    }

    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return (this.listId != null ? this.listId.hashCode() : 0);
    }

    
    
    @Override
    public String toString() {
        return "TodoList{" +
                "listId='" + this.listId + '\'' +
                ", name='" + this.name + '\'' +
                ", lastUpdate=" + this.lastUpdate +
                ", todos=" + this.todos +
                ", users=" + this.users +
                '}';
    }
    
    
}
