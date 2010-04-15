import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.Popup;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.views.AbstractView;


public class MigrationMain {
	
	private static Logger log = LoggerFactory.getLogger(MigrationMain.class);

	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JFrame f = new JFrame("Migration Tools");
		JTabbedPane pane = new JTabbedPane();
		
		MockDataManager mdm = new MockDataManager();
		
		
		
		JTable inst = new JTable(new ToolDataModel(mdm, "getInstalledTools"));
		JTable availMy = new JTable(new ToolDataModel(mdm, "getAvailableTools"));
		JTable availAll = new JTable(new ToolDataModel(mdm, "getAllAvailableTools"));
		

		
		MouseListener listener = new MyMouseListener();
		
		inst.addMouseListener(listener);
		availAll.addMouseListener(listener);
		availMy.addMouseListener(listener);
		
		pane.addTab("Installed Tools", new JScrollPane(inst));
		
		
		JTabbedPane availablePane = new JTabbedPane();
		availablePane.addTab("My platform", new JScrollPane(availMy));
		availablePane.addTab("All platforms", new JScrollPane(availAll));
		
		pane.addTab("Available", availablePane);
		f.setContentPane(pane);
		f.setSize(640,480);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	

	public static class MyMouseListener extends MouseAdapter {
		
		private void popup(MouseEvent e) {
			log.debug("mouseClicked");
			super.mouseClicked(e);
			if (e.isPopupTrigger() && e.getSource() instanceof javax.swing.JTable) {
				log.debug("popup triggered!");
				JTable table = (JTable) e.getComponent();
				int row = table.getSelectedRow();
				JPopupMenu popupMenu = new JPopupMenu();
				popupMenu.add(new InstallAction(table, row));
				popupMenu.add(new RemoveAction(table, row));
				popupMenu.show(table, e.getX(), e.getY());
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			popup(e);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			super.mouseReleased(e);
			popup(e);
		}
	}
	
	public static abstract class MouseAction extends AbstractAction {
		protected JTable table;
		protected int row;
		
		public MouseAction(String name, JTable table, int row) {
			super(name);
			this.table = table;
			this.row = row;
		}
	}
	public static class InstallAction extends MouseAction {
		
		public InstallAction(JTable table, int row) {
			super("Install", table, row);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			log.debug("install clicked. Source is " + e.getSource());
			ToolDataModel model = (ToolDataModel) table.getModel();
			model.install(row);
		}
	}
	
	public static class RemoveAction extends MouseAction {
		
		public RemoveAction(JTable table, int row) {
			super("Remove", table, row);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			log.debug("remove clicked. Souce is " + e.getSource().getClass());
			ToolDataModel model = (ToolDataModel) table.getModel();
			model.remove(row);
		}
	}
	
}
