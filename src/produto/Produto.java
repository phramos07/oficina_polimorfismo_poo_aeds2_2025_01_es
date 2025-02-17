package produto;
import java.text.NumberFormat;
import java.time.Clock;
import java.time.LocalDate;

// Classe abstrata que define o comportamento de um produto. Por se tratar
// de uma classe abstrata, não pode ser instanciada. A regra de negócio para
// o cálculo do valor de venda é implementada nesta classe, mas o cálculo do
// desconto é privado e deve ser implementado nas classes que herdam de Produto.
public abstract class Produto {
    private static final double MARGEM_PADRAO = 0.2;
    private static final double DESCONTO_PADRAO = 1.0;
    
    private static Clock clock = Clock.systemDefaultZone();
    
    private String descricao;
    private double precoCusto;
    private double margemLucro;
     
    // Inicializador privado (redireção do construtor)
    private void init(String desc, double precoCusto, double margemLucro){
               
        if(desc.length()<3 ||precoCusto<=0||margemLucro<=0)
            throw new IllegalArgumentException("Valores inválidos para o produto");
        descricao = desc;
        this.precoCusto = precoCusto;
        this.margemLucro = margemLucro;
    }

    // Construtor padrão
    public Produto(String desc, double precoCusto, double margemLucro){
        init(desc, precoCusto, margemLucro);
    }
    
    // Construtor sem estoque mínimo - fica considerado como 0. 
    public Produto(String desc, double precoCusto){
        init(desc, precoCusto, MARGEM_PADRAO);
    }

    // Regra de negócio do cálculo do valor de venda do produto.
    // Considera o preço de custo, a margem de lucro, e cálculo de eventuais descontos.
    public double valorDeVenda(){
        return this.precoCusto * (1+this.margemLucro) * this.getDesconto();
    }        

    // Descrição em string do produto, contendo sua descrição e o valor de venda.
    @Override
    public String toString(){
        NumberFormat moeda = NumberFormat.getCurrencyInstance();
        return String.format("NOME: %s: %s", this.descricao, moeda.format(this.valorDeVenda()));
    }

    // Método para calcular o desconto do produto, a ser
    // sobrescrito nas classes que herdam de Produto.
    protected double getDesconto(){
        return DESCONTO_PADRAO;
    }

    // Metodo opcional para setar o clock
    protected static void setClock(Clock novoClock) {
        clock = novoClock != null ? novoClock : Clock.systemDefaultZone();
    }
    
    // Método para pegar a data atual
    protected static LocalDate getDataAtual() {
        return LocalDate.now(clock);
    }
    
    // Método para resetar o clock
    protected static void resetClock() {
        clock = Clock.systemDefaultZone();
    }
}
