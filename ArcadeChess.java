/**
 * Quicker version of chess that only goes up to one check
 *
 * date         20201109
 * @filename	ArcadeChess.java
 * @author      jtruong, nmarinkovic, dhoang
 * @version     1.0
 * @see         Assignment 4.3
 */

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Button;
import javax.swing.JTextArea;

public class ArcadeChess extends JFrame{

	private JPanel contentPane;

	JLabel[][] pieces = new JLabel[8][8];
	JLabel[][] background = new JLabel[8][8];
	int[][] pieceType = new int[8][8];

	private final Action action = new SwingAction();


	// https://www.pinclipart.com/pindetail/hRbwim_file-pieces-sprite-wikimedia-chess-pieces-png-clipart/ 
	// https://www.pngkey.com/png/full/816-8162670_light-blue-box-light-blue-overlay-transparent.png 
	// https://cool4dads.com/portfolio-item/grid-gallery/sample_1920_light_red/ https://www.regencychess.co.uk/blog/wp-content/uploads/2012/05/empty-numbered-chess-set.gif 
	// https://www.regencychess.co.uk/blog/wp-content/uploads/2012/05/empty-numbered-chess-set.gif 

	ImageIcon boardI = new ImageIcon("chessboard.gif");
	ImageIcon clearI = new ImageIcon("clear.png");
	ImageIcon lightBI = new ImageIcon("lightblue.png");
	ImageIcon lightRI = new ImageIcon("redBack.png");

	ImageIcon bBishopI = new ImageIcon("blackBishop.png");
	ImageIcon bKingI = new ImageIcon("blackKing.png");
	ImageIcon bQueenI = new ImageIcon("blackQueen.png");
	ImageIcon bRookI = new ImageIcon("blackRook.png");
	ImageIcon bPawnI = new ImageIcon("blackPawn.png");
	ImageIcon bKnightI = new ImageIcon("blackKnight.png");

	ImageIcon wBishopI = new ImageIcon("whiteBishop.png");
	ImageIcon wKingI = new ImageIcon("whiteKing.png");
	ImageIcon wQueenI = new ImageIcon("whiteQueen.png");
	ImageIcon wRookI = new ImageIcon("whiteRook.png");
	ImageIcon wPawnI = new ImageIcon("whitePawn.png");
	ImageIcon wKnightI = new ImageIcon("whiteKnight.png");

	boolean select = false;
	boolean wKingCheck;
	boolean bKingCheck;

