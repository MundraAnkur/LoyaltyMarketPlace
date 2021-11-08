package com.example.marketplace.ssh;

import com.example.marketplace.util.Util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author ankurmundra
 * October 16, 2021
 */
@WebListener
public class MyContextListener implements ServletContextListener {
    private SSHConnection connections;

    public MyContextListener()
    {
        super();
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent arg0)
    {
        System.out.println("Context initialized ... !");
        try
        {
            System.out.println("Unity ... "+ Util.userName+", ,,,,"+Util.pass_phrase);
            connections = new SSHConnection();
        }
        catch (Throwable e)
        {
            e.printStackTrace(); // error connecting SSH server
        }
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0)
    {
        System.out.println("Context destroyed ... !");
        connections.closeSSH(); // disconnect
    }
}
