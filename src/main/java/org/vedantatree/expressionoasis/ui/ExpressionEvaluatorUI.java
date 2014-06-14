/**	
 *  Copyright (c) 2005-2014 VedantaTree all rights reserved.
 * 
 *  This file is part of ExpressionOasis.
 *
 *  ExpressionOasis is free software. You can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ExpressionOasis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT SHALL 
 *  THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES 
 *  OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 *  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE 
 *  OR OTHER DEALINGS IN THE SOFTWARE.See the GNU Lesser General Public License 
 *  for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with ExpressionOasis. If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Please consider to contribute any enhancements to upstream codebase. 
 *  It will help the community in getting improved code and features, and 
 *  may help you to get the later releases with your changes.
 */
package org.vedantatree.expressionoasis.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * 
 * @author
 * 
 *         This class will draw the Calculator and will call a method in the
 *         calculator class for logic and calculations.
 * 
 */
public class ExpressionEvaluatorUI implements ActionListener, IExpressionEvaluator
{

	private ExpressionEvaluator	calc				= null;
	private JFrame				calcFrame			= new JFrame( EXPRESSIONEVALUATOR );
	private JPanel[]			rows				= new JPanel[9];

	/*
	 * Text Box for entering expression
	 */
	private JTextField			expressionText		= new JTextField();
	private JLabel				expressionLabel		= new JLabel( EXPRESSIONLABEL );

	/*
	 * Text Box for showing result
	 */
	private JTextField			result				= new JTextField();
	private JLabel				resultLabel			= new JLabel( RESULTLABEL );

	/*
	 * Numeric Keys
	 */
	private JButton[]			numericKeys			= new JButton[10];

	/**
	 * Buttons for functions
	 */
	private JButton				addKey				= new JButton( "    " + PLUS + "    " );
	private JButton				minusKey			= new JButton( "    " + MINUS + "    " );
	private JButton				multiplyKey			= new JButton( "    " + MULTIPLY + "    " );
	private JButton				divideKey			= new JButton( "    " + DIVISION + "    " );
	private JButton				equalsKey			= new JButton( "             " + EQUALS + "              " );
	private JButton				resetKey			= new JButton( "    " + CLEAR + "    " );
	private JButton				fractionKey			= new JButton( "    " + FRACTION + "    " );

	/**
	 * Direct Functions calling buttons
	 */
	private JButton				absKey				= new JButton( " " + ABS + " " );
	private JButton				sinKey				= new JButton( "  " + SIN + "   " );
	private JButton				cosKey				= new JButton( " " + COS + " " );
	private JButton				tanKey				= new JButton( "  " + TAN + " " );
	private JButton				asinKey				= new JButton( "  " + ASIN + " " );
	private JButton				acosKey				= new JButton( "  " + ACOS + " " );
	private JButton				atanKey				= new JButton( " " + ATAN + "   " );
	private JButton				atan2Key			= new JButton( ATAN2 );
	private JButton				expKey				= new JButton( " " + EXP + " " );
	private JButton				logKey				= new JButton( "  " + LOG + "   " );
	private JButton				sqrtKey				= new JButton( " " + SQRT + " " );
	private JButton				ceilKey				= new JButton( "  " + CEIL + "  " );
	private JButton				floorKey			= new JButton( "  " + FLOOR + "  " );
	private JButton				rintKey				= new JButton( "  " + RINT + "   " );
	private JButton				roundKey			= new JButton( " " + ROUND + " " );
	private JButton				randomKey			= new JButton( RANDOM );

