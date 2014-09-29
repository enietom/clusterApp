package org.jboss.refarch.eap6.cluster.http;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ClusteredServlet
 */
public class ClusteredServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ClusteredServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null) {
			session = request.getSession(true);
			session.setAttribute("initialization", new Date());
			session.setAttribute("initial_server",
					System.getProperty("jboss.server.name"));
		}

		if (request.getParameter("save") != null) {
			String key = request.getParameter("key");
			String value = request.getParameter("value");
			if (key.length() > 0) {
				if (value.length() == 0) {
					session.removeAttribute(key);
				} else {
					session.setAttribute(key, value);
				}
			}
		}
		
		String serverName = System.getProperty("jboss.server.name");
		request.setAttribute("serverName", serverName);
		
		String nodeName = System.getProperty("jboss.node.name");
		request.setAttribute("nodeName", nodeName);
		
		RequestDispatcher rd = request.getRequestDispatcher("/result.jsp");
		rd.forward(request, response);
	}

}
