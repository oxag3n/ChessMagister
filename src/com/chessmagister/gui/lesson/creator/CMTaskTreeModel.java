package com.chessmagister.gui.lesson.creator;

import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.chessmagister.gui.lesson.common.CMTask;

public class CMTaskTreeModel implements TreeModel
{
	protected CMTask m_root;
	
	public CMTaskTreeModel(CMTask root)
	{
		m_root = root;
	}

	@Override
	public Object getRoot()
	{
		return m_root;
	}

	@Override
	public void addTreeModelListener(TreeModelListener arg0)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public Object getChild(Object node, int index)
	{
		CMTask task = (CMTask) node;
		return task.getChildren().get(index);
	}

	@Override
	public int getChildCount(Object node)
	{
		CMTask task = (CMTask)node;
		return task.getChildCount();
	}

	@Override
	public int getIndexOfChild(Object node, Object child)
	{
		CMTask task = (CMTask) node;
		List<CMTask> children = task.getChildren();
		if (children == null)
		{
			return -1;
		}
		for (int i = 0; i < children.size(); i++)
		{
			if (children.get(i) == child)
				return i;
		}
		return -1;
	}

	@Override
	public boolean isLeaf(Object node)
	{
		CMTask task = (CMTask)node;
		return task.isLeaf();
	}

	@Override
	public void removeTreeModelListener(TreeModelListener arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1)
	{
		// TODO Auto-generated method stub
		
	}
}
