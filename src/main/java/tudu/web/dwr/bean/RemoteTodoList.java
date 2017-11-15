package tudu.web.dwr.bean;

import java.io.Serializable;

public class RemoteTodoList implements Serializable, Comparable<RemoteTodoList> {

    /**
     * serialVersionUID : long :<br/>
     * .<br/>
     */
    private static final long serialVersionUID = -158971969843741512L;

    
    /**
     * listId : String :<br/>
     * .<br/>
     */
    private String listId;

    
    /**
     * name : String :<br/>
     * .<br/>
     */
    private String name;

    
    /**
     * description : String :<br/>
     * .<br/>
     */
    private String description;

    
    /**
     * rssAllowed : boolean :<br/>
     * .<br/>
     */
    private boolean rssAllowed;

    
    
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
     * @param pName :  :  .<br/>
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
     * {@inheritDoc}
     */
    @Override
	public int compareTo(
			final RemoteTodoList pOther) {
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
        
        if (!(pObject instanceof RemoteTodoList)) {
            return false;
        }

        final RemoteTodoList other = (RemoteTodoList) pObject;

        return !(this.listId != null ? !this.listId.equals(other.listId)
                : other.listId != null);

    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return (this.listId != null ? this.listId.hashCode() : 0);
    }
    
    
}