	int turn = 1;
	int rows;
	int cols;
	int bWins = 0, wWins = 0;
	private final Action action_1 = new SwingAction_1();
	JTextArea winCount;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ArcadeChess frame = new ArcadeChess();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	class MouseClicks extends MouseAdapter{

		public void mouseClicked(MouseEvent e){
			int mx = e.getX();
			int my = e.getY();
			int bX = 84;
			int bY = 21;
			for(int i = 0; bX + i * 70 < 640; i++){
				for(int o = 0; bY + o * 69 < 577 && bX + i * 70 < 640; o++){
					if(mouseOn(mx, my, bX + i * 70, bY + o * 69, 70, 69) && mx < 640 && my < 577){
						if(!select && turn == pieceType[o][i]%2){
							if(turn == 1){
								if(!wKingCheck){
									rows = i;
									cols = o;
									select = true;
									showMovement(rows, cols);
									clearKings();
								}
							}
							else{
								if(!bKingCheck){
									rows = i;
									cols = o;
									select = true;
									showMovement(rows, cols);
									clearKings();
								}
							}

						}
						else{
							if(background[o][i].getIcon() == lightBI || background[o][i].getIcon() == lightRI){
								pieces[o][i].setIcon(pieces[cols][rows].getIcon());
								pieces[cols][rows].setIcon(clearI);
								pieceType[o][i] = pieceType[cols][rows];
								pieceType[cols][rows] = 0;
								select = false;
								if(turn == 1){
									turn = 0;
								}
								else{
									turn = 1;
								}

							}
							else{
								select = false;
							}
							if((pieceType[o][i] == 1 || pieceType[o][i] == 2) && (o == 7 || o == 0)){
								Object[] options = {"Queen",
										"Rook",
										"Bishop", "Knight"};
								int n = JOptionPane.showOptionDialog(contentPane,
										"What would you like to upgrade your pawn to?",
										"Pawn Upgrade",
										JOptionPane.YES_NO_CANCEL_OPTION,
										JOptionPane.QUESTION_MESSAGE,
										null,
										options,
										options[2]);
								if(pieceType[o][i] == 1){
									if(n == 0){
										pieceType[o][i] = 9;
										pieces[o][i].setIcon(wQueenI);
									}
									else if(n == 1){
										pieceType[o][i] = 7;
										pieces[o][i].setIcon(wRookI);
									}
									else if(n == 2){
										pieceType[o][i] = 5;
										pieces[o][i].setIcon(wBishopI);
									}
									else if(n == 3){
										pieceType[o][i] = 3;
										pieces[o][i].setIcon(wKnightI);
									}
								}
								else{
									if(n == 0){
										pieceType[o][i] = 10;
										pieces[o][i].setIcon(bQueenI);
									}
									else if(n == 1){
										pieceType[o][i] = 8;
										pieces[o][i].setIcon(bRookI);
									}
									else if(n == 2){
										pieceType[o][i] = 6;
										pieces[o][i].setIcon(bBishopI);
									}
									else if(n == 3){
										pieceType[o][i] = 4;
										pieces[o][i].setIcon(bKnightI);
									}
								}

							}
							isCheck();
							if(wKingCheck){
								bWins++;
								clearBack();
								JOptionPane.showMessageDialog(contentPane,
										"Black wins!");
								winCount.setText("Black Wins: " + Integer.toString(bWins) + "		" + "White Wins: " + Integer.toString(wWins));
							}
							if(bKingCheck){
								clearBack();
								wWins++;
								JOptionPane.showMessageDialog(contentPane,
										"White wins!");
								winCount.setText("Black Wins: " + Integer.toString(bWins) + "		" + "White Wins: " + Integer.toString(wWins));
							}
							clearBack();
						}
					}
				}
			}


		}

		private void showMovement(int row, int col){
			//System.out.println(pieceType[col][row]);
			if(isStartingPawn(row,col)){
				if(pieceType[col][row] == 1){
					if(pieceType[col-1][row] == 0){
						background[col-1][row].setIcon(lightBI);
						if(pieceType[col-2][row] == 0){
							background[col-2][row].setIcon(lightBI);
						}
					}
					if(col-1 >= 0 && row-1 >= 0 && pieceType[col-1][row-1] != 0 && pieceType[col-1][row-1] % 2 == 0){
						background[col-1][row-1].setIcon(lightRI);
					}
					if(col-1 >= 0 && row+1 < 8 && pieceType[col-1][row+1] != 0 && pieceType[col-1][row+1] % 2 == 0){
						background[col-1][row+1].setIcon(lightRI);
					}
				}
				else if(pieceType[col][row] == 2){
					if(pieceType[col+1][row] == 0){
						background[col+1][row].setIcon(lightBI);
						if(pieceType[col+2][row] == 0){
							background[col+2][row].setIcon(lightBI);
						}
					}
					if(col+1 < 8 && row - 1 >= 0 && pieceType[col+1][row-1] != 0 && pieceType[col+1][row-1] % 2 == 1){
						background[col+1][row-1].setIcon(lightRI);
					}
					if(col+1 < 8 && row + 1 < 8 && pieceType[col+1][row+1] != 0 && pieceType[col+1][row+1] % 2 == 1){
						background[col+1][row+1].setIcon(lightRI);
					}
				}
			}
			else{
				if(pieceType[col][row] == 1){
					if(col-1 >= 0 && pieceType[col-1][row] == 0){
						background[col-1][row].setIcon(lightBI);
					}
					if(col-1 >= 0 && row-1 >= 0 && pieceType[col-1][row-1] != 0 && pieceType[col-1][row-1] % 2 == 0){
						background[col-1][row-1].setIcon(lightRI);
					}
					if(col-1 >= 0 && row+1 < 8 && pieceType[col-1][row+1] != 0 && pieceType[col-1][row+1] % 2 == 0){
						background[col-1][row+1].setIcon(lightRI);
					}
				}
				else if(pieceType[col][row] == 2){
					if(col+1 < 8 && pieceType[col+1][row] == 0){
						background[col+1][row].setIcon(lightBI);
					}
					if(col+1 < 8 && row - 1 >= 0 && pieceType[col+1][row-1] != 0 && pieceType[col+1][row-1] % 2 == 1){
						background[col+1][row-1].setIcon(lightRI);
					}
					if(col+1 < 8 && row + 1 < 8 && pieceType[col+1][row+1] != 0 && pieceType[col+1][row+1] % 2 == 1){
						background[col+1][row+1].setIcon(lightRI);
					}
				}
				else if(pieceType[col][row] == 3){
					if(col + 2 < 8 && row + 1 < 8 && pieceType[col+2][row+1] == 0){
						background[col+2][row+1].setIcon(lightBI);
					}
					else if(col + 2 < 8 && row + 1 < 8 && pieceType[col+2][row+1] != 0 && pieceType[col+2][row+1] % 2 == 0){
						background[col+2][row+1].setIcon(lightRI);
					}

					if(col + 2 < 8 && row - 1 >= 0 && pieceType[col+2][row-1] == 0){
						background[col+2][row-1].setIcon(lightBI);
					}
					else if(col + 2 < 8 && row - 1 >= 0 && pieceType[col+2][row-1] != 0 && pieceType[col+2][row-1] % 2 == 0){
						background[col+2][row-1].setIcon(lightRI);
					}

					if(col - 2 >= 0 && row - 1 >= 0 && pieceType[col-2][row-1] == 0){
						background[col-2][row-1].setIcon(lightBI);
					}
					else if(col - 2 >= 0 && row - 1 >= 0 && pieceType[col-2][row-1] != 0 && pieceType[col-2][row-1] % 2 == 0){
						background[col-2][row-1].setIcon(lightRI);
					}

					if(col - 2 >= 0 && row + 1 < 8 && pieceType[col-2][row+1] == 0){
						background[col-2][row+1].setIcon(lightBI);
					}
					else if(col - 2 >= 0 && row + 1 < 8 && pieceType[col-2][row+1] != 0 && pieceType[col-2][row+1] % 2 == 0){
						background[col-2][row+1].setIcon(lightRI);
					}

					if(col + 1 < 8 && row - 2 >= 0 && pieceType[col+1][row-2] == 0){
						background[col+1][row-2].setIcon(lightBI);
					}
					else if(col + 1 < 8 && row - 2 >= 0 && pieceType[col+1][row-2] != 0 && pieceType[col+1][row-2] % 2 == 0){
						background[col+1][row-2].setIcon(lightRI);
					}

					if(col - 1 >= 0 && row - 2 >= 0 && pieceType[col-1][row-2] == 0){
						background[col-1][row-2].setIcon(lightBI);
					}
					else if(col - 1 >= 0 && row - 2 >= 0 && pieceType[col-1][row-2] != 0 && pieceType[col-1][row-2] % 2 == 0){
						background[col-1][row-2].setIcon(lightRI);
					}

					if(col + 1 < 8 && row + 2 < 8 && pieceType[col+1][row+2] == 0){
						background[col+1][row+2].setIcon(lightBI);
					}
					else if(col + 1 < 8 && row + 2 < 8 && pieceType[col+1][row+2] != 0 && pieceType[col+1][row+2] % 2 == 0){
						background[col+1][row+2].setIcon(lightRI);
					}

					if(col - 1 >= 0 && row + 2 < 8 && pieceType[col-1][row+2] == 0){
						background[col-1][row+2].setIcon(lightBI);
					}
					else if(col - 1 >= 0 && row + 2 < 8 && pieceType[col-1][row+2] != 0 && pieceType[col-1][row+2] % 2 == 0){
						background[col-1][row+2].setIcon(lightRI);
					}

				}
				else if(pieceType[col][row] == 4){
					if(col + 2 < 8 && row + 1 < 8 && pieceType[col+2][row+1] == 0){
						background[col+2][row+1].setIcon(lightBI);
					}
					else if(col + 2 < 8 && row + 1 < 8 && pieceType[col+2][row+1] != 0 && pieceType[col+2][row+1] % 2 == 1){
						background[col+2][row+1].setIcon(lightRI);
					}

					if(col + 2 < 8 && row - 1 >= 0 && pieceType[col+2][row-1] == 0){
						background[col+2][row-1].setIcon(lightBI);
					}
					else if(col + 2 < 8 && row - 1 >= 0 && pieceType[col+2][row-1] != 0 && pieceType[col+2][row-1] % 2 == 1){
						background[col+2][row-1].setIcon(lightRI);
					}

					if(col - 2 >= 0 && row - 1 >= 0 && pieceType[col-2][row-1] == 0){
						background[col-2][row-1].setIcon(lightBI);
					}
					else if(col - 2 >= 0 && row - 1 >= 0 && pieceType[col-2][row-1] != 0 && pieceType[col-2][row-1] % 2 == 1){
						background[col-2][row-1].setIcon(lightRI);
					}

					if(col - 2 >= 0 && row + 1 < 8 && pieceType[col-2][row+1] == 0){
						background[col-2][row+1].setIcon(lightBI);
					}
					else if(col - 2 >= 0 && row + 1 < 8 && pieceType[col-2][row+1] != 0 && pieceType[col-2][row+1] % 2 == 1){
						background[col-2][row+1].setIcon(lightRI);
					}

					if(col + 1 < 8 && row - 2 >= 0 && pieceType[col+1][row-2] == 0){
						background[col+1][row-2].setIcon(lightBI);
					}
					else if(col + 1 < 8 && row - 2 >= 0 && pieceType[col+1][row-2] != 0 && pieceType[col+1][row-2] % 2 == 1){
						background[col+1][row-2].setIcon(lightRI);
					}

					if(col - 1 >= 0 && row - 2 >= 0 && pieceType[col-1][row-2] == 0){
						background[col-1][row-2].setIcon(lightBI);
					}
					else if(col - 1 >= 0 && row - 2 >= 0 && pieceType[col-1][row-2] != 0 && pieceType[col-1][row-2] % 2 == 1){
						background[col-1][row-2].setIcon(lightRI);
					}

					if(col + 1 < 8 && row + 2 < 8 && pieceType[col+1][row+2] == 0){
						background[col+1][row+2].setIcon(lightBI);
					}
					else if(col + 1 < 8 && row + 2 < 8 && pieceType[col+1][row+2] != 0 && pieceType[col+1][row+2] % 2 == 1){
						background[col+1][row+2].setIcon(lightRI);
					}

					if(col - 1 >= 0 && row + 2 < 8 && pieceType[col-1][row+2] == 0){
						background[col-1][row+2].setIcon(lightBI);
					}
					else if(col - 1 >= 0 && row + 2 < 8 && pieceType[col-1][row+2] != 0 && pieceType[col-1][row+2] % 2 == 1){
						background[col-1][row+2].setIcon(lightRI);
					}

				}
				else if(pieceType[col][row] == 5){

					boolean topL = true;
					boolean topR = true;
					boolean botL = true;
					boolean botR = true;

					for(int a = 1; a < 8; a++){
						if(topL && col - a >= 0 && row - a >= 0 && pieceType[col-a][row-a] == 0){
							background[col-a][row-a].setIcon(lightBI);
						}
						else if(topL && col - a >= 0 && row - a >= 0 && pieceType[col-a][row-a] != 0 && pieceType[col-a][row-a] % 2 == 0){
							background[col-a][row-a].setIcon(lightRI);
							topL = false;
						}
						else if(topL && col - a >= 0 && row - a >= 0 && pieceType[col-a][row-a] != 0){
							topL = false;
						}

						if(topR && col - a >= 0 && row + a < 8 && pieceType[col-a][row+a] == 0){
							background[col-a][row+a].setIcon(lightBI);
						}
						else if(topR && col - a >= 0 && row + a < 8 && pieceType[col-a][row+a] != 0 && pieceType[col-a][row+a] % 2 == 0){
							background[col-a][row+a].setIcon(lightRI);
							topR = false;
						}
						else if(topR && col - a >= 0 && row + a < 8 && pieceType[col-a][row+a] != 0){
							topR = false;
						}

						if(botR && col + a < 8 && row + a < 8 && pieceType[col+a][row+a] == 0){
							background[col+a][row+a].setIcon(lightBI);
						}
						else if(botR && col + a < 8 && row + a < 8 && pieceType[col+a][row+a] != 0 && pieceType[col+a][row+a] % 2 == 0){
							background[col+a][row+a].setIcon(lightRI);
							botR = false;
						}
						else if(botR && col + a < 8 && row + a < 8 && pieceType[col+a][row+a] != 0){
							botR = false;
						}

						if(botL && col + a < 8 && row - a >= 0 && pieceType[col+a][row-a] == 0){
							background[col+a][row-a].setIcon(lightBI);
						}
						else if(botL && col + a < 8 && row - a >= 0 && pieceType[col+a][row-a] != 0 && pieceType[col+a][row-a] % 2 == 0){
							background[col+a][row-a].setIcon(lightRI);
							botL = false;
						}
						else if(botL && col + a < 8 && row - a >= 0 && pieceType[col+a][row-a] != 0){
							botL = false;
						}

					}
				}
				else if(pieceType[col][row] == 6){

					boolean topL = true;
					boolean topR = true;
					boolean botL = true;
					boolean botR = true;

					for(int a = 1; a < 8; a++){
						if(topL && col - a >= 0 && row - a >= 0 && pieceType[col-a][row-a] == 0){
							background[col-a][row-a].setIcon(lightBI);
						}
						else if(topL && col - a >= 0 && row - a >= 0 && pieceType[col-a][row-a] != 0 && pieceType[col-a][row-a] % 2 == 1){
							background[col-a][row-a].setIcon(lightRI);
							topL = false;
						}
						else if(topL && col - a >= 0 && row - a >= 0 && pieceType[col-a][row-a] != 0){
							topL = false;
						}

						if(topR && col - a >= 0 && row + a < 8 && pieceType[col-a][row+a] == 0){
							background[col-a][row+a].setIcon(lightBI);
						}
						else if(topR && col - a >= 0 && row + a < 8 && pieceType[col-a][row+a] != 0 && pieceType[col-a][row+a] % 2 == 1){
							background[col-a][row+a].setIcon(lightRI);
							topR = false;
						}
						else if(topR && col - a >= 0 && row + a < 8 && pieceType[col-a][row+a] != 0){
							topR = false;
						}

						if(botR && col + a < 8 && row + a < 8 && pieceType[col+a][row+a] == 0){
							background[col+a][row+a].setIcon(lightBI);
						}
						else if(botR && col + a < 8 && row + a < 8 && pieceType[col+a][row+a] != 0 && pieceType[col+a][row+a] % 2 == 1){
							background[col+a][row+a].setIcon(lightRI);
							botR = false;
						}
						else if(botR && col + a < 8 && row + a < 8 && pieceType[col+a][row+a] != 0){
							botR = false;
						}

						if(botL && col + a < 8 && row - a >= 0 && pieceType[col+a][row-a] == 0){
							background[col+a][row-a].setIcon(lightBI);
						}
						else if(botL && col + a < 8 && row - a >= 0 && pieceType[col+a][row-a] != 0 && pieceType[col+a][row-a] % 2 == 1){
							background[col+a][row-a].setIcon(lightRI);
							botL = false;
						}
						else if(botL && col + a < 8 && row - a >= 0 && pieceType[col+a][row-a] != 0){
							botL = false;
						}

					}
				}
				else if(pieceType[col][row] == 7){

					boolean left = true;
					boolean right = true;
					boolean down = true;
					boolean up = true;

					for(int a = 1 ; a < 8; a++){
						if(left && row - a >= 0 && pieceType[col][row-a] == 0){
							background[col][row-a].setIcon(lightBI);
						}
						else if(left && row - a >= 0 && pieceType[col][row-a] != 0 && pieceType[col][row-a] % 2 == 0){
							background[col][row-a].setIcon(lightRI);
							left = false;
						}
						else if(left && row - a >= 0 && pieceType[col][row-a] != 0){
							left = false;
						}

						if(right && row + a < 8 && pieceType[col][row+a] == 0){
							background[col][row+a].setIcon(lightBI);
						}
						else if(right && row + a < 8 && pieceType[col][row+a] != 0 && pieceType[col][row+a] % 2 == 0){
							background[col][row+a].setIcon(lightRI);
							right = false;
						}
						else if(right && row + a < 8 && pieceType[col][row+a] != 0){
							right = false;
						}

						if(down && col + a < 8 && pieceType[col+a][row] == 0){
							background[col+a][row].setIcon(lightBI);
						}
						else if(down && col + a < 8 && pieceType[col+a][row] != 0 && pieceType[col+a][row] % 2 == 0){
							background[col+a][row].setIcon(lightRI);
							down = false;
						}
						else if(down && col + a < 8 && pieceType[col+a][row] != 0){
							down = false;
						}

						if(up && col - a >= 0 && pieceType[col-a][row] == 0){
							background[col-a][row].setIcon(lightBI);
						}
						else if(up && col - a >= 0 && pieceType[col-a][row] != 0 && pieceType[col-a][row] % 2 == 0){
							background[col-a][row].setIcon(lightRI);
							up = false;
						}
						else if(up && col - a >= 0 && pieceType[col-a][row] != 0){
							up = false;
						}

					}
				}
				else if(pieceType[col][row] == 8){

					boolean left = true;
					boolean right = true;
					boolean down = true;
					boolean up = true;

					for(int a = 1 ; a < 8; a++){
						if(left && row - a >= 0 && pieceType[col][row-a] == 0){
							background[col][row-a].setIcon(lightBI);
						}
						else if(left && row - a >= 0 && pieceType[col][row-a] != 0 && pieceType[col][row-a] % 2 == 1){
							background[col][row-a].setIcon(lightRI);
							left = false;
						}
						else if(left && row - a >= 0 && pieceType[col][row-a] != 0){
							left = false;
						}

						if(right && row + a < 8 && pieceType[col][row+a] == 0){
							background[col][row+a].setIcon(lightBI);
						}
						else if(right && row + a < 8 && pieceType[col][row+a] != 0 && pieceType[col][row+a] % 2 == 1){
							background[col][row+a].setIcon(lightRI);
							right = false;
						}
						else if(right && row + a < 8 && pieceType[col][row+a] != 0){
							right = false;
						}

						if(down && col + a < 8 && pieceType[col+a][row] == 0){
							background[col+a][row].setIcon(lightBI);
						}
						else if(down && col + a < 8 && pieceType[col+a][row] != 0 && pieceType[col+a][row] % 2 == 1){
							background[col+a][row].setIcon(lightRI);
							down = false;
						}
						else if(down && col + a < 8 && pieceType[col+a][row] != 0){
							down = false;
						}

						if(up && col - a >= 0 && pieceType[col-a][row] == 0){
							background[col-a][row].setIcon(lightBI);
						}
						else if(up && col - a >= 0 && pieceType[col-a][row] != 0 && pieceType[col-a][row] % 2 == 1){
							background[col-a][row].setIcon(lightRI);
							up = false;
						}
						else if(up && col - a >= 0 && pieceType[col-a][row] != 0){
							up = false;
						}

					}
				}
				else if(pieceType[col][row] == 9){

					boolean left = true;
					boolean right = true;
					boolean down = true;
					boolean up = true;

					for(int a = 1 ; a < 8; a++){
						if(left && row - a >= 0 && pieceType[col][row-a] == 0){
							background[col][row-a].setIcon(lightBI);
						}
						else if(left && row - a >= 0 && pieceType[col][row-a] != 0 && pieceType[col][row-a] % 2 == 0){
							background[col][row-a].setIcon(lightRI);
							left = false;
						}
						else if(left && row - a >= 0 && pieceType[col][row-a] != 0){
							left = false;
						}

						if(right && row + a < 8 && pieceType[col][row+a] == 0){
							background[col][row+a].setIcon(lightBI);
						}
						else if(right && row + a < 8 && pieceType[col][row+a] != 0 && pieceType[col][row+a] % 2 == 0){
							background[col][row+a].setIcon(lightRI);
							right = false;
						}
						else if(right && row + a < 8 && pieceType[col][row+a] != 0){
							right = false;
						}

						if(down && col + a < 8 && pieceType[col+a][row] == 0){
							background[col+a][row].setIcon(lightBI);
						}
						else if(down && col + a < 8 && pieceType[col+a][row] != 0 && pieceType[col+a][row] % 2 == 0){
							background[col+a][row].setIcon(lightRI);
							down = false;
						}
						else if(down && col + a < 8 && pieceType[col+a][row] != 0){
							down = false;
						}

						if(up && col - a >= 0 && pieceType[col-a][row] == 0){
							background[col-a][row].setIcon(lightBI);
						}
						else if(up && col - a >= 0 && pieceType[col-a][row] != 0 && pieceType[col-a][row] % 2 == 0){
							background[col-a][row].setIcon(lightRI);
							up = false;
						}
						else if(up && col - a >= 0 && pieceType[col-a][row] != 0){
							up = false;
						}

					}

					boolean topL = true;
					boolean topR = true;
					boolean botL = true;
					boolean botR = true;

					for(int a = 1; a < 8; a++){
						if(topL && col - a >= 0 && row - a >= 0 && pieceType[col-a][row-a] == 0){
							background[col-a][row-a].setIcon(lightBI);
						}
						else if(topL && col - a >= 0 && row - a >= 0 && pieceType[col-a][row-a] != 0 && pieceType[col-a][row-a] % 2 == 0){
							background[col-a][row-a].setIcon(lightRI);
							topL = false;
						}
						else if(topL && col - a >= 0 && row - a >= 0 && pieceType[col-a][row-a] != 0){
							topL = false;
						}

						if(topR && col - a >= 0 && row + a < 8 && pieceType[col-a][row+a] == 0){
							background[col-a][row+a].setIcon(lightBI);
						}
						else if(topR && col - a >= 0 && row + a < 8 && pieceType[col-a][row+a] != 0 && pieceType[col-a][row+a] % 2 == 0){
							background[col-a][row+a].setIcon(lightRI);
							topR = false;
						}
						else if(topR && col - a >= 0 && row + a < 8 && pieceType[col-a][row+a] != 0){
							topR = false;
						}

						if(botR && col + a < 8 && row + a < 8 && pieceType[col+a][row+a] == 0){
							background[col+a][row+a].setIcon(lightBI);
						}
						else if(botR && col + a < 8 && row + a < 8 && pieceType[col+a][row+a] != 0 && pieceType[col+a][row+a] % 2 == 0){
							background[col+a][row+a].setIcon(lightRI);
							botR = false;
						}
						else if(botR && col + a < 8 && row + a < 8 && pieceType[col+a][row+a] != 0){
							botR = false;
						}

						if(botL && col + a < 8 && row - a >= 0 && pieceType[col+a][row-a] == 0){
							background[col+a][row-a].setIcon(lightBI);
						}
						else if(botL && col + a < 8 && row - a >= 0 && pieceType[col+a][row-a] != 0 && pieceType[col+a][row-a] % 2 == 0){
							background[col+a][row-a].setIcon(lightRI);
							botL = false;
						}
						else if(botL && col + a < 8 && row - a >= 0 && pieceType[col+a][row-a] != 0){
							botL = false;
						}

					}

				}
				else if(pieceType[col][row] == 10){

					boolean left = true;
					boolean right = true;
					boolean down = true;
					boolean up = true;

					for(int a = 1 ; a < 8; a++){
						if(left && row - a >= 0 && pieceType[col][row-a] == 0){
							background[col][row-a].setIcon(lightBI);
						}
						else if(left && row - a >= 0 && pieceType[col][row-a] != 0 && pieceType[col][row-a] % 2 == 1){
							background[col][row-a].setIcon(lightRI);
							left = false;
						}
						else if(left && row - a >= 0 && pieceType[col][row-a] != 0){
							left = false;
						}

						if(right && row + a < 8 && pieceType[col][row+a] == 0){
							background[col][row+a].setIcon(lightBI);
						}
						else if(right && row + a < 8 && pieceType[col][row+a] != 0 && pieceType[col][row+a] % 2 == 1){
							background[col][row+a].setIcon(lightRI);
							right = false;
						}
						else if(right && row + a < 8 && pieceType[col][row+a] != 0){
							right = false;
						}

						if(down && col + a < 8 && pieceType[col+a][row] == 0){
							background[col+a][row].setIcon(lightBI);
						}
						else if(down && col + a < 8 && pieceType[col+a][row] != 0 && pieceType[col+a][row] % 2 == 1){
							background[col+a][row].setIcon(lightRI);
							down = false;
						}
						else if(down && col + a < 8 && pieceType[col+a][row] != 0){
							down = false;
						}

						if(up && col - a >= 0 && pieceType[col-a][row] == 0){
							background[col-a][row].setIcon(lightBI);
						}
						else if(up && col - a >= 0 && pieceType[col-a][row] != 0 && pieceType[col-a][row] % 2 == 1){
							background[col-a][row].setIcon(lightRI);
							up = false;
						}
						else if(up && col - a >= 0 && pieceType[col-a][row] != 0){
							up = false;
						}

					}

					boolean topL = true;
					boolean topR = true;
					boolean botL = true;
					boolean botR = true;

					for(int a = 1; a < 8; a++){
						if(topL && col - a >= 0 && row - a >= 0 && pieceType[col-a][row-a] == 0){
							background[col-a][row-a].setIcon(lightBI);
						}
						else if(topL && col - a >= 0 && row - a >= 0 && pieceType[col-a][row-a] != 0 && pieceType[col-a][row-a] % 2 == 1){
							background[col-a][row-a].setIcon(lightRI);
							topL = false;
						}
						else if(topL && col - a >= 0 && row - a >= 0 && pieceType[col-a][row-a] != 0){
							topL = false;
						}

						if(topR && col - a >= 0 && row + a < 8 && pieceType[col-a][row+a] == 0){
							background[col-a][row+a].setIcon(lightBI);
						}
						else if(topR && col - a >= 0 && row + a < 8 && pieceType[col-a][row+a] != 0 && pieceType[col-a][row+a] % 2 == 1){
							background[col-a][row+a].setIcon(lightRI);
							topR = false;
						}
						else if(topR && col - a >= 0 && row + a < 8 && pieceType[col-a][row+a] != 0){
							topR = false;
						}

						if(botR && col + a < 8 && row + a < 8 && pieceType[col+a][row+a] == 0){
							background[col+a][row+a].setIcon(lightBI);
						}
						else if(botR && col + a < 8 && row + a < 8 && pieceType[col+a][row+a] != 0 && pieceType[col+a][row+a] % 2 == 1){
							background[col+a][row+a].setIcon(lightRI);
							botR = false;
						}
						else if(botR && col + a < 8 && row + a < 8 && pieceType[col+a][row+a] != 0){
							botR = false;
						}

						if(botL && col + a < 8 && row - a >= 0 && pieceType[col+a][row-a] == 0){
							background[col+a][row-a].setIcon(lightBI);
						}
						else if(botL && col + a < 8 && row - a >= 0 && pieceType[col+a][row-a] != 0 && pieceType[col+a][row-a] % 2 == 1){
							background[col+a][row-a].setIcon(lightRI);
							botL = false;
						}
						else if(botL && col + a < 8 && row - a >= 0 && pieceType[col+a][row-a] != 0){
							botL = false;
						}

					}

				}
				else if(pieceType[col][row] == 11){
					boolean topL = true;
					boolean topR = true;
					boolean botL = true;
					boolean botR = true;
					boolean left = true;
					boolean right = true;
					boolean down = true;
					boolean up = true;


					if(col - 1 >= 0 && row - 1 >= 0 && (pieceType[col-1][row-1] == 0 || pieceType[col-1][row-1] % 2 == 0)){
						boolean temp = kingMove(row, col , row-1, col-1);

						if(!temp){
							if(pieceType[col-1][row-1] == 0){
								background[col-1][row-1].setIcon(lightBI);
							}
							else if(pieceType[col-1][row-1] % 2 == 0){
								background[col-1][row-1].setIcon(lightRI);
							}
						}
						else{
							topL = false;
							background[col-1][row-1].setIcon(clearI);
						}
					}

					if(col + 1 < 8 && row - 1 >= 0 && (pieceType[col+1][row-1] == 0 || pieceType[col+1][row-1] % 2 == 0)){
						boolean temp = kingMove(row, col , row-1, col+1);
						if(!temp){
							if(pieceType[col+1][row-1] == 0){
								background[col+1][row-1].setIcon(lightBI);
							}
							else if(pieceType[col+1][row-1] % 2 == 0){
								background[col+1][row-1].setIcon(lightRI);
							}
						}
						else{
							botL = false;
							background[col+1][row-1].setIcon(clearI);
						}
					}

					if(col - 1 >= 0 && row + 1 < 8 && (pieceType[col-1][row+1] == 0 || pieceType[col-1][row+1] % 2 == 0)){
						boolean temp = kingMove(row, col , row+1, col-1);
						if(!temp){
							if(pieceType[col-1][row+1] == 0){
								background[col-1][row+1].setIcon(lightBI);
							}
							else if(pieceType[col-1][row+1] % 2 == 0){
								background[col-1][row+1].setIcon(lightRI);
							}
						}
						else{
							topR = false;
							background[col-1][row+1].setIcon(clearI);
						}
					}

					if(col + 1 < 8 && row + 1 < 8 && (pieceType[col+1][row+1] == 0 || pieceType[col+1][row+1] % 2 == 0)){
						boolean temp = kingMove(row, col , row+1, col+1);
						if(!temp){
							if(pieceType[col+1][row+1] == 0){
								background[col+1][row+1].setIcon(lightBI);
							}
							else if(pieceType[col+1][row+1] % 2 == 0){
								background[col+1][row+1].setIcon(lightRI);
							}
						}
						else{
							botR = false;
							background[col+1][row+1].setIcon(clearI);
						}
					}

					if(col + 1 < 8 && (pieceType[col+1][row] == 0 || pieceType[col+1][row] % 2 == 0)){
						boolean temp = kingMove(row, col , row, col+1);
						if(!temp){
							if(pieceType[col+1][row] == 0){
								background[col+1][row].setIcon(lightBI);
							}
							else if(pieceType[col+1][row] % 2 == 0){
								background[col+1][row].setIcon(lightRI);
							}
						}
						else{
							down = false;
							background[col+1][row].setIcon(clearI);
						}
					}

					if(col - 1 >= 0 && (pieceType[col-1][row] == 0 || pieceType[col-1][row] % 2 == 0)){
						boolean temp = kingMove(row, col , row, col-1);
						if(!temp){
							if(pieceType[col-1][row] == 0){
								background[col-1][row].setIcon(lightBI);
							}
							else if(pieceType[col-1][row] % 2 == 0){
								background[col-1][row].setIcon(lightRI);
							}
						}
						else{
							up = false;
							background[col-1][row].setIcon(clearI);
						}
					}

					if(row + 1 < 8 && (pieceType[col][row+1] == 0 || pieceType[col][row+1] % 2 == 0)){
						boolean temp = kingMove(row, col , row+1, col);
						if(!temp){
							if(pieceType[col][row+1] == 0){
								background[col][row+1].setIcon(lightBI);
							}
							else if(pieceType[col][row+1] % 2 == 0){
								background[col][row+1].setIcon(lightRI);
							}
						}
						else{
							right = false;
							background[col][row+1].setIcon(clearI);
						}
					}

					if(row -1 >= 0 && (pieceType[col][row-1] == 0 || pieceType[col][row-1] % 2 == 0)){
						boolean temp = kingMove(row, col , row-1, col);

						if(!temp){
							if(pieceType[col][row-1] == 0){
								background[col][row-1].setIcon(lightBI);
							}
							else if(pieceType[col][row-1] % 2 == 0){
								background[col][row-1].setIcon(lightRI);
							}
						}
						else{
							left = false;
							background[col][row-1].setIcon(clearI);
						}
					}

					if(!left){
						background[col][row-1].setIcon(clearI);
					}
					if(!right){
						background[col][row+1].setIcon(clearI);
					}
					if(!up){
						background[col-1][row].setIcon(clearI);
					}
					if(!down){
						background[col+1][row].setIcon(clearI);
					}
					if(!botL){
						background[col+1][row-1].setIcon(clearI);
					}
					if(!topR){
						background[col-1][row+1].setIcon(clearI);
					}
					if(!topL){
						background[col-1][row-1].setIcon(clearI);
					}
					if(!botR){
						background[col+1][row+1].setIcon(clearI);
					}

				}
				else if(pieceType[col][row] == 12){

					boolean topL = true;
					boolean topR = true;
					boolean botL = true;
					boolean botR = true;
					boolean left = true;
					boolean right = true;
					boolean down = true;
					boolean up = true;


					if(col - 1 >= 0 && row - 1 >= 0 && (pieceType[col-1][row-1] == 0 || pieceType[col-1][row-1] % 2 == 1)){
						boolean temp = kingMove(row, col , row-1, col-1);

						if(!temp){
							if(pieceType[col-1][row-1] == 0){
								background[col-1][row-1].setIcon(lightBI);
							}
							else if(pieceType[col-1][row-1] % 2 == 1){
								background[col-1][row-1].setIcon(lightRI);
							}
						}
						else{
							topL = false;
							background[col-1][row-1].setIcon(clearI);
						}
					}

					if(col + 1 < 8 && row - 1 >= 0 && (pieceType[col+1][row-1] == 0 || pieceType[col+1][row-1] % 2 == 1)){
						boolean temp = kingMove(row, col , row-1, col+1);
						if(!temp){
							if(pieceType[col+1][row-1] == 0){
								background[col+1][row-1].setIcon(lightBI);
							}
							else if(pieceType[col+1][row-1] % 2 == 1){
								background[col+1][row-1].setIcon(lightRI);
							}
						}
						else{
							botL = false;
							background[col+1][row-1].setIcon(clearI);
						}
					}

					if(col - 1 >= 0 && row + 1 < 8 && (pieceType[col-1][row+1] == 0 || pieceType[col-1][row+1] % 2 == 1)){
						boolean temp = kingMove(row, col , row+1, col-1);
						if(!temp){
							if(pieceType[col-1][row+1] == 0){
								background[col-1][row+1].setIcon(lightBI);
							}
							else if(pieceType[col-1][row+1] % 2 == 1){
								background[col-1][row+1].setIcon(lightRI);
							}
						}
						else{
							topR = false;
							background[col-1][row+1].setIcon(clearI);
						}
					}

					if(col + 1 < 8 && row + 1 < 8 && (pieceType[col+1][row+1] == 0 || pieceType[col+1][row+1] % 2 == 1)){
						boolean temp = kingMove(row, col , row+1, col+1);
						if(!temp){
							if(pieceType[col+1][row+1] == 0){
								background[col+1][row+1].setIcon(lightBI);
							}
							else if(pieceType[col+1][row+1] % 2 == 1){
								background[col+1][row+1].setIcon(lightRI);
							}
						}
						else{
							botR = false;
							background[col+1][row+1].setIcon(clearI);
						}
					}

					if(col + 1 < 8 && (pieceType[col+1][row] == 0 || pieceType[col+1][row] % 2 == 1)){
						boolean temp = kingMove(row, col , row, col+1);
						if(!temp){
							if(pieceType[col+1][row] == 0){
								background[col+1][row].setIcon(lightBI);
							}
							else if(pieceType[col+1][row] % 2 == 1){
								background[col+1][row].setIcon(lightRI);
							}
						}
						else{
							down = false;
							background[col+1][row].setIcon(clearI);
						}
					}

					if(col - 1 >= 0 && (pieceType[col-1][row] == 0 || pieceType[col-1][row] % 2 == 1)){
						boolean temp = kingMove(row, col , row, col-1);
						if(!temp){
							if(pieceType[col-1][row] == 0){
								background[col-1][row].setIcon(lightBI);
							}
							else if(pieceType[col-1][row] % 2 == 1){
								background[col-1][row].setIcon(lightRI);
							}
						}
						else{
							up = false;
							background[col-1][row].setIcon(clearI);
						}
					}

					if(row + 1 < 8 && (pieceType[col][row+1] == 0 || pieceType[col][row+1] % 2 == 1)){
						boolean temp = kingMove(row, col , row+1, col);
						if(!temp){
							if(pieceType[col][row+1] == 0){
								background[col][row+1].setIcon(lightBI);
							}
							else if(pieceType[col][row+1] % 2 == 1){
								background[col][row+1].setIcon(lightRI);
							}
						}
						else{
							right = false;
							background[col][row+1].setIcon(clearI);
						}
					}

					if(row -1 >= 0 && (pieceType[col][row-1] == 0 || pieceType[col][row-1] % 2 == 1)){
						boolean temp = kingMove(row, col , row-1, col);
						if(!temp){
							if(pieceType[col][row-1] == 0){
								background[col][row-1].setIcon(lightBI);
							}
							else if(pieceType[col][row-1] % 2 == 1){
								background[col][row-1].setIcon(lightRI);
							}
						}
						else{
							left = false;
							background[col][row-1].setIcon(clearI);
						}
					}

					if(!left){
						background[col][row-1].setIcon(clearI);
					}
					if(!right){
						background[col][row+1].setIcon(clearI);
					}
					if(!up){
						background[col-1][row].setIcon(clearI);
					}
					if(!down){
						background[col+1][row].setIcon(clearI);
					}
					if(!botL){
						background[col+1][row-1].setIcon(clearI);
					}
					if(!topR){
						background[col-1][row+1].setIcon(clearI);
					}
					if(!topL){
						background[col-1][row-1].setIcon(clearI);
					}
					if(!botR){
						background[col+1][row+1].setIcon(clearI);
					}

				}

			}
		}

		private boolean kingMove(int row, int col, int mRow, int mCol){
			int typeKing = pieceType[col][row];

			int temp = pieceType[mCol][mRow];
			pieceType[mCol][mRow] = pieceType[col][row];
			pieceType[col][row] = 0;
			isCheck();
			pieceType[col][row] = pieceType[mCol][mRow];
			pieceType[mCol][mRow] = temp;
			clearSurrounding(row,col);

			if(typeKing == 12){
				if(bKingCheck){
					return true;
				}
				return false;
			}
			else{
				if(wKingCheck){
					return true;
				}
				return false;
			}



		}

		private void clearSurrounding(int row, int col){
			int team = pieceType[col][row]%2;
			for(int i = 0 ; i < 8; i++){
				for(int o = 0 ; o < 8; o++){
					if(Math.abs(o-col) > 1 || Math.abs(i-row) > 1){
						background[o][i].setIcon(clearI);
					}
					else{
						if((pieceType[o][i]%2) == team){
							background[o][i].setIcon(clearI);
						}
						else if((pieceType[o][i]%2) != team && pieceType[o][i] != 0){
							background[o][i].setIcon(lightRI);
						}
						else if(background[o][i].getIcon() == lightRI && pieceType[o][i] == 0){
							background[o][i].setIcon(lightBI);
						}
					}


				}
			}
		}

		private void clearBack(){
			for(int a = 0 ; a < 8; a++){
				for(int b = 0 ; b < 8; b++){
					background[a][b].setIcon(clearI);
				}
			}
		}

		private void clearKings(){
			for(int a = 0; a < 8; a++){
				for(int b = 0 ; b < 8; b++){
					if(pieceType[a][b] == 11 || pieceType[a][b] == 12){
						background[a][b].setIcon(clearI);
					}
				}
			}
		}

		private void isCheck(){

			int wKingX = 0;
			int wKingY = 0;
			int bKingX = 0;
			int bKingY = 0;

			wKingCheck = false;
			bKingCheck = false;

			for(int i = 0 ; i < 8; i++){
				for(int o = 0 ; o < 8; o++){
					if(pieceType[i][o] == 11){
						wKingX = i;
						wKingY = o;
					}
					else if(pieceType[i][o] == 12){
						bKingX = i;
						bKingY = o;
					}
				}
			}
			for(int i = 0 ; i < 8; i++){
				for(int o = 0 ; o < 8; o++){
					if(pieceType[o][i] != 11 && pieceType[o][i] != 12){
						showMovement(i, o);
						if(background[wKingX][wKingY].getIcon() == lightRI){
							wKingCheck = true;
						}
						if(background[bKingX][bKingY].getIcon() == lightRI){
							bKingCheck = true;
						}
					}

				}
			}
		}


		private boolean isStartingPawn(int x, int y){

			if(pieceType[y][x] == 1 && y == 6){
				return true;
			}
			else if(pieceType[y][x] == 2 && y == 1){
				return true;
			}
			return false;
		}

		public boolean mouseOn(int mx, int my, int x, int y, int width, int height){
			if(mx > x && mx < x + width && my > y && my < y + height){
				return true;
			}
			return false;
		}

	}

