import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;
import java.lang.Math;

/**
 * Main is _the_ main part of the program. It is where the frame is defined and
 * all of the buttons are assigned. From here the string containing the
 * equation is passed to the Interpreter.
 *
 * @author Michael Ranciglio
 */
public class Main implements Runnable
{
	public static JTextPane display;

	public static String displayString;

	public static final double e = Math.E; //e should be lowercase if you ask me

	/**
	 * The run function is part of the Runnable implementation we're going off
	 * of. This is sort of like a constructor. We will set the stage for the
	 * GUI right here.
	 *
	 * It constructs the grid buttons and display screen.
	 *
	 * @author Michael Ranciglio
	 */
	@Override //override run
    public void run()
	{
        //create the window itself
        JFrame f = new JFrame("sciCalc");

		//sets the behavior for when the window is closed
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //add a layout manager so that the button is not placed on top of the label
		f.setLayout(new FlowLayout());

		JPanel v = new JPanel();
		v.setLayout(new BoxLayout(v, BoxLayout.Y_AXIS));

		//horizontal panel
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(0,6));

		char sqr = (char)8730;

		JButton sin = new JButton("sin(");
		sin.addActionListener(new GenericActionListener("sin("));
		JButton cos = new JButton("cos(");
		cos.addActionListener(new GenericActionListener("cos("));
		JButton tan = new JButton("tan(");
		tan.addActionListener(new GenericActionListener("tan("));
		JButton pi = new JButton("π");
		pi.addActionListener(new GenericActionListener("π"));
		JButton e = new JButton("e");
		e.addActionListener(new GenericActionListener("e"));
		JButton fact = new JButton("fact(");
		fact.addActionListener(new GenericActionListener("fact("));
		JButton ln = new JButton("ln(");
		ln.addActionListener(new GenericActionListener("ln("));
		JButton log = new JButton("log(");
		log.addActionListener(new GenericActionListener("log("));
		JButton sqroot = new JButton(Character.toString(sqr) + "(");
		sqroot.addActionListener(new GenericActionListener(Character.toString(sqr) + "("));
		JButton pow = new JButton("^");
		pow.addActionListener(new GenericActionListener("^"));
		JButton div = new JButton("/");
		div.addActionListener(new GenericActionListener(" / "));
		JButton mult = new JButton("*");
		mult.addActionListener(new GenericActionListener(" * "));
		JButton sub = new JButton("-");
		sub.addActionListener(new GenericActionListener(" - "));
		JButton add = new JButton("+");
		add.addActionListener(new GenericActionListener(" + "));
		JButton seven = new JButton("7");
		seven.addActionListener(new GenericActionListener("7"));
		JButton four = new JButton("4");
		four.addActionListener(new GenericActionListener("4"));
		JButton one = new JButton("1");
		one.addActionListener(new GenericActionListener("1"));
		JButton period = new JButton(".");
		period.addActionListener(new GenericActionListener("."));
		JButton eight = new JButton("8");
		eight.addActionListener(new GenericActionListener("8"));
		JButton five = new JButton("5");
		five.addActionListener(new GenericActionListener("5"));
		JButton two = new JButton("2");
		two.addActionListener(new GenericActionListener("2"));
		JButton zero = new JButton("0");
		zero.addActionListener(new GenericActionListener("0"));
		JButton nine = new JButton("9");
		nine.addActionListener(new GenericActionListener("9"));
		JButton six = new JButton("6");
		six.addActionListener(new GenericActionListener("6"));
		JButton three = new JButton("3");
		three.addActionListener(new GenericActionListener("3"));
		JButton open = new JButton("(");
		open.addActionListener(new GenericActionListener("("));
		JButton close = new JButton(")");
		close.addActionListener(new GenericActionListener(")"));
		JButton mod = new JButton("%");
		mod.addActionListener(new GenericActionListener(" % "));
		JButton neg = new JButton("(-)");
		neg.addActionListener(new GenericActionListener("-"));

		JButton clear = new JButton("C");
		clear.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				emptyDisplay();
			}
		});

		JButton enter = new JButton("=");
		enter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Interpreter i = new Interpreter();
				String resultString = new String(i.interpret(displayString));
				emptyDisplay();
				display.setText(resultString);
			}
		});

		p.add(sin); p.add(fact); p.add(open); p.add(close); p.add(mod); p.add(clear);
		p.add(cos); p.add(ln); p.add(seven); p.add(eight); p.add(nine); p.add(div);
		p.add(tan); p.add(log); p.add(four); p.add(five); p.add(six); p.add(mult);
		p.add(pi); p.add(sqroot); p.add(one); p.add(two); p.add(three); p.add(sub);
		p.add(e); p.add(pow); p.add(zero); p.add(period); p.add(enter); p.add(add);
		p.add(neg);

		display = new JTextPane(); //allocate mem for the text pane
		display.setEditable(false); //make it not editable

		displayString = new String(); //allocate mem for the string of the text pane
		emptyDisplay(); //ensure display is empty

		v.add(display);
		v.add(p);

		f.add(v);

		//arrange the components inside the window
        f.pack();
        //by default, the window is not visible. Make it visible.
        f.setVisible(true);
    }

	/**
	 * emptyDisplay does exactly what it sounds like it does. It empties the
	 * display and sets the current display value to nothing.
	 *
	 * @author Michael Ranciglio
	 */
	public static void emptyDisplay()
	{
		displayString = "";
		display.setText(displayString);
	}

	/**
	 * addToDisplay updates the display with the string by appending it to our
	 * current string value and updating our display to match that.
	 *
	 * @author Micheal Ranciglio
	 */
	public static void addToDisplay(String add)
	{
		displayString += add;
		display.setText(displayString);
	}

	/**
	 * main is our main function. It starts the program and everything else is
	 * event driven from within the code of the Main frame we create.
	 *
	 * @author Michael Ranciglio
	 */
    public static void main(String[] args)
	{
        Main se = new Main();
        // Schedules the application to be run at the correct time in the event queue.
        SwingUtilities.invokeLater(se);
    }

	/**
	 * GenericActionListener is an easy way for me to set up actions for my
	 * buttons in one line. It takes the string as a constructor argument
	 * and uses that to determine what it adds to the displayString each time.
	 *
	 * @author Michael Ranciglio
	 */
	static class GenericActionListener implements ActionListener
	{
		String str;

		public GenericActionListener(String str)
		{
			this.str = str;
		}

		public void actionPerformed(ActionEvent e)
		{
			addToDisplay(str);
		}
	}
}
