package tudu.security;

import org.springframework.core.NestedRuntimeException;

/**
 * Exception that gets thrown on non-allowed object invocation.
 * 
 * @author Julien Dubois
 */
public class PermissionDeniedException extends NestedRuntimeException {

    private static final long serialVersionUID = 8068297009284976718L;

    
     /**
     * method CONSTRUCTEUR PermissionDeniedException() :<br/>
     * .<br/>
     * <br/>
     *
     * @param msg
     */
    public PermissionDeniedException(String msg) {
        super(msg);
    }
    
    
}