	void reset(){
		turn = 1;
		select = false;
		wKingCheck = false;
		bKingCheck = false;
		for(int i = 0 ; i < 8; i++){
			for(int o = 0 ; o < 8; o++){
				background[i][o].setIcon(clearI);
				pieces[i][o].setIcon(clearI);
				pieceType[i][o] = 0;
			}
		}
		pieceType[0][0] = 8;
		pieces[0][0].setIcon(bRookI);
		pieceType[0][7] = 8;
		pieces[0][7].setIcon(bRookI);
		pieceType[7][0] = 7;
		pieces[7][0].setIcon(wRookI);
		pieceType[7][7] = 7;
		pieces[7][7].setIcon(wRookI);;

		pieceType[0][1] = 4;
		pieces[0][1].setIcon(bKnightI);
		pieceType[0][6] = 4;
		pieces[0][6].setIcon(bKnightI);
		pieceType[7][1] = 3;
		pieces[7][1].setIcon(wKnightI);
		pieceType[7][6] = 3;
		pieces[7][6].setIcon(wKnightI);

		pieceType[0][2] = 6;
		pieces[0][2].setIcon(bBishopI);
		pieceType[0][5] = 6;
		pieces[0][5].setIcon(bBishopI);
		pieceType[7][2] = 5;
		pieces[7][2].setIcon(wBishopI);
		pieceType[7][5] = 5;
		pieces[7][5].setIcon(wBishopI);

		pieceType[0][3] = 10;
		pieces[0][3].setIcon(bQueenI);
		pieceType[7][3] = 9;
		pieces[7][3].setIcon(wQueenI);

		pieceType[0][4] = 12;
		pieces[0][4].setIcon(bKingI);
		pieceType[7][4] = 11;
		pieces[7][4].setIcon(wKingI);

		for(int i = 0 ; i < 8; i++){
			pieceType[1][i] = 2;
			pieceType[6][i] = 1;
			pieces[1][i].setIcon(bPawnI);
			pieces[6][i].setIcon(wPawnI);

		}

	}


