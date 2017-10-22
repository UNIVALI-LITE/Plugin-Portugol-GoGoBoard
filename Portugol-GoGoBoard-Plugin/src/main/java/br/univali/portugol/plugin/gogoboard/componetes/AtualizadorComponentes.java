package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.util.UtilGoGoBoard;
import java.util.List;

/**
 * Classe para atualizar os componentes que são exibidos no monitor de recursos.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class AtualizadorComponentes {

    private final List<Sensor> sensores;
    private final Infravermelho infravermelho;
    private final GoGoDriver goGoDriver;

    /**
     * Construtor padrão do atualizador de componentes.
     * @param sensores Lista de componentes do tipo Sensor.
     * @param infraVermelho Componente do tipo infravermelho.
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     */
    public AtualizadorComponentes(List<Sensor> sensores, Infravermelho infraVermelho, GoGoDriver.TipoDriver tipoDriver) {
        this.sensores = sensores;
        this.infravermelho = infraVermelho;
        this.goGoDriver = GerenciadorDriver.getGoGoDriver(tipoDriver);
    }

    /**
     * Método para atualizar os componentes.
     *
     * @throws br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void atualizar() throws ErroExecucaoBiblioteca {
        int[] mensagem = goGoDriver.receberMensagem();

        if (mensagem[0] == GoGoDriver.GOGOBOARD) {
            // Atualizar valores dos sensores
            for (int i = 0; i < 8; i++) {
                int valor = UtilGoGoBoard.bytesToInt(mensagem[1 + (i * 2)], mensagem[1 + (i * 2) + 1]);
                sensores.get(i).setValor(valor);
            }
            // Atualizar Infravermelho
            infravermelho.setValorRecebido(mensagem[GoGoDriver.INDICE_VALOR_IR]);
        }
    }

}