	/**
	 * Evaluate to expression first and than call to evaluation
	 */
	private JButton				leftShiftKey		= new JButton( "  " + SIGNEDLEFTSHIFT + " " );
	private JButton				rightShiftKey		= new JButton( "  " + SIGNEDRIGHTSHIFT + " " );
	private JButton				unRightShiftKey		= new JButton( "  " + UNSIGNEDRIGHTSHIFT + " " );
	private JButton				bitORKey			= new JButton( "     " + BITWISEOR + "      " );
	private JButton				bitANDKey			= new JButton( "    " + BITWISEAND + "     " );
	private JButton				bitComplementKey	= new JButton( "     " + BITWISECOMPLEMENT + "     " );
	private JButton				greaterEqKey		= new JButton( "     " + GREATERTHANEQUALTO + "      " );
	private JButton				greaterKey			= new JButton( "   " + GREATERTHAN + "  " );
	private JButton				lessEqKey			= new JButton( "  " + LESSTHANEQUALTO + "  " );
	private JButton				lessKey				= new JButton( "    " + LESSTHAN + "   " );
	private JButton				notEqKey			= new JButton( "    " + NOTEQUALTO + "    " );
	private JButton				equalsToKey			= new JButton( "    " + EQUALTO + "    " );
	private JButton				leftBraceKey		= new JButton( "    " + LEFTBRACE + "    " );
	private JButton				rightBraceKey		= new JButton( "    " + RIGHTBRACE + "    " );

