/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.univali.portugol.plugin.gogoboard.acoes;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Ailton Jr
 */
public class AcaoCompilarLogo extends AbstractAction {

    public AcaoCompilarLogo()
    {
        super("Ação personalizada estática", carregarIcone());
    }

    private static Icon carregarIcone()
    {
        try
        {
            String caminho = "br/univali/portugol/plugin/GoGoBoard/imagens/monitor.png";
            Image imagem = ImageIO.read(AcaoCompilarLogo.class.getClassLoader().getResourceAsStream(caminho));

            return new ImageIcon(imagem);
        }
        catch (IOException ex)
        {
            System.err.println("Erro ao carregar o icone do plugin na ação Compilar Logo");
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JOptionPane.showMessageDialog(null, "O plugin executou uma ação personalizada estática!!", "Plugin GoGoBoard", JOptionPane.INFORMATION_MESSAGE);
    }

}