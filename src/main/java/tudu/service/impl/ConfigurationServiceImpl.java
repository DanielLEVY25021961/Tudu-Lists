package tudu.service.impl;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import tudu.domain.Property;
import tudu.domain.Role;
import tudu.domain.RolesEnum;
import tudu.domain.User;
import tudu.service.ConfigurationService;
import tudu.service.UserAlreadyExistsException;
import tudu.service.UserService;

/**
 * Implementation of the tudu.service.ConfigurationService interface.
 *
 * @author Julien Dubois
 */
@Service
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {

    /**
     * staticContent : String :<br/>
     * .<br/>
     */
    public static String staticContent = "";

    
    /**
     * googleAnalyticsKey : String :<br/>
     * .<br/>
     */
    public static String googleAnalyticsKey = "";

    
    /**
     * this.log : Log :<br/>
     * .<br/>
     */
    private final Log log = LogFactory.getLog(ConfigurationServiceImpl.class);

    
    /**
     * entityManager : EntityManager :<br/>
     * .<br/>
     */
    @PersistenceContext
    private EntityManager entityManager;

    
    /**
     * userService : UserService :<br/>
     * .<br/>
     */
    @Autowired
    private UserService userService;

    
    
    /**
     * transactionManager : PlatformTransactionManager :<br/>
     * .<br/>
     */
    @Autowired
    private PlatformTransactionManager transactionManager;

    
    
    @PostConstruct
    public void init() {
    	
        this.log.warn("Initializing Tudu Lists");
        
        TransactionTemplate transactionTemplate = new TransactionTemplate(this.transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
        	
        	@Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    initDatabase();
                    initApplicationProperties();
                } catch (Exception e) {
                	
                	ConfigurationServiceImpl.this.log.fatal("Could not intialize the database: " + e.getMessage());
                    if (e instanceof ConstraintViolationException) {
                    	
                        Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) e)
                                .getConstraintViolations();

                        for (ConstraintViolation<?> violation : violations) {
                        	ConfigurationServiceImpl.this.log.warn("Constraint violation while initializing the database: "
                                    + violation.getPropertyPath().toString()
                                    + " - "
                                    + violation.getMessage());
                        }
                    }
                    status.setRollbackOnly();
                    throw new BeanInitializationException("Could not intialize the database.", e);
                }
            }
        });
    }

    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initDatabase() {
        this.log.warn("Testing Database.");
        Role role = this.entityManager.find(Role.class, RolesEnum.ROLE_USER.name());
        if (role != null) {
            this.log.info("Database is already populated.");
        } else {
            this.log.warn("Database is empty : populating with default values.");

            this.log.warn("Populating HSQLDB database.");
            Property hostProperty = new Property();
            hostProperty.setKey("smtp.host");
            hostProperty.setValue("");
            this.entityManager.persist(hostProperty);
            Property portProperty = new Property();
            portProperty.setKey("smtp.port");
            portProperty.setValue("25");
            this.entityManager.persist(portProperty);
            Property userProperty = new Property();
            userProperty.setKey("smtp.user");
            userProperty.setValue("");
            this.entityManager.persist(userProperty);
            Property passwordProperty = new Property();
            passwordProperty.setKey("smtp.password");
            passwordProperty.setValue("");
            this.entityManager.persist(passwordProperty);
            Property fromProperty = new Property();
            fromProperty.setKey("smtp.from");
            fromProperty.setValue("");
            this.entityManager.persist(fromProperty);

            Role userRole = new Role();
            userRole.setRole(RolesEnum.ROLE_USER.name());
            this.entityManager.persist(userRole);
            Role adminRole = new Role();
            adminRole.setRole(RolesEnum.ROLE_ADMIN.name());
            this.entityManager.persist(adminRole);

            this.entityManager.flush();

            User adminUser = new User();
            adminUser.setLogin("admin");
            adminUser.setPassword("password");
            adminUser.setVerifyPassword("password");
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setDateFormat("MM/dd/yyyy");
            try {
                this.userService.createUser(adminUser);
            } catch (UserAlreadyExistsException e) {
                this.log.error("Error while creating the admin user :"
                        + " the user already exists.");
            }
            Set<Role> roles = adminUser.getRoles();
            roles.add(adminRole);

            this.entityManager.flush();

            User user = new User();
            user.setLogin("user");
            user.setPassword("password");
            user.setVerifyPassword("password");
            user.setFirstName("Default");
            user.setLastName("User");
            user.setDateFormat("MM/dd/yyyy");
            try {
                this.userService.createUser(user);
            } catch (UserAlreadyExistsException e) {
                this.log.error("Error while creating the admin user : "
                        + "the user already exists.");
            }
            this.entityManager.flush();
        }

    }


    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public void initApplicationProperties() {
    	
        Property staticFilesPathProperty = this
                .getProperty("application.static.path");

        if (staticFilesPathProperty != null) {
            staticContent = staticFilesPathProperty.getValue();

        } else {
            staticContent = "/static";
        }

        Property googleAnalyticsKeyProperty = this
                .getProperty("google.analytics.key");

        if (googleAnalyticsKeyProperty != null) {
            googleAnalyticsKey = googleAnalyticsKeyProperty.getValue();
        }
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Property getProperty(
    		final String key) {
        return this.entityManager.find(Property.class, key);
    }


    
    /**
     * @see tudu.service.ConfigurationService#updateEmailProperties(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEmailProperties(
    		final String smtpHost, final String smtpPort
    		, final String smtpUser, final String smtpPassword
    			, final String smtpFrom) {

        Property hostProperty = this.entityManager.find(Property.class, "smtp.host");
        hostProperty.setValue(smtpHost);
        Property portProperty = this.entityManager.find(Property.class, "smtp.port");
        portProperty.setValue(smtpPort);
        Property userProperty = this.entityManager.find(Property.class, "smtp.user");
        userProperty.setValue(smtpUser);
        Property passwordProperty = this.entityManager.find(Property.class, "smtp.password");
        passwordProperty.setValue(smtpPassword);
        Property fromProperty = this.entityManager.find(Property.class, "smtp.from");
        fromProperty.setValue(smtpFrom);
    }

    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateApplicationProperties(
    		final String staticPath, final String googleKey) {
    	
        Property pathProperty = new Property();
        pathProperty.setKey("application.static.path");
        pathProperty.setValue(staticPath);
        this.setProperty(pathProperty);
        staticContent = staticPath;

        Property googleProperty = new Property();
        googleProperty.setKey("google.analytics.key");
        googleProperty.setValue(googleKey);
        this.setProperty(googleProperty);
        googleAnalyticsKey = googleKey;
    }

    
    
    /**
     * Set a property.
     * <p>
     * If the property doesn't exist yet, it is created.
     * </p>
     *
     * @param property The property
     */
    private void setProperty(Property property) {
        Property databaseProperty = this.getProperty(property.getKey());
        if (databaseProperty == null) {
            this.entityManager.persist(property);
        } else {
            databaseProperty.setValue(property.getValue());
        }
    }
}
