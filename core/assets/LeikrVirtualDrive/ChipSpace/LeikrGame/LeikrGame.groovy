
import com.leikr.core.LeikrEngine; // Required for extending LeikrEngine

class LeikrGame extends LeikrEngine{

    def x;
    def y;
    def id;

    def p = [:];

    def void create(){
        super.create();// Very important for initializing core engine variables.
        println("Hello, World! From the LeikrGame script.");
        loadMap(1);
        x = 0;
        y = 0;

        p.x = 32;
        p.y = 32;
        p.spriteId = 4;
        p.vx = 0;
        p.vy = 0;
    }

    def solid(x, y){
    	int nx = Math.floor(x/8);
    	int ny = Math.floor(y/8);

    	return getCellTileId(nx,ny) == 3 || getCellTileId(nx,ny) == 4;
    }

    def movep(){

        if(rightKeyPressed() || rightBtnPressed()){
            p.vx = 1;
        }
        else if(leftKeyPressed() || leftBtnPressed()){
            p.vx = -1;
        }else{
            p.vx = 0;
        }

        if(solid(p.x+p.vx, (p.y+p.vy)+8) || solid(p.x+7+p.vx,(p.y+p.vy)+8) || solid(p.x+p.vx,(p.y-7+p.vy)+8) || solid(p.x+7+p.vx,(p.y-7+p.vy)+8)){
            p.vx = 0;
        }


        if (solid(p.x+p.vx, p.y-8+p.vy+8) || solid(p.x+7+p.vx, p.y-8+p.vy+8) ){
            p.vy=0;
        }
        else{
            p.vy=p.vy-0.2;
        }

        if(p.vy == 0 && (upKeyPressed() || upBtnPressed())){
            p.vy = 4.5;
        }

        if (p.vy > 0 && (solid(p.x+p.vx,p.y+p.vy+8) || solid(p.x+7+p.vx,p.y+p.vy+8))){
            p.vy=0;
        }



        p.x = p.vx + p.x;
        p.y = (int)Math.floor(p.vy + p.y);
    }

    def void render(){
        id = getCellTileId(x,y);

        drawText(Integer.toString(id), 100, 100, 9 );

        movep();
        drawSprite(p.spriteId, p.x, p.y);
        // drawRect(p.x, p.y, 8, -8, 8, "Filled");
        setCamera(p.x, p.y);
        //setCamera(0,0);

    }

}
