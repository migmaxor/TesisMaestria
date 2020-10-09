/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

import java.util.Random;
import java.util.Vector;

/**
 *
 * @author migma
 */
public class Genome 
{
    //Attributes which are arrays 3-dimensional that are used to store informations as follows:
    //array[0]= upper deviation of the main datum.
    //array[1]= main datum.
    //array[2]= lower deviation of the main datum.
    private double [] age;
    private int gender; // 0= female. 1= male
    private double [] pressure;
    private double [] cholesterol;
    private int sugar; //criterio: azucar > 120 mg/dl -> 1= true. 0= false
    private int electro; //0= normal. 1= anormal
    private double [] hearRate;
    private int type; //0= healthy genome. 1= sick genome.
    
    private int correctDiagnosis; //Ca
    private int diagnosableCases; //Ce
    private int possibleDiagnosis; //Dd
    private double fitness;

    public double getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
    
    public Genome(double upperAge, double age, double lowerAge, int gender, double upperPressure, double pressure,
                double lowerPressure, double upperCholesterol, double cholesterol, double lowerCholesterol,
                int sugar, int electro, double upperHearRate, double hearRate,
                double lowerHeartRate, int type)
    {
        this.age= new double[] {upperAge, age, lowerAge};
        this.gender= gender;
        this.pressure= new double[]{upperPressure, pressure, lowerPressure};
        this.cholesterol= new double[]{upperCholesterol, cholesterol, lowerCholesterol};
        this.sugar= sugar;
        this.electro= electro;
        this.hearRate= new double[]{upperHearRate, hearRate, lowerHeartRate};
        this.type= type;        
    }
    
    public Genome()
    {
        
    }
    
    public double calculateFitness()
    {
        try
        {
            //1 cases: 165
            //0 cases: 138
            /*if(type==0)
            {
                diagnosableCases= 138;
            }else
            {
                diagnosableCases= 165;
            }*/
            fitness= (double)correctDiagnosis/(double)possibleDiagnosis; //+ ((double)possibleDiagnosis/(double)diagnosableCases))/2.0;
            //fitness= (correctDiagnosis/diagnosableCases)+(possibleDiagnosis/diagnosableCases);
        }catch(ArithmeticException e)
        {
            fitness= 0.0;
        }        
        return fitness;
    }
    
    private boolean firstIteration= true;
    public Vector<Genome> reproduce(Genome mother)
    {
        Vector<Genome> result= new Vector(0,1);
        if(firstIteration)
        {            
        
            if(gender==mother.gender && sugar==mother.getSugar() && electro== mother.getElectro())
            {
                Genome originalChild= newChild(this, mother);
                result.add(originalChild);
                Genome deviatedChild1= deviateGenome(originalChild);
                Genome deviatedChild2= deviateGenome(originalChild);
                result.add(deviatedChild1);
                result.add(deviatedChild2);

            }else
            {        
                result=null;
            }
        }else
        {
            if(gender==mother.gender && sugar==mother.getSugar() && electro== mother.getElectro())
            {
                Genome originalChild= newSecondChild(this, mother);
                result.add(originalChild);
                Genome deviatedChild1= deviateGenome(originalChild);
                Genome deviatedChild2= deviateGenome(originalChild);
                result.add(deviatedChild1);
                result.add(deviatedChild2);
            }else
            {        
                result=null;
            }
        }
        
        
        
        /*
        if(!firstIteration)
        {
            result.add(mother);
            result.add(this);
        }
        */
        firstIteration= false;       
        
        
        return result;
    }
    
    private Genome deviateGenome(Genome g)
    {
        Genome result= new Genome(deviate(g.getAge()[0]), g.getAge()[1], deviate(g.getAge()[0]), 
                g.getGender(), 
                deviate(g.getPressure()[0]), g.getPressure()[1], deviate(g.getPressure()[0]),
                deviate(g.getCholesterol()[0]), g.getCholesterol()[1], deviate(g.getCholesterol()[0]),
                g.getSugar(), 
                g.getElectro(),                 
                deviate(g.getHearRate()[0]), g.getHearRate()[1], deviate(g.getHearRate()[0]), 
                g.getType());
        return result;
    }
    
    private double deviate(double number)
    {
        Random r= new Random();
        int n= r.nextInt(5)+1;
        return number+ (double) n; 
    }
    
    //The following method return a deviation altered randomly. +-16%
    private int newDeviation(int numberToChange)
    {
        Random r= new Random(System.currentTimeMillis());
        if(numberToChange==0)
        {
            numberToChange= r.nextInt(8)+1;
        }
        int percentageToDeviate= r.nextInt(10)+10;
        int decission= r.nextInt(2);
        int multiplier= 1;
        if(decission==1)
        {
            multiplier= -1;
        }
        return numberToChange* ((1+(percentageToDeviate/100))*multiplier);
    }
    
