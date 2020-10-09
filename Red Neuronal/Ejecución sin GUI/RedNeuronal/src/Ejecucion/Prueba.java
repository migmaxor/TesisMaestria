/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejecucion;

import java.util.Timer;
import java.util.TimerTask;


/**
 *
 * @author migma
 */
public class Prueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Analysis a= new Analysis();
        a.analyse();
        a.imprimirMatriz();
        
    }
    
}