	public ArcadeChess() {
		MouseClicks mouse = new MouseClicks();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 670, 770);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		int bX = 84;
		int bY = 21;
		for(int i = 0; bX + i * 69 < 640 && i < 8; i++){
			for(int o = 0; bY + o * 70 < 577 && bX + i * 70 < 640 && o < 8; o++){
				pieces[o][i] = new JLabel(clearI);
				pieces[o][i].setBounds(bX + i * 69, bY + o * 70, 70, 69);
				contentPane.add(pieces[o][i]);
			}
		}

		pieceType[0][0] = 8;
		pieces[0][0] = new JLabel(bRookI);
		pieces[0][0].setBounds(81, 25, 70, 69);
		contentPane.add(pieces[0][0]);
		pieceType[0][7] = 8;
		pieces[0][7] = new JLabel(bRookI);
		pieces[0][7].setBounds(571, 25, 70, 69);
		contentPane.add(pieces[0][7]);
		pieceType[7][0] = 7;
		pieces[7][0] = new JLabel(wRookI);
		pieces[7][0].setBounds(84, 510, 70, 69);
		contentPane.add(pieces[7][0]);
		pieceType[7][7] = 7;
		pieces[7][7] = new JLabel(wRookI);
		pieces[7][7].setBounds(572, 510, 70, 69);
		contentPane.add(pieces[7][7]);

