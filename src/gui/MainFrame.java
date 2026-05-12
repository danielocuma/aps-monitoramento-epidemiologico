package gui;

import model.dao.CidadeDAO;
import model.dao.ColetaDAO;
import model.entities.Cidade;
import model.entities.Coleta;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainFrame extends JFrame {

    private static final Color AZUL_FUNDO    = new Color( 10,  18,  40);
    private static final Color AZUL_PAINEL   = new Color( 16,  28,  60);
    private static final Color AZUL_CARD     = new Color( 22,  38,  80);
    private static final Color AZUL_BORDA    = new Color( 40,  70, 130);
    private static final Color LARANJA       = new Color(255, 140,  30);
    private static final Color VERDE         = new Color( 50, 200, 130);
    private static final Color VERMELHO      = new Color(220,  60,  60);
    private static final Color BRANCO        = new Color(240, 245, 255);
    private static final Color CINZA_TEXTO   = new Color(160, 175, 210);
    private static final Color AZUL_DEST     = new Color( 80, 140, 255);
    private static final Color SEPARADOR     = new Color( 35,  55, 100);
    private static final Color ROXO          = new Color(180,  80, 255);
    private static final Color CIANO         = new Color( 80, 200, 200);
    private static final Color INPUT_BG      = new Color( 12,  22,  50);
    private static final Color TABELA_PAR    = new Color( 14,  24,  52);
    private static final Color TABELA_IMPAR  = new Color( 18,  32,  68);

    private static final Font F_TITULO    = new Font("Segoe UI", Font.BOLD,  20);
    private static final Font F_SUBTITULO = new Font("Segoe UI", Font.BOLD,  14);
    private static final Font F_LABEL     = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font F_NORMAL    = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font F_SMALL     = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font F_MICRO     = new Font("Segoe UI", Font.BOLD,  10);
    private static final Font F_BOTAO     = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font F_NUMERO    = new Font("Segoe UI", Font.BOLD,  32);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final CidadeDAO cidadeDAO = new CidadeDAO();
    private final ColetaDAO coletaDAO = new ColetaDAO();

    private final JPanel painelConteudo = new JPanel(new BorderLayout());
    private final JLabel lblBreadcrumb  = new JLabel("Menu Principal");

    public MainFrame() {
        super("SisVigiDengue — Monitoramento Epidemiológico");
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        configurarJanela();
        construirLayout();
        mostrarMenu();
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);
        getContentPane().setBackground(AZUL_FUNDO);
    }

    private void construirLayout() {
        setLayout(new BorderLayout(0, 0));
        add(criarSidebar(), BorderLayout.WEST);
        JPanel areaDireita = new JPanel(new BorderLayout(0, 0));
        areaDireita.setBackground(AZUL_FUNDO);
        areaDireita.add(criarTopbar(),  BorderLayout.NORTH);
        areaDireita.add(criarRodape(),  BorderLayout.SOUTH);
        painelConteudo.setBackground(AZUL_FUNDO);
        painelConteudo.setBorder(new EmptyBorder(20, 28, 20, 28));
        areaDireita.add(painelConteudo, BorderLayout.CENTER);
        add(areaDireita, BorderLayout.CENTER);
    }

    private JPanel criarSidebar() {
        JPanel sidebar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, AZUL_PAINEL, 0, getHeight(), new Color(8, 14, 32)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(AZUL_BORDA);
                g2.fillRect(getWidth() - 1, 0, 1, getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);

        JPanel logo = new JPanel(new BorderLayout());
        logo.setOpaque(false);
        logo.setBorder(new EmptyBorder(24, 18, 20, 18));
        logo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel icone = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 140, 30, 40));
                g2.fillOval(0, 0, 42, 42);
                g2.setColor(LARANJA);
                g2.setStroke(new BasicStroke(2.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(21, 5, 21, 37);
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(new Color(255, 190, 80));
                int[] xs = {21,27,21,15,21,27,21,15,21};
                int[] ys = { 8,14,18,22,26,30,34,38,38};
                for (int i = 0; i < xs.length - 1; i++) g2.drawLine(xs[i], ys[i], xs[i+1], ys[i+1]);
                g2.setColor(LARANJA);
                g2.fillOval(18, 4, 7, 7);
                g2.setColor(new Color(255, 200, 80));
                g2.fillOval(17, 3, 8, 8);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(44, 44); }
        };

        JPanel textoLogo = new JPanel();
        textoLogo.setLayout(new BoxLayout(textoLogo, BoxLayout.Y_AXIS));
        textoLogo.setOpaque(false);
        textoLogo.setBorder(new EmptyBorder(0, 10, 0, 0));
        JLabel lNome = new JLabel("SisVigiDengue");
        lNome.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lNome.setForeground(BRANCO);
        JLabel lSub = new JLabel("Epidemiologia v1.0");
        lSub.setFont(F_SMALL);
        lSub.setForeground(CINZA_TEXTO);
        textoLogo.add(lNome);
        textoLogo.add(lSub);
        logo.add(icone, BorderLayout.WEST);
        logo.add(textoLogo, BorderLayout.CENTER);
        sidebar.add(logo);
        sidebar.add(criarSepH());
        sidebar.add(Box.createVerticalStrut(10));

        JLabel lblNav = new JLabel("  NAVEGAÇÃO");
        lblNav.setFont(F_MICRO);
        lblNav.setForeground(new Color(70, 95, 150));
        lblNav.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(lblNav);
        sidebar.add(Box.createVerticalStrut(4));

        String[]   icos   = { "⊕", "✎", "▤", "⇄", "◈" };
        String[]   textos = { "Cadastrar Coleta", "Atualizar Coleta", "Rel. por Cidade", "Comparar Cidades", "Rel. Geral" };
        Color[]    cores  = { VERDE, AZUL_DEST, LARANJA, ROXO, CIANO };
        Runnable[] acoes  = { this::telasCadastrarColeta, this::telaAtualizarColeta, this::telaRelatorioCidade, this::telaCompararCidades, this::telaRelatorioGeral };
        for (int i = 0; i < icos.length; i++) sidebar.add(criarItemSidebar(icos[i], textos[i], cores[i], acoes[i]));

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(criarSepH());

        JPanel rodSidebar = new JPanel(new BorderLayout());
        rodSidebar.setOpaque(false);
        rodSidebar.setBorder(new EmptyBorder(8, 18, 14, 14));
        rodSidebar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JLabel lblUnip = new JLabel("UNIP — APS 2026");
        lblUnip.setFont(F_SMALL);
        lblUnip.setForeground(new Color(55, 75, 120));
        rodSidebar.add(lblUnip, BorderLayout.WEST);
        sidebar.add(rodSidebar);
        return sidebar;
    }

    private JPanel criarItemSidebar(String ico, String texto, Color acento, Runnable acao) {
        boolean[] hov = {false};
        JPanel item = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                if (hov[0]) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(acento.getRed(), acento.getGreen(), acento.getBlue(), 22));
                    g2.fillRoundRect(8, 2, getWidth() - 16, getHeight() - 4, 8, 8);
                    g2.setColor(acento);
                    g2.fillRoundRect(8, 6, 3, getHeight() - 12, 3, 3);
                    g2.dispose();
                }
            }
        };
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        item.setBorder(new EmptyBorder(5, 16, 5, 12));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lIco = new JLabel(ico) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(acento.getRed(), acento.getGreen(), acento.getBlue(), hov[0] ? 50 : 30));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lIco.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lIco.setForeground(acento);
        lIco.setPreferredSize(new Dimension(26, 26));
        lIco.setHorizontalAlignment(SwingConstants.CENTER);
        lIco.setVerticalAlignment(SwingConstants.CENTER);

        JLabel lTxt = new JLabel("  " + texto);
        lTxt.setFont(F_NORMAL);
        lTxt.setForeground(CINZA_TEXTO);

        item.add(lIco, BorderLayout.WEST);
        item.add(lTxt, BorderLayout.CENTER);
        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hov[0]=true;  lTxt.setForeground(BRANCO); item.repaint(); }
            public void mouseExited (MouseEvent e) { hov[0]=false; lTxt.setForeground(CINZA_TEXTO); item.repaint(); }
            public void mouseClicked(MouseEvent e) { acao.run(); }
        });
        return item;
    }

    private JPanel criarTopbar() {
        JPanel top = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(14, 22, 48));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(SEPARADOR);
                g.fillRect(0, getHeight() - 1, getWidth(), 1);
            }
        };
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(11, 24, 11, 24));

        JPanel bc = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        bc.setOpaque(false);
        JLabel lHome = new JLabel("Início"); lHome.setFont(F_SMALL); lHome.setForeground(CINZA_TEXTO);
        JLabel lSep  = new JLabel("›");      lSep.setFont(F_SMALL);  lSep.setForeground(AZUL_BORDA);
        lblBreadcrumb.setFont(F_LABEL);
        lblBreadcrumb.setForeground(LARANJA);
        bc.add(lHome); bc.add(lSep); bc.add(lblBreadcrumb);

        JPanel st = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        st.setOpaque(false);
        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(VERDE);
                g2.fillOval(0, 3, 8, 8);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(10, 14); }
        };
        dot.setOpaque(false);
        JLabel lAtivo = new JLabel("Sistema Ativo"); lAtivo.setFont(F_SMALL); lAtivo.setForeground(VERDE);
        JLabel lData  = new JLabel("  |  " + LocalDate.now().format(FMT)); lData.setFont(F_SMALL); lData.setForeground(CINZA_TEXTO);
        st.add(dot); st.add(lAtivo); st.add(lData);
        top.add(bc, BorderLayout.WEST);
        top.add(st, BorderLayout.EAST);
        return top;
    }

    private JPanel criarRodape() {
        JPanel rod = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(9, 15, 34));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(SEPARADOR);
                g.fillRect(0, 0, getWidth(), 1);
            }
        };
        rod.setOpaque(false);
        rod.setBorder(new EmptyBorder(6, 24, 6, 24));
        JLabel esq = new JLabel("Sistema de Vigilância Epidemiológica — Dengue  |  Grande São Paulo");
        esq.setFont(F_SMALL); esq.setForeground(new Color(55, 75, 115));
        JLabel dir = new JLabel("Java Swing  ·  MySQL  ·  JDBC");
        dir.setFont(F_SMALL); dir.setForeground(new Color(55, 75, 115));
        rod.add(esq, BorderLayout.WEST);
        rod.add(dir, BorderLayout.EAST);
        return rod;
    }

    private void mostrarMenu() {
        lblBreadcrumb.setText("Menu Principal");
        painelConteudo.removeAll();

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;

        JPanel cab = new JPanel();
        cab.setLayout(new BoxLayout(cab, BoxLayout.Y_AXIS));
        cab.setOpaque(false);
        cab.setBorder(new EmptyBorder(0, 0, 4, 0));
        JLabel lT = new JLabel("Painel de Controle"); lT.setFont(F_TITULO); lT.setForeground(BRANCO);
        JLabel lS = new JLabel("Selecione uma operação para iniciar o monitoramento epidemiológico."); lS.setFont(F_NORMAL); lS.setForeground(CINZA_TEXTO);
        cab.add(lT); cab.add(Box.createVerticalStrut(3)); cab.add(lS);
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 16, 0);
        wrapper.add(cab, gbc);

        JPanel grade = new JPanel(new GridLayout(3, 2, 12, 12));
        grade.setOpaque(false);
        String[]   tits  = { "Cadastrar Coleta", "Atualizar Coleta", "Relatório por Cidade", "Comparar Cidades", "Relatório Geral" };
        String[]   descs = { "Registrar novos casos e óbitos por município", "Corrigir dados de coletas já registradas", "Analisar histórico e indicadores de uma cidade", "Confrontar totais entre dois municípios", "Panorama regional filtrado por período" };
        String[]   icos  = { "⊕", "✎", "▤", "⇄", "◈" };
        Color[]    acs   = { VERDE, AZUL_DEST, LARANJA, ROXO, CIANO };
        Runnable[] acoes = { this::telasCadastrarColeta, this::telaAtualizarColeta, this::telaRelatorioCidade, this::telaCompararCidades, this::telaRelatorioGeral };
        for (int i = 0; i < tits.length; i++) grade.add(criarCardMenu(icos[i], tits[i], descs[i], acs[i], acoes[i]));
        JPanel vazio = new JPanel(); vazio.setOpaque(false);
        grade.add(vazio);

        gbc.gridy = 1; gbc.weighty = 1; gbc.fill = GridBagConstraints.BOTH; gbc.insets = new Insets(0, 0, 0, 0);
        wrapper.add(grade, gbc);
        painelConteudo.add(wrapper, BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    private JPanel criarCardMenu(String ico, String titulo, String desc, Color acento, Runnable acao) {
        boolean[] hov = {false};
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hov[0] ? new Color(28, 48, 95) : AZUL_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(acento);
                g2.fillRoundRect(0, 0, getWidth(), 4, 4, 4);
                g2.fillRect(0, 2, getWidth(), 2);
                g2.setColor(hov[0] ? acento : AZUL_BORDA);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 22, 20, 22));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lIco = new JLabel(ico) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(acento.getRed(), acento.getGreen(), acento.getBlue(), 28));
                g2.fillOval(0, 0, 44, 44);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lIco.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        lIco.setForeground(acento);
        lIco.setPreferredSize(new Dimension(46, 46));
        lIco.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel seta = new JLabel("→");
        seta.setFont(new Font("Segoe UI", Font.BOLD, 16));
        seta.setForeground(new Color(acento.getRed(), acento.getGreen(), acento.getBlue(), 140));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.add(lIco, BorderLayout.WEST);
        topo.add(seta, BorderLayout.EAST);

        JLabel lTit = new JLabel(titulo); lTit.setFont(F_SUBTITULO); lTit.setForeground(BRANCO);
        JLabel lDesc = new JLabel("<html><body style='width:155px'>" + desc + "</body></html>"); lDesc.setFont(F_SMALL); lDesc.setForeground(CINZA_TEXTO);

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);
        textos.add(lTit); textos.add(Box.createVerticalStrut(4)); textos.add(lDesc);

        card.add(topo, BorderLayout.NORTH);
        card.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        card.add(textos, BorderLayout.SOUTH);
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hov[0]=true;  card.repaint(); }
            public void mouseExited (MouseEvent e) { hov[0]=false; card.repaint(); }
            public void mouseClicked(MouseEvent e) { acao.run(); }
        });
        return card;
    }

    private void telasCadastrarColeta() {
        lblBreadcrumb.setText("Cadastrar Coleta");
        painelConteudo.removeAll();
        List<Cidade> cidades = cidadeDAO.listar();
        JPanel painel = criarPainelTela("⊕  Cadastrar Nova Coleta", VERDE, "Registre os dados da coleta epidemiológica para o município selecionado.");

        JComboBox<String> comboCidade = new JComboBox<>();
        for (Cidade c : cidades) comboCidade.addItem(c.getNome() + "  —  pop. " + String.format("%,d", c.getPopulacao()));
        estilizarCombo(comboCidade);
        adicionarCampo(painel, "MUNICÍPIO", comboCidade);

        JTextField campData   = criarField("dd/MM/yyyy");
        JTextField campCasos  = criarField("Número de casos");
        JTextField campObitos = criarField("Número de óbitos");
        adicionarCampo(painel, "DATA DA COLETA",     campData);
        adicionarCampo(painel, "CASOS REGISTRADOS",  campCasos);
        adicionarCampo(painel, "ÓBITOS REGISTRADOS", campObitos);

        JPanel botoes = criarBarraBotoes();
        JButton btnVoltar = criarBotao("← Voltar",      AZUL_BORDA, false);
        JButton btnSalvar = criarBotao("Salvar Coleta", VERDE,      true);
        btnVoltar.addActionListener(e -> mostrarMenu());
        btnSalvar.addActionListener(e -> {
            try {
                Cidade c = cidades.get(comboCidade.getSelectedIndex());
                LocalDate data = LocalDate.parse(lerField(campData), FMT);
                int casos  = Integer.parseInt(lerField(campCasos).trim());
                int obitos = Integer.parseInt(lerField(campObitos).trim());
                if (casos < 0 || obitos < 0) throw new IllegalArgumentException("Valores não podem ser negativos.");
                if (obitos > casos)           throw new IllegalArgumentException("Óbitos não podem superar o total de casos.");
                coletaDAO.inserir(new Coleta(data, casos, obitos, c));
                mostrarSucesso("Coleta cadastrada com sucesso!");
                mostrarMenu();
            } catch (DateTimeParseException ex) { mostrarErro("Data inválida. Use o formato dd/MM/yyyy.");
            } catch (NumberFormatException   ex) { mostrarErro("Casos e Óbitos devem ser números inteiros.");
            } catch (IllegalArgumentException ex) { mostrarErro(ex.getMessage());
            } catch (Exception               ex) { mostrarErro("Erro ao salvar: " + ex.getMessage()); }
        });
        botoes.add(btnVoltar); botoes.add(Box.createHorizontalStrut(8)); botoes.add(btnSalvar);
        painel.add(Box.createVerticalStrut(16)); painel.add(botoes);
        embutirTela(painel);
    }

    private void telaAtualizarColeta() {
        lblBreadcrumb.setText("Atualizar Coleta");
        painelConteudo.removeAll();
        List<Cidade> cidades = cidadeDAO.listar();
        JPanel painel = criarPainelTela("✎  Atualizar Coleta", AZUL_DEST, "Selecione a cidade, clique em uma linha da tabela e edite os valores.");

        JComboBox<String> comboCidade = new JComboBox<>();
        for (Cidade c : cidades) comboCidade.addItem(c.getNome());
        estilizarCombo(comboCidade);
        adicionarCampo(painel, "MUNICÍPIO", comboCidade);

        String[] cols = {"#", "Data", "Casos", "Óbitos"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        JTable tabela = criarTabela(modelo);
        JScrollPane scrollTab = estilizarScroll(new JScrollPane(tabela));
        scrollTab.setAlignmentX(LEFT_ALIGNMENT);
        scrollTab.setMaximumSize(new Dimension(Integer.MAX_VALUE, 165));

        JTextField campData   = criarField(""); campData.setEnabled(false);
        JTextField campCasos  = criarField(""); campCasos.setEnabled(false);
        JTextField campObitos = criarField(""); campObitos.setEnabled(false);

        JLabel lblSel = new JLabel("← Clique em uma linha da tabela para editar");
        lblSel.setFont(F_SMALL); lblSel.setForeground(CINZA_TEXTO); lblSel.setAlignmentX(LEFT_ALIGNMENT);

        final Coleta[] coletaSelecionada = {null};

        Runnable carregar = () -> {
            modelo.setRowCount(0);
            coletaSelecionada[0] = null;
            Cidade c = cidades.get(comboCidade.getSelectedIndex());
            List<Coleta> col = coletaDAO.listarPorCidade(c.getId());
            for (int i = 0; i < col.size(); i++) {
                Coleta x = col.get(i);
                modelo.addRow(new Object[]{ i+1, x.getDataColeta().format(FMT), x.getCasos(), x.getObitos() });
            }
            campData.setEnabled(false);   campData.setText("");
            campCasos.setEnabled(false);  campCasos.setText("");
            campObitos.setEnabled(false); campObitos.setText("");
            lblSel.setForeground(CINZA_TEXTO);
            lblSel.setText(col.isEmpty() ? "Nenhuma coleta cadastrada para este município." : "← Clique em uma linha da tabela para editar");
        };

        comboCidade.addActionListener(e -> carregar.run());
        carregar.run();

        tabela.getSelectionModel().addListSelectionListener(ev -> {
            int row = tabela.getSelectedRow();
            if (row >= 0) {
                Cidade c = cidades.get(comboCidade.getSelectedIndex());
                List<Coleta> col = coletaDAO.listarPorCidade(c.getId());
                if (row < col.size()) {
                    coletaSelecionada[0] = col.get(row);
                    campData.setText(coletaSelecionada[0].getDataColeta().format(FMT)); campData.setForeground(BRANCO);
                    campCasos.setText(String.valueOf(coletaSelecionada[0].getCasos())); campCasos.setForeground(BRANCO);
                    campObitos.setText(String.valueOf(coletaSelecionada[0].getObitos())); campObitos.setForeground(BRANCO);
                    campData.setEnabled(true); campCasos.setEnabled(true); campObitos.setEnabled(true);
                    lblSel.setText("Editando coleta #" + (row+1) + "  —  dados pré-preenchidos para facilitar");
                    lblSel.setForeground(LARANJA);
                }
            }
        });

        painel.add(scrollTab);
        painel.add(Box.createVerticalStrut(6));
        painel.add(lblSel);
        painel.add(Box.createVerticalStrut(10));
        adicionarCampo(painel, "DATA DA COLETA", campData);
        adicionarCampo(painel, "CASOS",          campCasos);
        adicionarCampo(painel, "ÓBITOS",         campObitos);

        JPanel botoes = criarBarraBotoes();
        JButton btnVoltar  = criarBotao("← Voltar",        AZUL_BORDA, false);
        JButton btnRemover = criarBotao("Remover Coleta",   VERMELHO,   false);
        JButton btnSalvar  = criarBotao("Salvar Alteração", AZUL_DEST,  true);
        btnVoltar.addActionListener(e -> mostrarMenu());

        btnRemover.addActionListener(e -> {
            if (coletaSelecionada[0] == null) { mostrarErro("Selecione uma coleta na tabela."); return; }
            int row = tabela.getSelectedRow();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja remover permanentemente a coleta #" + (row+1) + " (" + coletaSelecionada[0].getDataColeta().format(FMT) + ")?",
                    "Confirmar Remoção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try { coletaDAO.deletar(coletaSelecionada[0].getId()); mostrarSucesso("Coleta removida com sucesso!"); carregar.run();
                } catch (Exception ex) { mostrarErro("Erro ao remover: " + ex.getMessage()); }
            }
        });

        btnSalvar.addActionListener(e -> {
            if (coletaSelecionada[0] == null) { mostrarErro("Selecione uma coleta na tabela."); return; }
            try {
                LocalDate novaData = LocalDate.parse(campData.getText().trim(), FMT);
                int casos  = Integer.parseInt(campCasos.getText().trim());
                int obitos = Integer.parseInt(campObitos.getText().trim());
                if (casos < 0 || obitos < 0) throw new IllegalArgumentException("Valores não podem ser negativos.");
                if (obitos > casos)           throw new IllegalArgumentException("Óbitos não podem superar o total de casos.");
                coletaDAO.atualizar(new Coleta(coletaSelecionada[0].getId(), novaData, casos, obitos, coletaSelecionada[0].getCidade()));
                mostrarSucesso("Coleta atualizada com sucesso!"); carregar.run();
            } catch (DateTimeParseException ex) { mostrarErro("Data inválida. Use o formato dd/MM/yyyy.");
            } catch (NumberFormatException   ex) { mostrarErro("Casos e Óbitos devem ser números inteiros.");
            } catch (IllegalArgumentException ex) { mostrarErro(ex.getMessage());
            } catch (Exception               ex) { mostrarErro("Erro: " + ex.getMessage()); }
        });

        botoes.add(btnVoltar); botoes.add(Box.createHorizontalStrut(8)); botoes.add(btnRemover); botoes.add(Box.createHorizontalStrut(8)); botoes.add(btnSalvar);
        painel.add(Box.createVerticalStrut(16)); painel.add(botoes);
        embutirTela(painel);
    }

    private void telaRelatorioCidade() {
        lblBreadcrumb.setText("Relatório por Cidade");
        painelConteudo.removeAll();
        List<Cidade> cidades = cidadeDAO.listar();
        JPanel painel = criarPainelTela("▤  Relatório por Cidade", LARANJA, "Visualize o histórico de coletas e os indicadores epidemiológicos do município.");

        JComboBox<String> comboCidade = new JComboBox<>();
        for (Cidade c : cidades) comboCidade.addItem(c.getNome());
        estilizarCombo(comboCidade);
        adicionarCampo(painel, "MUNICÍPIO", comboCidade);

        JSpinner spinnerQtd = new JSpinner(new SpinnerNumberModel(5, 1, 999, 1));
        spinnerQtd.setFont(F_NORMAL);
        estilizarSpinner(spinnerQtd);
        adicionarCampo(painel, "NÚMERO DE COLETAS A EXIBIR", spinnerQtd);

        JPanel botoes = criarBarraBotoes();
        JButton btnVoltar = criarBotao("← Voltar",       AZUL_BORDA, false);
        JButton btnGerar  = criarBotao("Gerar Relatório", LARANJA,    true);
        btnVoltar.addActionListener(e -> mostrarMenu());

        String[] cols = {"Data", "Casos", "Óbitos"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        JTable tabela = criarTabela(modelo);
        JScrollPane scrollTab = estilizarScroll(new JScrollPane(tabela));
        scrollTab.setAlignmentX(LEFT_ALIGNMENT);
        scrollTab.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));

        JPanel cardsGrid = new JPanel(new GridLayout(1, 4, 10, 0));
        cardsGrid.setOpaque(false);
        cardsGrid.setAlignmentX(LEFT_ALIGNMENT);
        cardsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        String[] nomesCard = {"Total Casos", "% Infectada", "Total Óbitos", "% Falecida"};
        Color[]  coresCard = { AZUL_DEST, LARANJA, VERMELHO, ROXO };
        JLabel[] numsCard  = new JLabel[4];
        for (int i = 0; i < 4; i++) { JPanel mc = criarMiniCard(nomesCard[i], coresCard[i]); numsCard[i] = (JLabel) mc.getClientProperty("num"); cardsGrid.add(mc); }

        btnGerar.addActionListener(e -> {
            modelo.setRowCount(0);
            Cidade cidade = cidades.get(comboCidade.getSelectedIndex());
            List<Coleta> col = coletaDAO.listarPorCidade(cidade.getId());
            int qtd = (int) spinnerQtd.getValue(), total = 0, totalOb = 0;
            for (int i = 0; i < Math.min(qtd, col.size()); i++) {
                Coleta x = col.get(i);
                modelo.addRow(new Object[]{ x.getDataColeta().format(FMT), x.getCasos(), x.getObitos() });
                total += x.getCasos(); totalOb += x.getObitos();
            }
            long pop = cidade.getPopulacao();
            double pctC = pop > 0 ? (double) total   / pop * 100 : 0;
            double pctO = pop > 0 ? (double) totalOb / pop * 100 : 0;
            numsCard[0].setText(String.format("%,d", total));
            numsCard[1].setText(String.format("%.4f%%", pctC));
            numsCard[2].setText(String.format("%,d", totalOb));
            numsCard[3].setText(String.format("%.4f%%", pctO));
        });

        botoes.add(btnVoltar); botoes.add(Box.createHorizontalStrut(8)); botoes.add(btnGerar);
        painel.add(botoes);
        painel.add(Box.createVerticalStrut(14));
        painel.add(scrollTab);
        painel.add(Box.createVerticalStrut(12));
        painel.add(cardsGrid);
        embutirTela(painel);
    }

    private void telaCompararCidades() {
        lblBreadcrumb.setText("Comparar Cidades");
        painelConteudo.removeAll();
        List<Cidade> cidades = cidadeDAO.listar();
        JPanel painel = criarPainelTela("⇄  Comparar Dois Municípios", ROXO, "Confronte o total acumulado de casos entre dois municípios.");

        JPanel seletores = new JPanel(new GridLayout(1, 2, 16, 0));
        seletores.setOpaque(false);
        seletores.setAlignmentX(LEFT_ALIGNMENT);
        seletores.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));

        JComboBox<String> combo1 = new JComboBox<>();
        JComboBox<String> combo2 = new JComboBox<>();
        for (Cidade c : cidades) { combo1.addItem(c.getNome()); combo2.addItem(c.getNome()); }
        if (cidades.size() > 1) combo2.setSelectedIndex(1);
        estilizarCombo(combo1); estilizarCombo(combo2);

        painel.add(seletores);
        seletores.add(colCombo("MUNICÍPIO A", combo1));
        seletores.add(colCombo("MUNICÍPIO B", combo2));
        painel.add(Box.createVerticalStrut(12));

        JPanel cardsComp = new JPanel(new GridLayout(1, 2, 16, 0));
        cardsComp.setOpaque(false);
        cardsComp.setAlignmentX(LEFT_ALIGNMENT);
        cardsComp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        cardsComp.setVisible(false);

        JPanel cardA = criarCardComp();
        JPanel cardB = criarCardComp();
        cardsComp.add(cardA); cardsComp.add(cardB);

        JLabel lblDiff = new JLabel(" ");
        lblDiff.setFont(F_LABEL); lblDiff.setForeground(CINZA_TEXTO); lblDiff.setAlignmentX(LEFT_ALIGNMENT);

        JPanel botoes = criarBarraBotoes();
        JButton btnVoltar   = criarBotao("← Voltar", AZUL_BORDA, false);
        JButton btnComparar = criarBotao("Comparar",  ROXO,       true);
        btnVoltar.addActionListener(e -> mostrarMenu());

        btnComparar.addActionListener(e -> {
            Cidade c1 = cidades.get(combo1.getSelectedIndex());
            Cidade c2 = cidades.get(combo2.getSelectedIndex());
            if (c1.getId().equals(c2.getId())) { mostrarErro("Selecione municípios diferentes."); return; }
            int t1 = coletaDAO.listarPorCidade(c1.getId()).stream().mapToInt(Coleta::getCasos).sum();
            int t2 = coletaDAO.listarPorCidade(c2.getId()).stream().mapToInt(Coleta::getCasos).sum();
            atualizarCardComp(cardA, c1.getNome(), t1, t1 >= t2);
            atualizarCardComp(cardB, c2.getNome(), t2, t2 >= t1);
            lblDiff.setText("Diferença: " + String.format("%,d", Math.abs(t1 - t2)) + " casos");
            cardsComp.setVisible(true);
            painel.revalidate(); painel.repaint();
        });

        botoes.add(btnVoltar); botoes.add(Box.createHorizontalStrut(8)); botoes.add(btnComparar);
        painel.add(botoes);
        painel.add(Box.createVerticalStrut(12));
        painel.add(cardsComp);
        painel.add(Box.createVerticalStrut(6));
        painel.add(lblDiff);
        embutirTela(painel);
    }

    private JPanel colCombo(String rotulo, JComboBox<String> cb) {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);
        JLabel r = new JLabel(rotulo); r.setFont(F_MICRO); r.setForeground(new Color(100, 120, 170));
        col.add(r); col.add(Box.createVerticalStrut(4)); col.add(cb);
        return col;
    }

    private JPanel criarCardComp() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AZUL_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(AZUL_BORDA);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        JLabel lNome   = new JLabel("—"); lNome.setFont(F_SUBTITULO); lNome.setForeground(CINZA_TEXTO); lNome.setAlignmentX(CENTER_ALIGNMENT);
        JLabel lNum    = new JLabel("—"); lNum.setFont(F_NUMERO);     lNum.setForeground(BRANCO);       lNum.setAlignmentX(CENTER_ALIGNMENT);
        JLabel lStatus = new JLabel(" "); lStatus.setFont(F_SMALL);   lStatus.setForeground(CINZA_TEXTO); lStatus.setAlignmentX(CENTER_ALIGNMENT);
        card.add(lNome); card.add(Box.createVerticalStrut(8)); card.add(lNum); card.add(Box.createVerticalStrut(4)); card.add(lStatus);
        card.putClientProperty("lNome", lNome); card.putClientProperty("lNum", lNum); card.putClientProperty("lStatus", lStatus);
        return card;
    }

    private void atualizarCardComp(JPanel card, String nome, int total, boolean maior) {
        ((JLabel) card.getClientProperty("lNome")).setText(nome);
        JLabel lNum = (JLabel) card.getClientProperty("lNum");
        lNum.setText(String.format("%,d", total));
        lNum.setForeground(maior ? VERMELHO : AZUL_DEST);
        JLabel lSt = (JLabel) card.getClientProperty("lStatus");
        lSt.setText(maior ? "⚠  Maior número de casos" : "Menor número de casos");
        lSt.setForeground(maior ? VERMELHO : CINZA_TEXTO);
    }

    private void telaRelatorioGeral() {
        lblBreadcrumb.setText("Relatório Geral");
        painelConteudo.removeAll();
        JPanel painel = criarPainelTela("◈  Relatório Geral por Período", CIANO, "Panorama de todos os municípios filtrado por intervalo de datas.");

        JPanel filtros = new JPanel(new GridLayout(1, 2, 16, 0));
        filtros.setOpaque(false);
        filtros.setAlignmentX(LEFT_ALIGNMENT);
        filtros.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));

        JTextField campIni = criarField("dd/MM/yyyy");
        JTextField campFim = criarField("dd/MM/yyyy");

        JPanel cIni = new JPanel(); cIni.setLayout(new BoxLayout(cIni, BoxLayout.Y_AXIS)); cIni.setOpaque(false);
        JPanel cFim = new JPanel(); cFim.setLayout(new BoxLayout(cFim, BoxLayout.Y_AXIS)); cFim.setOpaque(false);
        JLabel rIni = new JLabel("DATA INICIAL"); rIni.setFont(F_MICRO); rIni.setForeground(new Color(100, 120, 170));
        JLabel rFim = new JLabel("DATA FINAL");   rFim.setFont(F_MICRO); rFim.setForeground(new Color(100, 120, 170));
        cIni.add(rIni); cIni.add(Box.createVerticalStrut(4)); cIni.add(campIni);
        cFim.add(rFim); cFim.add(Box.createVerticalStrut(4)); cFim.add(campFim);
        filtros.add(cIni); filtros.add(cFim);
        painel.add(filtros);
        painel.add(Box.createVerticalStrut(12));

        String[] cols = {"Município", "Casos", "% Infectada", "Óbitos", "% Falecida"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        JTable tabela = criarTabela(modelo);
        JScrollPane scrollTab = estilizarScroll(new JScrollPane(tabela));
        scrollTab.setAlignmentX(LEFT_ALIGNMENT);
        scrollTab.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        JPanel botoes = criarBarraBotoes();
        JButton btnVoltar = criarBotao("← Voltar",       AZUL_BORDA, false);
        JButton btnGerar  = criarBotao("Gerar Relatório", CIANO,      true);
        btnVoltar.addActionListener(e -> mostrarMenu());
        btnGerar.addActionListener(e -> {
            try {
                LocalDate ini = LocalDate.parse(lerField(campIni), FMT);
                LocalDate fim = LocalDate.parse(lerField(campFim),  FMT);
                if (ini.isAfter(fim)) { mostrarErro("A data inicial deve ser anterior à data final."); return; }
                modelo.setRowCount(0);
                for (Cidade cidade : cidadeDAO.listar()) {
                    List<Coleta> col = coletaDAO.listarPorCidade(cidade.getId());
                    int tc = col.stream().filter(x -> !x.getDataColeta().isBefore(ini) && !x.getDataColeta().isAfter(fim)).mapToInt(Coleta::getCasos).sum();
                    int to = col.stream().filter(x -> !x.getDataColeta().isBefore(ini) && !x.getDataColeta().isAfter(fim)).mapToInt(Coleta::getObitos).sum();
                    long pop = cidade.getPopulacao();
                    double pc = pop > 0 ? (double) tc / pop * 100 : 0;
                    double po = pop > 0 ? (double) to / pop * 100 : 0;
                    modelo.addRow(new Object[]{ cidade.getNome(), String.format("%,d", tc), String.format("%.6f%%", pc), String.format("%,d", to), String.format("%.6f%%", po) });
                }
            } catch (DateTimeParseException ex) { mostrarErro("Data inválida. Use o formato dd/MM/yyyy.");
            } catch (Exception               ex) { mostrarErro("Erro: " + ex.getMessage()); }
        });

        botoes.add(btnVoltar); botoes.add(Box.createHorizontalStrut(8)); botoes.add(btnGerar);
        painel.add(botoes);
        painel.add(Box.createVerticalStrut(14));
        painel.add(scrollTab);
        embutirTela(painel);
    }

    private JPanel criarPainelTela(String titulo, Color acento, String sub) {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AZUL_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(acento);
                g2.fillRoundRect(0, 0, 5, getHeight(), 4, 4);
                g2.fillRect(3, 0, 2, getHeight());
                g2.dispose();
            }
        };
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(22, 28, 22, 28));
        JLabel lT = new JLabel(titulo); lT.setFont(F_TITULO); lT.setForeground(BRANCO); lT.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lS = new JLabel(sub);    lS.setFont(F_SMALL);  lS.setForeground(CINZA_TEXTO); lS.setAlignmentX(LEFT_ALIGNMENT);
        JSeparator sep = new JSeparator() {
            @Override protected void paintComponent(Graphics g) { g.setColor(SEPARADOR); g.fillRect(0,0,getWidth(),1); }
        };
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lT); p.add(Box.createVerticalStrut(3)); p.add(lS);
        p.add(Box.createVerticalStrut(14)); p.add(sep); p.add(Box.createVerticalStrut(16));
        return p;
    }

    private void adicionarCampo(JPanel painel, String rotulo, JComponent campo) {
        JLabel lbl = new JLabel(rotulo);
        lbl.setFont(F_MICRO); lbl.setForeground(new Color(100, 120, 170));
        lbl.setAlignmentX(LEFT_ALIGNMENT); lbl.setBorder(new EmptyBorder(6, 0, 3, 0));
        campo.setFont(F_NORMAL);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        campo.setAlignmentX(LEFT_ALIGNMENT);
        if (campo instanceof JTextField tf) {
            tf.setBackground(INPUT_BG); tf.setForeground(BRANCO); tf.setCaretColor(LARANJA);
            tf.setBorder(BorderFactory.createCompoundBorder(new LineBorder(AZUL_BORDA, 1, true), new EmptyBorder(6, 14, 6, 10)));
        }
        painel.add(lbl); painel.add(campo); painel.add(Box.createVerticalStrut(8));
    }

    private JTextField criarField(String ph) {
        JTextField f = new JTextField();
        f.setBackground(INPUT_BG); f.setForeground(!ph.isEmpty() ? CINZA_TEXTO : BRANCO);
        f.setCaretColor(LARANJA); f.setFont(F_NORMAL);
        f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(AZUL_BORDA, 1, true), new EmptyBorder(6, 14, 6, 10)));
        if (!ph.isEmpty()) {
            f.setText(ph);
            f.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { if (f.getText().equals(ph)) { f.setText(""); f.setForeground(BRANCO); } }
                public void focusLost (FocusEvent e) { if (f.getText().isEmpty()) { f.setText(ph); f.setForeground(CINZA_TEXTO); } }
            });
        }
        return f;
    }

    private String lerField(JTextField f) { return f.getForeground().equals(CINZA_TEXTO) ? "" : f.getText().trim(); }

    private void estilizarCombo(JComboBox<String> cb) {
        cb.setBackground(INPUT_BG); cb.setForeground(BRANCO); cb.setFont(F_NORMAL);
        cb.setBorder(new LineBorder(AZUL_BORDA, 1, true));
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cb.setAlignmentX(LEFT_ALIGNMENT);
    }

    private void estilizarSpinner(JSpinner sp) {
        sp.setBackground(INPUT_BG); sp.setForeground(BRANCO);
        sp.setBorder(new LineBorder(AZUL_BORDA, 1, true));
        JFormattedTextField tf = ((JSpinner.DefaultEditor) sp.getEditor()).getTextField();
        tf.setBackground(INPUT_BG); tf.setForeground(BRANCO); tf.setCaretColor(LARANJA);
    }

    private JTable criarTabela(DefaultTableModel modelo) {
        JTable t = new JTable(modelo);
        t.setFont(F_NORMAL); t.setRowHeight(30); t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setBackground(TABELA_PAR); t.setForeground(BRANCO);
        t.setSelectionBackground(new Color(40, 80, 160)); t.setSelectionForeground(BRANCO);
        t.setFillsViewportHeight(true);
        final Color HDR_BG = new Color(22, 38, 80);
        JTableHeader h = t.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 11));
        h.setBackground(HDR_BG); h.setForeground(BRANCO);
        h.setBorder(new MatteBorder(0, 0, 1, 0, AZUL_BORDA));
        h.setReorderingAllowed(false);
        h.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tb, Object v, boolean sel, boolean foc, int row, int col) {
                JLabel lbl = new JLabel(v == null ? "" : v.toString());
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
                lbl.setForeground(BRANCO); lbl.setBackground(HDR_BG); lbl.setOpaque(true);
                lbl.setBorder(new EmptyBorder(0, 12, 0, 12));
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
                return lbl;
            }
        });
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tb, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tb, v, sel, foc, row, col);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                if (!sel) { setBackground(row % 2 == 0 ? TABELA_PAR : TABELA_IMPAR); setForeground(BRANCO); }
                return this;
            }
        });
        return t;
    }

    private JScrollPane estilizarScroll(JScrollPane sp) {
        sp.setBorder(new LineBorder(AZUL_BORDA, 1, true));
        sp.getViewport().setBackground(TABELA_PAR);
        return sp;
    }

    private JButton criarBotao(String texto, Color cor, boolean primario) {
        boolean[] hov = {false};
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (primario) {
                    g2.setColor(hov[0] ? cor.darker() : cor);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(Color.WHITE);
                } else {
                    g2.setColor(hov[0] ? new Color(cor.getRed(), cor.getGreen(), cor.getBlue(), 50) : new Color(0,0,0,0));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(hov[0] ? BRANCO : CINZA_TEXTO);
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                }
                FontMetrics fm = g2.getFontMetrics(getFont());
                int x = (getWidth()  - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.setFont(getFont()); g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false); btn.setOpaque(false);
        btn.setFont(F_BOTAO); btn.setBorder(new EmptyBorder(10, 22, 10, 22));
        btn.setFocusPainted(false); btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hov[0]=true;  btn.repaint(); }
            public void mouseExited (MouseEvent e) { hov[0]=false; btn.repaint(); }
        });
        return btn;
    }

    private JPanel criarBarraBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        p.setOpaque(false); p.setAlignmentX(LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        return p;
    }

    private JPanel criarMiniCard(String titulo, Color cor) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(cor.getRed(), cor.getGreen(), cor.getBlue(), 18));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(cor.getRed(), cor.getGreen(), cor.getBlue(), 70));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.setColor(cor);
                g2.fillRoundRect(0, 0, getWidth(), 3, 3, 3);
                g2.fillRect(0, 1, getWidth(), 2);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(10, 14, 10, 14));
        JLabel lT = new JLabel(titulo); lT.setFont(F_MICRO); lT.setForeground(new Color(cor.getRed(), cor.getGreen(), cor.getBlue(), 200)); lT.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lN = new JLabel("—"); lN.setFont(new Font("Segoe UI", Font.BOLD, 20)); lN.setForeground(cor); lN.setAlignmentX(LEFT_ALIGNMENT);
        card.add(lT); card.add(Box.createVerticalStrut(5)); card.add(lN);
        card.putClientProperty("num", lN);
        return card;
    }

    private JPanel criarSepH() {
        JPanel sep = new JPanel() {
            @Override protected void paintComponent(Graphics g) { g.setColor(SEPARADOR); g.fillRect(12, 0, getWidth()-24, 1); }
            @Override public Dimension getPreferredSize() { return new Dimension(0, 1); }
            @Override public Dimension getMaximumSize()   { return new Dimension(Integer.MAX_VALUE, 1); }
        };
        sep.setOpaque(false);
        return sep;
    }

    private void embutirTela(JPanel painel) {
        JScrollPane scroll = new JScrollPane(painel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setBackground(AZUL_FUNDO);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        painelConteudo.add(scroll, BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    private void mostrarErro(String msg)    { JOptionPane.showMessageDialog(this, msg, "Erro",    JOptionPane.ERROR_MESSAGE); }
    private void mostrarSucesso(String msg) { JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE); }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}