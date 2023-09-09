package com.chessmagister.gui.lesson.creator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMXmlComplexNode;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.io.CMXmlNode;
import com.chessmagister.utils.CMBoolNodeWrapper;
import com.chessmagister.utils.CMStringNodeWrapper;

public class CMTaskEditor extends JPanel implements CMSerializableComplex, ActionListener, Serializable
{
	private static final long serialVersionUID = 8440108344776691112L;

	protected JEditorPane m_editor = null;
	protected JRadioButton m_textAnswer = null;
	protected JRadioButton m_playAnswer = null;
	protected JCheckBox m_taskEnabled = null;
	@CMXmlComplexNode public CMTextAnswersEditor m_textAnswers = null;
	@CMXmlComplexNode public CMBoardAnswersConfiguration m_boardAnswers = null;

	@CMXmlNode public CMBoolNodeWrapper m_enabled =
		new CMBoolNodeWrapper(CMXmlConstants.TASK_EDITOR_ENABLED_ID);
	@CMXmlNode public CMStringNodeWrapper m_text =
		new CMStringNodeWrapper(CMXmlConstants.TASK_EDITOR_TEXT_ID);
	@CMXmlNode public CMBoolNodeWrapper m_textAnswerFlag =
		new CMBoolNodeWrapper(CMXmlConstants.TASK_EDITOR_TEXT_ANSWER_ID);
	@CMXmlNode public CMBoolNodeWrapper m_boardAnswerFlag =
		new CMBoolNodeWrapper(CMXmlConstants.TASK_EDITOR_BOARD_ANSWER_ID);

	public CMTaskEditor()
	{
//		super(new BorderLayout());
		super(new GridBagLayout());
		init();
		registerEvents();

		setComponentsEnabled(false);
	}

	protected void registerEvents()
	{
		m_taskEnabled.addActionListener(this);
		m_textAnswer.addActionListener(this);
		m_playAnswer.addActionListener(this);
		m_taskEnabled.addActionListener(this);
	}
	
	protected void init()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;

		m_taskEnabled = new JCheckBox();
		this.add(m_taskEnabled, c);
		
		c.gridwidth = 4;
		c.gridheight = 1;
		c.weightx = 4;
		c.weighty = 3;
		c.gridx = 1;
		c.gridy = 0;

		m_editor = new JEditorPane();
		JScrollPane editorScroller = new JScrollPane(m_editor);
		m_editor.setVisible(true);
		editorScroller.setVisible(true);
		this.add(editorScroller, c);
		
		m_textAnswer = new JRadioButton("Text list answer");
		m_playAnswer = new JRadioButton("Answer on board");
		m_textAnswer.setSelected(true);
		
		ButtonGroup answerTypeGroup = new ButtonGroup();
		answerTypeGroup.add(m_textAnswer);
		answerTypeGroup.add(m_playAnswer);
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 1;
		c.gridy = 1;
		this.add(m_textAnswer, c);

		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 2;
		c.gridy = 1;
		this.add(m_playAnswer, c);

		m_textAnswers = new CMTextAnswersEditor();
		m_boardAnswers = new CMBoardAnswersConfiguration();
		showTextAnswers();
	}

	protected void checkAnswersType()
	{
		if(m_textAnswer.isSelected())
		{
			showTextAnswers();
		}
		else
		{
			showBoardAnswers();
		}
	}
	
	protected void showTextAnswers()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.gridwidth = 4;
		c.gridheight = 1;
		c.weightx = 4;
		c.weighty = 3;
		c.gridx = 1;
		c.gridy = 2;

		this.remove(m_boardAnswers);
		this.add(m_textAnswers, c);
		this.revalidate();
		this.repaint();
	}
	
	protected void showBoardAnswers()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.gridwidth = 4;
		c.gridheight = 1;
		c.weightx = 4;
		c.weighty = 3;
		c.gridx = 1;
		c.gridy = 2;

		this.remove(m_textAnswers);
		this.add(m_boardAnswers, c);
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void fireUpdatedEvent() throws Exception
	{
		m_taskEnabled.setSelected(m_enabled.getBoolValue());
		m_editor.setText(m_text.getValue());
		m_textAnswer.setSelected(m_textAnswerFlag.getBoolValue());
		m_playAnswer.setSelected(m_boardAnswerFlag.getBoolValue());

		m_textAnswers.fireUpdatedEvent();
		m_boardAnswers.fireUpdatedEvent();

		setComponentsEnabled(m_taskEnabled.isSelected());
		
		checkAnswersType();
	}

	@Override
	public String getID()
	{
		return CMXmlConstants.TASK_EDITOR_ID;
	}

	@Override
	public void performSave() throws Exception
	{
		m_enabled.setValue(m_taskEnabled.isSelected());
		m_text.setValue(m_editor.getText() != null ? m_editor.getText() : "");
		m_textAnswerFlag.setValue(m_textAnswer.isSelected());
		m_boardAnswerFlag.setValue(m_playAnswer.isSelected());
	}

	protected void setComponentsEnabled(boolean enabled)
	{
		m_editor.setEnabled(enabled);
		m_textAnswer.setEnabled(enabled);
		m_playAnswer.setEnabled(enabled);
		m_textAnswers.setEnabled(enabled);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == m_taskEnabled)
		{
			setComponentsEnabled(m_taskEnabled.isSelected());
		} else if(event.getSource() == m_textAnswer ||
				event.getSource() == m_playAnswer)
		{
			checkAnswersType();
		}
	}
}
