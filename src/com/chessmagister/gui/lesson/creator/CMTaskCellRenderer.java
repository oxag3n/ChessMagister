package com.chessmagister.gui.lesson.creator;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.print.Book;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import com.chessmagister.gui.lesson.common.CMTask;

public class CMTaskCellRenderer implements TreeCellRenderer
{
	DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

	public CMTaskCellRenderer()
	{
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		Component returnValue = null;
		if ((value != null) && (value instanceof CMTask))
		{
			if (value instanceof CMTask)
			{
				JPanel taskPanel = ((CMTask)value).getTaskPanel();
				taskPanel.setEnabled(tree.isEnabled());
				returnValue = taskPanel;
			}
		}
		if (returnValue == null)
		{
			returnValue = defaultRenderer.getTreeCellRendererComponent(tree,
					value, selected, expanded, leaf, row, hasFocus);
		}
		return returnValue;
	}
}