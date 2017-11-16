package tudu.web.dwr.bean;

import java.io.Serializable;

/**
 * class RemoteTodo :<br/>
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
public class RemoteTodo implements Serializable {

    /**
     * serialVersionUID : long :<br/>
     * .<br/>
     */
    private static final long serialVersionUID = 2801256525807891607L;

    
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
     * dueDate : String :<br/>
     * .<br/>
     */
    private String dueDate;

    
    /**
     * hasNotes : boolean :<br/>
     * .<br/>
     */
    private boolean hasNotes;

    
    /**
     * notes : String :<br/>
     * .<br/>
     */
    private String notes;

    
    /**
     * assignedUserLogin : String :<br/>
     * .<br/>
     */
    private String assignedUserLogin;

    
    
    
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
     * method getDueDate() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public String getDueDate() {
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
    		final String pDueDate) {
        this.dueDate = pDueDate;
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
     * method getAssignedUserLogin() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public String getAssignedUserLogin() {
        return this.assignedUserLogin;
    }

    
    
    /**
     * method setAssignedUserLogin() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pAssignedUserLogin :  :  .<br/>
     */
    public void setAssignedUserLogin(
    		final String pAssignedUserLogin) {
        this.assignedUserLogin = pAssignedUserLogin;
    }
    
    
}
