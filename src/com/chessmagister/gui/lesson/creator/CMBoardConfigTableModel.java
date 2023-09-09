package com.chessmagister.gui.lesson.creator;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.io.CMXmlListNode;
import com.chessmagister.utils.CMBoardConfigEntry;

class CMBoardConfigTableModel extends AbstractTableModel implements CMSerializableComplex
{
	protected String[] columnNames = { "Answer", "Correct"};
	@CMXmlListNode(	id = CMXmlConstants.ANSWER_BOARD_CONFIG_LIST_ID,
					elementId = CMXmlConstants.ANSWER_BOARD_CONFIG_ENTRY_ID,
					itemClass = CMBoardConfigEntry.class)
	public List<CMBoardConfigEntry> m_data = new Vector<CMBoardConfigEntry>();

	public List<CMBoardConfigEntry> getData()
	{
		return m_data;
	}

	public void setData(List<CMBoardConfigEntry> mData)
	{
		m_data = mData;
	}

	// Constructor: calculate currency change to create the last column
	public CMBoardConfigTableModel()
	{
//		this.setColumnIdentifiers(columnNames)
	}

	public void addRow(CMBoardConfigEntry entry)
	{
		m_data.add(entry);
		this.fireTableDataChanged();
	}

	public void deleteRow(int row)
	{
		if(row >= 0)
		{
			m_data.remove(row);
			this.fireTableDataChanged();
		}
	}

	// Implementation of TableModel interface
	public int getRowCount()
	{
		return m_data.size();
	}

	public int getColumnCount()
	{
		return COLUMN_COUNT;
	}

	public Object getValueAt(int row, int column)
	{
		if(column == 0)
		{
			return m_data.get(row).m_ID.getValue();
		}
		else
		{
			return m_data.get(row).getValue();
		}
	}

//	public Class getColumnClass(int column)
//	{
//		return column == 0 ? String.class : Boolean.class;
//	}

	public String getColumnName(int column)
	{
		return columnNames[column];
	}
	
	public boolean isCellEditable(int row, int col)
	{
		return true;
	}
	
	public void setValueAt(Object value, int row, int col)
	{
		if(col == 0)
		{
			m_data.get(row).m_ID.setValue((String)value);
		}
		else
		{
			m_data.get(row).setValue(value);
		}
		fireTableCellUpdated(row, col);
	}

	protected static final int COLUMN_COUNT = 2;

	protected static final Class thisClass = CMBoardConfigTableModel.class;

	@Override
	public void fireUpdatedEvent() throws Exception
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getID()
	{
		return CMXmlConstants.ANSWER_TABLE_MODEL_ID;
	}

	@Override
	public void performSave() throws Exception
	{
		// TODO Auto-generated method stub
		
	}
}