/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author migma
 */
public class Analysis 
{
    private Vector<Genome> generationToTestHealthy;
    private Vector<Genome> generationToTestRisk;
    private Vector<Genome> originalGeneration;
    
    public Analysis()
    {
        generationToTestHealthy= new Vector(0,1);
        generationToTestRisk= new Vector(0,1);
        originalGeneration= new Vector(0,1);
    }
    
    private void fillGenome(String ruta, Vector<Genome> generation)
    {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try 
        {
           //archivo = new File ("src/datos/BDOriginal.csv");
           archivo = new File (ruta);
           fr = new FileReader (archivo);
           br = new BufferedReader(fr);
           String linea= "";
           while((linea=br.readLine())!=null)
           {
              generation.add(createCompleteGenomeFromFile(linea));
           }
        }
        catch(Exception e){
           e.printStackTrace();
        }finally{
           try{                    
              if( null != fr ){   
                 fr.close();     
              }                  
           }catch (Exception e2){ 
              e2.printStackTrace();
           }
        }        
    }
    
    private void fillfirstGeneration()
    {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try 
        {
           archivo = new File ("src/datos/BDOriginal.csv");
           
           fr = new FileReader (archivo);
           br = new BufferedReader(fr);

           String linea=br.readLine();
           while((linea=br.readLine())!=null)
           {
              originalGeneration.add(createGenomeFromFile(linea));
           }
        }
        catch(Exception e){
           e.printStackTrace();
        }finally{
           try{                    
              if( null != fr ){   
                 fr.close();     
              }                  
           }catch (Exception e2){ 
              e2.printStackTrace();
           }
        }        
    }
    
    private Genome createGenomeFromFile(String line)
    {
        StringTokenizer st= new StringTokenizer(line, ",");
        Genome result= new Genome();
        result.setAge(new double[]{0, Double.parseDouble(st.nextToken()),0});
        result.setGender(Integer.parseInt(st.nextToken()));
        result.setPressure(new double[]{0, Double.parseDouble(st.nextToken()),0});
        result.setCholesterol(new double[]{0, Double.parseDouble(st.nextToken()),0});
        result.setSugar(Integer.parseInt(st.nextToken()));
        result.setElectro(Integer.parseInt(st.nextToken()));
        result.setHearRate(new double[]{0, Double.parseDouble(st.nextToken()),0});
        result.setType(Integer.parseInt(st.nextToken()));
        return result;
    }
    
    private Genome createCompleteGenomeFromFile(String line)
    {
        StringTokenizer st= new StringTokenizer(line, ",");
        Genome result= new Genome();
        result.setAge(new double[]{Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken())});
        result.setGender(Integer.parseInt(st.nextToken()));
        result.setPressure(new double[]{Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken())});
        result.setCholesterol(new double[]{Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken())});
        result.setSugar(Integer.parseInt(st.nextToken()));
        result.setElectro(Integer.parseInt(st.nextToken()));
        result.setHearRate(new double[]{Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken())});
        result.setType(Integer.parseInt(st.nextToken()));
        return result;
    }
    
    private int casos0= 0;
    private int casos1= 0;
    private int falsosCasos0= 0;
    private int falsosCasos1= 0;
    
    public int[] analyse() //[0]= healthy cases; [1]= risk cases; [2]= undetermined cases;
    {
        fillGenome("src/datos/Genomapara0.txt", generationToTestHealthy);
        fillGenome("src/datos/Genomapara1.txt", generationToTestRisk);
        int[] result= new int[]{0,0,0,0,0};
        
        fillfirstGeneration();
        int contador= 0;
        while(contador<61)
        {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Analysis.class.getName()).log(Level.SEVERE, null, ex);
            }
            Random r= new Random(System.currentTimeMillis());
            int seleccion= r.nextInt(originalGeneration.size());
            Genome analizar= originalGeneration.remove(seleccion);
            if(analizar.getType()==0)
            {
                result[3]= result[3]+1;
            }else
            {
                result[4]= result[4]+1;
            }
            
            int riskCounter= 0;
            for(int i= 0; i<generationToTestRisk.size(); i++)
            {
                riskCounter+= generationToTestRisk.get(i).testGenome(analizar);
            }
            
            int healthyCounter= 0;
            for(int i= 0; i<generationToTestHealthy.size(); i++)
            {
                healthyCounter+= generationToTestHealthy.get(i).testGenome(analizar);
            }
            
            double percentageRisk= (double) riskCounter/1341;
            double percentagehealthy= (double) healthyCounter/1134;
            
            boolean sano= true;
            if(percentageRisk>=percentagehealthy)
            {
                sano= false;
            }
            
            if(sano)
            {                
                if(analizar.getType()==0)
                {
                    casos0++;
                }else
                {
                    falsosCasos0++;
                }
            }else
            {
                if(analizar.getType()==1)
                {
                    casos1++;
                }else
                {
                    falsosCasos1++;
                }
            }
            
            /*
            
            if(isHealthy(analizar))
            {
                casos0++;                
            }else
            {
                if(isRisk(analizar))
                {
                    casos1++;
                }else
                {
                    if(analizar.getType()==0)
                    {
                        falsosCasos0++;
                    }else
                    {
                        falsosCasos1++;
                    }
                }
            }*/
            
            
            
            if(isRisk(analizar))
            {
                result[1]= result[1]+1;
                
            }else
            {
                
                
                if(isHealthy(analizar))
                {
                    result[0]= result[0]+1;
                    
                }else
                {
                    result[2]= result[2]+1;
                }
            }
            contador++;
        }
        return result;                
    }
    
    
    
    public void imprimirMatriz()
    {
        System.out.println("Casos 0: " + casos0);
        System.out.println("Falsos casos 0: " + falsosCasos0);
        System.out.println("Casos 1: " + casos1);
        System.out.println("Falsos casos 1: " + falsosCasos1);
    }
    
    public boolean isHealthy(Genome gen)
    {
        boolean result= false;
        for(int i= 0; i<generationToTestHealthy.size(); i++)
        {
            if(generationToTestHealthy.get(i).testGenome(gen)==1)
            {
                result= true;
                break;
            }
        }
        return result;
    }
    
    public boolean isRisk(Genome gen)
    {
        boolean result= false;
        for(int i= 0; i<generationToTestRisk.size(); i++)
        {
            if(generationToTestRisk.get(i).testGenome(gen)==1)
            {
                result= true;
                break;
            }
        }
        return result;
    }
    
}
