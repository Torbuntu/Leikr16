
import com.leikr.core.LeikrEngine; // Required for extending LeikrEngine

public class GAME_NAME extends LeikrEngine {

    public void create(){
        super.create(); // Very important for initializing core engine variables.
        System.out.println("Hello, World! From the LeikrGame script.");        
    }
   
    public void render(){        
        drawRect(0,0,320,240,5, "filled");
    }    
}
