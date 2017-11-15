package tudu.web.mvc;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * Form used by RestoreController.
 *
 * @author Julien Dubois
 */
public class RestoreTodoListModel {

    /**
     * listId : String :<br/>
     * .<br/>
     */
    @NotNull
    private String listId;

    /**
     * restoreChoice : String :<br/>
     * .<br/>
     */
    @NotNull
    private String restoreChoice;

    @NotNull
    private MultipartFile backupFile;

    
    
    /**
     * method getRestoreChoice() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public String getRestoreChoice() {
        return this.restoreChoice;
    }

    
    
    /**
     * method setRestoreChoice() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pRestoreChoice :  :  .<br/>
     */
    public void setRestoreChoice(
    		final String pRestoreChoice) {
        this.restoreChoice = pRestoreChoice;
    }

    
    
    /**
     * method getBackupFile() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public MultipartFile getBackupFile() {
        return this.backupFile;
    }

    
    
    /**
     * method setBackupFile() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pBackupFile :  :  .<br/>
     */
    public void setBackupFile(
    		final MultipartFile pBackupFile) {
        this.backupFile = pBackupFile;
    }

    
    
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
     * {@inheritDoc}
     */
    @Override
    public boolean equals(
    		final Object pObject) {
    	
        if (this == pObject) return true;
        
        if (pObject == null || getClass() != pObject.getClass()) {
        	return false;
        }

        final RestoreTodoListModel other 
        	= (RestoreTodoListModel) pObject;

        if (this.backupFile != null ? !this.backupFile.equals(other.backupFile) : other.backupFile != null) {
        	return false;
        }
        
        if (this.listId != null ? !this.listId.equals(other.listId) : other.listId != null) {
        	return false;
        }
        if (this.restoreChoice != null ? !this.restoreChoice.equals(other.restoreChoice) : other.restoreChoice != null) {
        	return false;
        }
            
        return true;
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
    	
        int result = this.listId != null ? this.listId.hashCode() : 0;
        result = 31 * result + (this.restoreChoice != null ? this.restoreChoice.hashCode() : 0);
        result = 31 * result + (this.backupFile != null ? this.backupFile.hashCode() : 0);
        return result;
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "RestoreTodoListModel{" +
                "listId='" + this.listId + '\'' +
                ", restoreChoice='" + this.restoreChoice + '\'' +
                ", backupFile=" + this.backupFile +
                '}';
    }
    
    
}
