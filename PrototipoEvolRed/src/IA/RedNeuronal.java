package IA;

import algoritmo.Genome;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Vector;
import org.neuroph.core.NeuralNetwork;

/**
 *
 * @author Miguel Askar
 */
public class RedNeuronal {
    
    private int casos0= 0;
    private int casos1= 0;
    private int falsosCasos0= 0;
    private int falsosCasos1= 0;
    private long tiempoEjecucion= 0;
    private NeuralNetwork neuralNetwork;
    
    public RedNeuronal()
    {
        neuralNetwork = NeuralNetwork.createFromFile("src/datos/7-14-10-8-1.nnet");
    }
    
    private Vector<String> datosEntrenamiento;
    public void analizarArchivo(String ruta) //[0]= healthy cases; [1]= risk cases; [2]= undetermined cases;
    {
        long tiempo= System.currentTimeMillis();
        casos0= 0;
        falsosCasos0= 0;        
        casos1= 0;
        falsosCasos1= 0;
        datosEntrenamiento= new Vector(0,1);
        
        crearArchivoRNA(ruta);
        
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try 
        {
           archivo = new File (ruta.substring(0, ruta.length()-4)+"FormatoRNA.csv");
           
           fr = new FileReader (archivo);
           br = new BufferedReader(fr);
           
           String linea= "";
           while((linea=br.readLine())!=null)
           {
                StringTokenizer tokenizer= new StringTokenizer(linea, ",");
                neuralNetwork.setInput(Double.parseDouble(tokenizer.nextToken()), Double.parseDouble(tokenizer.nextToken()),
                        Double.parseDouble(tokenizer.nextToken()), Double.parseDouble(tokenizer.nextToken()),
                        Double.parseDouble(tokenizer.nextToken()), Double.parseDouble(tokenizer.nextToken()),
                        Double.parseDouble(tokenizer.nextToken()));
                neuralNetwork.calculate(); 
                double[] networkOutput = neuralNetwork.getOutput(); 
                
                int pronostico= Integer.parseInt(tokenizer.nextToken());
                
                boolean sano= true;
                if(networkOutput[0]>0.5)
                {
                    sano= false;
                }
                
                if(sano)
                {                
                    if(pronostico==0)
                    {
                        casos0++;
                    }else
                    {
                        falsosCasos0++;
                    }
                }else
                {
                    if(pronostico==1)
                    {
                        casos1++;
                    }else
                    {
                        falsosCasos1++;
                    }
                }
                
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
        
        tiempoEjecucion= System.currentTimeMillis()-tiempo;
    }  
    
    public void crearArchivoRNA(String ruta)
    {
        File archivo1 = null;
        FileReader fr1 = null;
        BufferedReader br1 = null;
        
        try 
        {
           archivo1 = new File (ruta); //Change the original data
           fr1 = new FileReader (archivo1);
           br1 = new BufferedReader(fr1);

           String linea=br1.readLine();
           while((linea=br1.readLine())!=null)
           {
              datosEntrenamiento.add(linea);
           }           
           
        }
        catch(Exception e){
           e.printStackTrace();
        }finally{
           try{                    
              if( null != fr1 ){   
                 fr1.close();     
              }                  
           }catch (Exception e2){ 
              e2.printStackTrace();
           }
        }        
        
        //--------------------------------------------
        //Se convierte el vector a datos con entradas válidas para la RNA
        datosEntrenamiento= convertirVector(datosEntrenamiento);
        
        //--------------------------------------------
        
        FileWriter fichero1 = null;
        PrintWriter pw1 = null;
        
        try
        {
            fichero1 = new FileWriter(ruta.substring(0, ruta.length()-4)+"FormatoRNA.csv", false);
            pw1 = new PrintWriter(fichero1);            
            
            for (int i = 0; i < datosEntrenamiento.size(); i++)
            {
                pw1.println(datosEntrenamiento.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero1)
              fichero1.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
    }
    
    public Vector<String> convertirVector(Vector<String> entrada)
    {
        Vector<Vector<Double>> versionNumeros= new Vector(0,1);
        Vector<String> resultado= new Vector(0,1);
        for(int i= 0; i< entrada.size(); i++)
        {
            StringTokenizer tokenizer= new StringTokenizer(entrada.get(i), ",");
            Vector<Double> temporal= new Vector(8,1);
            temporal.insertElementAt(Double.parseDouble(tokenizer.nextToken()),0);
            temporal.insertElementAt(Double.parseDouble(tokenizer.nextToken()),1);
            temporal.insertElementAt(Double.parseDouble(tokenizer.nextToken()),2);
            temporal.insertElementAt(Double.parseDouble(tokenizer.nextToken()),3);
            temporal.insertElementAt(Double.parseDouble(tokenizer.nextToken()),4);
            temporal.insertElementAt(Double.parseDouble(tokenizer.nextToken()),5);
            temporal.insertElementAt(Double.parseDouble(tokenizer.nextToken()),6);
            temporal.insertElementAt(Double.parseDouble(tokenizer.nextToken()),7);      
            versionNumeros.add(temporal);
        }

        //Valores estáticos del dataset de entrenamiento, corresponden a los mínimos y máximo de los atributos no binarios.
        //double[] mayoresMenores= {77,29,180,94,564,131,202,71};
        
        double[] mayoresMenores= traerMayoresMenores(versionNumeros);
        
        for(int i= 0; i< versionNumeros.size(); i++)
        {
            versionNumeros.get(i).set(0, ((1/(mayoresMenores[0]-mayoresMenores[1]))*(versionNumeros.get(i).get(0)-mayoresMenores[1])));
            versionNumeros.get(i).set(2, ((1/(mayoresMenores[2]-mayoresMenores[3]))*(versionNumeros.get(i).get(2)-mayoresMenores[3])));
            versionNumeros.get(i).set(3, ((1/(mayoresMenores[4]-mayoresMenores[5]))*(versionNumeros.get(i).get(3)-mayoresMenores[5])));
            versionNumeros.get(i).set(6, ((1/(mayoresMenores[6]-mayoresMenores[7]))*(versionNumeros.get(i).get(6)-mayoresMenores[7])));
            resultado.add(versionNumeros.get(i).get(0) + "," + versionNumeros.get(i).get(1).intValue() + "," + versionNumeros.get(i).get(2) + "," +
                    versionNumeros.get(i).get(3) + "," + versionNumeros.get(i).get(4).intValue() + "," + versionNumeros.get(i).get(5).intValue() + "," + 
                    versionNumeros.get(i).get(6) + "," + versionNumeros.get(i).get(7).intValue());
        }
        return resultado;
    }
    
    public double[] traerMayoresMenores(Vector<Vector<Double>> numeros)
    {
       
        double[] resultado= {numeros.get(0).get(0), numeros.get(0).get(0), numeros.get(0).get(2), numeros.get(0).get(2), numeros.get(0).get(3), numeros.get(0).get(3), numeros.get(0).get(6), numeros.get(0).get(6)};
        for(int i= 1; i<numeros.size(); i++)
        {
            if(numeros.get(i).get(0)> resultado[0])
            {
                resultado[0]=numeros.get(i).get(0);
            }
            if(numeros.get(i).get(0)< resultado[1])
            {
                resultado[1]=numeros.get(i).get(0);
            }
            
            if(numeros.get(i).get(2)> resultado[2])
            {
                resultado[2]=numeros.get(i).get(2);
            }
            if(numeros.get(i).get(2)< resultado[3])
            {
                resultado[3]=numeros.get(i).get(2);
            }
            
            if(numeros.get(i).get(3)> resultado[4])
            {
                resultado[4]=numeros.get(i).get(3);
            }
            if(numeros.get(i).get(3)< resultado[5])
            {
                resultado[5]=numeros.get(i).get(3);
            }
            
            if(numeros.get(i).get(6)> resultado[6])
            {
                resultado[6]=numeros.get(i).get(6);
            }
            if(numeros.get(i).get(6)< resultado[7])
            {
                resultado[7]=numeros.get(i).get(6);
            }
        }        
        return resultado;
    }
    
    public boolean analizarPaciente(String linea) //[0]= healthy cases; [1]= risk cases; [2]= undetermined cases;
    {
        long tiempo= System.currentTimeMillis();
        StringTokenizer tokenizer= new StringTokenizer(linea, ",");
                neuralNetwork.setInput(Double.parseDouble(tokenizer.nextToken()), Double.parseDouble(tokenizer.nextToken()),
                        Double.parseDouble(tokenizer.nextToken()), Double.parseDouble(tokenizer.nextToken()),
                        Double.parseDouble(tokenizer.nextToken()), Double.parseDouble(tokenizer.nextToken()),
                        Double.parseDouble(tokenizer.nextToken()));
        neuralNetwork.calculate(); 
        double[] networkOutput = neuralNetwork.getOutput(); 

        boolean sano= true;
        if(networkOutput[0]>0.5)
        {
            sano= false;
        }
        return sano;                
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
