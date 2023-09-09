package com.chessmagister.gui.lesson.creator;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class CMBoardConfigTable extends JTable
{
	public CMBoardConfigTable(CMBoardConfigTableModel model)
	{
		super(model);
	}

	public TableCellRenderer getCellRenderer(int row, int column)
	{
		TableCellRenderer renderer = null;
		if (getValueAt(row,column) != null)
		{
			renderer = getDefaultRenderer(getValueAt(row,column).getClass());
		}

		if (renderer == null)
		{
			renderer = getDefaultRenderer(getColumnClass(column));
		}
			return renderer;
	}
	
	public TableCellEditor getCellEditor(int row, int column)
	{
		TableCellEditor editor = null;
		if (getValueAt(row,column) != null)
		{
			editor = getDefaultEditor(getValueAt(row,column).getClass());
		}

		if (editor == null)
		{
			editor = getDefaultEditor(getColumnClass(column));
		}
			return editor;
	}
}