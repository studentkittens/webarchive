package webarchive.server;

import webarchive.connection.Connection;
import webarchive.connection.NetworkModule;
import webarchive.dbaccess.DbConfigHandler;
import webarchive.dbaccess.SqlHandler;
import webarchive.dbaccess.SqliteAccess;
import webarchive.handler.HandlerCollection;
import webarchive.init.ConfigHandler;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class Server implements Runnable,NetworkModule {


    private int listenPort;
    private ServerSocket svSock;
    private List<Connection> cList;
    private List<Connection> observers;
    private HandlerCollection handlers;

    private Boolean running = false;
    
    private Thread thread;
	private String configPath = "conf/webarchive.conf.xml";
    
    public Thread getThread() {
		return thread;
	}
/**
 * set before start()
 * @param configPath path of configfile
 */
	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}


	private static Server sv=null;

    private Server() {
    	sv = this;
    	getHandlers().add(new ConfigHandler(configPath));
    	getHandlers().add(new SvConfigHandler());
    	getHandlers().add(new DbConfigHandler());
    	
    	this.listenPort = new Integer(((SvConfigHandler)getHandlers().get("SvConfigHandler")).getValue("port"));
    	
        this.cList = new ArrayList<Connection>();
        this.observers = new ArrayList<Connection>();
        
        getHandlers().add(new FileHandler());
        try {
			getHandlers().add(new LockHandlerImpl(InetAddress.getLocalHost(),42421));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        try {
//			getHandlers().add(new XmlConf()); //TODO
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        getHandlers().add( new SqlHandler( new SqliteAccess( new File(((DbConfigHandler)getHandlers().get("DbConfigHandler")).getValue("path")))));
    }
    
    public static Server getInstance() {
    	if (sv != null) {
    		return sv;
    	} else {
    		new Server();
    		return sv;
    	}
    }
    
    public boolean start() {
    	synchronized (running) {
    	if(isRunning())
			return false;
    		thread = new Thread(sv);
    		thread.start();
    	}
    	return isRunning();
    }
    
    public boolean stop() {
    	synchronized (running) {
	    	if(!isRunning())
	    		return false;
	    	try {
	    		System.out.println("closing svSock");
				svSock.close();
			} catch (IOException e) {
				e.printStackTrace();

			}
    	}
    	return true;
    }
    
    @Override
	public void run() {
    	
		
		synchronized (running) {
			if(isRunning())
				return;
			setRunning(true);
		}
			accept();
		synchronized (running) {
			setRunning(false);
		}
	}
    
    
    private void setRunning(boolean running) {
    	this.running=running;
    }
    
    private boolean isRunning() {
    	return running;
    }
    
    private void disconnectClients() {
    	System.out.println("disconnecting Clients " + cList.size());
    	synchronized (cList) {
	    	for(Connection c : cList) {
					try {
						c.getSocket().close();
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    	}
		    cList.clear();
    	}
    	System.out.println("Clients disconnected");
    }
    
    private void accept() {
    	try {
			this.svSock = new ServerSocket(this.listenPort);
			System.out.println("creating svSocket");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        while(true)
        {
            Socket sock=null;

            try {
            	System.out.println("awaiting incomming connection");
                sock = svSock.accept();
            } catch (SocketException e) {
            	//TODO
            	disconnectClients();
            	break;
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                continue;
            }

            ObjectInputStream ois=null;
            ObjectOutputStream oos = null;
            System.out.println("trying to get streams");
            try {
                oos = new ObjectOutputStream(sock.getOutputStream());
                ois = new ObjectInputStream(sock.getInputStream());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("trying to safe connection");
            Connection c = new Connection(sock,oos,ois);
            c.setConHandler(new ServerConnectionHandler(c,this));
            
            new Thread(c).start();
            
            if(doHandShake(c))
            {
                addNewConnection(c);
                System.out.println("HANDSHAKE SUCCESS");
            }
            else
            {
                System.out.println("HANDSHAKE FAILED");
                try {
                    sock.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                continue;
            }

            

        }
    }

    private void addNewConnection(Connection c)
    {
    	synchronized (cList) {
    		cList.add(c);
    	}
    }

    private boolean doHandShake(Connection c)
    {
        Message h = null;
        try {
            System.out.println("try sending handshake");

            Message m = new Message(Header.HANDSHAKE);
            
            c.send(m);
            System.out.println("handshake sent, try receiving handshake");

            h = c.waitForAnswer(m);//Go to sleep
            
            System.out.println("handshake received");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        if( (h != null) && (h.getHeader() == Header.HANDSHAKE) ) {
            return true;
        }

        return false;
    }

	@Override
	public void removeConnection(Connection c) {
		synchronized (cList) {
			cList.remove(c);
		}
		synchronized (observers) {
			observers.remove(c);
		}
	}
	

	@Override
	public HandlerCollection getHandlers() {
		if(handlers == null) {
			handlers = new HandlerCollection();
		}
		return handlers;
	}

	public List<Connection> getObservers() {
		return observers;
	}

    
}
