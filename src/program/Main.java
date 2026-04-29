package program;

import service.MenuService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        MenuService menu = new MenuService();

        int opcao = -1;

        do {
            try {
                System.out.println("\n=== SISTEMA DE MONITORAMENTO DA DENGUE ===");
                System.out.println("1 - Cadastrar coleta");
                System.out.println("2 - Atualizar coleta");
                System.out.println("3 - Relatório por cidade");
                System.out.println("4 - Comparar duas cidades");
                System.out.println("5 - Relatório geral");
                System.out.println("0 - Sair\n");

                System.out.print("Escolha: ");
                opcao = sc.nextInt();

                switch (opcao) {
                    case 1:
                        System.out.println("Opção 1 selecionada");
                        menu.cadastrarColeta(sc);
                        break;
                    case 2:
                        System.out.println("Opção 2 selecionada");
                        menu.atualizarColeta(sc);
                        break;
                    case 3:
                        System.out.println("Opção 3 selecionada");
                        menu.relatorioCidade(sc);
                        break;
                    case 4:
                        System.out.println("Opção 4 selecionada");
                        menu.compararCidades(sc);
                        break;
                    case 5:
                        System.out.println("Opção 5 selecionada");
                        menu.relatorioGeral(sc);
                        break;
                    case 0:
                        System.out.println("Encerrando...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Erro: Input inválido");
                sc.nextLine();

            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
                sc.nextLine();
            }

        } while (opcao != 0);

        sc.close();
    }
}