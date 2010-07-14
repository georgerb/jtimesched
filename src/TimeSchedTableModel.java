import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;


@SuppressWarnings("serial")
public class TimeSchedTableModel extends AbstractTableModel {
	private static final int COLUMN_COUNT = 6;
	
	public static final int COLUMN_ACTION_DELETE = 0;
	public static final int COLUMN_TITLE = 1;
	public static final int COLUMN_CREATED = 2;
	public static final int COLUMN_TIMEOVERALL = 3;
	public static final int COLUMN_TIMETODAY = 4;
	public static final int COLUMN_ACTION_STARTPAUSE = 5;
	
	private String[] columnNames = new String[] {
		"", "Title", "Created", "Time Overall", "Time Today", "",
	};
	
	
	private ArrayList<Project> arPrj;
	
	public TimeSchedTableModel(ArrayList<Project> arPrj) {
		this.arPrj = arPrj;
	}
	
	@Override
	public int getColumnCount() {
		return TimeSchedTableModel.COLUMN_COUNT;
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
		case TimeSchedTableModel.COLUMN_TITLE:
			o = prj.getTitle();
			break;
		case TimeSchedTableModel.COLUMN_CREATED:
			o = prj.getTimeCreated();
			break;
		case TimeSchedTableModel.COLUMN_TIMEOVERALL:
			o = new Integer(prj.getSecondsOverall());
			break;
		case TimeSchedTableModel.COLUMN_TIMETODAY:
			o = new Integer(prj.getSecondsToday());
			break;
		case TimeSchedTableModel.COLUMN_ACTION_DELETE:
		case TimeSchedTableModel.COLUMN_ACTION_STARTPAUSE:
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
		case TimeSchedTableModel.COLUMN_TIMEOVERALL:
		case TimeSchedTableModel.COLUMN_TIMETODAY:
			return Integer.class;
		case TimeSchedTableModel.COLUMN_CREATED:
			return Date.class;
		case TimeSchedTableModel.COLUMN_ACTION_DELETE:
		case TimeSchedTableModel.COLUMN_ACTION_STARTPAUSE:
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
		case TimeSchedTableModel.COLUMN_TITLE:
			return true;
		case TimeSchedTableModel.COLUMN_TIMEOVERALL:
		case TimeSchedTableModel.COLUMN_TIMETODAY:
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
		case TimeSchedTableModel.COLUMN_TITLE:
			prj.setTitle((String)value);
			break;
		case TimeSchedTableModel.COLUMN_TIMEOVERALL:
			prj.setSecondsOverall(((Integer)value).intValue());
			break;
		case TimeSchedTableModel.COLUMN_TIMETODAY:
			prj.setSecondsToday(((Integer)value).intValue());
			break;
		}
		
		this.fireTableCellUpdated(row, column);
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
