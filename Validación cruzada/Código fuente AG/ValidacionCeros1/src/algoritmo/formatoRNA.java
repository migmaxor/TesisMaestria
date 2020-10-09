package algoritmo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
public class formatoRNA {
    
    private static Vector<String> datosEntrenamiento= new Vector(0,1);
    private static Vector<String> datosTest= new Vector(0,1);
    
    public static Vector<String> convertirVector(Vector<String> entrada)
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
    
    public static double[] traerMayoresMenores(Vector<Vector<Double>> numeros)
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        File archivo1 = null;
        FileReader fr1 = null;
        BufferedReader br1 = null;
        
        File archivo2 = null;
        FileReader fr2 = null;
        BufferedReader br2 = null;
        try 
        {
           archivo1 = new File ("src/datos/BDOriginal.csv"); //Change the original data
           fr1 = new FileReader (archivo1);
           br1 = new BufferedReader(fr1);

           String linea=br1.readLine();
           while((linea=br1.readLine())!=null)
           {
              datosEntrenamiento.add(linea);
           }
           
           archivo2 = new File ("src/datos/BDPrueba.csv"); //Change the original data
           fr2 = new FileReader (archivo2);
           br2 = new BufferedReader(fr2);

           String linea2=br2.readLine();
           while((linea2=br2.readLine())!=null)
           {
              datosTest.add(linea2);
           }
        }
        catch(Exception e){
           e.printStackTrace();
        }finally{
           try{                    
              if( null != fr1 ){   
                 fr1.close();     
                 fr2.close();  
              }                  
           }catch (Exception e2){ 
              e2.printStackTrace();
           }
        }
        
        
        //--------------------------------------------
        
        datosEntrenamiento= convertirVector(datosEntrenamiento);
        datosTest= convertirVector(datosTest);
        
        //--------------------------------------------
        
        FileWriter fichero1 = null;
        PrintWriter pw1 = null;
        
        FileWriter fichero2 = null;
        PrintWriter pw2 = null;
        try
        {
            fichero1 = new FileWriter("src/datos/BDOriginalRNA.csv", false);
            pw1 = new PrintWriter(fichero1);            
            
            for (int i = 0; i < datosEntrenamiento.size(); i++)
            {
                pw1.println(datosEntrenamiento.get(i));
            }
            
            fichero2 = new FileWriter("src/datos/BDPruebaRNA.csv", false);
            pw2 = new PrintWriter(fichero2);            
            
            for (int i = 0; i < datosTest.size(); i++)
            {
                pw2.println(datosTest.get(i));
            }   

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero1)
              fichero1.close();
              fichero2.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
        
        
    }
    
}
