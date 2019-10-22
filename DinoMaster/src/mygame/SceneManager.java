/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Bob
 */
public class SceneManager extends AbstractAppState {
  
  private SimpleApplication app;
  private Geometry          floor;
    
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app = (SimpleApplication) app;
    initFloor();
    }
    
  public void initFloor() {
    Box box        = new Box(100,.1f,100);
    box.scaleTextureCoordinates(new Vector2f(10, 10));
    floor          = new Geometry("The Floor", box);
    Material mat   = app.getAssetManager().loadMaterial("Materials/Grass.j3m");
    floor.setMaterial(mat);
    app.getRootNode().attachChild(floor);
    floor.setLocalTranslation(0,-.5f,0);
    }
  
  @Override
  public void update(float tpf){
    
    InteractionManager inter   = app.getStateManager().getState(InteractionManager.class);
    Vector3f           moveDir = new Vector3f(0,0,0);
    boolean            up,down,left,right;
    
      //Checks the Interaction Manager for Input
      up    = inter.up;
      down  = inter.down;
      left  = inter.left;
      right = inter.right;
      
      //Sets players movement to 0
      float xMove = 0;
      float yMove = 0;
       
      //If there is any input, set the move accordingly 
      if (down) {
        yMove = 6;
        }
      
      else if (up) {
        yMove = -6;  
        }
      
      if (left) {
        xMove = -6;  
        }
      
      else if (right) {
        xMove = 6;  
        }
      
    moveDir = moveDir.addLocal(xMove,0,yMove);
    
    floor.move(moveDir.mult(tpf));
    
    if (floor.getLocalTranslation().x > 80 || floor.getLocalTranslation().x < -80) {
      floor.setLocalTranslation(0,0,0);
      }
     
    if (floor.getLocalTranslation().z > 80 || floor.getLocalTranslation().z < -80) {
      floor.setLocalTranslation(0,0,0);
      }
    
    }

  }
