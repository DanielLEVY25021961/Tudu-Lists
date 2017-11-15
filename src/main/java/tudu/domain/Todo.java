package tudu.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * A Todo.
 *
 * @author Julien Dubois
 */
@Entity
@Table(name = "todo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Todo implements Serializable, Comparable<Todo> {

    /**
     * The serialVersionUID.
     */
    private static final long serialVersionUID = 4048798961366546485L;

    
    /**
     * todoId : String :<br/>
     * .<br/>
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String todoId;

    
    /**
     * todoList : TodoList :<br/>
     * .<br/>
     */
    @ManyToOne
    private TodoList todoList;

   
    /**
     * creationDate : Date :<br/>
     * .<br/>
     */
    private Date creationDate;

       
    /**
     * description : String :<br/>
     * .<br/>
     */
    private String description;

       
    /**
     * priority : int :<br/>
     * .<br/>
     */
    private int priority;

       
    /**
     * completed : boolean :<br/>
     * .<br/>
     */
    private boolean completed;

    
    /**
     * completionDate : Date :<br/>
     * .<br/>
     */
    private Date completionDate;

    
    /**
     * dueDate : Date :<br/>
     * .<br/>
     */
    private Date dueDate;

    
    /**
     * assignedUser : User :<br/>
     * .<br/>
     */
    @ManyToOne
    private User assignedUser;

    
    
    /**
     * The length of this field is 10000, which is OK with MySQL but which will
     * cause trouble with other databases (Oracle is limited at 4000 characters,
     * SQL Server at 8000).
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(length = 10000)
    private String notes;

    
    /**
     * hasNotes : boolean :<br/>
     * .<br/>
     */
    private boolean hasNotes;

    
    
    /**
     * method getTodoId() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public String getTodoId() {
        return this.todoId;
    }

    
    
    /**
     * method setTodoId() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pTodoId :  :  .<br/>
     */
    public void setTodoId(
    		final String pTodoId) {
        this.todoId = pTodoId;
    }

    
    
    /**
     * method isCompleted() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public boolean isCompleted() {
        return this.completed;
    }


    
    /**
     * method setCompleted() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pCompleted :  :  .<br/>
     */
    public void setCompleted(
    		final boolean pCompleted) {
        this.completed = pCompleted;
    }

    
    
    /**
     * method getCompletionDate() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public Date getCompletionDate() {
        return this.completionDate;
    }

    
    
    /**
     * method setCompletionDate() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pCompletionDate :  :  .<br/>
     */
    public void setCompletionDate(Date pCompletionDate) {
        this.completionDate = pCompletionDate;
    }

    
    
    /**
     * method getCreationDate() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public Date getCreationDate() {
        return this.creationDate;
    }

    
    
    /**
     * method setCreationDate() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pCreationDate :  :  .<br/>
     */
    public void setCreationDate(
    		final Date pCreationDate) {
        this.creationDate = pCreationDate;
    }

    
    
    /**
     * method getDescription() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public String getDescription() {
        return this.description;
    }

    
    
    /**
     * method setDescription() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pDescription :  :  .<br/>
     */
    public void setDescription(
    		final String pDescription) {
        this.description = pDescription;
    }

    
    
    /**
     * method getDueDate() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public Date getDueDate() {
        return this.dueDate;
    }

    
    
    /**
     * method setDueDate() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pDueDate :  :  .<br/>
     */
    public void setDueDate(
    		final Date pDueDate) {
        this.dueDate = pDueDate;
    }

    
    
    /**
     * method getPriority() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public int getPriority() {
        return this.priority;
    }

    
    
    /**
     * method setPriority() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pPriority :  :  .<br/>
     */
    public void setPriority(
    		final int pPriority) {
        this.priority = pPriority;
    }

    
    
    /**
     * method getAssignedUser() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public User getAssignedUser() {
        return this.assignedUser;
    }

    
    
    /**
     * method setAssignedUser() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pAssignedUser :  :  .<br/>
     */
    public void setAssignedUser(
    		final User pAssignedUser) {
        this.assignedUser = pAssignedUser;
    }

    
    
    /**
     * method isHasNotes() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public boolean isHasNotes() {
        return this.hasNotes;
    }

    
    
    /**
     * method setHasNotes() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pHasNotes :  :  .<br/>
     */
    public void setHasNotes(
    		final boolean pHasNotes) {
        this.hasNotes = pHasNotes;
    }

    
    
    /**
     * method getNotes() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public String getNotes() {
        return this.notes;
    }

    
    
    /**
     * method setNotes() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pNotes :  :  .<br/>
     */
    public void setNotes(
    		final String pNotes) {
        this.notes = pNotes;
    }

    
    
    /**
     * method getTodoList() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public TodoList getTodoList() {
        return this.todoList;
    }

    
    
    /**
     * method setTodoList() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pTodoList :  :  .<br/>
     */
    public void setTodoList(
    		final TodoList pTodoList) {
        this.todoList = pTodoList;
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(
    		final Todo pOther) {
    	
        int order = pOther.getPriority() - this.getPriority();
        
        if (this.isCompleted()) {
            order += 10000;
        }
        if (pOther.isCompleted()) {
            order -= 10000;
        }
        if (order == 0) {
            order = (this.getDescription() + this.getTodoId()).compareTo(pOther
                    .getDescription()
                    + pOther.getTodoId());
        }
        return order;
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object pObjet) {
    	
        if (this == pObjet) {
        	return true;
        }
        
        if (pObjet == null || getClass() != pObjet.getClass()) {
        	return false;
        }

        final Todo other = (Todo) pObjet;

        if (this.todoId != null ? 
        		!this.todoId.equals(other.getTodoId()) :
        			other.getTodoId() != null) {
        	return false;
        }

        return true;
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.todoId != null ? this.todoId.hashCode() : 0;
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Todo{" +
                "this.todoId='" + this.todoId + '\'' +
                ", this.todoList=" + this.todoList.getListId() +
                ", this.description='" + this.description + '\'' +
                '}';
    }
    
    
}
