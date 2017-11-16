package tudu.service;

/**
 * Exception thrown when trying to create an already existing user.
 * 
 * @author Julien Dubois
 */
public class UserAlreadyExistsException extends Exception {

    private static final long serialVersionUID = 4301449299714922447L;

    
     /**
     * method CONSTRUCTEUR UserAlreadyExistsException() :<br/>
     * .<br/>
     * <br/>
     *
     * @param message
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    
    
}