	public void drawCalculator()
	{
		calc = new ExpressionEvaluator();
		calcFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		JPanel calculatorPane = (JPanel) calcFrame.getContentPane();

		/*
		 * Craeting the panels for the rows
		 */
		for( int i = 0; i < rows.length; i++ )
		{
			rows[i] = new JPanel();
		}

		/*
		 * Creating the Numeric buttons for entering numeric values
		 */
		for( int i = 0; i < numericKeys.length; i++ )
		{
			numericKeys[i] = new JButton( "     " + i + "     " );
			numericKeys[i].setActionCommand( String.valueOf( i ) );
			numericKeys[i].addActionListener( this );
		}

		/*
		 * Settings for result text box
		 */
		result.setColumns( 20 );
		result.setText( "" );
		result.setHorizontalAlignment( JTextField.RIGHT );
		result.setEditable( false );
		resultLabel.setHorizontalAlignment( JLabel.RIGHT );
		rows[0].add( resultLabel );
		rows[0].add( result );

		/*
		 * Settings for expression text box
		 */
		expressionText.setColumns( 20 );
		expressionText.setText( "" );
		expressionText.setHorizontalAlignment( JTextField.RIGHT );
		expressionLabel.setHorizontalAlignment( JLabel.RIGHT );
		rows[1].add( expressionLabel );
		rows[1].add( expressionText );

		/*
		 * Settings for the buttons
		 */

		rows[2].setLayout( new FlowLayout( FlowLayout.LEFT ) );
		rows[2].add( numericKeys[7] );
		rows[2].add( numericKeys[8] );
		rows[2].add( numericKeys[9] );
		rows[2].add( divideKey );
		rows[2].add( multiplyKey );
		rows[2].add( minusKey );
		rows[2].add( resetKey );
		divideKey.setActionCommand( DIVISION );
		divideKey.addActionListener( this );
		multiplyKey.setActionCommand( MULTIPLY );
		multiplyKey.addActionListener( this );
		minusKey.setActionCommand( MINUS );
		minusKey.addActionListener( this );
		resetKey.setActionCommand( RESET );
		resetKey.addActionListener( this );

		rows[3].setLayout( new FlowLayout( FlowLayout.LEFT ) );
		rows[3].add( numericKeys[4] );
		rows[3].add( numericKeys[5] );
		rows[3].add( numericKeys[6] );
		rows[3].add( addKey );
		rows[3].add( fractionKey );
		rows[3].add( absKey );
		rows[3].add( logKey );
		addKey.setActionCommand( PLUS );
		addKey.addActionListener( this );
		fractionKey.setActionCommand( FRACTION );
		fractionKey.addActionListener( this );
		absKey.setActionCommand( ABS );
		absKey.addActionListener( this );
		logKey.setActionCommand( LOG );
		logKey.addActionListener( this );

		rows[4].setLayout( new FlowLayout( FlowLayout.LEFT ) );
		rows[4].add( numericKeys[1] );
		rows[4].add( numericKeys[2] );
		rows[4].add( numericKeys[3] );
		rows[4].add( sinKey );
		rows[4].add( cosKey );
		rows[4].add( tanKey );
		rows[4].add( asinKey );
		sinKey.setActionCommand( SIN );
		sinKey.addActionListener( this );
		cosKey.setActionCommand( COS );
		cosKey.addActionListener( this );
		tanKey.setActionCommand( TAN );
		tanKey.addActionListener( this );
		asinKey.setActionCommand( ASIN );
		asinKey.addActionListener( this );

		rows[5].setLayout( new FlowLayout( FlowLayout.LEFT ) );
		rows[5].add( numericKeys[0] );
		rows[5].add( acosKey );
		rows[5].add( atanKey );
		rows[5].add( atan2Key );
		rows[5].add( expKey );
		rows[5].add( sqrtKey );
		rows[5].add( ceilKey );
		acosKey.setActionCommand( ACOS );
		acosKey.addActionListener( this );
		atanKey.setActionCommand( ATAN );
		atanKey.addActionListener( this );
		atan2Key.setActionCommand( ATAN2 );
		atan2Key.addActionListener( this );
		expKey.setActionCommand( EXP );
		expKey.addActionListener( this );
		sqrtKey.setActionCommand( SQRT );
		sqrtKey.addActionListener( this );
		ceilKey.setActionCommand( CEIL );
		ceilKey.addActionListener( this );

		rows[6].setLayout( new FlowLayout( FlowLayout.LEFT ) );
		rows[6].add( floorKey );
		rows[6].add( rintKey );
		rows[6].add( roundKey );
		rows[6].add( randomKey );
		rows[6].add( leftShiftKey );
		rows[6].add( rightShiftKey );
		rows[6].add( unRightShiftKey );
		floorKey.setActionCommand( FLOOR );
		floorKey.addActionListener( this );
		rintKey.setActionCommand( RINT );
		rintKey.addActionListener( this );
		roundKey.setActionCommand( ROUND );
		roundKey.addActionListener( this );
		randomKey.setActionCommand( RANDOM );
		randomKey.addActionListener( this );
		leftShiftKey.setActionCommand( SIGNEDLEFTSHIFT );
		leftShiftKey.addActionListener( this );
		rightShiftKey.setActionCommand( SIGNEDRIGHTSHIFT );
		rightShiftKey.addActionListener( this );
		unRightShiftKey.setActionCommand( UNSIGNEDRIGHTSHIFT );
		unRightShiftKey.addActionListener( this );

		rows[7].setLayout( new FlowLayout( FlowLayout.LEFT ) );
		rows[7].add( bitORKey );
		rows[7].add( bitANDKey );
		rows[7].add( bitComplementKey );
		rows[7].add( greaterEqKey );
		rows[7].add( greaterKey );
		rows[7].add( lessEqKey );
		rows[7].add( lessKey );
		bitORKey.setActionCommand( BITWISEOR );
		bitORKey.addActionListener( this );
		bitANDKey.setActionCommand( BITWISEAND );
		bitANDKey.addActionListener( this );
		bitComplementKey.setActionCommand( BITWISECOMPLEMENT );
		bitComplementKey.addActionListener( this );
		greaterEqKey.setActionCommand( GREATERTHANEQUALTO );
		greaterEqKey.addActionListener( this );
		greaterKey.setActionCommand( GREATERTHAN );
		greaterKey.addActionListener( this );
		lessEqKey.setActionCommand( LESSTHANEQUALTO );
		lessEqKey.addActionListener( this );
		lessKey.setActionCommand( LESSTHAN );
		lessKey.addActionListener( this );

		rows[8].setLayout( new FlowLayout( FlowLayout.LEFT ) );
		rows[8].add( notEqKey );
		rows[8].add( equalsToKey );
		rows[8].add( leftBraceKey );
		rows[8].add( rightBraceKey );
		rows[8].add( equalsKey );
		notEqKey.setActionCommand( NOTEQUALTO );
		notEqKey.addActionListener( this );
		equalsToKey.setActionCommand( EQUALTO );
		equalsToKey.addActionListener( this );
		leftBraceKey.setActionCommand( LEFTBRACE );
		leftBraceKey.addActionListener( this );
		rightBraceKey.setActionCommand( RIGHTBRACE );
		rightBraceKey.addActionListener( this );
		equalsKey.setActionCommand( EQUALS );
		equalsKey.addActionListener( this );
		calculatorPane.setLayout( new BoxLayout( calculatorPane, BoxLayout.Y_AXIS ) );
		for( JPanel jPanel : rows )
		{
			calculatorPane.add( jPanel );
		}

		calcFrame.pack();
		calcFrame.setVisible( true );
	}

