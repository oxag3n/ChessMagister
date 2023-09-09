package com.chessmagister.gui.lesson.common;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

public class CMTask
{
	protected JPanel m_taskPanel;
	protected List<CMTask> m_children = new Vector<CMTask>();
	protected boolean m_isLeaf = true;

	public CMTask()
	{
		m_taskPanel = new JPanel(new GridLayout(0, 1));
	}

	public void add(Component component)
	{
		m_taskPanel.add(component);
	}
	
	public JPanel getTaskPanel()
	{
		return m_taskPanel;
	}
	
	public boolean isLeaf()
	{
		return (m_children.size() == 0);
	}
	
	public int getChildCount()
	{
		return m_children.size();
	}
	
	public List<CMTask> getChildren()
	{
		return m_children;
	}
	
	public void addChild(CMTask child)
	{
		m_children.add(child);
	}
}
