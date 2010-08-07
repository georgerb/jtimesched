package de.dominik_geyer.jtimesched.project;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;


@SuppressWarnings("serial")
public class ProjectTableModel extends AbstractTableModel {
	private static final int COLUMN_COUNT = 7;
	
	public static final int COLUMN_ACTION_DELETE = 0;
	public static final int COLUMN_TITLE = 1;
	public static final int COLUMN_COLOR = 2;
	public static final int COLUMN_CREATED = 3;
	public static final int COLUMN_TIMEOVERALL = 4;
	public static final int COLUMN_TIMETODAY = 5;
	public static final int COLUMN_ACTION_STARTPAUSE = 6;
	
	private String[] columnNames = new String[] {
		"", "Title", "", "Created", "Time Overall", "Time Today", "",
	};
	
	
	private ArrayList<Project> arPrj;
	
	public ProjectTableModel(ArrayList<Project> arPrj) {
		this.arPrj = arPrj;
	}
	
	@Override
	public int getColumnCount() {
		return ProjectTableModel.COLUMN_COUNT;
	}

	@Override
	public int getRowCount() {
		return arPrj.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		Object o;
		
		Project prj = arPrj.get(row);
		
		switch (column) {
		case ProjectTableModel.COLUMN_TITLE:
			o = prj.getTitle();
			break;
		case ProjectTableModel.COLUMN_COLOR:
			o = prj.getColor();
			break;
		case ProjectTableModel.COLUMN_CREATED:
			o = prj.getTimeCreated();
			break;
		case ProjectTableModel.COLUMN_TIMEOVERALL:
			o = new Integer(prj.getSecondsOverall());
			break;
		case ProjectTableModel.COLUMN_TIMETODAY:
			o = new Integer(prj.getSecondsToday());
			break;
		case ProjectTableModel.COLUMN_ACTION_DELETE:
		case ProjectTableModel.COLUMN_ACTION_STARTPAUSE:
			o = (prj.isRunning()) ? new Boolean(true) : new Boolean(false);
			break;
		default:
			o = "wtf?";
		}

		
		return o;
	}
	
	public Project getProjectAt(int row) {
		return this.arPrj.get(row);
	}

	@Override
	public String getColumnName(int column) {
		return this.columnNames[column];
	}
	
	@Override
	public Class<?> getColumnClass(int column) {
		switch (column) {
		case ProjectTableModel.COLUMN_COLOR:
			return Color.class;
		case ProjectTableModel.COLUMN_CREATED:
			return Date.class;
		case ProjectTableModel.COLUMN_TIMEOVERALL:
		case ProjectTableModel.COLUMN_TIMETODAY:
			return Integer.class;
		case ProjectTableModel.COLUMN_ACTION_DELETE:
		case ProjectTableModel.COLUMN_ACTION_STARTPAUSE:
			return Boolean.class;
		default:
			return String.class;
			//return getValueAt(0, column).getClass();   // WARNING: sorter would throw exception!
		}
	}


	@Override
	public boolean isCellEditable(int row, int column) {
		Project prj = this.getProjectAt(row);
		
		switch (column) {
		case ProjectTableModel.COLUMN_TITLE:
		case ProjectTableModel.COLUMN_COLOR:
			return true;
		case ProjectTableModel.COLUMN_TIMEOVERALL:
		case ProjectTableModel.COLUMN_TIMETODAY:
			// running tasks cannot be edited
			return (prj.isRunning() ? false : true);
		default:
			return false;	
		}
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		Project prj = this.getProjectAt(row);
		
		switch (column) {
		case ProjectTableModel.COLUMN_TITLE:
			prj.setTitle((String)value);
			break;
		case ProjectTableModel.COLUMN_COLOR:
			prj.setColor((Color)value);
			break;
		case ProjectTableModel.COLUMN_TIMEOVERALL:
			prj.setSecondsOverall(((Integer)value).intValue());
			break;
		case ProjectTableModel.COLUMN_TIMETODAY:
			prj.adjustSecondsToday(((Integer)value).intValue());
			break;
		}
		
		this.fireTableRowsUpdated(row, row);
	}
	
	public void addProject(Project p) {
		this.arPrj.add(p);
		this.fireTableRowsInserted(this.getRowCount() -1, this.getRowCount() -1);
	}

	public void removeProject(int row) {
		Project p = this.getProjectAt(row);
		this.arPrj.remove(p);
		this.fireTableRowsDeleted(row, row);
	}
}