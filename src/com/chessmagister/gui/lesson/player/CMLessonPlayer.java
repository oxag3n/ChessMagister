package com.chessmagister.gui.lesson.player;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.chessmagister.gui.lesson.common.CMChessBoard;
import com.chessmagister.gui.lesson.creator.CMSlide;
import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.io.CMXmlComplexNode;
import com.chessmagister.io.CMXmlIo;
import com.chessmagister.io.CMXmlListNode;
import com.chessmagister.logic.BoardCoordinates;
import com.chessmagister.logic.CMFigure;
import com.chessmagister.utils.CMAnswer;
import com.chessmagister.utils.CMComplexNodeReader;
import com.chessmagister.utils.CMConstants;

public class CMLessonPlayer extends JInternalFrame implements CMSerializableComplex
{
	private static final long serialVersionUID = 1L;

	protected CMChessBoard m_chessBoard = null;
	protected CMLessonPlayerContent m_playerContent;
	protected CMPlayerControlls m_controlls;
	protected int m_slideIndex = -1;

	public List<CMSlide> m_slides = new Vector<CMSlide>();
	@CMXmlComplexNode public CMComplexNodeReader m_slideEditorReader = new CMComplexNodeReader(CMXmlConstants.SLIDE_EDITOR_ID)
	{
		@CMXmlComplexNode public CMComplexNodeReader m_slideScrollerReader = new CMComplexNodeReader(CMXmlConstants.SLIDES_SCROLLER_ID)
		{
			@CMXmlListNode(	id = CMXmlConstants.SLIDE_LIST_ID,
					elementId = CMXmlConstants.SLIDE_ID,
					itemClass = CMSlide.class)
					public List<CMSlide> m_slidesReader = m_slides;
		};
	};

	protected String m_filePath = null;

	protected void init() throws Exception
	{
		this.setLayout(new BorderLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.00;
		c.weighty = 1.00;
		c.gridx = 0;
		c.gridy = 0;
		this.add(m_chessBoard = new CMChessBoard(), BorderLayout.WEST);
		m_chessBoard.setPreferredSize(new Dimension(CMConstants.BOARD_SIZE +
													CMConstants.BOARD_X_OFFSET*4,
													CMConstants.BOARD_SIZE +
													CMConstants.BOARD_Y_OFFSET*4));

		c.weightx = 0.01;
		c.weighty = 1.00;
		c.gridx = 1;
		c.gridy = 0;
		this.add(m_playerContent = new CMLessonPlayerContent(m_chessBoard), BorderLayout.EAST);
		m_playerContent.setPreferredSize(new Dimension(CMConstants.BOARD_SIZE, 0));

		c.weightx = 0.0;
		c.gridwidth = 2;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		this.add(m_controlls = new CMPlayerControlls(this), BorderLayout.SOUTH);
		
		this.pack();
	}
	
	public CMLessonPlayer(String filePath) throws Exception
	{
		super("Lesson - " + filePath, true, true, true, true);
		m_filePath = filePath;
		init();
	}
	
	public CMLessonPlayer() throws Exception
	{
		super("Lesson", true, true, true, true);
		init();
	}

	@Override
	public String getID()
	{
		return CMXmlConstants.LESSON_ID;
	}

	public void checkAnswer()
	{
		
	}
	
	protected void updateSlide() throws Exception
	{
		CMSlide slide = m_slides.get(m_slideIndex);

		m_chessBoard.setBoardMatrix(slide.m_boardMatrix);
		m_playerContent.removeAll();
		m_playerContent.setLessonText(slide.getLessonText().getValue());
		m_playerContent.setLessonQuestion(slide.getLessonQuestion().getValue());

		if(slide.getTextAnswerFlag().getBoolValue())
		{
			m_playerContent.setAnswers(slide.getTextAnswers());
		}
	}
	
	public boolean nextSlide() throws Exception
	{
		if(m_slides.size() == 0)
		{
			throw new Exception("No slides after update");
		}
		else
		{
			if(m_slideIndex == -1)
			{
				m_slideIndex = 0;
			}
			else if(m_slideIndex < m_slides.size() - 1)
			{
				m_slideIndex++;
			}
			else
			{
				throw new Exception("Choose slide out of bound");
			}

			updateSlide();

			this.pack();
			// Return if there's next slide
			return (m_slideIndex < m_slides.size() - 1);
		}
	}

	public boolean prevSlide() throws Exception
	{
		if(m_slides.size() == 0)
		{
			throw new Exception("No slides after update");
		}
		else
		{
			if(m_slideIndex > 0)
			{
				m_slideIndex--;
			}
			else
			{
				throw new Exception("Choose slide out of bound");
			}

			updateSlide();

			this.pack();
			// Return if there's next slide
			return (m_slideIndex > 0);
		}
	}

	@Override
	public void fireUpdatedEvent() throws Exception
	{
		if(nextSlide())
		{
			m_controlls.setControllsEnabled(false, true);
		}
	}

	@Override
	public void performSave() throws Exception
	{
	}
}
