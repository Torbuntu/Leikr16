import com.leikr.core.LeikrEngine;

class LeikrGame extends LeikrEngine{

    def x;
    def y;
    def id;

    def p = [:];
    def void create(){

        println("Hello, World! From the LeikrGame script.");
        loadMap(); //This game uses the provided map files.
        x = 0;
        y = 0;

        p.x = 30;
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

        if(key("right") || button("right")){
            p.vx = 1;
        }
        else if(key("left") || button("left")){
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

        if(p.vy == 0 && (key("up") || button("up"))){
            p.vy = 4.5;
            playBeep(0.1, 340.5, 0.5, "triangle");
        }

        if (p.vy > 0 && (solid(p.x+p.vx,p.y+p.vy+8) || solid(p.x+7+p.vx,p.y+p.vy+8))){
            p.vy=0;                        
        }

        p.x = p.vx + p.x;
        p.y = (int)Math.floor(p.vy + p.y);
    }

    def void render(){
        rect(0,0,320,240,5, "filled");

        // setMapSection(0, 0);
        drawMap();
        //setCellTile(p.x/8, (p.y-8)/8, 3);
        movep();
        sprite(p.spriteId, p.x, p.y);
        //setCamera(p.x, p.y);

    }
}