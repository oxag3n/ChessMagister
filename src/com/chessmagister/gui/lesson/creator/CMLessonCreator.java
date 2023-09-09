package com.chessmagister.gui.lesson.creator;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.chessmagister.gui.lesson.common.CMChessBoard;
import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.io.CMXmlComplexNode;
import com.chessmagister.io.CMXmlIo;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
public class CMLessonCreator extends JInternalFrame implements CMSerializableComplex
{
	private static final long serialVersionUID = 1L;

	protected CMChessBoard m_chessBoard = null;
	protected CMBoardEditor m_boardEditor;
	@CMXmlComplexNode public CMSlideEditor m_slideEditor;
	
	protected String m_filePath = null;

	protected void init() throws Exception
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.weightx = 0.5;
		c.weighty = 1.00;
		c.gridx = 0;
		c.gridy = 0;
		this.add(m_chessBoard = new CMChessBoard(), c);

		c.weightx = 0.5;
		c.weighty = 1.00;
		c.gridx = 1;
		c.gridy = 0;
		this.add(m_boardEditor = new CMBoardEditor(m_chessBoard), c);

		c.weightx = 0.0;
		c.gridwidth = 2;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		this.add(m_slideEditor = new CMSlideEditor(m_boardEditor), c);
	}
	
	public CMLessonCreator(String filePath) throws Exception
	{
		super("Lesson Creator - " + filePath, true, true, true, true);
		m_filePath = filePath;
		init();
	}
	
	public CMLessonCreator() throws Exception
	{
		super("Lesson Creator", true, true, true, true);
		init();
	}

	public void save() throws Exception
	{
		if(m_filePath == null)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		    int returnVal = chooser.showSaveDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       System.out.println("You chose to open this file: " +
		            chooser.getSelectedFile().getPath());
		       CMXmlIo.writeToXml(this, chooser.getSelectedFile().getPath());
		       this.setTitle("Lesson Creator - " + chooser.getSelectedFile().getName());
		       m_filePath = chooser.getSelectedFile().getPath();
		    }
		}
		else
		{
			CMXmlIo.writeToXml(this, m_filePath);
		}
	}

	@Override
	public String getID()
	{
		return CMXmlConstants.LESSON_ID;
	}
	
	

	@Override
	public void fireUpdatedEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performSave() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
