package com.trifork.sdm.importer.monitoring;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.trifork.sdm.importer.configuration.Configuration;
import com.trifork.sdm.importer.importers.FileImporterControlledIntervals;
import com.trifork.sdm.importer.persistence.mysql.MySQLConnectionManager;
import com.trifork.sdm.importer.spoolers.FileSpooler;
import com.trifork.sdm.importer.spoolers.SpoolerManager;
import com.trifork.sdm.util.DateUtils;


public class ImporterServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(ImporterServlet.class);

	private SpoolerManager manager = null;
	private DbIsAlive isAlive;
	private ProjectInfo build;
	
	
	@Override
	public void init() throws ServletException
	{
		super.init();
		
		manager = new SpoolerManager(Configuration.getString("spooler.rootdir"));
		
		isAlive = new DbIsAlive();
		
		build = new ProjectInfo(getServletConfig().getServletContext());
		
		getServletContext().setAttribute("manager", manager);
		getServletContext().setAttribute("dbstatus", isAlive);
		getServletContext().setAttribute("build", build);
	}


	@Override
	public void destroy()
	{
		super.destroy();
		
		super.destroy();
		manager.destroy();
		manager = null;
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		if ("spoolers".equals(req.getParameter("isAlive")))
		{
			isSpoolersAlive(manager, resp);
		}
		else if ("db".equals(req.getParameter("isAlive")))
		{
			isDbAlive(resp);
		}
		else if (req.getParameter("history") != null)
		{
			importHistory(resp);
		}
		else
		{
			String rej = req.getParameter("rejectedFiles");
			String overdue = req.getParameter("overdue");
			
			if (rej != null)
			{
				rejectedFiles(resp, rej);
			}
			else if (overdue != null)
			{
				overdue(resp, overdue);
			}
			else
			{
				getServletContext().getRequestDispatcher("/jsp/showStatus.jsp").forward(req, resp);
			}
		}
	}


	private void overdue(HttpServletResponse resp, String type) throws IOException
	{
		ServletOutputStream os = resp.getOutputStream();
		
		try
		{
			FileSpooler spooler = manager.getSpooler(type);
			if (!spooler.isOverdue())
				os.print("SDM-" + build.getVersion() + "\nFile import for type: '" + type
					+ "' is not overdue.");
			else
				resp.sendError(
					500,
					"SDM-"
						+ build.getVersion()
						+ "\nFile import for type: '"
						+ type
						+ "' is overdue! "
						+ "Last import: "
						+ DateUtils.toMySQLdate(spooler.getLastRun())
						+ " Next run was expected before: "
						+ DateUtils.toMySQLdate(((FileImporterControlledIntervals) spooler
							.getImporter()).getNextImportExpectedBefore(spooler.getLastRun())));

		}
		catch (IllegalArgumentException e)
		{
			resp.sendError(500, "SDM-" + build.getVersion()
				+ "\nUsage: rejectedFiles=type  example types: takst, cpr, ... " + e.getMessage());
		}
	}


	private void rejectedFiles(HttpServletResponse resp, String type) throws IOException
	{
		ServletOutputStream os = resp.getOutputStream();

		try
		{
			if (manager.isRejectDirEmpty(type))
			{
				os.println("SDM-" + build.getVersion());
				os.println("No files in rejected directory for type: '"+ type + "'.");
			}
			else
			{
				resp.sendError(500, "SDM-" + build.getVersion() + "\nrejected dirs contain rejected files!");
			}

		}
		catch (IllegalArgumentException e)
		{
			resp.sendError(500, "SDM-" + build.getVersion()
				+ "\nUsage: rejectedFiles=type  example types: takst, cpr, ... " + e.getMessage());
		}
	}


	private void isDbAlive(HttpServletResponse resp) throws IOException
	{
		if (isAlive.isDbAlive())
		{
			resp.getOutputStream().print("SDM-" + build.getVersion() + "\ndb connection is up");
		}
		else
		{
			resp.sendError(500, "SDM-" + build.getVersion() + "\ndb connection down");
		}
	}


	private void isSpoolersAlive(SpoolerManager manager, HttpServletResponse resp) throws IOException
	{
		if (manager.isAllSpoolersRunning())
		{
			resp.getOutputStream().println(
				"SDM-" + build.getVersion() + "\nall spoolers configured and running");
		}
		else
		{
			resp.sendError(500, "SDM-" + build.getVersion() + "\nOne or more spoolers are not running");
		}
	}


	public class DbIsAlive
	{
		public boolean isDbAlive()
		{
			boolean isAlive = false;
			Connection con = null;
			try
			{
				con = MySQLConnectionManager.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT 1");
				rs.next();
				if (1 == rs.getInt(1)) isAlive = true;
			}
			catch (Exception e)
			{
				//logger.error("db connection down", e);
			}
			finally
			{
				MySQLConnectionManager.close(con);
			}
			return isAlive;
		}
	}

	private void importHistory(HttpServletResponse resp) throws IOException
	{
		ServletOutputStream os = resp.getOutputStream();
		os.print("<h3>Import history</h3><table>");

		// boolean isAlive = false;

		Connection connection = null;

		try
		{
			connection = MySQLConnectionManager.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * from "
				+ MySQLConnectionManager.getHousekeepingDBName() + ".Import");
			while (rs.next())
			{
				os.print("<tr><td>" + rs.getTimestamp("importtime") + "</td><td>"
					+ rs.getString("spoolername") + "</td></tr>");
			}
			os.print("</table>");
		}
		catch (Exception e)
		{
			//logger.error("cannot retrieve import statistics.", e);
			os.print("cannot retrieve import statistics " + e.getMessage());
		}
		finally
		{
			MySQLConnectionManager.close(connection);
		}
	}
}
