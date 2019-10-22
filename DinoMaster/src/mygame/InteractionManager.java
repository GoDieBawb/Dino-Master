/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Bob
 */
public class InteractionManager extends AbstractAppState implements ActionListener {

  private SimpleApplication app;
  private AppStateManager   stateManager;
  private AssetManager      assetManager;
  private InputManager      inputManager;
  public  boolean           up,down,left,right,dead;
  private int               deathCount;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app){
    super.initialize(stateManager, app);
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.assetManager = this.app.getAssetManager();
    this.inputManager = this.app.getInputManager();
    dead              = true;
    setUpKeys();
    }
  
  //Sets up the keys
  public void setUpKeys() {
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    inputManager.addListener(this, "Up");
    inputManager.addListener(this, "Down");
    inputManager.addListener(this, "Left");
    inputManager.addListener(this, "Right");
    inputManager.addListener(this, "Click");
    }
  
  public void onAction(String binding, boolean isPressed, float tpf) {
  
  if (!dead) {    
      
    if (binding.equals("Up")) {
      up = isPressed;
      }
    
    else if (binding.equals("Down")) {
      down = isPressed;
      }

    else if (binding.equals("Left")) {
      left = isPressed;
      }  

    else if (binding.equals("Right")) {
      right = isPressed;
      } 
   
    
    }
  
  else {
      
    if (deathCount == 0) {
      deathCount++;
      stateManager.getState(GuiManager.class).displayScores();
      }
    
    else if (deathCount == 2) {
      deathCount++;
      stateManager.getState(GuiManager.class).titleDisplay.setFontColor(ColorRGBA.Red);
      stateManager.getState(GuiManager.class).titleDisplay.setText("Tap to Start");
      stateManager.getState(GuiManager.class).resetScore();
      }
    
    else if (deathCount > 3){
      dead       = false;
      deathCount = 0;
      stateManager.getState(DinoManager.class).setEnabled(true);
      stateManager.getState(PlayerManager.class).dinosEaten = 0;
      stateManager.getState(GuiManager.class).hideTitle();
      stateManager.getState(GuiManager.class).showJoystick();
      }
    
    else {
      deathCount++;
      }
    
    
      }
      
    }
  
  @Override
  public void update(float tpf) {
    if (up||down||left||right){
      }
    else{
      stateManager.getState(PlayerManager.class).animChannel.setAnim("Run");  
      }
    }
  
  }
