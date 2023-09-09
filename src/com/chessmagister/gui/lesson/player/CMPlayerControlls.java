package com.chessmagister.gui.lesson.player;

import java.awt.Button;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JPanel;

import com.chessmagister.io.CMSerializableComplex;
import com.chessmagister.io.CMXmlComplexNode;
import com.chessmagister.io.CMXmlConstants;

public class CMPlayerControlls extends JPanel implements CMSerializableComplex 
{
	protected JPanel slidesPanel = null;
	protected String m_filePath = null;
	protected CMLessonPlayer m_player = null;
	protected Button m_backButton = null;
	protected Button m_nextButton = null;

//	@CMXmlComplexNode public CMSlideScroller m_slideScroller = null;

	public void setControllsEnabled(boolean back, boolean next)
	{
		m_backButton.setEnabled(back);
		m_nextButton.setEnabled(next);
	}

	public CMPlayerControlls(CMLessonPlayer player) throws Exception
	{
		this.m_player = player;
		this.setLayout(new GridBagLayout());

		JPanel menu = new JPanel();

		GridBagConstraints c = new GridBagConstraints();
//		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 1;

		c.gridx = 0;
		c.gridy = 0;
		this.add(menu, c);

		m_backButton = new Button("Back");
		m_backButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(!m_player.prevSlide())
					{
						m_backButton.setEnabled(false);
					}

					m_nextButton.setEnabled(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		menu.add(m_backButton);

		m_nextButton = new Button("Next");
		m_nextButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					if(!m_player.nextSlide())
					{
						m_nextButton.setEnabled(false);
					}

					m_backButton.setEnabled(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		menu.add(m_nextButton);

		setControllsEnabled(false, false);
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