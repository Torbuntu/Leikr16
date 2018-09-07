
import com.leikr.core.LeikrEngine; // Required for extending LeikrEngine

public class JavaTest extends LeikrEngine{

    int ballX;
    int ballY;
    int ballWidth;
    int ballHeight;

    int ballSpeedY;
    int ballSpeedX;

    public void create(){
        //println("Hello, World! From the LeikrGame script.");
        
        ballWidth = 80;
        ballHeight = 80;
        
        ballY = 0;
        ballX = getScreenWidth() / 2;
        
        ballSpeedY = 1;
        ballSpeedX = 1;
        
        //setBackgroundColor("GREEN");
    }
   
    public void moveBall(){
        if(ballY <= 0){
            ballSpeedY = 1;
        }
        if(ballY >= getScreenHeight()-32){
            ballSpeedY = -1;
        }
        if(ballX <= 0){
            ballSpeedX = 1;
        }
        if(ballX >= getScreenWidth()-32){
            ballSpeedX = -1;
        }
        ballY = ballY + ballSpeedY;
        ballX = ballX + ballSpeedX;
    }

    public void render(){
        
        
        moveBall();
        circle(ballX, ballY, 15, 4, "FILLED");
        line(10, 10, 130, 80, 6);
        //drawSprite(0, ballX, ballY, 32, 32);//testing sprite
        //drawSprite(255, 100, 100);
        drawPalette(5, 50, 10, 10);
        color(0, 10, 10);
        color(5, 20, 20);
        
    }
    
}
