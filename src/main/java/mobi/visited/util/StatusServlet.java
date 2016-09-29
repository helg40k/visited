package mobi.visited.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StatusServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(StatusServlet.class);

    private static boolean enabled = true;

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String pathInfo = (request.getPathInfo() != null) ? request.getPathInfo() : "";
        pathInfo = pathInfo.toLowerCase();

        if(pathInfo.contains("enable")) {
            LOG.info("Enabling Service in the load balancer");
            enabled = true;
        }
        if(pathInfo.contains("disable")) {
            LOG.info("Disabling Service in the load balancer");
            enabled = false;
        }

        if(enabled) {
            response.getWriter().write("Enabled");
        } else {
            response.getWriter().write("Disabled");
        }
    }
}
