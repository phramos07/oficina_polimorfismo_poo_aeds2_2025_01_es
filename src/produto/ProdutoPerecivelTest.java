package produto;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;

public class ProdutoPerecivelTest {
    // Dias que faltam para o produto vencer.
    // O número não é arbitrário. 8 dias permite testar o desconto
    // de 7 dias e posteriormente a proibição de venda, com simulação
    // de passagem de tempo.
    static int diasProdutoVencimento = 8;

    // Data fixa (mock)
    static Clock dataFixaHoje;

    static Produto produtoValido;

    static double epsilon = 0.000001f;

        
    @BeforeEach
    public void prepare(){
        // Seta a data atual para uma data fixa, passada,
        // para realizar os testes.
        // 20/03/2024 às 10:00:00
        dataFixaHoje = Clock.fixed(
            Instant.parse("2024-03-20T10:00:00Z"), 
            ZoneId.systemDefault()
        );
        Produto.setClock(dataFixaHoje);

        // Seta o locale para PTBR
        Locale.setDefault(new Locale("pt", "BR"));

        // Cria um produto que vence em X dias
        produtoValido = new ProdutoPerecivel(
            "Produto teste valido",
            100,
            0.1,
            LocalDate.now(dataFixaHoje).plusDays(diasProdutoVencimento)
        );
    }

    @AfterEach
    public void cleanup(){
        // Reseta o clock para o padrão (data atual do sistema)
        Produto.resetClock();
    }
    
    @Test
    public void calculaPrecoCorretamente(){
        assertEquals(110.0, produtoValido.valorDeVenda(), epsilon);
    }

    @Test
    public void stringComDescricaoEValor(){
        String desc = produtoValido.toString();
        assertTrue(
            desc.contains("Produto teste valido")
            && desc.contains("R$")
            && desc.contains("110,00"));
    }

    @Test
    public void naoCriaProdutoComPrecoNegativo(){
        assertThrows(
            IllegalArgumentException.class,
            () -> new ProdutoPerecivel(
                "teste",
                -5,
                0.5,
                LocalDate.now(dataFixaHoje).plusDays(diasProdutoVencimento)
            )
        );
    }
    
    @Test
    public void naoCriaProdutoComMargemNegativa(){
        assertThrows(
            IllegalArgumentException.class,
            () -> new ProdutoPerecivel(
                "teste",
                5,
                -1,
                LocalDate.now(dataFixaHoje).plusDays(diasProdutoVencimento)
            )
        );
    }

    @Test 
    public void naoCriaProdutoComDataDeValidadeNula(){
        assertThrows(
            IllegalArgumentException.class,
            () -> new ProdutoPerecivel(
                "teste",
                5,
                0.5,
                null
            )
        );
    }

    @Test
    public void naoCriaProdutoComDataDeValidadeAnterior(){
        assertThrows(
            IllegalArgumentException.class,
            () -> new ProdutoPerecivel(
                "teste",
                5,
                0.5,
                Produto.getDataAtual().minusDays(1)
            )
        );
    }

    @Test
    public void calculaCorretamenteDescontoProximoDataValidade() {
        simulaPassagemDeTempo(diasProdutoVencimento - 1);
        assertEquals(82.50, produtoValido.valorDeVenda(), epsilon);
    }

    @Test 
    public void naoVendeProdutoForaDaDataDeValidade(){
        simulaPassagemDeTempo(diasProdutoVencimento + 1);
        assertThrows(IllegalStateException.class, () -> produtoValido.valorDeVenda());
    }

    private void simulaPassagemDeTempo(int dias){
        // Simula a passagem de dias a partir de 20/03/2024 às 10:00:00, data arbitrária.
        // Para testar o desconto do produto perecível.
        Produto.setClock(Clock.offset(dataFixaHoje, Duration.ofDays(dias)));
    }
}
