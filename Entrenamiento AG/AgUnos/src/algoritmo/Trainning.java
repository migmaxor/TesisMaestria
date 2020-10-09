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
           archivo = new File ("src/datos/BDOriginalUnos.csv"); //Change the original data
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
        
        Vector<Genome> result= new Vector(10,1);
       
        for(int i= 0; i<currentGeneration.size(); i++)
        {
            System.out.println("Ordenando generación: " + generation + "con fitness promedio= " + averageFitness+ ", especimen: " + i);
            if(currentGeneration.get(i).getAge()[0]!=0.0 && currentGeneration.get(i).getFitness()>0.8)
            {
                int positionInArray= (int)(currentGeneration.get(i).getFitness()*result.size());
                result.add(positionInArray, currentGeneration.get(i));
            }            
            
        }
        
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
            averageFitness= acumulatedFitness/138.0; //138 es la cantidad de pacientes sanos.
        }else
        {
            averageFitness= acumulatedFitness/165.0; //165 es la cantidad de pacientes con riesgo.
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
    
    private void addOriginal() //Método para recuperar la generación original en las nuevas generaciones.
    {
        currentGeneration.addAll(firstGeneration);
    }
    
    public void run()
    {
        analysisCase= 0;
        long tiempoInicial= System.currentTimeMillis();

        fillTestGeneration(); //Se llena el dataset de prueba.
        fillFirstGeneration(); //Se llena la primera generación.

        createNextGeneration(); //Se crea la siguiente generación.
        
        testCurrentGeneration(); //Se prueba  la generación nueva.
         
        sortCurrentGeneration(); //Se organiza de mayor a menor fitness (ordenamiento no cuadrático).
        
        filterCurrentGeneration(); //Se seleccionan los mejores genomas.
        
        calculateAverageFitness(); //Se cálcula el fitness promedio de la generación.
        
        while(averageFitness<=0.8) //Se repite el proceso hasta lograr el fitness deseado.
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
