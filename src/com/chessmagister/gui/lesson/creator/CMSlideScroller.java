package com.chessmagister.gui.lesson.creator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMXmlComplexNode;
import com.chessmagister.io.CMXmlConstants;
import com.chessmagister.io.CMXmlListNode;
import com.chessmagister.utils.CMConstants;

public class CMSlideScroller extends JScrollPane implements ActionListener, CMSerializableComplex
{
	protected JPanel m_slidesPanel = null;
	protected CMSlide m_selected = null;
	public CMBoardEditor m_boardEditor;
	@CMXmlListNode(	id = CMXmlConstants.SLIDE_LIST_ID,
					elementId = CMXmlConstants.SLIDE_ID,
					itemClass = CMSlide.class)
	public List<CMSlide> m_slides = new Vector<CMSlide>();

	public CMSlideScroller(JPanel slidesPanel, CMBoardEditor boardEditor) throws Exception
	{
		super(slidesPanel);
		m_slidesPanel = slidesPanel;
		m_boardEditor = boardEditor;

		// At least one slide must present
		addSlide();
	}

	public void addSlide() throws Exception
	{
		if(m_selected != null)
		{
			m_selected.save();
			m_selected.unSelect();
		}

		CMSlide newSlide;
		m_selected = newSlide = new CMSlide(m_boardEditor);
		newSlide.setSelected(true);
		newSlide.addActionListener(this);
		newSlide.setBounds(0, 0, CMConstants.CELL_SIZE, CMConstants.CELL_SIZE);
		m_slidesPanel.add(newSlide);
		m_slides.add(newSlide);
		m_slidesPanel.repaint();
		this.repaint();
		this.validate();
		
		m_selected.load();
		m_selected.save();
		m_selected.select();
	}

	public void removeSlide() throws Exception
	{
		if(m_slides.size() > 1)
		{
			m_slidesPanel.remove(m_selected);
			int removedIndex = m_slides.indexOf(m_selected);
			m_slides.remove(m_selected);
			m_slidesPanel.repaint();
			this.repaint();
			this.validate();

			// Select another slide
			if(removedIndex + 1 > m_slides.size())
			{
				m_selected = m_slides.get(m_slides.size() - 1);
			}
			else
			{
				m_selected = m_slides.get(removedIndex);
			}

			m_selected.save();
			m_selected.select();
		}
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		try {
			m_selected.unSelect();
			m_selected = (CMSlide)event.getSource();
			m_selected.select();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getID()
	{
		return CMXmlConstants.SLIDES_SCROLLER_ID;
	}

	@Override
	public void fireUpdatedEvent() throws Exception
	{
		if(m_slides.size() == 0)
		{
			throw new Exception("No slides after update");
		}
		
		// Erase scroll panel
		m_slidesPanel.removeAll();
		
		for(CMSlide slide : m_slides)
		{
			slide.setBoardEditor(m_boardEditor);
			slide.addActionListener(this);
			slide.setBounds(0, 0, CMConstants.CELL_SIZE, CMConstants.CELL_SIZE);
			m_slidesPanel.add(slide);
		}
		m_selected = m_slides.get(0);
		m_selected.load();
		m_selected.setSelected(true);
		m_slidesPanel.repaint();
		this.repaint();
		this.validate();
	}

	@Override
	public void performSave() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
