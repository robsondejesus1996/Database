/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.principal;

import br.udesc.tela.TelaDb;

/**
 *
 * @author Rj
 */
public class Principal {
    
   public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaDb().setVisible(true);
            }
        });
    }
    
}
