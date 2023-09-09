package com.chessmagister.gui.lesson.creator;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.io.CMXmlListNode;
import com.chessmagister.utils.CMAnswer;

class CMTaskAnswerTableModel extends AbstractTableModel implements CMSerializableComplex
{
	protected String[] columnNames = { "Answer", "Correct"};
	@CMXmlListNode(	id = CMXmlConstants.ANSWER_TABLE_DATA_LIST_ID,
					elementId = CMXmlConstants.ANSWER_TABLE_PAIR_ID,
					itemClass = CMAnswer.class)
	public List<CMAnswer> m_data = new Vector<CMAnswer>();

	public List<CMAnswer> getData()
	{
		return m_data;
	}

	public void setData(List<CMAnswer> mData)
	{
		m_data = mData;
	}

	// Constructor: calculate currency change to create the last column
	public CMTaskAnswerTableModel()
	{
//		this.setColumnIdentifiers(columnNames)
	}

	public void addRow()
	{
		m_data.add(new CMAnswer("", false));
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
		return column == 0 ? m_data.get(row).m_answer.getValue() : m_data.get(row).m_correct.getBoolValue();
	}

	public Class getColumnClass(int column)
	{
		return column == 0 ? String.class : Boolean.class;
	}

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
			m_data.get(row).m_answer.setValue((String)value);
		}
		else
		{
			m_data.get(row).m_correct.setValue((Boolean)value);
		}
		fireTableCellUpdated(row, col);
	}

	protected static final int COLUMN_COUNT = 2;

	protected static final Class thisClass = CMTaskAnswerTableModel.class;

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