		pieceType[0][1] = 4;
		pieces[0][1] = new JLabel(bKnightI);
		pieces[0][1].setBounds(155, 23, 70, 69);
		contentPane.add(pieces[0][1]);
		pieceType[0][6] = 4;
		pieces[0][6] = new JLabel(bKnightI);
		pieces[0][6].setBounds(498, 23, 70, 69);
		contentPane.add(pieces[0][6]);
		pieceType[7][1] = 3;
		pieces[7][1] = new JLabel(wKnightI);
		pieces[7][1].setBounds(153, 510, 70, 69);
		contentPane.add(pieces[7][1]);
		pieceType[7][6] = 3;
		pieces[7][6] = new JLabel(wKnightI);
		pieces[7][6].setBounds(498, 510, 70, 69);
		contentPane.add(pieces[7][6]);

		pieceType[0][2] = 6;
		pieces[0][2] = new JLabel(bBishopI);
		pieces[0][2].setBounds(224, 23, 70, 69);
		contentPane.add(pieces[0][2]);
		pieceType[0][5] = 6;
		pieces[0][5] = new JLabel(bBishopI);
		pieces[0][5].setBounds(430, 23, 70, 69);
		contentPane.add(pieces[0][5]);
		pieceType[7][2] = 5;
		pieces[7][2] = new JLabel(wBishopI);
		pieces[7][2].setBounds(222, 510, 70, 69);
		contentPane.add(pieces[7][2]);
		pieceType[7][5] = 5;
		pieces[7][5] = new JLabel(wBishopI);
		pieces[7][5].setBounds(430, 510, 70, 69);
		contentPane.add(pieces[7][5]);

