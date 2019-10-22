package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
      this.setShowSettings(false);
      this.setDisplayFps(false);
      this.setDisplayStatView(false);
      flyCam.setEnabled(false);
      cam.setLocation(new Vector3f(0,20,0));
      cam.lookAt(new Vector3f(0,0,0), new Vector3f(0,1,0));
      getStateManager().attach(new SceneManager());
      getStateManager().attach(new InteractionManager());
      getStateManager().attach(new DinoManager());
      getStateManager().attach(new PlayerManager());
      getStateManager().attach(new GuiManager());
      }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
