
import com.leikr.core.LeikrEngine; // Required for extending LeikrEngine

class Pong extends LeikrEngine{
    
    int pOneScore = 0;
    int pTwoScore = 0;    

    def style = "Filled";

    int playerOneX = 5;
    int playerOneY = getScreenHeight()/2;
    int playerOneHeight = 35;

    int playerTwoX = getScreenWidth() - 10;
    int playerTwoY = getScreenHeight()/2;
    int playerTwoHeight = 35;

    int ballX = getScreenWidth() / 2 - 5;
    int ballY = getScreenHeight() / 2 - 5;
    int ballSpeedX = 0;
    int ballSpeedY = 0;
    
    int textPosX = 25;
    int textPosY = getScreenHeight() - 12;
    
    def playing = false;
    def gameOver = true;
    
    def winner = "";
    def notStart = true;
    
    
    int centerLineX = getScreenWidth() / 2;
    

    def void playerOneMove(){
        if(key("down")){
            if(playerTwoY > 0){
                playerTwoY -= 1;
            }              
        }
        if(key("up")){
            if(playerTwoY < getScreenHeight()-playerTwoHeight){
                playerTwoY += 1;
            }
        }
    }
    
    def playerTwoMove(){
        if(key("z")){
            if(playerOneY > 0){
                playerOneY -= 1;
            }              
        }
        if(key("x")){
            if(playerOneY < getScreenHeight()-playerOneHeight){
                playerOneY += 1;
            }
        }
    }

    def checkYbounds(){
        if((ballY+5) <= 5){
            ballSpeedY = ballSpeedY * -1;
        }
        if((ballY+5) >= getScreenHeight()-5){
            ballSpeedY = ballSpeedY * -1;
        }
    }

    def checkBallCollisionPlayerOne(){
        if(ballSpeedX < 0 && (ballX+5) <= 11){
            if((ballY+5) >= playerOneY && (ballY+5) <= playerOneY + 35 ){
                ballSpeedX = ballSpeedX * -1;
                pOneScore++;
            }else{
                ballSpeedY = 0;
                ballSpeedX = 0;
                playing = false;
                gameOver = true;
                winner = "Player 2";
            }
        }
        
    }
    
    def checkBallCollisionPlayerTwo(){
        if(ballSpeedX > 0 && (ballX+5) >= getScreenWidth() - 11){
            if((ballY+5) >= playerTwoY && (ballY+5) <= playerTwoY + 35){

                ballSpeedX = ballSpeedX * -1;
                pTwoScore++;
            }else{
                ballSpeedY = 0;
                ballSpeedX = 0;
                playing = false;
                gameOver = true;
                winner = "Player 1";
            }
        }
        
    }

    def ballMove(){
        if(ballSpeedX == 0 && ballSpeedY == 0){
            if(pOneScore > pTwoScore){
                ballSpeedX = 1;
                ballSpeedY = 1;
            }else{
                ballSpeedX = -1;
                ballSpeedY = -1;
            }
        }
        checkYbounds();

        checkBallCollisionPlayerOne();
        checkBallCollisionPlayerTwo();
        
        ballX += ballSpeedX;
        ballY += ballSpeedY;
    }


    def void create(){
        println("Hello, World! From the LeikrGame script.");
    }
   
    

    def void render(){
    
        rect(0, 0, 2, getScreenHeight(), 1, "Filled");
        rect(getScreenWidth()-2, 0, 2, getScreenHeight(), 1, "Filled");
        rect(0, 0, getScreenWidth(), 2, 1, "Filled");
        rect(0, getScreenHeight()-2, getScreenWidth(), 2, 1, "Filled");
        playerOneMove();
        playerTwoMove();
        if(playing){
            ballMove();
        }
        if(gameOver){
            if(notStart){
                drawText("Press space to play.", 30, 92, 0);
                
            }else{
                drawText("Press space to play again.", 30, 92, 0);
                drawText(winner+" Wins!", 50, 100, 0);

            }
        }else{
            rect(centerLineX-1, 0, 2, getScreenHeight(), 1, "Filled");
        }
        if(key("space")){
            if(notStart){
                notStart = false;
                playing = true;
            }
            pOneScore = 0;
            pTwoScore = 0;
            ballX = getScreenWidth() / 2 - 5;
            ballY = getScreenHeight() / 2 - 5;
            gameOver = false;
            playing = true;
            ballMove();
        }
        drawText("P1 Score: "+pOneScore, textPosX, textPosY, 0);
        drawText("P2 Score: "+pTwoScore, textPosX+120, textPosY, 0);
        
        rect(playerOneX, playerOneY, 5, playerOneHeight, 1, style);
        rect(playerTwoX, playerTwoY, 5, playerTwoHeight, 1, style);
        sprite(0, ballX, ballY);//testing sprite
        
    }
    
}
