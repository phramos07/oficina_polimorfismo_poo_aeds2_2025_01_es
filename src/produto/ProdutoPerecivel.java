package produto;

import java.time.LocalDate;

public class ProdutoPerecivel extends Produto {
    private static final double DESCONTO_PERECIVEL = 0.25;
    private static final int PRAZO_DESCONTO_DIAS = 7;
    private LocalDate dataValidade;

    public ProdutoPerecivel(
        String desc,
        double precoCusto,
        double margemLucro,
        LocalDate dataValidade
    ) throws IllegalArgumentException {
        super(desc, precoCusto, margemLucro);
        if (dataValidade == null) {
            throw new IllegalArgumentException(
                "Data de validade não pode ser nula"
            );
        }

        if (dataValidade.isBefore(Produto.getDataAtual())) {
            throw new IllegalArgumentException(
                "Data de validade não pode ser anterior à data atual"
            );
        }

        this.dataValidade = dataValidade;
    }

    @Override
    public double valorDeVenda() {
        LocalDate hoje = Produto.getDataAtual();
        if (hoje.isAfter(dataValidade)) {
            throw new IllegalStateException("O produto está fora da data de validade.");
        }

        return super.valorDeVenda();
    }

    @Override
    protected double getDesconto() {
        LocalDate hoje = Produto.getDataAtual();
        LocalDate prazoDesconto = dataValidade.minusDays(PRAZO_DESCONTO_DIAS);
        if (hoje.isAfter(prazoDesconto)) {
            return (1.0 - DESCONTO_PERECIVEL);
        }

        return super.getDesconto();
    }
}
