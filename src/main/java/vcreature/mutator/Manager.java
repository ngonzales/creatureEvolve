package vcreature.mutator;

import vcreature.genotype.Genome;

/**
 * This class handles switching between Genetics and HillClimbing.
 */
public class Manager
{

  private float switchingThreshhold = 0.001f;

  private long timeStart;
  private float startFitness;

  private Genome currentGenome;

  private  MutationType currentMutationType = MutationType.HILL;

  private GeneticManager geneticManager;
  private HillClimbingManager hillClimbingManager;

  public Manager()
  {
    initializeGenetics();
    initializeHillClimbing();
  }

  private void initializeHillClimbing()
  {
    hillClimbingManager = new HillClimbingManager();
  }

  private void initializeGenetics()
  {
    geneticManager = new GeneticManager();
  }

  public void setSwitchingThreshhold(float threshhold)
  {
    this.switchingThreshhold = threshhold;
  }

  public Genome getCurrentGenome()
  {
    return this.currentGenome;
  }

  public Genome getNextCreature(float lastFitness)
  {
      switch (currentMutationType)
      {
        case GENETIC:
          System.out.println("genetic");
          currentGenome = geneticManager.getNextGenome(lastFitness);
          break;
        case HILL:
          System.out.println("hill");
          currentGenome = hillClimbingManager.getNextCreature(lastFitness);
          break;
      }
    return currentGenome;
  }
  public void setCurrentMutationType(MutationType mutationType)
  {
    currentMutationType = mutationType;
  }

  public enum MutationType
  {
    HILL, GENETIC
  }
}