		pieceType[0][3] = 10;
		pieces[0][3] = new JLabel(bQueenI);
		pieces[0][3].setBounds(293, 23, 70, 69);
		contentPane.add(pieces[0][3]);
		pieceType[7][3] = 9;
		pieces[7][3] = new JLabel(wQueenI);
		pieces[7][3].setBounds(292, 510, 70, 69);
		contentPane.add(pieces[7][3]);

		pieceType[0][4] = 12;
		pieces[0][4] = new JLabel(bKingI);
		pieces[0][4].setBounds(363, 23, 70, 69);
		contentPane.add(pieces[0][4]);
		pieceType[7][4] = 11;
		pieces[7][4] = new JLabel(wKingI);
		pieces[7][4].setBounds(363, 510, 70, 69);
		contentPane.add(pieces[7][4]);

		for(int i = 0 ; i < 8; i++){
			pieces[1][i] = new JLabel(bPawnI);
			pieces[1][i].setBounds(84 + i * 70, 92, 70, 69);
			pieceType[1][i] = 2;
			contentPane.add(pieces[1][i]);
			pieces[6][i] = new JLabel(wPawnI);
			pieces[6][i].setBounds(83 + i * 69, 440, 70, 69);
			pieceType[6][i] = 1;
			contentPane.add(pieces[6][i]);
		}

