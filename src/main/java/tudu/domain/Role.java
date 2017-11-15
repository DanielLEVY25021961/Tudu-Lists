package tudu.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * A user's role.
 * 
 * @author Julien Dubois
 */
@Entity
@Table(name = "role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role implements Serializable {

    private static final long serialVersionUID = -5636845397516495671L;

    /**
     * role : String :<br/>
     * .<br/>
     */
    @Id
    private String role;

    
    
    /**
     * method getRole() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public String getRole() {
        return this.role;
    }

    
    
    /**
     * method setRole() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pRole :  :  .<br/>
     */
    public void setRole(
    		final String pRole) {
        this.role = pRole;
    }
    
    
}
