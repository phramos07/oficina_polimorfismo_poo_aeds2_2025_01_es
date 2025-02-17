import produto.Produto;
import produto.ProdutoNaoPerecivel;

public class App {
    public static void main(String[] args) {
        Produto produto = new ProdutoNaoPerecivel("Produto teste", 100, 0.1);

        System.out.println("Preço de venda: " + produto.valorDeVenda());
    }
}
