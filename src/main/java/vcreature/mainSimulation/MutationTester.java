package vcreature.mainSimulation;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Geometry;
import com.jme3.system.AppSettings;
import vcreature.genotype.GenomeCreature;
import vcreature.mutator.Manager;
import vcreature.phenotype.PhysicsConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * MutationTester is the background application for mutations and Genetic merging.
 * It contains the physics space for actual testing.
 */
class MutationTester extends SimpleApplication implements ActionListener
{
  private PhysicsSpace physicsSpace;

  private boolean speedSet = false;
  private final int SPEEDSETTING = 20;
  private Timer currentBestFitness = new Timer(0,this);

  //Temporary vectors used on each frame. They here to avoid instanciating new vectors on each frame
  private GenomeCreature currentCreature;
  private float elapsedSimulationTime;
  private Manager manager;

  private float previousFitness = -1;


  public MutationTester(Manager manager)
  {
    this.manager = manager;
  }

  /**
   * Initalizes a BulletAppState and a Physics Space
   * Sets relevant Physics constants
   * Creates AppSettings for this SimpleApp
   * Creates a new Creature
   */
  @Override
  public void simpleInitApp()
  {
    BulletAppState bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    physicsSpace = bulletAppState.getPhysicsSpace();
    physicsSpace.setGravity(PhysicsConstants.GRAVITY);
    physicsSpace.setAccuracy(PhysicsConstants.PHYSICS_UPDATE_RATE);
    AppSettings settings = new AppSettings(true);
    setSettings(settings);
    settings.setResolution(1024, 768);
    this.setPauseOnLostFocus(false);
    com.jme3.scene.shape.Box floor = new com.jme3.scene.shape.Box(50f, 0.1f, 50f);
    Geometry floor_geo = new Geometry("Floor", floor);
    floor_geo.setLocalTranslation(0, -0.11f, 0);
    rootNode.attachChild(floor_geo);

    RigidBodyControl floor_phy = new RigidBodyControl(0.0f);
    floor_geo.addControl(floor_phy);
    physicsSpace.add(floor_phy);
    floor_phy.setFriction(PhysicsConstants.GROUND_SLIDING_FRICTION);
    floor_phy.setRestitution(PhysicsConstants.GROUND_BOUNCINESS);
    floor_phy.setDamping(PhysicsConstants.GROUND_LINEAR_DAMPINING,
                         PhysicsConstants.GROUND_ANGULAR_DAMPINING);

    currentCreature = new GenomeCreature(physicsSpace, rootNode, manager.getNextCreature(-1));
    currentBestFitness.setDelay(60000);
    currentBestFitness.start();
  }


  /**
   * Use the main event loop to trigger repeating actions.
   *
   * @param deltaSeconds change in simulation time seconds.
   */
  @Override
  public void simpleUpdate(float deltaSeconds)
  {
    try
    {
      //Just needs to happen the first time. Doesn't appear to work if called in initapp.
      if (!this.speedSet)
      {
        this.setSpeed(SPEEDSETTING);
        this.speedSet = true;
      }

      //System.out.println(deltaSeconds);
      float currentFitness = currentCreature.updateBrain(elapsedSimulationTime);
      elapsedSimulationTime += deltaSeconds;

      if (elapsedSimulationTime < 4f && currentFitness > 1.0f)
      {
        currentCreature.remove();
        previousFitness = -1;
        elapsedSimulationTime = 0;
        currentCreature = new GenomeCreature(physicsSpace, rootNode, manager.getNextCreature(0.0f));
        return;
      }
      if ((elapsedSimulationTime > 10 && currentFitness < previousFitness) || elapsedSimulationTime > 20)
      {
        currentCreature.remove();
        elapsedSimulationTime = 0;
        previousFitness = -1;

        currentCreature = new GenomeCreature(physicsSpace, rootNode, manager.getNextCreature(currentFitness));
      }
      previousFitness = currentFitness;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

  }

  /**
   * Sets speeds of headless GUI and bullet physics engine
   * @param speed
   */
  private void setSpeed(int speed)
  {
    this.speed = speed;
    physicsSpace.setMaxSubSteps(4 * speed);
    settings.setFrequency(speed * 60);
    this.restart();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
      System.out.println("Best Fitness So Far: "  + GenePool.getBest().getFitness());
  }
}