    private Genome newChild(Genome father, Genome mother)
    {
        double ageMedia= (father.getAge()[1]+mother.getAge()[1])/2;
        double pressureMedia= (father.getPressure()[1]+mother.getPressure()[1])/2;
        double cholMedia= (father.getCholesterol()[1]+mother.getCholesterol()[1])/2;
        double heartRateMedia= (father.getHearRate()[1]+mother.getHearRate()[1])/2;
        Genome result= new Genome(Math.abs(father.getAge()[1]-ageMedia), ageMedia, Math.abs(father.getAge()[1]-ageMedia), 
                father.getGender(), 
                Math.abs(father.getPressure()[1]-pressureMedia), pressureMedia, Math.abs(father.getPressure()[1]-pressureMedia),
                Math.abs(father.getCholesterol()[1]-cholMedia), cholMedia, Math.abs(father.getCholesterol()[1]-cholMedia),
                mother.getSugar(), 
                mother.getElectro(),                 
                Math.abs(father.getHearRate()[1]-heartRateMedia), heartRateMedia, Math.abs(father.getHearRate()[1]-heartRateMedia), 
                mother.getType());
        return result;
    }
    
    private Genome newSecondChild(Genome father, Genome mother)
    {
        Genome result= new Genome();
        result.setAge(selectData(father.getAge(), mother.getAge()));
        result.setGender(father.getGender());
        result.setPressure(selectData(father.getPressure(), mother.getPressure()));
        result.setCholesterol(selectData(father.getCholesterol(), mother.getCholesterol()));
        result.setSugar(father.getSugar());
        result.setElectro(father.getElectro());
        result.setHearRate(selectData(father.getHearRate(), mother.getHearRate()));
        result.setType(father.getType());        
        return result;
    }
    
    private double[] selectData(double[] father, double[] mother)
    {
        double[] result= new double[]{0.0, 0.0, 0.0};
        double menor= menor(father[1]-father[0], mother[1]-mother[0]);
        double mayor= mayor(father[1]+father[0], mother[1]+mother[0]);
        double promedio= (menor+mayor)/2;
        result[0]= Math.abs(promedio-menor);
        result[1]= promedio;
        result[2]= Math.abs(promedio-menor);
        return result;
    }
    
    private double mayor(double n1, double n2)
    {
        if(n1>n2)
        {
            return n1;
        }else
        {
            return n2;
        }
    }
    
    private double menor(double n1, double n2)
    {
        if(n1<n2)
        {
            return n1;
        }else
        {
            return n2;
        }
    }
    
    //The following method creates a child with average of mother and father and deviation with diferences between mother and father.
    private Genome newChildFromFather(Genome father, Genome mother)
    {
        Genome result= new Genome(Math.abs(father.getAge()[1]-mother.getAge()[1]), father.getAge()[1], Math.abs(father.getAge()[1]-mother.getAge()[1]), 
                mother.getGender(), 
                Math.abs(mother.getPressure()[1]-father.getPressure()[1]), father.getPressure()[1], Math.abs(mother.getPressure()[1]-father.getPressure()[1]),
                Math.abs(mother.getCholesterol()[1]-father.getCholesterol()[1]),father.getCholesterol()[1], Math.abs(mother.getCholesterol()[1]-father.getCholesterol()[1]),
                mother.getSugar(), 
                mother.getElectro(),                 
                Math.abs(mother.getHearRate()[1]-father.getHearRate()[1]), father.getHearRate()[1], Math.abs(mother.getHearRate()[1]-father.getHearRate()[1]), 
                mother.getType());
        return result;
    }
    
    private Genome newChildFromMother(Genome father, Genome mother)
    {
        Genome result= new Genome(Math.abs(father.getAge()[1]-mother.getAge()[1]), mother.getAge()[1], Math.abs(father.getAge()[1]-mother.getAge()[1]), 
                mother.getGender(), 
                Math.abs(mother.getPressure()[1]-father.getPressure()[1]), mother.getPressure()[1], Math.abs(mother.getPressure()[1]-father.getPressure()[1]),
                Math.abs(mother.getCholesterol()[1]-father.getCholesterol()[1]), mother.getCholesterol()[1], Math.abs(mother.getCholesterol()[1]-father.getCholesterol()[1]),
                mother.getSugar(), 
                mother.getElectro(),                 
                Math.abs(mother.getHearRate()[1]-father.getHearRate()[1]), mother.getHearRate()[1], Math.abs(mother.getHearRate()[1]-father.getHearRate()[1]), 
                mother.getType());
        return result;
    }
    
