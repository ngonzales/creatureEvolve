package vcreature.mainSimulation;

import vcreature.genotype.GenoFile;
import vcreature.genotype.Genome;

import java.util.LinkedList;
import java.util.Random;

/**
 * GenePool manages the list of creatures that exist.
 * Upon initialization it reads all genomes in GenePool folder
 */
public class GenePool
{
  private static final LinkedList<Genome> GENOMES;
  private static Random rand=new Random();
  static
  {
    GENOMES = GenoFile.loadGenePool();
  }

  /**
   * Return a random Genome from the pool.
   * @return a Genome
   */
  public static Genome getRandom()
  {
    synchronized (GENOMES)
    {
      int index = Math.abs(rand.nextInt()) % GENOMES.size();
      return GENOMES.get(index);
    }
  }

  /**
   * Replace 2 parents with a child.
   * @param newGenome Genome to store
   * @param parent1 Genome to remove
   * @param parent2 Genome to remove
   */
  public static void replace(Genome newGenome, Genome parent1, Genome parent2)
  {
    synchronized (GENOMES)
    {
      GENOMES.remove(parent1);
      GENOMES.remove(parent2);
      GENOMES.add(newGenome);
    }
  }

  /**
   * Replace a single parent with a child Genome
   * @param newGenome Genome to store
   * @param parent Genome to replace
   */
  public static void replace(Genome newGenome, Genome parent)
  {
    synchronized (GENOMES)
    {
      GENOMES.remove(parent);
      GENOMES.add(newGenome);
    }
  }

  /**
   * Adds a Genome to the pool
   * @param newGenome Genome to add
   */
  public static void add(Genome newGenome)
  {
    synchronized (GENOMES)
    {
      GENOMES.add(newGenome);
    }
  }
  
  public static Genome getBest()
  {
    return getRandom();
  }


}
