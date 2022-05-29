package sample;


import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Controller {
    @FXML
    private ImageView Image;
    @FXML
    private Button p1;
    @FXML
    private Button p2;
    @FXML
    private Label l;
// Player 1 and 2 must be accessed through 2 different buttons
    @FXML
    private Label lol;
    @FXML
    private ImageView Image3;
    @FXML
    private ImageView Image4;
    @FXML
    private ImageView Image5;
    private static final int ht = 10;
    private static final int wd = 10;
    private Player player1;
    private Player player2;
    private Dice die;
    private Snake snake;
    private Ladder lad;
    private int en1 = 0;
    private int en2 =0;
    private boolean chance1 = true;
    private boolean chance2 = true;

    public void initialize(){
        int count = 100;
        boolean odd;
        ArrayList<Space> sp = new ArrayList<>();
        for (int i = 0; i < ht; i++) {
            for (int j = 0; j < wd; j++) {
                odd = ((i % 2)!= 0);
                sp.add(new Space(i*40,j*40, count, odd));
                count -= 1;
            }
        }
        snake = new Snake(sp);
        lad = new Ladder(sp);
        player1 = new Player("Player 1", Image, 0, 373, 7, lol, p1);
        player2 = new Player("Player 2",Image3, 1, 373, 22, lol, p2);
        die = new Dice(l,Image5);
    }

    public void movePlayer1(ActionEvent e) {
        if ((!player1.lock)&&chance1) {
            if (en1 == 0) {
                die.rollDice();
                player2.myturn = true;
                player1.myturn = false;
                if (die.result != 1) {
                    chance1 = false;
                    chance2 = true;
                    Image4.setLayoutX(537);
                    Image4.setLayoutY(128);
                    return;
                }
                player1.token.setLayoutY(373);
                player1.myturn = false;
                player2.myturn = true;
                en1 = 1;
                Image4.setLayoutX(537);
                Image4.setLayoutY(128);
            }
            if (checkWhoseTurn() == player1) {
                p2.setDisable(true);
                player1.movePlayer(die, snake, lad).setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Image4.setLayoutX(537);
                        Image4.setLayoutY(128);
                        p2.setDisable(false);
                        if (player1.lock) {
                            JOptionPane.showMessageDialog(null, "Congratulations! "+player1.name+" Won the Game");
                            player2.lock = true;
                        }
                        player2.myturn = true;
                    }
                });
            }
        }
        chance1 = false;
        chance2 = true;
    }
    public void movePlayer2(ActionEvent e) {
        if ((!player2.lock)&&chance2) {
            if (en2 == 0) {
                die.rollDice();
                player1.myturn = true;
                player2.myturn = false;
                if (die.result != 1) {
                    chance1 = true;
                    chance2 = false;
                    Image4.setLayoutX(537);
                    Image4.setLayoutY(55);
                    return;
                }
                player2.token.setLayoutY(373);
                player2.myturn = false;
                player1.myturn = true;
                en2 = 1;
                Image4.setLayoutX(537);
                Image4.setLayoutY(55);
            }

            if (checkWhoseTurn() == player2) {
                p1.setDisable(true);
                player2.movePlayer(die, snake, lad).setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Image4.setLayoutX(537);
                        Image4.setLayoutY(55);
                        p1.setDisable(false);
                        if (player2.lock) {
                            JOptionPane.showMessageDialog(null, "Congratulations! "+player2.name+" Won the Game");
                            player1.lock = true;
                        }
                        player1.myturn = true;
                    }
                });
            }
        }
        chance2 = false;
        chance1 = true;
    }
    public Player checkWhoseTurn(){
        if(player1.myturn){
            return player1;
        }
        else
            return player2;
    }
}

class Player{
    String name;
    boolean myturn;
    ImageView token;
    boolean odd = true;
    double x, y;
    int id;
    Label lit;
    boolean lock;
    Button b;

    Player(String nam, ImageView tok, int id, int y, int x, Label l, Button p){
        this.lock = false;
        this.name = nam;
        this.token = tok;
        this.id = id;
        this.y = y;
        this.x = x;
        this.b = p;
        this.lit = l;
        lit.setText("Let's see who's gonna Win!");
    }
    public Animation movePlayer(Dice roll, Snake snake, Ladder lad){
        int show;
        int a = 0;
        ArrayList<TranslateTransition> arr = new ArrayList<>();
        double xprev2=0, yprev2=0;
        int m = 0;
        Animation animate = null;
        show = roll.rollDice();
        if((x>=0&&x<=280)&&(y>=0&&y<=40)&&show>(((int)x)/40)){
            m = 1;
            TranslateTransition t = new TranslateTransition(Duration.millis(50), token);
            t.setByX(0);
            t.setByY(0);
            t.play();
            animate = t;
        }

        if(m==0) {
            for (int i = 0; i < show; i++) {
                arr.add(translatePlayer());
            }
            xprev2 = x;
            yprev2 = y;

            for (int i = 0; i < 10; i++) {
                if((x >= snake.snakeloc.get(i).xp && x <= (snake.snakeloc.get(i).xp+40))&&(y >= snake.snakeloc.get(i).yp && y<=(snake.snakeloc.get(i).yp+40))){
                    a = 1;
                    if(id == 0) {
                        x = snake.downsnake.get(i).xp+7;
                        y = snake.downsnake.get(i).yp+13;
                        odd = snake.downsnake.get(i).odd;
                    }
                    if(id == 1) {
                        x = snake.downsnake.get(i).xp+22;
                        y = snake.downsnake.get(i).yp+13;
                        odd = snake.downsnake.get(i).odd;
                    }
                }
                else if ((x >= lad.ladderloc.get(i).xp && x <= (lad.ladderloc.get(i).xp+40))&& (y >= lad.ladderloc.get(i).yp && y <= (lad.ladderloc.get(i).yp+40))){
                    a = 1;
                    if(id == 0) {
                        x = lad.upladder.get(i).xp+7;
                        y = lad.upladder.get(i).yp+13;
                        odd = lad.upladder.get(i).odd;
                    }
                    if(id == 1) {
                        x = lad.upladder.get(i).xp+22;
                        y = lad.upladder.get(i).yp+13;
                        odd = lad.upladder.get(i).odd;
                    }
                }
            }
            if((x>=0&&x<=40)&&(y>=0&&y<=40)) {
                lit.setText("And your winner is : " + name);
                lock = true;
            }
            if(a==1) {
                animate = smoothTransition(x - xprev2, y - yprev2, arr);
            }
            else
                animate = oneTransition(arr);
        }

        myturn = false;
        return animate;
    }

