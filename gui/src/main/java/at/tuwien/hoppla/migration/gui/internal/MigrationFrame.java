package at.tuwien.hoppla.migration.gui.internal;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.Popup;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.DefaultTableModel;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.views.AbstractView;


public class MigrationFrame extends JFrame {
	
	private static Logger log = LoggerFactory.getLogger(MigrationFrame.class);

	public MigrationFrame(BundleContext ctx)  {
		super("Migration Tools");
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		JTabbedPane pane = new JTabbedPane();
		
		MockDataManager mdm = new MockDataManager(ctx);
		
		
		
		JTable inst = new JTable(new ToolDataModel(mdm, "getInstalledTools"));
		JTable availMy = new JTable(new ToolDataModel(mdm, "getAvailableTools"));
		JTable availAll = new JTable(new ToolDataModel(mdm, "getAllAvailableTools"));
		log.error("before service data model");
		JTable instTypes = new JTable(new ServiceDataModel(ctx));
		log.error("after service data model");
		
		MouseListener listener = new MyMouseListener();
		
		inst.addMouseListener(listener);
		availAll.addMouseListener(listener);
		availMy.addMouseListener(listener);
		
		pane.addTab("Installed Tools", new JScrollPane(inst));
		pane.addTab("Installation services", new JScrollPane(instTypes));
		
		JTabbedPane availablePane = new JTabbedPane();
		availablePane.addTab("My platform", new JScrollPane(availMy));
		availablePane.addTab("All platforms", new JScrollPane(availAll));
		
		pane.addTab("Available", availablePane);
		setContentPane(pane);
	}
	
	public void start() {
		setSize(640,480);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);		
	}
	
	public void stop() {
		this.setVisible(false);
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
				popupMenu.add(new CheckAction(table, row));
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
	
	public static class CheckAction extends MouseAction {
		
		public CheckAction(JTable table, int row) {
			super("Check", table, row);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			log.debug("check clicked. Source is " + e.getSource());
			ToolDataModel model = (ToolDataModel) table.getModel();
			boolean result = model.check(row);
			if (result) {
				JOptionPane.showMessageDialog(table, "is installed");
			}
			else {
				JOptionPane.showMessageDialog(table, "is NOT installed");
			}
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
