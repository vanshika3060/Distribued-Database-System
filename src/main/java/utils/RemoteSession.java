package utils;

import com.jcraft.jsch.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Properties;

import static utils.Constants.VM1_ROOT_PATH;
import static utils.Constants.VM2_ROOT_PATH;

public class RemoteSession {
    static Session session = null;
    static Channel channel = null;
    static ChannelSftp sftpChannel = null;

    public RemoteSession() {
        getChannelSession();
    }

    public static void closeRemoteConnection() {
        sftpChannel.exit();
        channel.disconnect();
        session.disconnect();

    }

    public static void putFile(String localFilePath){
        try{
            getChannelSession();
            String host = getHostName();
            String[] parts = localFilePath.split("\\.\\.");

            String remotePathName="";
            if (host.equals(Constants.VM1_HOST)) {
                remotePathName=VM2_ROOT_PATH+parts[1];
            } else if (host.equals(Constants.VM2_HOST)) {
                remotePathName=VM1_ROOT_PATH+parts[1];
            }

            sftpChannel.put(localFilePath, remotePathName);

        }catch (SftpException e){
            System.out.println("here"+e.getMessage());
        } catch (UnknownHostException e) {
            System.out.println("Except here"+e.getMessage());
        }finally{
            closeRemoteConnection();
        }
  

    }

    public static String getPath() {
        String filePath = null;
        try {
            String host = getHostName();
            if (host.equals(Constants.VM1_HOST)) {
                filePath = Constants.DATABASE_DIRECTORY_VM2;
            } else if (host.equals(Constants.VM2_HOST)) {
                filePath = Constants.DATABASE_DIRECTORY_VM1;
            }
            return filePath;

        } catch (UnknownHostException e) {
            System.out.println(e);
        }
        return filePath;


    }

    public static boolean makeDatabase(String dbName) {
        try {
            String dbDirectory = getPath();
            sftpChannel.cd(dbDirectory);
            sftpChannel.mkdir(dbName);
        } catch (SftpException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            closeRemoteConnection();
        }
        return true;
    }

    public static String getHostName() throws UnknownHostException {
        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost().getHostName();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostname;
    }


    public static void getChannelSession() {
        try {
            JSch jSch = new JSch();
            String user = null;
            String privatekey = null;
            String host = getHostName();
            if (host.equals(Constants.VM1_HOST) || host.equals(Constants.VM2_HOST)) {
                if (host.equals(Constants.VM1_HOST)) {
                    user = Constants.VM2_USER;
                    host = Constants.VM2_IP;
                    privatekey = Constants.VM2_PRIVATEKEY;

                } else if (host.equals(Constants.VM2_HOST)) {
                    user = Constants.VM1_USER;
                    host = Constants.VM1_IP;
                    privatekey = Constants.VM1_PRIVATEKEY;
                }
                jSch.addIdentity(privatekey);
                session = jSch.getSession(user, host, Constants.PORT);
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.connect();
                channel = session.openChannel("sftp");
                channel.setInputStream(System.in);
                channel.setOutputStream(System.out);
                channel.connect();
                sftpChannel = (ChannelSftp) channel;
            }

        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (JSchException e) {
            System.out.println(e.getMessage());
        }


    }
}
