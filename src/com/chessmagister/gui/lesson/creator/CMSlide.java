package com.chessmagister.gui.lesson.creator;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.io.CMXmlListNode;
import com.chessmagister.io.CMXmlMapNode;
import com.chessmagister.io.CMXmlNode;
import com.chessmagister.logic.BoardCoordinates;
import com.chessmagister.logic.CMFigure;
import com.chessmagister.utils.CMAnswer;
import com.chessmagister.utils.CMBoardConfigEntry;
import com.chessmagister.utils.CMBoolNodeWrapper;
import com.chessmagister.utils.CMConstants;
import com.chessmagister.utils.CMStringNodeWrapper;

public class CMSlide extends JButton implements CMSerializableComplex
{
	protected boolean m_isSelected = false;
	public CMBoardEditor m_editor = null;
	@CMXmlNode public CMStringNodeWrapper m_lessonText = new CMStringNodeWrapper(CMXmlConstants.LESSON_TEXT_ID);
//	protected byte[] m_boardMatrix = null;
	@CMXmlMapNode(	id = CMXmlConstants.BOARD_MATRIX_ID,
			keyClass = BoardCoordinates.class,
			objClass = CMFigure.class)
	public Map<BoardCoordinates, CMFigure> m_boardMatrix = new HashMap<BoardCoordinates, CMFigure>();

//	@CMXmlComplexNode public CMTaskEditor m_taskEditor = null;
	
	@CMXmlNode public CMBoolNodeWrapper m_taskEditorEnabled = new CMBoolNodeWrapper(CMXmlConstants.TASK_EDITOR_ENABLED_ID);
	@CMXmlNode public CMStringNodeWrapper m_question = new CMStringNodeWrapper(CMXmlConstants.TASK_EDITOR_TEXT_ID);;
	@CMXmlNode public CMBoolNodeWrapper m_boardAnswerFlag = new CMBoolNodeWrapper(CMXmlConstants.TASK_EDITOR_BOARD_ANSWER_ID);;
	@CMXmlNode public CMBoolNodeWrapper m_textAnswerFlag = new CMBoolNodeWrapper(CMXmlConstants.TASK_EDITOR_TEXT_ANSWER_ID);;
	
	@CMXmlListNode(	id = CMXmlConstants.ANSWER_TABLE_DATA_LIST_ID,
			elementId = CMXmlConstants.ANSWER_TABLE_PAIR_ID,
			itemClass = CMAnswer.class)
	public List<CMAnswer> m_answers = new Vector<CMAnswer>();
	
	@CMXmlListNode(	id = CMXmlConstants.ANSWER_BOARD_CONFIG_LIST_ID,
			elementId = CMXmlConstants.ANSWER_BOARD_CONFIG_ENTRY_ID,
			itemClass = CMBoardConfigEntry.class)
	public List<CMBoardConfigEntry> m_boardConfig = CMBoardAnswersConfiguration.getDefaultConfigList();

	protected static Image m_icon = null;

