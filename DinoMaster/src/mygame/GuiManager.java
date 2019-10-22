/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.font.LineWrapMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.texture.Texture;
import tonegod.gui.controls.extras.android.Joystick;
import tonegod.gui.controls.text.TextElement;
import tonegod.gui.core.Screen;
import tonegod.gui.effects.Effect;

/**
 *
 * @author Bob
 */
public class GuiManager extends AbstractAppState {

  private SimpleApplication app;
  private AppStateManager   stateManager;
  private AssetManager      assetManager;
  private Joystick          stick;
  private Screen            screen;
  private TextElement       scoreDisplay;
  public  TextElement       titleDisplay;
  private BitmapFont        font;
  private int               dinosEaten;
  private boolean           isAndroid;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app){
    super.initialize(stateManager, app);
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.assetManager = this.app.getAssetManager();
    screen            = new Screen(app, "tonegod/gui/style/atlasdef/style_map.gui.xml");
    dinosEaten      = stateManager.getState(PlayerManager.class).dinosEaten;
    font              = this.assetManager.loadFont("Interface/Impact.fnt");
    isAndroid         = "Dalvik".equals(System.getProperty("java.vm.name"));
    screen.setUseTextureAtlas(true,"tonegod/gui/style/atlasdef/atlas.png");
    screen.setUseMultiTouch(true);
    app.getInputManager().setSimulateMouse(true);
    this.app.getGuiNode().addControl(screen);
    
    initScoreDisplay();
    initTitleDisplay();
    initJoystick();
    
    }
  
  private void initScoreDisplay() {
    scoreDisplay = new TextElement(screen, "ScoreText", Vector2f.ZERO, new Vector2f(300,50), font) {
    @Override
    public void onUpdate(float tpf) { }
    @Override
    public void onEffectStart() { }
    @Override
    public void onEffectStop() { }
    };
    
    //Sets up the details of the score display
    scoreDisplay.setIsResizable(false);
    scoreDisplay.setIsMovable(false);
    scoreDisplay.setTextWrap(LineWrapMode.NoWrap);
    scoreDisplay.setTextVAlign(VAlign.Center);
    scoreDisplay.setTextAlign(Align.Center);
    scoreDisplay.setFontSize(18);
 
    //Add the score display
    screen.addElement(scoreDisplay);
    scoreDisplay.setFontColor(ColorRGBA.Red);
    
    scoreDisplay.setText("Dinos Eaten: " + dinosEaten);
    scoreDisplay.setPosition(screen.getWidth() / 1.1f - scoreDisplay.getWidth()/1.8f, screen.getHeight() / 1.05f - scoreDisplay.getHeight() );
    }
  
  private void initTitleDisplay() {
    titleDisplay = new TextElement(screen, "TitleText", Vector2f.ZERO, new Vector2f(300,50), font) {
    @Override
    public void onUpdate(float tpf) { }
    @Override
    public void onEffectStart() { }
    @Override
    public void onEffectStop() { }
    };
    
    //Sets up the details of the title display
    titleDisplay.setIsResizable(false);
    titleDisplay.setIsMovable(false);
    titleDisplay.setTextWrap(LineWrapMode.NoWrap);
    titleDisplay.setTextVAlign(VAlign.Center);
    titleDisplay.setTextAlign(Align.Center);
    titleDisplay.setFontSize(25);
 
    //Add the title display
    screen.addElement(titleDisplay);
    titleDisplay.setFontColor(ColorRGBA.Red);
    
    titleDisplay.setText("Dino Master");
    titleDisplay.setPosition(screen.getWidth() / 2f - titleDisplay.getWidth()/2f, screen.getHeight() / 2f - titleDisplay.getHeight()/2);
    }
  
  private void initJoystick() {
    stick = new Joystick(screen, Vector2f.ZERO, (int)screen.getWidth()/6) {
    @Override
    public void onUpdate(float tpf, float deltaX, float deltaY) {
        
        float dzVal = .2f; // Dead zone threshold
        
            if (deltaX < -dzVal) {
              stateManager.getState(InteractionManager.class).left  = true;
              stateManager.getState(InteractionManager.class).right = false;
              } 
            
            else if (deltaX > dzVal) {
              stateManager.getState(InteractionManager.class).right = true;
              stateManager.getState(InteractionManager.class).left  = false;
              }
            
            else {
              stateManager.getState(InteractionManager.class).right = false;
              stateManager.getState(InteractionManager.class).left  = false; 
              }
            
        
            if (deltaY < -dzVal) {
              stateManager.getState(InteractionManager.class).down = true;
              stateManager.getState(InteractionManager.class).up   = false;
              } 
            
            else if (deltaY > dzVal) {
              stateManager.getState(InteractionManager.class).down = false;
              stateManager.getState(InteractionManager.class).up   = true;
              }
            
            else {
              stateManager.getState(InteractionManager.class).up   = false;
              stateManager.getState(InteractionManager.class).down = false;    
              }
            
            }
    
          };
    
      // getGUIRegion returns region info “x=0|y=0|w=50|h=50″, etc
      TextureKey key = new TextureKey("Textures/barrel.png", false);
      Texture tex = assetManager.loadTexture(key);
      stick.setTextureAtlasImage(tex, "x=20|y=20|w=120|h=35");
      stick.getThumb().setTextureAtlasImage(tex, "x=20|y=20|w=120|h=35");
      screen.addElement(stick, true);
      stick.setPosition(screen.getWidth()/10 - stick.getWidth()/2, screen.getHeight()/10 - stick.getHeight()/2);
      // Add some fancy effects
      Effect fxIn = new Effect(Effect.EffectType.FadeIn, Effect.EffectEvent.Show,.5f);
      stick.addEffect(fxIn);
      Effect fxOut = new Effect(Effect.EffectType.FadeOut, Effect.EffectEvent.Hide,.5f);
      stick.addEffect(fxOut);
      stick.hide();
      }
  
  public void hideJoystick() { 
    stick.hide();
    }
  
  public void showJoystick() {
    if (isAndroid)  
    stick.show();
    }
  
  public void hideTitle() {
    titleDisplay.hide();
    }
  
  public void showTitle() {
    titleDisplay.show();
    }
  
  public void displayScores() {
    int highScore  = stateManager.getState(PlayerManager.class).highScore;
    int finalScore = stateManager.getState(PlayerManager.class).dinosEaten;
    
    if (finalScore > highScore) {
    titleDisplay.setText("New High Score: " + finalScore);
    titleDisplay.setFontColor(ColorRGBA.Yellow);
    }
    else {    
    titleDisplay.setText("High Score: " + highScore);
    }
    scoreDisplay.setFontSize(25);
    scoreDisplay.setPosition(screen.getWidth() / 2f - scoreDisplay.getWidth()/2f, screen.getHeight() / 2f - scoreDisplay.getHeight()/2 - screen.getHeight()/10);
    }
  
  public void resetScore(){
    scoreDisplay.setPosition(screen.getWidth() / 1.1f - scoreDisplay.getWidth()/1.8f, screen.getHeight() / 1.05f - scoreDisplay.getHeight());    
    scoreDisplay.setFontSize(18);
    }
  
  @Override
  public void update(float tpf) {
    scoreDisplay.setText("Dinos Eaten: " + stateManager.getState(PlayerManager.class).dinosEaten);
    }
  
  }
