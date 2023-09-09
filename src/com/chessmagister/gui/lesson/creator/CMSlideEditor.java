package com.chessmagister.gui.lesson.creator;

import java.awt.Button;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import com.chessmagister.utils.CMConstants;

public class CMSlideEditor extends JPanel implements CMSerializableComplex 
{
	protected JPanel slidesPanel = null;
	protected String m_filePath = null;
	
	@CMXmlComplexNode public CMSlideScroller m_slideScroller = null;

	public CMSlideEditor(CMBoardEditor boardEditor) throws Exception
	{
		this.setLayout(new GridBagLayout());

		slidesPanel = new JPanel();
//		slidesPanel.setLayout(new GridLayout(1, 0));
		m_slideScroller = new CMSlideScroller(slidesPanel, boardEditor);
		m_slideScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		JPanel menu = new JPanel();

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 1;

		c.weighty = 0.2;
		c.gridx = 0;
		c.gridy = 0;
		this.add(menu, c);

		c.gridwidth = 2;
		c.weighty = 0.8;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 1;
		this.add(m_slideScroller, c);

		Button addButton = new Button("+");
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					m_slideScroller.addSlide();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		menu.add(addButton);
		
		Button removeButton = new Button("-");
		removeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					m_slideScroller.removeSlide();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		menu.add(removeButton);
		menu.add(new Button("Save"));
	}

	@Override
	public String getID()
	{
		return CMXmlConstants.SLIDE_EDITOR_ID;
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