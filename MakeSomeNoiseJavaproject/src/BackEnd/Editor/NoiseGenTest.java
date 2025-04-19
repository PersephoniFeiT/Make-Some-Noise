package BackEnd.Editor;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

public class NoiseGenTest {
    @Test
    public void NL1(){
        NoiseLayer rnoise = new RandomNoiseLayer(0, 0.5, 1, 0, 1, 1, BlendMode.MULTIPLY);
        NoiseLayer s2noise = new Simplex2NoiseLayer(0, 0.5, 1, 0, 1, 1, BlendMode.MULTIPLY);
        NoiseLayer s3noise = new Simplex3NoiseLayer(0, 0.5, 1, 0, 1, 1, BlendMode.MULTIPLY);
        for(int i = 0; i <= 1000; i++){ //statistically exhaustive NL.1.0 case
            assertTrue(rnoise.evaluate(i % 500, (1000 - i) % 250) >= 0.5);
            assertTrue(s2noise.evaluate(i % 500, (1000 - i) % 250) >= 0.5);
            assertTrue(s3noise.evaluate(i % 500, (1000 - i) % 250) >= 0.5);
        }

        rnoise.setFloor(0);
        rnoise.setCeiling(.5);
        System.out.println(rnoise.getCeiling());
        s2noise.setFloor(0);
        s2noise.setCeiling(.5);
        s3noise.setFloor(0);
        s3noise.setCeiling(.5);
        for(int i = 0; i <= 1000; i++){ //statistically exhaustive NL.1.1 case
            double v = rnoise.evaluate(i % 500, (1000 - i) % 250);
            System.out.println(v);
            assertTrue(v <= 0.5);
            assertTrue(s2noise.evaluate(i % 500, (1000 - i) % 250) <= 0.5);
            assertTrue(s3noise.evaluate(i % 500, (1000 - i) % 250) <= 0.5);
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
            //System.out.println(rval);
            assertTrue(rval <= 0.75);
            assertTrue(s2val <= 0.75);
            assertTrue(s3val <= 0.75);
            assertTrue("RandomNoiseLayer broke lower amplitude constraints: " + rval, rval >= 0.25);
            assertTrue(s2val >= 0.25);
            assertTrue(s3val >= 0.25);
        }
    }

    @Test
    public void NL2(){

        Random r = new Random();
        int seed1 = r.nextInt(10000);
        int seed2 = seed1;
        NoiseLayer rn1 = new RandomNoiseLayer(seed1, 0, 1, 0, 1, 1, BlendMode.MULTIPLY);
        NoiseLayer rn2 = new RandomNoiseLayer(seed2, 0, 1, 0, 1, 1, BlendMode.MULTIPLY);
        NoiseLayer s2n1 = new Simplex2NoiseLayer(seed1, 0, 1, 0, 1,1, BlendMode.MULTIPLY);
        NoiseLayer s2n2 = new Simplex2NoiseLayer(seed2, 0, 1, 0, 1, 1, BlendMode.MULTIPLY);
        NoiseLayer s3n1 = new Simplex3NoiseLayer(seed1, 0, 1, 0, 1, 1, BlendMode.MULTIPLY);
        NoiseLayer s3n2 = new Simplex3NoiseLayer(seed2, 0, 1, 0, 1, 1, BlendMode.MULTIPLY); //TODO: Test on Simplex2 noise once it's seed-dependant

        for(int i = 0; i < 1000; i++){ //statistically exhaustive  NL.2.0 cases
            assertTrue(rn1.evaluate(i % 500, (1000 - i % 500) % 250) == rn2.evaluate(i % 500, (1000 - i % 500) % 250));
            assertTrue(s2n1.evaluate(i % 500, (1000 - i % 500) % 250) == s2n2.evaluate(i % 500, (1000 - i % 500) % 250));
            assertTrue(s3n1.evaluate(i % 500, (1000 - i % 500) % 250) == s3n2.evaluate(i % 500, (1000 - i % 500) % 250));
        }

        seed2 = r.nextInt(10000);
        rn2 = new RandomNoiseLayer(seed2, 0, 1, 0, 1, 1, BlendMode.MULTIPLY);
        s3n2 = new Simplex3NoiseLayer(seed2, 0, 1, 0, 1, 1, BlendMode.MULTIPLY); //TODO: Test on Simplex2 noise once it's seed-dependant
        
        int rUniquenessScore = 1000;
        int s3UniquenessScore = 1000;
        int rIncreaseScore = 0;
        int rDecreaseScore = 0;
        int s3IncreaseScore = 0;
        int s3DecreaseScore = 0;

        for(int i = 0; i < 1000; i++){ //Statistically exhaustive NL.2.1 case. Nondeterministic so there is a very very small chance of failure, but as long as it passes most of the time this functional requirement is met.
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
            //System.out.println(s31val + "    " + s32val);
        }

        assertTrue(rUniquenessScore >= 750);
        assertTrue(rIncreaseScore >= 250);
        assertTrue(rDecreaseScore >= 250);
        assertTrue(s3UniquenessScore >= 750);
        assertTrue(s3IncreaseScore >= 250);
        assertTrue(s3DecreaseScore >= 250);
    }

