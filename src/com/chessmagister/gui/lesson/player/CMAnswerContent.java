package com.chessmagister.gui.lesson.player;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import com.chessmagister.utils.CMAnswer;

public class CMAnswerContent extends JPanel
{
	List<CMAnswer> m_answers = null;
	Map<JToggleButton, Boolean> m_answerSelectors = new HashMap<JToggleButton, Boolean>();
	public CMAnswerContent(List<CMAnswer> answers) throws Exception
	{
		if(answers.size() == 0)
		{
			return;
		}

		int correctAnswers = calcRightAnswers(answers);
		if(correctAnswers == 0)
		{
			throw new Exception("No correct answers in the slide");
		}

		ButtonGroup answerTypeGroup = new ButtonGroup();
		for(CMAnswer answer : answers)
		{
			if(correctAnswers == 1)
			{
				JRadioButton radio = new JRadioButton(answer.m_answer.getValue());
				m_answerSelectors.put(radio, answer.m_correct.getBoolValue());
				answerTypeGroup.add(radio);
			}
			else // > 1
			{
				JCheckBox check = new JCheckBox(answer.m_answer.getValue());
				m_answerSelectors.put(check, answer.m_correct.getBoolValue());
			}
		}

		Set<JToggleButton> buttons = m_answerSelectors.keySet();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		for(JToggleButton button : buttons)
		{
			this.add(button);
		}
	}
	
	public int answersSize()
	{
		return m_answers.size();
	}
	
	protected int calcRightAnswers(List<CMAnswer> answers)
	{
		int result = 0;
		for(CMAnswer answer : answers)
		{
			if(answer.m_correct.getBoolValue())
			{
				result++;
			}
		}
		return result;
	}
}
