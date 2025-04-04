package BackEnd.Editor;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.Random;

public class NoiseGenTest {
    @Test
    public void NL1(){
        NoiseLayer rnoise = new RandomNoiseLayer(0, 0.5, 1, 1, 1);
        NoiseLayer s2noise = new Simplex2NoiseLayer(0.5, 1, 1, 1);
        NoiseLayer s3noise = new Simplex3NoiseLayer(0, 0.5, 1, 1, 1);
        for(int i = 0; i <= 1000; i++){ //statistically exhaustive NL.1.0 case
            assertTrue(rnoise.evaluate(i % 500, (1000 - i) % 250) >= 0.5);
            assertTrue(s2noise.evaluate(i % 500, (1000 - i) % 250) >= 0.5);
            assertTrue(s3noise.evaluate(i % 500, (1000 - i) % 250) >= 0.5);
        }

        rnoise.setFloor(0);
        rnoise.setCeiling(.5);
        s2noise.setFloor(0);
        s2noise.setCeiling(.5);
        s3noise.setFloor(0);
        s3noise.setCeiling(.5);
        for(int i = 0; i <= 1000; i++){ //statistically exhaustive NL.1.1 case
            assertTrue(rnoise.evaluate(i % 500, (1000 - i) % 250) <= 0.5);
            assertTrue(s2noise.evaluate(i % 500, (1000 - i) % 250) <= 0.5);
            assertTrue(s3noise.evaluate(i, (1000 - i) % 250) <= 0.5);
        }
        s2noise.setCeiling(0.5);
        
        rnoise.setCeiling(1);
        rnoise.setAmp(.5);
        s2noise.setCeiling(1);
        s2noise.setAmp(0.5);
        s3noise.setCeiling(1);
        s3noise.setAmp(.5);
        for(int i = 0; i < 1000; i++){
            double rval = rnoise.evaluate(i % 500, (1000 - i) % 250);
            double s2val = s2noise.evaluate(i % 500, (1000 - i) % 250);
            double s3val = s2noise.evaluate(i % 500, (1000 - i) % 250);
            System.out.println(rval);
            assertTrue(rval <= 0.75);
            assertTrue(s2val <= 0.75);
            assertTrue(s3val <= 0.75);
            assertTrue(rval >= 0.25);
            assertTrue(s2val >= 0.25);
            assertTrue(s3val >= 0.25);
        }
    }

    @Test
    public void NL2(){

        Random r = new Random();
        int seed1 = r.nextInt(10000);
        int seed2 = seed1;
        NoiseLayer rn1 = new RandomNoiseLayer(seed1, 0, 1, 1, 1);
        NoiseLayer rn2 = new RandomNoiseLayer(seed2, 0, 1, 1, 1);
        NoiseLayer s3n1 = new Simplex3NoiseLayer(seed1, 0, 1, 1, 1);
        NoiseLayer s3n2 = new Simplex3NoiseLayer(seed2, 0, 1, 1, 1); //TODO: Test on Simplex2 noise once it's seed-dependant

        for(int i = 0; i < 1000; i++){ //statistically exhaustive  NL.2.0 cases
            assertTrue(rn1.evaluate(i % 500, (1000 - i % 500) % 250) == rn2.evaluate(i % 500, (1000 - i % 500) % 250));
            assertTrue(s3n1.evaluate(i % 500, (1000 - i % 500) % 250) == s3n2.evaluate(i % 500, (1000 - i % 500) % 250));
        }

        seed2 = r.nextInt(10000);
        rn2 = new RandomNoiseLayer(seed2, 0, 1, 1, 1);
        s3n2 = new Simplex3NoiseLayer(seed2, 0, 1, 1, 1); //TODO: Test on Simplex2 noise once it's seed-dependant
        
        int rUniquenessScore = 1000;
        int s3UniquenessScore = 1000;
        int rIncreaseScore = 0;
        int rDecreaseScore = 0;
        int s3IncreaseScore = 0;
        int s3DecreaseScore = 0;

        for(int i = 0; i < 1000; i++){
            double r1val = rn1.evaluate(i % 500, (1000 - i % 500) % 250);
            double r2val = rn2.evaluate(i % 500, (1000 - i % 500) % 250);
            double s31val = s3n1.evaluate(i % 500, (1000 - i % 500) % 250);
            double s32val = s3n2.evaluate(i % 500, (1000 - i % 500) % 250);
            
            if(r1val == r2val){
                rUniquenessScore--;
            }
            
            if(r2val > r1val){
                rIncreaseScore++;
            }
            
            if(r2val < r1val){
                rDecreaseScore++;
            }
            if(s31val == s32val){
                s3UniquenessScore--;
            }
            if(s32val > s31val){
                s3IncreaseScore++;
            }
            if(s32val < s31val){
                s3DecreaseScore++;
            }
            System.out.println(s31val + "    " + s32val);
        }

        assertTrue(rUniquenessScore >= 750);
        assertTrue(rIncreaseScore >= 250);
        assertTrue(rDecreaseScore >= 250);
        assertTrue(s3UniquenessScore >= 750);
        assertTrue(s3IncreaseScore >= 250);
        assertTrue(s3DecreaseScore >= 250);
    }
    
}
