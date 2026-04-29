package service;

import model.dao.CidadeDAO;
import model.dao.ColetaDAO;
import model.entities.Cidade;
import model.entities.Coleta;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuService {

    private static final String MSG_ID_CIDADE = "Digite o ID da cidade: ";

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // =========================
    // CASE 1
    // =========================
    public void cadastrarColeta(Scanner sc) {

        CidadeDAO cidadeDAO = new CidadeDAO();
        ColetaDAO coletaDAO = new ColetaDAO();

        List<Cidade> cidades = cidadeDAO.listar();

        System.out.println("\n=== CIDADES ===");
        cidades.forEach(c ->
                System.out.println(c.getId() + " - " + c.getNome())
        );

        System.out.print(MSG_ID_CIDADE);
        int cidadeId = sc.nextInt();

        Cidade cidadeEscolhida = cidades.stream()
                .filter(c -> c.getId() == cidadeId)
                .findFirst()
                .orElse(null);

        if (cidadeEscolhida == null) {
            System.out.println("Cidade não encontrada!");
            return;
        }

        System.out.print("Data (dd/MM/yyyy): ");
        LocalDate data = LocalDate.parse(sc.next(), formatter);

        System.out.print("Casos: ");
        int casos = sc.nextInt();

        System.out.print("Óbitos: ");
        int obitos = sc.nextInt();

        coletaDAO.inserir(new Coleta(data, casos, obitos, cidadeEscolhida));

        System.out.println("Coleta cadastrada!");
    }

    // =========================
    // CASE 2
    // =========================
    public void atualizarColeta(Scanner sc) {

        CidadeDAO cidadeDAO = new CidadeDAO();
        ColetaDAO coletaDAO = new ColetaDAO();

        List<Cidade> cidades = cidadeDAO.listar();

        System.out.println("\n=== CIDADES ===");
        cidades.forEach(c ->
                System.out.println(c.getId() + " - " + c.getNome())
        );

        System.out.print(MSG_ID_CIDADE);
        int cidadeId = sc.nextInt();

        List<Coleta> coletas = coletaDAO.listarPorCidade(cidadeId);

        System.out.println("\n=== COLETAS ===");
        for (Coleta c : coletas) {
            System.out.println(
                    "ID: " + c.getId() +
                            " | " + c.getDataColeta().format(formatter) +
                            " | Casos: " + c.getCasos() +
                            " | Óbitos: " + c.getObitos()
            );
        }

        System.out.print("Digite o ID da coleta: ");
        int id = sc.nextInt();

        Coleta coleta = coletas.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);

        if (coleta == null) {
            System.out.println("Coleta não encontrada!");
            return;
        }

        System.out.print("Novos casos: ");
        int casos = sc.nextInt();

        System.out.print("Novos óbitos: ");
        int obitos = sc.nextInt();

        coletaDAO.atualizar(new Coleta(
                coleta.getId(),
                coleta.getDataColeta(),
                casos,
                obitos,
                coleta.getCidade()
        ));

        System.out.println("Coleta atualizada!");;
    }

    // =========================
    // CASE 3
    // =========================
    public void relatorioCidade(Scanner sc) {

        CidadeDAO cidadeDAO = new CidadeDAO();
        ColetaDAO coletaDAO = new ColetaDAO();

        List<Cidade> cidades = cidadeDAO.listar();

        cidades.forEach(c ->
                System.out.println(c.getId() + " - " + c.getNome())
        );

        System.out.print(MSG_ID_CIDADE);
        int cidadeId = sc.nextInt();

        Cidade cidade = cidades.stream()
                .filter(c -> c.getId() == cidadeId)
                .findFirst()
                .orElse(null);

        if (cidade == null) {
            System.out.println("Cidade não encontrada!");
            return;
        }

        List<Coleta> coletas = coletaDAO.listarPorCidade(cidadeId);

        System.out.print("Quantas coletas deseja visualizar? ");
        int qtd = sc.nextInt();

        int total = 0;
        System.out.println("\n=== RELATÓRIO ===");

        for (int i = 0; i < Math.min(qtd, coletas.size()); i++) {
            Coleta c = coletas.get(i);

            System.out.println(
                    "Data: " + c.getDataColeta().format(formatter) +
                            " | Casos: " + c.getCasos() +
                            " | Óbitos: " + c.getObitos()
            );

            total += c.getCasos();
        }

        double percentual = (double) total / cidade.getPopulacao() * 100;

        System.out.println("Total de casos: " + total);
        System.out.printf("Percentual da população: %.6f%%\n", percentual);
    }

    // =========================
    // CASE 4
    // =========================
    public void compararCidades(Scanner sc) {

        CidadeDAO cidadeDAO = new CidadeDAO();
        ColetaDAO coletaDAO = new ColetaDAO();

        List<Cidade> cidades = cidadeDAO.listar();

        cidades.forEach(c ->
                System.out.println(c.getId() + " - " + c.getNome())
        );

        System.out.print("ID cidade 1: ");
        int id1 = sc.nextInt();

        System.out.print("ID cidade 2: ");
        int id2 = sc.nextInt();

        int total1 = coletaDAO.listarPorCidade(id1).stream().mapToInt(Coleta::getCasos).sum();
        int total2 = coletaDAO.listarPorCidade(id2).stream().mapToInt(Coleta::getCasos).sum();

        System.out.println("\n=== COMPARAÇÃO ===");
        System.out.println("Cidade 1: " + total1);
        System.out.println("Cidade 2: " + total2);
        System.out.println("Diferença: " + Math.abs(total1 - total2));
    }

    // =========================
    // CASE 5
    // =========================
    public void relatorioGeral(Scanner sc) {

        CidadeDAO cidadeDAO = new CidadeDAO();
        ColetaDAO coletaDAO = new ColetaDAO();

        LocalDate inicio = null;
        LocalDate fim = null;

        // leitura e validação
        while (true) {
            try {
                System.out.print("Data inicial (dd/MM/yyyy): ");
                inicio = LocalDate.parse(sc.next(), formatter);

                System.out.print("Data final (dd/MM/yyyy): ");
                fim = LocalDate.parse(sc.next(), formatter);

                if (inicio.isAfter(fim)) {
                    System.out.println("Erro: a data inicial deve ser menor ou igual à final.\n");
                    continue;
                }

                break;

            } catch (Exception e) {
                System.out.println("Erro: formato de data inválido. Use dd/MM/yyyy.\n");
                sc.nextLine();
            }
        }

        // resolve lambda (variáveis efetivamente finais)
        final LocalDate inicioFinal = inicio;
        final LocalDate fimFinal = fim;

        System.out.println("\n=== RELATÓRIO GERAL ===");

        for (Cidade cidade : cidadeDAO.listar()) {

            int total = coletaDAO.listarPorCidade(cidade.getId())
                    .stream()
                    .filter(c -> !c.getDataColeta().isBefore(inicioFinal) &&
                            !c.getDataColeta().isAfter(fimFinal))
                    .mapToInt(Coleta::getCasos)
                    .sum();

            double percentual = (double) total / cidade.getPopulacao() * 100;

            System.out.printf(
                    "%s | Casos: %d | %.4f%%\n",
                    cidade.getNome(),
                    total,
                    percentual
            );
        }
    }
}