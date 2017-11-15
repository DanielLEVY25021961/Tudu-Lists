package tudu.web.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * RSS feed presentation layer.
 * 
 * @author Julien Dubois
 */
public class BackupServlet extends HttpServlet {

    private static final long serialVersionUID = -5600239261220987185L;

    private final Log log = LogFactory.getLog(BackupServlet.class);

    @Override
    protected final void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        Document doc = (Document) request.getSession().getAttribute(
                "todoListDocument");
        request.getSession().removeAttribute("todoListDocument");
        response.setContentType("Content-Type: application/force-download");
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        Writer writer = response.getWriter();
        outputter.output(doc, writer);
        writer.close();
    }
}
