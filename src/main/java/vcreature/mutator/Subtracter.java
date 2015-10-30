package vcreature.mutator;

import com.jme3.math.Vector3f;
import vcreature.genotype.*;
import vcreature.genotype.phenoConversion.ProtoBlock;
import vcreature.phenotype.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Removes genes from a genome, either random or seeded.
 */
public class Subtracter
{

  public static Genome subtractBlock(Genome genome)
  {
    Random rand = new Random();
    ArrayList<GeneBlock> geneBlocks = genome.getGENE_BLOCKS();
    ArrayList<GeneNeuron> geneNeurons = genome.getGENE_NEURONS();
    geneBlocks.remove(geneBlocks.size()-1);


    Genome newGenome =
        new Genome(genome.getRootSize(), genome.getRootEulerAngles());
    for (int i = 0; i < geneBlocks.size(); i++)
    {
      newGenome.addGeneBlock(geneBlocks.get(i));

        for (int j = 0; j < geneNeurons.size(); j++)
        {
          if (geneNeurons.get(j).BLOCK_INDEX == i)
          {
            newGenome.addGeneNeuron(geneNeurons.get(i));
          }
        }
      }
    return newGenome;
  }

  public static Genome subtractNeurons(Genome genome)
  {
    Random rand = new Random();
    Genome newGenome =
        subtractNeurons(genome, rand.nextInt(genome.getGENE_BLOCKS().size()));
    return newGenome;
  }

  public static Genome subtractNeurons(Genome genome, int index)
  {
    ArrayList<GeneBlock> geneBlocks = genome.getGENE_BLOCKS();
    ArrayList<GeneNeuron> geneNeurons = genome.getGENE_NEURONS();
    geneNeurons.remove(index);

    Genome newGenome =
        new Genome(genome.getRootSize(), genome.getRootEulerAngles());
    for (int i = 0; i < geneBlocks.size(); i++)
    {

      newGenome.addGeneBlock(geneBlocks.get(i));
      if (i != index)
      {
        for (int j = 0; j < geneNeurons.size(); j++)
        {
          if (geneNeurons.get(j).BLOCK_INDEX == i)
          {
            newGenome.addGeneNeuron(geneNeurons.get(i));
          }
        }
      }
    }
    return newGenome;
  }
}
