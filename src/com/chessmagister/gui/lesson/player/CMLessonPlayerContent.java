package com.chessmagister.gui.lesson.player;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.chessmagister.events.CMBoardListener;
import com.chessmagister.gui.lesson.common.CMChessBoard;
import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.logic.BoardCoordinates;
import com.chessmagister.logic.CMFigure;
import com.chessmagister.utils.CMAnswer;
import com.chessmagister.utils.CMConstants;

public class CMLessonPlayerContent extends JPanel implements CMBoardListener, CMSerializableComplex
{
	private static final long serialVersionUID = 1L;

	protected CMChessBoard m_chessBoard = null;

	protected CMFigure m_selectedFigure = null;
	
	protected JEditorPane m_lessonText = null;
	protected JEditorPane m_question = null;
	protected CMAnswerContent m_answers = null;
	
	GridBagConstraints m_constraints = new GridBagConstraints();

	public void setLessonText(String lessonText)
	{
		if(lessonText != null && lessonText.length() != 0)
		{
			m_constraints.weightx = 0.50;
			m_constraints.weighty = 0.30;
			m_constraints.gridx = 0;
			m_constraints.gridy = 0;

			m_lessonText = new JEditorPane();
			m_lessonText.setEditable(false);
			JScrollPane editorScroller = new JScrollPane(m_lessonText);
			m_lessonText.setVisible(true);
			editorScroller.setVisible(true);
			
			m_lessonText.setText(lessonText);
			
			this.add(editorScroller, m_constraints);
		}
	}
	
	public void setAnswers(List<CMAnswer> answers) throws Exception
	{
		if(answers != null && answers.size() != 0)
		{
			m_constraints.weightx = 0.50;
			m_constraints.weighty = 0.30;
			m_constraints.gridx = 0;
			m_constraints.gridy = 2;

			m_answers = new CMAnswerContent(answers);
			JScrollPane answersScroller = new JScrollPane(m_answers);
			m_answers.setVisible(true);
			answersScroller.setVisible(true);
			
			this.add(answersScroller, m_constraints);
		}
	}
	
	public void setLessonQuestion(String questionText)
	{
		if(questionText != null && questionText.length() != 0)
		{
			m_constraints.weightx = 0.50;
			m_constraints.weighty = 0.30;
			m_constraints.gridx = 0;
			m_constraints.gridy = 1;

			m_question = new JEditorPane();
			m_question.setEditable(false);
			JScrollPane questionScroller = new JScrollPane(m_question);
			m_question.setVisible(true);
			questionScroller.setVisible(true);
			m_question.setText(questionText);
			this.add(questionScroller, m_constraints);
		}
	}
	
	public CMLessonPlayerContent(CMChessBoard board) throws Exception
	{
		if(board == null)
		{
			throw new ExceptionInInitializerError("Board cannot be null");
		}
		m_chessBoard = board;
		board.setBoardListener(this);

		this.setLayout(new GridBagLayout());
		m_constraints.fill = GridBagConstraints.BOTH;
		m_constraints.insets = new Insets(	CMConstants.BOARD_X_OFFSET,
								CMConstants.BOARD_Y_OFFSET,
								CMConstants.BOARD_X_OFFSET,
								CMConstants.BOARD_Y_OFFSET);
	}
	
	public JEditorPane getEditor()
	{
		return m_lessonText;
	}

	@Override
	public void onCellClick(BoardCoordinates coordinates)
	{
		if(m_selectedFigure != null)
		{
			m_chessBoard.removeFigure(m_chessBoard.getSelectedCoordinates());
			m_chessBoard.putFigure(m_selectedFigure, coordinates);
			m_selectedFigure = null;
		}
		else
		{
			m_selectedFigure = m_chessBoard.getFigure(coordinates);
		}
		
		m_chessBoard.select(coordinates);
	}

	public CMChessBoard getChessBoard()
	{
		return m_chessBoard;
	}

	public void setChessBoard(CMChessBoard mChessBoard)
	{
		m_chessBoard = mChessBoard;
	}

	@Override
	public String getID()
	{
		return CMXmlConstants.BOARD_EDITOR_ID;
	}

	@Override
	public void fireUpdatedEvent()
	{
		
	}

	@Override
	public void performSave() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
