package tudu.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * A property, used to hold the application configuration.
 * 
 * @author Julien Dubois
 */
@Entity
@Table(name = "property")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Property implements Serializable {

    private static final long serialVersionUID = 3434972458764657217L;

    /**
     * key : String :<br/>
     * .<br/>
     */
    @Id
    @Column(name = "pkey")
    private String key;

    
    /**
     * value : String :<br/>
     * .<br/>
     */
    private String value;


    
    /**
     * method getKey() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public String getKey() {
        return this.key;
    }

    
    
    /**
     * method setKey() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pKey :  :  .<br/>
     */
    public void setKey(
    		final String pKey) {
        this.key = pKey;
    }

    
    
    /**
     * method getValue() :<br/>
     * .<br/>
     * <br/>
     *
     * @return :  :  .<br/>
     */
    public String getValue() {
        return this.value;
    }

    
    
    /**
     * method setValue() :<br/>
     * .<br/>
     * <br/>
     *
     * @param pValue :  :  .<br/>
     */
    public void setValue(
    		final String pValue) {
        this.value = pValue;
    }
}