    @Test
    public void NL3(){
        double k = 4;
        RandomNoiseLayer rOracle = new RandomNoiseLayer(0, 0, 1, 0, 1, 1, BlendMode.MULTIPLY);
        RandomNoiseLayer rAmpl = new RandomNoiseLayer(0, 0, 1, 0, 1/k, 1, BlendMode.MULTIPLY);
        Simplex2NoiseLayer s2Oracle = new Simplex2NoiseLayer(0, 0, 1, 0, 1, 0, BlendMode.MULTIPLY);
        Simplex2NoiseLayer s2Ampl = new Simplex2NoiseLayer(0, 0, 1, 0, 1/k, 1, BlendMode.MULTIPLY);
        Simplex3NoiseLayer s3Oracle = new Simplex3NoiseLayer(0, 0, 1, 0, 1, 1, BlendMode.MULTIPLY);
        Simplex3NoiseLayer s3Ampl = new Simplex3NoiseLayer(0, 0, 1, 0, 1/k, 1, BlendMode.MULTIPLY);
        for (int i = 0; i < 1000; i++){ //statistically exhaustive  NL.3.0 case
            double rOrVal = rOracle.evaluate(i % 500, (1000 - i % 500) % 250);
            double rval = rAmpl.evaluate(i % 500, (1000 - i % 500) % 250);
            double rNorm = (rval - 0.5) * k + 0.5;

            double s2OrVal = s2Oracle.evaluate(i % 500, (1000 - i % 500) % 250);
            double s2val = s2Ampl.evaluate(i % 500, (1000 - i % 500) % 250);
            double s2Norm = (s2val - 0.5) * k + 0.5;

            double s3OrVal = s3Oracle.evaluate(i % 500, (1000 - i % 500) % 250);
            double s3val = s3Ampl.evaluate(i % 500, (1000 - i % 500) % 250);
            double s3Norm = (s3val - 0.5) * k + 0.5;

            assertTrue(rNorm < rOrVal + 0.00005); //Using a stict margin of error for floating point errors.
            assertTrue(rNorm > rOrVal - 0.00005);

            assertTrue(s2Norm < s2OrVal + 0.00005);
            assertTrue(s2Norm > s2OrVal - 0.00005);

            //System.out.println(s3OrVal + "     " + s3val + "     " + s3Norm);
            assertTrue(s3Norm < s3OrVal + 0.00005);
            assertTrue(s3Norm > s3OrVal - 0.00005);
        }
    }

    @Test
    public void NL4(){
        int m = 3;
        int n = 4;
        ArrayList<NoiseLayer> layerList = new ArrayList<>();
        layerList.add(new NoiseLayer(){ //anonymous noiselayer mock for evaluating to m
            @Override
            public double evaluate(int x, int y) {
                return m;
            }
            @Override
            public int getSeed() {return 1;}
            @Override
            public double getFreq() {return 1;}
            @Override
            public double getAmp() {return 1;}
            public double getFloor() {return 1;}
            @Override
            public double getCeiling() {return 1;}
            @Override
            public double getGain() {return 1;}
            @Override
            public void setFreq(double newFreq) {}
            @Override
            public void setAmp(double newAmp) {}
            @Override
            public void setFloor(double newFloor) {}
            @Override
            public void setCeiling(double newCeiling) {}
            @Override
            public void setGain(double newGain) {}
            @Override
            public void setBlendMode(BlendMode m){}
            @Override
            public BlendMode getBlendMode(){return null;}
        });
        layerList.add(new NoiseLayer(){ //anonymous noiselayer mock for evaluating to n
            @Override
            public double evaluate(int x, int y) {
                return n;
            }
            @Override
            public int getSeed() {return 1;}
            @Override
            public double getFreq() {return 1;}
            @Override
            public double getAmp() {return 1;}
            public double getFloor() {return 1;}
            @Override
            public double getCeiling() {return 1;}
            @Override
            public double getGain() {return 1;}
            @Override
            public void setFreq(double newFreq) {}
            @Override
            public void setAmp(double newAmp) {}
            @Override
            public void setFloor(double newFloor) {}
            @Override
            public void setCeiling(double newCeiling) {}
            @Override
            public void setGain(double newGain) {}
            @Override
            public void setBlendMode(BlendMode m){}
            @Override
            public BlendMode getBlendMode(){return null;}
        });

        double[][] vals = LayerManager.multiplyLayers(100, 100, layerList);
        for(int i = 0; i < 100; i++){
            for(int j = 0; j < 100; j++){
                assertTrue(vals[i][j] == m*n);
            }
        }
    }
}
