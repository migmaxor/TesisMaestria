/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author migma
 */
public class Trainning 
{
    private Vector<Genome> testGeneration;
    private Vector<Genome> firstGeneration;
    private Vector<Genome> currentGeneration;
    private Vector<Genome> nextGeneration;
    private int generation;
    private double averageFitness;
    private double acumulatedFitness;
    private int targetPopulation;
    private int analysisCase;
    
    public Trainning()
    {
        testGeneration= new Vector(0,1);
        firstGeneration= new Vector(0,1);
        currentGeneration= new Vector(0,1);
        nextGeneration= new Vector(0,1);
        generation= 0;
    }
    
    private void fillTestGeneration()
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
              testGeneration.add(createGenomeFromFile(linea));
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
    
    private void fillFirstGeneration()
    {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try 
        {
           archivo = new File ("src/datos/BDOriginalCeros.csv"); //Change the original data
           fr = new FileReader (archivo);
           br = new BufferedReader(fr);

           String linea=br.readLine();
           while((linea=br.readLine())!=null)
           {
              firstGeneration.add(createGenomeFromFile(linea));
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
        currentGeneration= firstGeneration;
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
    
    private void printCurrentGeneration(String number)
    {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("src/datos/generation" + number + ".txt", false);
            pw = new PrintWriter(fichero);            
            
            for (int i = 0; i < currentGeneration.size(); i++)
            {
                pw.println(currentGeneration.get(i).toString());
            }
            pw.println(averageFitness);
                

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
    }
    
    //This is the reproduction.
    private void createNextGeneration()
    {
        System.out.println("Imprimiendo generación actual");
        printCurrentGeneration(String.valueOf(generation));
        generation++;
        System.out.println("Creando generación: " + generation);
        for(int i= 0; i<currentGeneration.size()-1; i++)
        {
            System.out.println("Reproduciendo al especimen: " + i + ", de la generación: " + generation);
            for(int j= i+1; j<currentGeneration.size(); j++)
            {
                if(currentGeneration.get(i).getType()==currentGeneration.get(j).getType())
                {
                    Vector<Genome> children= currentGeneration.get(i).reproduce(currentGeneration.get(j));
                    if(children!= null )nextGeneration.addAll(children);
                    
                }
            }
        }
        targetPopulation= (int) (currentGeneration.size()*0.98);
        currentGeneration= nextGeneration;
        
    }
    
    private void createNextGeneration2()
    {
        System.out.println("Imprimiendo generación actual");
        printCurrentGeneration(String.valueOf(generation));
        generation++;
        System.out.println("Creando generación: " + generation);
        for(int i= 0; i<currentGeneration.size()-1; i++)
        {
            System.out.println("Reproduciendo al especimen: " + i + ", de la generación: " + generation);
            for(int j= i+1; j<currentGeneration.size(); j++)
            {
                if(currentGeneration.get(i).getType()==currentGeneration.get(j).getType())
                {
                    Vector<Genome> children= currentGeneration.get(i).reproduce(currentGeneration.get(j));
                    if(children!= null )nextGeneration.addAll(children);
                    
                }
            }
        }
        targetPopulation= (int) (currentGeneration.size()*0.8);
        currentGeneration= nextGeneration;
        
    }
    
    private void testCurrentGeneration()
    {        
        for(int i= 0; i<currentGeneration.size(); i++)
        {
            System.out.println("Testeando actual generación, especimen: " + i);
            for(int j= 0; j<testGeneration.size(); j++)
            {
                currentGeneration.get(i).testCase(testGeneration.get(j));
            }
            currentGeneration.get(i).calculateFitness();
        }
    }
    
    private void sortCurrentGeneration()
    {
        /*
        Vector<Genome> result= new Vector(0,1);
        result.add(currentGeneration.get(0));
        for(int i= 1; i<currentGeneration.size(); i++)
        {
            System.out.println("Ordenando la generación actual");
            for(int j= 0; j<result.size(); j++)
            {
                if(currentGeneration.get(i).getFitness()<result.get(j).getFitness())
                {
                    result.add(j, currentGeneration.get(i));
                    break;
                }else
                {
                    if(j==result.size()-1)
                    {
                        result.add(currentGeneration.get(i));
                        break;
                    }
                }
            }
        }
        currentGeneration= result;*/
        Vector<Genome> result= new Vector(10,1);
        /*for(int i=0; i<100; i++)            
        {
            result.add(currentGeneration.get(i));
        } */       
        for(int i= 0; i<currentGeneration.size(); i++)
        {
            System.out.println("Ordenando generación: " + generation + "con fitness promedio= " + averageFitness+ ", especimen: " + i);
            if(currentGeneration.get(i).getAge()[0]!=0.0 && currentGeneration.get(i).getFitness()>0.8)
            {
                int positionInArray= (int)(currentGeneration.get(i).getFitness()*result.size());
                result.add(positionInArray, currentGeneration.get(i));
            }            
            
        }
        /*
        
        for(int i= 0; i<currentGeneration.size(); i++)
        {
            System.out.println("Ordenando generación: " + generation + "con fitness promedio= " + averageFitness+ ", especimen: " + i);
            if(currentGeneration.get(i).getFitness()>=0.9)
            {
                result.add((result.size()/10)*9, currentGeneration.get(i));
            }else
            {
                if(currentGeneration.get(i).getFitness()>=0.8)
                {
                    result.add((result.size()/10)*8, currentGeneration.get(i));
                }else
                {
                    if(currentGeneration.get(i).getFitness()>=0.7)
                    {
                        result.add((result.size()/10)*7, currentGeneration.get(i));
                    }else
                    {
                        if(currentGeneration.get(i).getFitness()>=0.6)
                        {
                            result.add((result.size()/10)*6, currentGeneration.get(i));
                        }else
                        {
                            if(currentGeneration.get(i).getFitness()>=0.5)
                            {
                                result.add((result.size()/10)*5, currentGeneration.get(i));
                            }else
                            {
                                if(currentGeneration.get(i).getFitness()>=0.4)
                                {
                                    result.add((result.size()/10)*4, currentGeneration.get(i));
                                }else
                                {
                                    if(currentGeneration.get(i).getFitness()>=0.3)
                                    {
                                        result.add((result.size()/10)*3, currentGeneration.get(i));
                                    }else
                                    {
                                        if(currentGeneration.get(i).getFitness()>=0.2)
                                        {
                                            result.add((result.size()/10)*2, currentGeneration.get(i));
                                        }else
                                        {
                                            result.add((result.size()/10), currentGeneration.get(i));
                                        } 
                                    } 
                                } 
                            }                                 
                        } 
                    }                  
                } 
            }
        }*/
        currentGeneration= result;
        
    }
    
    private void filterCurrentGeneration()
    {
        System.out.println("Filtrando generación: " + generation);
        Vector<Genome> replacementGeneration= new Vector(0,1);
        if(targetPopulation<150)
        {
            targetPopulation= 150;
        }
        for(int i= currentGeneration.size()-targetPopulation; i<currentGeneration.size(); i++)
        {
            replacementGeneration.add(currentGeneration.get(i));
        }
        currentGeneration= replacementGeneration;
    }
    
    private void calculateAverageFitness()
    {
        /*
        acumulatedFitness= 0.0;
        System.out.println("Calculando fitness promedio");
        for(int i= 0; i<currentGeneration.size(); i++)
        {
            acumulatedFitness+= currentGeneration.get(i).getFitness();
            System.out.println("Fitness de: " + i + " es igual a: " + currentGeneration.get(i).getFitness());
        }
        averageFitness= acumulatedFitness/(double)currentGeneration.size();*/
        acumulatedFitness= 0.0;
        System.out.println("Calculando fitness promedio");
        for(int i= 0; i<firstGeneration.size(); i++)
        {
            if(isDiagnosableThisGenoma(firstGeneration.get(i)))
            {
                acumulatedFitness+= 1.0;
            }
        }
        if(currentGeneration.get(0).getType()==0)
        {
            averageFitness= acumulatedFitness/138.0;
        }else
        {
            averageFitness= acumulatedFitness/89.0;
        }
        
        
    }
    
    private boolean isDiagnosableThisGenoma(Genome gen)
    {
        boolean result= false;
        for(int i=0; i<currentGeneration.size(); i++)
        {
            if(currentGeneration.get(i).testGenomeAndDiagnosis(gen)==1)
            {
                result= true;
                break;
            }
        }
        return result;
        
    }
    
    private void addOriginal()
    {
        currentGeneration.addAll(firstGeneration);
    }
    
    public void run()
    {
        analysisCase= 0;
        long tiempoInicial= System.currentTimeMillis();
        fillTestGeneration();        
        fillFirstGeneration();
        
        createNextGeneration();
        
        testCurrentGeneration();
        
        sortCurrentGeneration();            
        
        filterCurrentGeneration();
        
        calculateAverageFitness();
        
        while(averageFitness<=0.8)
        {
            addOriginal();
            createNextGeneration();
            testCurrentGeneration();   
            sortCurrentGeneration();            
            filterCurrentGeneration();
            calculateAverageFitness();
        }
        System.out.println("Fitness final: "+ averageFitness);
        printCurrentGeneration("final");
        long tiempoFinal= System.currentTimeMillis();
        System.out.println("TiempoTotal: " + (tiempoFinal- tiempoInicial));
                
                
    }
    
}
