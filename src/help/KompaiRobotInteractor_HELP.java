package help;
/*
 * @author mcs10smn
 * Department of computing science
 */




import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import aimaResources.AimaConstants;


// TODO: Auto-generated Javadoc
/**
 * The Class KompaiRobotInteractor.
 */
public class KompaiRobotInteractor_HELP extends JPanel implements ActionListener
{
	    
		private static final long serialVersionUID = 1L;
    	protected JButton stop_button, avoid_obstacle_button;
    	protected JButton forward_button, backward_button, 
    					right_button, left_button;
		JPanel base_panel;
		JPanel navigator_panel;
		
		double currentRoboXPos=0.0,currentRoboYPos=0.0;
		public static boolean isRobotMoving=false;
		
		Directions directionsObject=new Directions();
		
	    /**
    	 * Instantiates a new kompai robot interactor.
    	 */
    	public KompaiRobotInteractor_HELP() 
	    {
	    	
	        stop_button = new JButton("STOP KOMPAI ROBOT");
	        stop_button.setVerticalTextPosition(AbstractButton.BOTTOM);
	        stop_button.setHorizontalTextPosition(AbstractButton.CENTER);
	        stop_button.setMnemonic(KeyEvent.VK_S);
	        stop_button.setActionCommand(AimaConstants.stop_button_command);
	        
	        avoid_obstacle_button = new JButton("AVOID OBSTACLE");
	        avoid_obstacle_button.setVerticalTextPosition(AbstractButton.BOTTOM);
	        avoid_obstacle_button.setHorizontalTextPosition(AbstractButton.CENTER);
	        avoid_obstacle_button.setActionCommand("obstacle");

	        //Listen for actions on buttons 1 and 2.
	        stop_button.addActionListener(this);
	        avoid_obstacle_button.addActionListener(this);

	        stop_button.setToolTipText("Click this button to " +
	        		"stop Kompai Robot.");

	        base_panel=new JPanel(new GridLayout(1,1));
	  	  	base_panel.add(stop_button);
	  	  	base_panel.add(avoid_obstacle_button);
	  	  	
	  	    forward_button = new JButton("FORWARD");	  
	  	    forward_button.setActionCommand(AimaConstants.forward_button_command);
	  	    backward_button = new JButton("BACKWARD");
	  	    backward_button.setActionCommand(AimaConstants.backward_button_command);
	  	    right_button = new JButton("RIGHT");
	  	    right_button.setActionCommand(AimaConstants.right_button_command);
	  	    left_button = new JButton("LEFT");
	  	  	left_button.setActionCommand(AimaConstants.left_button_command);
	  	    
	  	  	forward_button.addActionListener(this);
	  	  	backward_button.addActionListener(this);
		  	right_button.addActionListener(this);
		  	left_button.addActionListener(this);
	  	    
	  	    navigator_panel=new JPanel();
	  	  	
	  	    navigator_panel.setBorder(new TitledBorder("Directions"));
	  	    navigator_panel.add(forward_button);
	  	    navigator_panel.add(backward_button);
	  	    navigator_panel.add(right_button);
	  	    navigator_panel.add(left_button);
	      
	  	    // add the components to the container
	  	    add(navigator_panel,BorderLayout.CENTER);
	  	    add(base_panel,BorderLayout.SOUTH);
	    }

	    /* (non-Javadoc)
    	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
    	 */
    	public void actionPerformed(ActionEvent e) 
    	{
	    	//System.out.println("actionPerformed");
	    	//System.out.println("e.getActionCommand()"+e.getActionCommand());
	    	if(e.getActionCommand().equalsIgnoreCase(AimaConstants.forward_button_command))
	    	{
	    		directionsObject.moveKompaiForward();
	    	}
	    	else if(e.getActionCommand().equalsIgnoreCase(AimaConstants.backward_button_command))
	    	{
	    		directionsObject.moveKompaiBackward();
	    	}
	    	else if(e.getActionCommand().equalsIgnoreCase(AimaConstants.right_button_command))
	    	{
	    		directionsObject.turnKompaiRight();
	    	}
	    	else if(e.getActionCommand().equalsIgnoreCase(AimaConstants.left_button_command))
	    	{
	    		directionsObject.turnKompaiLeft();
	    	}
	    	else if(e.getActionCommand().equalsIgnoreCase(AimaConstants.stop_button_command))
	    	{
	    		directionsObject.stopForReading();
	    	}
	    	else if(e.getActionCommand().equalsIgnoreCase(AimaConstants.start_button_command))
	    	{
	    		directionsObject.moveKompaiForward();
	    	}
	        
	    }

	    

	    /**
	     * Create the GUI and show it.  For thread safety, 
	     * this method should be invoked from the 
	     * event-dispatching thread.
	     */
	    private static void createAndShowGUI() {

	        //Create and set up the window.
	        JFrame frame = new JFrame("Kompai Robot Interactor");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        //Create and set up the content pane.
	        KompaiRobotInteractor_HELP newContentPane = new KompaiRobotInteractor_HELP();
	        newContentPane.setOpaque(true); //content panes must be opaque
	        frame.setContentPane(newContentPane);

	        //Display the window.
	        frame.pack();
	        frame.setSize(450, 200);
	        frame.setVisible(true);
	    }

	    
    	/**
	     * Display kompai robot panel.
	     */
	    public void displayKompaiPanel() 
	    {
	        //Schedule a job for the event-dispatching thread:
	        //creating and showing this application's GUI.
	        javax.swing.SwingUtilities.invokeLater(new Runnable() 
	        {
	            public void run() 
	            {
	                createAndShowGUI(); 
	            }
	        });
	    }
	    
	}

	

