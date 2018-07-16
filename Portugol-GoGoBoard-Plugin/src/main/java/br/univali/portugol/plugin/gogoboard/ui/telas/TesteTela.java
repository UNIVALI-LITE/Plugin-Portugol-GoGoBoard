package br.univali.portugol.plugin.gogoboard.ui.telas;

import br.univali.portugol.plugin.gogoboard.componetes.DispositivoGoGo;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.ps.ui.telas.TelaCustomBorder;
import javax.swing.JFrame;

/**
 *
 * @author Ailton Cardoso Jr
 */
public class TesteTela {

    public static void main(String[] args) {
        DispositivoGoGo dispositivoGoGo = new DispositivoGoGo(GoGoDriver.TIPODRIVER.COMPARTILHADO);
        TelaCustomBorder janelaMonitor = new TelaCustomBorder(new JanelaMonitor(dispositivoGoGo), "Monitor de Recursos GoGo Board");
        janelaMonitor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaMonitor.setLocationRelativeTo(null);
        janelaMonitor.setVisible(true);
    }
}
