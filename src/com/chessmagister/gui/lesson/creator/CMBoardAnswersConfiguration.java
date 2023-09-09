package com.chessmagister.gui.lesson.creator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMXmlComplexNode;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.utils.CMBoardConfigEntry;

public class CMBoardAnswersConfiguration extends JPanel implements CMSerializableComplex
{
	protected CMBoardConfigTable m_answersTable = null;
	@CMXmlComplexNode public CMBoardConfigTableModel m_configTableModel = null;

	public void setEnabled(boolean enabled)
	{
		m_answersTable.setEnabled(enabled);
	}

	public CMBoardConfigTableModel getAnswersTableModel()
	{
		return m_configTableModel;
	}

	public CMBoardAnswersConfiguration()
	{
		super(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		m_configTableModel = new CMBoardConfigTableModel();
		m_answersTable = new CMBoardConfigTable(m_configTableModel);

		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 4;
		c.gridheight = 4;
		c.weightx = 4;
		c.weighty = 4;
		c.gridx = 0;
		c.gridy = 1;

		this.add(m_answersTable, c);
	}

	public static List<CMBoardConfigEntry> getDefaultConfigList()
	{
		List<CMBoardConfigEntry> entries = new Vector<CMBoardConfigEntry>();
		
		entries.add(new CMBoardConfigEntry("AI Enabled",
				CMBoardConfigEntry.ConfigType.BOOL,
				"false"));
		entries.add(new CMBoardConfigEntry("AB Depth",
				CMBoardConfigEntry.ConfigType.STRING,
				"4"));
		entries.add(new CMBoardConfigEntry("Maximum moves(0 - unlim)",
				CMBoardConfigEntry.ConfigType.STRING,
				"0"));
		return entries;
	}
	
	@Override
	public void fireUpdatedEvent() throws Exception
	{
		m_answersTable.revalidate();
	}

	@Override
	public String getID()
	{
		return CMXmlConstants.ANSWER_EDITOR_ID;
	}

	@Override
	public void performSave() throws Exception
	{
		// TODO Auto-generated method stub
		
	}
}
