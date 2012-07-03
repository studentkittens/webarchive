package webarchive.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import webarchive.api.model.MetaData;
import webarchive.api.select.Select;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

public class SqlProcessor implements MessageProcessor {

	private ServerConnectionHandler cH;
	private Message msg;

	@Override
	public void process(Message msg, ServerConnectionHandler cH) {
		new Thread(new SqlProcessor(cH,msg)).start();
	}
	
	private SqlProcessor(ServerConnectionHandler cH, Message msg) {
		super();
		this.cH = cH;
		this.msg = msg;
	}

	public SqlProcessor() {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void run() {
		List list = null;
		try {
			list = cH.getSql().select((Select) msg.getData());
		} catch (UnsupportedOperationException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,null,e);
		} catch (SQLException e) {
			Message exception = new Message(msg,e);
			exception.setHeader(Header.EXCEPTION);
			try {
				cH.send(exception);
			} catch (Exception e1) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, "Could not send the exception to the client!\n{0}",e1 );
			}
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,null,e);
		}
		Message answer = new Message(msg, list);
		try {
			cH.send(answer);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,"Could not send an answer to the client!\n{0}",e);
		}
		
	}

}
