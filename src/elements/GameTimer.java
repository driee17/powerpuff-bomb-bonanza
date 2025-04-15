package elements;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.util.Duration;
import java.text.DecimalFormat;

public class GameTimer {
	//attributes needed for displaying the timer
    private Label timerLabel; //label for displaying the timer
    private Timeline timeline;	//timeline used to control the timer's countdown, ref: https://www.educba.com/javafx-timeline/
    private int remainingSeconds;
    private boolean timeUp; //handling the timer if done already or not

    //constructor
    public GameTimer(int startSeconds) {
        this.remainingSeconds = startSeconds;
        this.timerLabel = new Label();
        //sets the initial text for the timer
        this.timerLabel.setText("Time Left: " + formatSeconds(remainingSeconds));
       //setting the font type and size
        Font font = Font.loadFont(getClass().getResourceAsStream("/font.ttf"), 20);
        this.timerLabel.setFont(font);
        //positioning of the timer
        this.timerLabel.setLayoutX(600);
        this.timerLabel.setLayoutY(10);

        //initializes the timeline with an interval of 1 second for the timer's countdown
        this.timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    remainingSeconds--;
                    //updates the timer
                    timerLabel.setText("Time Left: " + formatSeconds(remainingSeconds));
                    //for checking if time is up
                    if (remainingSeconds <= 0) {
                        timeline.stop();
                        timeUp = true;
                    }
                }
            })
        );
        //sets timeline to repeat indefinitely
        this.timeline.setCycleCount(Animation.INDEFINITE);
    }

    //method to start timer
    public void start() {
        this.timeline.play();
    }

    //method to stop timer
    public void stop() {
        this.timeline.stop();
    }

    //getter method for timer
    public Label getTimerLabel() {
        return timerLabel;
    }

    //setter method to set listener for timer events
    public boolean isTimeUp() {
        return timeUp;
    }

    //setter method for font of timer
    public void setFont(Font font) {
        this.timerLabel.setFont(font);
    }

    //setter method for the style of timer
    public void setStyle(String style) {
        this.timerLabel.setStyle(style);
    }

    //method for the format of timer
    private String formatSeconds(int seconds) {
        DecimalFormat df = new DecimalFormat("00");
        int min = seconds / 60;
        int sec = seconds % 60;
        return df.format(min) + ":" + df.format(sec);
    }
}