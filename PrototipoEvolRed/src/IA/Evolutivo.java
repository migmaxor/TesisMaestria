package IA;

import algoritmo.Genome;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Miguel Askar
 */
public class Evolutivo {
    
    private Vector<Genome> generationToTestHealthy;
    private Vector<Genome> generationToTestRisk;
    private Vector<Genome> testGeneration;
    private int casos0;
    private int casos1;
    private int falsosCasos0;
    private int falsosCasos1;
    private long tiempoEjecucion;
    private boolean diagnostico;
    private String ruta;
    
    public Evolutivo()
    {
        generationToTestHealthy= new Vector(0,1);
        generationToTestRisk= new Vector(0,1);
        testGeneration= new Vector(0,1);
        fillGenome("src/datos/Genomapara0.txt", generationToTestHealthy);
        fillGenome("src/datos/Genomapara1.txt", generationToTestRisk);
        casos0= 0;
        casos1= 0;
        falsosCasos0= 0;
        falsosCasos1= 0;
        tiempoEjecucion= 0;        
    }
    
    private void fillGenome(String ruta, Vector<Genome> generation)
    {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try 
        {
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
    
    private void llenarTestGeneracion(String ruta)
    {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try 
        {
           archivo = new File (ruta);
           
           fr = new FileReader (archivo);
           br = new BufferedReader(fr);

           String linea=br.readLine();
           while((linea=br.readLine())!=null)
           {
              testGeneration.add(createGenomeFromFile(linea));
           }
        }
        catch(IOException e){
           //e.printStackTrace();
        }finally{
           try{                    
              if( null != fr ){   
                 fr.close();     
              }                  
           }catch (IOException e2){ 
              //e2.printStackTrace();
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
        if(st.hasMoreTokens()) result.setType(Integer.parseInt(st.nextToken()));
        else result.setType(-1);        
        
        return result;
    }
    
    
    
    public void analizarArchivo(String ruta) 
    {
        casos0= 0;
        falsosCasos0= 0;        
        casos1= 0;
        falsosCasos1= 0;
        testGeneration= new Vector(0,1);
        long tiempo= System.currentTimeMillis();
        llenarTestGeneracion(ruta);
        
        for(int n= 0; n<testGeneration.size(); n++)
        {
            
            Genome analizar= testGeneration.get(n);
                        
            int riskCounter= 0;
            for(int m= 0; m<generationToTestRisk.size(); m++)
            {
                riskCounter+= generationToTestRisk.get(m).testGenome(analizar);
            }
            
            int healthyCounter= 0;
            for(int m= 0; m<generationToTestHealthy.size(); m++)
            {
                healthyCounter+= generationToTestHealthy.get(m).testGenome(analizar);
            }
            
            double percentageRisk= (double) riskCounter/generationToTestRisk.size();
            double percentagehealthy= (double) healthyCounter/generationToTestHealthy.size();
            
            boolean sano= true;
            if(percentageRisk>percentagehealthy)
            {
                sano= false;
            }
            
            if(sano) //Este condicional permite llenar los datos para la matriz de confusión.
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
        }
        tiempoEjecucion= System.currentTimeMillis()-tiempo;            
    }  
    
    public boolean analizarPaciente(String datos) //[0]= healthy cases; [1]= risk cases; [2]= undetermined cases;
    {
        Genome analizar= createGenomeFromFile(datos);
          
        int riskCounter= 0;
        for(int m= 0; m<generationToTestRisk.size(); m++)
        {
            riskCounter+= generationToTestRisk.get(m).testGenome(analizar);
        }

        int healthyCounter= 0;
        for(int m= 0; m<generationToTestHealthy.size(); m++)
        {
            healthyCounter+= generationToTestHealthy.get(m).testGenome(analizar);
        }

        double percentageRisk= (double) riskCounter/1341;
            double percentagehealthy= (double) healthyCounter/1134;           
         
        boolean sano= true;
        if(percentageRisk>percentagehealthy)
        {
            sano= false;
        }
        return sano;                
    } 
    
    public int analizarPacienteConGenoma(Genome datos) //[0]= healthy cases; [1]= risk cases; [2]= undetermined cases;
    {
        Genome analizar= datos;
          
        int riskCounter= 0;
        for(int m= 0; m<generationToTestRisk.size(); m++)
        {
            riskCounter+= generationToTestRisk.get(m).testGenome(analizar);
        }

        int healthyCounter= 0;
        for(int m= 0; m<generationToTestHealthy.size(); m++)
        {
            healthyCounter+= generationToTestHealthy.get(m).testGenome(analizar);
        }

        double percentageRisk= (double) riskCounter/1111;
        double percentagehealthy= (double) healthyCounter/1000;            
         
        int diagnostico= 0;
        if(percentageRisk>percentagehealthy)
        {
            diagnostico= 1;
        }
        return diagnostico;                
    } 
    
    public void crearArchivo(boolean diagnostico, String ruta)
    {
        this.diagnostico= diagnostico;
        this.ruta= ruta.substring(0, ruta.length()-4)+"Diagnosticado.csv";
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(ruta.substring(0, ruta.length()-4)+"Diagnosticado.csv");
            pw = new PrintWriter(fichero);
            
            if(this.diagnostico)
            {
                pw.println("age,sex,trestbps,chol,fbs,restecg,thalach,target,diagnosisAE,diagnosisRNA");  
            }else
            {
                pw.println("age,sex,trestbps,chol,fbs,restecg,thalach,diagnosisAE,diagnosisRNA");  
            }            

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
    
    public void agregarLineasArchivo()
    {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(ruta, true);
            pw = new PrintWriter(fichero);
            
            for(int i= 0; i<testGeneration.size(); i++)
            {
                Genome genoma= testGeneration.get(i);
                String datosRedNeuronal= ((1/(77-29))*(genoma.getAge()[1])-29) + "," +                
                genoma.getGender() + "," +
                ((1/(180-94))*(genoma.getPressure()[1]-94)) + "," +
                ((1/(564-131))*(genoma.getCholesterol()[1]-131)) + "," + 
                genoma.getSugar() + "," +
                genoma.getElectro() + "," +
                ((1/(202-71))*(genoma.getHearRate()[1]-171)) + ",0"; 
                RedNeuronal redNeuronal= new RedNeuronal();
                boolean resultadoRedNeuronal= redNeuronal.analizarPaciente(datosRedNeuronal);
                int analisisRNA= 0;
                if(!resultadoRedNeuronal) analisisRNA= 1;
                
                if(diagnostico)
                {   
                    pw.println(genoma.toStringSimple()+ 
                            "," + analizarPacienteConGenoma(genoma)+
                            "," + analisisRNA);
                }else
                {
                    pw.println(genoma.toStringSimple().substring(0, genoma.toStringSimple().length()-3)+ 
                            "," + analizarPacienteConGenoma(genoma)+
                            "," + analisisRNA);
                }
            }

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
    
    
    public long getTiempoEjecucion()
    {
        return tiempoEjecucion;
    }

    public int getCasos0() {
        return casos0;
    }

    public int getCasos1() {
        return casos1;
    }

    public int getFalsosCasos0() {
        return falsosCasos0;
    }

    public int getFalsosCasos1() {
        return falsosCasos1;
    }

}
