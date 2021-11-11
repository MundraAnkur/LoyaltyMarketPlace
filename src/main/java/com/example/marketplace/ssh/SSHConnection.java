package com.example.marketplace.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * @author ankurmundra
 * October 16, 2021
 */
public class SSHConnection {
    private final static int LOCAl_PORT = 4321;
    private final static int REMOTE_PORT = 1521;
    private final static int SSH_REMOTE_PORT = 22;
    private final static String SSH_USER = "DUOAdP5TI7TGwuCSSKjrUA==";
    private final static String S_PASS_PHRASE = "SFL3pcU6bGqnfAS5BxxRay7b37J3lvC1";
    private final static String SSH_REMOTE_SERVER = "remote.eos.ncsu.edu";
    private final static String ORACLE_REMOTE_SERVER = "ora.csc.ncsu.edu";
    private Session session; //represents each ssh session

    public SSHConnection () {
        try {
            StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
            decryptor.setPassword("youDontNeedToWorry");
            JSch jsch = new JSch();
            String user = decryptor.decrypt(SSH_USER);
            session = jsch.getSession(user, SSH_REMOTE_SERVER, SSH_REMOTE_PORT);
            session.setPassword(decryptor.decrypt(S_PASS_PHRASE));
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(); //ssh connection established!
            int forwardedPort = session.setPortForwardingL(LOCAl_PORT, ORACLE_REMOTE_SERVER, REMOTE_PORT);
            System.out.println("localhost:"+ forwardedPort +" -> "+ORACLE_REMOTE_SERVER+":"+REMOTE_PORT);
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public void closeSSH () {
        if(session != null && session.isConnected())
            session.disconnect();
    }
}
