package com.chessmagister.gui.lesson.creator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMXmlComplexNode;
import com.chessmagister.io.CMXmlConstants;

public class CMTextAnswersEditor extends JPanel implements CMSerializableComplex
{
	protected JTable m_answersTable = null;
	protected JButton m_addAnswer = null;
	protected JButton m_removeAnswer = null;
	@CMXmlComplexNode public CMTaskAnswerTableModel m_answersTableModel = null;

	public void setEnabled(boolean enabled)
	{
		m_answersTable.setEnabled(enabled);
		m_addAnswer.setEnabled(enabled);
		m_removeAnswer.setEnabled(enabled);
	}
	
	public CMTaskAnswerTableModel getAnswersTableModel()
	{
		return m_answersTableModel;
	}

	public CMTextAnswersEditor()
	{
		super(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;

		m_addAnswer = new JButton("Add answer");
		m_addAnswer.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				m_answersTableModel.addRow();
			}
		});
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		this.add(m_addAnswer, c);
		
		m_removeAnswer = new JButton("Remove answer");
		m_removeAnswer.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_answersTableModel.deleteRow(m_answersTable.getSelectedRow());
			}
		});

		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 0;
		this.add(m_removeAnswer, c);

		c.fill = GridBagConstraints.BOTH;
		
		m_answersTableModel = new CMTaskAnswerTableModel();
		m_answersTable = new JTable(m_answersTableModel);

		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 4;
		c.gridheight = 4;
		c.weightx = 4;
		c.weighty = 4;
		c.gridx = 0;
		c.gridy = 1;

		this.add(m_answersTable, c);
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
