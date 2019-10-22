/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.Random;

/**
 *
 * @author Bob
 */
public class DinoManager extends AbstractAppState {
    
  private SimpleApplication   app;
  private Random              rand;
  public  Node                dinoNode;
  private InteractionManager  inter;
  private boolean             up,down,left,right;
  private Material            red,green,blue;
    
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app = (SimpleApplication) app;
    inter    = app.getStateManager().getState(InteractionManager.class);
    init();
    setEnabled(false);
    }
  
  private void init(){
    dinoNode        = new Node();
    rand            = new Random();
    blue            = app.getAssetManager().loadMaterial("Materials/Blue.j3m");
    red             = app.getAssetManager().loadMaterial("Materials/Red.j3m");
    green           = app.getAssetManager().loadMaterial("Materials/Green.j3m");
    app.getRootNode().attachChild(dinoNode);
    }
  
  private void createDino() {
    Dino dino        = new Dino();
    Node model       = (Node) app.getAssetManager().loadModel("Models/Dino.j3o");
    AnimControl ac   = ((Node) model.getChild("Dino")).getChild("Body skin").getControl(AnimControl.class);
    AnimChannel anch = ac.createChannel();
    anch.setAnim("Run");
    model.setName("Model");
    dino.attachChild(model);
    dinoNode.attachChild(dino);
    randomizeDino(dino);
    }
  
  private void randomizeDino(Dino dino) {
      
    float x     = randInt(0, 50)-25;
    float y     = randInt(0, 50)-25;  
    
    x = x/2;
    y = y/2;
    
    if (x>y) {
        
      if (x>0) {
        x = 15.5f;
        dino.moveDir = new Vector3f(-1,0,0);
        dino.rotate(0,-89.5f,0);
        }
      
      else {
        x = -15.5f;
        dino.moveDir = new Vector3f(1,0,0);
        dino.rotate(0,89.5f,0);
        }
      
      }
    
    else {
        
      if (y>0) {
        y = 15.5f;
        dino.moveDir = new Vector3f(0,0,-1);
        dino.rotate(0,179f,0);
        }
      
      else {
        y = -15.5f;
        dino.moveDir = new Vector3f(0,0,1);
        }
      
      }
    
    dino.speed   = randInt(3,7);
    dino.moveDir = dino.moveDir.mult(dino.speed);
    dino.size    = (float) randInt(1,10)/10;
    dino.setLocalTranslation(x, 0, y);
    changeSize(dino);
    changeColor(dino);
    }
  
  private void changeColor(Dino dino){
    
    Node body = (Node) ((Node) ((Node) dino.getChild("Model")).getChild("Dino")).getChild("Body skin");
      
    int random = randInt(1,3);
    if (random == 1)
    body.setMaterial(green);
    else if (random == 2)
    body.setMaterial(blue);    
    else if (random == 3)
    body.setMaterial(red);    
    
    }
  
  private void changeSize(Dino dino){
    dino.setLocalScale(dino.size);
    }
  
  private int randInt(int min, int max) {
    int    randomNum = rand.nextInt((max - min) + 1) + min;
    return randomNum;
    }
  
  @Override
  public void update(float tpf) {
    
    //Creates a dino if there is less than 10 dinos  
    if (dinoNode.getQuantity() < 10) {
      createDino();
      }
    
    //Checks Each Dino
    for (int i = 0; i < dinoNode.getQuantity(); i++) {
      
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
      
      //Gets the Current Dino
      Dino dino = (Dino) dinoNode.getChild(i);
      
      //Actually is doing the moving of the dino
      dino.move((dino.moveDir.add(xMove,0,yMove)).mult(tpf));
      
      //Remove the Dino if it is too far away
      if (dino.getLocalTranslation().x > 16 ^ dino.getLocalTranslation().x < -16)
      dino.removeFromParent();
      
      //Remove the Dino if it is too far away
      if (dino.getLocalTranslation().z > 16 ^ dino.getLocalTranslation().z < -16)
      dino.removeFromParent();
      
      //Checks each dino for collision with Current Dino
      for (int j = 0; j < dinoNode.getQuantity(); j++) {
       
        //Gets the Current Dino
        Dino currentDino         = (Dino) dinoNode.getChild(j);
        CollisionResults results = new CollisionResults();
        
        //Checks to be sure it is not checking itself, then checks collision with current dino.
        if (dino != currentDino) 
        ((Node)dino.getChild("Model")).getChild("Collider").collideWith(((Node)currentDino.getChild("Model")).getChild("Collider").getWorldBound(), results);
        
        //Checks if a Dino is Hit
        if (results.size() > 0) {
          
          //If Current Dino is Bigger than the Hit Dino Make it Bigger and remove Hit Dino 
          if (currentDino.size > dino.size) {
            dino.removeFromParent();
            if (currentDino.size < .12) {
              currentDino.size = dino.size + .1f;
              changeSize(currentDino);
              }
            }
          
          //If Current Dino is Smaller than the Hit Dino Remove It and make Hit Dino Bigger
          else {
            currentDino.removeFromParent();
            if (dino.size < .12) {
              dino.size = dino.size + .1f;
              changeSize(dino);
              }
            }
            
          }          
          
        }
      
      } 
    
    }
    
  }

