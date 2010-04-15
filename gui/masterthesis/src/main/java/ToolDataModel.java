import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ToolDataModel extends AbstractTableModel implements Observer {

	private static Logger log = LoggerFactory.getLogger(ToolDataModel.class);
	
	private List<ToolInstance> tools;
	private int columnCount;
	private ToolDataManager mgr;
	private String method;
	
	public ToolDataModel(ToolDataManager mgr,  String method) {
		this(mgr, method, 3);
	}
	
	public ToolDataModel(ToolDataManager mgr, String method, int columnCount) {
		super();
		mgr.addObserver(this);
		this.mgr = mgr;
		this.method = method;
		this.columnCount = columnCount;
		updateTools();
	}
	
	private void updateTools() {
		
		try {
			this.tools = (List<ToolInstance>) mgr.getClass().getMethod(method, null).invoke(mgr, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getColumnCount() {
		return columnCount;
	}
	
	@Override
	public int getRowCount() {
		return tools.size();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ToolInstance t = tools.get(rowIndex);
		switch (columnIndex) {
			case 0: return t.getTool().getName();
			case 1: return t.getVersion();
			case 2: return t.getStatus().toString();
			default: return "";
		}
	}
	
	public void install(int row) {
		log.debug("install row" + row);
		mgr.install(tools.get(row));
		//updateTools();
		//fireTableDataChanged();
	}
	public void remove(int row) {
		log.debug("remove row" + row);
		mgr.remove(tools.get(row));
		updateTools();
		fireTableDataChanged();
	}

	@Override
	public void update(Observable o, Object arg) {
		log.debug("Updating model with method: " + method);
		updateTools();
		fireTableDataChanged();
	}

}
