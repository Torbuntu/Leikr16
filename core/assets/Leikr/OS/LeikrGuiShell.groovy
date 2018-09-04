
import com.leikr.core.DesktopEnvironment.LeikrDesktopEngine; // Required for extending LeikrEngine

class LeikrGuiShell extends LeikrDesktopEngine{

    def void create(){
        setAppMenu(0,0,"Logo.png");

    }

    def void render(){
       solidBg(0.3f, 0.3f, 0.3f);

    }
}