	static
	{
		try {
			m_icon = ImageIO.read(new File("res/Slide.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public CMSlide()
	{
		super(new ImageIcon(m_icon.getScaledInstance(CMConstants.CELL_SIZE*2,
				CMConstants.CELL_SIZE*2, Image.SCALE_FAST)));

//		m_taskEditor = new CMTaskEditor();
	}
	
	public CMSlide(CMBoardEditor editor) throws IOException
	{
		super(new ImageIcon(m_icon.getScaledInstance(CMConstants.CELL_SIZE*2,
													CMConstants.CELL_SIZE*2, Image.SCALE_FAST)));
		m_editor = editor;
		this.setVisible(true);
		this.setBorder(BorderFactory.createRaisedBevelBorder());
	}

	public void select() throws Exception
	{
		m_isSelected = true;
		this.setBorderPainted(true);
		load();
		save();
	}

	public void unSelect() throws Exception
	{
		m_isSelected = false;
		this.setBorderPainted(false);
		save();
	}

	protected Object duplicateUsingStream(Object original) throws IOException, ClassNotFoundException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(bos);
		out.writeObject(original);
		out.close();
		
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray())); 
		return in.readObject();
	}
	
	public void save() throws IOException, Exception
	{
		m_boardMatrix = (Map<BoardCoordinates, CMFigure>)duplicateUsingStream(m_editor.getChessBoard().getBoardMatrix());

		BufferedImage img = new BufferedImage(	CMConstants.BOARD_X_OFFSET*2 + CMConstants.BOARD_SIZE,
												CMConstants.BOARD_Y_OFFSET*2 + CMConstants.BOARD_SIZE,
												BufferedImage.TYPE_4BYTE_ABGR);
		Graphics gx = img.getGraphics();
		m_editor.getChessBoard().paint(gx);
		this.setIcon(new ImageIcon(img.getScaledInstance(CMConstants.CELL_SIZE*2,
				CMConstants.CELL_SIZE*2, Image.SCALE_FAST)));

		m_lessonText.setValue(m_editor.getEditor().getText());

		m_editor.getTaskEditor().performSave();
		
//		m_taskEditor = (CMTaskEditor)duplicateUsingStream(m_editor.getTaskEditor());
		
		m_taskEditorEnabled = (CMBoolNodeWrapper)duplicateUsingStream(m_editor.getTaskEditor().m_enabled);
		m_question = (CMStringNodeWrapper)duplicateUsingStream(m_editor.getTaskEditor().m_text);
		m_boardAnswerFlag = (CMBoolNodeWrapper)duplicateUsingStream(m_editor.getTaskEditor().m_boardAnswerFlag);
		m_textAnswerFlag = (CMBoolNodeWrapper)duplicateUsingStream(m_editor.getTaskEditor().m_textAnswerFlag);
		m_answers = (Vector<CMAnswer>)duplicateUsingStream(m_editor.getTaskEditor().m_textAnswers.m_answersTableModel.m_data);
		m_boardConfig = (Vector<CMBoardConfigEntry>)duplicateUsingStream(m_editor.getTaskEditor().m_boardAnswers.m_configTableModel.m_data);
	}

	public void load() throws Exception
	{
		if(	m_boardMatrix != null &&
			m_lessonText != null &&
			m_taskEditorEnabled != null &&
			m_question != null &&
			m_boardAnswerFlag != null &&
			m_textAnswerFlag != null &&
			m_answers != null &&
			m_boardConfig != null)
		{
			m_editor.getChessBoard().setBoardMatrix((Map<BoardCoordinates, CMFigure>)duplicateUsingStream(m_boardMatrix));

			m_editor.getEditor().setText((String)duplicateUsingStream(m_lessonText.getValue()));

			m_editor.getTaskEditor().m_enabled = (CMBoolNodeWrapper)duplicateUsingStream(m_taskEditorEnabled);
			m_editor.getTaskEditor().m_text = (CMStringNodeWrapper)duplicateUsingStream(m_question);
			m_editor.getTaskEditor().m_boardAnswerFlag = (CMBoolNodeWrapper)duplicateUsingStream(m_boardAnswerFlag);
			m_editor.getTaskEditor().m_textAnswerFlag = (CMBoolNodeWrapper)duplicateUsingStream(m_textAnswerFlag);
			m_editor.getTaskEditor().m_textAnswers.getAnswersTableModel().setData(m_answers);
			m_editor.getTaskEditor().m_boardAnswers.m_configTableModel.m_data = m_boardConfig;

			m_editor.getTaskEditor().fireUpdatedEvent();
		}
	}

	public CMStringNodeWrapper getLessonText()
	{
		return m_lessonText;
	}
	
	public CMStringNodeWrapper getLessonQuestion()
	{
		return m_question;
	}
	
	public CMBoolNodeWrapper getTextAnswerFlag()
	{
		return m_textAnswerFlag;
	}
	
	public List<CMAnswer> getTextAnswers()
	{
		return m_answers;
	}

	@Override
	public String getID()
	{
		return CMXmlConstants.SLIDE_ID;
	}

	public void setBoardEditor(CMBoardEditor editor)
	{
		m_editor = editor;
	}
	
	@Override
	public void fireUpdatedEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performSave() throws Exception
	{
		if(m_isSelected == true)
		{
			this.save();
		}
	}
}
