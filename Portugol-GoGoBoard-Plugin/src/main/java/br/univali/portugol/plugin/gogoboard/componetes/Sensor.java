package br.univali.portugol.plugin.gogoboard.componetes;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.plugin.gogoboard.gerenciadores.GerenciadorDriver;
import br.univali.portugol.plugin.gogoboard.driver.GoGoDriver;
import br.univali.portugol.plugin.gogoboard.util.UtilGoGoBoard;

/**
 * Classe que representa o Sensor.
 *
 * @author Ailton Cardoso Jr
 * @version 1.0
 */
public class Sensor {

    private GoGoDriver goGoDriver;
    private int numSensor;
    private int valor;

    /**
     * Construtor padrão do motor servo.
     *
     * @param numSensor Número correspondente ao índice do motor, iniciando em
     * 0.
     * @param tipoDriver Enum referente ao tipo de driver necessário.
     */
    public Sensor(int numSensor, GoGoDriver.TipoDriver tipoDriver) {
        this.goGoDriver = GerenciadorDriver.getGoGoDriver(tipoDriver);
        this.numSensor = numSensor;
    }

    /**
     * Método para obter o ID do sensor.
     *
     * @return ID do sensor.
     */
    public int getNumSensor() {
        return numSensor;
    }

    /**
     * Método para obter o valor do sensor.
     *
     * @return valor do sensor.
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public int getValor() throws ErroExecucaoBiblioteca {
        return valor;
    }

    /**
     * Método para setar o valor do sensor.
     *
     * @param valor Inteiro correspondente ao valor do sensor.
     */
    public void setValor(int valor) {
        this.valor = valor;
    }

    /**
     * Método para atualizar o valor do sensor com o valor atual.
     *
     * @throws
     * br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca
     */
    public void atualizaValor() throws ErroExecucaoBiblioteca {
        int[] mensagem;
        do {
            mensagem = goGoDriver.receberMensagem();
        } while (mensagem[0] != GoGoDriver.GOGOBOARD);       // Se não for uma mensagem da GoGo, tenta novamente
        int byteAlto = mensagem[1 + (numSensor * 2)];
        int byteBaixo = mensagem[1 + (numSensor * 2) + 1];
        valor = UtilGoGoBoard.bytesToInt(byteAlto, byteBaixo);
    }
}