	public void actionPerformed( ActionEvent e )
	{
		String actionCommand = e.getActionCommand();
		if( actionCommand == null || actionCommand.trim().length() <= 0 )
		{
			return;
		}

		int number = -1;
		try
		{
			number = Integer.parseInt( actionCommand );
		}
		catch( NumberFormatException e1 )
		{
		}
		if( number >= 0 )
		{
			expressionText.setText( expressionText.getText() + number );
			result.setText( "" );
		}
		else if( actionCommand.equals( RESET ) )
		{
			expressionText.setText( "" );
			result.setText( "" );
		}
		else if( actionCommand.equals( DIVISION ) )
		{
			expressionText.setText( expressionText.getText() + DIVISION );
		}
		else if( actionCommand.equals( MULTIPLY ) )
		{
			expressionText.setText( expressionText.getText() + MULTIPLY );
		}
		else if( actionCommand.equals( MINUS ) )
		{
			expressionText.setText( expressionText.getText() + MINUS );
		}
		else if( actionCommand.equals( PLUS ) )
		{
			expressionText.setText( expressionText.getText() + PLUS );
		}
		else if( actionCommand.equals( SIGNEDLEFTSHIFT ) )
		{
			expressionText.setText( expressionText.getText() + SIGNEDLEFTSHIFT );
		}
		else if( actionCommand.equals( SIGNEDRIGHTSHIFT ) )
		{
			expressionText.setText( expressionText.getText() + SIGNEDRIGHTSHIFT );
		}
		else if( actionCommand.equals( UNSIGNEDRIGHTSHIFT ) )
		{
			expressionText.setText( expressionText.getText() + UNSIGNEDRIGHTSHIFT );
		}
		else if( actionCommand.equals( BITWISEOR ) )
		{
			expressionText.setText( expressionText.getText() + BITWISEOR );
		}
		else if( actionCommand.equals( BITWISEAND ) )
		{
			expressionText.setText( expressionText.getText() + BITWISEAND );
		}
		else if( actionCommand.equals( BITWISECOMPLEMENT ) )
		{
			expressionText.setText( expressionText.getText() + BITWISECOMPLEMENT );
		}
		else if( actionCommand.equals( GREATERTHANEQUALTO ) )
		{
			expressionText.setText( expressionText.getText() + GREATERTHANEQUALTO );
		}
		else if( actionCommand.equals( GREATERTHAN ) )
		{
			expressionText.setText( expressionText.getText() + GREATERTHAN );
		}
		else if( actionCommand.equals( LESSTHANEQUALTO ) )
		{
			expressionText.setText( expressionText.getText() + LESSTHANEQUALTO );
		}
		else if( actionCommand.equals( LESSTHAN ) )
		{
			expressionText.setText( expressionText.getText() + LESSTHAN );
		}
		else if( actionCommand.equals( NOTEQUALTO ) )
		{
			expressionText.setText( expressionText.getText() + NOTEQUALTO );
		}
		else if( actionCommand.equals( EQUALTO ) )
		{
			expressionText.setText( expressionText.getText() + EQUALTO );
		}
		else if( actionCommand.equals( LEFTBRACE ) )
		{
			expressionText.setText( expressionText.getText() + LEFTBRACE );
		}
		else if( actionCommand.equals( RIGHTBRACE ) )
		{
			expressionText.setText( expressionText.getText() + RIGHTBRACE );
		}
		else if( actionCommand.equals( FRACTION ) )
		{
			expressionText.setText( expressionText.getText() + FRACTION );
		}
		else if( expressionText.getText() != null && expressionText.getText().trim().length() > 0 )
		{
			String expression = "";
			if( actionCommand.equals( EQUALS ) )
			{
				expression = expressionText.getText();
			}
			else if( actionCommand.equals( ABS ) )
			{
				expression = ABS + LEFTBRACE + expressionText.getText() + RIGHTBRACE;
			}
			else if( actionCommand.equals( SIN ) )
			{
				expression = SIN + LEFTBRACE + expressionText.getText() + RIGHTBRACE;
			}
			else if( actionCommand.equals( COS ) )
			{
				expression = COS + LEFTBRACE + expressionText.getText() + RIGHTBRACE;
			}
			else if( actionCommand.equals( TAN ) )
			{
				expression = TAN + LEFTBRACE + expressionText.getText() + RIGHTBRACE;
			}
			else if( actionCommand.equals( ASIN ) )
			{
				expression = ASIN + LEFTBRACE + expressionText.getText() + RIGHTBRACE;
			}
			else if( actionCommand.equals( ACOS ) )
			{
				expression = ACOS + LEFTBRACE + expressionText.getText() + RIGHTBRACE;
			}
			else if( actionCommand.equals( ATAN ) )
			{
				expression = ATAN + LEFTBRACE + expressionText.getText() + RIGHTBRACE;
			}
			else if( actionCommand.equals( ATAN2 ) )
			{
				expression = ATAN2 + LEFTBRACE + expressionText.getText() + RIGHTBRACE;
			}
			else if( actionCommand.equals( EXP ) )
			{
				expression = EXP + LEFTBRACE + expressionText.getText() + RIGHTBRACE;
			}
			else if( actionCommand.equals( LOG ) )
			{
				expression = LOG + LEFTBRACE + expressionText.getText() + RIGHTBRACE;
			}
			else if( actionCommand.equals( SQRT ) )
			{
				expression = SQRT + LEFTBRACE + expressionText.getText() + RIGHTBRACE;
			}
			else if( actionCommand.equals( CEIL ) )
			{
				expression = CEIL + LEFTBRACE + expressionText.getText() + RIGHTBRACE;
			}
			else if( actionCommand.equals( FLOOR ) )
			{
				expression = FLOOR + LEFTBRACE + expressionText.getText() + RIGHTBRACE;
			}
			else if( actionCommand.equals( RINT ) )
			{
				expression = RINT + LEFTBRACE + expressionText.getText() + RIGHTBRACE;
			}
			else if( actionCommand.equals( ROUND ) )
			{
				expression = ROUND + LEFTBRACE + expressionText.getText() + RIGHTBRACE;
			}
			result.setText( "" + ( calc.getResult( expression ) ) );
			expressionText.setText( "" );
		}
		else if( actionCommand.equals( RANDOM ) )
		{
			result.setText( "" + ( calc.getResult( RANDOM + LEFTBRACE + expressionText.getText() + RIGHTBRACE ) ) );
			expressionText.setText( "" );
		}
	}

	public static void main( String[] args )
	{
		ExpressionEvaluatorUI sc = new ExpressionEvaluatorUI();
		sc.drawCalculator();
	}
}