    public void testCase(Genome objective)
    {
        //diagnosableCases++;
        boolean possible= false;
        if((age[1]+age[0])>=objective.getAge()[1] && objective.getAge()[1] >= (age[1]-age[2]))
        {
            if(gender==objective.getGender())
            {
               if((pressure[1]+pressure[0])>=objective.getPressure()[1] && objective.getPressure()[1] >= (pressure[1]-pressure[2])) 
               {
                   if((cholesterol[1]+cholesterol[0])>=objective.getCholesterol()[1] && objective.getCholesterol()[1] >= (cholesterol[1]-cholesterol[2])) 
                   {
                       if(sugar==objective.getSugar())
                       {
                           if(electro==objective.getElectro())
                           {
                                if((hearRate[1]+hearRate[0])>=objective.getHearRate()[1] && objective.getHearRate()[1] >= (hearRate[1]-hearRate[2])) 
                                {
                                    possible= true;
                                }                               
                           }
                       }
                   }
               }
            }
        }
        if(possible)
        {
            possibleDiagnosis++;
            if(objective.getType()==type)
            {
                correctDiagnosis++;
            }
        }
    }
    
    public String toString()
    {
        return String.valueOf(age[0]) + "," + age[1] + "," + age[2] + "," + gender + "," + pressure[0] + "," + pressure[1] + "," +
                pressure[2] + "," + cholesterol[0] + "," + cholesterol[1] + "," + cholesterol[2] + "," +
                sugar + "," + electro + "," + hearRate[0] + "," + hearRate[1] + "," +
                hearRate[2] + "," + type;
    }

    public double[] getAge() {
        return age;
    }

    public void setAge(double[] age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public double[] getPressure() {
        return pressure;
    }

    public void setPressure(double[] pressure) {
        this.pressure = pressure;
    }

    public double[] getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(double[] cholesterol) {
        this.cholesterol = cholesterol;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public int getElectro() {
        return electro;
    }

    public void setElectro(int electro) {
        this.electro = electro;
    }

    public double[] getHearRate() {
        return hearRate;
    }

    public void setHearRate(double[] hearRate) {
        this.hearRate = hearRate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCorrectDiagnosis() {
        return correctDiagnosis;
    }

    public void setCorrectDiagnosis(int correctDiagnosis) {
        this.correctDiagnosis = correctDiagnosis;
    }

    public int getPossibleDiagnosis() {
        return possibleDiagnosis;
    }

    public void setPossibleDiagnosis(int possibleDiagnosis) {
        this.possibleDiagnosis = possibleDiagnosis;
    }

    public int getDiagnosableCases() {
        return diagnosableCases;
    }

    public void setDiagnosableCases(int diagnosableCases) {
        this.diagnosableCases = diagnosableCases;
    }
    
    public int testGenome(Genome objective)
    {
        boolean possible= false;
        if((age[1]+age[0])>=objective.getAge()[1] && objective.getAge()[1] >= (age[1]-age[2]))
        {
            if(gender==objective.getGender())
            {
               if((pressure[1]+pressure[0])>=objective.getPressure()[1] && objective.getPressure()[1] >= (pressure[1]-pressure[2])) 
               {
                   if((cholesterol[1]+cholesterol[0])>=objective.getCholesterol()[1] && objective.getCholesterol()[1] >= (cholesterol[1]-cholesterol[2])) 
                   {
                       if(sugar==objective.getSugar())
                       {
                           if(electro==objective.getElectro())
                           {
                                if((hearRate[1]+hearRate[0])>=objective.getHearRate()[1] && objective.getHearRate()[1] >= (hearRate[1]-hearRate[2])) 
                                {
                                    possible= true;
                                }                               
                           }
                       }
                   }
               }
            }
        }
        if(possible)
        {            
            return 1;
        }else
        {
            return 0;
        }
    }
    
    public int testGenomeAndDiagnosis(Genome objective)
    {
        boolean possible= false;
        if((age[1]+age[0])>=objective.getAge()[1] && objective.getAge()[1] >= (age[1]-age[2]))
        {
            if(gender==objective.getGender())
            {
               if((pressure[1]+pressure[0])>=objective.getPressure()[1] && objective.getPressure()[1] >= (pressure[1]-pressure[2])) 
               {
                   if((cholesterol[1]+cholesterol[0])>=objective.getCholesterol()[1] && objective.getCholesterol()[1] >= (cholesterol[1]-cholesterol[2])) 
                   {
                       if(sugar==objective.getSugar())
                       {
                           if(electro==objective.getElectro())
                           {
                                if((hearRate[1]+hearRate[0])>=objective.getHearRate()[1] && objective.getHearRate()[1] >= (hearRate[1]-hearRate[2])) 
                                {
                                    if(type==objective.getType())
                                    {
                                        possible= true;
                                    }                                    
                                }                               
                           }
                       }
                   }
               }
            }
        }
        if(possible)
        {            
            return 1;
        }else
        {
            return 0;
        }
    }
    
}
