import javax.swing.*;
import  java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public class SudokuUI {

    JTextField[][] cells = new JTextField[9][9];        // 9 x 9 Grid

    public SudokuUI(){

        // Main Window
        JFrame frame = new JFrame("Sudoku Solver");
        frame.setSize(600,600);     // Window Size
        frame.setLocationRelativeTo(null);      // Open Window on Center
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       // Close Window

        // Creating Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9,9));       // Panel with 9 x 9 Grid Layout

        // Loop to Create 81 TextFields
        for(int i=0;i<9;i++){

            for(int j=0;j<9;j++){

                cells[i][j] = new JTextField();     // Creating TextField
                cells[i][j].setOpaque(true);  // Ensure background color is visible
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);      // Set Alignment
                cells[i][j].setFont(new Font("Arial",Font.BOLD,22));        // Set Font Size
                panel.add(cells[i][j]);         // Add TextField to Panel

                // Adding Border
                int top = 1, left = 1, bottom = 1, right = 1;   // Default Border
                if (i % 3 == 0) {               // Top Border for each 3rd Row
                    top = 3;
                }
                if (j % 3 == 0) {               // Left Border for each 3rd Column
                    left = 3;
                }
                if (i == 8) {                   // Bottom Border for last Row
                    bottom = 3;
                }
                if (j == 8) {                   // Right Border for last Column
                    right = 3;
                }
                Border border = new MatteBorder(top, left, bottom, right, Color.BLUE);
                cells[i][j].setBorder(border);

                // Alternate cell color (chess pattern)
                if ((i + j) % 2 == 0) {
                    cells[i][j].setBackground(new Color(220, 220, 255));
                } else {
                    cells[i][j].setBackground(Color.WHITE);
                }
            }
        }

        // Creating Button Panel
        JPanel buttonPanel = new JPanel();

        JButton solveButton = new JButton("SOLVE");     // Creating Solve Button
        JButton resetButton = new JButton("RESET");     // Creating Reset Button

        // Adding Action to Reset Button
        resetButton.addActionListener(e ->{

            for(int i=0; i<9; i++){
                for(int j=0; j<9; j++){

                    cells[i][j].setText("");        // Clear Text in Each Cell
                }
            }
        });

        // Adding Action to Solve Button
        solveButton.addActionListener(e ->{
            int[][] board = new int[9][9];

            for(int i=0; i<9; i++){
                for(int j=0; j<9; j++){

                    String text = cells[i][j].getText();

                    if (!text.isEmpty()) {
                        try {
                            int num = Integer.parseInt(text);
                            if (num >= 1 && num <= 9) {     // Allow only 1 to 9
                                board[i][j] = num;
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Enter numbers between 1 and 9");
                                return;
                            }

                        }
                        catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid input!");
                            return;
                        }
                    }
                    else {
                        board[i][j] = 0;
                    }
                }
            }

            // Checking Initial Board
            if (!isBoardValid(board)) {
                JOptionPane.showMessageDialog(null, "Invalid Sudoku input!");
                return;
            }

            // Call Solver
            if(solveSudoku(board)){
                for(int i=0; i<9; i++){
                    for(int j=0; j<9; j++){

                        cells[i][j].setText(String.valueOf(board[i][j]));
                    }
                }
            }

            else{
                JOptionPane.showMessageDialog(null,"No Solution Exists!!");
            }
        });

        buttonPanel.add(solveButton);           // Adding Buttons to Panel
        buttonPanel.add(resetButton);

        // Creating Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(panel, BorderLayout.CENTER);       // Adding Panel to Main Panel
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);         // Adding Button Panel to Main Panel
        frame.add(mainPanel);               // Adding Main Panel to Frame

        frame.setVisible(true);
    }

    // Function to Solve Sudoku using Backtracking
    private boolean solveSudoku(int[][] board){

        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){

                if(board[i][j] == 0){       // Find Empty Cell
                    for(int num=1; num<=9; num++){
                        if(isValid(board,i,j,num)){

                            board[i][j] = num;      // Place Number

                            if(solveSudoku(board)){     // Recursive Call
                                return true;
                            }

                            board[i][j] = 0;            // BackTracking
                        }


                    }

                    return false;       // If No Valid Number Found
                }
            }
        }

        return true;
    }

    // Function to Check Placing Number is Valid or Not
    private boolean isValid(int[][] board, int row, int col, int num){

        for(int i=0; i<9; i++){     // Check Row
            if(board[row][i] == num)
                return false;
        }

        for(int i=0; i<9; i++){     // Check Column
            if(board[i][col] == num)
                return false;
        }

        int startRow = row - row % 3;   // Check 3 x 3 Box
        int startCol = col - col % 3;

        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){

                if(board[startRow + i][startCol + j] == num)
                    return false;
            }
        }
        return true;
    }

    // Check if Initial Board is Valid or Not
    private boolean isBoardValid(int[][] board) {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                int num = board[i][j];
                if (num != 0) {
                    board[i][j] = 0;

                    if (!isValid(board, i, j, num)) {
                        board[i][j] = num; // restore
                        return false;
                    }

                    board[i][j] = num; // restore
                }
            }
        }

        return true;
    }
}
