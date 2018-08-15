package br.univali.portugol.plugin.gogoboard.acoes;

import br.univali.portugol.plugin.gogoboard.GoGoBoardPlugin;
import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorConversao;
import br.univali.portugol.plugin.gogoboard.ui.telas.TelaErroPythonFaltando;
import br.univali.ps.nucleo.Configuracoes;
import br.univali.ps.ui.Lancador;
import br.univali.ps.ui.swing.ColorController;
import br.univali.ps.ui.telas.TelaCustomBorder;
import java.awt.Dialog;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

/**
 * Classe inicial do botão da ação enviar bytecode para a GoGo Board.
 *
 * @author Ailton Jr
 * @version 1.0
 */
public class AcaoEnviarByteCode extends AbstractAction {

    private GoGoBoardPlugin plugin;
    boolean temPython = false;
    private static JDialog indicadorProgresso;

    /**
     * Construtor da ação enviar byte code.
     *
     * @param plugin Instancia de plugin.
     */
    public AcaoEnviarByteCode(GoGoBoardPlugin plugin) {
        super("Envia o programa para a GoGo Board", carregarIcone());
        this.plugin = plugin;
        this.temPython = encontrouPython();
        
        if(temPython)
        extrairPython();
        configuraLoader();
    }

    /**
     * Método para carregar o icone da ação.
     *
     * @return ImageIcon
     */
    
    private void configuraLoader() {
        boolean usandoTemaDark = Configuracoes.getInstancia().isTemaDark();
        String caminhoIcone = String.format("/br/univali/ps/ui/icones/%s/grande/load.gif", usandoTemaDark ? "Dark" : "Portugol");
        Icon icone = new ImageIcon(getClass().getResource(caminhoIcone));
        indicadorProgresso = new JDialog();
        indicadorProgresso.setUndecorated(true);

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(ColorController.FUNDO_CLARO);
        painel.add(new JLabel(icone, JLabel.CENTER));
        JLabel instalando = new JLabel("Enviando...", JLabel.CENTER);
        instalando.setForeground(ColorController.COR_LETRA);
        instalando.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        painel.add(instalando);

        indicadorProgresso.add(painel);        
        indicadorProgresso.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        indicadorProgresso.pack();
        indicadorProgresso.setLocationRelativeTo(Lancador.getJFrame());
    }
    
    private static Icon carregarIcone() {
        try {
            String caminho = "br/univali/portugol/plugin/gogoboard/imagens/submit.png";
            Image imagem = ImageIO.read(AcaoEnviarByteCode.class.getClassLoader().getResourceAsStream(caminho));

            return new ImageIcon(imagem);
        } catch (IOException ex) {
            System.err.println("Erro ao carregar o icone do plugin na ação Compilar Logo");
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(temPython)
        {
            new Thread(() -> {
                GerenciadorConversao gerenciadorConversao = new GerenciadorConversao(plugin);
                gerenciadorConversao.enviarBytecodeParaGoGo();
                SwingUtilities.invokeLater(() -> {
                    indicadorProgresso.setVisible(false);
                });  
            }).start();
            SwingUtilities.invokeLater(() -> {
                indicadorProgresso.setVisible(true);
            });            
        }
        else{
            TelaCustomBorder tela = new TelaCustomBorder(new TelaErroPythonFaltando(), "Erro Python Faltando");
            tela.setLocationRelativeTo(Lancador.getJFrame());
            tela.setVisible(true);
        }
        
    }
    
    private boolean encontrouPython()
    {
        CommandLine cmdLine = new CommandLine("python");
        ByteArrayOutputStream streamSaida = new ByteArrayOutputStream();
        DefaultExecutor executor = new DefaultExecutor();
        PumpStreamHandler streamHandler = new PumpStreamHandler(streamSaida);
        executor.setStreamHandler(streamHandler);
        try 
        {
            executor.execute(cmdLine);
        } 
        catch (IOException ex) 
        {
            return false;
        }
        return true;
    }
    
    private void extrairPython()
    {
        String caminho = "br/univali/portugol/plugin/gogoboard/compilador";

        File diretorioTemporario = new File(Configuracoes.getInstancia().getDiretorioTemporario().getAbsolutePath(), "pluginPythonDependence");
        String[] arquivos = {"logoc.py", "parser.out", "parsetab.py", "ply/lex.py", "ply/yacc.py", "ply/__init__.py"};

        try {
            for (String arquivo : arquivos) {                
                extrairArquivoJar(caminho+"/"+arquivo, new File(diretorioTemporario, arquivo));
            }
        } catch (IOException ex) {
            Logger.getLogger(AcaoEnviarByteCode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void extrairArquivoJar(String caminhoArquivo, File arquivoDestino) throws IOException
    {
        ClassLoader loader = getClass().getClassLoader();
        
        try (InputStream stream = loader.getResourceAsStream(caminhoArquivo))
        {
            byte[] buffer = new byte[1048576]; // 1MB
            int lido = 0;
            arquivoDestino.getParentFile().mkdirs();
            try(FileOutputStream fos = new FileOutputStream(arquivoDestino))
            {            
                while ((lido = stream.read(buffer, 0, buffer.length)) > 0)
                {
                    fos.write(buffer, 0, lido);
                }
            }
        }        
    }
}
