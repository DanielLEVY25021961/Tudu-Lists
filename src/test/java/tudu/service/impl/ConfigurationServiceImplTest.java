package tudu.service.impl;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import tudu.domain.Property;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.easymock.EasyMock.*;

public class ConfigurationServiceImplTest {

    /**
     * configurationService : ConfigurationServiceImpl :<br/>
     * .<br/>
     */
    ConfigurationServiceImpl configurationService = new ConfigurationServiceImpl();

    
    /**
     * entityManager : EntityManager :<br/>
     * .<br/>
     */
    EntityManager entityManager;

    
    
    /**
     * method before() :<br/>
     * .<br/>
     * <br/>
     * :  :  .<br/>
     */
    @Before
    public void before() {
        this.entityManager = createMock(EntityManager.class);
        ReflectionTestUtils.setField(this.configurationService, "entityManager", this.entityManager);
    }

    
    
    /**
     * method after() :<br/>
     * .<br/>
     * <br/>
     * :  :  .<br/>
     */
    @After
    public void after() {
        verify(this.entityManager);
    }

    
    
    /**
     * method replay() :<br/>
     * .<br/>
     * <br/>
     * :  :  .<br/>
     */
    private void replay() {
        EasyMock.replay(this.entityManager);
    }

    
    
    /**
     * method testGetProperty() :<br/>
     * .<br/>
     * <br/>
     * :  :  .<br/>
     */
    @Test
    public void testGetProperty() {
        Property property = new Property();
        property.setKey("key");
        property.setValue("value");
        expect(this.entityManager.find(Property.class, "key")).andReturn(property);

        replay();
        Property test = this.configurationService.getProperty("key");
        assertEquals("value", test.getValue());
    }

    
    
    /**
     * method testUpdateEmailProperties() :<br/>
     * .<br/>
     * <br/>
     * :  :  .<br/>
     */
    @Test
    public void testUpdateEmailProperties() {
    	
        Property hostProperty = new Property();
        hostProperty.setKey("smtp.host");
        hostProperty.setValue("value");
        expect(this.entityManager.find(Property.class, "smtp.host")).andReturn(hostProperty);

        Property portProperty = new Property();
        portProperty.setKey("smtp.port");
        portProperty.setValue("value");
        expect(this.entityManager.find(Property.class, "smtp.port")).andReturn(portProperty);

        Property userProperty = new Property();
        userProperty.setKey("smtp.user");
        userProperty.setValue("value");
        expect(this.entityManager.find(Property.class, "smtp.user")).andReturn(userProperty);

        Property passwordProperty = new Property();
        passwordProperty.setKey("smtp.password");
        passwordProperty.setValue("value");
        expect(this.entityManager.find(Property.class, "smtp.password")).andReturn(passwordProperty);

        Property fromProperty = new Property();
        fromProperty.setKey("smtp.host");
        fromProperty.setValue("value");
        expect(this.entityManager.find(Property.class, "smtp.from")).andReturn(fromProperty);

        replay();

        this.configurationService.updateEmailProperties("host", "port", "user",
                "password", "from");
        assertEquals("host", hostProperty.getValue());
        assertEquals("port", portProperty.getValue());
        assertEquals("user", userProperty.getValue());
        assertEquals("password", passwordProperty.getValue());
        assertEquals("from", fromProperty.getValue());
    }
    
    

}
