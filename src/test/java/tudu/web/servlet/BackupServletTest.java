package tudu.web.servlet;


import static org.junit.Assert.assertTrue;

import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;



/**
 * class BackupServletTest :<br/>
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
public class BackupServletTest {

	
	
    /**
     * method testDoGet() :<br/>
     * .<br/>
     * <br/>
     *
     * @throws Exception : void :  .<br/>
     */
    @Test
    public void testDoGet() throws Exception {

        Document doc = new Document();
        Element todoListElement = new Element("todolist");
        todoListElement.addContent(new Element("title")
                .addContent("Backup List"));
        doc.addContent(todoListElement);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("todoListDocument", doc);
        request.setSession(session);

        MockHttpServletResponse response = new MockHttpServletResponse();

        BackupServlet backupServlet = new BackupServlet();
        backupServlet.doGet(request, response);

        String xmlContent = response.getContentAsString();

        assertTrue(xmlContent.indexOf("<title>Backup List</title>") > 0);
    }
    
    
}