		bX = 84;
		bY = 21;
		for(int i = 0; bX + i * 69 < 640 && i < 8; i++){
			for(int o = 0; bY + o * 70 < 577 && bX + i * 70 < 640 && o < 8; o++){
				background[o][i] = new JLabel(clearI);
				background[o][i].setBounds(bX + i * 69, bY + o * 70, 70, 69);
				contentPane.add(background[o][i]);
			}
		}


		JLabel board = new JLabel(boardI);
		board.setBounds(0, 0, 654, 670);
		contentPane.add(board);
		board.setIcon(boardI);

		JButton btnNewButton = new JButton("New button");
		btnNewButton.setAction(action);
		btnNewButton.setBounds(40, 665, 108, 23);
		contentPane.add(btnNewButton);

		JButton button = new JButton("New button");
		button.setAction(action_1);
		button.setBounds(500, 665, 108, 23);
		contentPane.add(button);

		winCount = new JTextArea();
		winCount.setEditable(false);
		winCount.setBounds(193, 664, 307, 22);
		contentPane.add(winCount);

		winCount.setText("Black Wins: 0		White Wins: 0");

		contentPane.addMouseListener(mouse);
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Reset");
		}
		public void actionPerformed(ActionEvent e) {
			reset();
		}
	}
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "Exit");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}
