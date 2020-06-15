import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;


public class QuizCardPlayer{
    private JFrame frame;
    private JTextArea display;
    private JTextArea answer;
    private ArrayList<QuizCard> cardList;
    private QuizCard currentCard;
    private int currentCardIndex;
    private JButton nextButton;
    private boolean isShowAnswer;

    public static void main(String[] args) {
        QuizCardPlayer player = new QuizCardPlayer();
        player.go();
    }

    public void go(){
        frame = new JFrame("Quiz Card Player");
        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        display = new JTextArea(10, 20);
        display.setFont(bigFont);
        display.setLineWrap(true);
        display.setEditable(false);

        JScrollPane qScroller = new JScrollPane(display);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        nextButton = new JButton("Show Question");
        mainPanel.add(qScroller);
        mainPanel.add(nextButton);
        nextButton.addActionListener(new NextCardListener());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load card set");
        loadMenuItem.addActionListener(new OpenMenuListener());
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
        frame.setSize(640, 500);
    }

    public class NextCardListener implements ActionListener{
        public void actionPerformed(ActionEvent ev){
           if(isShowAnswer){
               display.setText(currentCard.getAnswer());
               nextButton.setText("Next Card");
               isShowAnswer = false;
           } else{
               display.setText("No more Cards");
               nextButton.setEnabled(false);
           }
        }
    }

    public class OpenMenuListener implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            JFileChooser openFile = new JFileChooser();
            openFile.showOpenDialog(frame);
            loadFile(openFile.getSelectedFile());
        }
    }

    private void loadFile(File file){
        cardList = new ArrayList<QuizCard>();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = reader.readLine()) != null){
                makeCard(line);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        showCard();
    }

    private void makeCard(String lineToParse){
        String[] result = lineToParse.split("/");
        QuizCard card = new QuizCard(result[0], result[1]);
        cardList.add(card);
    }

    private void showCard(){
       currentCard = cardList.get(currentCardIndex);
       currentCardIndex++;
       display.setText(currentCard.getQuestion());
       nextButton.setText("Show Answer");
       isShowAnswer = true; 
    }

}