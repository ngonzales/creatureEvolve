package vcreature.mainSimulation;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.texture.Texture;
import com.jme3.input.controls.ActionListener;
import vcreature.genotype.GenomeCreature;
import vcreature.phenotype.Block;
import vcreature.phenotype.Creature;
import vcreature.phenotype.PhysicsConstants;
import java.awt.*;

/**
 * Created by Tess Daughton on 10/14/15.
 * JFrame to embed BulletApp inside of
 * Add user controls
 **/
public class SimAnimation extends SimpleApplication implements ActionListener
{
  private BulletAppState bulletAppState;
  private PhysicsSpace physicsSpace;
  private Analysis goodCreature = new Analysis();
  private float cameraAngle = (float) (Math.PI / 2.0);
  private float elapsedSimulationTime = 0.0f;
  private boolean runGUI = false;
  //Temporary vectors used on each frame. They here to avoid instanciating new vectors on each frame
  private Vector3f tmpVec3; //
  private Creature myCreature;
  private boolean isCameraRotating = true;

  @Override
  public void simpleInitApp()
  {
    /**
     * Set up Physics
     */
//
//    bulletAppState = new BulletAppState();
//    stateManager.attach(bulletAppState);
//    physicsSpace = bulletAppState.getPhysicsSpace();
//    //bulletAppState.setDebugEnabled(true);
//
//    physicsSpace.setGravity(PhysicsConstants.GRAVITY);
//    physicsSpace.setAccuracy(PhysicsConstants.PHYSICS_UPDATE_RATE);
//    physicsSpace.setMaxSubSteps(4);

    AppSettings settings = new AppSettings(true);
    settings.setResolution(1024, 768);
    settings.setSamples(4); //activate antialising (softer edges, may be slower.)
    settings.setVSync(true);
    settings.setFrequency(60);//Frames per second
    settings.setTitle("Flappy Bird Creature");
    setShowSettings(false);
    setSettings(settings);

    //Set up inmovable floor
//    com.jme3.scene.shape.Box floor = new com.jme3.scene.shape.Box(50f, 0.1f, 50f);
//    Material floor_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//    Texture floorTexture = assetManager.loadTexture("Textures/FloorTile.png");
//
//    floorTexture.setWrap(Texture.WrapMode.Repeat);
//    floor_mat.setTexture("ColorMap", floorTexture);
//
//    floor.scaleTextureCoordinates(new Vector2f(50, 50));
//    Geometry floor_geo = new Geometry("Floor", floor);
//    floor_geo.setMaterial(floor_mat);
//    floor_geo.setShadowMode(RenderQueue.ShadowMode.Receive);
//    floor_geo.setLocalTranslation(0, -0.11f, 0);
//    rootNode.attachChild(floor_geo);
//
//    /* Make the floor physical with mass 0.0f */
//    RigidBodyControl floor_phy = new RigidBodyControl(0.0f);
//    floor_geo.addControl(floor_phy);
//    physicsSpace.add(floor_phy);
//    floor_phy.setFriction(PhysicsConstants.GROUND_SLIDING_FRICTION);
//    floor_phy.setRestitution(PhysicsConstants.GROUND_BOUNCINESS);
//    floor_phy.setDamping(PhysicsConstants.GROUND_LINEAR_DAMPINING,
//        PhysicsConstants.GROUND_ANGULAR_DAMPINING);


    Block.initStaticMaterials(assetManager);

    /********************
     * TableMonster4LegsFlat
     * TableMonster4Legs
     * TableMonster2Legs
     * FlappyBird
     ***********************/
//
//    FlappyBirdGenoform flappy = new FlappyBirdGenoform();
//    myCreature = new GenomeCreature(physicsSpace, rootNode, flappy.getGenome());

    initLighting();
    initKeys();
    flyCam.setDragToRotate(true);
  }

  private void initLighting()
  {
    //  ust add a light to make the lit object visible!
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(0, -10, -2).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);

    //Without ambient light, the seen looks like outerspace with razer sharp black shadows.
    AmbientLight ambient = new AmbientLight();
    ambient.setColor(ColorRGBA.White.mult(0.3f));
    rootNode.addLight(ambient);

    // SHADOW
    // the second parameter is the resolution. Experiment with it! (Must be a power of 2)
    DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 2048, 2);
    dlsr.setLight(sun);
    viewPort.addProcessor(dlsr);
  }

  public void onAction(String name, boolean isPressed, float timePerFrame)
  {
    if (isPressed && name.equals("Toggle Camera Rotation"))
    {
      isCameraRotating = !isCameraRotating;
    }
    else if (name.equals("Quit"))
    {
      System.out.format("Creature Fitness (Maximium height of lowest point) = %.3f meters]\n", myCreature.getFitness());
      System.exit(0);
    }
  }

  private void initKeys()
  {
    inputManager.addMapping("Quit", new KeyTrigger(KeyInput.KEY_Q));
    inputManager.addMapping("Toggle Camera Rotation", new KeyTrigger(KeyInput.KEY_P));

    // Add the names to the action listener.
    inputManager.addListener(this, "Quit");
    inputManager.addListener(this, "Toggle Camera Rotation");
  }

  /* Use the main event loop to trigger repeating actions. */
  @Override
  public void simpleUpdate(float deltaSeconds)
  {
    elapsedSimulationTime += deltaSeconds;
    //print("simpleUpdate() elapsedSimulationTime=", (float)elapsedSimulationTime);
    //print("simpleUpdate() joint1.getHingeAngle()=", joint1.getHingeAngle());
//    myCreature.updateBrain(elapsedSimulationTime);

    if (isCameraRotating)
    {
      //Move camera continously in circle of radius 25 meters centered 10 meters
      //  above the origin.
      cameraAngle += deltaSeconds * 2.0 * Math.PI / 60.0; //rotate full circle every minute
      float x = (float) (25.0 * Math.cos(cameraAngle));
      float z = (float) (25.0 * Math.sin(cameraAngle));

      tmpVec3 = new Vector3f(x, 10.0f, z);
      cam.setLocation(tmpVec3);
      cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
    }
  }
}