    public TranslateTransition translatePlayer(){
        TranslateTransition tran = new TranslateTransition(Duration.millis(400),token);
        int ypre = (int) y;
        int xpre = (int) x;
        if(x>=360&&x<=400&&odd){
            y-=40;
            odd = false;
            tran.setByY(y-ypre);
        }
        else if(x>=0&&x<=40&&(!odd)){
            y-=40;
            odd = true;
            tran.setByY(y-ypre);
        }
        else if(odd){
            x+=40;
            tran.setByX(x-xpre);
        }
        else if(!odd){
            x-=40;
            tran.setByX(x-xpre);
        }
        return tran;
    }

    public Animation smoothTransition(double xpos1, double ypos1, ArrayList<TranslateTransition> a){

        TranslateTransition smooth2 = new TranslateTransition(Duration.millis(1000), token);
        TranslateTransition[] arr = new TranslateTransition[a.size()+1];
        smooth2.setByX(xpos1);
        smooth2.setByY(ypos1);
        for (int i = 0; i < a.size(); i++) {
            arr[i] = a.get(i);
        }
        arr[a.size()] = smooth2;
        SequentialTransition seq = new SequentialTransition(Arrays.stream(arr).filter(Objects::nonNull).toArray(TranslateTransition[]::new));
        seq.play();
        return seq;
    }

    public Animation oneTransition(ArrayList<TranslateTransition> a){
        TranslateTransition[] arr = new TranslateTransition[a.size()];
        for (int i = 0; i < a.size(); i++) {
            arr[i] = a.get(i);
        }
        SequentialTransition seq = new SequentialTransition(Arrays.stream(arr).filter(Objects::nonNull).toArray(TranslateTransition[]::new));
        seq.play();
        return seq;
    }
}

class Dice {
    int result;
    private Label l;
    private ImageView die;

    Dice(Label label, ImageView Image5){
        this.l = label;
        l.setText("Move Any Player First: ");
        this.die = Image5;
    }

    public int rollDice(){
        result = (int)(Math.random() * 6+1);
        dieShow();
        dieUpdate("Result :  "+result);
        return result;
    }

    void dieUpdate(String s){
        l.setText(s);
    }

    @FXML
    private void dieShow() {Thread thread = new Thread(){
        public void run(){
            System.out.println("Thread Running");
            try {
                for (int i = 0; i < 20; i++) {

                        File file = new File("src/sample/die/die" + (int) (Math.random() * 6 + 1) + ".png");
                        die.setImage(new Image(file.toURI().toString()));
                        Thread.sleep(10);
                    }
                    File file = new File("src/sample/die/die" + (result) + ".png");
                    die.setImage(new Image(file.toURI().toString()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
        thread.start();
    }
}

class Snake {
    ArrayList<Space> snakeloc = new ArrayList<>();
    ArrayList<Space> downsnake = new ArrayList<>();
    Snake(ArrayList<Space> space) {
        snakeloc.add(space.get(2));
        snakeloc.add(space.get(6));
        snakeloc.add(space.get(8));
        snakeloc.add(space.get(14));
        snakeloc.add(space.get(21));
        snakeloc.add(space.get(37));
        snakeloc.add(space.get(53));
        snakeloc.add(space.get(67));
        snakeloc.add(space.get(71));
        snakeloc.add(space.get(85));
        downsnake.add(space.get(11));
        downsnake.add(space.get(56));
        downsnake.add(space.get(29));
        downsnake.add(space.get(34));
        downsnake.add(space.get(52));
        downsnake.add(space.get(59));
        downsnake.add(space.get(72));
        downsnake.add(space.get(97));
        downsnake.add(space.get(91));
        downsnake.add(space.get(94));
    }
}

class Ladder{
    ArrayList<Space> ladderloc = new ArrayList<>();
    ArrayList<Space> upladder = new ArrayList<>();
    Ladder(ArrayList<Space> space){
        ladderloc.add(space.get(22));
        ladderloc.add(space.get(25));
        ladderloc.add(space.get(38));
        ladderloc.add(space.get(40));
        ladderloc.add(space.get(44));
        ladderloc.add(space.get(64));
        ladderloc.add(space.get(80));
        ladderloc.add(space.get(88));
        ladderloc.add(space.get(92));
        ladderloc.add(space.get(96));
        upladder.add(space.get(3));
        upladder.add(space.get(5));
        upladder.add(space.get(7));
        upladder.add(space.get(10));
        upladder.add(space.get(32));
        upladder.add(space.get(55));
        upladder.add(space.get(50));
        upladder.add(space.get(69));
        upladder.add(space.get(73));
        upladder.add(space.get(66));
    }
}

