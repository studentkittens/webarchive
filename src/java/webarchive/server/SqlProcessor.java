package webarchive.server;

import java.sql.SQLException;
import java.util.List;

import webarchive.api.model.MetaData;
import webarchive.api.select.Select;
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
		List<MetaData> list = null;
		try {
			list = cH.getSql().select((Select) msg.getData());
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message answer = new Message(msg, list);
		try {
			cH.send(answer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
